package com.pending.model;

import com.pending.location.MapTools;
import com.pending.model.data.serializables.Location;

public class LocationUI {

    public void run() {
        // A Charleston address near the waterfront
        Location userAddress = new Location(
                "1 Vendue Range",
                "Charleston",
                "SC",
                "29401"
        );
        Location closestShelter;

        // Example hurricane-shelter locations
        Location[] shelters = {
                new Location("2950 Carner Avenue",
                        "North Charleston", "SC", "29405"), // Military Magnet
                
                new Location("5200 Lackawanna Boulevard",
                        "North Charleston", "SC", "29405"), // North Charleston CAES
                
                new Location("3841 Leeds Avenue",
                        "North Charleston", "SC", "29405"), // Bond Court
                
                new Location("3625 Ashley Phosphate Road",
                        "North Charleston", "SC", "29418"), // Stall High School
                
                new Location("6401 Dorchester Road",
                        "North Charleston", "SC", "29418"), // Zucker Middle School
                
                new Location("1624 State Road",
                        "Summerville", "SC", "29483"), // Cane Bay High School
                
                new Location("1053 Russellville Road",
                        "St. Stephen", "SC", "29479"), // St. Stephen Elementary
                
                new Location("200 Macedonia Foxes Circle",
                        "St. Stephen", "SC", "29479"), // Macedonia Middle School
                
                new Location("101 Westview Boulevard",
                        "Goose Creek", "SC", "29445"), // Westview Middle School
                
                new Location("1101 Boone Hill Road",
                        "Summerville", "SC", "29483") // Summerville High School
        };

        try {
            closestShelter = MapTools.getInstance()
                    .findClosestAddress(userAddress, shelters);

            System.out.println("User address: "
                    + userAddress);

            System.out.println("Closest shelter: "
                    + closestShelter);

            String[] directions = MapTools.getInstance().getDrivingDirections(userAddress, closestShelter);

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