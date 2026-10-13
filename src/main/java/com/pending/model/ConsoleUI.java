package com.pending.model;

import java.util.ArrayList;
import java.util.Scanner;
import com.pending.model.data.serializables.Shelter;
import java.util.ArrayList;

public class ConsoleUI {
    private Scanner scanner;
    private Application app;

    public ConsoleUI() {
        app = Application.getInstance();
    }

    public void run() {
        readAndPrintSheltersScenario();
    }

    public void loginScenario() {
        System.out.println("\nPlease enter your email and password:\nemail> heisenberg@aol.com");
        System.out.println("password> !mTh3Cook");
        if(!app.attemptLogin("heisenberg@aol.com", "!mTh3Cook")) {
            System.out.println("Login Failed");
            return;
        }
        System.out.println(app.getUserFullName() + "is now logged in");
    }

    public void createAccountScenario() {
        System.out.println("Create an account. Please enter the following information:");
        System.out.println("Contact Infomation");
        System.out.println("\temail> newestemail@gmail.com");
        System.out.println("\tphone> *skipped*");
        System.out.println("\tcontact_preference> email");
        System.out.println("Emergency Contact Information");
        System.out.println("\temail> bestfriend@gmail.com");
        System.out.println("\tphone> 8033448978");
        System.out.println("\tcontact_preference> CALL");
        System.out.println("Personal Infomation");
        System.out.println("\tfirst_name> Joe");
        System.out.println("\tlast_name> Smith");
        System.out.println("\tpassword> strong_p@ssword");
        System.out.println("\tlocation> 300 Main St, Columbia, SC, 29208");

        if(!app.requestCreateAccount("Joe", "Smith", "strong_p@ssword",
            "300 Main St, Columbia, SC, 29208",
            "newestemail@gmail.com", "", "EMAIL",
            "bestfriend@gmail.com", "8033448978", "CALL")) {
            System.out.println("Create Account Failed");
            return;
        }
        System.out.println("Account successsfully created");
        System.out.println(app.getUserFullName() + "is now logged in");

        // return requestCreateAccount(firstName, lastName, password, location, userContact, emergencyContact);
    }

    // Landon
    public void readAndPrintSheltersScenario() {
    ArrayList<Shelter> shelters = app.getShelters();

    System.out.println("\nShelters loaded from JSON:");

    if (shelters.isEmpty()) {
        System.out.println("No shelters found.");
    ArrayList<Shelter> shelters = app.getShelters();

    System.out.println("\nShelters loaded from JSON:");

    if (shelters.isEmpty()) {
        System.out.println("No shelters found.");
        return;
    }

    for (Shelter shelter : shelters) {
        System.out.println("Name: " + shelter.getName());
        System.out.println("Capacity: " + shelter.getOccupancy()
                + "/" + shelter.getCapacity());
        System.out.println("Status: " + shelter.getStatus());
        System.out.println();
    }
}
    }

    for (Shelter shelter : shelters) {
        System.out.println("Name: " + shelter.getName());
        System.out.println("Capacity: " + shelter.getOccupancy()
                + "/" + shelter.getCapacity());
        System.out.println("Status: " + shelter.getStatus());
        System.out.println();
    }
}
    

    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        ui.run();
    }
}
