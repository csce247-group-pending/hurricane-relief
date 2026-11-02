package com.pending.model.data.serializables.behaviors;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

import com.pending.model.data.serializables.Contact;
import com.pending.model.enums.ContactPreference;

/*
 * AI Usage: Used AI to generate the test method assertions, boilerplate setup, and table formatting.
 *
 * +---------------------------------------------------------+-------------------------------------------------------------------+
 * | Test                                                    | Reasoning                                                         |
 * +---------------------------------------------------------+-------------------------------------------------------------------+
 * | testSerializeContact                                    | Verifies all string properties and enum conversions map cleanly.  |
 * | testDeserializeContact                                  | Ensures JSON values are properly parsed back to object fields.    |
 * +---------------------------------------------------------+-------------------------------------------------------------------+
 */
class SerializeJSONContactTest {

    private Contact testContact;
    private SerializeJSONContact serializer;
    private UUID testId;

    @BeforeEach
    public void setUp() {
        testId = UUID.randomUUID();
        testContact = new Contact();
        testContact.setId(testId);
        testContact.setEmail("test@email.com");
        testContact.setPhone("8035550199");
        testContact.setContactPreference(ContactPreference.EMAIL);
        
        serializer = new SerializeJSONContact(testContact);
    }

    @Test
    public void testSerializeContact() {
        JSONObject jsonObject = serializer.serialize();

        // HARDCODED ASSERTIONS: Verify exact JSON string outputs
        assertEquals(testId.toString(), jsonObject.get("id"), "UUID should serialize to string.");
        assertEquals("test@email.com", jsonObject.get("email"), "Email should serialize exactly.");
        assertEquals("8035550199", jsonObject.get("phone"), "Phone should serialize exactly.");
        assertEquals("EMAIL", jsonObject.get("contact_preference"), "Enum should serialize to its exact string name.");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDeserializeContact() {
        JSONObject mockJson = new JSONObject();
        UUID mockId = UUID.randomUUID();
        
        mockJson.put("id", mockId.toString());
        mockJson.put("email", "mock@email.com");
        mockJson.put("phone", "8430001111");
        mockJson.put("contact_preference", "CALL");

        serializer.deserialize(mockJson);

        // HARDCODED ASSERTIONS: Verify the deserialized object absorbed the exact mock properties
        assertEquals(mockId, testContact.getId(), "Should parse UUID string back to UUID object.");
        assertEquals("mock@email.com", testContact.getEmail(), "Should parse email string.");
        assertEquals("8430001111", testContact.getPhone(), "Should parse phone string.");
        assertEquals(ContactPreference.CALL, testContact.getContactPreference(), "Should parse 'CALL' string back to Enum.");
    }
}