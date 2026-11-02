package com.pending.model.data.serializables.behaviors;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

import com.pending.model.data.serializables.Location;

/*
 * AI Usage: Used AI to generate the test method assertions, boilerplate setup, and table formatting.
 *
 * +---------------------------------------------------------+-------------------------------------------------------------------+
 * | Test                                                    | Reasoning                                                         |
 * +---------------------------------------------------------+-------------------------------------------------------------------+
 * | testSerializeFullyPopulatedLocation                     | Ensures complete location data writes accurately to JSON.         |
 * | testSerializeLocationWithNullStreetAndZip               | Tests branch ensuring null values default to empty strings/zeros. |
 * | testDeserializeLocation                                 | Ensures string fields and defaults map correctly from JSON.       |
 * +---------------------------------------------------------+-------------------------------------------------------------------+
 */
class SerializeJSONLocationTest {

    private Location testLocation;
    private SerializeJSONLocation serializer;
    private UUID testId;

    @BeforeEach
    public void setUp() {
        testId = UUID.randomUUID();
        testLocation = new Location("SC", "Charleston", "145 King St", "29401");
        testLocation.setId(testId);
        
        serializer = new SerializeJSONLocation(testLocation);
    }

    @Test
    public void testSerializeFullyPopulatedLocation() {
        JSONObject jsonObject = serializer.serialize();

        // HARDCODED ASSERTIONS
        assertEquals("145 King St", jsonObject.get("street"));
        assertEquals("29401", jsonObject.get("zip"));
        assertEquals("Charleston", jsonObject.get("city"));
        assertEquals("SC", jsonObject.get("state"));
    }

    @Test
    public void testSerializeLocationWithNullStreetAndZip() {
        testLocation.setStreet(null);
        testLocation.setZip(null);

        JSONObject jsonObject = serializer.serialize();

        // HARDCODED ASSERTIONS: Verify exact fallback values from SerializeJSONLocation.java
        assertEquals("", jsonObject.get("street"), "A null street should serialize as an empty string.");
        assertEquals("00000", jsonObject.get("zip"), "A null zip should serialize to default '00000'.");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDeserializeLocation() {
        JSONObject mockJson = new JSONObject();
        UUID mockId = UUID.randomUUID();
        
        mockJson.put("id", mockId.toString());
        mockJson.put("state", "NC");
        mockJson.put("city", "Charlotte");
        mockJson.put("street", "123 Test Ave");
        mockJson.put("zip", null); 

        serializer.deserialize(mockJson);

        // HARDCODED ASSERTIONS
        assertEquals("NC", testLocation.getState());
        assertEquals("Charlotte", testLocation.getCity());
        assertEquals("123 Test Ave", testLocation.getStreet());
        assertEquals("00000", testLocation.getZip(), "A null zip in JSON should deserialize to default '00000'.");
    }
}