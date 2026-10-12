package com.pending.model.data;

import jdk.jshell.spi.ExecutionControl;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class DataManager {
    private JSONParser parser;
    private String filename;
    private FileMode mode = FileMode.JSON;

    public DataManager(String filename) {
        this.filename = filename;
        try {
            parser = new JSONParser();
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    public ArrayList<?> read() throws ExecutionControl.NotImplementedException {
        System.out.println("Reading file " + filename);
        switch (mode) {
            case FileMode.JSON:
                return readJSON();
            case FileMode.SQL:
                throw new ExecutionControl.NotImplementedException("SQL");
            default:
                return null;
        }
    }

    public ArrayList<JSONObject> readJSON() {
        ArrayList<JSONObject> jsonObjects = new ArrayList<>();
        try (FileReader reader = new FileReader(filename)) {
            // reader = new FileReader(filename);
            jsonObjects = (JSONArray) parser.parse(reader);

            return jsonObjects;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<JSONObject> readUUIDList() {
        return null;
    }

    public void write(JSONObject object) {

    }

    public enum FileMode {
        JSON,
        SQL
    }
}
