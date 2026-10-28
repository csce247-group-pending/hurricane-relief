package com.pending.model.data.serializables.behaviors;

import com.pending.model.data.DataList;
import com.pending.model.data.Database;
import com.pending.model.data.serializables.Hurricane;
import com.pending.model.data.serializables.Location;
import com.pending.model.data.serializables.Serializable;
import com.pending.model.enums.HurricaneStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SerializeJSONHurricaneTest {

    private final ArrayList<Location> addedLocations = new ArrayList<>();

    @AfterEach
    void cleanUp() {
        for (Location location : addedLocations) {
            removeFromDatabase(Location.class, location);
        }
        addedLocations.clear();
    }

    @SuppressWarnings("unchecked")
    private static <T extends Serializable> void addToDatabase(Class<T> cls, T object) {
        ((DataList<T>) Database.getInstance().get(cls)).add(object);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Serializable> void removeFromDatabase(Class<T> cls, T object) {
        ((DataList<T>) Database.getInstance().get(cls)).remove(object);
    }

    private Location registeredLocation() {
        Location location = new Location("SC", "Charleston", "1 Main St", "29401");
        addToDatabase(Location.class, location);
        addedLocations.add(location);
        return location;
    }

    @Test
    void serializeWritesIdAsAQuotedString() {
        Hurricane hurricane = new Hurricane("Helene", 3, new ArrayList<>(), HurricaneStatus.APPROACHING);
        SerializeJSONHurricane behavior = new SerializeJSONHurricane(hurricane);

        JSONObject json = behavior.serialize();

        assertEquals(hurricane.getId().toString(), json.get("id"));
        assertTrue(json.get("id") instanceof String, "id should be written as a String, not a raw UUID");
    }

    @Test
    void serializeUsesLocationObjectsWhenPresent() {
        Location location = new Location("SC", "Charleston", "1 Main St", "29401");
        ArrayList<Location> locations = new ArrayList<>(java.util.List.of(location));
        Hurricane hurricane = new Hurricane("Helene", 3, locations, HurricaneStatus.APPROACHING);
        SerializeJSONHurricane behavior = new SerializeJSONHurricane(hurricane);

        JSONObject json = behavior.serialize();

        JSONArray jsonLocations = (JSONArray) json.get("locations");
        assertEquals(1, jsonLocations.size());
        assertEquals(location.getId().toString(), jsonLocations.get(0));
    }

    @Test
    void serializeFallsBackToUUIDReferencesWhenLocationsListIsEmpty() {
        Hurricane hurricane = new Hurricane("Helene", 3, new ArrayList<>(), HurricaneStatus.APPROACHING);
        UUID refId = UUID.randomUUID();
        hurricane.getLocationsUUIDReference().add(refId);
        SerializeJSONHurricane behavior = new SerializeJSONHurricane(hurricane);

        JSONObject json = behavior.serialize();

        JSONArray jsonLocations = (JSONArray) json.get("locations");
        assertEquals(1, jsonLocations.size());
        assertEquals(refId.toString(), jsonLocations.get(0));
    }

    @Test
    void deserializeRestoresIdNameCategoryStatusAndLocationReferences() {
        UUID id = UUID.randomUUID();
        UUID locationId = UUID.randomUUID();
        JSONObject json = new JSONObject();
        json.put("id", id.toString());
        json.put("name", "Milton");
        json.put("category", 4L);
        json.put("status", "LANDFALL");
        JSONArray locations = new JSONArray();
        locations.add(locationId.toString());
        json.put("locations", locations);

        Hurricane hurricane = new Hurricane();
        new SerializeJSONHurricane(hurricane).deserialize(json);

        assertEquals(id, hurricane.getId());
        assertEquals("Milton", hurricane.getName());
        assertEquals(4, hurricane.getCategory());
        assertEquals(HurricaneStatus.LANDFALL, hurricane.getStatus());
        assertEquals(1, hurricane.getLocationsUUIDReference().size());
        assertEquals(locationId, hurricane.getLocationsUUIDReference().get(0));
    }

    @Test
    void deserializeDoesNotClearStaleLocationObjects() {
        // deserialize() only refills locationsUUIDReference; the Location object
        // list (getLocations()) is left untouched until resolveReferences() runs.
        Location stale = new Location("SC", "Charleston", "1 Main St", "29401");
        Hurricane hurricane = new Hurricane("Helene", 3,
                new ArrayList<>(java.util.List.of(stale)), HurricaneStatus.APPROACHING);

        JSONObject json = new JSONObject();
        json.put("id", UUID.randomUUID().toString());
        json.put("name", "Milton");
        json.put("category", 4L);
        json.put("status", "LANDFALL");
        json.put("locations", new JSONArray());

        new SerializeJSONHurricane(hurricane).deserialize(json);

        assertTrue(hurricane.getLocations().contains(stale),
                "deserialize alone should not clear getLocations(); that happens in resolveReferences()");
    }

    @Test
    void resolveReferencesPopulatesLocationsFromTheDatabase() {
        Location location = registeredLocation();
        Hurricane hurricane = new Hurricane();
        hurricane.getLocationsUUIDReference().add(location.getId());

        new SerializeJSONHurricane(hurricane).resolveReferences();

        assertEquals(1, hurricane.getLocations().size());
        assertSame(location, hurricane.getLocations().get(0));
    }

    @Test
    void resolveReferencesSkipsLocationIdsNotFoundInTheDatabase() {
        Hurricane hurricane = new Hurricane();
        hurricane.getLocationsUUIDReference().add(UUID.randomUUID());

        new SerializeJSONHurricane(hurricane).resolveReferences();

        assertTrue(hurricane.getLocations().isEmpty());
    }

    @Test
    void resolveReferencesClearsPreviouslyResolvedLocations() {
        Location oldLocation = registeredLocation();
        Hurricane hurricane = new Hurricane("Helene", 3,
                new ArrayList<>(java.util.List.of(oldLocation)), HurricaneStatus.APPROACHING);
        hurricane.getLocationsUUIDReference().clear(); // nothing to resolve now

        new SerializeJSONHurricane(hurricane).resolveReferences();

        assertTrue(hurricane.getLocations().isEmpty(),
                "resolveReferences should clear getLocations() before repopulating it");
    }
}
