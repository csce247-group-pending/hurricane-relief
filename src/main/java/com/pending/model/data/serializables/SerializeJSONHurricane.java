package com.pending.model.data.serializables;

import com.pending.model.data.Database;
import com.pending.model.enums.HurricaneStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pending.model.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class SerializeJSONHurricane extends SerializeJSON<Hurricane> {
    public SerializeJSONHurricane(Hurricane serializable) {
        super(serializable);
    }

    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", serializable.getId());
        jsonObject.put("name", serializable.getName());
        jsonObject.put("category", serializable.getCategory());
        jsonObject.put("start_date", serializable.getStartDate().toString());
        jsonObject.put("end_date", serializable.getEndDate().toString());
        JSONArray locations = new JSONArray();
        for (Location location: serializable.getLocations()) {
            locations.add(location.getId().toString());
        }
        jsonObject.put("locations", locations);
        jsonObject.put("status", serializable.getStatus().name());
        JSONArray observers = new JSONArray();
        for (Admin observer: serializable.getObservers()) {
            observers.add(observer.getId().toString());
        }
        jsonObject.put("observers", observers);
        return jsonObject;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        serializable.setId(UUID.fromString((String) jsonObject.get("id")));
        serializable.setName((String) jsonObject.get("name"));
        serializable.setCategory(((Long) jsonObject.get("category")).intValue());
        serializable.setStartDate(LocalDateTime.parse((String) jsonObject.get("start_date")));
        serializable.setEndDate(LocalDateTime.parse((String) jsonObject.get("end_date")));
        serializable.getLocationsUUIDReference().clear();
        ArrayList<UUID> locations = serializable.getLocationsUUIDReference();
        locations.clear();
        for (Object locationUUIDJson: (JSONArray) jsonObject.get("locations")) {
            locations.add(UUID.fromString((String) locationUUIDJson));
        }
        serializable.setStatus(HurricaneStatus.valueOf((String) jsonObject.get("status")));
        ArrayList<UUID> observers = serializable.getObserversUUIDReference();
        observers.clear();
        for (Object observerUUIDJson: (JSONArray) jsonObject.get("observers")) {
            locations.add(UUID.fromString((String) observerUUIDJson));
        }
    }

    @Override
    public void resolveReferences() {
        Database database = Database.getInstance();

        serializable.getLocations().clear();
        for (UUID id: serializable.getLocationsUUIDReference()) {
            serializable.getLocations().add(database.getLocationByUUID(id));
        }

        serializable.getObservers().clear();
        for (UUID id: serializable.getObserversUUIDReference()) {
            serializable.getObservers().add((Admin) database.getUserByUUID(id));
        }
    }
}
