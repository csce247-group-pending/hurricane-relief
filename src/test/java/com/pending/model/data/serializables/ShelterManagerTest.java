package com.pending.model.data.serializables;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.pending.model.enums.ContactPreference;
import com.pending.model.enums.ReliefStatus;
import com.pending.model.enums.ReliefType;

class ShelterManagerTest {

    private Volunteer sampleVolunteer() {
        return new Volunteer(UUID.randomUUID(), "Jane", "Doe",
                LocalDate.of(1990, 1, 1), new ArrayList<>(),
                "password", new Location("SC", "Charleston",
                        "1 Main St", "29401"),
                new Contact("jane@example.com", "8035550100",
                        ContactPreference.EMAIL),
                new Contact("emergency@example.com", "8035550101",
                        ContactPreference.CALL),
                true, new ArrayList<>(List.of(
                        ReliefType.MEDICAL_CARE)), null);
    }

    @Test
    void defaultConstructorCreatesAvailableManagerWithoutShelter() {
        ShelterManager manager = new ShelterManager();

        assertTrue(manager.isAvailable());
        assertNull(manager.getShelter());
        assertNotNull(manager.getId());
    }

    @Test
    void volunteerConstructorCopiesVolunteerInformation() {
        Volunteer volunteer = sampleVolunteer();

        ShelterManager manager = new ShelterManager(volunteer);

        assertEquals(volunteer.getId(), manager.getId());
        assertEquals(volunteer.getFirstName(), manager.getFirstName());
        assertEquals(volunteer.getLastName(), manager.getLastName());
        assertEquals(volunteer.getPassword(), manager.getPassword());
        assertEquals(volunteer.isAvailable(), manager.isAvailable());
        assertTrue(manager.getReliefTypes()
                .contains(ReliefType.MEDICAL_CARE));
    }

    @Test
    void fullConstructorStoresManagerInformation() {
        UUID id = UUID.randomUUID();

        ShelterManager manager = new ShelterManager(id, "Jane", "Doe",
                LocalDate.of(1990, 1, 1), "password",
                new Location("SC", "Charleston", "1 Main St", "29401"),
                new Contact("jane@example.com", "8035550100",
                        ContactPreference.EMAIL),
                new Contact("emergency@example.com", "8035550101",
                        ContactPreference.CALL),
                true, new ArrayList<>(List.of(
                        ReliefType.TRANSPORTATION)), null);

        assertEquals(id, manager.getId());
        assertEquals("Jane", manager.getFirstName());
        assertEquals("Doe", manager.getLastName());
        assertTrue(manager.isAvailable());
        assertTrue(manager.getReliefTypes()
                .contains(ReliefType.TRANSPORTATION));
    }

    @Test
    void closeReliefRequestCompletesRequestAndFreesVolunteer() {
        ShelterManager manager = new ShelterManager();
        Volunteer volunteer = sampleVolunteer();
        volunteer.setAvailable(false);

        ReliefRequest request = new ReliefRequest();
        request.setStatus(ReliefStatus.ASSIGNED);
        request.setAssignedTo(volunteer);
        volunteer.setTask(request);

        assertTrue(manager.closeReliefRequest(request));
        assertEquals(ReliefStatus.COMPLETED, request.getStatus());
        assertTrue(volunteer.isAvailable());
        assertNull(volunteer.getTask());
    }

    @Test
    void closeReliefRequestRejectsRequestWithoutAssignedVolunteer() {
        ShelterManager manager = new ShelterManager();
        ReliefRequest request = new ReliefRequest();

        assertDoesNotThrow(() -> manager.closeReliefRequest(request));
        assertEquals(ReliefStatus.REQUESTED, request.getStatus());
    }
}