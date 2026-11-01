package com.pending.model.data.serializables;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.pending.model.enums.ContactPreference;

class ContactTest {

    @Test
    void defaultConstructorCreatesEmptyContactWithCallPreference() {
        Contact contact = new Contact();

        assertEquals("", contact.getEmail());
        assertEquals("", contact.getPhone());
        assertEquals(ContactPreference.CALL,
                contact.getContactPreference());
        assertNotNull(contact.getId());
    }

    @Test
    void constructorStoresProvidedContactDetails() {
        Contact contact = new Contact(
                "james@example.com",
                "8035550100",
                ContactPreference.EMAIL);

        assertEquals("james@example.com", contact.getEmail());
        assertEquals("8035550100", contact.getPhone());
        assertEquals(ContactPreference.EMAIL,
                contact.getContactPreference());
        assertNotNull(contact.getId());
    }

    @Test
    void constructorWithIdUsesProvidedId() {
        UUID id = UUID.randomUUID();

        Contact contact = new Contact(
                id,
                "doris@example.com",
                "8035550101",
                ContactPreference.TEXT);

        assertEquals(id, contact.getId());
        assertEquals("doris@example.com", contact.getEmail());
        assertEquals("8035550101", contact.getPhone());
        assertEquals(ContactPreference.TEXT,
                contact.getContactPreference());
    }

    @Test
    void newContactsReceiveDifferentIds() {
        Contact firstContact = new Contact();
        Contact secondContact = new Contact();

        assertNotEquals(firstContact.getId(), secondContact.getId());
    }

    @Test
    void toStringFormatsContactInformation() {
        Contact contact = new Contact(
                "shelter@example.com",
                "8435550100",
                ContactPreference.CALL);

        assertEquals("{shelter@example.com, 8435550100, CALL}",
                contact.toString());
    }
}