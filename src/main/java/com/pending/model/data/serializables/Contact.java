package com.pending.model.data.serializables;

import com.pending.model.ContactPreference;
import com.pending.model.JSONFile;
import org.json.simple.JSONObject;

import java.util.UUID;

public class Contact extends Serializable {
    public static String file = JSONFile.CONTACTS.getFilename();

    private String email;
    private String phone;
    private ContactPreference contactPreference;

    public Contact(UUID id, String email, String phoneNumber,
        ContactPreference contactPreference) {

    }

    public Contact(String email, String phoneNumber,
        ContactPreference contactPreference) {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ContactPreference getContactPreference() {
        return contactPreference;
    }

    public void setContactPreference(ContactPreference contactPreference) {
        this.contactPreference = contactPreference;
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

    public void deserialize(JSONObject jsonObject) {
        id = UUID.fromString((String) jsonObject.get("id"));
        email = (String) jsonObject.get("email");
        phone = (String) jsonObject.get("phone");
        contactPreference = (ContactPreference) jsonObject.get("contact_preference");
    }
}