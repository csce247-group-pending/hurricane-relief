package com.pending.model.data.serializables;

import com.pending.model.enums.ContactPreference;
import com.pending.model.enums.ReliefStatus;
import com.pending.model.enums.ReliefType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ShelterManagerTest {

    private static Volunteer sampleVolunteer() {
        return new Volunteer(UUID.randomUUID(), "Jane", "Doe", LocalDate.of(1990, 1, 1),
                new ArrayList<>(), "password", new Location("SC", "Charleston", "1 Main St", "29401"),
                new Contact("jane@example.com", "555-0100", ContactPreference.EMAIL),
                new Contact("em@example.com", "555-0101", ContactPreference.CALL),
                true, new ArrayList<>(List.of(ReliefType.MEDICAL_CARE)), null);
    }

    @Test
    void defaultConstructorCreatesAnAvailableManagerWithNoShelter() {
        ShelterManager manager = new ShelterManager();

        assertTrue(manager.isAvailable());
        assertNull(manager.getShelter());
        assertNotNull(manager.getId());
    }

    @Test
    void volunteerConstructorCopiesIdAndFields() {
        Volunteer volunteer = sampleVolunteer();

        ShelterManager manager = new ShelterManager(volunteer);

        assertEquals(volunteer.getId(), manager.getId());
        assertEquals(volunteer.getFirstName(), manager.getFirstName());
        assertEquals(volunteer.getLastName(), manager.getLastName());
        assertEquals(volunteer.getPassword(), manager.getPassword());
        assertEquals(volunteer.isAvailable(), manager.isAvailable());
        assertTrue(manager.getReliefTypes().contains(ReliefType.MEDICAL_CARE));
    }

    @Test
    void fullConstructorSetsFields() {
        ShelterManager manager = new ShelterManager(UUID.randomUUID(), "Jane", "Doe",
                LocalDate.of(1990, 1, 1), "password",
                new Location("SC", "Charleston", "1 Main St", "29401"),
                new Contact("jane@example.com", "555-0100", ContactPreference.EMAIL),
                new Contact("em@example.com", "555-0101", ContactPreference.CALL),
                true, new ArrayList<>(List.of(ReliefType.TRANSPORTATION)), null);

        assertEquals("Jane", manager.getFirstName());
        assertTrue(manager.isAvailable());
        assertTrue(manager.getReliefTypes().contains(ReliefType.TRANSPORTATION));
    }

    @Test
    void shelterGetterAndSetter() {
        ShelterManager manager = new ShelterManager();
        Shelter shelter = new Shelter();

        manager.setShelter(shelter);

        assertSame(shelter, manager.getShelter());
    }

    @Test
    void shelterUUIDReferenceGetterAndSetter() {
        ShelterManager manager = new ShelterManager();
        UUID shelterId = UUID.randomUUID();

        manager.setShelterUUIDReference(shelterId);

        assertEquals(shelterId, manager.getShelterUUIDReference());
    }

    @Test
    void closeReliefRequestMarksCompletedAndFreesTheAssignedVolunteer() {
        ShelterManager manager = new ShelterManager();
        Volunteer assigned = sampleVolunteer();
        assigned.setAvailable(false);

        ReliefRequest request = new ReliefRequest();
        request.setStatus(ReliefStatus.ASSIGNED);
        request.setAssignedTo(assigned);
        assigned.setTask(request);

        boolean result = manager.closeReliefRequest(request);

        assertTrue(result);
        assertEquals(ReliefStatus.COMPLETED, request.getStatus());
        assertTrue(assigned.isAvailable());
        assertNull(assigned.getTask());
    }

    @Test
    void closeReliefRequestThrowsWhenNoOneIsAssigned() {
        // closeReliefRequest() calls reliefRequest.getAssignedTo().setAvailable(true)
        // without a null check, so an unassigned request causes a NullPointerException.
        ShelterManager manager = new ShelterManager();
        ReliefRequest request = new ReliefRequest(); // assignedTo is null by default

        assertThrows(NullPointerException.class, () -> manager.closeReliefRequest(request));
    }
}