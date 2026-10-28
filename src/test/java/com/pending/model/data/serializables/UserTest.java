package com.pending.model.data.serializables;

import com.pending.model.enums.ContactPreference;
import com.pending.model.enums.ReliefType;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private static Location sampleLocation() {
        return new Location("SC", "Charleston", "1 Main St", "29401");
    }

    private static Contact sampleContact(String email) {
        return new Contact(email, "555-0100", ContactPreference.EMAIL);
    }

    @Test
    void defaultConstructorHasEmptyFieldsAndNonNullContacts() {
        User user = new User();

        assertEquals("", user.getFirstName());
        assertEquals("", user.getPassword());
        assertNotNull(user.getLocation());
        assertNotNull(user.getUserContact());
        assertNotNull(user.getEmergencyContact());
        assertTrue(user.getUserReliefRequests().isEmpty());
    }

    @Test
    void eightArgConstructorSetsFieldsAndUUIDReferences() {
        Location location = sampleLocation();
        Contact userContact = sampleContact("jane@example.com");
        Contact emergencyContact = sampleContact("emergency@example.com");

        User user = new User("Jane", "Doe", LocalDate.of(1990, 1, 1),
                new ArrayList<>(List.of(ReliefType.FOOD)), "password",
                location, userContact, emergencyContact);

        assertEquals("Jane", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("password", user.getPassword());
        assertSame(location, user.getLocation());
        assertSame(userContact, user.getUserContact());
        assertSame(emergencyContact, user.getEmergencyContact());
        assertEquals(location.getId(), user.getLocationUUIDReference());
        assertEquals(userContact.getId(), user.getUserContactUUIDReference());
        assertEquals(emergencyContact.getId(), user.getEmergencyContactUUIDReference());
    }

    @Test
    void idConstructorUsesProvidedId() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "Jane", "Doe", LocalDate.of(1990, 1, 1), new ArrayList<>(),
                "password", sampleLocation(), sampleContact("jane@example.com"), sampleContact("em@example.com"));

        assertEquals(id, user.getId());
    }

    @Test
    void constructorWithNullEmergencyContactLeavesItsReferenceNull() {
        User user = new User("Jane", "Doe", LocalDate.of(1990, 1, 1), new ArrayList<>(), "password",
                sampleLocation(), sampleContact("jane@example.com"), null);

        assertNull(user.getEmergencyContact());
        assertNull(user.getEmergencyContactUUIDReference());
    }

    /**
     * BUG: unlike emergencyContact (which has a null check), the constructor
     * does `userContactUUIDReference = userContact.getId();` with no null
     * check, so a null userContact throws instead of leaving the reference
     * null the way a null emergencyContact does.
     */
    @Test
    void constructorWithNullUserContactThrowsUnlikeNullEmergencyContact() {
        assertThrows(NullPointerException.class, () ->
                new User("Jane", "Doe", LocalDate.of(1990, 1, 1), new ArrayList<>(), "password",
                        sampleLocation(), null, sampleContact("em@example.com")));
    }

    @Test
    void settersUpdateFields() {
        User user = new User();
        Location location = sampleLocation();
        Contact userContact = sampleContact("new@example.com");
        Contact emergencyContact = sampleContact("em@example.com");

        user.setPassword("newpass");
        user.setLocation(location);
        user.setUserContact(userContact);
        user.setEmergencyContact(emergencyContact);

        assertEquals("newpass", user.getPassword());
        assertSame(location, user.getLocation());
        assertSame(userContact, user.getUserContact());
        assertSame(emergencyContact, user.getEmergencyContact());
    }

    @Test
    void uuidReferenceSetters() {
        User user = new User();
        UUID locationRef = UUID.randomUUID();
        UUID userContactRef = UUID.randomUUID();
        UUID emergencyContactRef = UUID.randomUUID();

        user.setLocationUUIDReference(locationRef);
        user.setUserContactUUIDReference(userContactRef);
        user.setEmergencyContactUUIDReference(emergencyContactRef);

        assertEquals(locationRef, user.getLocationUUIDReference());
        assertEquals(userContactRef, user.getUserContactUUIDReference());
        assertEquals(emergencyContactRef, user.getEmergencyContactUUIDReference());
    }

    @Test
    void getUserContactReturnsNullAndPrintsAWarningWhenSetToNull() {
        User user = new User();
        user.setUserContact(null);

        ByteArrayOutputStream capturedOut = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(capturedOut));
        Contact result;
        try {
            result = user.getUserContact();
        } finally {
            System.setOut(originalOut);
        }

        assertNull(result);
        assertTrue(capturedOut.toString().contains("has null contact"));
    }

    @Test
    void addUserReliefRequestWithObjectAddsToBothLists() {
        User user = new User();
        ReliefRequest request = new ReliefRequest();

        boolean result = user.addUserReliefRequest(request);

        assertTrue(result);
        assertTrue(user.getUserReliefRequests().contains(request));
        assertTrue(user.getUserReliefRequestsUUIDReference().contains(request.getId()));
    }

    @Test
    void addUserReliefRequestWithUUIDOnlyAddsToReferenceList() {
        User user = new User();
        UUID id = UUID.randomUUID();

        boolean result = user.addUserReliefRequest(id);

        assertTrue(result);
        assertTrue(user.getUserReliefRequestsUUIDReference().contains(id));
        assertTrue(user.getUserReliefRequests().isEmpty());
    }

    @Test
    void getFullNameJoinsFirstAndLastName() {
        User user = new User();
        user.setFirstName("Jane");
        user.setLastName("Doe");

        assertEquals("Jane Doe", user.getFullName());
    }

    @Test
    void toStringDoesNotThrowWithoutReliefRequests() {
        User user = new User();
        user.setFirstName("Jane");
        user.setLastName("Doe");

        assertTrue(user.toString().contains("Jane Doe"));
    }

    @Test
    void toStringDoesNotThrowWithReliefRequests() {
        User user = new User("Jane", "Doe", LocalDate.of(1990, 1, 1), new ArrayList<>(), "password",
                sampleLocation(), sampleContact("jane@example.com"), sampleContact("em@example.com"));
        user.addUserReliefRequest(new ReliefRequest());

        String result = user.toString();

        assertTrue(result.contains("Jane Doe"));
        assertTrue(result.contains("Relief Requests"));
    }

    @Test
    void toStringLivingBeingDelegatesToLivingBeingToString() {
        // LivingBeing.toString() throws for an empty relief-needs list (see
        // LivingBeingTest), so this user needs at least one relief need.
        User user = new User("Jane", "Doe", LocalDate.of(1990, 1, 1),
                new ArrayList<>(List.of(ReliefType.FOOD)), "password",
                sampleLocation(), sampleContact("jane@example.com"), sampleContact("em@example.com"));

        String result = user.toStringLivingBeing();

        assertTrue(result.contains("Jane Doe"));
        assertTrue(result.contains("HUMAN"));
        assertFalse(result.contains("Relief Requests"),
                "toStringLivingBeing should use LivingBeing's format, not User's");
    }
}