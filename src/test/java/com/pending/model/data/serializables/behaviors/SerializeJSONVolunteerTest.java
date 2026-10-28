package com.pending.model.data.serializables.behaviors;

import com.pending.model.data.DataList;
import com.pending.model.data.Database;
import com.pending.model.data.serializables.ReliefRequest;
import com.pending.model.data.serializables.Serializable;
import com.pending.model.data.serializables.Volunteer;
import com.pending.model.enums.ReliefType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SerializeJSONVolunteerTest {

    private final ArrayList<Runnable> cleanupTasks = new ArrayList<>();

    @AfterEach
    void cleanUp() {
        cleanupTasks.forEach(Runnable::run);
        cleanupTasks.clear();
    }

    @SuppressWarnings("unchecked")
    private <T extends Serializable> void addToDatabase(Class<T> cls, T object) {
        ((DataList<T>) Database.getInstance().get(cls)).add(object);
        cleanupTasks.add(() -> ((DataList<T>) Database.getInstance().get(cls)).remove(object));
    }

    private ReliefRequest registeredRequest() {
        ReliefRequest request = new ReliefRequest();
        addToDatabase(ReliefRequest.class, request);
        return request;
    }

    @Test
    void serializeIncludesReliefTypesAndNullTaskWhenUnassigned() {
        Volunteer volunteer = new Volunteer();
        volunteer.addReliefType(ReliefType.MEDICAL_CARE);

        JSONObject json = new SerializeJSONVolunteer<>(volunteer).serialize();

        JSONArray reliefTypes = (JSONArray) json.get("relief_types");
        assertEquals(1, reliefTypes.size());
        assertEquals("MEDICAL_CARE", reliefTypes.get(0));
        assertTrue(json.containsKey("task"));
        assertNull(json.get("task"));
    }

    @Test
    void serializeIncludesTaskUUIDReferenceWhenAssigned() {
        Volunteer volunteer = new Volunteer();
        ReliefRequest request = new ReliefRequest();
        volunteer.setTask(request);
        volunteer.setTaskUUIDReference(request.getId());

        JSONObject json = new SerializeJSONVolunteer<>(volunteer).serialize();

        assertEquals(request.getId().toString(), json.get("task"));
    }

    @Test
    void serializeIncludesInheritedUserAndLivingBeingFields() {
        Volunteer volunteer = new Volunteer();

        JSONObject json = new SerializeJSONVolunteer<>(volunteer).serialize();

        assertTrue(json.containsKey("password"));
        assertTrue(json.containsKey("first_name"));
        assertEquals(volunteer.getId().toString(), json.get("id"));
    }

    @Test
    void deserializeRestoresReliefTypesAndTask() {
        UUID taskId = UUID.randomUUID();
        JSONObject json = baseVolunteerJson();
        JSONArray reliefTypes = new JSONArray();
        reliefTypes.add("TRANSPORTATION");
        json.put("relief_types", reliefTypes);
        json.put("task", taskId.toString());

        Volunteer volunteer = new Volunteer();
        new SerializeJSONVolunteer<>(volunteer).deserialize(json);

        assertEquals(1, volunteer.getReliefTypes().size());
        assertEquals(ReliefType.TRANSPORTATION, volunteer.getReliefTypes().get(0));
        assertEquals(taskId, volunteer.getTaskUUIDReference());
    }

    @Test
    void deserializeClearsExistingReliefTypesBeforeAdding() {
        Volunteer volunteer = new Volunteer();
        volunteer.addReliefType(ReliefType.OTHER);
        JSONObject json = baseVolunteerJson();
        json.put("relief_types", new JSONArray());
        json.put("task", null);

        new SerializeJSONVolunteer<>(volunteer).deserialize(json);

        assertTrue(volunteer.getReliefTypes().isEmpty());
    }

    @Test
    void deserializeWithMissingReliefTypesArrayDoesNotThrow() {
        JSONObject json = baseVolunteerJson();
        // "relief_types" intentionally omitted
        json.put("task", null);

        Volunteer volunteer = new Volunteer();

        assertDoesNotThrow(() -> new SerializeJSONVolunteer<>(volunteer).deserialize(json));
        assertTrue(volunteer.getReliefTypes().isEmpty());
    }

    /**
     * BUG: deserialize() only sets taskUUIDReference when "task" is non-null:
     *     if (jsonObject.get("task") != null) { serializable.setTaskUUIDReference(...); }
     * There is no else branch, so a volunteer that already had a (possibly
     * stale) taskUUIDReference keeps it even when the JSON says "task": null.
     * Every Volunteer constructor also sets a random, meaningless
     * taskUUIDReference by default, which makes this easy to trigger.
     */
    @Test
    void deserializeDoesNotClearTaskUUIDReferenceWhenTaskIsNull() {
        Volunteer volunteer = new Volunteer();
        UUID staleReference = volunteer.getTaskUUIDReference(); // set randomly by the constructor
        assertNotNull(staleReference);

        JSONObject json = baseVolunteerJson();
        json.put("task", null);

        new SerializeJSONVolunteer<>(volunteer).deserialize(json);

        assertEquals(staleReference, volunteer.getTaskUUIDReference(),
                "if this now fails (reference was cleared), the bug has been fixed and "
                        + "this test should assert getTaskUUIDReference() is null instead");
    }

    private JSONObject baseVolunteerJson() {
        Volunteer template = new Volunteer();
        JSONObject json = new SerializeJSONVolunteer<>(template).serialize();
        json.put("id", UUID.randomUUID().toString());
        return json;
    }

    @Test
    void resolveReferencesPopulatesTheAssignedTask() {
        ReliefRequest request = registeredRequest();
        Volunteer volunteer = new Volunteer();
        volunteer.setTaskUUIDReference(request.getId());

        new SerializeJSONVolunteer<>(volunteer).resolveReferences();

        assertSame(request, volunteer.getTask());
    }

    @Test
    void resolveReferencesSetsTaskNullWhenReferenceIsUnknown() {
        Volunteer volunteer = new Volunteer();
        volunteer.setTaskUUIDReference(UUID.randomUUID());

        new SerializeJSONVolunteer<>(volunteer).resolveReferences();

        assertNull(volunteer.getTask());
    }

    @Test
    void serializeThenDeserializeRoundTripsReliefTypes() {
        Volunteer original = new Volunteer();
        original.addReliefType(ReliefType.MEDICAL_CARE);
        original.addReliefType(ReliefType.TRANSPORTATION);

        JSONObject json = new SerializeJSONVolunteer<>(original).serialize();

        Volunteer restored = new Volunteer();
        new SerializeJSONVolunteer<>(restored).deserialize(json);

        assertEquals(List.of(ReliefType.MEDICAL_CARE, ReliefType.TRANSPORTATION), restored.getReliefTypes());
        assertEquals(original.getId(), restored.getId());
    }
}
