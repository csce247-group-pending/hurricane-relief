package com.pending.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Provides terminal-based demonstration scenarios for the Hurricane Relief
 * System. It displays user actions and sends requests to the Application.
 */
public class ConsoleUI {

    // ANSI Color codes
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String TEAL = "\u001B[36m";

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
     * Runs the primary presentation scenarios with interactive pauses
     * between each scenario.
     */
    public void run() {
        createReliefRequestScenario();
        pauseForNextScenario("Scenario 2: Open and Match Request");

        openAndMatchRequestScenario();
        pauseForNextScenario(
                "Scenario 3: Find a Shelter and Assign Request");

        findShelterAndAssignRequest();
        pauseForNextScenario("Scenario 4: Export Trip Information");

        exportTripInformationScenario();
        pauseForNextScenario("Scenario 5: Role Permission Check");

        rolePermissionScenario();
        pauseForNextScenario("Scenario 6: Check Into Shelter");

        checkIntoShelterScenario();

        printHeader("DEMONSTRATION COMPLETE");
    }

    /**
     * Pauses the demonstration until the user presses Enter.
     *
     * @param nextScenarioName the name of the upcoming scenario
     */
    private void pauseForNextScenario(String nextScenarioName) {
        System.out.println();
        System.out.print(YELLOW + "Press ENTER to continue to "
                + nextScenarioName + "..." + RESET);
        scanner.nextLine();
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
        System.out.println(TEAL
                + "============================================================"
                + RESET);
        System.out.println(TEAL + " " + title + RESET);
        System.out.println(TEAL
                + "============================================================"
                + RESET);
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

        System.out.println();
        System.out.println(success
                ? GREEN + "[SUCCESS] " + RESET
                        + GREEN + successMessage + RESET
                : RED + "[FAILED] " + RESET
                        + RED + failureMessage + RESET);
        System.out.println();
    }

    /**
     * Attempts a login and displays an appropriate result.
     *
     * @param email the account email address
     * @param password the account password
     * @return true if login succeeds; otherwise false
     */
    public boolean loginScenario(String email, String password) {
        System.out.println(YELLOW + "email> " + RESET + email);
        System.out.println(YELLOW + "password> " + RESET + password);

        String loginResult = app.attemptLogin(email, password);

        if (loginResult == null) {
            printResult(false, "", "Login was unsuccessful.");
            return false;
        }

        printResult(true, app.getUserFullName() + " is now logged in.", "");
        System.out.println("Profile: " + RESET + loginResult);

        return true;
    }

    /**
     * Prints all relief requests available to the currently logged-in user.
     */
    private void printReliefRequests() {
        printHeader("CURRENT RELIEF REQUESTS");

        ArrayList<String> requests = app.getReliefRequestStrings();

        if (requests.isEmpty()) {
            System.out.println(YELLOW
                    + "No relief requests are currently available."
                    + RESET);
            return;
        }

        System.out.println("Total requests: " + requests.size() + RESET);

        for (int i = 0; i < requests.size(); i++) {
            System.out.println();
            System.out.println("Request " + (i + 1) + ":" + RESET);
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
            System.out.println(YELLOW
                    + "No shelters are currently available."
                    + RESET);
            return;
        }

        for (int i = 0; i < shelters.size(); i++) {
            System.out.println();
            System.out.println("Shelter " + (i + 1) + ":" + RESET);
            System.out.println(shelters.get(i));
        }
    }

    /**
     * Demonstrates creating an account and submitting a relief request
     * for a family that includes a pet.
     */
    public void createReliefRequestScenario() {
        printHeader("SCENARIO 1: CREATE A RELIEF REQUEST");

        String firstName = "James";
        String lastName = "Carter";
        String email = "jimmycarter@gmail.com";
        String password = "Buddy1&";
        String dateOfBirth = "1952-11-02";

        System.out.println("Creating an account for James Carter..." + RESET);

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

        printResult(accountCreationSuccess,
                "Account created for " + firstName + " " + lastName + ".",
                "Account creation failed (or account already exists).");

        loginScenario(email, password);

        System.out.println("\nCreating a Relief Request:" + RESET);

        app.setCurrentUserReliefNeeds(new ArrayList<>(
                List.of("TRANSPORTATION", "SHELTER", "MEDICAL_CARE")
        ));

        app.startNewReliefGroup();

        System.out.println(TEAL
                + "\t+ Adding Doris Carter (HUMAN) to Relief Group" + RESET);

        app.addMemberToReliefGroup(
                "Doris",
                "Carter",
                "1954-05-23",
                "HUMAN",
                new ArrayList<>(List.of("TRANSPORTATION", "SHELTER"))
        );

        System.out.println(TEAL
                + "\t+ Adding Sophie Carter (HUMAN) to Relief Group" + RESET);

        app.addMemberToReliefGroup(
                "Sophie",
                "Carter",
                "2014-08-20",
                "HUMAN",
                new ArrayList<>(List.of(
                        "TRANSPORTATION", "SHELTER", "MEDICAL_CARE"))
        );

        System.out.println(TEAL
                + "\t+ Adding Buddy Carter (PET) to Relief Group" + RESET);

        app.addMemberToReliefGroup(
                "Buddy",
                "Carter",
                "2021-01-15",
                "PET",
                new ArrayList<>(List.of("TRANSPORTATION", "PET_SHELTER"))
        );

        System.out.println();

        boolean requestSubmitted = app.submitGroupReliefRequest(
                "Need a Ford F-350 to give us a ride to the nearest "
                        + "pet-friendly shelter"
        );

        printResult(requestSubmitted,
                "Request posted successfully:",
                "Request upload failed.");

        System.out.println(app.getUserString());

        System.out.println(YELLOW + "\nLogging out..." + RESET);
        app.logout();
        System.out.println(GREEN + "Requesting user logged out." + RESET);
    }

    /**
     * Demonstrates a volunteer opening a relief request that has been
     * prioritized based on skills and proximity.
     */
    public void openAndMatchRequestScenario() {
        printHeader("SCENARIO 2: OPEN AND MATCH REQUEST");

        String email = "rosa.delgado@example.com";
        String password = "RosaStrong2026!";

        System.out.println();

        if (!loginScenario(email, password)) {
            return;
        }

        System.out.println();
        printReliefRequests();

        int requestIndex = 0;

        System.out.println("\nChoose a Request. Requests are sorted by "
                + "Relief Needs and Proximity." + RESET);
        System.out.println(YELLOW + "choice> " + RESET + (requestIndex + 1));

        System.out.println(TEAL + "\nOpening Request "
                + (requestIndex + 1) + "..." + RESET);

        if (!app.openReliefRequest(requestIndex)) {
            printResult(false, "", "Failed Opening Request");
        }

        System.out.println(app.getCurrentReliefRequestString());

        System.out.println(YELLOW + "Logging Out "
                + app.getUserFullName() + "..." + RESET);

        app.logout();
    }

    /**
     * Demonstrates finding suitable shelters and assigning a request to a
     * volunteer.
     */
    public void findShelterAndAssignRequest() {
        printHeader("SCENARIO 3: FIND A SHELTER AND ASSIGN REQUEST");

        System.out.println();

        if (!loginScenario("rosa.delgado@example.com",
                "RosaStrong2026!")) {
            return;
        }

        System.out.println();

        ArrayList<String> requests = app.getReliefRequestStrings();

        if (requests.isEmpty()) {
            printResult(false, "", "No requests are available.");
            app.logout();
            return;
        }

        System.out.println("\nSearching for the nearest Shelters matching "
                + "your request's needs..." + RESET);

        ArrayList<String> shelters = app.getSortedShelterStrings();

        if (shelters == null || shelters.isEmpty()) {
            printResult(false, "", "No suitable shelters were found.");
            app.logout();
            return;
        }

        for (int i = 0; i < shelters.size(); i++) {
            System.out.println((i + 1) + ". " + RESET + shelters.get(i));
        }

        int choice = 0;

        System.out.println("\nEnter the number of the shelter you want: "
                + RESET);
        System.out.println(YELLOW + "\tchoice> " + RESET + (choice + 1));

        app.selectShelter(choice);

        System.out.println(TEAL + "\nSaving Chosen Shelter:\n"
                + RESET + app.getShelter());

        printResult(app.acceptCurrentReliefRequest(),
                "Volunteer accepted the relief request.",
                "Volunteer could not accept the relief request.");
    }

    /**
     * Demonstrates exporting trip information for the volunteer's assigned
     * request and selected shelter.
     */
    public void exportTripInformationScenario() {
        printHeader("SCENARIO 4: EXPORT TRIP INFORMATION");

        printResult(app.exportTripInformation(),
                "Trip information was exported successfully.",
                "Trip information could not be exported.");

        app.logout();
    }

    /**
     * Demonstrates role permissions by attempting a shelter-manager-only
     * action while logged in as a volunteer.
     */
    public void rolePermissionScenario() {
        printHeader("SCENARIO 5: ROLE PERMISSION CHECK");

        if (!loginScenario("rosa.delgado@example.com",
                "RosaStrong2026!")) {
            return;
        }

        System.out.println();

        printResult(!app.closeReliefRequest(),
                "Only a shelter manager can complete a relief request.",
                "Relief request was completed.");

        System.out.println(YELLOW + "Logging out "
                + app.getUserFullName() + "..." + RESET);

        app.logout();
    }

    /**
     * Demonstrates checking family members into a shelter and completing
     * their relief request.
     */
    public void checkIntoShelterScenario() {
        printHeader("SCENARIO 6: CHECK INTO SHELTER");

        String managerEmail = "felix.mendelssohn@sheltermanager.org";
        String managerPassword = "S@xUndV10lins";

        System.out.println();
        System.out.println("Logging in as the shelter manager..." + RESET);

        if (!loginScenario(managerEmail, managerPassword)) {
            return;
        }

        printReliefRequests();

        int requestIndex = 0;
        app.openReliefRequest(requestIndex);

        System.out.println("\n" + app.getShelter());
        System.out.println(TEAL + "Attempting Check In..." + RESET);

        boolean checkedIn = app.checkIntoShelter();

        printResult(checkedIn,
                "Family members were checked into the shelter.",
                "The family could not be checked into the shelter.");

        boolean requestCompleted = app.closeReliefRequest();

        printResult(requestCompleted,
                "Relief request was marked as completed.",
                "The relief request could not be completed.");

        printReliefRequests();

        System.out.println("\nShelter After:" + RESET
                + "\n" + app.getShelter());

        app.logout();
    }

    /**
     * Demonstrates creating and logging in to an additional sample account.
     */
    public void createAccountAndLoginScenario() {
        String email = "newestemail@gmail.com";
        String password = "strong_p@ssword";

        printHeader("OPTIONAL: CREATE AN ACCOUNT");

        boolean accountCreated = app.requestCreateAccount(
                "Joe",
                "Smith",
                "1997-12-10",
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
            printResult(false, "", "Shelter travel times are unavailable.");
            return;
        }

        for (int i = 0; i < shelters.size(); i++) {
            System.out.println(shelters.get(i));
            System.out.println(TEAL + "\tEstimated driving time: "
                    + (int) (shelterTimes.get(i) / 60)
                    + " minutes" + RESET);
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