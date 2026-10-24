package com.pending.model.data.serializables.behaviors;

import java.util.UUID;

import org.json.simple.JSONObject;

import com.pending.model.data.serializables.Contact;
import com.pending.model.enums.ContactPreference;

/**
 * Provides JSON serialization behavior for Contact objects.
 * This class converts contact fields to JSON and restores them from JSON.
 */
public class SerializeJSONContact extends SerializeJSON<Contact> {

    /**
     * Creates JSON serialization behavior for a contact.
     *
     * @param serializable the contact managed by this behavior
     */
    public SerializeJSONContact(Contact serializable) {
        super(serializable);
    }

    /**
     * Converts the contact into a JSON object.
     *
     * @return the JSON representation of the contact
     */
    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", serializable.getId().toString());
        jsonObject.put("email", serializable.getEmail());
        jsonObject.put("phone", serializable.getPhone());
        jsonObject.put("contact_preference",
                serializable.getContactPreference().name());

        return jsonObject;
    }

    /**
     * Loads contact fields from a JSON object.
     *
     * @param jsonObject the JSON object containing contact data
     */
    @Override
    public void deserialize(JSONObject jsonObject) {
        serializable.setId(
                UUID.fromString((String) jsonObject.get("id")));
        serializable.setEmail((String) jsonObject.get("email"));
        serializable.setPhone((String) jsonObject.get("phone"));
        serializable.setContactPreference(
                ContactPreference.valueOf(
                        (String) jsonObject.get("contact_preference")));
    }
}