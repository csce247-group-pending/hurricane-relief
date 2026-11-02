package com.pending.model.data.serializables.behaviors;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

import com.pending.model.data.serializables.ShelterManager;

/*
 * AI Usage: Used AI to generate the test method assertions, boilerplate setup, and table formatting.
 *
 * +---------------------------------------------------------+-------------------------------------------------------------------+
 * | Test                                                    | Reasoning                                                         |
 * +---------------------------------------------------------+-------------------------------------------------------------------+
 * | testSerializeManagerWithNullShelter                     | Verifies fallback to null when manager has no assigned shelter.   |
 * | testSerializeManagerWithShelter                         | Verifies UUID string is properly appended for active shelters.    |
 * | testDeserializeShelterManager                           | Ensures shelter UUID string parses back to a UUID reference.      |
 * +---------------------------------------------------------+-------------------------------------------------------------------+
 */
class SerializeJSONShelterManagerTest {
    
    private ShelterManager testManager;
    private SerializeJSONShelterManager<ShelterManager> serializer;

    @BeforeEach
    public void setUp() {
        testManager = new ShelterManager();
        testManager.setId(UUID.randomUUID());
        testManager.setLocationUUIDReference(UUID.randomUUID());
        testManager.setUserContactUUIDReference(UUID.randomUUID());
        serializer = new SerializeJSONShelterManager<>(testManager);
    }

    @Test
    public void testSerializeManagerWithNullShelter() {
        testManager.setShelterUUIDReference(null);
        JSONObject jsonObject = serializer.serialize();

        assertNull(jsonObject.get("shelter"), "The 'shelter' key should map to null if manager is unassigned.");
    }

    @Test
    public void testSerializeManagerWithShelter() {
        UUID shelterId = UUID.randomUUID();
        testManager.setShelterUUIDReference(shelterId);
        
        JSONObject jsonObject = serializer.serialize();

        // HARDCODED ASSERTION
        assertEquals(shelterId.toString(), jsonObject.get("shelter"), "Should extract UUID string for the shelter reference.");
    }

    @Test
    public void testDeserializeShelterManager() {
        JSONObject mockJson = new JSONObject();
        UUID mockShelterId = UUID.randomUUID();
        
        mockJson.put("id", UUID.randomUUID().toString());
        mockJson.put("first_name", "Felix");
        mockJson.put("last_name", "Mendelssohn");
        mockJson.put("date_of_birth", "1980-01-01");
        mockJson.put("type", "HUMAN");
        mockJson.put("relief_needs", new org.json.simple.JSONArray());
        mockJson.put("password", "pass");
        mockJson.put("location", UUID.randomUUID().toString());
        mockJson.put("user_contact", UUID.randomUUID().toString());
        mockJson.put("emergency_contact", UUID.randomUUID().toString());
        mockJson.put("user_relief_requests", new org.json.simple.JSONArray());
        
        mockJson.put("shelter", mockShelterId.toString());

        serializer.deserialize(mockJson);

        assertEquals(mockShelterId, testManager.getShelterUUIDReference(), "Should successfully parse shelter UUID string.");
    }
}