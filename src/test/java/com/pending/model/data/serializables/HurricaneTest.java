package com.pending.model.data.serializables;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.pending.model.enums.HurricaneStatus;

class HurricaneTest {

    @Test
    void defaultConstructorCreatesPassedCategoryFiveHurricane() {
        Hurricane hurricane = new Hurricane();

        assertEquals("", hurricane.getName());
        assertEquals(5, hurricane.getCategory());
        assertEquals(HurricaneStatus.PASSED, hurricane.getStatus());
        assertTrue(hurricane.getLocations().isEmpty());
        assertTrue(hurricane.getLocationsUUIDReference().isEmpty());
        assertNotNull(hurricane.getId());
    }

    @Test
    void constructorStoresProvidedHurricaneDetails() {
        Location location = new Location(
                "SC", "Charleston", "1 Main St", "29401");
        ArrayList<Location> locations = new ArrayList<>();
        locations.add(location);

        Hurricane hurricane = new Hurricane(
                "Helene",
                3,
                locations,
                HurricaneStatus.APPROACHING);

        assertEquals("Helene", hurricane.getName());
        assertEquals(3, hurricane.getCategory());
        assertEquals(HurricaneStatus.APPROACHING, hurricane.getStatus());
        assertEquals(1, hurricane.getLocations().size());
        assertEquals(location, hurricane.getLocations().get(0));
        assertTrue(hurricane.getLocationsUUIDReference().isEmpty());
    }

    @Test
    void constructorWithIdPreservesProvidedId() {
        UUID id = UUID.randomUUID();

        Hurricane hurricane = new Hurricane(
                id,
                "Milton",
                4,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(3),
                new ArrayList<>(),
                HurricaneStatus.LANDFALL);

        assertEquals(id, hurricane.getId());
        assertEquals("Milton", hurricane.getName());
        assertEquals(4, hurricane.getCategory());
        assertEquals(HurricaneStatus.LANDFALL, hurricane.getStatus());
    }

    @Test
    void addLocationAddsLocationToHurricane() {
        Hurricane hurricane = new Hurricane();
        Location location = new Location(
                "SC", "Charleston", "1 Main St", "29401");

        hurricane.addLocation(location);

        assertEquals(1, hurricane.getLocations().size());
        assertEquals(location, hurricane.getLocations().get(0));
    }

    @Test
    void removeLocationRemovesExistingLocation() {
        Hurricane hurricane = new Hurricane();
        Location location = new Location(
                "SC", "Charleston", "1 Main St", "29401");

        hurricane.addLocation(location);
        hurricane.removeLocation(location);

        assertTrue(hurricane.getLocations().isEmpty());
    }

    @Test
    void removeLocationDoesNotRemoveOtherLocations() {
        Hurricane hurricane = new Hurricane();
        Location charleston = new Location(
                "SC", "Charleston", "1 Main St", "29401");
        Location columbia = new Location(
                "SC", "Columbia", "2 Main St", "29201");

        hurricane.addLocation(charleston);
        hurricane.removeLocation(columbia);

        assertEquals(1, hurricane.getLocations().size());
        assertEquals(charleston, hurricane.getLocations().get(0));
    }

    @Test
    void newlyCreatedHurricanesReceiveDifferentIds() {
        Hurricane firstHurricane = new Hurricane();
        Hurricane secondHurricane = new Hurricane();

        assertNotEquals(firstHurricane.getId(), secondHurricane.getId());
    }
}