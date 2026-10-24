package com.pending.model;

import com.pending.location.MapTools;
import com.pending.model.data.serializables.Location;

public class LocationUI {

    public void run() {
        Location userAddress = new Location(
                "SC",
                "Charleston",
                "1 Vendue Range",
                "29401"
        );

        Location closestShelter;

        Location[] shelters = {
                new Location("SC", "North Charleston",
                        "2950 Carner Avenue", "29405"),

                new Location("SC", "North Charleston",
                        "5200 Lackawanna Boulevard", "29405"),

                new Location("SC", "North Charleston",
                        "3841 Leeds Avenue", "29405"),

                new Location("SC", "North Charleston",
                        "3625 Ashley Phosphate Road", "29418"),

                new Location("SC", "North Charleston",
                        "6401 Dorchester Road", "29418"),

                new Location("SC", "Summerville",
                        "1624 State Road", "29483"),

                new Location("SC", "St. Stephen",
                        "1053 Russellville Road", "29479"),

                new Location("SC", "St. Stephen",
                        "200 Macedonia Foxes Circle", "29479"),

                new Location("SC", "Goose Creek",
                        "101 Westview Boulevard", "29445"),

                new Location("SC", "Summerville",
                        "1101 Boone Hill Road", "29483")
        };

        try {
            closestShelter = MapTools.getInstance()
                    .findClosestAddress(userAddress, shelters);

            System.out.println("User address: " + userAddress);
            System.out.println("Closest shelter: " + closestShelter);

            String[] directions = MapTools.getInstance()
                    .getDrivingDirections(userAddress, closestShelter);

            for (String direction : directions) {
                System.out.println(direction);
            }

        } catch (Exception e) {
            System.out.println("There was a problem finding a shelter.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new LocationUI().run();
    }
}