package com.pending.model.data.serializables.behaviors;

import com.pending.model.data.serializables.Location;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SerializeJSONLocationTest {

    @Test
    void serializeWritesAllFieldsAsStrings() {
        Location location = new Location("SC", "Charleston", "1 Main St", "29401");
        SerializeJSONLocation behavior = new SerializeJSONLocation(location);

        JSONObject json = behavior.serialize();

        assertEquals(location.getId().toString(), json.get("id"));
        assertEquals("SC", json.get("state"));
        assertEquals("Charleston", json.get("city"));
        assertEquals("1 Main St", json.get("street"));
        assertEquals("29401", json.get("zip"));
    }

    @Test
    void serializeDefaultsNullStreetToEmptyString() {
        Location location = new Location("SC", "Charleston", null, "29401");
        SerializeJSONLocation behavior = new SerializeJSONLocation(location);

        JSONObject json = behavior.serialize();

        assertEquals("", json.get("street"));
    }

    @Test
    void serializeDefaultsNullZipTo00000() {
        Location location = new Location("SC", "Charleston", "1 Main St", null);
        SerializeJSONLocation behavior = new SerializeJSONLocation(location);

        JSONObject json = behavior.serialize();

        assertEquals("00000", json.get("zip"));
    }

    @Test
    void deserializeRestoresAllFields() {
        UUID id = UUID.randomUUID();
        JSONObject json = new JSONObject();
        json.put("id", id.toString());
        json.put("state", "NC");
        json.put("city", "Raleigh");
        json.put("street", "2 Oak Ave");
        json.put("zip", "27601");

        Location location = new Location();
        new SerializeJSONLocation(location).deserialize(json);

        assertEquals(id, location.getId());
        assertEquals("NC", location.getState());
        assertEquals("Raleigh", location.getCity());
        assertEquals("2 Oak Ave", location.getStreet());
        assertEquals("27601", location.getZip());
    }

    @Test
    void deserializeDefaultsMissingZipTo00000() {
        JSONObject json = new JSONObject();
        json.put("id", UUID.randomUUID().toString());
        json.put("state", "SC");
        json.put("city", "Charleston");
        json.put("street", "1 Main St");
        // "zip" intentionally omitted

        Location location = new Location();
        new SerializeJSONLocation(location).deserialize(json);

        assertEquals("00000", location.getZip());
    }

    @Test
    void serializeThenDeserializeRoundTrips() {
        Location original = new Location("SC", "Charleston", "1 Main St", "29401");

        JSONObject json = new SerializeJSONLocation(original).serialize();

        Location restored = new Location();
        new SerializeJSONLocation(restored).deserialize(json);

        assertEquals(original.getId(), restored.getId());
        assertEquals(original.getState(), restored.getState());
        assertEquals(original.getCity(), restored.getCity());
        assertEquals(original.getStreet(), restored.getStreet());
        assertEquals(original.getZip(), restored.getZip());
    }
}
