package com.pending.model.data.serializables.behaviors;

import com.pending.model.data.DataList;
import com.pending.model.data.Database;
import com.pending.model.data.serializables.Contact;
import com.pending.model.data.serializables.LivingBeing;
import com.pending.model.data.serializables.Location;
import com.pending.model.data.serializables.ReliefRequest;
import com.pending.model.data.serializables.Serializable;
import com.pending.model.data.serializables.Volunteer;
import com.pending.model.enums.ContactPreference;
import com.pending.model.enums.LivingBeingType;
import com.pending.model.enums.ReliefStatus;
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

class SerializeJSONReliefRequestTest {

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

    private Contact registeredContact() {
        Contact contact = new Contact("a@example.com", "555-0100", ContactPreference.EMAIL);
        addToDatabase(Contact.class, contact);
        return contact;
    }

    private LivingBeing registeredLivingBeing() {
        LivingBeing being = new LivingBeing("Jane", "Doe", LocalDate.of(1990, 1, 1),
                LivingBeingType.HUMAN, new ArrayList<>(List.of(ReliefType.FOOD)));
        addToDatabase(LivingBeing.class, being);
        return being;
    }

    private Volunteer registeredVolunteer() {
        Volunteer volunteer = new Volunteer();
        addToDatabase(Volunteer.class, volunteer);
        return volunteer;
    }

    private static ReliefRequest sampleRequest(Location location, Contact contact, LivingBeing being) {
        return new ReliefRequest("need help", location, ReliefStatus.ASSIGNED,
                new ArrayList<>(List.of(being)), new ArrayList<>(List.of(ReliefType.FOOD)), contact);
    }

    @Test
    void serializeWritesLocationAndContactAsUUIDStrings() {
        Location location = new Location("SC", "Charleston", "1 Main St", "29401");
        Contact contact = new Contact("a@example.com", "555-0100", ContactPreference.EMAIL);
        ReliefRequest request = sampleRequest(location, contact, new LivingBeing());

        JSONObject json = new SerializeJSONReliefRequest(request).serialize();

        assertEquals(location.getId().toString(), json.get("location"));
        assertEquals(contact.getId().toString(), json.get("contact"));
    }

    @Test
    void serializeOmitsAssignedToWhenThereIsNoVolunteer() {
        ReliefRequest request = new ReliefRequest();

        JSONObject json = new SerializeJSONReliefRequest(request).serialize();

        assertFalse(json.containsKey("assigned_to"));
    }

    @Test
    void serializeIncludesAssignedToWhenAVolunteerIsSet() {
        ReliefRequest request = new ReliefRequest();
        Volunteer volunteer = new Volunteer();
        request.setAssignedTo(volunteer);

        JSONObject json = new SerializeJSONReliefRequest(request).serialize();

        assertEquals(volunteer.getId().toString(), json.get("assigned_to"));
    }

    @Test
    void serializeUsesLivingBeingObjectsWhenPresent() {
        LivingBeing being = new LivingBeing();
        ReliefRequest request = new ReliefRequest("", new Location(), ReliefStatus.REQUESTED,
                new ArrayList<>(List.of(being)), new ArrayList<>(), new Contact());

        JSONObject json = new SerializeJSONReliefRequest(request).serialize();

        JSONArray livingBeings = (JSONArray) json.get("living_beings");
        assertEquals(1, livingBeings.size());
        assertEquals(being.getId().toString(), livingBeings.get(0));
    }

    @Test
    void serializeFallsBackToUUIDReferencesWhenLivingBeingsListIsEmpty() {
        ReliefRequest request = new ReliefRequest();
        UUID refId = UUID.randomUUID();
        request.getLivingBeingsUUIDReference().add(refId);

        JSONObject json = new SerializeJSONReliefRequest(request).serialize();

        JSONArray livingBeings = (JSONArray) json.get("living_beings");
        assertEquals(1, livingBeings.size());
        assertEquals(refId.toString(), livingBeings.get(0));
    }

    @Test
    void deserializeRestoresAllFields() {
        UUID id = UUID.randomUUID();
        UUID locationId = UUID.randomUUID();
        UUID contactId = UUID.randomUUID();
        UUID beingId = UUID.randomUUID();
        UUID assignedId = UUID.randomUUID();

        JSONObject json = new JSONObject();
        json.put("id", id.toString());
        json.put("status", "ASSIGNED");
        json.put("description", "need help");
        json.put("location", locationId.toString());
        json.put("contact", contactId.toString());
        JSONArray types = new JSONArray();
        types.add("FOOD");
        json.put("types", types);
        json.put("assigned_to", assignedId.toString());
        JSONArray livingBeings = new JSONArray();
        livingBeings.add(beingId.toString());
        json.put("living_beings", livingBeings);

        ReliefRequest request = new ReliefRequest();
        new SerializeJSONReliefRequest(request).deserialize(json);

        assertEquals(id, request.getId());
        assertEquals(ReliefStatus.ASSIGNED, request.getStatus());
        assertEquals("need help", request.getDescription());
        assertEquals(locationId, request.getLocationUUIDReference());
        assertEquals(contactId, request.getContactUUIDReference());
        assertEquals(1, request.getTypes().size());
        assertEquals(ReliefType.FOOD, request.getTypes().get(0));
        assertEquals(assignedId, request.getAssignedToUUIDReference());
        assertEquals(1, request.getLivingBeingsUUIDReference().size());
        assertEquals(beingId, request.getLivingBeingsUUIDReference().get(0));
    }

    @Test
    void deserializeWithNoAssignedToLeavesItNull() {
        JSONObject json = new JSONObject();
        json.put("id", UUID.randomUUID().toString());
        json.put("status", "REQUESTED");
        json.put("description", "");
        json.put("location", UUID.randomUUID().toString());
        json.put("contact", UUID.randomUUID().toString());
        json.put("types", new JSONArray());
        json.put("living_beings", new JSONArray());
        // "assigned_to" intentionally omitted

        ReliefRequest request = new ReliefRequest();
        new SerializeJSONReliefRequest(request).deserialize(json);

        assertNull(request.getAssignedToUUIDReference());
    }

    @Test
    void deserializeWithMissingLivingBeingsArrayLeavesReferenceListEmpty() {
        JSONObject json = new JSONObject();
        json.put("id", UUID.randomUUID().toString());
        json.put("status", "REQUESTED");
        json.put("description", "");
        json.put("location", UUID.randomUUID().toString());
        json.put("contact", UUID.randomUUID().toString());
        json.put("types", new JSONArray());
        // "living_beings" intentionally omitted

        ReliefRequest request = new ReliefRequest();

        assertDoesNotThrow(() -> new SerializeJSONReliefRequest(request).deserialize(json));
        assertTrue(request.getLivingBeingsUUIDReference().isEmpty());
    }

    /**
     * BUG: unlike "living_beings" (which is null-checked), deserialize() calls
     * UUID.fromString((String) jsonObject.get("location")) with no null check.
     * A request whose location was never set (so both getLocation() and
     * getLocationUUIDReference() are null) serializes "location":null, and
     * deserializing that back throws instead of leaving the reference null.
     */
    @Test
    void deserializeThrowsWhenLocationIsNull() {
        ReliefRequest withNullLocation = new ReliefRequest();
        withNullLocation.setLocation(null);
        withNullLocation.setLocationUUIDReference(null);
        JSONObject json = new SerializeJSONReliefRequest(withNullLocation).serialize();

        ReliefRequest restored = new ReliefRequest();

        assertThrows(NullPointerException.class, () -> new SerializeJSONReliefRequest(restored).deserialize(json),
                "if this now passes without throwing, the null-location bug has been "
                        + "fixed and this test should assert getLocationUUIDReference() is null instead");
    }

    @Test
    void serializeThenDeserializeRoundTripsNormally() {
        Location location = new Location("SC", "Charleston", "1 Main St", "29401");
        Contact contact = new Contact("a@example.com", "555-0100", ContactPreference.EMAIL);
        LivingBeing being = new LivingBeing();
        ReliefRequest original = sampleRequest(location, contact, being);

        JSONObject json = new SerializeJSONReliefRequest(original).serialize();

        ReliefRequest restored = new ReliefRequest();
        new SerializeJSONReliefRequest(restored).deserialize(json);

        assertEquals(original.getId(), restored.getId());
        assertEquals(original.getStatus(), restored.getStatus());
        assertEquals(original.getDescription(), restored.getDescription());
        assertEquals(location.getId(), restored.getLocationUUIDReference());
        assertEquals(contact.getId(), restored.getContactUUIDReference());
        assertEquals(original.getTypes(), restored.getTypes());
        assertEquals(List.of(being.getId()), restored.getLivingBeingsUUIDReference());
    }

    @Test
    void resolveReferencesPopulatesLocationContactLivingBeingsAndAssignedVolunteer() {
        Location location = registeredLocation();
        Contact contact = registeredContact();
        LivingBeing being = registeredLivingBeing();
        Volunteer volunteer = registeredVolunteer();

        ReliefRequest request = new ReliefRequest();
        request.setLocationUUIDReference(location.getId());
        request.setContactUUIDReference(contact.getId());
        request.getLivingBeingsUUIDReference().add(being.getId());
        request.setAssignedToUUIDReference(volunteer.getId());

        new SerializeJSONReliefRequest(request).resolveReferences();

        assertSame(location, request.getLocation());
        assertSame(contact, request.getContactInfo());
        assertEquals(1, request.getLivingBeings().size());
        assertSame(being, request.getLivingBeings().get(0));
        assertSame(volunteer, request.getAssignedTo());
    }

    @Test
    void resolveReferencesLeavesAssignedToNullWhenThereIsNoReference() {
        ReliefRequest request = new ReliefRequest();
        request.setAssignedToUUIDReference(null);

        assertDoesNotThrow(() -> new SerializeJSONReliefRequest(request).resolveReferences());
        assertNull(request.getAssignedTo());
    }

    @Test
    void resolveReferencesSkipsUnknownLivingBeingIds() {
        ReliefRequest request = new ReliefRequest();
        request.getLivingBeingsUUIDReference().add(UUID.randomUUID());

        new SerializeJSONReliefRequest(request).resolveReferences();

        assertTrue(request.getLivingBeings().isEmpty());
    }
}
