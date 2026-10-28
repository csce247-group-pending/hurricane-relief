package com.pending.model.data.serializables.behaviors;

import com.pending.model.data.DataList;
import com.pending.model.data.Database;
import com.pending.model.data.serializables.Serializable;
import com.pending.model.data.serializables.Shelter;
import com.pending.model.data.serializables.ShelterManager;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SerializeJSONShelterManagerTest {

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

    private Shelter registeredShelter() {
        Shelter shelter = new Shelter();
        addToDatabase(Shelter.class, shelter);
        return shelter;
    }

    @Test
    void serializeIncludesShelterIdWhenShelterObjectIsSet() {
        ShelterManager manager = new ShelterManager();
        Shelter shelter = new Shelter();
        manager.setShelter(shelter);

        JSONObject json = new SerializeJSONShelterManager<>(manager).serialize();

        assertEquals(shelter.getId().toString(), json.get("shelter"));
    }

    @Test
    void serializeFallsBackToShelterUUIDReferenceWhenShelterObjectIsNull() {
        ShelterManager manager = new ShelterManager();
        UUID shelterRef = UUID.randomUUID();
        manager.setShelterUUIDReference(shelterRef);

        JSONObject json = new SerializeJSONShelterManager<>(manager).serialize();

        assertEquals(shelterRef.toString(), json.get("shelter"));
    }

    @Test
    void serializeWritesNullShelterWhenThereIsNeitherObjectNorReference() {
        ShelterManager manager = new ShelterManager();
        manager.setShelterUUIDReference(null);

        JSONObject json = new SerializeJSONShelterManager<>(manager).serialize();

        assertTrue(json.containsKey("shelter"));
        assertNull(json.get("shelter"));
    }

    @Test
    void serializeIncludesInheritedVolunteerAndUserFields() {
        ShelterManager manager = new ShelterManager();

        JSONObject json = new SerializeJSONShelterManager<>(manager).serialize();

        assertTrue(json.containsKey("password"));
        assertTrue(json.containsKey("location"));
        assertTrue(json.containsKey("relief_types"));
        assertTrue(json.containsKey("task"));
    }

    @Test
    void deserializeRestoresTheShelterUUIDReference() {
        UUID shelterId = UUID.randomUUID();
        JSONObject json = baseManagerJson();
        json.put("shelter", shelterId.toString());

        ShelterManager manager = new ShelterManager();
        new SerializeJSONShelterManager<>(manager).deserialize(json);

        assertEquals(shelterId, manager.getShelterUUIDReference());
    }

    @Test
    void deserializeWithNullShelterClearsTheReference() {
        ShelterManager manager = new ShelterManager();
        manager.setShelterUUIDReference(UUID.randomUUID());
        JSONObject json = baseManagerJson();
        // "shelter" intentionally omitted

        new SerializeJSONShelterManager<>(manager).deserialize(json);

        assertNull(manager.getShelterUUIDReference());
    }

    private JSONObject baseManagerJson() {
        ShelterManager template = new ShelterManager();
        JSONObject json = new SerializeJSONShelterManager<>(template).serialize();
        json.put("id", UUID.randomUUID().toString());
        return json;
    }

    @Test
    void resolveReferencesPopulatesTheManagedShelter() {
        Shelter shelter = registeredShelter();
        ShelterManager manager = new ShelterManager();
        manager.setShelterUUIDReference(shelter.getId());

        new SerializeJSONShelterManager<>(manager).resolveReferences();

        assertSame(shelter, manager.getShelter());
    }

    @Test
    void resolveReferencesLeavesShelterNullWhenNoReference() {
        ShelterManager manager = new ShelterManager();
        manager.setShelterUUIDReference(null);

        assertDoesNotThrow(() -> new SerializeJSONShelterManager<>(manager).resolveReferences());
        assertNull(manager.getShelter());
    }

    @Test
    void serializeThenDeserializeRoundTripsTheShelterReference() {
        Shelter shelter = new Shelter();
        ShelterManager original = new ShelterManager();
        original.setShelter(shelter);

        JSONObject json = new SerializeJSONShelterManager<>(original).serialize();

        ShelterManager restored = new ShelterManager();
        new SerializeJSONShelterManager<>(restored).deserialize(json);

        assertEquals(shelter.getId(), restored.getShelterUUIDReference());
    }
}
