package com.pending.model.data.serializables;

import com.pending.model.data.DataManager;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * NOTE on constructor parameter order: Location's constructor is
 * Location(String state, String city, String street, String zip), matching
 * what SerializeJSONLocation reads and writes. Some other code in this
 * project (LocationUI) calls `new Location(street, city, state, zip)`,
 * which silently swaps the state and street values. These tests use the
 * real, documented parameter order.
 */
class LocationTest {

    @TempDir
    Path tempDir;

    @Test
    void defaultConstructorIsAllEmptyStrings() {
        Location location = new Location();

        assertEquals("", location.getState());
        assertEquals("", location.getCity());
        assertEquals("", location.getStreet());
        assertEquals("", location.getZip());
        assertNotNull(location.getId());
    }

    @Test
    void fourArgConstructorSetsFieldsInStateCityStreetZipOrder() {
        Location location = new Location("SC", "Charleston", "1 Main St", "29401");

        assertEquals("SC", location.getState());
        assertEquals("Charleston", location.getCity());
        assertEquals("1 Main St", location.getStreet());
        assertEquals("29401", location.getZip());
    }

    @Test
    void idConstructorUsesProvidedId() {
        UUID id = UUID.randomUUID();
        Location location = new Location(id, "SC", "Charleston", "1 Main St", "29401");

        assertEquals(id, location.getId());
    }

    @Test
    void isInStateCityZipMatchExactly() {
        Location location = new Location("SC", "Charleston", "1 Main St", "29401");

        assertTrue(location.isInState("SC"));
        assertFalse(location.isInState("NC"));
        assertTrue(location.isInCity("Charleston"));
        assertFalse(location.isInCity("Columbia"));
        assertTrue(location.isInZip("29401"));
        assertFalse(location.isInZip("29403"));
    }

    @Test
    void settersUpdateFields() {
        Location location = new Location();

        location.setState("NC");
        location.setCity("Raleigh");
        location.setStreet("2 Oak Ave");
        location.setZip("27601");

        assertEquals("NC", location.getState());
        assertEquals("Raleigh", location.getCity());
        assertEquals("2 Oak Ave", location.getStreet());
        assertEquals("27601", location.getZip());
    }

    @Test
    void toStringFormatsAsStreetCityStateZip() {
        Location location = new Location("SC", "Charleston", "1 Main St", "29401");

        assertEquals("1 Main St, Charleston, SC 29401", location.toString());
    }

    @Test
    void serializeDeserializeRoundTripThroughFile() {
        Location original = new Location("SC", "Charleston", "1 Main St", "29401");

        DataManager manager = new DataManager(tempDir.resolve("locations.json").toString());
        ArrayList<JSONObject> toWrite = new ArrayList<>();
        toWrite.add((JSONObject) original.serialize());
        assertTrue(manager.writeJSON(toWrite));

        ArrayList<JSONObject> readBack = manager.readJSON();
        assertNotNull(readBack);

        Location restored = new Location();
        restored.deserialize(readBack.get(0));

        assertEquals(original.getId(), restored.getId());
        assertEquals(original.getState(), restored.getState());
        assertEquals(original.getCity(), restored.getCity());
        assertEquals(original.getStreet(), restored.getStreet());
        assertEquals(original.getZip(), restored.getZip());
    }

    @Test
    void deserializeDefaultsNullZipTo00000() {
        Location restored = new Location();
        JSONObject json = new JSONObject();
        json.put("id", UUID.randomUUID().toString());
        json.put("state", "SC");
        json.put("city", "Charleston");
        json.put("street", "1 Main St");
        // "zip" intentionally omitted

        restored.deserialize(json);

        assertEquals("00000", restored.getZip());
    }

    // getCoordinates() calls out to MapTools, a real routing/geocoding
    // dependency with behavior (network access, API keys, etc.) that these
    // unit tests can't verify. It is intentionally not tested here.
}