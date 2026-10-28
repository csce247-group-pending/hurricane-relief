package com.pending.model.data.serializables.behaviors;

import com.pending.model.data.serializables.Contact;
import com.pending.model.enums.ContactPreference;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SerializeBehaviorTest {

    /** Minimal concrete subclass used only to exercise the base class. */
    private static class StubBehavior extends SerializeBehavior<Contact> {
        StubBehavior(Contact serializable) {
            super(serializable);
        }

        @Override
        public Object serialize() {
            return "stub";
        }

        @Override
        public void deserialize(Object object) {
            // no-op
        }
    }

    @Test
    void constructorStoresTheServiceableObject() {
        Contact contact = new Contact("a@example.com", "555-0100", ContactPreference.EMAIL);
        StubBehavior behavior = new StubBehavior(contact);

        // "serializable" is protected, which this test can read directly
        // since it lives in the same package as SerializeBehavior.
        assertSame(contact, behavior.serializable);
    }

    @Test
    void defaultResolveReferencesDoesNothingAndDoesNotThrow() {
        StubBehavior behavior = new StubBehavior(new Contact());

        assertDoesNotThrow(behavior::resolveReferences);
    }
}
