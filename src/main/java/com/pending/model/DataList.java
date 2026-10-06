package com.pending.model;

import java.util.ArrayList;
import java.util.HashMap;

public class DataList<T extends Serializable> {
    private static final HashMap<String, DataList<?>> dataLists = new HashMap<>();
    ArrayList<T> objects;
    private String fileName;
    private DataManager dataManager;

    private DataList() {

    }

    public static <T extends Serializable>DataList<?> getInstance(String filename) {
        if (!dataLists.containsKey(filename)) {
            dataLists.put(filename, new DataList<T>());
        }
        return dataLists.get(filename);
    }

    public ArrayList<T> filterByAttribute(Filter<T> filter) {
        ArrayList<T> output = new ArrayList<>();
        for (T object: objects) {
            if (filter.filter(object)) {
                output.add(object);
            }
        }
        return output;
    }

    public ArrayList<T> getObjects() {
        return objects;
    }

    public T getObject(Filter<T> filter) {
        for (T object: objects) {
            if (filter.filter(object)) {
                return object;
            }
        }
        return null;
    }

    public boolean add(T object) {
        return objects.add(object);
    }

    public boolean remove(T object) {
        return objects.remove(object);
    }
    
    public boolean save() {
        return false;
    }
}
