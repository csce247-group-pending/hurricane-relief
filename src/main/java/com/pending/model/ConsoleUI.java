package com.pending.model;

import java.time.LocalDate;
import java.util.ArrayList;
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
        readAndPrintSheltersScenario();
        loginScenario("heisenberg@aol.com", "!mTh3Cook");
        // createAccountAndLoginScenario();
        readAndPrintReliefRequests();
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