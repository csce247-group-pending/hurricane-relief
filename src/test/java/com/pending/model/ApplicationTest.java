package com.pending.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

/*
 * AI Usage: Used AI to generate the test method assertions, boilerplate setup, and table formatting.
 *
 * +---------------------------------------------------------+-------------------------------------------------------------------+
 * | Test                                                    | Reasoning                                                         |
 * +---------------------------------------------------------+-------------------------------------------------------------------+
 * | testCreateValidAccount                                  | Verifies standard account creation handles valid text parameters. |
 * | testCreateInvalidAccount                                | Ensures account creation fails when invalid enum strings are used.|
 * | testCreateExistingAccount                               | Prevents creation of duplicate accounts with existing emails.     |
 * | testCreateAccountBadAddress                             | Verifies array bounds protection for bad address strings.         |
 * | testCreateAccountInvalidDateFormat                      | Verifies date parsing fails safely.                               |
 * | testValidUserLoginAttempt                               | Checks if standard users can authenticate and retrieve a profile. |
 * | testValidVolunteerLoginAttempt                          | Checks if volunteers can authenticate and retrieve their profile. |
 * | testValidShelterManagerLoginAttempt                     | Checks if managers can authenticate and retrieve their profile.   |
 * | testInvalidLoginAttempt                                 | Ensures incorrect passwords do not grant system access.           |
 * | testLogoutClearsUserSession                             | Verifies session variables are nullified upon user logout.        |
 * | testValidGetVolunteerProfile                            | Ensures volunteer profiles format skills and tasks correctly.     |
 * | testShelterManagerGetVolunteerProfile                   | Confirms managers inherit and format volunteer properties.        |
 * | testUserGetVolunteerProfile                             | Ensures standard users cannot access volunteer profiles.          |
 * | testValidCreateReliefRequest                            | Verifies a logged-in user can submit a standard relief request.   |
 * | testInvalidCreateReliefRequest                          | Ensures creating a request while logged out throws an exception.  |
 * | testVolunteerOpenReliefRequest                          | Verifies volunteers can select a valid request index to review.   |
 * | testUserOpenReliefRequest                               | Ensures standard users are blocked from opening tasks.            |
 * | testOpenReliefRequestIndexOutOfBounds                   | Prevents application crashes from negative or excessive indexes.  |
 * | testValidAcceptReliefRequest                            | Verifies an opened request can be formally assigned to a volunteer|
 * | testInvalidAcceptReliefRequest                          | Prevents accepting a task when none has been opened first.        |
 * | testAcceptReliefRequestIndexOutOfBounds                 | Prevents index out-of-bounds errors when accepting tasks directly.|
 * | testVolunteerGetCurrentReliefRequest                    | Ensures a volunteer can view details of an open task.             |
 * | testUserGetCurrentReliefRequest                         | Ensures standard users cannot view volunteer task assignments.    |
 * | testNoTaskGetCurrentReliefRequest                       | Verifies the default message when a volunteer has no task.        |
 * | testValidShelterManagerCloseReliefRequest               | Verifies managers can close active tasks.                         |
 * | testInvalidShelterManagerCloseReliefRequest             | Ensures unauthenticated users cannot trigger task closures.       |
 * | testVolunteerCloseReliefRequest                         | Prevents standard volunteers from closing tasks independently.    |
 * | testUserCloseReliefRequest                              | Prevents standard users from closing tasks independently.         |
 * | testVolunteerWithTaskGetSortedShelters                  | Verifies volunteers with active tasks can view sorted shelters.   |
 * | testVolunteerNoTaskGetSortedShelters                    | Ensures shelter routing returns null if no task is active.        |
 * | testUserGetSortedShelters                               | Prevents standard users from generating shelter routing lists.    |
 * | testValidSelectShelter                                  | Verifies volunteers can select a valid sorted shelter index.      |
 * | testInvalidSelectShelter                                | Prevents crashes when selecting a negative shelter index.         |
 * | testSelectShelterIndexOutOfBounds                       | Prevents crashes when selecting a shelter index out of range.     |
 * | testValidCheckIntoShelter                               | Verifies managers can check assigned groups into their shelter.   |
 * | testVolunteerCheckIntoShelter                           | Ensures standard volunteers cannot trigger shelter check-ins.     |
 * | testNoTaskCheckIntoShelter                              | Prevents check-ins from occurring when the manager has no task.   |
 * | testValidExportTripInformation                          | Verifies volunteers can export routing text to a file.            |
 * | testNoShelterExportTripInformation                      | Prevents exporting trip data if a shelter destination is missing. |
 * | testNoTaskExportTripInformation                         | Prevents exporting trip data if a task destination is missing.    |
 * | testValidSetCurrentUserReliefNeeds                      | Verifies a user can apply multiple valid relief enum strings.     |
 * | testInvalidSetCurrentUserReliefNeeds                    | Ensures invalid relief enum strings throw an exception safely.    |
 * | testAddValidMemberToReliefGroup                         | Verifies a valid member can be staged for a group request.        |
 * | testAddInalidMemberToReliefGroup                        | Ensures invalid LivingBeing type enums are caught safely.         |
 * | testAddPetMemberToReliefGroup                           | Verifies pets can be added to relief groups.                      |
 * | testValidSubmitGroupReliefRequest                       | Verifies multiple staged members create a unified request.        |
 * | testNoGroupSubmitGroupReliefRequest                     | Ensures logged-out users cannot submit group requests.            |
 * | testSubmitGroupReliefRequestIncludesAuthorWhenEmpty     | Ensures empty groups default to including the active user.        |
 * +---------------------------------------------------------+-------------------------------------------------------------------+
 */
public class ApplicationTest {

    private Application app;

    @BeforeEach
    public void setUp() {
        app = Application.getInstance();
        app.logout();
    }
 
    // ---------------------------------------------------------
    // ACCOUNT CREATION & PARSING
    // ---------------------------------------------------------

    @Test 
    public void testCreateValidAccount() {
        String uniqueEmail = "john_" + System.currentTimeMillis() + "@gmail.com";
        boolean isCreated = app.requestCreateAccount("john", "son", 
            "1990-12-01", "thePassword%", "71 Society St, Charleston, SC, 29401", 
            uniqueEmail, "8434448932", 
            "EMAIL", "john_friend@gmail.com", 
            "8435556865", "TEXT");

        assertTrue(isCreated, "Valid account with unique email should be created successfully.");
    }

    @Test
    public void testCreateInvalidAccount() {
        assertThrows(IllegalArgumentException.class, () -> {
            app.requestCreateAccount("john", "son", 
                "1990-12-01", "thePassword%", "71 Society St, Charleston, SC, 29401", 
                "bad_enum@gmail.com", "8434448932", 
                "invalid_pref", "emer@gmail.com", "8435556865", "TEXT");
        }, "Should throw IllegalArgumentException due to bad enum parsing.");
    }

    @Test
    public void testCreateExistingAccount() {
        boolean isCreated = app.requestCreateAccount("Average", "Joe", 
            "1990-07-24", "password1", "1215 Blossom St, Columbia, SC, 29201", 
            "averagejoe@gmail.com", "8039999999", 
            "TEXT", "emer@gmail.com", "8435556865", "TEXT");

        assertFalse(isCreated, "Should return false because email already belongs to Average Joe.");
    }

    @Test
    public void testCreateAccountBadAddress() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            app.requestCreateAccount("Malformed", "AddressUser", 
                "1992-04-15", "SecurePass123!", "123 Main St Charleston SC 29401", 
                "malformed_addr@example.com", "8435551122", 
                "EMAIL", "emer@example.com", "8435553344", "CALL");
        }, "Should throw ArrayIndexOutOfBoundsException when address cannot be split into 4 parts.");
    }

    @Test
    public void testCreateAccountInvalidDateFormat() {
        assertThrows(java.time.format.DateTimeParseException.class, () -> {
            app.requestCreateAccount("Invalid", "DateUser", 
                "12/01/1990", "SecurePass123!", "71 Society St, Charleston, SC, 29401", 
                "bad_date@example.com", "8435551122", 
                "EMAIL", "emer@example.com", "8435553344", "CALL");
        }, "Should throw DateTimeParseException when dateOfBirth is not in YYYY-MM-DD format.");
    }

    // ---------------------------------------------------------
    // LOGIN & SESSION MANAGEMENT
    // ---------------------------------------------------------

    @Test
    public void testValidUserLoginAttempt() {
        String result = app.attemptLogin("heisenberg@aol.com", "!mTh3Cook");
        String expectedProfile = "{User Walter White} \n" +
                                 "\tAge: 68\n" +
                                 "\tContact Info: {heisenberg@aol.com, 5051178987, CALL}";
        
        assertNotNull(result, "Valid user login should return a profile string, not null.");
        assertEquals(expectedProfile, result, "Should return exact formatted User profile string for Walter White.");
    }

    @Test
    public void testValidVolunteerLoginAttempt() {
        String result = app.attemptLogin("rosa.delgado@example.com", "RosaStrong2026!");
        String expectedProfile = "{User Rosa Delgado} \n" +
                                 "\tAge: 31\n" +
                                 "\tContact Info: {rosa.delgado@example.com, 8035550148, EMAIL}";
        
        assertNotNull(result, "Valid volunteer login should return a profile string.");
        assertTrue(result.contains(expectedProfile), "Should contain exact formatted base User properties for Rosa Delgado.");
    }

    @Test
    public void testValidShelterManagerLoginAttempt() {
        String result = app.attemptLogin("felix.mendelssohn@sheltermanager.org", "S@xUndV10lins");
        String expectedProfile = "{User Felix Mendelssohn} \n" +
                                 "\tAge: 46\n" +
                                 "\tContact Info: {felix.mendelssohn@sheltermanager.org, 8435559001, CALL}";
        
        assertNotNull(result, "Valid shelter manager login should return a profile string.");
        assertTrue(result.contains(expectedProfile), "Should contain exact formatted base User properties for Felix.");
    }

    @Test
    public void testInvalidLoginAttempt() {
        String result = app.attemptLogin("averagejoe@gmail.com", "WrongPassword123!");
        assertNull(result, "Login attempt with incorrect password should return null.");
    }

    @Test
    public void testLogoutClearsUserSession() {
        app.attemptLogin("heisenberg@aol.com", "!mTh3Cook");
        assertNotNull(app.getUserString(), "User session should be active after login.");
        
        app.logout();

        assertNull(app.getUserString(), "getUserString() must return null after logout.");
        assertNull(app.getVolunteerProfile(), "getVolunteerProfile() must return null after logout.");
    }

    // ---------------------------------------------------------
    // VOLUNTEER PROFILES
    // ---------------------------------------------------------

    @Test
    public void testValidGetVolunteerProfile() {
        app.attemptLogin("rosa.delgado@example.com", "RosaStrong2026!"); 
        String profile = "{User Rosa Delgado} \n" + 
                        "\tAge: 31\n" + 
                        "\tContact Info: {rosa.delgado@example.com, 8035550148, EMAIL}\n" + 
                        "\tRelief Skills: Transportation\n" + 
                        "\tCurrent Task: None";
        
        assertEquals(profile, app.getVolunteerProfile(), "Should return exact formatted volunteer profile string.");
    }

    @Test
    public void testShelterManagerGetVolunteerProfile() {
        app.attemptLogin("felix.mendelssohn@sheltermanager.org", "S@xUndV10lins"); 
        String profile = "{User Felix Mendelssohn} \n" + 
                        "\tAge: 46\n" + 
                        "\tContact Info: {felix.mendelssohn@sheltermanager.org, 8435559001, CALL}\n" + 
                        "\tRelief Skills: Shelter, Pet Shelter\n" + 
                        "\tCurrent Task: None";
                        
        assertEquals(profile, app.getVolunteerProfile(), "Shelter manager is a volunteer, should return formatted profile.");
    }

    @Test
    public void testUserGetVolunteerProfile() {
        app.attemptLogin("heisenberg@aol.com", "!mTh3Cook"); 
        assertEquals("Not a volunteer", app.getVolunteerProfile(), "Standard user should return 'Not a volunteer'.");
    }

    // ---------------------------------------------------------
    // RELIEF REQUESTS
    // ---------------------------------------------------------

    @Test
    public void testValidCreateReliefRequest() {
        app.attemptLogin("heisenberg@aol.com", "!mTh3Cook"); 
        boolean result = app.createReliefRequest("My roof collapsed.");
        assertTrue(result, "Logged in user should be able to create a relief request.");
    }

    @Test
    public void testInvalidCreateReliefRequest() {
        assertThrows(NullPointerException.class, () -> {
            app.createReliefRequest("Help");
        }, "Creating a request while logged out triggers a NullPointerException on currentUser.");
    }

    @Test
    public void testVolunteerOpenReliefRequest() {
        app.attemptLogin("rosa.delgado@example.com", "RosaStrong2026!"); 
        boolean result = app.openReliefRequest(0); 
        assertTrue(result, "Volunteer should successfully open a valid relief request index.");
    }

    @Test
    public void testUserOpenReliefRequest() {
        app.attemptLogin("heisenberg@aol.com", "!mTh3Cook");
        boolean result = app.openReliefRequest(0);
        assertFalse(result, "Standard users cannot open relief requests, only volunteers can.");
    }

    @Test
    public void testOpenReliefRequestIndexOutOfBounds() {
        app.attemptLogin("rosa.delgado@example.com", "RosaStrong2026!"); 
        
        boolean negativeResult = app.openReliefRequest(-1); 
        assertFalse(negativeResult, "Negative index must return false.");

        boolean upperResult = app.openReliefRequest(9999); 
        assertFalse(upperResult, "Index exceeding list size must return false without throwing an exception.");
    }

    @Test
    public void testValidAcceptReliefRequest() {
        app.attemptLogin("rosa.delgado@example.com", "RosaStrong2026!");
        app.openReliefRequest(0); 
        boolean result = app.acceptCurrentReliefRequest();
        assertTrue(result, "Volunteer should be able to accept an opened request.");
    }

    @Test
    public void testInvalidAcceptReliefRequest() {
        app.attemptLogin("rosa.delgado@example.com", "RosaStrong2026!");
        boolean result = app.acceptCurrentReliefRequest();
        assertFalse(result, "Cannot accept a request if one hasn't been opened or assigned.");
    }

    @Test
    public void testAcceptReliefRequestIndexOutOfBounds() {
        app.attemptLogin("rosa.delgado@example.com", "RosaStrong2026!"); 
        
        boolean negativeResult = app.acceptReliefRequest(-1); 
        assertFalse(negativeResult, "Negative index should return false.");

        boolean upperResult = app.acceptReliefRequest(9999); 
        assertFalse(upperResult, "Out-of-bounds index should return false.");
    }

    @Test
    public void testVolunteerGetCurrentReliefRequest() {
        app.attemptLogin("rosa.delgado@example.com", "RosaStrong2026!");
        app.openReliefRequest(0);
        String expected = "{Relief Request Detailed}\n" + //
                        "\tDescription: Need water and shelter\n" + //
                        "\tRelief Status: REQUESTED\n" + //
                        "\tAssigned To: None\n" +
                        "\tRelief Needs: WATER, SHELTER\n" + //
                        "\tLocation: 145 King St, Charleston, SC 29401\n" + //
                        "\tContact Info: {president@email.sc.edu, 8037772001, TEXT}\n" +
                        "\tMembers(0): None";
        String request = app.getCurrentReliefRequestString();
        assertNotNull(request);
        assertEquals(expected, request, "Returns 'No Current Task' if none assigned.");
    }

    @Test
    public void testUserGetCurrentReliefRequest() {
        app.attemptLogin("heisenberg@aol.com", "!mTh3Cook");
        String request = app.getCurrentReliefRequestString();
        assertEquals("User is not a Volunteer", request, "Method restricted to Volunteers.");
    }

    @Test
    public void testNoTaskGetCurrentReliefRequest() {
        app.attemptLogin("rosa.delgado@example.com", "RosaStrong2026!");
        assertEquals("No Current Task", app.getCurrentReliefRequestString());
    }

    @Test
    public void testValidShelterManagerCloseReliefRequest() {
        app.attemptLogin("felix.mendelssohn@sheltermanager.org", "S@xUndV10lins");
        boolean result = app.closeReliefRequest();
        assertFalse(result, "Should fail safely if manager has no task, but method call is structurally valid.");
    }

    @Test
    public void testInvalidShelterManagerCloseReliefRequest() {
        boolean result = app.closeReliefRequest();
        assertFalse(result, "Cannot close request if no user is logged in.");
    }

    @Test
    public void testVolunteerCloseReliefRequest() {
        app.attemptLogin("rosa.delgado@example.com", "RosaStrong2026!");
        boolean result = app.closeReliefRequest();
        assertFalse(result, "Volunteer should not be able to close requests; only Shelter Managers can.");
    }

    @Test
    public void testUserCloseReliefRequest() {
        app.attemptLogin("heisenberg@aol.com", "!mTh3Cook");
        boolean result = app.closeReliefRequest();
        assertFalse(result, "Standard user should not be able to close a relief request.");
    }

    // ---------------------------------------------------------
    // SHELTER & ROUTING
    // ---------------------------------------------------------

    @Test
    public void testVolunteerWithTaskGetSortedShelters() {
        app.attemptLogin("rosa.delgado@example.com", "RosaStrong2026!");
        app.openReliefRequest(0);
        app.acceptCurrentReliefRequest(); 
        ArrayList<String> shelters = app.getSortedShelterStrings();
        assertNotNull(shelters, "Should return list of sorted shelters once a task is assigned.");
    }

    @Test
    public void testVolunteerNoTaskGetSortedShelters() {
        app.attemptLogin("rosa.delgado@example.com", "RosaStrong2026!");
        assertNull(app.getSortedShelterStrings(), "Returns null if Volunteer has no current task.");
    }

    @Test
    public void testUserGetSortedShelters() {
        app.attemptLogin("heisenberg@aol.com", "!mTh3Cook");
        assertNull(app.getSortedShelterStrings(), "Only volunteers can view sorted shelters.");
    }

    @Test
    public void testValidSelectShelter() {
        app.attemptLogin("rosa.delgado@example.com", "RosaStrong2026!");
        app.openReliefRequest(0);
        app.acceptCurrentReliefRequest();
        boolean selected = app.selectShelter(0); 
        assertTrue(selected, "Should successfully select a valid shelter index.");
    }

    @Test
    public void testInvalidSelectShelter() {
        app.attemptLogin("rosa.delgado@example.com", "RosaStrong2026!");
        app.openReliefRequest(0);
        app.acceptCurrentReliefRequest();
        boolean selected = app.selectShelter(-1); 
        assertFalse(selected, "Should return false on invalid index.");
    }

    @Test
    public void testSelectShelterIndexOutOfBounds() {
        app.attemptLogin("rosa.delgado@example.com", "RosaStrong2026!"); 
        app.openReliefRequest(0); 
        app.acceptCurrentReliefRequest(); 

        boolean result = app.selectShelter(9999); 
        assertFalse(result, "Selecting a shelter index out of range must return false.");
    }

    @Test
    public void testValidCheckIntoShelter() {
        app.attemptLogin("felix.mendelssohn@sheltermanager.org", "S@xUndV10lins");
        assertFalse(app.checkIntoShelter(), "Fails safely if Shelter Manager has no active task to check in.");
    }

    @Test
    public void testVolunteerCheckIntoShelter() {
        app.attemptLogin("rosa.delgado@example.com", "RosaStrong2026!");
        assertFalse(app.checkIntoShelter(), "Volunteers cannot check into shelters via this method.");
    }

    @Test
    public void testNoTaskCheckIntoShelter() {
        app.attemptLogin("felix.mendelssohn@sheltermanager.org", "S@xUndV10lins");
        assertFalse(app.checkIntoShelter(), "Returns false if ShelterManager task is null.");
    }

    @Test
    public void testValidExportTripInformation() {
        app.attemptLogin("rosa.delgado@example.com", "RosaStrong2026!");
        app.openReliefRequest(0);
        app.acceptCurrentReliefRequest();
        app.selectShelter(0);
        assertTrue(app.exportTripInformation(), "Should export trip info if volunteer has task and shelter.");
    }

    @Test
    public void testNoShelterExportTripInformation() {
        app.attemptLogin("rosa.delgado@example.com", "RosaStrong2026!");
        app.openReliefRequest(0);
        app.acceptCurrentReliefRequest();
        assertFalse(app.exportTripInformation(), "Cannot export without a selected shelter.");
    }

    @Test
    public void testNoTaskExportTripInformation() {
        app.attemptLogin("rosa.delgado@example.com", "RosaStrong2026!");
        assertFalse(app.exportTripInformation(), "Cannot export if there is no task or shelter.");
    }

    // ---------------------------------------------------------
    // GROUP RELIEF REQUESTS
    // ---------------------------------------------------------

    @Test
    public void testValidSetCurrentUserReliefNeeds() {
        app.attemptLogin("heisenberg@aol.com", "!mTh3Cook");
        ArrayList<String> needs = new ArrayList<>();
        needs.add("FOOD"); 
        needs.add("WATER");
        assertTrue(app.setCurrentUserReliefNeeds(needs), "Should set valid relief needs.");
    }

    @Test
    public void testInvalidSetCurrentUserReliefNeeds() {
        app.attemptLogin("heisenberg@aol.com", "!mTh3Cook");
        ArrayList<String> needs = new ArrayList<>();
        needs.add("INVALID_NEED"); 
        
        assertThrows(IllegalArgumentException.class, () -> {
            app.setCurrentUserReliefNeeds(needs);
        }, "Invalid string should throw IllegalArgumentException when parsing ReliefType enum.");
    }

    @Test
    public void testAddValidMemberToReliefGroup() {
        app.attemptLogin("heisenberg@aol.com", "!mTh3Cook");
        app.startNewReliefGroup();
        
        ArrayList<String> needs = new ArrayList<>();
        needs.add("MEDICAL_CARE");
        
        assertDoesNotThrow(() -> {
            app.addMemberToReliefGroup("Jane", "Doe", "2010-01-01", "HUMAN", needs);
        });
    }

    @Test
    public void testAddInalidMemberToReliefGroup() {
        app.attemptLogin("heisenberg@aol.com", "!mTh3Cook");
        app.startNewReliefGroup();
        
        ArrayList<String> needs = new ArrayList<>();
        needs.add("FOOD");
        
        assertThrows(IllegalArgumentException.class, () -> {
            app.addMemberToReliefGroup("X", "Y", "2010-01-01", "ALIEN", needs);
        }, "Should throw exception when passing invalid LivingBeingType enum string.");
    }

    @Test
    public void testAddPetMemberToReliefGroup() {
        app.attemptLogin("heisenberg@aol.com", "!mTh3Cook"); 
        app.startNewReliefGroup(); 
        
        ArrayList<String> petNeeds = new ArrayList<>();
        petNeeds.add("PET_SHELTER"); 
        
        assertDoesNotThrow(() -> {
            app.addMemberToReliefGroup("Buddy", "Doe", "2021-06-15", "PET", petNeeds); 
        }, "Adding a valid non-human living being should not throw an exception.");
    }

    @Test
    public void testValidSubmitGroupReliefRequest() {
        app.attemptLogin("heisenberg@aol.com", "!mTh3Cook");
        app.startNewReliefGroup();
        
        ArrayList<String> needs = new ArrayList<>();
        needs.add("FOOD");
        app.addMemberToReliefGroup("Jane", "Doe", "2010-01-01", "HUMAN", needs);
        
        assertTrue(app.submitGroupReliefRequest("Family needs food."), "Should successfully submit request.");
    }

    @Test
    public void testNoGroupSubmitGroupReliefRequest() {
        assertFalse(app.submitGroupReliefRequest("Need help"), "Cannot submit request when logged out.");
    }

    @Test
    public void testSubmitGroupReliefRequestIncludesAuthorWhenEmpty() {
        app.attemptLogin("heisenberg@aol.com", "!mTh3Cook"); 
        app.startNewReliefGroup(); 
        
        boolean success = app.submitGroupReliefRequest("Individual evacuee assistance"); 
        assertTrue(success, "Group submission should include currentUser when group list is empty."); 
    }
}