package com.pending.model.data.serializables.behaviors;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

import com.pending.model.data.serializables.ReliefRequest;
import com.pending.model.data.serializables.LivingBeing;
import com.pending.model.enums.ReliefStatus;
import com.pending.model.enums.ReliefType;
import com.pending.model.enums.LivingBeingType;
import java.time.LocalDate;

/*
 * AI Usage: Used AI to generate the test method assertions, boilerplate setup, and table formatting.
 *
 * +---------------------------------------------------------+-------------------------------------------------------------------+
 * | Test                                                    | Reasoning                                                         |
 * +---------------------------------------------------------+-------------------------------------------------------------------+
 * | testSerializeFullyPopulatedReliefRequest                | Ensures all populated fields and object arrays serialize properly.|
 * | testSerializeReliefRequestWithNullAssignedTo            | Verifies the branch where assignedTo is null safely skips it.     |
 * | testDeserializeValidReliefRequestJSON                   | Checks Enum parsing and UUID list extraction from JSON.           |
 * | testDeserializeReliefRequestWithNullLivingBeings        | Ensures a missing living_beings array does not cause a crash.     |
 * +---------------------------------------------------------+-------------------------------------------------------------------+
 */
class SerializeJSONReliefRequestTest {

    private ReliefRequest testRequest;
    private SerializeJSONReliefRequest serializer;
    private UUID testId;
    private UUID locationId;
    private UUID contactId;

    @BeforeEach
    public void setUp() {
        testId = UUID.randomUUID();
        locationId = UUID.randomUUID();
        contactId = UUID.randomUUID();

        testRequest = new ReliefRequest();
        testRequest.setId(testId);
        testRequest.setDescription("Need supplies");
        testRequest.setStatus(ReliefStatus.REQUESTED);
        testRequest.setLocationUUIDReference(locationId);
        testRequest.setContactUUIDReference(contactId);

        ArrayList<ReliefType> types = new ArrayList<>();
        types.add(ReliefType.FOOD);
        testRequest.setTypes(types);

        serializer = new SerializeJSONReliefRequest(testRequest);
    }

    @Test
    public void testSerializeFullyPopulatedReliefRequest() {
        LivingBeing person = new LivingBeing("John", "Doe", LocalDate.of(1990, 1, 1), LivingBeingType.HUMAN, new ArrayList<>());
        UUID personId = UUID.randomUUID();
        person.setId(personId);
        testRequest.addLivingBeing(person);

        JSONObject jsonObject = serializer.serialize();

        // HARDCODED ASSERTIONS
        assertEquals("Need supplies", jsonObject.get("description"));
        assertEquals("REQUESTED", jsonObject.get("status"), "Enum should serialize exactly to its string name.");
        assertEquals(locationId.toString(), jsonObject.get("location"));
        assertNull(jsonObject.get("assigned_to"), "assigned_to should not be put into the JSON if it is null.");

        JSONArray livingBeings = (JSONArray) jsonObject.get("living_beings");
        assertEquals(1, livingBeings.size(), "Should contain exactly one living being UUID string.");
        assertEquals(personId.toString(), livingBeings.get(0));
    }

    @Test
    public void testSerializeReliefRequestWithNullAssignedTo() {
        testRequest.setAssignedToUUIDReference(null);
        JSONObject jsonObject = serializer.serialize();
        
        assertFalse(jsonObject.containsKey("assigned_to"), "The 'assigned_to' key should not exist if the reference is null.");
    }

    @Test
    public void testDeserializeValidReliefRequestJSON() {
        JSONObject mockJson = new JSONObject();
        UUID mockId = UUID.randomUUID();
        UUID locId = UUID.randomUUID();
        UUID contId = UUID.randomUUID();
        
        mockJson.put("id", mockId.toString());
        mockJson.put("status", "ASSIGNED");
        mockJson.put("description", "Needs rescue");
        mockJson.put("location", locId.toString());
        mockJson.put("contact", contId.toString());
        
        JSONArray types = new JSONArray();
        types.add("MEDICAL_CARE");
        mockJson.put("types", types);

        JSONArray beings = new JSONArray();
        beings.add(UUID.randomUUID().toString());
        mockJson.put("living_beings", beings);

        serializer.deserialize(mockJson);

        // HARDCODED ASSERTIONS
        assertEquals(mockId, testRequest.getId());
        assertEquals("Needs rescue", testRequest.getDescription());
        assertEquals(ReliefStatus.ASSIGNED, testRequest.getStatus(), "Should parse exact enum string back to Enum.");
        assertEquals(locId, testRequest.getLocationUUIDReference());
        assertEquals(contId, testRequest.getContactUUIDReference());
        assertTrue(testRequest.getTypes().contains(ReliefType.MEDICAL_CARE), "Should parse JSON array to Enum list.");
        assertEquals(1, testRequest.getLivingBeingsUUIDReference().size(), "Should parse exactly one UUID into the reference list.");
    }

    @Test
    public void testDeserializeReliefRequestWithNullLivingBeings() {
        JSONObject mockJson = new JSONObject();
        mockJson.put("id", UUID.randomUUID().toString());
        mockJson.put("status", "COMPLETED");
        mockJson.put("description", "Done");
        mockJson.put("location", UUID.randomUUID().toString());
        mockJson.put("contact", UUID.randomUUID().toString());
        mockJson.put("types", new JSONArray());
        mockJson.put("living_beings", null);

        assertDoesNotThrow(() -> {
            serializer.deserialize(mockJson);
        }, "Deserializing with a null 'living_beings' array should not throw NullPointerException.");
    }
}