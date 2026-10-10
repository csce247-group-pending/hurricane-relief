package com.pending.model.data;

import org.json.simple.JSONObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class DataManager {
    private FileReader reader;
    private FileWriter writer;
    private String filename;

    public DataManager(String filename) {
        try {
            reader = new FileReader(filename);
            writer = new FileWriter(filename);
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    public ArrayList<JSONObject> read() {
        return null;
    }

    public ArrayList<JSONObject> readUUIDList() {
        return null;
    }

    public void write(JSONObject object) {

    }
}
