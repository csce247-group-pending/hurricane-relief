package com.pending.model.data.serializables.behaviors;

import java.util.UUID;

import com.pending.model.data.serializables.Contact;
import org.json.simple.JSONObject;

import com.pending.model.enums.ContactPreference;

public class SerializeJSONContact extends SerializeJSON<Contact> {
    public SerializeJSONContact(Contact serializable) {
        super(serializable);
    }

    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", serializable.getId().toString());
        jsonObject.put("email", serializable.getEmail());
        jsonObject.put("phone", serializable.getPhone());
        jsonObject.put("contact_preference", serializable.getContactPreference().name());
        return jsonObject;
    }

    public void deserialize(JSONObject jsonObject) {
        serializable.setId(UUID.fromString((String) jsonObject.get("id")));
        serializable.setEmail((String) jsonObject.get("email"));
        serializable.setPhone((String) jsonObject.get("phone"));
        serializable.setContactPreference(ContactPreference.valueOf((String) jsonObject.get("contact_preference")));
    }
}
