package com.pending.scripts;

import java.util.ArrayList;
import java.util.UUID;

public class UUIDGen {
    public static void main(String[] args) {
//        System.out.println(UUID.randomUUID());
        printUUIDsAsJSON(10);
    }

    public static void printAsJSON(UUID id) {
        System.out.printf("\"id\": \"%s\"\n", id.toString());
    }

    public static ArrayList<UUID> generateUUIDs(int count) {
        ArrayList<UUID> output = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            output.add(UUID.randomUUID());
        }
        return output;
    }

    public static void printUUIDsAsJSON(int count) {
        for (UUID id: generateUUIDs(count)) {
            printAsJSON(id);
        }
    }
}
