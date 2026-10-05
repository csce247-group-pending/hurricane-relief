package com.pending.model;

import java.util.UUID;

public class Contact {

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

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phoneNumber) {

    }

    public ContactPreference getContactPreference() {
        return contactPreference;
    }

    public void setContactPreference(ContactPreference contactPreference) {

    }
}