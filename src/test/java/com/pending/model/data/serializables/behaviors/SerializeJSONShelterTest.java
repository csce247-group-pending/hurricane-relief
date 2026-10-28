package com.pending.model.data.serializables.behaviors;

import com.pending.model.data.DataList;
import com.pending.model.data.Database;
import com.pending.model.data.serializables.Contact;
import com.pending.model.data.serializables.LivingBeing;
import com.pending.model.data.serializables.Location;
import com.pending.model.data.serializables.Serializable;
import com.pending.model.data.serializables.Shelter;
import com.pending.model.data.serializables.ShelterManager;
import com.pending.model.data.serializables.Volunteer;
import com.pending.model.enums.ContactPreference;
import com.pending.model.enums.ShelterStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SerializeJSONShelterTest {

    private final ArrayList<Runnable> cleanupTasks = new ArrayList<>();

    @AfterEach
    void cleanUp() {
        cleanupTasks.forEach(Runnable::run);
        cleanupTasks.clear();
    }

    @SuppressWarnings("unchecked")
    private <T extends Serializable> void addToDatabase(Class<T> cls, T object) {
        ((DataList<T>) Database.getInstance().get(cls)).add(object);
        cleanupTasks.add(() -> ((DataList<T>) Database.getInstance().get(cls)).remove(object));
    }

    private Location registeredLocation() {
        Location location = new Location("SC", "Charleston", "1 Main St", "29401");
        addToDatabase(Location.class, location);
        return location;
    }

    private Contact registeredContact() {
        Contact contact = new Contact("a@example.com", "555-0100", ContactPreference.EMAIL);
        addToDatabase(Contact.class, contact);
        return contact;
    }

    private LivingBeing registeredLivingBeing() {
        LivingBeing being = new LivingBeing();
        addToDatabase(LivingBeing.class, being);
        return being;
    }

    private Volunteer registeredVolunteer() {
        Volunteer volunteer = new Volunteer();
        addToDatabase(Volunteer.class, volunteer);
        return volunteer;
    }

    private ShelterManager registeredManager() {
        ShelterManager manager = new ShelterManager();
        addToDatabase(ShelterManager.class, manager);
        addToDatabase(com.pending.model.data.serializables.User.class, manager);
        return manager;
    }

    @Test
    void serializeWritesLocationContactAndPetFriendly() {
        Location location = new Location("SC", "Charleston", "1 Main St", "29401");
        Contact contact = new Contact();
        Shelter shelter = new Shelter("Shelter A", location, 50, new ArrayList<>(),
                ShelterStatus.OPEN, contact, true);

        JSONObject json = new SerializeJSONShelter(shelter).serialize();

        assertEquals(shelter.getId().toString(), json.get("id"));
        assertEquals("Shelter A", json.get("name"));
        assertEquals(location.getId().toString(), json.get("location"));
        assertEquals(contact.getId().toString(), json.get("contact"));
        assertEquals(50L, json.get("capacity"));
        assertEquals("OPEN", json.get("status"));
        assertEquals(Boolean.TRUE, json.get("pet_friendly"));
    }

    @Test
    void serializeOmitsManagerWhenThereIsNone() {
        Shelter shelter = new Shelter();

        JSONObject json = new SerializeJSONShelter(shelter).serialize();

        assertFalse(json.containsKey("manager"));
    }

    @Test
    void serializeIncludesManagerWhenSet() {
        Shelter shelter = new Shelter();
        ShelterManager manager = new ShelterManager();
        shelter.setManager(manager);

        JSONObject json = new SerializeJSONShelter(shelter).serialize();

        assertEquals(manager.getId().toString(), json.get("manager"));
    }

    @Test
    void serializeUsesOccupantAndVolunteerObjectsWhenPresent() {
        Shelter shelter = new Shelter();
        LivingBeing occupant = new LivingBeing();
        Volunteer volunteer = new Volunteer();
        shelter.addOccupant(occupant);
        shelter.addVolunteer(volunteer);

        JSONObject json = new SerializeJSONShelter(shelter).serialize();

        JSONArray occupants = (JSONArray) json.get("occupants");
        JSONArray volunteers = (JSONArray) json.get("volunteers");
        assertEquals(List.of(occupant.getId().toString()), occupants);
        assertEquals(List.of(volunteer.getId().toString()), volunteers);
    }

    @Test
    void serializeFallsBackToUUIDReferencesWhenListsAreEmpty() {
        Shelter shelter = new Shelter();
        UUID occupantRef = UUID.randomUUID();
        UUID volunteerRef = UUID.randomUUID();
        shelter.getOccupantsUUIDReference().add(occupantRef);
        shelter.getVolunteersUUIDReference().add(volunteerRef);

        JSONObject json = new SerializeJSONShelter(shelter).serialize();

        assertEquals(List.of(occupantRef.toString()), json.get("occupants"));
        assertEquals(List.of(volunteerRef.toString()), json.get("volunteers"));
    }

    @Test
    void deserializeRestoresAllFields() {
        UUID id = UUID.randomUUID();
        UUID locationId = UUID.randomUUID();
        UUID contactId = UUID.randomUUID();
        UUID occupantId = UUID.randomUUID();
        UUID volunteerId = UUID.randomUUID();
        UUID managerId = UUID.randomUUID();

        JSONObject json = new JSONObject();
        json.put("id", id.toString());
        json.put("name", "Shelter A");
        json.put("location", locationId.toString());
        json.put("contact", contactId.toString());
        json.put("capacity", 50L);
        json.put("status", "OPEN");
        json.put("pet_friendly", true);
        JSONArray occupants = new JSONArray();
        occupants.add(occupantId.toString());
        json.put("occupants", occupants);
        JSONArray volunteers = new JSONArray();
        volunteers.add(volunteerId.toString());
        json.put("volunteers", volunteers);
        json.put("manager", managerId.toString());

        Shelter shelter = new Shelter();
        new SerializeJSONShelter(shelter).deserialize(json);

        assertEquals(id, shelter.getId());
        assertEquals("Shelter A", shelter.getName());
        assertEquals(locationId, shelter.getLocationUUIDReference());
        assertEquals(contactId, shelter.getContactUUIDReference());
        assertEquals(50, shelter.getCapacity());
        assertEquals(ShelterStatus.OPEN, shelter.getStatus());
        assertTrue(shelter.isPetFriendly());
        assertEquals(List.of(occupantId), shelter.getOccupantsUUIDReference());
        assertEquals(List.of(volunteerId), shelter.getVolunteersUUIDReference());
        assertEquals(managerId, shelter.getManagerUUIDReference());
    }

    @Test
    void deserializeFallsBackToAccommodatesPetsKeyForOlderFiles() {
        JSONObject json = baseShelterJson();
        json.remove("pet_friendly");
        json.put("accommodates_pets", true);

        Shelter shelter = new Shelter();
        new SerializeJSONShelter(shelter).deserialize(json);

        assertTrue(shelter.isPetFriendly());
    }

    @Test
    void deserializeWithNeitherPetKeyDefaultsToFalse() {
        JSONObject json = baseShelterJson();
        json.remove("pet_friendly");

        Shelter shelter = new Shelter();
        new SerializeJSONShelter(shelter).deserialize(json);

        assertFalse(shelter.isPetFriendly());
    }

    @Test
    void deserializeWithNullManagerClearsTheReference() {
        Shelter shelter = new Shelter();
        shelter.setManagerUUIDReference(UUID.randomUUID()); // pre-existing reference
        JSONObject json = baseShelterJson();
        // "manager" intentionally omitted

        new SerializeJSONShelter(shelter).deserialize(json);

        assertNull(shelter.getManagerUUIDReference());
    }

    private JSONObject baseShelterJson() {
        JSONObject json = new JSONObject();
        json.put("id", UUID.randomUUID().toString());
        json.put("name", "Shelter A");
        json.put("location", UUID.randomUUID().toString());
        json.put("contact", UUID.randomUUID().toString());
        json.put("capacity", 50L);
        json.put("status", "OPEN");
        json.put("pet_friendly", true);
        json.put("occupants", new JSONArray());
        json.put("volunteers", new JSONArray());
        return json;
    }

    @Test
    void resolveReferencesPopulatesLocationContactOccupantsVolunteersAndManager() {
        Location location = registeredLocation();
        Contact contact = registeredContact();
        LivingBeing occupant = registeredLivingBeing();
        Volunteer volunteer = registeredVolunteer();
        ShelterManager manager = registeredManager();

        Shelter shelter = new Shelter();
        shelter.setLocationUUIDReference(location.getId());
        shelter.setContactUUIDReference(contact.getId());
        shelter.getOccupantsUUIDReference().add(occupant.getId());
        shelter.getVolunteersUUIDReference().add(volunteer.getId());
        shelter.setManagerUUIDReference(manager.getId());

        new SerializeJSONShelter(shelter).resolveReferences();

        assertSame(location, shelter.getLocation());
        assertSame(contact, shelter.getContactInfo());
        assertEquals(1, shelter.getOccupants().size());
        assertSame(occupant, shelter.getOccupants().get(0));
        assertEquals(1, shelter.getVolunteers().size());
        assertSame(volunteer, shelter.getVolunteers().get(0));
        assertSame(manager, shelter.getManager());
    }

    @Test
    void resolveReferencesSkipsUnknownOccupantIds() {
        Shelter shelter = new Shelter();
        shelter.getOccupantsUUIDReference().add(UUID.randomUUID());

        new SerializeJSONShelter(shelter).resolveReferences();

        assertTrue(shelter.getOccupants().isEmpty());
    }

    /**
     * BUG: unlike the occupants loop (which null-checks each lookup before
     * adding), the volunteers loop does not:
     *     Volunteer volunteer = database.getVolunteerByUUID(id);
     *     serializable.addVolunteer(volunteer);
     * An unknown volunteer id results in a literal null entry in the
     * shelter's volunteer list instead of being skipped.
     */
    @Test
    void resolveReferencesAddsANullEntryForUnknownVolunteerIds() {
        Shelter shelter = new Shelter();
        shelter.getVolunteersUUIDReference().add(UUID.randomUUID());

        new SerializeJSONShelter(shelter).resolveReferences();

        assertEquals(1, shelter.getVolunteers().size(),
                "if this now fails with size 0, the missing null-check has been fixed "
                        + "and this test should assert the list is empty instead");
        assertNull(shelter.getVolunteers().get(0));
    }

    @Test
    void resolveReferencesLeavesManagerNullWhenNoReference() {
        Shelter shelter = new Shelter();
        shelter.setManagerUUIDReference(null);

        assertDoesNotThrow(() -> new SerializeJSONShelter(shelter).resolveReferences());
        assertNull(shelter.getManager());
    }
}
