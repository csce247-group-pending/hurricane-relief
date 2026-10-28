package com.pending.model.data.serializables;

import com.pending.model.enums.LivingBeingType;
import com.pending.model.enums.ReliefStatus;
import com.pending.model.enums.ReliefType;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ReliefRequestTest {

    private static LivingBeing human(String name, ReliefType... needs) {
        return new LivingBeing(name, "Test", LocalDate.of(1990, 1, 1),
                LivingBeingType.HUMAN, new ArrayList<>(List.of(needs)));
    }

    private static LivingBeing pet(String name, ReliefType... needs) {
        return new LivingBeing(name, "Test", LocalDate.of(2020, 1, 1),
                LivingBeingType.PET, new ArrayList<>(List.of(needs)));
    }

    @Test
    void defaultConstructorIsEmptyAndRequested() {
        ReliefRequest request = new ReliefRequest();

        assertEquals("", request.getDescription());
        assertEquals(ReliefStatus.REQUESTED, request.getStatus());
        assertTrue(request.getLivingBeings().isEmpty());
        assertTrue(request.getTypes().isEmpty());
        assertNull(request.getAssignedTo());
        assertEquals(0, request.getPartySize());
        assertFalse(request.hasPets());
    }

    @Test
    void sevenArgConstructorSetsFields() {
        Location location = new Location("SC", "Charleston", "1 Main St", "29401");
        Contact contact = new Contact("a@example.com", "555-0100", com.pending.model.enums.ContactPreference.EMAIL);
        ArrayList<LivingBeing> beings = new ArrayList<>(List.of(human("Jane")));
        ArrayList<ReliefType> types = new ArrayList<>(List.of(ReliefType.FOOD));

        ReliefRequest request = new ReliefRequest("need food", location,
                ReliefStatus.REQUESTED, beings, types, contact);

        assertEquals("need food", request.getDescription());
        assertSame(location, request.getLocation());
        assertSame(contact, request.getContactInfo());
        assertEquals(1, request.getLivingBeings().size());
        assertEquals(1, request.getTypes().size());
    }

    @Test
    void idConstructorUsesProvidedId() {
        UUID id = UUID.randomUUID();
        ReliefRequest request = new ReliefRequest(id, "desc", new Location(), ReliefStatus.REQUESTED,
                new ArrayList<>(), new ArrayList<>(), new Contact());

        assertEquals(id, request.getId());
    }

    @Test
    void hasPetsIsTrueOnlyWhenAPetIsPresent() {
        ReliefRequest withoutPets = new ReliefRequest("", new Location(), ReliefStatus.REQUESTED,
                new ArrayList<>(List.of(human("A"))), new ArrayList<>(), new Contact());
        ReliefRequest withPets = new ReliefRequest("", new Location(), ReliefStatus.REQUESTED,
                new ArrayList<>(List.of(human("A"), pet("B"))), new ArrayList<>(), new Contact());

        assertFalse(withoutPets.hasPets());
        assertTrue(withPets.hasPets());
    }

    @Test
    void getPartySizeAndPartySizeHumansCountCorrectly() {
        ArrayList<LivingBeing> beings = new ArrayList<>(List.of(human("A"), human("B"), pet("C")));
        ReliefRequest request = new ReliefRequest("", new Location(), ReliefStatus.REQUESTED,
                beings, new ArrayList<>(), new Contact());

        assertEquals(3, request.getPartySize());
        assertEquals(2, request.getPartySizeHumans());
    }

    @Test
    void addLivingBeingAppendsToTheList() {
        ReliefRequest request = new ReliefRequest();

        assertTrue(request.addLivingBeing(human("Jane")));

        assertEquals(1, request.getPartySize());
    }

    @Test
    void settersUpdateFields() {
        ReliefRequest request = new ReliefRequest();
        Location location = new Location("SC", "Charleston", "1 Main St", "29401");
        Contact contact = new Contact();
        ArrayList<ReliefType> types = new ArrayList<>(List.of(ReliefType.WATER));

        request.setDescription("updated");
        request.setStatus(ReliefStatus.ASSIGNED);
        request.setLocation(location);
        request.setContactInfo(contact);
        request.setTypes(types);

        assertEquals("updated", request.getDescription());
        assertEquals(ReliefStatus.ASSIGNED, request.getStatus());
        assertSame(location, request.getLocation());
        assertSame(contact, request.getContactInfo());
        assertSame(types, request.getTypes());
    }

    @Test
    void uuidReferenceGettersAndSetters() {
        ReliefRequest request = new ReliefRequest();
        UUID locationRef = UUID.randomUUID();
        UUID contactRef = UUID.randomUUID();
        UUID assignedRef = UUID.randomUUID();

        request.setLocationUUIDReference(locationRef);
        request.setContactUUIDReference(contactRef);
        request.setAssignedToUUIDReference(assignedRef);

        assertEquals(locationRef, request.getLocationUUIDReference());
        assertEquals(contactRef, request.getContactUUIDReference());
        assertEquals(assignedRef, request.getAssignedToUUIDReference());
    }

    @Test
    void toStringDoesNotThrowWithEmptyTypesAndShowsNA() {
        ReliefRequest request = new ReliefRequest();

        assertTrue(request.toString().contains("N/A"));
    }

    @Test
    void toStringDetailedShowsNoneWhenUnassignedAndEmpty() {
        ReliefRequest request = new ReliefRequest();

        String result = request.toStringDetailed();

        assertTrue(result.contains("Assigned To: None"));
        assertTrue(result.contains("Members(0): None"));
    }

    @Test
    void toStringDetailedListsEachLivingBeing() {
        // Each LivingBeing here needs at least one relief need, since
        // LivingBeing.toString() currently throws for an empty needs list
        // (see LivingBeingTest.toStringThrowsWhenReliefNeedsIsEmpty).
        ArrayList<LivingBeing> beings = new ArrayList<>(List.of(
                human("Jane", ReliefType.FOOD), pet("Rex", ReliefType.PET_SHELTER)));
        ReliefRequest request = new ReliefRequest("", new Location(), ReliefStatus.REQUESTED,
                beings, new ArrayList<>(), new Contact());

        String result = request.toStringDetailed();

        assertTrue(result.contains("Jane"));
        assertTrue(result.contains("Rex"));
    }

    /**
     * BUG: the 6-arg ReliefRequest constructor assigns
     *     locationUUIDReference = UUID.randomUUID();
     *     contactUUIDReference = UUID.randomUUID();
     * instead of location.getId() / contactInfo.getId() (the way User does
     * it for its own location/contact references). This means a freshly
     * created request's UUID references never actually point at the
     * location or contact it was built with.
     */
    @Test
    void locationAndContactUUIDReferencesDoNotMatchTheRealObjects() {
        Location location = new Location("SC", "Charleston", "1 Main St", "29401");
        Contact contact = new Contact("a@example.com", "555-0100", com.pending.model.enums.ContactPreference.EMAIL);

        ReliefRequest request = new ReliefRequest("desc", location, ReliefStatus.REQUESTED,
                new ArrayList<>(), new ArrayList<>(), contact);

        assertNotEquals(location.getId(), request.getLocationUUIDReference(),
                "if this now passes, the UUID-reference bug has been fixed and this "
                        + "assertion should be flipped to assertEquals");
        assertNotEquals(contact.getId(), request.getContactUUIDReference(),
                "if this now passes, the UUID-reference bug has been fixed and this "
                        + "assertion should be flipped to assertEquals");
    }

    /**
     * BUG: SerializeJSONReliefRequest.serialize() writes "location" and
     * "contact" as full nested JSON objects (location.serialize(),
     * contactInfo.serialize()), but deserialize() reads them back expecting
     * UUID strings:
     *     serializable.setLocationUUIDReference(UUID.fromString((String) jsonObject.get("location")));
     * Casting a JSONObject to String throws ClassCastException, so a
     * request's own serialize() output cannot be fed back into deserialize().
     */
    @Test
    void serializeThenDeserializeThrowsBecauseLocationAndContactAreWrongShape() {
        Location location = new Location("SC", "Charleston", "1 Main St", "29401");
        Contact contact = new Contact("a@example.com", "555-0100", com.pending.model.enums.ContactPreference.EMAIL);
        ReliefRequest original = new ReliefRequest("desc", location, ReliefStatus.REQUESTED,
                new ArrayList<>(List.of(human("Jane", ReliefType.FOOD))),
                new ArrayList<>(List.of(ReliefType.FOOD)), contact);

        JSONObject json = (JSONObject) original.serialize();

        ReliefRequest restored = new ReliefRequest();

        assertThrows(ClassCastException.class, () -> restored.deserialize(json),
                "if this now passes without throwing, the serialize/deserialize shape "
                        + "mismatch has been fixed and this test should be replaced with "
                        + "a real round-trip assertion");
    }
}