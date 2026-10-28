package com.pending.model.data.serializables;

import com.pending.model.data.serializables.behaviors.SerializeBehavior;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SerializableTest {

    /** Minimal concrete Serializable used only to exercise the base class. */
    private static class StubSerializable extends Serializable {
        StubBehavior behavior;

        StubSerializable() {
            super();
            behavior = new StubBehavior(this);
            serializeBehavior = behavior;
        }

        /** A StubSerializable with no behavior attached, to test the null-behavior warning path. */
        StubSerializable(boolean withBehavior) {
            super();
            if (withBehavior) {
                behavior = new StubBehavior(this);
                serializeBehavior = behavior;
            }
        }
    }

    private static class StubBehavior extends SerializeBehavior<StubSerializable> {
        boolean serializeCalled;
        boolean deserializeCalled;
        boolean resolveReferencesCalled;
        Object lastDeserializeArgument;
        Object serializeReturnValue = "stub-serialized";

        StubBehavior(StubSerializable serializable) {
            super(serializable);
        }

        @Override
        public Object serialize() {
            serializeCalled = true;
            return serializeReturnValue;
        }

        @Override
        public void deserialize(Object object) {
            deserializeCalled = true;
            lastDeserializeArgument = object;
        }

        @Override
        public void resolveReferences() {
            resolveReferencesCalled = true;
        }
    }

    @Test
    void constructorGeneratesANonNullId() {
        StubSerializable a = new StubSerializable();

        assertNotNull(a.getId());
    }

    @Test
    void twoInstancesGetDifferentRandomIds() {
        StubSerializable a = new StubSerializable();
        StubSerializable b = new StubSerializable();

        assertNotEquals(a.getId(), b.getId());
    }

    @Test
    void setIdOverridesTheGeneratedId() {
        StubSerializable a = new StubSerializable();
        UUID id = UUID.randomUUID();

        a.setId(id);

        assertEquals(id, a.getId());
    }

    @Test
    void serializeDelegatesToSerializeBehavior() {
        StubSerializable a = new StubSerializable();

        Object result = a.serialize();

        assertTrue(a.behavior.serializeCalled);
        assertEquals(a.behavior.serializeReturnValue, result);
    }

    @Test
    void deserializeDelegatesToSerializeBehaviorWithTheSameArgument() {
        StubSerializable a = new StubSerializable();
        Object payload = new Object();

        a.deserialize(payload);

        assertTrue(a.behavior.deserializeCalled);
        assertSame(payload, a.behavior.lastDeserializeArgument);
    }

    @Test
    void resolveReferencesDelegatesToSerializeBehavior() {
        StubSerializable a = new StubSerializable();

        a.resolveReferences();

        assertTrue(a.behavior.resolveReferencesCalled);
    }

    @Test
    void deserializeWithNoBehaviorPrintsAWarningInsteadOfThrowing() {
        StubSerializable a = new StubSerializable(false); // serializeBehavior left null

        ByteArrayOutputStream capturedOut = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(capturedOut));
        try {
            assertDoesNotThrow(() -> a.deserialize(new Object()));
        } finally {
            System.setOut(originalOut);
        }

        assertTrue(capturedOut.toString().contains("has no serialize behavior"));
    }

    @Test
    void resolveReferencesWithNoBehaviorThrowsNullPointerException() {
        // Unlike deserialize(), resolveReferences() has no null check before
        // calling serializeBehavior.resolveReferences().
        StubSerializable a = new StubSerializable(false);

        assertThrows(NullPointerException.class, a::resolveReferences);
    }
}