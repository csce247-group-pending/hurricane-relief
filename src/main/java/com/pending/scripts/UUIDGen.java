package com.pending.scripts;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Generates UUID values for use in JSON sample data.
 */
public class UUIDGen {

    /**
     * Generates and prints ten UUID values in JSON-property format.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        // System.out.println(UUID.randomUUID());
        printUUIDsAsJSON(10);
    }

    /**
     * Prints one UUID as an {@code id} JSON property.
     *
     * @param id the UUID to print
     */
    public static void printAsJSON(UUID id) {
        System.out.printf("\"id\": \"%s\"\n", id.toString());
    }

    /**
     * Creates a specified number of random UUID values.
     *
     * @param count the number of UUID values to generate
     * @return a list containing the generated UUID values
     */
    public static ArrayList<UUID> generateUUIDs(int count) {
        ArrayList<UUID> output = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            output.add(UUID.randomUUID());
        }

        return output;
    }

    /**
     * Generates UUID values and prints each one in JSON-property format.
     *
     * @param count the number of UUID values to generate and print
     */
    public static void printUUIDsAsJSON(int count) {
        for (UUID id : generateUUIDs(count)) {
            printAsJSON(id);
        }
    }
}