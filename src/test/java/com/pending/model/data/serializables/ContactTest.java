package com.pending.model.data.serializables;

import com.pending.model.data.DataManager;
import com.pending.model.enums.ContactPreference;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ContactTest {

    @TempDir
    Path tempDir;

    @Test
    void defaultConstructorUsesCallPreferenceAndEmptyStrings() {
        Contact contact = new Contact();

        assertEquals("", contact.getEmail());
        assertEquals("", contact.getPhone());
        assertEquals(ContactPreference.CALL, contact.getContactPreference());
        assertNotNull(contact.getId());
    }

    @Test
    void threeArgConstructorGeneratesAnId() {
        Contact contact = new Contact("a@example.com", "555-0100", ContactPreference.EMAIL);

        assertNotNull(contact.getId());
        assertEquals("a@example.com", contact.getEmail());
        assertEquals("555-0100", contact.getPhone());
        assertEquals(ContactPreference.EMAIL, contact.getContactPreference());
    }

    @Test
    void idConstructorUsesProvidedId() {
        UUID id = UUID.randomUUID();
        Contact contact = new Contact(id, "b@example.com", "555-0101", ContactPreference.TEXT);

        assertEquals(id, contact.getId());
        assertEquals("b@example.com", contact.getEmail());
    }

    @Test
    void twoContactsGetDifferentRandomIds() {
        Contact first = new Contact();
        Contact second = new Contact();

        assertNotEquals(first.getId(), second.getId());
    }

    @Test
    void settersUpdateFields() {
        Contact contact = new Contact();
        UUID id = UUID.randomUUID();

        contact.setId(id);
        contact.setEmail("new@example.com");
        contact.setPhone("555-9999");
        contact.setContactPreference(ContactPreference.TEXT);

        assertEquals(id, contact.getId());
        assertEquals("new@example.com", contact.getEmail());
        assertEquals("555-9999", contact.getPhone());
        assertEquals(ContactPreference.TEXT, contact.getContactPreference());
    }

    @Test
    void toStringIncludesEmailPhoneAndPreference() {
        Contact contact = new Contact("c@example.com", "555-0102", ContactPreference.EMAIL);

        String result = contact.toString();

        assertTrue(result.contains("c@example.com"));
        assertTrue(result.contains("555-0102"));
        assertTrue(result.contains("EMAIL"));
    }

    @Test
    void serializeDeserializeRoundTripThroughFile() {
        Contact original = new Contact("round@example.com", "555-0103", ContactPreference.TEXT);

        DataManager manager = new DataManager(tempDir.resolve("contacts.json").toString());
        ArrayList<JSONObject> toWrite = new ArrayList<>();
        toWrite.add((JSONObject) original.serialize());
        assertTrue(manager.writeJSON(toWrite));

        ArrayList<JSONObject> readBack = manager.readJSON();
        assertNotNull(readBack);
        assertEquals(1, readBack.size());

        Contact restored = new Contact();
        restored.deserialize(readBack.get(0));

        assertEquals(original.getId(), restored.getId());
        assertEquals(original.getEmail(), restored.getEmail());
        assertEquals(original.getPhone(), restored.getPhone());
        assertEquals(original.getContactPreference(), restored.getContactPreference());
    }
}