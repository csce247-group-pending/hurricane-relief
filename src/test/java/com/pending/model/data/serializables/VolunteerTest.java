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

class VolunteerTest {

    private static Location sampleLocation() {
        return new Location("SC", "Charleston", "1 Main St", "29401");
    }

    private static Contact sampleContact(String email) {
        return new Contact(email, "555-0100", ContactPreference.EMAIL);
    }

    @Test
    void defaultConstructorIsAvailableWithNoSkillsOrTask() {
        Volunteer volunteer = new Volunteer();

        assertTrue(volunteer.isAvailable());
        assertTrue(volunteer.getReliefTypes().isEmpty());
        assertNull(volunteer.getTask());
        assertNotNull(volunteer.getSorter());
    }

    @Test
    void elevenArgConstructorSetsFields() {
        ArrayList<ReliefType> skills = new ArrayList<>(List.of(ReliefType.MEDICAL_CARE));

        Volunteer volunteer = new Volunteer("Jane", "Doe", LocalDate.of(1990, 1, 1),
                new ArrayList<>(), "password", sampleLocation(),
                sampleContact("jane@example.com"), sampleContact("em@example.com"),
                false, skills, null);

        assertEquals("Jane", volunteer.getFirstName());
        assertFalse(volunteer.isAvailable());
        assertEquals(1, volunteer.getReliefTypes().size());
        assertNull(volunteer.getTask());
    }

    @Test
    void idConstructorUsesProvidedId() {
        UUID id = UUID.randomUUID();
        Volunteer volunteer = new Volunteer(id, "Jane", "Doe", LocalDate.of(1990, 1, 1),
                new ArrayList<>(), "password", sampleLocation(),
                sampleContact("jane@example.com"), sampleContact("em@example.com"),
                true, new ArrayList<>(), null);

        assertEquals(id, volunteer.getId());
    }

    @Test
    void availabilityGetterAndSetter() {
        Volunteer volunteer = new Volunteer();

        volunteer.setAvailable(false);

        assertFalse(volunteer.isAvailable());
    }

    @Test
    void addAndRemoveReliefType() {
        Volunteer volunteer = new Volunteer();

        volunteer.addReliefType(ReliefType.TRANSPORTATION);
        assertTrue(volunteer.getReliefTypes().contains(ReliefType.TRANSPORTATION));

        volunteer.removeReliefType(ReliefType.TRANSPORTATION);
        assertFalse(volunteer.getReliefTypes().contains(ReliefType.TRANSPORTATION));
    }

    @Test
    void taskGetterAndSetter() {
        Volunteer volunteer = new Volunteer();
        ReliefRequest request = new ReliefRequest();

        volunteer.setTask(request);

        assertSame(request, volunteer.getTask());
    }

    @Test
    void taskUUIDReferenceGetterAndSetter() {
        Volunteer volunteer = new Volunteer();
        UUID taskId = UUID.randomUUID();

        volunteer.setTaskUUIDReference(taskId);

        assertEquals(taskId, volunteer.getTaskUUIDReference());
    }

    @Test
    void acceptReliefRequestAssignsAndMarksUnavailable() {
        Volunteer volunteer = new Volunteer();
        ReliefRequest request = new ReliefRequest();

        boolean result = volunteer.acceptReliefRequest(request);

        assertTrue(result);
        assertSame(request, volunteer.getTask());
        assertEquals(request.getId(), volunteer.getTaskUUIDReference());
        assertSame(volunteer, request.getAssignedTo());
        assertEquals(volunteer.getId(), request.getAssignedToUUIDReference());
        assertEquals(ReliefStatus.ASSIGNED, request.getStatus());
        assertFalse(volunteer.isAvailable());
    }

    @Test
    void openReliefRequestSetsTaskWithoutAssigningOrChangingStatus() {
        Volunteer volunteer = new Volunteer();
        ReliefRequest request = new ReliefRequest();
        request.setStatus(ReliefStatus.REQUESTED);

        boolean result = volunteer.openReliefRequest(request);

        assertTrue(result);
        assertSame(request, volunteer.getTask());
        assertEquals(request.getId(), volunteer.getTaskUUIDReference());
        assertNull(request.getAssignedTo(), "openReliefRequest should not assign the request");
        assertEquals(ReliefStatus.REQUESTED, request.getStatus(), "openReliefRequest should not change status");
        assertTrue(volunteer.isAvailable(), "openReliefRequest should not change availability");
    }

    @Test
    void toStringIncludesSkillsAndCurrentTask() {
        Volunteer volunteer = new Volunteer();
        volunteer.addReliefType(ReliefType.MEDICAL_CARE);

        assertTrue(volunteer.toString().contains("Current Task: None"));

        ReliefRequest request = new ReliefRequest();
        request.setDescription("need help");
        volunteer.setTask(request);

        String result = volunteer.toString();
        assertTrue(result.contains("MEDICAL_CARE"));
        assertTrue(result.contains("need help"));
    }

    @Test
    void toStringWithNoSkillsOmitsTheSkillsSection() {
        Volunteer volunteer = new Volunteer();

        assertFalse(volunteer.toString().contains("Relief Skills"));
    }
}