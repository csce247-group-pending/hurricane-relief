package com.pending.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.pending.model.data.serializables.Contact;
import com.pending.model.data.serializables.LivingBeing;
import com.pending.model.data.serializables.Location;
import com.pending.model.data.serializables.ReliefRequest;
import com.pending.model.enums.ContactPreference;
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

    /**
     * Creates the console user interface.
     */
    public ConsoleUI() {
        app = Application.getInstance();
    }

    /**
     * Runs the demonstration scenarios used during the presentation.
     */
    public void run() {
        printHeader("HURRICANE RELIEF SYSTEM DEMONSTRATION");

        createReliefRequestScenario();
        readAndPrintReliefRequests();
        acceptReliefRequestAndCheckIntoShelterScenario();

        printHeader("DEMONSTRATION COMPLETE");
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
     * Gets and displays all current relief requests.
     */
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
     * Attempts a login and displays an appropriate result.
     *
     * @param email the account email address
     * @param password the account password
     * @return true if login succeeds; otherwise false
     */
    public boolean loginScenario(String email, String password) {
        System.out.println("Email: " + email);

        boolean loginSuccessful = app.attemptLogin(email, password);

        if (!loginSuccessful) {
            System.out.println("[FAILED] Login was unsuccessful.");
            return false;
        }

        System.out.println("[SUCCESS] " + app.getUserFullName()
                + " is now logged in.");
        System.out.println("Location: " + app.getLocation());

        return true;
    }

    /**
     * Creates a sample account and submits a relief request for a family
     * needing transportation and pet-friendly shelter assistance.
     */
    public void createReliefRequestScenario() {
        printHeader("SCENARIO 1: CREATE A RELIEF REQUEST");

        String firstName = "James";
        String lastName = "Carter";
        String email = "jimmycarter@gmail.com";
        String password = "Buddy1&";

        System.out.println("Creating an account for " + firstName + " "
                + lastName + "...");

        boolean accountCreated = app.requestCreateAccount(
                firstName,
                lastName,
                LocalDate.parse("1952-11-02"),
                password,
                new Location("SC", "Charleston", "32 Legare St", "29401"),
                new Contact(email, "8439847569", ContactPreference.CALL),
                new Contact("doriscarter@gmail.com", "8433458910",
                        ContactPreference.TEXT)
        );

        if (accountCreated) {
            System.out.println("[SUCCESS] Account created.");
        } else {
            System.out.println("[INFO] Account already exists; using the"
                    + " existing account for this demonstration.");
        }

        System.out.println();
        System.out.println("Logging in as the requesting user...");

        if (!loginScenario(email, password)) {
            return;
        }

        app.getCurrentUser().setReliefNeeds(new ArrayList<>(List.of(
                ReliefType.TRANSPORTATION,
                ReliefType.SHELTER
        )));

        ArrayList<LivingBeing> family = new ArrayList<>();

        family.add(app.getCurrentUser());

        family.add(new LivingBeing(
                "Doris",
                "Carter",
                LocalDate.parse("1954-05-23"),
                LivingBeingType.HUMAN,
                new ArrayList<>(List.of(
                        ReliefType.TRANSPORTATION,
                        ReliefType.SHELTER
                ))
        ));

        family.add(new LivingBeing(
                "Sophie",
                "Carter",
                LocalDate.parse("2014-08-20"),
                LivingBeingType.HUMAN,
                new ArrayList<>(List.of(
                        ReliefType.TRANSPORTATION,
                        ReliefType.SHELTER,
                        ReliefType.MEDICAL_CARE
                ))
        ));

        family.add(new LivingBeing(
                "Buddy",
                "Carter",
                LocalDate.parse("2021-01-15"),
                LivingBeingType.PET,
                new ArrayList<>(List.of(
                        ReliefType.TRANSPORTATION,
                        ReliefType.PET_SHELTER
                ))
        ));

        System.out.println();
        System.out.println("Submitting a request for " + family.size()
                + " family members, including one pet...");

        boolean requestCreated = app.createReliefRequest(
                DEMO_REQUEST_DESCRIPTION, family);

        printResult(requestCreated,
                "Relief request was submitted successfully.",
                "The relief request could not be submitted.");

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
        System.out.println("Shelter manager logged out.");
    }

    /**
     * Starts the terminal demonstration.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        ui.run();
    }
}