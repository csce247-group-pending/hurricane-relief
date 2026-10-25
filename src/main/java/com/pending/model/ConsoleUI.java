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
 * Provides terminal-based scenarios for using the Hurricane Relief System.
 * It displays information and sends user actions to the Application class.
 */
public class ConsoleUI {
    private Scanner scanner;
    private Application app;

    /**
     * Creates the console user interface and gets the Application instance.
     */
    public ConsoleUI() {
        app = Application.getInstance();
        scanner = new Scanner(System.in);
    }

    /**
     * Runs the current terminal scenarios.
     */
    public void run() {
        // readAndPrintReliefRequests();
        // readAndPrintSheltersScenario();
        createReliefRequestScenario();
        // acceptReliefRequestAndCheckIntoShelterScenario();
        // findShelterAndAssignRequest();
        // exportTripInformation();
        // rolePermissionScenario();
        // openAndMatchRequestScenario();
    }

    public void close() {
        scanner.close();
    }

    /**
     * Gets and prints all relief requests in the terminal.
     */
    public void readAndPrintReliefRequests() {
        System.out.println();

        ArrayList<String> requests = app.getRequests();

        if (requests.isEmpty()) {
            System.out.println("No relief requests found");
        }

        System.out.println("Printing All Relief Requests:");

        for (String request : requests) {
            System.out.println(request);
        }
    }

    /**
     * Displays a login scenario and attempts to log in the user.
     *
     * @param email the email address used to log in
     * @param password the password used to log in
     */
    public void login(String email, String password) {
        System.out.println("\nLogin:\nPlease enter your email and password:\n\temail> " + email);
        System.out.println("\tpassword> " + password);

        String loginResult = app.attemptLogin(email, password);
        if (loginResult == null) {
            System.out.println("Login Failed");
            return;
        }

        System.out.println("\n" + app.getUserFullName() + " is now logged in.");
        System.out.println("Profile: " + loginResult);
    }

    /**
     * Displays an account-creation scenario and then logs in the new user.
     */
    public void createAccountAndLoginScenario() {
        String email = "newestemail@gmail.com";
        String password = "strong_p@ssword";

        System.out.println("Create an account. Please enter the following information:");
        System.out.println("Contact Infomation");
        System.out.println("\temail> " + email);
        System.out.println("\tphone> *skipped*");
        System.out.println("\tcontact_preference> email");
        System.out.println("Emergency Contact Information");
        System.out.println("\temail> bestfriend@gmail.com");
        System.out.println("\tphone> 8033448978");
        System.out.println("\tcontact_preference> CALL");
        System.out.println("Personal Infomation");
        System.out.println("\tfirst_name> Joe");
        System.out.println("\tlast_name> Smith");
        System.out.println("\tpassword> " + password);
        System.out.println("\tlocation> 300 Main St, Columbia, SC, 29208");

        if (!app.requestCreateAccount("Joe", "Smith", 
                LocalDate.of(1997, 12, 10), password,
                "300 Main St, Columbia, SC, 29208",
                email, "", "EMAIL",
                "bestfriend@gmail.com", "8033448978", "CALL")) {
            System.out.println("Create Account Failed");
            return;
        }

        System.out.println("Account successfully created");

        login(email, password);
    }

    /**
     * Gets and prints all shelters in the terminal.
     */
    public void readAndPrintSheltersScenario() {
        System.out.println();

        ArrayList<String> shelters = app.getShelters();

        if (shelters.isEmpty()) {
            System.out.println("No shelters found.");
            return;
        }

        System.out.println("Printing all Shelters:");

        for (String shelter : shelters) {
            System.out.println(shelter);
        }
    }

    public void getShelterTimes() {
        System.out.println();

        System.out.println("Getting Shelters");
        ArrayList<String> shelters = app.getShelters();
        if (shelters.isEmpty()) {
            System.out.println("No shelters found.");
            return;
        }

        System.out.println("Printing Shelters and Times");

        login("jane.doe.chs@gmail.com", "SecurePassword123!");

        ArrayList<Double> shelterTimes = app.getShelterTimes();
        if(shelterTimes == null) {
            System.out.println("[Error] Shelters not loaded");
            return;
        }

        if(shelters.size() != shelterTimes.size()) {
            System.out.println("[Error] Not all shelters and times loaded");
            return;
        }

        for(int i = 0; i < shelters.size(); ++i) {
            System.out.print(shelters.get(i));
            System.out.println("\n\tEst Time: " + (int) (shelterTimes.get(i)/60) + "m");
        } 
    }

    public void createReliefRequestScenario() {
        String firstName = "James";
        String lastName = "Carter";
        LocalDate dateOfBirth = LocalDate.parse("1952-11-02");
        String password = "Buddy1&";

        System.out.println("Creating an account for James Carter");
        boolean accountCreationSuccess = app.requestCreateAccount(
            firstName,
            lastName,
            dateOfBirth,
            password,
            "32 Legare St, Charleston, SC, 29401",
            "jimmycarter@gmail.com",
            "8439847569",
            "CALL",
            "doriscarter@gmail.com",
            "8433458910",
            "TEXT"
    );
        if (!accountCreationSuccess) {
            System.out.println("Account creation failed");
        }

        login("jimmycarter@gmail.com", password);


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
    }

    public void acceptReliefRequestAndCheckIntoShelterScenario() {
        String email = "rosa.delgado@example.com";
        String password = "RosaStrong2026!";
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);

        login(email, password);

        ReliefRequest reliefRequest = app.getReliefRequests().getFirst();
        app.acceptReliefRequest(reliefRequest);
        app.logout();

        email = "pianoboiii1809@pigeonmail.old";
        password = "S@xUndV10lins";

        login(email, password);

        app.checkIntoShelter(reliefRequest.getLivingBeings());
        app.closeReliefRequest();

        app.logout();
    }

    public void openAndMatchRequestScenario() {
        // Rosa logs in
        String email = "rosa.delgado@example.com";
        String password = "RosaStrong2026!";
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);

        login(email, password);

        // Show Rosa's profile


        // Rosa sees 7 relief requests sorted based on ReliefType and proximity

        // App recommneds the Request to her

        // Rosa can open the request to see the information on the family
    }

    public void findShelterAndAssignRequest() {
        String email = "rosa.delgado@example.com";
        String password = "RosaStrong2026!";
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);

        login(email, password);

        // TODO update relief request to be scenario relief request
        ReliefRequest reliefRequest = app.getReliefRequests().getFirst();
        app.openReliefRequest(reliefRequest);


        System.out.println("\nSearching for the nearest Shelters");
        // get list of shelters sorted
        ArrayList<Shelter> shelters = app.getSortedShelters();
        
        if (app.getSortedShelters() == null) {
            System.out.println("Error getting shelters");
            return;
        }

        for (int i = 0; i < shelters.size(); i++) {
            System.out.println((i + 1) + ". " + shelters.get(i));
        }

        int choice = 0;

        System.out.println("\nEnter the number of the shelter you want: ");
        System.out.println("\tchoice> " + (choice + 1));

        app.setShelter(shelters.get(choice));
        System.out.println("\nSaving Chosen Shelter:\n" + shelters.get(choice));

        // assign task
        System.out.println("\nAttempt to accept the Relief Request:\n" + reliefRequest);
        app.acceptReliefRequest(reliefRequest);

        // System.out.println("\nLogging out");
        // app.logout();

        // login("vol9@example.com","Password123!");

        // System.out.println("\nPrinting All Relief Requests:");
        // for (String request : app.getRequests()) {
        //     System.out.println(request);
        // }
        
    }

    public void exportTripInformation() {
        if(!app.exportTripInformation()) {
            System.out.println("Error exporting trip info: Scenario Failed");
        } else {
            System.out.println("Trip Exported Successfully to file");
        }
    }

    public void rolePermissionScenario() {
        // Rosa tries to mark request complete
        app.closeReliefRequest();
    }

    /**
     * Starts the terminal user interface.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        ui.run();
        ui.close();
    }
}