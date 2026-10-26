package com.pending.model.data.serializables;

import java.util.UUID;

import com.pending.model.data.serializables.behaviors.SerializeJSONContact;
import com.pending.model.enums.ContactPreference;
import com.pending.model.enums.JSONFile;

/**
 * Stores contact information for a user, emergency contact, shelter,
 * or relief request.
 */
public class Contact extends Serializable {
    /** The JSON file used to store contact data. */
    public static String file = JSONFile.CONTACTS.getFilename();

    private String email;
    private String phone;
    private ContactPreference contactPreference;

    /**
     * Creates contact information with a generated UUID.
     *
     * @param email the email address
     * @param phone the phone number
     * @param contactPreference the preferred communication method
     */
    public Contact(String email, String phone,
            ContactPreference contactPreference) {

        super();
        this.email = email;
        this.phone = phone;
        this.contactPreference = contactPreference;
        serializeBehavior = new SerializeJSONContact(this);
    }

    /**
     * Creates contact information with a specified UUID.
     *
     * @param id the contact UUID
     * @param email the email address
     * @param phone the phone number
     * @param contactPreference the preferred communication method
     */
    public Contact(UUID id, String email, String phone,
            ContactPreference contactPreference) {

        this(email, phone, contactPreference);
        this.id = id;
    }

    /**
     * Creates empty contact information with phone calls as the default
     * contact preference.
     */
    public Contact() {
        this("", "", ContactPreference.CALL);
    }

    /**
     * Gets the email address.
     *
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address.
     *
     * @param email the new email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the phone number.
     *
     * @return the phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the phone number.
     *
     * @param phone the new phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets the preferred contact method.
     *
     * @return the contact preference
     */
    public ContactPreference getContactPreference() {
        return contactPreference;
    }

    /**
     * Sets the preferred contact method.
     *
     * @param contactPreference the new contact preference
     */
    public void setContactPreference(
            ContactPreference contactPreference) {

        this.contactPreference = contactPreference;
    }

    /**
     * Returns contact information in a readable format.
     *
     * @return the formatted email, phone number, and preference
     */
    @Override
    public String toString() {
        return String.format("{%s, %s, %s}",
                email, phone, contactPreference.name());
    }
}