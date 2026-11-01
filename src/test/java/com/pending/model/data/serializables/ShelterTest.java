package com.pending.model.data.serializables;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.pending.model.enums.LivingBeingType;
import com.pending.model.enums.ReliefType;
import com.pending.model.enums.ShelterStatus;

class ShelterTest {

    private LivingBeing createHuman(String firstName) {
        return new LivingBeing(firstName, "Test",
                LocalDate.of(1990, 1, 1), LivingBeingType.HUMAN,
                new ArrayList<>(List.of(ReliefType.FOOD)));
    }

    private LivingBeing createPet(String firstName) {
        return new LivingBeing(firstName, "Test",
                LocalDate.of(2020, 1, 1), LivingBeingType.PET,
                new ArrayList<>(List.of(ReliefType.PET_SHELTER)));
    }

    private Volunteer createVolunteer(ReliefType... skills) {
        Volunteer volunteer = new Volunteer();

        for (ReliefType skill : skills) {
            volunteer.addReliefType(skill);
        }

        return volunteer;
    }

    private Shelter createOpenShelter(int capacity,
            boolean petFriendly) {

        return new Shelter("Shelter A", new Location("SC",
                "Charleston", "1 Main St", "29401"), capacity,
                new ArrayList<>(), ShelterStatus.OPEN, new Contact(),
                petFriendly);
    }

    @Test
    void defaultConstructorCreatesEmptyOpenShelter() {
        Shelter shelter = new Shelter();

        assertEquals("", shelter.getName());
        assertEquals(0, shelter.getCapacity());
        assertEquals(ShelterStatus.OPEN, shelter.getStatus());
        assertFalse(shelter.isPetFriendly());
        assertTrue(shelter.getOccupants().isEmpty());
        assertTrue(shelter.getVolunteers().isEmpty());
        assertEquals(-1, shelter.getTimeTo());
        assertNotNull(shelter.getId());
    }

    @Test
    void constructorStoresProvidedShelterDetails() {
        Location location = new Location("SC", "Charleston",
                "1 Main St", "29401");
        Contact contact = new Contact();
        ArrayList<Volunteer> volunteers = new ArrayList<>(
                List.of(createVolunteer(ReliefType.FOOD)));

        Shelter shelter = new Shelter("Shelter A", location, 50,
                volunteers, ShelterStatus.OPEN, contact, true);

        assertEquals("Shelter A", shelter.getName());
        assertSame(location, shelter.getLocation());
        assertEquals(50, shelter.getCapacity());
        assertEquals(volunteers, shelter.getVolunteers());
        assertEquals(ShelterStatus.OPEN, shelter.getStatus());
        assertSame(contact, shelter.getContactInfo());
        assertTrue(shelter.isPetFriendly());
    }

    @Test
    void constructorWithIdUsesProvidedId() {
        UUID id = UUID.randomUUID();

        Shelter shelter = new Shelter(id, "Shelter A",
                new Location(), 20, new ArrayList<>(),
                ShelterStatus.OPEN, new Contact(), false);

        assertEquals(id, shelter.getId());
    }

    @Test
    void constructorCopiesVolunteerList() {
        ArrayList<Volunteer> volunteers = new ArrayList<>(
                List.of(createVolunteer(ReliefType.FOOD)));

        Shelter shelter = new Shelter("Shelter A", new Location(), 20,
                volunteers, ShelterStatus.OPEN, new Contact(), false);

        volunteers.add(createVolunteer(ReliefType.WATER));

        assertEquals(1, shelter.getVolunteers().size());
    }

    @Test
    void setVolunteersWithNullCreatesEmptyList() {
        Shelter shelter = new Shelter();

        shelter.setVolunteers(null);

        assertNotNull(shelter.getVolunteers());
        assertTrue(shelter.getVolunteers().isEmpty());
    }

    @Test
    void getNumOccupantsCountsOnlyHumans() {
        Shelter shelter = new Shelter();
        shelter.addOccupant(createHuman("Jane"));
        shelter.addOccupant(createHuman("Sam"));
        shelter.addOccupant(createPet("Rex"));

        assertEquals(2, shelter.getNumOccupants());
    }

    @Test
    void addOccupantAddsOccupantToShelter() {
        Shelter shelter = new Shelter();

        assertTrue(shelter.addOccupant(createHuman("Jane")));
        assertEquals(1, shelter.getOccupants().size());
    }

    @Test
    void removeOccupantRemovesExistingOccupant() {
        Shelter shelter = new Shelter();
        LivingBeing occupant = createHuman("Jane");
        shelter.addOccupant(occupant);

        assertTrue(shelter.removeOccupant(occupant));
        assertTrue(shelter.getOccupants().isEmpty());
    }

    @Test
    void hasCapacityReturnsFalseForClosedShelter() {
        Shelter shelter = new Shelter("Shelter A", new Location(), 20,
                new ArrayList<>(), ShelterStatus.CLOSED, new Contact(),
                false);

        assertFalse(shelter.hasCapacity(1));
    }

    @Test
    void hasCapacityReturnsTrueWhenOpenShelterHasEnoughRoom() {
        Shelter shelter = createOpenShelter(2, false);
        shelter.addOccupant(createHuman("Jane"));

        assertTrue(shelter.hasCapacity(1));
    }

    @Test
    void hasCapacityReturnsFalseWhenOpenShelterIsFull() {
        Shelter shelter = createOpenShelter(1, false);
        shelter.addOccupant(createHuman("Jane"));

        assertFalse(shelter.hasCapacity(1));
    }

    @Test
    void checkInAddsHumanGroupToShelter() {
        Shelter shelter = createOpenShelter(10, false);
        ArrayList<LivingBeing> group = new ArrayList<>(List.of(
                createHuman("Jane"), createHuman("Sam")));

        assertTrue(shelter.checkIn(group));
        assertEquals(2, shelter.getOccupants().size());
    }

    @Test
    void checkInRejectsGroupWithPetWhenShelterIsNotPetFriendly() {
        Shelter shelter = createOpenShelter(10, false);
        ArrayList<LivingBeing> group = new ArrayList<>(List.of(
                createHuman("Jane"), createPet("Rex")));

        assertFalse(shelter.checkIn(group));
        assertTrue(shelter.getOccupants().isEmpty());
    }

    @Test
    void checkInAcceptsGroupWithPetWhenShelterIsPetFriendly() {
        Shelter shelter = createOpenShelter(10, true);
        ArrayList<LivingBeing> group = new ArrayList<>(List.of(
                createHuman("Jane"), createPet("Rex")));

        assertTrue(shelter.checkIn(group));
        assertEquals(2, shelter.getOccupants().size());
    }

    @Test
    void hasMedicalStaffReturnsTrueWhenVolunteerHasMedicalSkill() {
        ArrayList<Volunteer> volunteers = new ArrayList<>(List.of(
                createVolunteer(ReliefType.TRANSPORTATION),
                createVolunteer(ReliefType.MEDICAL_CARE)));

        Shelter shelter = new Shelter("Shelter A", new Location(), 10,
                volunteers, ShelterStatus.OPEN, new Contact(), false);

        assertTrue(shelter.hasMedicalStaff());
    }

    @Test
    void hasMedicalStaffReturnsFalseWithoutMedicalVolunteer() {
        ArrayList<Volunteer> volunteers = new ArrayList<>(List.of(
                createVolunteer(ReliefType.TRANSPORTATION)));

        Shelter shelter = new Shelter("Shelter A", new Location(), 10,
                volunteers, ShelterStatus.OPEN, new Contact(), false);

        assertFalse(shelter.hasMedicalStaff());
    }

    @Test
    void toStringIncludesShelterDetails() {
        Shelter shelter = createOpenShelter(10, true);
        shelter.setTimeTo(300);

        String result = shelter.toString();

        assertTrue(result.contains("Shelter A"));
        assertTrue(result.contains("Pet Friendly"));
        assertTrue(result.contains("Time To: 5mins"));
    }

    @Test
    void managerToStringListsOccupants() {
        Shelter shelter = createOpenShelter(10, false);
        shelter.addOccupant(createHuman("Jane"));

        assertTrue(shelter.managerToString().contains("Jane Test"));
    }

    @Test
    void constructorUsesProvidedLocationIdForReference() {
        Location location = new Location("SC", "Charleston",
                "1 Main St", "29401");

        Shelter shelter = new Shelter("Shelter A", location, 10,
                new ArrayList<>(), ShelterStatus.OPEN, new Contact(),
                false);

        assertEquals(location.getId(),
                shelter.getLocationUUIDReference());
    }

    @Test
    void constructorUsesProvidedContactIdForReference() {
        Contact contact = new Contact();

        Shelter shelter = new Shelter("Shelter A", new Location(), 10,
                new ArrayList<>(), ShelterStatus.OPEN, contact, false);

        assertEquals(contact.getId(),
                shelter.getContactUUIDReference());
    }
}