package com.pending.model.data.serializables;

import com.pending.model.data.Database;
import com.pending.model.enums.Priority;
import com.pending.model.enums.ReliefStatus;
import com.pending.model.enums.ReliefType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class SerializeJSONReliefRequest extends SerializeJSON<ReliefRequest> {
    public SerializeJSONReliefRequest(ReliefRequest serializable) {
        super(serializable);
    }

    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", serializable.getId());
        jsonObject.put("priority", serializable.getPriority().name());
        jsonObject.put("status", serializable.getStatus().name());
        jsonObject.put("description", serializable.getDescription());
        jsonObject.put("location", serializable.getLocation().serialize());
        jsonObject.put("submitted_at", serializable.getSubmittedAt().toString());

        JSONArray neededResources = new JSONArray();
        for (Resource resource: serializable.getNeededResources()) {
            neededResources.add(resource.serialize());
        }
        jsonObject.put("needed_resources", neededResources);

        jsonObject.put("attached_photo", serializable.getAttachedPhoto().getName());
        jsonObject.put("type", serializable.getType().name());

        return jsonObject;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        serializable.setId(UUID.fromString((String) jsonObject.get("id")));
        serializable.setPriority(Priority.valueOf((String) jsonObject.get("priority")));
        serializable.setStatus(ReliefStatus.valueOf((String) jsonObject.get("status")));
        serializable.setDescription((String) jsonObject.get("description"));
        serializable.setLocationUUIDReference(UUID.fromString((String) jsonObject.get("location")));
        serializable.setSubmittedAt(LocalDateTime.parse((String) jsonObject.get("submitted_at")));

        ArrayList<Resource> neededResources = serializable.getNeededResources();
        neededResources.clear();
        JSONArray jsonNeededResources = (JSONArray) jsonObject.get("needed_resources");
        for (Object jsonResource: jsonNeededResources) {
            Resource resource = new Resource();
            resource.deserialize(jsonResource);
            neededResources.add(resource);
        }

        serializable.setAttachedPhoto(new File((String) jsonObject.get("attached_photo")));
        serializable.setType(ReliefType.valueOf((String) jsonObject.get("type")));
    }

    @Override
    public void resolveReferences() {
        Database database = Database.getInstance();

        serializable.setLocation(database.getLocationByUUID(serializable.getLocationUUIDReference()));
    }
}
