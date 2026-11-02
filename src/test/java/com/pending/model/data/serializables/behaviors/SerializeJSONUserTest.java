package com.pending.model.data.serializables.behaviors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.pending.model.data.serializables.User;

public class SerializeJSONUserTest {

    private User testUser;
    private SerializeJSONUser<User> serializer;

    @BeforeEach
    public void setUp() {
        // Initialize testUser with blank Location, Contacts, and UUIDs
        testUser = new User();
        
        // Initialize the behavior class wrapping the user
        serializer = new SerializeJSONUser<>(testUser);
    }

    @Test
    public void testSerializeFullyPopulatedUser() {

    }

    @Test
    public void testSerializeUserWithNullEmergencyContact() {

    }

    @Test
    public void testDeserializeValidUserJSON() {

    }

    @Test
    public void testDeserializeUserWithEmptyReliefRequests() {

    }

    @Test
    public void testResolveReferencesSuccessfully() {

    }

    @Test
    public void testResolveReferencesWithMissingDatabaseObjects() {

    }
}