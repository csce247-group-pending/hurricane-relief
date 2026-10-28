package com.pending.model.data.serializables.behaviors;

import com.pending.model.data.serializables.Contact;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SerializeJSONTest {

    /** Minimal concrete subclass used only to exercise the base class. */
    private static class StubSerializeJSON extends SerializeJSON<Contact> {
        boolean deserializeWithJSONObjectCalled;
        JSONObject lastArgument;

        StubSerializeJSON(Contact serializable) {
            super(serializable);
        }

        @Override
        public JSONObject serialize() {
            return new JSONObject();
        }

        @Override
        public void deserialize(JSONObject jsonObject) {
            deserializeWithJSONObjectCalled = true;
            lastArgument = jsonObject;
        }
    }

    @Test
    void deserializeWithObjectDelegatesToTheJSONObjectOverload() {
        StubSerializeJSON behavior = new StubSerializeJSON(new Contact());
        JSONObject payload = new JSONObject();
        payload.put("key", "value");

        behavior.deserialize((Object) payload);

        assertTrue(behavior.deserializeWithJSONObjectCalled);
        assertSame(payload, behavior.lastArgument);
    }

    @Test
    void deserializeWithObjectThrowsWhenGivenSomethingOtherThanAJSONObject() {
        StubSerializeJSON behavior = new StubSerializeJSON(new Contact());

        assertThrows(ClassCastException.class, () -> behavior.deserialize((Object) "not a json object"));
    }
}
