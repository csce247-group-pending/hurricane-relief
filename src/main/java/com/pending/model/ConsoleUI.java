package com.pending.model;

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

    // Josh
    public void loginScenario() {
        System.out.println("\nPlease enter your email and password:\nemail> heisenberg@aol.com");
        System.out.println("password> !mTh3Cook");
        if(!app.attemptLogin("heisenberg@aol.com", "!mTh3Cook")) {
            System.out.println("Login Failed");
            return;
        }
        System.out.println(app.getUserFullName() + "is now logged in");
    }

    // Landon
    public void readAndPrintSheltersScenario() {
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
    

    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        ui.run();
    }
}
