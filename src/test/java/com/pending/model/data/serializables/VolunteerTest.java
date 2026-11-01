package com.pending.model.data.serializables;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.pending.model.enums.ContactPreference;
import com.pending.model.enums.ReliefStatus;
import com.pending.model.enums.ReliefType;

class VolunteerTest {

    private Location sampleLocation() {
        return new Location("SC", "Charleston", "1 Main St",
                "29401");
    }

    private Contact sampleContact(String email) {
        return new Contact(email, "8035550100",
                ContactPreference.EMAIL);
    }

    private Volunteer createVolunteer() {
        return new Volunteer("Jane", "Doe",
                LocalDate.of(1990, 1, 1), new ArrayList<>(),
                "password", sampleLocation(),
                sampleContact("jane@example.com"),
                sampleContact("emergency@example.com"), true,
                new ArrayList<>(), null);
    }

    @Test
    void defaultConstructorCreatesAvailableVolunteerWithoutTask() {
        Volunteer volunteer = new Volunteer();

        assertTrue(volunteer.isAvailable());
        assertTrue(volunteer.getReliefTypes().isEmpty());
        assertNull(volunteer.getTask());
        assertNotNull(volunteer.getSorter());
        assertNotNull(volunteer.getId());
    }

    @Test
    void constructorStoresProvidedVolunteerDetails() {
        ArrayList<ReliefType> skills = new ArrayList<>(
                List.of(ReliefType.MEDICAL_CARE));

        Volunteer volunteer = new Volunteer("Jane", "Doe",
                LocalDate.of(1990, 1, 1), new ArrayList<>(),
                "password", sampleLocation(),
                sampleContact("jane@example.com"),
                sampleContact("emergency@example.com"), false,
                skills, null);

        assertEquals("Jane", volunteer.getFirstName());
        assertEquals("Doe", volunteer.getLastName());
        assertFalse(volunteer.isAvailable());
        assertEquals(skills, volunteer.getReliefTypes());
        assertNull(volunteer.getTask());
    }

    @Test
    void constructorWithIdUsesProvidedId() {
        UUID id = UUID.randomUUID();

        Volunteer volunteer = new Volunteer(id, "Jane", "Doe",
                LocalDate.of(1990, 1, 1), new ArrayList<>(),
                "password", sampleLocation(),
                sampleContact("jane@example.com"),
                sampleContact("emergency@example.com"), true,
                new ArrayList<>(), null);

        assertEquals(id, volunteer.getId());
    }

    @Test
    void addReliefTypeAddsSkillToVolunteer() {
        Volunteer volunteer = createVolunteer();

        volunteer.addReliefType(ReliefType.TRANSPORTATION);

        assertTrue(volunteer.getReliefTypes()
                .contains(ReliefType.TRANSPORTATION));
    }

    @Test
    void removeReliefTypeRemovesExistingSkill() {
        Volunteer volunteer = createVolunteer();
        volunteer.addReliefType(ReliefType.TRANSPORTATION);

        volunteer.removeReliefType(ReliefType.TRANSPORTATION);

        assertFalse(volunteer.getReliefTypes()
                .contains(ReliefType.TRANSPORTATION));
    }

    @Test
    void acceptReliefRequestAssignsRequestToVolunteer() {
        Volunteer volunteer = createVolunteer();
        ReliefRequest request = new ReliefRequest();

        assertTrue(volunteer.acceptReliefRequest(request));
        assertSame(request, volunteer.getTask());
        assertEquals(request.getId(),
                volunteer.getTaskUUIDReference());
        assertSame(volunteer, request.getAssignedTo());
        assertEquals(volunteer.getId(),
                request.getAssignedToUUIDReference());
        assertEquals(ReliefStatus.ASSIGNED, request.getStatus());
        assertFalse(volunteer.isAvailable());
    }

    @Test
    void openReliefRequestSetsTaskWithoutAssigningRequest() {
        Volunteer volunteer = createVolunteer();
        ReliefRequest request = new ReliefRequest();

        assertTrue(volunteer.openReliefRequest(request));
        assertSame(request, volunteer.getTask());
        assertEquals(request.getId(),
                volunteer.getTaskUUIDReference());
        assertNull(request.getAssignedTo());
        assertEquals(ReliefStatus.REQUESTED, request.getStatus());
        assertTrue(volunteer.isAvailable());
    }

    @Test
    void toStringListsVolunteerSkills() {
        Volunteer volunteer = createVolunteer();
        volunteer.addReliefType(ReliefType.MEDICAL_CARE);

        assertTrue(volunteer.toString()
                .contains("Relief Skills: Medical Care"));
    }

    @Test
    void toStringShowsNoneWhenVolunteerHasNoTask() {
        Volunteer volunteer = createVolunteer();

        assertTrue(volunteer.toString()
                .contains("Current Task: None"));
    }

    @Test
    void toStringIncludesCurrentTaskDetails() {
        Volunteer volunteer = createVolunteer();
        ReliefRequest request = new ReliefRequest();
        request.setDescription("Need water");
        volunteer.setTask(request);

        assertTrue(volunteer.toString().contains("Need water"));
    }

    @Test
    void constructorWithTaskUsesTaskIdForReference() {
        ReliefRequest task = new ReliefRequest();

        Volunteer volunteer = new Volunteer("Jane", "Doe",
                LocalDate.of(1990, 1, 1), new ArrayList<>(),
                "password", sampleLocation(),
                sampleContact("jane@example.com"),
                sampleContact("emergency@example.com"), false,
                new ArrayList<>(), task);

        assertEquals(task.getId(), volunteer.getTaskUUIDReference());
    }

    @Test
    void constructorWithoutTaskUsesNullTaskReference() {
        Volunteer volunteer = createVolunteer();

        assertNull(volunteer.getTaskUUIDReference());
    }
}