package com.pending.model.data.serializables.behaviors;

import com.pending.model.data.serializables.Contact;
import com.pending.model.enums.ContactPreference;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SerializeJSONContactTest {

    @Test
    void serializeWritesAllFieldsAsStrings() {
        Contact contact = new Contact("a@example.com", "555-0100", ContactPreference.EMAIL);
        SerializeJSONContact behavior = new SerializeJSONContact(contact);

        JSONObject json = behavior.serialize();

        assertEquals(contact.getId().toString(), json.get("id"));
        assertEquals("a@example.com", json.get("email"));
        assertEquals("555-0100", json.get("phone"));
        assertEquals("EMAIL", json.get("contact_preference"));
    }

    @Test
    void deserializeRestoresAllFields() {
        UUID id = UUID.randomUUID();
        JSONObject json = new JSONObject();
        json.put("id", id.toString());
        json.put("email", "b@example.com");
        json.put("phone", "555-0101");
        json.put("contact_preference", "TEXT");

        Contact contact = new Contact();
        SerializeJSONContact behavior = new SerializeJSONContact(contact);

        behavior.deserialize(json);

        assertEquals(id, contact.getId());
        assertEquals("b@example.com", contact.getEmail());
        assertEquals("555-0101", contact.getPhone());
        assertEquals(ContactPreference.TEXT, contact.getContactPreference());
    }

    @Test
    void serializeThenDeserializeRoundTrips() {
        Contact original = new Contact("round@example.com", "555-0102", ContactPreference.CALL);
        SerializeJSONContact originalBehavior = new SerializeJSONContact(original);

        JSONObject json = originalBehavior.serialize();

        Contact restored = new Contact();
        new SerializeJSONContact(restored).deserialize(json);

        assertEquals(original.getId(), restored.getId());
        assertEquals(original.getEmail(), restored.getEmail());
        assertEquals(original.getPhone(), restored.getPhone());
        assertEquals(original.getContactPreference(), restored.getContactPreference());
    }
}
