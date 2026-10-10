package com.pending.model.data;

import com.pending.model.Filter;
import com.pending.model.data.serializables.Serializable;

import java.util.ArrayList;
import java.util.HashMap;

public class DataList<T extends Serializable> {
    private static final HashMap<Class<? extends Serializable>, DataList<? extends Serializable>> dataLists = new HashMap<>();
    ArrayList<T> objects;
    private String fileName;
    private DataManager dataManager;

    private DataList(Class<T> cls) {
        try {
            System.out.println("Reading file " + cls.getField("file").get(null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T extends Serializable> DataList<T> getInstance(Class<T> cls) {
        if (!dataLists.containsKey(cls)) {
            dataLists.put(cls, new DataList<T>(cls));
        }

        return (DataList<T>) dataLists.get(cls);
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

    public ArrayList<T> getAll() {
        return objects;
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
