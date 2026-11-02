package com.pending.model.data;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import com.pending.model.data.serializables.Contact;
import com.pending.model.data.serializables.Hurricane;
import com.pending.model.data.serializables.LivingBeing;
import com.pending.model.data.serializables.Location;
import com.pending.model.data.serializables.ReliefRequest;
import com.pending.model.data.serializables.Serializable;
import com.pending.model.data.serializables.Shelter;
import com.pending.model.data.serializables.ShelterManager;
import com.pending.model.data.serializables.User;
import com.pending.model.data.serializables.Volunteer;
import com.pending.model.enums.ContactPreference;
import com.pending.model.enums.ReliefType;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

/**
 * Shared helpers for the data-package tests.
 *
 * <p>Everything that depends on the constructors of model classes that are
 * not part of the data package lives in the "ADAPT" section below, so a
 * signature mismatch only needs to be fixed in one place.
 */
final class TestSupport {

    private TestSupport() {
    }

    // ====================================================================
    // ADAPT: model-class construction (signatures inferred from the
    // comments in the original test files and from Database.java)
    // ====================================================================

    /** Builds a Contact: Contact(phone, email, preferredMethod). */
    static Contact contact(String email, String phone) {
        return new Contact(phone, email, ContactPreference.TEXT);
    }

    /** Builds Location("SC", "Columbia", "300 Main St", 29201). */
    static Location location() {
        return new Location("SC", "Columbia", "300 Main St", "29201");
    }

    /** Builds [MEDICAL_CARE, SHELTER]. */
    static ArrayList<ReliefType> reliefNeeds() {
        return new ArrayList<>(Arrays.asList(
                ReliefType.MEDICAL_CARE, ReliefType.SHELTER));
    }

    /** Builds a SkillSort priority filter (assumed to live in this package). */
    static PriorityFilter<ReliefRequest> skillSort(Volunteer volunteer) {
        return new SkillSort(volunteer);
    }

    // ====================================================================
    // Model objects built only from the User constructor used in Database
    // ====================================================================

    static User user(String email) {
        return new User("John", "Smith", LocalDate.of(1999, 2, 28),
                reliefNeeds(), "UIJK(*ix3", location(),
                contact(email, "803-444-4444"),
                contact("msmith@outlook.com", "803-444-4445"));
    }

    /** An instance of an anonymous subclass of User ("child class"). */
    static User childUser(String email) {
        return new User("John", "Smith", LocalDate.of(1999, 2, 28),
                reliefNeeds(), "UIJK(*ix3", location(),
                contact(email, "803-444-4444"),
                contact("msmith@outlook.com", "803-444-4445")) { };
    }

    // ====================================================================
    // Data files
    // ====================================================================

    static final List<Class<? extends Serializable>> MODEL_CLASSES =
            List.of(User.class, ReliefRequest.class, Hurricane.class,
                    Shelter.class, Contact.class, Location.class,
                    LivingBeing.class, Volunteer.class, ShelterManager.class);

    /** The data file of a model class, read from its static "file" field. */
    static Path fileOf(Class<? extends Serializable> cls) {
        try {
            return Paths.get((String) cls.getField("file").get(null));
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(
                    "No public static 'file' field on " + cls, e);
        }
    }

    static String readText(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    static void writeText(Path path, String text) {
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            Files.writeString(path, text);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    static void deleteIfExists(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /** Records the bytes of every model data file (null if missing). */
    static Map<Class<? extends Serializable>, byte[]> snapshotFiles() {
        Map<Class<? extends Serializable>, byte[]> snapshot = new HashMap<>();

        for (Class<? extends Serializable> cls : MODEL_CLASSES) {
            Path path = fileOf(cls);

            try {
                snapshot.put(cls,
                        Files.exists(path) ? Files.readAllBytes(path) : null);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        return snapshot;
    }

    /** Puts every model data file back exactly as it was snapshotted. */
    static void restoreFiles(
            Map<Class<? extends Serializable>, byte[]> snapshot) {

        for (Map.Entry<Class<? extends Serializable>, byte[]> entry
                : snapshot.entrySet()) {

            Path path = fileOf(entry.getKey());

            try {
                if (entry.getValue() == null) {
                    Files.deleteIfExists(path);
                } else {
                    if (path.getParent() != null) {
                        Files.createDirectories(path.getParent());
                    }
                    Files.write(path, entry.getValue());
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    /** Asserts that no model data file differs from the snapshot. */
    static void assertFilesUnchanged(
            Map<Class<? extends Serializable>, byte[]> snapshot) {

        Map<Class<? extends Serializable>, byte[]> now = snapshotFiles();

        for (Class<? extends Serializable> cls : MODEL_CLASSES) {
            assertArrayEquals(snapshot.get(cls), now.get(cls),
                    "data file of " + cls.getSimpleName()
                            + " must not change");
        }
    }

    // ====================================================================
    // JSON helpers
    // ====================================================================

    static JSONArray parseArray(String text) {
        try {
            return (JSONArray) new JSONParser().parse(text);
        } catch (Exception e) {
            throw new IllegalArgumentException("Not a JSON array: " + text, e);
        }
    }

    static JSONArray parseArray(Path path) {
        return parseArray(readText(path));
    }

    /** Number of entries in a JSON array file; 0 if the file is missing. */
    static int entryCount(Path path) {
        return Files.exists(path) ? parseArray(path).size() : 0;
    }

    /** A JSON array holding only the first entry of the given file. */
    static JSONArray firstEntryOnly(Path path) {
        JSONArray all = parseArray(path);
        JSONArray one = new JSONArray();

        if (!all.isEmpty()) {
            one.add(all.get(0));
        }

        return one;
    }

    // ====================================================================
    // Singleton control
    // ====================================================================

    /** Reloads every list of the shared Database from the data files. */
    static void reloadAll() {
        Database.getInstance().read();
    }

    /**
     * Runs the body while Database and every DataList are "fresh" (never
     * read), then puts the original singletons back.
     */
    @SuppressWarnings("unchecked")
    static void withFreshSingletons(Runnable body) {
        try {
            Field databaseField = Database.class.getDeclaredField("database");
            databaseField.setAccessible(true);

            Field listsField = DataList.class.getDeclaredField("dataLists");
            listsField.setAccessible(true);

            Object oldDatabase = databaseField.get(null);
            HashMap<Object, Object> lists =
                    (HashMap<Object, Object>) listsField.get(null);
            HashMap<Object, Object> savedLists = new HashMap<>(lists);

            try {
                databaseField.set(null, null);
                lists.clear();
                body.run();
            } finally {
                databaseField.set(null, oldDatabase);
                lists.clear();
                lists.putAll(savedLists);
            }
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    // ====================================================================
    // Reflection helpers (model schema is not needed)
    // ====================================================================

    private static List<Field> instanceFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();

        for (Class<?> c = type; c != null && c != Object.class;
             c = c.getSuperclass()) {

            for (Field field : c.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    field.setAccessible(true);
                    fields.add(field);
                }
            }
        }

        return fields;
    }

    private static Object valueOf(Field field, Object target) {
        try {
            return field.get(target);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    /** UUID-typed fields of an object that are not its own id. */
    static List<Field> uuidReferenceFields(Serializable object) {
        UUID id = object.getId();
        List<Field> out = new ArrayList<>();

        for (Field field : instanceFields(object.getClass())) {
            if (field.getType() != UUID.class) {
                continue;
            }

            Object value = valueOf(field, object);

            if (value == null || !value.equals(id)) {
                out.add(field);
            }
        }

        return out;
    }

    /** Sets the object's own id field to null; false if none was found. */
    static boolean clearId(Serializable object) {
        UUID id = object.getId();

        for (Field field : instanceFields(object.getClass())) {
            if (field.getType() == UUID.class
                    && id != null && id.equals(valueOf(field, object))) {

                try {
                    field.set(object, null);
                    return true;
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
            }
        }

        return false;
    }

    /** Serializable objects held directly or in collections by an object. */
    static List<Serializable> referencesOf(Serializable object) {
        List<Serializable> out = new ArrayList<>();

        for (Field field : instanceFields(object.getClass())) {
            Object value = valueOf(field, object);

            if (value instanceof Serializable) {
                out.add((Serializable) value);
            } else if (value instanceof Collection) {
                for (Object element : (Collection<?>) value) {
                    if (element instanceof Serializable) {
                        out.add((Serializable) element);
                    }
                }
            }
        }

        return out;
    }

    /** Nulls every directly held Serializable field; returns how many. */
    static int nullOutObjectReferences(Serializable object) {
        int count = 0;

        for (Field field : instanceFields(object.getClass())) {
            if (Serializable.class.isAssignableFrom(field.getType())
                    && valueOf(field, object) != null) {

                try {
                    field.set(object, null);
                    count++;
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
            }
        }

        return count;
    }
}