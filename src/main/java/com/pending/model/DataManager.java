package com.pending.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class DataManager {
    private String filename;

    public DataManager(String filename) {
        this.filename = filename;
    }

    public ArrayList<JSONObject> read() {
        ArrayList<JSONObject> objects = new ArrayList<JSONObject>();
        File file = new File(filename);

        if (!file.exists() || file.length() == 0) {
            return objects;
        }

        try (FileReader reader = new FileReader(file)) {
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(reader);

            for (Object object : jsonArray) {
                objects.add((JSONObject) object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return objects;
    }

    public ArrayList<JSONObject> readUUIDList() {
        return read();
    }

    public void write(JSONObject object) {
        ArrayList<JSONObject> objects = read();
        objects.add(object);

        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(objects);

        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(jsonArray.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
