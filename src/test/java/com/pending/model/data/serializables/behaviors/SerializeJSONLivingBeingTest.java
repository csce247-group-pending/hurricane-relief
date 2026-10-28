package com.pending.model.data.serializables.behaviors;

import com.pending.model.data.serializables.LivingBeing;
import com.pending.model.enums.LivingBeingType;
import com.pending.model.enums.ReliefType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SerializeJSONLivingBeingTest {

    @Test
    void serializeWritesIdNameDobTypeAndNeeds() {
        LivingBeing being = new LivingBeing("Jane", "Doe", LocalDate.of(1990, 1, 1),
                LivingBeingType.HUMAN, new ArrayList<>(List.of(ReliefType.FOOD, ReliefType.WATER)));
        SerializeJSONLivingBeing<LivingBeing> behavior = new SerializeJSONLivingBeing<>(being);

        JSONObject json = behavior.serialize();

        assertEquals(being.getId().toString(), json.get("id"));
        assertEquals("Jane", json.get("first_name"));
        assertEquals("Doe", json.get("last_name"));
        assertEquals("1990-01-01", json.get("date_of_birth"));
        assertEquals("HUMAN", json.get("type"));
        JSONArray needs = (JSONArray) json.get("relief_needs");
        assertEquals(2, needs.size());
        assertTrue(needs.contains("FOOD"));
        assertTrue(needs.contains("WATER"));
    }

    @Test
    void serializeWritesAnEmptyArrayWhenThereAreNoReliefNeeds() {
        LivingBeing being = new LivingBeing();
        SerializeJSONLivingBeing<LivingBeing> behavior = new SerializeJSONLivingBeing<>(being);

        JSONObject json = behavior.serialize();

        assertTrue(((JSONArray) json.get("relief_needs")).isEmpty());
    }

    @Test
    void deserializeRestoresAllFields() {
        UUID id = UUID.randomUUID();
        JSONObject json = new JSONObject();
        json.put("id", id.toString());
        json.put("first_name", "Max");
        json.put("last_name", "Pup");
        json.put("date_of_birth", "2020-05-01");
        json.put("type", "PET");
        JSONArray needs = new JSONArray();
        needs.add("PET_SHELTER");
        json.put("relief_needs", needs);

        LivingBeing being = new LivingBeing();
        new SerializeJSONLivingBeing<>(being).deserialize(json);

        assertEquals(id, being.getId());
        assertEquals("Max", being.getFirstName());
        assertEquals("Pup", being.getLastName());
        assertEquals(LocalDate.of(2020, 5, 1), being.getDateOfBirth());
        assertEquals(LivingBeingType.PET, being.getType());
        assertEquals(1, being.getReliefNeeds().size());
        assertEquals(ReliefType.PET_SHELTER, being.getReliefNeeds().get(0));
    }

    @Test
    void deserializeClearsExistingReliefNeedsBeforeAdding() {
        LivingBeing being = new LivingBeing();
        being.addReliefNeed(ReliefType.OTHER);

        JSONObject json = new JSONObject();
        json.put("id", UUID.randomUUID().toString());
        json.put("first_name", "");
        json.put("last_name", "");
        json.put("date_of_birth", "2000-01-01");
        json.put("type", "HUMAN");
        json.put("relief_needs", new JSONArray());

        new SerializeJSONLivingBeing<>(being).deserialize(json);

        assertTrue(being.getReliefNeeds().isEmpty());
    }

    @Test
    void serializeThenDeserializeRoundTripsIncludingId() {
        LivingBeing original = new LivingBeing("Jane", "Doe", LocalDate.of(1990, 1, 1),
                LivingBeingType.HUMAN, new ArrayList<>(List.of(ReliefType.FOOD)));

        JSONObject json = new SerializeJSONLivingBeing<>(original).serialize();

        LivingBeing restored = new LivingBeing();
        new SerializeJSONLivingBeing<>(restored).deserialize(json);

        assertEquals(original.getId(), restored.getId());
        assertEquals(original.getFirstName(), restored.getFirstName());
        assertEquals(original.getLastName(), restored.getLastName());
        assertEquals(original.getDateOfBirth(), restored.getDateOfBirth());
        assertEquals(original.getType(), restored.getType());
        assertEquals(original.getReliefNeeds(), restored.getReliefNeeds());
    }
}
