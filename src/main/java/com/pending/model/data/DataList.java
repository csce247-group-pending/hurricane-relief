package com.pending.model.data;

import com.pending.model.data.serializables.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.pending.model.data.serializables.Volunteer;

/**
 * Stores objects of one Serializable type and connects them to a data file.
 *
 * @param <T> the Serializable object type stored in this list
 */
public class DataList<T extends Serializable> {
    private static final HashMap<Class<? extends Serializable>,
            DataList<? extends Serializable>> dataLists = new HashMap<>();

    private ArrayList<T> objects;
    private HashMap<UUID, T> objectsById;
    private String fileName;
    private DataManager dataManager;
    private Class<T> cls;

    /**
     * Creates a data list for a specific Serializable class.
     *
     * @param cls the class of objects stored in this data list
     */
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

    /**
     * Gets the single DataList instance for a Serializable class.
     *
     * @param cls the class of objects stored in the data list
     * @param <T> the Serializable object type
     * @return the DataList for the requested class
     */
    public static <T extends Serializable> DataList<T> getInstance(Class<T> cls) {
        if (!dataLists.containsKey(cls)) {
            dataLists.put(cls, new DataList<T>(cls));
        }

        return (DataList<T>) dataLists.get(cls);
    }

    /**
     * Reads objects from the associated data file.
     */
    public void read() {
        try {
            objects.clear();
            objectsById.clear();

            ArrayList<?> dataObjects = dataManager.read();

            for (Object dataObject : dataObjects) {
                Serializable serializable = cls.getDeclaredConstructor().newInstance();
                serializable.deserialize(dataObject);

                objects.add((T) serializable);
                objectsById.put(serializable.getId(), (T) serializable);
            }
        } catch (Exception e) {
            System.out.println("Problematic class: " + cls);
            e.printStackTrace();
        }
    }

    /**
     * Writes all objects in this list to the associated data file.
     */
    public void write() {
        try {
            dataManager = new DataManager((String) cls.getField("file").get(null));

            ArrayList<Object> dataObjects = new ArrayList<>();

            for (T object : objects) {
                dataObjects.add(object.serialize());
            }

            dataManager.write(dataObjects);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Connects object references after the data list has been loaded.
     */
    public void resolveReferences() {
        for (T object : objects) {
            object.resolveReferences();
        }
    }

    /**
     * Gets objects that match a filter.
     *
     * @param filter the filter used to select objects
     * @return a list of matching objects
     */
    public ArrayList<T> filterByAttribute(Filter<T> filter) {
        ArrayList<T> output = new ArrayList<>();

        for (T object : objects) {
            if (filter.filter(object)) {
                output.add(object);
            }
        }

        return output;
    }

    /**
     * Gets all objects in this data list.
     *
     * @return the list of stored objects
     */
    public ArrayList<T> getObjects() {
        return objects;
    }

    /**
     * Gets objects that match a filter.
     *
     * @param filter the filter used to select objects
     * @return a list of matching objects
     */
    public ArrayList<T> getObjects(Filter<T> filter) {
        ArrayList<T> output = new ArrayList<>();

        for (T object : objects) {
            if (filter.filter(object)) {
                output.add(object);
            }
        }

        return output;
    }

    /**
     * Gets the first object that matches a filter.
     *
     * @param filter the filter used to select an object
     * @return the matching object, or null if no match is found
     */
    public T getObject(Filter<T> filter) {
        for (T object : objects) {
            if (filter.filter(object)) {
                return object;
            }
        }

        return null;
    }

    /**
     * Gets an object using its UUID.
     *
     * @param id the UUID of the object
     * @return the matching object, or null if it is not found
     */
    public T getObjectByUUID(UUID id) {
        return objectsById.get(id);
    }

    /**
     * Gets every object in this data list.
     *
     * @return a list of all stored objects
     */
    public ArrayList<T> getAll() {
        return objects;
    }

    /**
     * Adds an object if its UUID is not already in the data list.
     *
     * @param object the object to add
     * @return true if the object was added; otherwise false
     */
    public boolean add(T object) {
        UUID id = object.getId();

        if (objectsById.containsKey(id)) {
            return false;
        }

        objectsById.put(id, object);
        return objects.add(object);
    }

    /**
     * Removes an object from this data list.
     *
     * @param object the object to remove
     * @return true if the object was removed; otherwise false
     */
    public boolean remove(T object) {
        objectsById.remove(object.getId());
        return objects.remove(object);
    }

    /**
     * Saves the objects in this list through the DataManager.
     *
     * @return true if the save succeeds; otherwise false
     */
    public boolean save() {
        try {
            return dataManager.write(objects);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean exportTripInformation(String toWrite, String outputFileName) {
        return dataManager.writeTxt(toWrite, outputFileName);
    }
}