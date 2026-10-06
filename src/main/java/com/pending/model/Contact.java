package com.pending.model;

import org.json.simple.JSONObject;

import java.util.UUID;

public class Contact extends Serializable {
    private UUID id;
    private String email;
    private String phone;
    private ContactPreference contactPreference;

    public Contact(UUID id, String email, String phoneNumber,
        ContactPreference contactPreference) {

    }

    public Contact(String email, String phoneNumber,
        ContactPreference contactPreference) {

    }

    public boolean contactEmail() {
        return false;
    }

    public boolean contactPhoneCall() {
        return false;
    }

    public boolean contactPhoneText() {
        return false;
    }

    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id.toString());
        jsonObject.put("email", email);
        jsonObject.put("phone", phone);
        jsonObject.put(contactPreference, contactPreference.ordinal());
        return jsonObject;
    }

    public void deserialize(JSONObject object) {
        id = UUID.fromString((String)object.get("id"));
        email = (String)object.get("email");
        phone = (String)object.get("phone");
        contactPreference = (ContactPreference)object.get("contact_preference");
    }
}