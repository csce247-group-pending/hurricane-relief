package com.pending.model.data.serializables;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.pending.model.enums.ContactPreference;
import com.pending.model.enums.ReliefType;

class UserTest {

    private Location sampleLocation() {
        return new Location("SC", "Charleston", "1 Main St",
                "29401");
    }

    private Contact sampleContact(String email) {
        return new Contact(email, "8035550100",
                ContactPreference.EMAIL);
    }

    @Test
    void defaultConstructorCreatesUserWithEmptyFields() {
        User user = new User();

        assertEquals("", user.getFirstName());
        assertEquals("", user.getLastName());
        assertEquals("", user.getPassword());
        assertNotNull(user.getLocation());
        assertNotNull(user.getUserContact());
        assertNotNull(user.getEmergencyContact());
        assertTrue(user.getUserReliefRequests().isEmpty());
        assertNotNull(user.getId());
    }

    @Test
    void constructorStoresProvidedUserDetails() {
        Location location = sampleLocation();
        Contact userContact = sampleContact("jane@example.com");
        Contact emergencyContact = sampleContact(
                "emergency@example.com");

        User user = new User("Jane", "Doe",
                LocalDate.of(1990, 1, 1),
                new ArrayList<>(List.of(ReliefType.FOOD)),
                "password", location, userContact, emergencyContact);

        assertEquals("Jane", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("password", user.getPassword());
        assertSame(location, user.getLocation());
        assertSame(userContact, user.getUserContact());
        assertSame(emergencyContact, user.getEmergencyContact());
        assertEquals(location.getId(),
                user.getLocationUUIDReference());
        assertEquals(userContact.getId(),
                user.getUserContactUUIDReference());
        assertEquals(emergencyContact.getId(),
                user.getEmergencyContactUUIDReference());
    }

    @Test
    void constructorWithIdUsesProvidedId() {
        UUID id = UUID.randomUUID();

        User user = new User(id, "Jane", "Doe",
                LocalDate.of(1990, 1, 1), new ArrayList<>(),
                "password", sampleLocation(),
                sampleContact("jane@example.com"),
                sampleContact("emergency@example.com"));

        assertEquals(id, user.getId());
    }

    @Test
    void constructorAllowsNullEmergencyContact() {
        User user = new User("Jane", "Doe",
                LocalDate.of(1990, 1, 1), new ArrayList<>(),
                "password", sampleLocation(),
                sampleContact("jane@example.com"), null);

        assertNull(user.getEmergencyContact());
        assertNull(user.getEmergencyContactUUIDReference());
    }

    @Test
    void constructorRejectsNullPrimaryContact() {
        assertThrows(NullPointerException.class, () -> new User(
                "Jane", "Doe", LocalDate.of(1990, 1, 1),
                new ArrayList<>(), "password", sampleLocation(), null,
                sampleContact("emergency@example.com")));
    }

    @Test
    void addUserReliefRequestAddsRequestAndUuidReference() {
        User user = new User();
        ReliefRequest request = new ReliefRequest();

        assertTrue(user.addUserReliefRequest(request));
        assertTrue(user.getUserReliefRequests().contains(request));
        assertTrue(user.getUserReliefRequestsUUIDReference()
                .contains(request.getId()));
    }

    @Test
    void addUserReliefRequestByUuidAddsOnlyUuidReference() {
        User user = new User();
        UUID requestId = UUID.randomUUID();

        assertTrue(user.addUserReliefRequest(requestId));
        assertTrue(user.getUserReliefRequestsUUIDReference()
                .contains(requestId));
        assertTrue(user.getUserReliefRequests().isEmpty());
    }

    @Test
    void getFullNameCombinesFirstAndLastName() {
        User user = new User("Jane", "Doe",
                LocalDate.of(1990, 1, 1), new ArrayList<>(),
                "password", sampleLocation(),
                sampleContact("jane@example.com"),
                sampleContact("emergency@example.com"));

        assertEquals("Jane Doe", user.getFullName());
    }

    @Test
    void toStringIncludesUserNameAndContactInformation() {
        User user = new User("Jane", "Doe",
                LocalDate.of(1990, 1, 1), new ArrayList<>(),
                "password", sampleLocation(),
                sampleContact("jane@example.com"),
                sampleContact("emergency@example.com"));

        String result = user.toString();

        assertTrue(result.contains("Jane Doe"));
        assertTrue(result.contains("jane@example.com"));
    }

    @Test
    void toStringListsAssociatedReliefRequests() {
        User user = new User("Jane", "Doe",
                LocalDate.of(1990, 1, 1), new ArrayList<>(),
                "password", sampleLocation(),
                sampleContact("jane@example.com"),
                sampleContact("emergency@example.com"));

        user.addUserReliefRequest(new ReliefRequest());

        assertTrue(user.toString().contains("Relief Requests"));
    }

    @Test
    void toStringLivingBeingUsesLivingBeingFormat() {
        User user = new User("Jane", "Doe",
                LocalDate.of(1990, 1, 1),
                new ArrayList<>(List.of(ReliefType.FOOD)),
                "password", sampleLocation(),
                sampleContact("jane@example.com"),
                sampleContact("emergency@example.com"));

        String result = user.toStringLivingBeing();

        assertTrue(result.contains("Jane Doe"));
        assertTrue(result.contains("HUMAN"));
        assertTrue(result.contains("Relief Needs: Food"));
    }
}