package com.pending.model.data.serializables.behaviors;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

import com.pending.model.data.serializables.Volunteer;
import com.pending.model.enums.ReliefType;

/*
 * AI Usage: Used AI to generate the test method assertions, boilerplate setup, and table formatting.
 *
 * +---------------------------------------------------------+-------------------------------------------------------------------+
 * | Test                                                    | Reasoning                                                         |
 * +---------------------------------------------------------+-------------------------------------------------------------------+
 * | testSerializeVolunteerWithNullTask                      | Ensures null tasks serialize exactly to null in JSON.             |
 * | testSerializeVolunteerWithReliefTypes                   | Validates enums are converted and added to the skills JSONArray.  |
 * | testDeserializeVolunteer                                | Ensures task UUID and skill array strings parse successfully.     |
 * +---------------------------------------------------------+-------------------------------------------------------------------+
 */
class SerializeJSONVolunteerTest {

    private Volunteer testVolunteer;
    private SerializeJSONVolunteer<Volunteer> serializer;

    @BeforeEach
    public void setUp() {
        testVolunteer = new Volunteer();
        testVolunteer.setId(UUID.randomUUID());
        testVolunteer.setLocationUUIDReference(UUID.randomUUID());
        testVolunteer.setUserContactUUIDReference(UUID.randomUUID());
        serializer = new SerializeJSONVolunteer<>(testVolunteer);
    }

    @Test
    public void testSerializeVolunteerWithNullTask() {
        testVolunteer.setTaskUUIDReference(null);
        JSONObject jsonObject = serializer.serialize();

        assertNull(jsonObject.get("task"), "A null task reference should put explicit null into JSON 'task' key.");
    }

    @Test
    public void testSerializeVolunteerWithReliefTypes() {
        testVolunteer.addReliefType(ReliefType.TRANSPORTATION);
        testVolunteer.addReliefType(ReliefType.MEDICAL_CARE);
        
        JSONObject jsonObject = serializer.serialize();
        JSONArray typesArray = (JSONArray) jsonObject.get("relief_types");

        // HARDCODED ASSERTIONS
        assertEquals(2, typesArray.size(), "Should contain exactly two serialized skills.");
        assertTrue(typesArray.contains("TRANSPORTATION"));
        assertTrue(typesArray.contains("MEDICAL_CARE"));
    }

    @Test
    public void testDeserializeVolunteer() {
        JSONObject mockJson = new JSONObject();
        UUID mockTaskId = UUID.randomUUID();
        
        mockJson.put("id", UUID.randomUUID().toString());
        mockJson.put("first_name", "Rosa");
        mockJson.put("last_name", "Delgado");
        mockJson.put("date_of_birth", "1995-05-15");
        mockJson.put("type", "HUMAN");
        mockJson.put("relief_needs", new JSONArray());
        mockJson.put("password", "pass");
        mockJson.put("location", UUID.randomUUID().toString());
        mockJson.put("user_contact", UUID.randomUUID().toString());
        mockJson.put("emergency_contact", UUID.randomUUID().toString());
        mockJson.put("user_relief_requests", new JSONArray());
        
        JSONArray skills = new JSONArray();
        skills.add("TRANSPORTATION");
        mockJson.put("relief_types", skills);
        mockJson.put("task", mockTaskId.toString());

        serializer.deserialize(mockJson);

        // HARDCODED ASSERTIONS
        assertEquals(1, testVolunteer.getReliefTypes().size());
        assertEquals(ReliefType.TRANSPORTATION, testVolunteer.getReliefTypes().get(0), "Should parse string to ReliefType Enum.");
        assertEquals(mockTaskId, testVolunteer.getTaskUUIDReference(), "Should successfully parse task UUID string.");
    }
}