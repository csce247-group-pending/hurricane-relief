package com.pending.model.data.serializables;

import com.pending.model.enums.LivingBeingType;
import com.pending.model.enums.ReliefType;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LivingBeingTest {

    @Test
    void defaultConstructorIsAnEmptyPetBornToday() {
        LivingBeing being = new LivingBeing();

        assertEquals("", being.getFirstName());
        assertEquals("", being.getLastName());
        assertEquals(LivingBeingType.PET, being.getType());
        assertEquals(LocalDate.now(), being.getDateOfBirth());
        assertTrue(being.getReliefNeeds().isEmpty());
    }

    @Test
    void fiveArgConstructorSetsFields() {
        LocalDate dob = LocalDate.of(1995, 6, 15);
        ArrayList<ReliefType> needs = new ArrayList<>(List.of(ReliefType.FOOD, ReliefType.WATER));

        LivingBeing being = new LivingBeing("Jane", "Doe", dob, LivingBeingType.HUMAN, needs);

        assertEquals("Jane", being.getFirstName());
        assertEquals("Doe", being.getLastName());
        assertEquals(dob, being.getDateOfBirth());
        assertEquals(LivingBeingType.HUMAN, being.getType());
        assertEquals(2, being.getReliefNeeds().size());
    }

    @Test
    void settersUpdateFields() {
        LivingBeing being = new LivingBeing();

        being.setFirstName("Max");
        being.setLastName("Pup");
        being.setDateOfBirth(LocalDate.of(2020, 1, 1));
        being.setType(LivingBeingType.PET);

        assertEquals("Max", being.getFirstName());
        assertEquals("Pup", being.getLastName());
        assertEquals(LocalDate.of(2020, 1, 1), being.getDateOfBirth());
        assertEquals(LivingBeingType.PET, being.getType());
    }

    @Test
    void setReliefNeedsWithNullGivesAnEmptyListInsteadOfNull() {
        LivingBeing being = new LivingBeing();

        being.setReliefNeeds(null);

        assertNotNull(being.getReliefNeeds());
        assertTrue(being.getReliefNeeds().isEmpty());
    }

    @Test
    void addAndRemoveReliefNeed() {
        LivingBeing being = new LivingBeing();

        assertTrue(being.addReliefNeed(ReliefType.FOOD));
        assertTrue(being.getReliefNeeds().contains(ReliefType.FOOD));

        assertTrue(being.removeReliefNeed(ReliefType.FOOD));
        assertFalse(being.getReliefNeeds().contains(ReliefType.FOOD));
    }

    @Test
    void removeReliefNeedNotPresentReturnsFalse() {
        LivingBeing being = new LivingBeing();

        assertFalse(being.removeReliefNeed(ReliefType.MEDICAL_CARE));
    }

    @Test
    void getAgeComputesFullYearsSinceDateOfBirth() {
        LocalDate dob = LocalDate.now().minusYears(30).minusDays(1);
        LivingBeing being = new LivingBeing("A", "B", dob, LivingBeingType.HUMAN, new ArrayList<>());

        assertEquals(30, being.getAge());
        assertEquals(Period.between(dob, LocalDate.now()).getYears(), being.getAge());
    }

    @Test
    void getAgeIsZeroForNewborn() {
        LivingBeing being = new LivingBeing("A", "B", LocalDate.now(),
                LivingBeingType.HUMAN, new ArrayList<>());

        assertEquals(0, being.getAge());
    }

    @Test
    void toStringIncludesNameTypeAgeAndNeeds() {
        LivingBeing being = new LivingBeing("Jane", "Doe", LocalDate.of(1990, 1, 1),
                LivingBeingType.HUMAN, new ArrayList<>(List.of(ReliefType.FOOD, ReliefType.WATER)));

        String result = being.toString();

        assertTrue(result.contains("Jane Doe"));
        assertTrue(result.contains("HUMAN"));
        assertTrue(result.contains("FOOD"));
        assertTrue(result.contains("WATER"));
    }

    /**
     * BUG: LivingBeing.toString() unconditionally calls reliefNeeds.get(0)
     * before checking whether the list is empty:
     *     String reliefNeedsString = "Relief Needs: " + reliefNeeds.get(0);
     *     ...
     *     return ... + (!reliefNeeds.isEmpty() ? ... : "");
     * The emptiness check at the end never gets reached for an empty list,
     * because reliefNeeds.get(0) throws first. This means toString() crashes
     * for any LivingBeing with no relief needs -- including a brand new
     * LivingBeing() from the default constructor.
     */
    @Test
    void toStringThrowsWhenReliefNeedsIsEmpty() {
        LivingBeing being = new LivingBeing(); // default constructor has empty relief needs

        assertThrows(IndexOutOfBoundsException.class, being::toString,
                "toString() currently crashes on an empty relief-needs list; "
                        + "if this now passes, the bug has been fixed and this test "
                        + "should be replaced with an assertion that toString() "
                        + "returns a string with no \"Relief Needs\" section");
    }

    /**
     * BUG: SerializeJSONLivingBeing never reads or writes "id". Every
     * deserialized LivingBeing keeps whatever random id its no-arg
     * constructor generated, instead of the id that was actually serialized.
     * This matters because other classes (like Shelter) store occupants by
     * UUID reference and expect LivingBeing ids to be stable across a
     * save/load cycle.
     */
    @Test
    void deserializeDoesNotRestoreTheOriginalId() {
        LivingBeing original = new LivingBeing("Jane", "Doe", LocalDate.of(1990, 1, 1),
                LivingBeingType.HUMAN, new ArrayList<>(List.of(ReliefType.FOOD)));
        JSONObject json = (JSONObject) original.serialize();

        LivingBeing restored = new LivingBeing();
        restored.deserialize(json);

        // Other fields come back correctly...
        assertEquals(original.getFirstName(), restored.getFirstName());
        assertEquals(original.getDateOfBirth(), restored.getDateOfBirth());

        // ...but the id does not, because "id" is never written or read.
        assertNotEquals(original.getId(), restored.getId(),
                "if this now passes (ids match), the id-serialization bug has been "
                        + "fixed and this assertion should be flipped to assertEquals");
    }
}