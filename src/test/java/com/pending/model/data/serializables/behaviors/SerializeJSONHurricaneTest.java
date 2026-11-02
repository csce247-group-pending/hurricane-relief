package com.pending.model.data.serializables.behaviors;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

import com.pending.model.data.serializables.Hurricane;
import com.pending.model.data.serializables.Location;
import com.pending.model.enums.HurricaneStatus;

/*
 * AI Usage: Used AI to generate the test method assertions, boilerplate setup, and table formatting.
 *
 * +---------------------------------------------------------+-------------------------------------------------------------------+
 * | Test                                                    | Reasoning                                                         |
 * +---------------------------------------------------------+-------------------------------------------------------------------+
 * | testSerializeHurricaneWithEmptyLocations                | Tests the branch formatting JSON arrays from UUID references.     |
 * | testSerializeHurricaneWithPopulatedLocations            | Tests the branch formatting JSON arrays from initialized objects. |
 * | testDeserializeHurricane                                | Verifies numbers, enums, and JSON arrays parse into properties.   |
 * +---------------------------------------------------------+-------------------------------------------------------------------+
 */
class SerializeJSONHurricaneTest {

    private Hurricane testHurricane;
    private SerializeJSONHurricane serializer;
    private UUID testId;

    @BeforeEach
    public void setUp() {
        testId = UUID.randomUUID();
        testHurricane = new Hurricane(); 
        testHurricane.setId(testId);
        testHurricane.setName("Hugo");
        testHurricane.setCategory(5);
        testHurricane.setStatus(HurricaneStatus.APPROACHING);
        
        serializer = new SerializeJSONHurricane(testHurricane);
    }

    @Test
    public void testSerializeHurricaneWithEmptyLocations() {
        UUID locId = UUID.randomUUID();
        testHurricane.getLocationsUUIDReference().add(locId);

        JSONObject jsonObject = serializer.serialize();
        JSONArray locationsArray = (JSONArray) jsonObject.get("locations");

        assertEquals(1, locationsArray.size(), "Should serialize exactly one location reference.");
        assertEquals(locId.toString(), locationsArray.get(0), "Should extract the string directly from the UUID reference list.");
    }

    @Test
    public void testSerializeHurricaneWithPopulatedLocations() {
        Location loc = new Location();
        UUID locId = UUID.randomUUID();
        loc.setId(locId);
        testHurricane.getLocations().add(loc);

        JSONObject jsonObject = serializer.serialize();
        JSONArray locationsArray = (JSONArray) jsonObject.get("locations");

        assertEquals(1, locationsArray.size(), "Should serialize exactly one location object.");
        assertEquals(locId.toString(), locationsArray.get(0), "Should extract the string from the resolved Location object.");
    }

    @Test
    public void testDeserializeHurricane() {
        JSONObject mockJson = new JSONObject();
        UUID mockId = UUID.randomUUID();
        UUID mockLocId = UUID.randomUUID();
        
        mockJson.put("id", mockId.toString());
        mockJson.put("name", "Florence");
        mockJson.put("category", 4L); 
        mockJson.put("status", "PASSED");
        
        JSONArray locArray = new JSONArray();
        locArray.add(mockLocId.toString());
        mockJson.put("locations", locArray);

        serializer.deserialize(mockJson);

        // HARDCODED ASSERTIONS
        assertEquals("Florence", testHurricane.getName());
        assertEquals(4, testHurricane.getCategory(), "Should cast the JSON Long down to an int.");
        assertEquals(HurricaneStatus.PASSED, testHurricane.getStatus(), "Should parse exact enum string.");
        assertTrue(testHurricane.getLocationsUUIDReference().contains(mockLocId), "Should parse the JSONArray into the UUID list.");
    }
}