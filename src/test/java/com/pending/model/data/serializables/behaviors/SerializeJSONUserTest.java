package com.pending.model.data.serializables.behaviors;

import com.pending.model.data.DataList;
import com.pending.model.data.Database;
import com.pending.model.data.serializables.Contact;
import com.pending.model.data.serializables.Location;
import com.pending.model.data.serializables.ReliefRequest;
import com.pending.model.data.serializables.Serializable;
import com.pending.model.data.serializables.User;
import com.pending.model.enums.ContactPreference;
import com.pending.model.enums.ReliefType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SerializeJSONUserTest {

    private final ArrayList<Runnable> cleanupTasks = new ArrayList<>();

    @AfterEach
    void cleanUp() {
        cleanupTasks.forEach(Runnable::run);
        cleanupTasks.clear();
    }

    @SuppressWarnings("unchecked")
    private <T extends Serializable> void addToDatabase(Class<T> cls, T object) {
        ((DataList<T>) Database.getInstance().get(cls)).add(object);
        cleanupTasks.add(() -> ((DataList<T>) Database.getInstance().get(cls)).remove(object));
    }

    private Location registeredLocation() {
        Location location = new Location("SC", "Charleston", "1 Main St", "29401");
        addToDatabase(Location.class, location);
        return location;
    }

    private Contact registeredContact(String email) {
        Contact contact = new Contact(email, "555-0100", ContactPreference.EMAIL);
        addToDatabase(Contact.class, contact);
        return contact;
    }

    private ReliefRequest registeredRequest() {
        ReliefRequest request = new ReliefRequest();
        addToDatabase(ReliefRequest.class, request);
        return request;
    }

    private static User sampleUser() {
        return new User("Jane", "Doe", LocalDate.of(1990, 1, 1),
                new ArrayList<>(List.of(ReliefType.FOOD)), "password",
                new Location("SC", "Charleston", "1 Main St", "29401"),
                new Contact("jane@example.com", "555-0100", ContactPreference.EMAIL),
                new Contact("em@example.com", "555-0101", ContactPreference.CALL));
    }

    @Test
    void serializeIncludesLivingBeingFieldsAndUserFields() {
        User user = sampleUser();

        JSONObject json = new SerializeJSONUser<>(user).serialize();

        assertEquals(user.getId().toString(), json.get("id"));
        assertEquals("Jane", json.get("first_name"));
        assertEquals("password", json.get("password"));
        assertEquals(user.getLocation().getId().toString(), json.get("location"));
        assertEquals(user.getUserContact().getId().toString(), json.get("user_contact"));
        assertEquals(user.getEmergencyContact().getId().toString(), json.get("emergency_contact"));
        assertTrue(((JSONArray) json.get("user_relief_requests")).isEmpty());
    }

    /**
     * BUG: serialize() computes emergencyContactId using a null check, but
     * then does `jsonObject.put("emergency_contact", emergencyContactId.toString())`
     * with no null check on the result. A User with no emergency contact
     * (legal per the User constructor, see UserTest) throws NullPointerException
     * here instead of writing a null value, unlike how ReliefRequest/Shelter
     * handle their own optional UUID fields.
     */
    @Test
    void serializeThrowsWhenEmergencyContactIsNull() {
        User user = new User("Jane", "Doe", LocalDate.of(1990, 1, 1), new ArrayList<>(), "password",
                new Location("SC", "Charleston", "1 Main St", "29401"),
                new Contact("jane@example.com", "555-0100", ContactPreference.EMAIL),
                null);

        assertThrows(NullPointerException.class, () -> new SerializeJSONUser<>(user).serialize(),
                "if this now passes without throwing, the null-emergency-contact bug has "
                        + "been fixed and this test should assert a null JSON value instead");
    }

    @Test
    void deserializeRestoresFieldsAndClearsReliefRequestReferences() {
        User user = sampleUser();
        user.getUserReliefRequestsUUIDReference().add(UUID.randomUUID()); // stale

        UUID newRequestId = UUID.randomUUID();
        JSONObject json = new SerializeJSONUser<>(sampleUser()).serialize();
        json.put("id", user.getId().toString());
        json.put("password", "newpass");
        JSONArray requests = new JSONArray();
        requests.add(newRequestId.toString());
        json.put("user_relief_requests", requests);

        new SerializeJSONUser<>(user).deserialize(json);

        assertEquals("newpass", user.getPassword());
        assertEquals(List.of(newRequestId), user.getUserReliefRequestsUUIDReference());
    }

    @Test
    void serializeThenDeserializeRoundTripsNormally() {
        User original = sampleUser();

        JSONObject json = new SerializeJSONUser<>(original).serialize();

        User restored = new User();
        new SerializeJSONUser<>(restored).deserialize(json);

        assertEquals(original.getId(), restored.getId());
        assertEquals(original.getFirstName(), restored.getFirstName());
        assertEquals(original.getPassword(), restored.getPassword());
        assertEquals(original.getLocation().getId(), restored.getLocationUUIDReference());
        assertEquals(original.getUserContact().getId(), restored.getUserContactUUIDReference());
        assertEquals(original.getEmergencyContact().getId(), restored.getEmergencyContactUUIDReference());
    }

    @Test
    void resolveReferencesPopulatesLocationContactsAndReliefRequests() {
        Location location = registeredLocation();
        Contact userContact = registeredContact("a@example.com");
        Contact emergencyContact = registeredContact("b@example.com");
        ReliefRequest request = registeredRequest();

        User user = new User();
        user.setLocationUUIDReference(location.getId());
        user.setUserContactUUIDReference(userContact.getId());
        user.setEmergencyContactUUIDReference(emergencyContact.getId());
        user.getUserReliefRequestsUUIDReference().add(request.getId());

        new SerializeJSONUser<>(user).resolveReferences();

        assertSame(location, user.getLocation());
        assertSame(userContact, user.getUserContact());
        assertSame(emergencyContact, user.getEmergencyContact());
        assertEquals(1, user.getUserReliefRequests().size());
        assertSame(request, user.getUserReliefRequests().get(0));
    }

    /**
     * NOTE: unlike the occupants/locations loops elsewhere, this loop does not
     * null-check the lookup result, so an unknown relief-request id puts a
     * literal null into getUserReliefRequests() instead of being skipped.
     */
    @Test
    void resolveReferencesAddsANullEntryForUnknownReliefRequestIds() {
        User user = new User();
        user.getUserReliefRequestsUUIDReference().add(UUID.randomUUID());

        new SerializeJSONUser<>(user).resolveReferences();

        assertEquals(1, user.getUserReliefRequests().size());
        assertNull(user.getUserReliefRequests().get(0));
    }
}
