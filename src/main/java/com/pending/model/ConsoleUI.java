package com.pending.model;

import java.util.Scanner;

public class ConsoleUI {
    private Scanner scanner;

    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;

        while (running) {
            System.out.println("Welcome to this Hurricane Relief System!");
            System.out.println("Choose an option:");
            System.out.println("\t1. Read and Print Shelters");
            System.out.println("\tq. Quit");

            System.out.print("\nuser> ");

            String choice = scanner.nextLine();

            switch(choice) {
                case "1":
                    // talk to Application to read json
                    // talk to application to print Json
                    break;
                case "q":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid Choice.");
            }
        }

        scanner.close();
    }

}
