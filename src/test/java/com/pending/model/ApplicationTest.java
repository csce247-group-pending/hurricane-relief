package com.pending.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ApplicationTest {

    private Application app;

    @BeforeAll
    public void setUp() {
        app = Application.getInstance();
    }
 
    @Test 
    public void testCreateValidAccount() {
        // boolean isCreated = app.requestCreateAccount("john", "son", 
        //     "1990-12-01", "thePassword%", "71 Society St, Charleston, SC, 29401", 
        //     "john_son@gmail.com", "8434448932", 
        //     "email", "john_friend@gmail.com", 
        //     "8435556865", "phone");

        assertTrue(isCreated);
    }

    @Test
    public void testCreateInvalidAccount() {

    }

    @Test
    public void testCreateExistingAccount() {

    }

    @Test
    public void testValidUserLoginAttempt() {

    }

    @Test
    public void testValidVolunteerLoginAttempt() {

    }

    @Test
    public void testValidShelterManagerLoginAttempt() {

    }

    @Test
    public void testInvalidLoginAttempt() {
        
    }

    @Test
    public void testValidGetVolunteerProfile() {

    }

    @Test
    public void testShelterManagerGetVolunteerProfile() {

    }

    @Test
    public void testUserGetVolunteerProfile() {
        
    }

    @Test
    public void testValidCreateReliefRequest() {

    }

    @Test
    public void testInvalidCreateReliefRequest() {
        
    }

    @Test
    public void testVolunteerOpenReliefRequest() {

    }

    @Test
    public void testUserOpenReliefRequest() {

    }

    @Test
    public void testValidAcceptReliefRequest() {

    }

    @Test
    public void testInvalidAcceptReliefRequest() {

    }

    @Test
    public void testUserCloseReliefRequest() {

    }

    @Test
    public void testVolunteerCloseReliefRequest() {

    }

    @Test
    public void testValidShelterManagerCloseReliefRequest() {

    }

    @Test
    public void testInvalidShelterManagerCloseReliefRequest() {

    }

    @Test
    public void testWithLocationGetShelterTimes() {

    }

    @Test
    public void testNoLocationGetShelterTimes() {

    }

    @Test
    public void testValidCheckIntoShelter() {

    }

    @Test
    public void testVolunteerCheckIntoShelter() {

    }

    @Test
    public void testNoTaskCheckIntoShelter() {

    }

    @Test
    public void testVolunteerGetCurrentReliefRequest() {

    }

    @Test
    public void testUserGetCurrentReliefRequest() {

    }

    @Test
    public void testNoTaskGetCurrentReliefRequest() {

    }

    @Test
    public void testUserGetSortedShelters() {

    }

    @Test
    public void testVolunteerNoTaskGetSortedShelters() {

    }

    @Test
    public void testVolunteerWithTaskGetSortedShelters() {

    }

    @Test
    public void testValidSelectShelter() {

    }

    @Test
    public void testInvalidSelectShelter() {

    }

    @Test
    public void testValidExportTripInformation() {

    }

    @Test
    public void testNoShelterExportTripInformation() {

    }

    @Test
    public void testNoTaskExportTripInformation() {

    }

    @Test
    public void testValidSetCurrentUserReliefNeeds() {

    }

    @Test
    public void testInvalidSetCurrentUserReliefNeeds() {

    }

    @Test
    public void testAddValidMemberToReliefGroup() {

    }

    @Test
    public void testAddInalidMemberToReliefGroup() {

    }

    @Test
    public void testValidSubmitGroupReliefRequest() {

    }

    @Test
    public void testNoGroupSubmitGroupReliefRequest() {

    }
}
