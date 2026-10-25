package com.pending.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.pending.model.data.serializables.ReliefRequest;
import com.pending.model.data.serializables.Shelter;
import com.pending.model.enums.LivingBeingType;
import com.pending.model.enums.ReliefType;

/**
 * Provides terminal-based demonstration scenarios for the Hurricane Relief
 * System. It displays user actions and sends requests to the Application.
 */
public class ConsoleUI {
    private static final String DEMO_REQUEST_DESCRIPTION =
            "Need transportation to the nearest pet-friendly shelter.";

    private final Application app;
    private final Scanner scanner;

    /**
     * Creates the console user interface.
     */
    public ConsoleUI() {
        app = Application.getInstance();
        scanner = new Scanner(System.in);
    }

    /**
     * Runs the primary presentation scenarios.
     */
    public void run() {
        // readAndPrintReliefRequests();
        // readAndPrintSheltersScenario();
        createReliefRequestScenario();
        // readAndPrintReliefRequests();
        // acceptReliefRequestAndCheckIntoShelterScenario();

        printHeader("DEMONSTRATION COMPLETE");
    }

    /**
     * Closes the console scanner.
     */
    public void close() {
        scanner.close();
    }

    /**
     * Prints a formatted heading for a console section.
     *
     * @param title the heading text to display
     */
    private void printHeader(String title) {
        System.out.println();
        System.out.println("============================================================");
        System.out.println(" " + title);
        System.out.println("============================================================");
    }

    /**
     * Prints a formatted success or failure message.
     *
     * @param success whether the action succeeded
     * @param successMessage message displayed when successful
     * @param failureMessage message displayed when unsuccessful
     */
    private void printResult(boolean success, String successMessage,
            String failureMessage) {
        System.out.println(success ? "[SUCCESS] " + successMessage
                : "[FAILED] " + failureMessage);
    }

    /**
     * Prints a formatted success or failure message.
     *
     * @param email the email address used to log in
     * @param password the password used to log in
     */
    public boolean login(String email, String password) {
        System.out.println("\nLogin:\nPlease enter your email and password:\n\temail> " + email);
        System.out.println("\tpassword> " + password);

        String loginResult = app.attemptLogin(email, password);
        if (loginResult == null) {
            System.out.println("Login Failed");
            return false;
        }

        System.out.println("\n" + app.getUserFullName() + " is now logged in.");
        System.out.println("Profile: " + loginResult);
        return true;
    }

    public void readAndPrintReliefRequests() {
        printHeader("CURRENT RELIEF REQUESTS");

        ArrayList<String> requests = app.getRequests();

        if (requests.isEmpty()) {
            System.out.println("No relief requests are currently available.");
            return;
        }

        System.out.println("Total requests: " + requests.size());

        for (int i = 0; i < requests.size(); i++) {
            System.out.println();
            System.out.println("Request " + (i + 1) + ":");
            System.out.println(requests.get(i));
        }
    }

    /**
     * Gets and prints all shelters.
     */
    public void readAndPrintSheltersScenario() {
        printHeader("AVAILABLE SHELTERS");

        ArrayList<String> shelters = app.getShelters();

        if (shelters.isEmpty()) {
            System.out.println("No shelters are currently available.");
            return;
        }

        for (int i = 0; i < shelters.size(); i++) {
            System.out.println();
            System.out.println("Shelter " + (i + 1) + ":");
            System.out.println(shelters.get(i));
        }
    }

    /**
     * Attempts a login and displays an appropriate result.
     *
     * @param email the account email address
     * @param password the account password
     * @return true if login succeeds; otherwise false
     */
    public boolean loginScenario(String email, String password) {
        System.out.println("Email: " + email);

        String loginResult = app.attemptLogin(email, password);

        if (loginResult == null) {
            System.out.println("[FAILED] Login was unsuccessful.");
            return false;
        }

        System.out.println("[SUCCESS] " + app.getUserFullName()
                + " is now logged in.");
        System.out.println("Location: " + app.getLocation());

        return true;
    }

    /**
     * Demonstrates account creation and relief-request submission.
     */
    public void createReliefRequestScenario() {
        printHeader("SCENARIO 1: CREATE A RELIEF REQUEST");

        String firstName = "James";
        String lastName = "Carter";
        String email = "jimmycarter@gmail.com";
        String password = "Buddy1&";
        LocalDate dateOfBirth = LocalDate.parse("1952-11-02");

        System.out.println("Creating an account for James Carter");
        boolean accountCreationSuccess = app.requestCreateAccount(
            firstName,
            lastName,
            dateOfBirth,
            password,
            "32 Legare St, Charleston, SC, 29401",
            email,
            "8439847569",
            "CALL",
            "doriscarter@gmail.com",
            "8433458910",
            "TEXT"
        );
        
        if (!accountCreationSuccess) {
            System.out.println("Account creation failed");
        }

        login(email, password);

        System.out.println("\nCreating a Relief Request:");
        app.setCurrentUserReliefNeeds(new ArrayList<>(
            List.of(ReliefType.TRANSPORTATION, ReliefType.SHELTER, ReliefType.MEDICAL_CARE)
        ));

        // add family
        app.startNewReliefGroup();

        System.out.println("\tAdding Doris Carter (HUMAN) to Relief Group");
        app.addMemberToReliefGroup(
            "Doris",
            "Carter",
            LocalDate.parse("1954-05-23"),
            LivingBeingType.HUMAN,
            new ArrayList<>(List.of(ReliefType.TRANSPORTATION, ReliefType.SHELTER))
        );

        System.out.println("\tAdding Sophie Carter (HUMAN) to Relief Group");
        app.addMemberToReliefGroup(
                "Sophie",
                "Carter",
                LocalDate.parse("2014-08-20"),
                LivingBeingType.HUMAN,
                new ArrayList<>(List.of(ReliefType.TRANSPORTATION, ReliefType.SHELTER, ReliefType.MEDICAL_CARE))
        );

        System.out.println("\tAdding Buddy Carter (PET) to Relief Group");
        app.addMemberToReliefGroup(
                "Buddy",
                "Carter",
                LocalDate.parse("2021-01-15"),
                LivingBeingType.PET,
                new ArrayList<>(List.of(ReliefType.TRANSPORTATION, ReliefType.PET_SHELTER))
        );

        System.out.println(app.submitGroupReliefRequest("Need a Ford F-350 to give us a ride to the nearest pet-friendly shelter") ?
            "\nRequest posted successfully:" : "\nRequest upload failed.");

        System.out.println(app.getUserString());

        System.out.println("Logging out");
        app.logout();
        System.out.println("Requesting user logged out.");
    }

    /**
     * Finds the newest request with the provided description.
     *
     * @param reliefRequests the available relief requests
     * @param description the description to search for
     * @return the matching request, or null if it does not exist
     */
    private ReliefRequest findRequestByDescription(
            ArrayList<ReliefRequest> reliefRequests, String description) {

        for (int i = reliefRequests.size() - 1; i >= 0; i--) {
            ReliefRequest request = reliefRequests.get(i);

            if (description.equals(request.getDescription())) {
                return request;
            }
        }

        return null;
    }

    /**
     * Demonstrates accepting a request, checking family members into a
     * shelter, and completing the request.
     */
    public void acceptReliefRequestAndCheckIntoShelterScenario() {
        printHeader("SCENARIO 2: ASSIGN AND COMPLETE A RELIEF REQUEST");

        String volunteerEmail = "rosa.delgado@example.com";
        String volunteerPassword = "RosaStrong2026!";

        System.out.println("Logging in as a volunteer...");

        if (!loginScenario(volunteerEmail, volunteerPassword)) {
            return;
        }

        ReliefRequest reliefRequest = findRequestByDescription(
                app.getReliefRequests(), DEMO_REQUEST_DESCRIPTION);

        if (reliefRequest == null) {
            System.out.println("[FAILED] The demonstration request was not found.");
            app.logout();
            return;
        }

        System.out.println();
        System.out.println("Selected request:");
        System.out.println(reliefRequest);

        boolean requestAccepted = app.acceptReliefRequest(reliefRequest);

        printResult(requestAccepted,
                "Volunteer accepted the relief request.",
                "Volunteer could not accept the relief request.");

        app.logout();
        System.out.println("Volunteer logged out.");

        String managerEmail = "felix.mendelssohn@example.com";
        String managerPassword = "S@xUndV10lins";

        System.out.println();
        System.out.println("Logging in as the shelter manager...");

        if (!loginScenario(managerEmail, managerPassword)) {
            return;
        }

        boolean checkedIn = app.checkIntoShelter(
                reliefRequest.getLivingBeings());

        printResult(checkedIn,
                "Family members were checked into the shelter.",
                "The family could not be checked into the shelter.");

        boolean requestCompleted = app.completeRequest(reliefRequest);

        printResult(requestCompleted,
                "Relief request was marked as completed.",
                "The relief request could not be completed.");

        app.logout();
    }

    public void openAndMatchRequestScenario() {
        // Rosa logs in
        String email = "rosa.delgado@example.com";
        String password = "RosaStrong2026!";

        // Show Rosa's profile
        if (!login(email, password)) {
            return;
        }

        
        

        // Rosa sees 7 relief requests sorted based on ReliefType and proximity

        // App recommneds the Request to her

        // Rosa can open the request to see the information on the family
    }

    public void findShelterAndAssignRequest() {
        printHeader("OPTIONAL: FIND A SHELTER AND EXPORT TRIP INFORMATION");

        if (!loginScenario("rosa.delgado@example.com",
                "RosaStrong2026!")) {
            return;
        }

        ArrayList<ReliefRequest> requests = app.getReliefRequests();

        if (requests.isEmpty()) {
            System.out.println("[FAILED] No requests are available.");
            app.logout();
            return;
        }

        ReliefRequest reliefRequest = requests.getFirst();

        if (!app.openReliefRequest(reliefRequest)) {
            System.out.println("[FAILED] Could not open the relief request.");
            app.logout();
            return;
        }

        ArrayList<Shelter> shelters = app.getSortedShelters();

        if (shelters == null || shelters.isEmpty()) {
            System.out.println("[FAILED] No suitable shelters were found.");
            app.logout();
            return;
        }

        Shelter selectedShelter = shelters.getFirst();

        System.out.println("Selected shelter:");
        System.out.println(selectedShelter);

        app.setShelter(selectedShelter);

        printResult(app.acceptReliefRequest(reliefRequest),
                "Volunteer accepted the relief request.",
                "Volunteer could not accept the relief request.");

        printResult(app.exportTripInformation(),
                "Trip information was exported successfully.",
                "Trip information could not be exported.");

        app.logout();
    }

    /**
     * Demonstrates role permissions by attempting a manager-only action.
     */
    public void rolePermissionScenario() {
        printHeader("OPTIONAL: ROLE PERMISSION CHECK");

        if (!loginScenario("rosa.delgado@example.com",
                "RosaStrong2026!")) {
            return;
        }

        printResult(app.closeReliefRequest(),
                "Relief request was completed.",
                "Only a shelter manager can complete a relief request.");

        app.logout();
    }

    /**
     * Demonstrates account creation and login for an additional sample user.
     */
    public void createAccountAndLoginScenario() {
        String email = "newestemail@gmail.com";
        String password = "strong_p@ssword";

        printHeader("OPTIONAL: CREATE AN ACCOUNT");

        boolean accountCreated = app.requestCreateAccount(
                "Joe",
                "Smith",
                LocalDate.of(1997, 12, 10),
                password,
                "300 Main St, Columbia, SC, 29208",
                email,
                "",
                "EMAIL",
                "bestfriend@gmail.com",
                "8033448978",
                "CALL"
        );

        printResult(accountCreated,
                "Account created successfully.",
                "Account already exists or could not be created.");

        loginScenario(email, password);
        app.logout();
    }

    /**
     * Prints estimated driving times to every shelter.
     * Requires a configured MAP_API_KEY.
     */
    public void getShelterTimes() {
        ArrayList<String> shelters = app.getShelters();
        ArrayList<Double> shelterTimes = app.getShelterTimes();

        if (shelterTimes == null || shelters.size() != shelterTimes.size()) {
            System.out.println("[FAILED] Shelter travel times are unavailable.");
            return;
        }

        for (int i = 0; i < shelters.size(); i++) {
            System.out.println(shelters.get(i));
            System.out.println("\tEstimated driving time: "
                    + (int) (shelterTimes.get(i) / 60) + " minutes");
        }
    }

    /**
     * Starts the terminal demonstration.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        ui.run();
        ui.close();
    }
}