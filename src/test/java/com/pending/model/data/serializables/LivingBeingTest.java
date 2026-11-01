package com.pending.model.data.serializables;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.pending.model.enums.LivingBeingType;
import com.pending.model.enums.ReliefType;

class LivingBeingTest {

    @Test
    void defaultConstructorCreatesEmptyPetBornToday() {
        LivingBeing being = new LivingBeing();

        assertEquals("", being.getFirstName());
        assertEquals("", being.getLastName());
        assertEquals(LivingBeingType.PET, being.getType());
        assertEquals(LocalDate.now(), being.getDateOfBirth());
        assertTrue(being.getReliefNeeds().isEmpty());
        assertNotNull(being.getId());
    }

    @Test
    void constructorStoresProvidedLivingBeingDetails() {
        LocalDate dateOfBirth = LocalDate.of(1995, 6, 15);
        ArrayList<ReliefType> needs = new ArrayList<>(
                List.of(ReliefType.FOOD, ReliefType.WATER));

        LivingBeing being = new LivingBeing("Jane", "Doe",
                dateOfBirth, LivingBeingType.HUMAN, needs);

        assertEquals("Jane", being.getFirstName());
        assertEquals("Doe", being.getLastName());
        assertEquals(dateOfBirth, being.getDateOfBirth());
        assertEquals(LivingBeingType.HUMAN, being.getType());
        assertEquals(needs, being.getReliefNeeds());
        assertNotNull(being.getId());
    }

    @Test
    void constructorWithNullReliefNeedsCreatesEmptyList() {
        LivingBeing being = new LivingBeing("Jane", "Doe",
                LocalDate.of(1995, 6, 15),
                LivingBeingType.HUMAN, null);

        assertNotNull(being.getReliefNeeds());
        assertTrue(being.getReliefNeeds().isEmpty());
    }

    @Test
    void addReliefNeedAddsNeedToList() {
        LivingBeing being = new LivingBeing();

        assertTrue(being.addReliefNeed(ReliefType.FOOD));
        assertEquals(List.of(ReliefType.FOOD),
                being.getReliefNeeds());
    }

    @Test
    void removeReliefNeedRemovesExistingNeed() {
        LivingBeing being = new LivingBeing();
        being.addReliefNeed(ReliefType.FOOD);

        assertTrue(being.removeReliefNeed(ReliefType.FOOD));
        assertTrue(being.getReliefNeeds().isEmpty());
    }

    @Test
    void removeReliefNeedReturnsFalseWhenNeedIsMissing() {
        LivingBeing being = new LivingBeing();

        assertFalse(being.removeReliefNeed(ReliefType.MEDICAL_CARE));
        assertTrue(being.getReliefNeeds().isEmpty());
    }

    @Test
    void getAgeCalculatesCompletedYears() {
        LocalDate dateOfBirth = LocalDate.now()
                .minusYears(30).minusDays(1);

        LivingBeing being = new LivingBeing("A", "B",
                dateOfBirth, LivingBeingType.HUMAN,
                new ArrayList<>());

        assertEquals(30, being.getAge());
        assertEquals(Period.between(dateOfBirth,
                LocalDate.now()).getYears(), being.getAge());
    }

    @Test
    void getAgeReturnsZeroForPersonBornToday() {
        LivingBeing being = new LivingBeing("A", "B",
                LocalDate.now(), LivingBeingType.HUMAN,
                new ArrayList<>());

        assertEquals(0, being.getAge());
    }

    @Test
    void toStringFormatsLivingBeingWithReliefNeeds() {
        LivingBeing being = new LivingBeing("Jane", "Doe",
                LocalDate.of(1990, 1, 1), LivingBeingType.HUMAN,
                new ArrayList<>(List.of(ReliefType.FOOD,
                        ReliefType.WATER)));

        String expected = "Jane Doe is a HUMAN | Age: "
                + being.getAge()
                + " | Relief Needs: Food, Water";

        assertEquals(expected, being.toString());
    }

    @Test
    void toStringHandlesEmptyReliefNeeds() {
        LivingBeing being = new LivingBeing();

        assertDoesNotThrow(being::toString);
    }
}