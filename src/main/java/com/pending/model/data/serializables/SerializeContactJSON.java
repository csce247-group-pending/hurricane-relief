package com.pending.model.data.serializables;

import com.pending.model.ContactPreference;
import org.json.simple.JSONObject;

import java.util.UUID;

public class SerializeContactJSON extends SerializeJSON<Contact> {
    public SerializeContactJSON(Contact serializable) {
        super(serializable);
    }

    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", serializable.getId().toString());
        jsonObject.put("email", serializable.getEmail());
        jsonObject.put("phone", serializable.getPhone());
        jsonObject.put("contactPreference", serializable.getContactPreference().ordinal());
        return jsonObject;
    }

    public void deserialize(JSONObject jsonObject) {
        serializable.setId(UUID.fromString((String) jsonObject.get("id")));
        serializable.setEmail((String) jsonObject.get("email"));
        serializable.setPhone((String) jsonObject.get("phone"));
        serializable.setContactPreference(ContactPreference.valueOf((String) jsonObject.get("contact_preference")));
    }
}
