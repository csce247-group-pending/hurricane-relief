package com.pending.model;

import java.util.Scanner;

public class ConsoleUI {
    private Scanner scanner;
    private Application app;

    public ConsoleUI() {
        app = Application.getInstance();
    }

    public void run() {
        loginScenario();
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
        return;
    }
    

    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        ui.run();
    }
}
