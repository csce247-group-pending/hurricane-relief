package com.pending.model.data.serializables;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class LocationTest {

    @Test
    void defaultConstructorCreatesEmptyLocation() {
        Location location = new Location();

        assertEquals("", location.getState());
        assertEquals("", location.getCity());
        assertEquals("", location.getStreet());
        assertEquals("", location.getZip());
        assertNotNull(location.getId());
    }

    @Test
    void constructorStoresAddressInStateCityStreetZipOrder() {
        Location location = new Location("SC", "Charleston",
                "1 Main St", "29401");

        assertEquals("SC", location.getState());
        assertEquals("Charleston", location.getCity());
        assertEquals("1 Main St", location.getStreet());
        assertEquals("29401", location.getZip());
    }

    @Test
    void constructorWithIdUsesProvidedId() {
        UUID id = UUID.randomUUID();

        Location location = new Location(id, "SC", "Charleston",
                "1 Main St", "29401");

        assertEquals(id, location.getId());
    }

    @Test
    void isInStateReturnsTrueForMatchingState() {
        Location location = new Location("SC", "Charleston",
                "1 Main St", "29401");

        assertTrue(location.isInState("SC"));
    }

    @Test
    void isInStateReturnsFalseForDifferentState() {
        Location location = new Location("SC", "Charleston",
                "1 Main St", "29401");

        assertFalse(location.isInState("NC"));
    }

    @Test
    void isInCityReturnsTrueForMatchingCity() {
        Location location = new Location("SC", "Charleston",
                "1 Main St", "29401");

        assertTrue(location.isInCity("Charleston"));
    }

    @Test
    void isInCityReturnsFalseForDifferentCity() {
        Location location = new Location("SC", "Charleston",
                "1 Main St", "29401");

        assertFalse(location.isInCity("Columbia"));
    }

    @Test
    void isInZipReturnsTrueForMatchingZipCode() {
        Location location = new Location("SC", "Charleston",
                "1 Main St", "29401");

        assertTrue(location.isInZip("29401"));
    }

    @Test
    void isInZipReturnsFalseForDifferentZipCode() {
        Location location = new Location("SC", "Charleston",
                "1 Main St", "29401");

        assertFalse(location.isInZip("29403"));
    }

    @Test
    void toStringFormatsFullAddress() {
        Location location = new Location("SC", "Charleston",
                "1 Main St", "29401");

        assertEquals("1 Main St, Charleston, SC 29401",
                location.toString());
    }
}