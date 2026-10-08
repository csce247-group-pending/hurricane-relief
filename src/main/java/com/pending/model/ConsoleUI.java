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
        
    }

    // Landon
    public void readAndPrintSheltersScenario() {

    }
    

    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        ui.run();
    }
}
