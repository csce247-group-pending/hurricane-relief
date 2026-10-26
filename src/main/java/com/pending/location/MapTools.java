package com.pending.location;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pending.model.data.serializables.Location;

/**
 * Provides map-related tools, including address geocoding, route generation,
 * driving directions, and travel-time calculations through the map API.
 */
public class MapTools {
    /** API endpoint used to convert addresses to coordinates. */
    private static final String GEOCODING_URL =
        "https://api.heigit.org/pelias/v1/search";

    /** API endpoint used to retrieve driving directions. */
    private static final String DIRECTIONS_URL =
        "https://api.heigit.org/openrouteservice/v2/directions/driving-car";

    /** API endpoint used to retrieve route geometry as GeoJSON. */
    private static final String ROUTE_GEOJSON_URL =
        "https://api.heigit.org/openrouteservice/v2/directions/driving-car/geojson";

    /** The single shared MapTools instance. */
    private static MapTools mapTools;

    /** The map API key retrieved from the environment. */
    private final String apiKey = System.getenv("MAP_API_KEY");

    /** Client used to send requests to the map API. */
    private final HttpClient httpClient;

    /** Converts JSON API responses into usable Java objects. */
    private final ObjectMapper objectMapper;

    /**
     * Creates the map-tools service and configures its HTTP client.
     */
    private MapTools() {
        httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        objectMapper = new ObjectMapper();
    }

    /**
     * Gets the shared MapTools instance.
     *
     * @return the application's MapTools instance
     */
    public static MapTools getInstance() {
        if (mapTools == null) {
            mapTools = new MapTools();
        }

        return mapTools;
    }

    /**
     * Converts a street address into latitude and longitude coordinates.
     *
     * @param location the address to convert
     * @return coordinates for the best available address match
     * @throws IOException if the API cannot find or return the location
     * @throws InterruptedException if the HTTP request is interrupted
     */
    public Coordinates getCoordinates(Location location)
            throws IOException, InterruptedException {

        checkForApiKey();

        String encodedLocation = URLEncoder.encode(
                location.toString(),
                StandardCharsets.UTF_8
        );

        URI uri = URI.create(
                GEOCODING_URL
                        + "?api_key=" + apiKey
                        + "&text=" + encodedLocation
                        + "&boundary.country=USA"
                        + "&size=10"
        );

        HttpRequest request = HttpRequest.newBuilder(uri)
                .header("Accept", "application/json")
                .timeout(Duration.ofSeconds(15))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() != 200) {
            throw new IOException(
                    "Could not convert: " + location
                            + " (HTTP " + response.statusCode() + ")"
                            + "\n" + response.body()
            );
        }

        JsonNode features = objectMapper
                .readTree(response.body())
                .path("features");

        JsonNode bestResult = findBestResult(features);

        return readCoordinates(bestResult);
    }

    /**
     * Finds the shelter with the shortest driving time from an address.
     *
     * @param startingAddress the address from which to begin traveling
     * @param shelters the shelters to compare
     * @return the shelter with the shortest driving time
     * @throws IOException if a map request fails
     * @throws InterruptedException if a map request is interrupted
     */
    public Location findClosestAddress(Location startingAddress,
            Location[] shelters) throws IOException, InterruptedException {

        Coordinates startingCoordinates = getCoordinates(startingAddress);

        Location closestShelter = null;
        double shortestTime = Double.MAX_VALUE;

        for (Location shelter : shelters) {
            Coordinates shelterCoordinates = getCoordinates(shelter);

            double drivingTime = getDrivingTime(
                    startingCoordinates,
                    shelterCoordinates
            );

            if (drivingTime < shortestTime) {
                shortestTime = drivingTime;
                closestShelter = shelter;
            }
        }

        return closestShelter;
    }

    /**
     * Gets turn-by-turn driving directions between two addresses.
     *
     * @param startingAddress the address where the route begins
     * @param endingAddress the address where the route ends
     * @return an array of driving-direction instructions
     * @throws IOException if a map request fails
     * @throws InterruptedException if a map request is interrupted
     */
    public String[] getDrivingDirections(
            Location startingAddress,
            Location endingAddress
    ) throws IOException, InterruptedException {

        Coordinates start = getCoordinates(startingAddress);
        Coordinates end = getCoordinates(endingAddress);

        JsonNode route = getRoute(start, end);
        JsonNode steps = route.path("segments").path(0).path("steps");

        String[] directions = new String[steps.size()];

        for (int i = 0; i < steps.size(); i++) {
            directions[i] = steps.get(i)
                    .path("instruction")
                    .asText();
        }

        return directions;
    }

    /**
     * Selects the best available result from geocoding API results.
     * Address-level results are preferred when available.
     *
     * @param features the geocoding results returned by the API
     * @return the best matching location result
     * @throws IOException if no location result is available
     */
    private JsonNode findBestResult(JsonNode features) throws IOException {
        if (!features.isArray() || features.isEmpty()) {
            throw new IOException("No location was found.");
        }

        for (JsonNode result : features) {
            String layer = result.path("properties").path("layer").asText();

            if (layer.equals("address")) {
                return result;
            }
        }

        return features.get(0);
    }

    /**
     * Converts a map API result node into a Coordinates object.
     *
     * @param result the API result containing coordinate data
     * @return the latitude and longitude from the result
     * @throws IOException if coordinate data is missing
     */
    private Coordinates readCoordinates(JsonNode result) throws IOException {
        JsonNode coordinates = result.path("geometry").path("coordinates");

        if (!coordinates.isArray() || coordinates.size() < 2) {
            throw new IOException("Coordinates were missing.");
        }

        double longitude = coordinates.get(0).asDouble();
        double latitude = coordinates.get(1).asDouble();

        return new Coordinates(latitude, longitude);
    }

    /**
     * Calculates driving time in seconds between two coordinates.
     *
     * @param start the route starting coordinates
     * @param end the route ending coordinates
     * @return the driving duration in seconds
     * @throws IOException if a map request fails
     * @throws InterruptedException if a map request is interrupted
     */
    private double getDrivingTime(Coordinates start, Coordinates end)
            throws IOException, InterruptedException {

        JsonNode route = getRoute(start, end);

        return route.path("summary").path("duration").asDouble();
    }

    /**
     * Calculates driving time in seconds between two addresses.
     *
     * @param startingAddress the address where the route begins
     * @param endingAddress the address where the route ends
     * @return the driving duration in seconds
     * @throws IOException if a map request fails
     * @throws InterruptedException if a map request is interrupted
     */
    public double getDrivingTime(Location startingAddress,
            Location endingAddress) throws IOException, InterruptedException {

        Coordinates start = getCoordinates(startingAddress);
        Coordinates end = getCoordinates(endingAddress);

        JsonNode route = getRoute(start, end);

        return route.path("summary").path("duration").asDouble();
    }

    /**
     * Retrieves route information between two coordinates from the map API.
     *
     * @param start the route starting coordinates
     * @param end the route ending coordinates
     * @return a JSON node representing the route
     * @throws IOException if a map request fails
     * @throws InterruptedException if a map request is interrupted
     */
    private JsonNode getRoute(Coordinates start, Coordinates end)
            throws IOException, InterruptedException {

        String requestBody =
                "{ \"coordinates\": [[" +
                        start.getLongitude() + "," + start.getLatitude() +
                        "],[" +
                        end.getLongitude() + "," + end.getLatitude() +
                        "]], \"instructions\": true, \"language\": \"en\" }";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(DIRECTIONS_URL))
                .header("Authorization", apiKey)
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(15))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() != 200) {
            throw new IOException("Could not get driving directions.");
        }

        return objectMapper
                .readTree(response.body())
                .path("routes")
                .path(0);
    }

    /**
     * Creates a driving route, including start and end locations and route
     * points that can be displayed on a map.
     *
     * @param startingAddress the address where the route begins
     * @param endingAddress the address where the route ends
     * @return the complete route between the two addresses
     * @throws IOException if a map request fails
     * @throws InterruptedException if a map request is interrupted
     */
    public DrivingRoute getDrivingRoute(Location startingAddress,
            Location endingAddress) throws IOException, InterruptedException {

        Coordinates startCoordinates = getCoordinates(startingAddress);
        Coordinates endCoordinates = getCoordinates(endingAddress);

        ArrayList<Coordinates> routePoints = getRoutePoints(
                startCoordinates,
                endCoordinates
        );

        MapLocation startingLocation = new MapLocation(
                startingAddress,
                startCoordinates
        );

        MapLocation endingLocation = new MapLocation(
                endingAddress,
                endCoordinates
        );

        return new DrivingRoute(
                startingLocation,
                endingLocation,
                routePoints
        );
    }

    /**
     * Gets the coordinate points needed to draw a route on a map.
     *
     * @param start the route starting coordinates
     * @param end the route ending coordinates
     * @return a list of coordinates that make up the route
     * @throws IOException if a map request fails
     * @throws InterruptedException if a map request is interrupted
     */
    private ArrayList<Coordinates> getRoutePoints(Coordinates start,
            Coordinates end) throws IOException, InterruptedException {

        String requestBody =
                "{ \"coordinates\": [[" +
                        start.getLongitude() + "," + start.getLatitude() +
                        "],[" +
                        end.getLongitude() + "," + end.getLatitude() +
                        "]]}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ROUTE_GEOJSON_URL))
                .header("Authorization", apiKey)
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(15))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() != 200) {
            throw new IOException("Could not get the driving route.");
        }

        JsonNode points = objectMapper
                .readTree(response.body())
                .path("features")
                .path(0)
                .path("geometry")
                .path("coordinates");

        ArrayList<Coordinates> routePoints = new ArrayList<>();

        for (JsonNode point : points) {
            double longitude = point.get(0).asDouble();
            double latitude = point.get(1).asDouble();

            routePoints.add(new Coordinates(latitude, longitude));
        }

        return routePoints;
    }

    /**
     * Confirms that a map API key is available in the environment.
     *
     * @throws IllegalStateException if the MAP_API_KEY variable is missing
     */
    private void checkForApiKey() {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "MAP_API_KEY environment variable has not been set."
            );
        }
    }
}