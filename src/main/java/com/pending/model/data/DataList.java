package com.pending.model.data;

import com.pending.model.data.serializables.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class DataList<T extends Serializable> {
    private static final HashMap<Class<? extends Serializable>, DataList<? extends Serializable>> dataLists = new HashMap<>();
    private ArrayList<T> objects;
    private HashMap<UUID, T> objectsById;
    private String fileName;
    private DataManager dataManager;
    private Class<T> cls;

    private DataList(Class<T> cls) {
        try {
            objects = new ArrayList<>();
            objectsById = new HashMap<>();
            this.cls = cls;
            dataManager = new DataManager((String) cls.getField("file").get(null));
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

    public void read() {
        try {
            objects.clear();
            ArrayList<?> dataObjects = dataManager.read();
            for (Object dataObject: dataObjects) {
                Serializable serializable = cls.getDeclaredConstructor().newInstance();
                serializable.deserialize(dataObject);
                objects.add((T) serializable);
                objectsById.put(serializable.getId(), (T) serializable);
            }
        } catch (Exception e) {
            System.out.println("Problematic class: " +cls);
            e.printStackTrace();
        }
    }

    public void write() {
        try {
            dataManager = new DataManager((String) cls.getField("file").get(null));

            ArrayList<Object> dataObjects = new ArrayList<>();
            for (T object: objects) {
                dataObjects.add(object.serialize());
            }
            dataManager.write(dataObjects);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resolveReferences() {
        for (T object: objects) {
            object.resolveReferences();
        }
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

    public ArrayList<T> getObjects(Filter<T> filter) {
        ArrayList<T> output = new ArrayList<>();

        for (T object : objects) {
            if (filter.filter(object)) {
                output.add(object);
            }
        }

    return output;
}

    public T getObject(Filter<T> filter) {
        for (T object: objects) {
            if (filter.filter(object)) {
                return object;
            }
        }
        return null;
    }

    public T getObjectByUUID(UUID id) {
        return objectsById.get(id);
    }

    public ArrayList<T> getAll() {
        return objects;
    }

    public boolean add(T object) {
        UUID id = object.getId();
        if (objectsById.containsKey(id)) {
            return false;
        }
        objectsById.put(id, object);
        return objects.add(object);
    }

    public boolean remove(T object) {
        return objects.remove(object);
    }
    
    public boolean save() {
        try {
            return dataManager.write(objects);
        } catch (Exception e) {
            return false;
        }
    }   
}
