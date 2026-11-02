package com.pending.model.data.serializables.behaviors;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

import com.pending.model.data.serializables.Shelter;
import com.pending.model.enums.ShelterStatus;

/*
 * AI Usage: Used AI to generate the test method assertions, boilerplate setup, and table formatting.
 *
 * +---------------------------------------------------------+-------------------------------------------------------------------+
 * | Test                                                    | Reasoning                                                         |
 * +---------------------------------------------------------+-------------------------------------------------------------------+
 * | testSerializeShelterWithEmptyOccupants                  | Verifies UUID list extraction when actual objects are empty.      |
 * | testSerializeShelterWithNullManager                     | Ensures manager ID correctly falls back to null without crashing. |
 * | testDeserializeShelterWithPetFriendly                   | Checks standard boolean extraction for pet_friendly field.        |
 * | testDeserializeShelterWithAccommodatesPetsFallback      | Verifies backward compatibility fallback to 'accommodates_pets'.  |
 * +---------------------------------------------------------+-------------------------------------------------------------------+
 */
class SerializeJSONShelterTest {

    private Shelter testShelter;
    private SerializeJSONShelter serializer;
    private UUID testId;

    @BeforeEach
    public void setUp() {
        testId = UUID.randomUUID();
        testShelter = new Shelter();
        testShelter.setId(testId);
        testShelter.setName("Community Center");
        testShelter.setCapacity(100);
        testShelter.setStatus(ShelterStatus.OPEN);
        testShelter.setLocationUUIDReference(UUID.randomUUID());
        testShelter.setContactUUIDReference(UUID.randomUUID());

        serializer = new SerializeJSONShelter(testShelter);
    }

    @Test
    public void testSerializeShelterWithEmptyOccupants() {
        UUID mockOccupantId = UUID.randomUUID();
        testShelter.getOccupantsUUIDReference().add(mockOccupantId);
        
        JSONObject jsonObject = serializer.serialize();
        JSONArray occupants = (JSONArray) jsonObject.get("occupants");

        // HARDCODED ASSERTIONS
        assertEquals(1, occupants.size(), "Should extract UUID from reference list when object list is empty.");
        assertEquals(mockOccupantId.toString(), occupants.get(0));
    }

    @Test
    public void testSerializeShelterWithNullManager() {
        testShelter.setManagerUUIDReference(null);
        JSONObject jsonObject = serializer.serialize();

        assertFalse(jsonObject.containsKey("manager"), "Should not include 'manager' key if reference is null.");
    }

    @Test
    public void testDeserializeShelterWithPetFriendly() {
        JSONObject mockJson = new JSONObject();
        mockJson.put("id", testId.toString());
        mockJson.put("name", "High School");
        mockJson.put("location", UUID.randomUUID().toString());
        mockJson.put("contact", UUID.randomUUID().toString());
        mockJson.put("capacity", 250L);
        mockJson.put("status", "FULL");
        mockJson.put("pet_friendly", true); // Modern key

        serializer.deserialize(mockJson);

        assertTrue(testShelter.isPetFriendly(), "Should correctly parse 'pet_friendly' boolean.");
        assertEquals(250, testShelter.getCapacity(), "Should cast Long capacity down to int.");
        assertEquals(ShelterStatus.FULL, testShelter.getStatus());
    }

    @Test
    public void testDeserializeShelterWithAccommodatesPetsFallback() {
        JSONObject mockJson = new JSONObject();
        mockJson.put("id", testId.toString());
        mockJson.put("name", "Middle School");
        mockJson.put("location", UUID.randomUUID().toString());
        mockJson.put("contact", UUID.randomUUID().toString());
        mockJson.put("capacity", 50L);
        mockJson.put("status", "OPEN");
        mockJson.put("pet_friendly", null);
        mockJson.put("accommodates_pets", true);

        serializer.deserialize(mockJson);

        assertTrue(testShelter.isPetFriendly(), "Should fallback and parse 'accommodates_pets' when 'pet_friendly' is null.");
    }
}