package com.pending.model.data.serializables;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.pending.model.enums.ContactPreference;
import com.pending.model.enums.LivingBeingType;
import com.pending.model.enums.ReliefStatus;
import com.pending.model.enums.ReliefType;

class ReliefRequestTest {

    private LivingBeing createHuman(String firstName,
            ReliefType... reliefNeeds) {

        return new LivingBeing(firstName, "Test",
                LocalDate.of(1990, 1, 1), LivingBeingType.HUMAN,
                new ArrayList<>(List.of(reliefNeeds)));
    }

    private LivingBeing createPet(String firstName,
            ReliefType... reliefNeeds) {

        return new LivingBeing(firstName, "Test",
                LocalDate.of(2020, 1, 1), LivingBeingType.PET,
                new ArrayList<>(List.of(reliefNeeds)));
    }

    private Contact createContact() {
        return new Contact("help@example.com", "8035550100",
                ContactPreference.EMAIL);
    }

    @Test
    void defaultConstructorCreatesEmptyRequestedReliefRequest() {
        ReliefRequest request = new ReliefRequest();

        assertEquals("", request.getDescription());
        assertEquals(ReliefStatus.REQUESTED, request.getStatus());
        assertTrue(request.getLivingBeings().isEmpty());
        assertTrue(request.getTypes().isEmpty());
        assertNull(request.getAssignedTo());
        assertEquals(0, request.getPartySize());
        assertFalse(request.hasPets());
        assertNotNull(request.getId());
    }

    @Test
    void constructorStoresProvidedRequestDetails() {
        Location location = new Location("SC", "Charleston",
                "1 Main St", "29401");
        Contact contact = createContact();
        ArrayList<LivingBeing> livingBeings = new ArrayList<>(
                List.of(createHuman("Jane", ReliefType.FOOD)));
        ArrayList<ReliefType> reliefTypes = new ArrayList<>(
                List.of(ReliefType.FOOD));

        ReliefRequest request = new ReliefRequest("Need food",
                location, ReliefStatus.REQUESTED, livingBeings,
                reliefTypes, contact);

        assertEquals("Need food", request.getDescription());
        assertEquals(ReliefStatus.REQUESTED, request.getStatus());
        assertSame(location, request.getLocation());
        assertSame(contact, request.getContactInfo());
        assertEquals(livingBeings, request.getLivingBeings());
        assertEquals(reliefTypes, request.getTypes());
    }

    @Test
    void constructorWithIdUsesProvidedId() {
        UUID id = UUID.randomUUID();

        ReliefRequest request = new ReliefRequest(id, "Need water",
                new Location(), ReliefStatus.REQUESTED,
                new ArrayList<>(), new ArrayList<>(), createContact());

        assertEquals(id, request.getId());
    }

    @Test
    void hasPetsReturnsFalseWhenRequestContainsOnlyHumans() {
        ReliefRequest request = new ReliefRequest("", new Location(),
                ReliefStatus.REQUESTED, new ArrayList<>(
                        List.of(createHuman("Jane", ReliefType.FOOD))),
                new ArrayList<>(), createContact());

        assertFalse(request.hasPets());
    }

    @Test
    void hasPetsReturnsTrueWhenRequestContainsPet() {
        ReliefRequest request = new ReliefRequest("", new Location(),
                ReliefStatus.REQUESTED, new ArrayList<>(
                        List.of(createPet("Rex",
                                ReliefType.PET_SHELTER))),
                new ArrayList<>(), createContact());

        assertTrue(request.hasPets());
    }

    @Test
    void getPartySizeCountsHumansAndPets() {
        ReliefRequest request = new ReliefRequest("", new Location(),
                ReliefStatus.REQUESTED, new ArrayList<>(List.of(
                        createHuman("Jane", ReliefType.FOOD),
                        createHuman("Sam", ReliefType.WATER),
                        createPet("Rex", ReliefType.PET_SHELTER))),
                new ArrayList<>(), createContact());

        assertEquals(3, request.getPartySize());
    }

    @Test
    void getPartySizeHumansExcludesPets() {
        ReliefRequest request = new ReliefRequest("", new Location(),
                ReliefStatus.REQUESTED, new ArrayList<>(List.of(
                        createHuman("Jane", ReliefType.FOOD),
                        createHuman("Sam", ReliefType.WATER),
                        createPet("Rex", ReliefType.PET_SHELTER))),
                new ArrayList<>(), createContact());

        assertEquals(2, request.getPartySizeHumans());
    }

    @Test
    void addLivingBeingAddsMemberToRequest() {
        ReliefRequest request = new ReliefRequest();

        assertTrue(request.addLivingBeing(
                createHuman("Jane", ReliefType.FOOD)));
        assertEquals(1, request.getPartySize());
    }

    @Test
    void toStringUsesNaForRequestWithNoReliefTypes() {
        ReliefRequest request = new ReliefRequest();

        assertTrue(request.toString().contains("Relief Needs: N/A"));
    }

    @Test
    void toStringDetailedShowsNoneForUnassignedEmptyRequest() {
        ReliefRequest request = new ReliefRequest();

        String result = request.toStringDetailed();

        assertTrue(result.contains("Assigned To: None"));
        assertTrue(result.contains("Members(0): None"));
    }

    @Test
    void toStringDetailedListsEachMember() {
        ReliefRequest request = new ReliefRequest("", new Location(),
                ReliefStatus.REQUESTED, new ArrayList<>(List.of(
                        createHuman("Jane", ReliefType.FOOD),
                        createPet("Rex", ReliefType.PET_SHELTER))),
                new ArrayList<>(), createContact());

        String result = request.toStringDetailed();

        assertTrue(result.contains("Jane Test"));
        assertTrue(result.contains("Rex Test"));
    }

    @Test
    void constructorUsesProvidedLocationIdForReference() {
        Location location = new Location("SC", "Charleston",
                "1 Main St", "29401");

        ReliefRequest request = new ReliefRequest("Need help",
                location, ReliefStatus.REQUESTED, new ArrayList<>(),
                new ArrayList<>(), createContact());

        assertEquals(location.getId(),
                request.getLocationUUIDReference());
    }

    @Test
    void constructorUsesProvidedContactIdForReference() {
        Contact contact = createContact();

        ReliefRequest request = new ReliefRequest("Need help",
                new Location(), ReliefStatus.REQUESTED,
                new ArrayList<>(), new ArrayList<>(), contact);

        assertEquals(contact.getId(),
                request.getContactUUIDReference());
    }
}