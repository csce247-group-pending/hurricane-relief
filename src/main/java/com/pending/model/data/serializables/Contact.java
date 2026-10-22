package com.pending.model.data.serializables;

import com.pending.model.data.serializables.behaviors.SerializeJSONContact;
import com.pending.model.enums.ContactPreference;
import com.pending.model.enums.JSONFile;

import java.util.UUID;

public class Contact extends Serializable {
    public static String file = JSONFile.CONTACTS.getFilename();

    private String email;
    private String phone;
    private ContactPreference contactPreference;

    public Contact(String email, String phone, ContactPreference contactPreference) {
        super();
        this.email = email;
        this.phone = phone;
        this.contactPreference = contactPreference;
        serializeBehavior = new SerializeJSONContact(this);
    }

    public Contact(UUID id, String email, String phone, ContactPreference contactPreference) {
        this(email, phone, contactPreference);
        this.id = id;
    }

    public Contact() {
        this("", "", ContactPreference.CALL);
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

    @Override
    public String toString() {
        return String.format("{%s, %s, %s}", email, phone, contactPreference.name());
    }
}