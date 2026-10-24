package com.pending.model;

import com.pending.model.data.serializables.Contact;
import com.pending.model.data.serializables.LivingBeing;
import com.pending.model.data.serializables.Location;
import com.pending.model.data.serializables.ReliefRequest;
import com.pending.model.enums.ContactPreference;
import com.pending.model.enums.LivingBeingType;
import com.pending.model.enums.ReliefType;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
    }

    /**
     * Runs the current terminal scenarios.
     */
    public void run() {
        createReliefRequestScenario();
        readAndPrintReliefRequests();
        acceptReliefRequestAndCheckIntoShelterScenario();
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

        for (String request : app.getRequests()) {
            System.out.println(request);
        }
    }

    /**
     * Displays a login scenario and attempts to log in the user.
     *
     * @param email the email address used to log in
     * @param password the password used to log in
     */
    public void loginScenario(String email, String password) {
        System.out.println("\nLogin:\nPlease enter your email and password:\n\temail> " + email);
        System.out.println("\tpassword> " + password);

        if (!app.attemptLogin(email, password)) {
            System.out.println("Login Failed");
            return;
        }

        System.out.println(app.getUserFullName() + " is now logged in");
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

        if (!app.requestCreateAccount("Joe", "Smith", LocalDate.of(1997, 12, 10), password,
                "300 Main St, Columbia, SC, 29208",
                email, "", "EMAIL",
                "bestfriend@gmail.com", "8033448978", "CALL")) {
            System.out.println("Create Account Failed");
            return;
        }

        System.out.println("Account successfully created");

        loginScenario(email, password);
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

        loginScenario("jane.doe.chs@gmail.com", "SecurePassword123!");

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

        boolean accountCreationSuccess = app.requestCreateAccount(
                firstName,
                lastName,
                dateOfBirth,
                password,
                new Location("SC", "Charleston", "32 Legare St", "29401"),
                new Contact("jimmycarter@gmail.com", "8439847569", ContactPreference.CALL),
                new Contact("doriscarter@gmail.com", "8433458910", ContactPreference.TEXT)
        );
        if (!accountCreationSuccess) {
            System.out.println("Account creation failed");
        }

        if (!app.attemptLogin("jimmycarter@gmail.com", password)) {
            System.out.println("Login failed");
            return;
        }

        app.getCurrentUser().setReliefNeeds(new ArrayList<>(List.of(ReliefType.TRANSPORTATION, ReliefType.SHELTER)));

        ArrayList<LivingBeing> family = new ArrayList<>();
        family.add((LivingBeing) app.getCurrentUser());
        family.add(new LivingBeing(
                "Doris",
                "Carter",
                LocalDate.parse("1954-05-23"),
                LivingBeingType.HUMAN,
                new ArrayList<>(List.of(ReliefType.TRANSPORTATION, ReliefType.SHELTER))
        ));
        family.add(new LivingBeing(
                "Sophie",
                "Carter",
                LocalDate.parse("2014-08-20"),
                LivingBeingType.HUMAN,
                new ArrayList<>(List.of(ReliefType.TRANSPORTATION, ReliefType.SHELTER, ReliefType.MEDICAL_CARE))
        ));
        family.add(new LivingBeing(
                "Buddy",
                "Carter",
                LocalDate.parse("2021-01-15"),
                LivingBeingType.PET,
                new ArrayList<>(List.of(ReliefType.TRANSPORTATION, ReliefType.PET_SHELTER))
        ));
        System.out.println(app.createReliefRequest("Need a Ford F-350 to give us a ride to the nearest pet-friendly shelter", family)?
                "Request posted successfully." : "Request upload failed.");
        app.logout();
    }

    public void acceptReliefRequestAndCheckIntoShelterScenario() {
        String email = "rosa.delgado@example.com";
        String password = "RosaStrong2026!";
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);

        loginScenario(email, password);

        ReliefRequest reliefRequest = app.getReliefRequests().getFirst();
        app.acceptReliefRequest(reliefRequest);
        app.logout();

        email = "pianoboiii1809@pigeonmail.old";
        password = "S@xUndV10lins";

        loginScenario(email, password);

        app.checkIntoShelter(reliefRequest.getLivingBeings());
        app.completeRequest(reliefRequest);

        app.logout();
    }

    /**
     * Starts the terminal user interface.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        ui.run();
    }
}