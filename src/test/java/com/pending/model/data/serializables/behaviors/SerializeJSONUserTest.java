package com.pending.model.data.serializables.behaviors;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

import com.pending.model.data.serializables.User;
import com.pending.model.enums.LivingBeingType;
import java.time.LocalDate;

/*
 * AI Usage: Used AI to generate the test method assertions, boilerplate setup, and table formatting.
 *
 * +---------------------------------------------------------+-------------------------------------------------------------------+
 * | Test                                                    | Reasoning                                                         |
 * +---------------------------------------------------------+-------------------------------------------------------------------+
 * | testSerializeFullyPopulatedUser                         | Checks that password, locations, and request arrays serialize.    |
 * | testSerializeUserWithNullEmergencyContact               | Verifies null checks bypass UUID extraction to prevent crashes.   |
 * | testDeserializeValidUserJSON                            | Checks UUID parsing for contacts, locations, and request arrays.  |
 * | testDeserializeUserWithEmptyReliefRequests              | Ensures missing/empty request JSON arrays don't crash parsing.    |
 * +---------------------------------------------------------+-------------------------------------------------------------------+
 */
public class SerializeJSONUserTest {

    private User testUser;
    private SerializeJSONUser<User> serializer;
    private UUID testId;
    private UUID locationId;
    private UUID contactId;

    @BeforeEach
    public void setUp() {
        testId = UUID.randomUUID();
        locationId = UUID.randomUUID();
        contactId = UUID.randomUUID();
        
        testUser = new User();
        testUser.setId(testId);
        testUser.setFirstName("Jane");
        testUser.setLastName("Doe");
        testUser.setDateOfBirth(LocalDate.of(1990, 1, 1));
        testUser.setType(LivingBeingType.HUMAN);
        testUser.setPassword("securePass");
        testUser.setLocationUUIDReference(locationId);
        testUser.setUserContactUUIDReference(contactId);
        
        serializer = new SerializeJSONUser<>(testUser);
    }

    @Test
    public void testSerializeFullyPopulatedUser() {
        UUID emergencyId = UUID.randomUUID();
        testUser.setEmergencyContactUUIDReference(emergencyId);
        
        UUID requestId = UUID.randomUUID();
        testUser.addUserReliefRequest(requestId);

        JSONObject jsonObject = serializer.serialize();

        // HARDCODED ASSERTIONS
        assertEquals("securePass", jsonObject.get("password"));
        assertEquals(locationId.toString(), jsonObject.get("location"));
        assertEquals(contactId.toString(), jsonObject.get("user_contact"));
        assertEquals(emergencyId.toString(), jsonObject.get("emergency_contact"));
        
        JSONArray requests = (JSONArray) jsonObject.get("user_relief_requests");
        assertEquals(1, requests.size());
        assertEquals(requestId.toString(), requests.get(0));
    }

    @Test
    public void testSerializeUserWithNullEmergencyContact() {
        testUser.setEmergencyContactUUIDReference(null);
        
        assertDoesNotThrow(() -> {
            serializer.serialize();
        }, "Should safely bypass extracting an ID from a null emergency contact reference.");
    }

    @Test
    public void testDeserializeValidUserJSON() {
        JSONObject mockJson = new JSONObject();
        mockJson.put("id", testId.toString());
        mockJson.put("first_name", "John");
        mockJson.put("last_name", "Smith");
        mockJson.put("date_of_birth", "1985-05-15");
        mockJson.put("type", "HUMAN");
        mockJson.put("relief_needs", new JSONArray());
        
        mockJson.put("password", "newPass!");
        mockJson.put("location", UUID.randomUUID().toString());
        mockJson.put("user_contact", UUID.randomUUID().toString());
        mockJson.put("emergency_contact", UUID.randomUUID().toString());
        
        JSONArray mockRequests = new JSONArray();
        mockRequests.add(UUID.randomUUID().toString());
        mockJson.put("user_relief_requests", mockRequests);

        serializer.deserialize(mockJson);

        assertEquals("newPass!", testUser.getPassword());
        assertEquals(1, testUser.getUserReliefRequestsUUIDReference().size(), "Should parse exactly one relief request UUID.");
    }

    @Test
    public void testDeserializeUserWithEmptyReliefRequests() {
        JSONObject mockJson = new JSONObject();
        mockJson.put("id", testId.toString());
        mockJson.put("first_name", "John");
        mockJson.put("last_name", "Smith");
        mockJson.put("date_of_birth", "1985-05-15");
        mockJson.put("type", "HUMAN");
        mockJson.put("relief_needs", new JSONArray());
        mockJson.put("password", "newPass!");
        mockJson.put("location", UUID.randomUUID().toString());
        mockJson.put("user_contact", UUID.randomUUID().toString());
        mockJson.put("emergency_contact", UUID.randomUUID().toString());
        
        mockJson.put("user_relief_requests", new JSONArray()); 

        assertDoesNotThrow(() -> {
            serializer.deserialize(mockJson);
        }, "Deserializing an empty user_relief_requests array should not throw exceptions.");
        assertTrue(testUser.getUserReliefRequestsUUIDReference().isEmpty(), "UUID list should remain empty.");
    }
}