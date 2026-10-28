package com.pending.model.data;

import com.pending.model.data.serializables.Serializable;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DataListTest {

    /**
     * Minimal Serializable used only by these tests. Its "file" field points at a
     * temp file, so the tests never read or overwrite the real json/ files.
     *
     * NOTE: this is the only part of the test tied to the shape of Serializable.
     * If your Serializable differs (interface, different abstract methods or
     * constructor), adjust this class and nothing else.
     */
    public static class TestItem extends Serializable {
        public static final String file = createTempJsonFile();

        private UUID id = UUID.randomUUID();
        private String name;
        private int resolveCount;

        public TestItem() {
        }

        public TestItem(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public int getResolveCount() {
            return resolveCount;
        }

        @Override
        public UUID getId() {
            return id;
        }

        @Override
        public JSONObject serialize() {
            JSONObject json = new JSONObject();
            json.put("id", id.toString());
            json.put("name", name);
            return json;
        }

        @Override
        public void deserialize(Object object) {
            JSONObject json = (JSONObject) object;
            id = UUID.fromString((String) json.get("id"));
            name = (String) json.get("name");
        }

        @Override
        public void resolveReferences() {
            resolveCount++;
        }

        private static String createTempJsonFile() {
            try {
                Path path = Files.createTempFile("datalisttest", ".json");
                path.toFile().deleteOnExit();
                return path.toString();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    private DataList<TestItem> list;
    private Path filePath;

    @BeforeEach
    void setUp() throws Exception {
        filePath = Paths.get(TestItem.file);
        Files.writeString(filePath, "[]");
        list = newList();
    }

    /** Creates a fresh DataList so tests don't share the singleton's state. */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private DataList<TestItem> newList() throws Exception {
        Constructor<DataList> constructor = DataList.class.getDeclaredConstructor(Class.class);
        constructor.setAccessible(true);
        return constructor.newInstance(TestItem.class);
    }

    private void writeToFile(TestItem... items) {
        ArrayList<JSONObject> json = new ArrayList<>();
        for (TestItem item : items) {
            json.add(item.serialize());
        }
        assertTrue(new DataManager(TestItem.file).writeJSON(json));
    }

    // ------------------------------------------------------------ getInstance

    @Test
    void getInstance() {
        DataList<TestItem> first = DataList.getInstance(TestItem.class);
        DataList<TestItem> second = DataList.getInstance(TestItem.class);

        assertNotNull(first);
        assertSame(first, second);
    }

    // ------------------------------------------------------------------- read

    @Test
    void read() {
        TestItem alpha = new TestItem("alpha");
        TestItem beta = new TestItem("beta");
        writeToFile(alpha, beta);

        list.read();

        assertEquals(2, list.getAll().size());
        assertEquals(alpha.getId(), list.getAll().get(0).getId());
        assertEquals("alpha", list.getAll().get(0).getName());
        assertEquals("beta", list.getObjectByUUID(beta.getId()).getName());
    }

    @Test
    void readReplacesExistingContents() {
        list.add(new TestItem("old"));
        TestItem fromFile = new TestItem("fromFile");
        writeToFile(fromFile);

        list.read();

        assertEquals(1, list.getAll().size());
        assertEquals("fromFile", list.getAll().get(0).getName());
    }

    @Test
    void readTwiceDoesNotDuplicateObjects() {
        writeToFile(new TestItem("alpha"), new TestItem("beta"));

        list.read();
        list.read();

        assertEquals(2, list.getAll().size());
    }

    @Test
    void readEmptyFileGivesEmptyList() {
        list.read();

        assertTrue(list.getAll().isEmpty());
    }

    @Test
    void readMissingFileDoesNotThrowAndLeavesListEmpty() throws IOException {
        list.add(new TestItem("old"));
        Files.delete(filePath);

        assertDoesNotThrow(() -> list.read());
        assertTrue(list.getAll().isEmpty());
    }

    /** Fails unless read() also clears the UUID lookup map. */
    @Test
    void readClearsStaleUuidLookup() {
        TestItem old = new TestItem("old");
        list.add(old);

        list.read(); // the file is empty, so "old" must be gone from both structures

        assertNull(list.getObjectByUUID(old.getId()));
    }

    // ------------------------------------------------------------------ write

    @Test
    void write() throws Exception {
        TestItem alpha = new TestItem("alpha");
        TestItem beta = new TestItem("beta");
        list.add(alpha);
        list.add(beta);

        list.write();

        DataList<TestItem> reloaded = newList();
        reloaded.read();
        assertEquals(2, reloaded.getAll().size());
        assertEquals("alpha", reloaded.getObjectByUUID(alpha.getId()).getName());
        assertEquals("beta", reloaded.getObjectByUUID(beta.getId()).getName());
    }

    @Test
    void writeEmptyListWritesEmptyArray() {
        list.write();

        ArrayList<JSONObject> contents = new DataManager(TestItem.file).readJSON();
        assertNotNull(contents);
        assertTrue(contents.isEmpty());
    }

    // ------------------------------------------------------ resolveReferences

    @Test
    void resolveReferences() {
        TestItem a = new TestItem("a");
        TestItem b = new TestItem("b");
        TestItem c = new TestItem("c");
        list.add(a);
        list.add(b);
        list.add(c);

        list.resolveReferences();

        assertEquals(1, a.getResolveCount());
        assertEquals(1, b.getResolveCount());
        assertEquals(1, c.getResolveCount());
    }

    @Test
    void resolveReferencesOnEmptyListDoesNothing() {
        assertDoesNotThrow(() -> list.resolveReferences());
    }

    // ------------------------------------------------------ filterByAttribute

    @Test
    void filterByAttribute() {
        TestItem apple = new TestItem("apple");
        TestItem avocado = new TestItem("avocado");
        TestItem banana = new TestItem("banana");
        list.add(apple);
        list.add(avocado);
        list.add(banana);

        ArrayList<TestItem> result = list.filterByAttribute(item -> item.getName().startsWith("a"));

        assertEquals(2, result.size());
        assertTrue(result.contains(apple));
        assertTrue(result.contains(avocado));
        assertFalse(result.contains(banana));
    }

    @Test
    void filterByAttributeWithNoMatchesReturnsEmptyList() {
        list.add(new TestItem("apple"));

        ArrayList<TestItem> result = list.filterByAttribute(item -> false);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void filterByAttributeReturnsANewList() {
        list.add(new TestItem("apple"));

        ArrayList<TestItem> result = list.filterByAttribute(item -> true);
        result.clear();

        assertEquals(1, list.getAll().size());
    }

    // ------------------------------------------------------------- getObjects

    @Test
    void getObjects() {
        TestItem a = new TestItem("a");
        TestItem b = new TestItem("b");
        list.add(a);
        list.add(b);

        ArrayList<TestItem> result = list.getObjects();

        assertEquals(2, result.size());
        assertSame(a, result.get(0));
        assertSame(b, result.get(1));
    }

    @Test
    void getObjectsOnEmptyListIsEmpty() {
        assertTrue(list.getObjects().isEmpty());
    }

    @Test
    void testGetObjects() {
        TestItem apple = new TestItem("apple");
        TestItem banana = new TestItem("banana");
        TestItem blueberry = new TestItem("blueberry");
        list.add(apple);
        list.add(banana);
        list.add(blueberry);

        ArrayList<TestItem> result = list.getObjects(item -> item.getName().startsWith("b"));

        assertEquals(2, result.size());
        assertTrue(result.contains(banana));
        assertTrue(result.contains(blueberry));
        assertFalse(result.contains(apple));
    }

    @Test
    void testGetObjectsWithNoMatchesReturnsEmptyList() {
        list.add(new TestItem("apple"));

        ArrayList<TestItem> result = list.getObjects(item -> false);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // -------------------------------------------------------------- getObject

    @Test
    void getObject() {
        TestItem apple = new TestItem("apple");
        TestItem banana = new TestItem("banana");
        list.add(apple);
        list.add(banana);

        assertSame(banana, list.getObject(item -> item.getName().equals("banana")));
    }

    @Test
    void getObjectReturnsFirstMatch() {
        TestItem first = new TestItem("match");
        TestItem second = new TestItem("match");
        list.add(first);
        list.add(second);

        assertSame(first, list.getObject(item -> item.getName().equals("match")));
    }

    @Test
    void getObjectReturnsNullWhenNothingMatches() {
        list.add(new TestItem("apple"));

        assertNull(list.getObject(item -> false));
    }

    // ------------------------------------------------------- getObjectByUUID

    @Test
    void getObjectByUUID() {
        TestItem apple = new TestItem("apple");
        TestItem banana = new TestItem("banana");
        list.add(apple);
        list.add(banana);

        assertSame(apple, list.getObjectByUUID(apple.getId()));
        assertSame(banana, list.getObjectByUUID(banana.getId()));
    }

    @Test
    void getObjectByUUIDReturnsNullForUnknownId() {
        list.add(new TestItem("apple"));

        assertNull(list.getObjectByUUID(UUID.randomUUID()));
        assertNull(list.getObjectByUUID(null));
    }

    // ----------------------------------------------------------------- getAll

    @Test
    void getAll() {
        assertTrue(list.getAll().isEmpty());

        TestItem a = new TestItem("a");
        TestItem b = new TestItem("b");
        list.add(a);
        list.add(b);

        assertEquals(2, list.getAll().size());
        assertSame(a, list.getAll().get(0));
        assertSame(b, list.getAll().get(1));
    }

    // -------------------------------------------------------------------- add

    @Test
    void add() {
        TestItem item = new TestItem("apple");

        assertTrue(list.add(item));

        assertEquals(1, list.getAll().size());
        assertSame(item, list.getObjectByUUID(item.getId()));
    }

    @Test
    void addRejectsDuplicateUuid() {
        TestItem item = new TestItem("apple");
        assertTrue(list.add(item));

        assertFalse(list.add(item));

        assertEquals(1, list.getAll().size());
    }

    @Test
    void addRejectsDifferentObjectWithSameUuid() throws Exception {
        TestItem original = new TestItem("original");
        TestItem impostor = new TestItem("impostor");
        impostor.deserialize(original.serialize()); // copies the original's id

        assertTrue(list.add(original));
        assertFalse(list.add(impostor));

        assertSame(original, list.getObjectByUUID(original.getId()));
    }

    @Test
    void addAllowsDifferentUuids() {
        assertTrue(list.add(new TestItem("a")));
        assertTrue(list.add(new TestItem("b")));

        assertEquals(2, list.getAll().size());
    }

    // ----------------------------------------------------------------- remove

    @Test
    void remove() {
        TestItem keep = new TestItem("keep");
        TestItem drop = new TestItem("drop");
        list.add(keep);
        list.add(drop);

        assertTrue(list.remove(drop));

        assertEquals(1, list.getAll().size());
        assertSame(keep, list.getAll().get(0));
    }

    @Test
    void removeReturnsFalseWhenObjectIsNotInList() {
        list.add(new TestItem("apple"));

        assertFalse(list.remove(new TestItem("stranger")));
        assertEquals(1, list.getAll().size());
    }

    /** Fails unless remove() also removes the object from the UUID lookup map. */
    @Test
    void removeAlsoRemovesUuidLookup() {
        TestItem item = new TestItem("apple");
        list.add(item);

        list.remove(item);

        assertNull(list.getObjectByUUID(item.getId()));
    }

    /** Fails unless remove() also removes the object from the UUID lookup map. */
    @Test
    void removedObjectCanBeAddedAgain() {
        TestItem item = new TestItem("apple");
        list.add(item);
        list.remove(item);

        assertTrue(list.add(item));
        assertEquals(1, list.getAll().size());
    }

    // ------------------------------------------------------------------- save

    @Test
    void save() throws IOException {
        list.add(new TestItem("apple"));
        Files.deleteIfExists(filePath);

        assertTrue(list.save());

        assertTrue(Files.exists(filePath));
    }

    @Test
    void saveEmptyListSucceeds() {
        assertTrue(list.save());
    }

    // -------------------------------------------------- exportTripInformation

    @Disabled("DataList.exportTripInformation was not in the DataList.java I was given; "
            + "add real assertions once the method's signature is known")
    @Test
    void exportTripInformation() {
    }
}