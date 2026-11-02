package com.pending.model.data.serializables.behaviors;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

import com.pending.model.data.serializables.LivingBeing;
import com.pending.model.enums.LivingBeingType;
import com.pending.model.enums.ReliefType;

/*
 * AI Usage: Used AI to generate the test method assertions, boilerplate setup, and table formatting.
 *
 * +---------------------------------------------------------+-------------------------------------------------------------------+
 * | Test                                                    | Reasoning                                                         |
 * +---------------------------------------------------------+-------------------------------------------------------------------+
 * | testSerializeLivingBeing                                | Ensures LocalDate and enum array fields format cleanly for JSON.  |
 * | testDeserializeLivingBeing                              | Verifies JSON string arrays parse correctly back to enums.        |
 * +---------------------------------------------------------+-------------------------------------------------------------------+
 */
class SerializeJSONLivingBeingTest {

    private LivingBeing testLivingBeing;
    private SerializeJSONLivingBeing<LivingBeing> serializer;
    private UUID testId;

    @BeforeEach
    public void setUp() {
        testId = UUID.randomUUID();
        ArrayList<ReliefType> needs = new ArrayList<>();
        needs.add(ReliefType.WATER);
        
        testLivingBeing = new LivingBeing("Test", "Being", LocalDate.of(1990, 1, 1), LivingBeingType.HUMAN, needs);
        testLivingBeing.setId(testId);
        
        serializer = new SerializeJSONLivingBeing<>(testLivingBeing);
    }

    @Test
    public void testSerializeLivingBeing() {
        JSONObject jsonObject = serializer.serialize();

        // HARDCODED ASSERTIONS
        assertEquals("1990-01-01", jsonObject.get("date_of_birth"), "LocalDate should serialize strictly to ISO-8601 YYYY-MM-DD string.");
        assertEquals("HUMAN", jsonObject.get("type"), "LivingBeingType enum should serialize to string.");
        
        JSONArray reliefNeeds = (JSONArray) jsonObject.get("relief_needs");
        assertEquals(1, reliefNeeds.size(), "Should contain exactly one relief need.");
        assertEquals("WATER", reliefNeeds.get(0), "ReliefType should serialize to exact string.");
    }

    @Test
    public void testDeserializeLivingBeing() {
        JSONObject mockJson = new JSONObject();
        UUID mockId = UUID.randomUUID();
        
        mockJson.put("id", mockId.toString());
        mockJson.put("first_name", "Jane");
        mockJson.put("last_name", "Doe");
        mockJson.put("date_of_birth", "2005-10-31");
        mockJson.put("type", "PET");
        
        JSONArray mockNeeds = new JSONArray();
        mockNeeds.add("FOOD");
        mockNeeds.add("SHELTER");
        mockJson.put("relief_needs", mockNeeds);

        serializer.deserialize(mockJson);

        // HARDCODED ASSERTIONS
        assertEquals(LocalDate.of(2005, 10, 31), testLivingBeing.getDateOfBirth(), "Should successfully parse YYYY-MM-DD string back to LocalDate.");
        assertEquals(LivingBeingType.PET, testLivingBeing.getType());
        assertEquals(2, testLivingBeing.getReliefNeeds().size(), "Should parse all elements from the JSONArray.");
        assertTrue(testLivingBeing.getReliefNeeds().contains(ReliefType.FOOD));
        assertTrue(testLivingBeing.getReliefNeeds().contains(ReliefType.SHELTER));
    }
}