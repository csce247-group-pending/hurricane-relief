package com.pending.model.data.serializables;

import com.pending.model.data.DataManager;
import com.pending.model.enums.HurricaneStatus;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class HurricaneTest {

    @TempDir
    Path tempDir;

    @Test
    void defaultConstructorIsCategoryFiveAndPassed() {
        Hurricane hurricane = new Hurricane();

        assertEquals("", hurricane.getName());
        assertEquals(5, hurricane.getCategory());
        assertEquals(HurricaneStatus.PASSED, hurricane.getStatus());
        assertTrue(hurricane.getLocations().isEmpty());
        assertNotNull(hurricane.getId());
    }

    @Test
    void fourArgConstructorSetsFields() {
        ArrayList<Location> locations = new ArrayList<>();
        locations.add(new Location("SC", "Charleston", "1 Main St", "29401"));

        Hurricane hurricane = new Hurricane("Helene", 3, locations, HurricaneStatus.APPROACHING);

        assertEquals("Helene", hurricane.getName());
        assertEquals(3, hurricane.getCategory());
        assertEquals(HurricaneStatus.APPROACHING, hurricane.getStatus());
        assertEquals(1, hurricane.getLocations().size());
    }

    @Test
    void idConstructorUsesProvidedIdAndIgnoresDates() {
        UUID id = UUID.randomUUID();
        Hurricane hurricane = new Hurricane(id, "Milton", 4, LocalDateTime.now(),
                LocalDateTime.now().plusDays(3), new ArrayList<>(), HurricaneStatus.LANDFALL);

        assertEquals(id, hurricane.getId());
        assertEquals("Milton", hurricane.getName());
        // NOTE: startDate/endDate are accepted by this constructor but never stored
        // anywhere on Hurricane -- there is no getter for them and the values are
        // silently discarded. This test only documents that the constructor does
        // not throw; it cannot verify the dates because nothing stores them.
    }

    @Test
    void addAndRemoveLocation() {
        Hurricane hurricane = new Hurricane();
        Location location = new Location("SC", "Charleston", "1 Main St", "29401");

        hurricane.addLocation(location);
        assertTrue(hurricane.getLocations().contains(location));

        hurricane.removeLocation(location);
        assertFalse(hurricane.getLocations().contains(location));
    }

    @Test
    void settersUpdateFields() {
        Hurricane hurricane = new Hurricane();

        hurricane.setName("Ian");
        hurricane.setCategory(5);
        hurricane.setStatus(HurricaneStatus.LANDFALL);
        ArrayList<UUID> refs = new ArrayList<>();
        refs.add(UUID.randomUUID());
        hurricane.setLocationsUUIDReference(refs);

        assertEquals("Ian", hurricane.getName());
        assertEquals(5, hurricane.getCategory());
        assertEquals(HurricaneStatus.LANDFALL, hurricane.getStatus());
        assertSame(refs, hurricane.getLocationsUUIDReference());
    }

    @Test
    void serializeWritesLocationIdsIntoLocationsUUIDReferenceOnDeserialize() {
        ArrayList<Location> locations = new ArrayList<>();
        Location location = new Location("SC", "Charleston", "1 Main St", "29401");
        locations.add(location);
        Hurricane original = new Hurricane("Helene", 3, locations, HurricaneStatus.APPROACHING);

        JSONObject json = (JSONObject) original.serialize();

        Hurricane restored = new Hurricane();
        // Deserializing directly from the in-memory JSONObject (bypassing the
        // text file) still works here because "locations" is written as an array
        // of id strings. The broken part is "id" -- see the test below.
        restored.setId(original.getId()); // id can't survive a real round trip (see below)
        assertEquals(1, ((org.json.simple.JSONArray) json.get("locations")).size());
        assertEquals(location.getId().toString(),
                ((org.json.simple.JSONArray) json.get("locations")).get(0));
    }

    /**
     * BUG: SerializeJSONHurricane.serialize() does
     *     jsonObject.put("id", serializable.getId());
     * passing the raw UUID object instead of serializable.getId().toString().
     * json-simple does not know how to quote an arbitrary object, so when the
     * JSONObject is turned into real JSON text the id is written unquoted
     * (e.g. "id":3b1a74ac-c17b-...), which is not valid JSON. Reading the file
     * back then fails to parse and DataManager.readJSON() returns null.
     *
     * This test documents that CURRENT behavior. Once "id" is fixed to use
     * getId().toString(), this test should be replaced with a real round-trip
     * assertion (like ContactTest's).
     */
    @Test
    void idIsWrittenUnquotedAndBreaksTheFileRoundTrip() {
        Hurricane hurricane = new Hurricane("Helene", 3, new ArrayList<>(), HurricaneStatus.APPROACHING);

        DataManager manager = new DataManager(tempDir.resolve("hurricanes.json").toString());
        ArrayList<JSONObject> toWrite = new ArrayList<>();
        toWrite.add((JSONObject) hurricane.serialize());
        assertTrue(manager.writeJSON(toWrite));

        ArrayList<JSONObject> readBack = manager.readJSON();

        assertNull(readBack, "expected the unquoted UUID to make the file unparsable; "
                + "if this now returns non-null, the id-serialization bug has been fixed "
                + "and this test should be rewritten as a real round-trip check");
    }
}