package com.pending.model.data;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import jdk.jshell.spi.ExecutionControl;

/**
 * Reads and writes application data files.
 * The current implementation supports JSON files and reserves SQL support
 * for future development.
 */
public class DataManager {
    private static final String TXT_DESTINATION = "./out/export_files/";

    private JSONParser parser;
    private String filename;
    private FileMode mode = FileMode.JSON;

    /**
     * Creates a DataManager for a specific data file.
     *
     * @param filename the path of the file to read and write
     */
    public DataManager(String filename) {
        this.filename = filename;

        try {
            parser = new JSONParser();
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * Reads data using the selected file mode.
     *
     * @return a list of objects read from the file
     * @throws ExecutionControl.NotImplementedException if SQL mode is selected
     */
    public ArrayList<?> read() throws ExecutionControl.NotImplementedException {
        switch (mode) {
            case JSON:
                return readJSON();
            case SQL:
                throw new ExecutionControl.NotImplementedException("SQL");
            default:
                return null;
        }
    }

    /**
     * Writes objects using the selected file mode.
     *
     * @param objects the objects to write to the file
     * @return true if the write succeeds; otherwise false
     * @throws ExecutionControl.NotImplementedException if SQL mode is selected
     */
    public boolean write(ArrayList<?> objects) throws ExecutionControl.NotImplementedException {
        // System.out.println("Writing to file " + filename);

        switch (mode) {
            case JSON:
                return writeJSON((ArrayList<JSONObject>) objects);
            case SQL:
                throw new ExecutionControl.NotImplementedException("SQL");
            default:
                return false;
        }
    }

    /**
     * Reads a JSON array from the configured file.
     *
     * @return a list of JSON objects, or null if the file cannot be read
     */
    public ArrayList<JSONObject> readJSON() {
        ArrayList<JSONObject> jsonObjects = new ArrayList<>();

        try (FileReader reader = new FileReader(filename)) {
            jsonObjects = (JSONArray) parser.parse(reader);
            return jsonObjects;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Writes a list of JSON objects to the configured file.
     *
     * @param objects the JSON objects to write
     * @return true if the write succeeds; otherwise false
     */
    public boolean writeJSON(ArrayList<JSONObject> objects) {
        JSONArray jsonFileArray = new JSONArray();
        jsonFileArray.addAll(objects);

        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(jsonFileArray.toJSONString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Reads a list of UUID values from a JSON file.
     * This method is not implemented yet.
     *
     * @return a list of JSON objects containing UUID values
     */
    public ArrayList<JSONObject> readUUIDList() {
        return null;
    }

    /**
     * Writes one JSON object to the configured file.
     * This method is not implemented yet.
     *
     * @param object the JSON object to write
     * @throws ExecutionControl.NotImplementedException because this method is
     *         not implemented
     */
    public void write(JSONObject object)
            throws ExecutionControl.NotImplementedException {

        throw new ExecutionControl.NotImplementedException("Not implemented");
    }

    /**
     * Writes exported trip information to a text file.
     *
     * @param toWrite the text content to export
     * @param outputFileName the name of the output text file
     * @return true if the file was written successfully; otherwise false
     */
    public boolean writeTxt(String toWrite, String outputFileName) {
        String outputPath = TXT_DESTINATION + outputFileName;

        try (FileWriter writer = new FileWriter(outputPath)) {
            writer.write(toWrite);
        } catch (Exception e) {
            System.out.println("[ERROR] Error exporting files");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Lists the supported data-storage formats.
     */
    public enum FileMode {
        JSON,
        SQL
    }
}