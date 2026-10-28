package com.pending.model.data.serializables;

import com.pending.model.enums.LivingBeingType;
import com.pending.model.enums.ReliefType;
import com.pending.model.enums.ShelterStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShelterTest {

    private static LivingBeing human(String name, ReliefType... needs) {
        return new LivingBeing(name, "Test", LocalDate.of(1990, 1, 1),
                LivingBeingType.HUMAN, new ArrayList<>(List.of(needs)));
    }

    private static LivingBeing pet(String name, ReliefType... needs) {
        return new LivingBeing(name, "Test", LocalDate.of(2020, 1, 1),
                LivingBeingType.PET, new ArrayList<>(List.of(needs)));
    }

    private static Volunteer volunteerWithSkill(ReliefType... skills) {
        Volunteer volunteer = new Volunteer();
        for (ReliefType skill : skills) {
            volunteer.addReliefType(skill);
        }
        return volunteer;
    }

    @Test
    void defaultConstructorIsEmptyAndOpen() {
        Shelter shelter = new Shelter();

        assertEquals("", shelter.getName());
        assertEquals(0, shelter.getCapacity());
        assertEquals(ShelterStatus.OPEN, shelter.getStatus());
        assertFalse(shelter.isPetFriendly());
        assertTrue(shelter.getOccupants().isEmpty());
        assertTrue(shelter.getVolunteers().isEmpty());
        assertEquals(-1, shelter.getTimeTo());
    }

    @Test
    void sevenArgConstructorSetsFields() {
        Location location = new Location("SC", "Charleston", "1 Main St", "29401");
        Contact contact = new Contact();
        ArrayList<Volunteer> volunteers = new ArrayList<>(List.of(volunteerWithSkill()));

        Shelter shelter = new Shelter("Shelter A", location, 50, volunteers,
                ShelterStatus.OPEN, contact, true);

        assertEquals("Shelter A", shelter.getName());
        assertSame(location, shelter.getLocation());
        assertEquals(50, shelter.getCapacity());
        assertEquals(1, shelter.getVolunteers().size());
        assertSame(contact, shelter.getContactInfo());
        assertTrue(shelter.isPetFriendly());
    }

    @Test
    void settersCopyTheVolunteerListDefensively() {
        Shelter shelter = new Shelter();
        ArrayList<Volunteer> volunteers = new ArrayList<>(List.of(volunteerWithSkill()));

        shelter.setVolunteers(volunteers);
        volunteers.add(volunteerWithSkill()); // mutate the original list afterward

        assertEquals(1, shelter.getVolunteers().size(), "setVolunteers should copy, not alias, the list");
    }

    @Test
    void setVolunteersWithNullGivesAnEmptyListInsteadOfNull() {
        Shelter shelter = new Shelter();

        shelter.setVolunteers(null);

        assertNotNull(shelter.getVolunteers());
        assertTrue(shelter.getVolunteers().isEmpty());
    }

    @Test
    void getNumOccupantsCountsOnlyHumans() {
        Shelter shelter = new Shelter();
        shelter.addOccupant(human("A"));
        shelter.addOccupant(human("B"));
        shelter.addOccupant(pet("C"));

        assertEquals(2, shelter.getNumOccupants(),
                "getNumOccupants only counts HUMAN occupants; pets do not count toward capacity");
        assertEquals(3, shelter.getOccupants().size());
    }

    @Test
    void addAndRemoveOccupant() {
        Shelter shelter = new Shelter();
        LivingBeing occupant = human("A");

        assertTrue(shelter.addOccupant(occupant));
        assertTrue(shelter.getOccupants().contains(occupant));

        assertTrue(shelter.removeOccupant(occupant));
        assertFalse(shelter.getOccupants().contains(occupant));
    }

    @Test
    void addAndRemoveVolunteer() {
        Shelter shelter = new Shelter();
        Volunteer volunteer = volunteerWithSkill();

        shelter.addVolunteer(volunteer);
        assertTrue(shelter.getVolunteers().contains(volunteer));

        shelter.removeVolunteer(volunteer);
        assertFalse(shelter.getVolunteers().contains(volunteer));
    }

    @Test
    void hasCapacityIsFalseWhenNotOpen() {
        Shelter shelter = new Shelter("S", new Location(), 100, new ArrayList<>(),
                ShelterStatus.CLOSED, new Contact(), false);

        assertFalse(shelter.hasCapacity(1));
    }

    @Test
    void hasCapacityComparesAvailableSpaceToPartySize() {
        Shelter shelter = new Shelter("S", new Location(), 2, new ArrayList<>(),
                ShelterStatus.OPEN, new Contact(), false);
        shelter.addOccupant(human("A"));

        assertTrue(shelter.hasCapacity(1));
        assertFalse(shelter.hasCapacity(2));
    }

    @Test
    void checkInAddsAllWhenNoPetsInvolved() {
        Shelter shelter = new Shelter("S", new Location(), 10, new ArrayList<>(),
                ShelterStatus.OPEN, new Contact(), false);
        ArrayList<LivingBeing> group = new ArrayList<>(List.of(human("A"), human("B")));

        assertTrue(shelter.checkIn(group));
        assertEquals(2, shelter.getOccupants().size());
    }

    @Test
    void checkInRejectsPetsWhenNotPetFriendlyAndAddsNothing() {
        Shelter shelter = new Shelter("S", new Location(), 10, new ArrayList<>(),
                ShelterStatus.OPEN, new Contact(), false);
        ArrayList<LivingBeing> group = new ArrayList<>(List.of(human("A"), pet("B")));

        assertFalse(shelter.checkIn(group));
        assertTrue(shelter.getOccupants().isEmpty(),
                "checkIn should not add anyone from a rejected group, including the "
                        + "humans that came before the pet in the list");
    }

    @Test
    void checkInAcceptsPetsWhenPetFriendly() {
        Shelter shelter = new Shelter("S", new Location(), 10, new ArrayList<>(),
                ShelterStatus.OPEN, new Contact(), true);
        ArrayList<LivingBeing> group = new ArrayList<>(List.of(human("A"), pet("B")));

        assertTrue(shelter.checkIn(group));
        assertEquals(2, shelter.getOccupants().size());
    }

    @Test
    void hasMedicalStaffTrueWhenAVolunteerHasMedicalCareSkill() {
        ArrayList<Volunteer> volunteers = new ArrayList<>(List.of(
                volunteerWithSkill(ReliefType.TRANSPORTATION),
                volunteerWithSkill(ReliefType.MEDICAL_CARE)));
        Shelter shelter = new Shelter("S", new Location(), 10, volunteers,
                ShelterStatus.OPEN, new Contact(), false);

        assertTrue(shelter.hasMedicalStaff());
    }

    @Test
    void hasMedicalStaffFalseWhenNoVolunteerHasTheSkill() {
        ArrayList<Volunteer> volunteers = new ArrayList<>(List.of(
                volunteerWithSkill(ReliefType.TRANSPORTATION)));
        Shelter shelter = new Shelter("S", new Location(), 10, volunteers,
                ShelterStatus.OPEN, new Contact(), false);

        assertFalse(shelter.hasMedicalStaff());
    }

    @Test
    void petFriendlyGetterAndSetter() {
        Shelter shelter = new Shelter();

        shelter.setPetFriendly(true);

        assertTrue(shelter.isPetFriendly());
    }

    @Test
    void timeToGetterAndSetter() {
        Shelter shelter = new Shelter();

        shelter.setTimeTo(120.5);

        assertEquals(120.5, shelter.getTimeTo());
    }

    @Test
    void managerAndLocationAndContactGettersAndSetters() {
        Shelter shelter = new Shelter();
        ShelterManager manager = new ShelterManager();
        Location location = new Location("SC", "Charleston", "1 Main St", "29401");
        Contact contact = new Contact();

        shelter.setManager(manager);
        shelter.setLocation(location);
        shelter.setContactInfo(contact);

        assertSame(manager, shelter.getManager());
        assertSame(location, shelter.getLocation());
        assertSame(contact, shelter.getContactInfo());
    }

    @Test
    void toStringAndManagerToStringDoNotThrow() {
        // Occupants need at least one relief need each, since LivingBeing.toString()
        // currently throws for an empty needs list (see LivingBeingTest).
        Shelter shelter = new Shelter("Shelter A", new Location("SC", "Charleston", "1 Main St", "29401"),
                10, new ArrayList<>(List.of(volunteerWithSkill(ReliefType.MEDICAL_CARE))),
                ShelterStatus.OPEN, new Contact(), true);
        shelter.addOccupant(human("Jane", ReliefType.FOOD));
        shelter.setTimeTo(300);

        String summary = shelter.toString();
        String detailed = shelter.managerToString();

        assertTrue(summary.contains("Shelter A"));
        assertTrue(summary.contains("Pet Friendly"));
        assertTrue(summary.contains("Has Medical Staff"));
        assertTrue(detailed.contains("Jane"));
    }
}