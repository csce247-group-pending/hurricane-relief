package com.pending.model.data;

import jdk.jshell.spi.ExecutionControl;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

class DataManagerTest {

    /** Same directory DataManager.writeTxt uses (relative to the working directory). */
    private static final Path TXT_DIR = Paths.get("./out/export_files/");
    private static final String TXT_FILE = "dataManagerTest_export.txt";

    @TempDir
    Path tempDir;

    private Path jsonPath;
    private DataManager manager;
    private boolean createdTxtDir;

    @BeforeEach
    void setUp() {
        jsonPath = tempDir.resolve("test.json");
        manager = new DataManager(jsonPath.toString());
        createdTxtDir = false;
    }

    @AfterEach
    void cleanUpTxt() throws IOException {
        Files.deleteIfExists(TXT_DIR.resolve(TXT_FILE));
        if (createdTxtDir) {
            Files.deleteIfExists(TXT_DIR);
        }
    }

    private JSONObject sampleObject(String id, String name, long capacity) {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("name", name);
        object.put("capacity", capacity);
        JSONArray tags = new JSONArray();
        tags.add("OPEN");
        tags.add("PET");
        object.put("tags", tags);
        return object;
    }

    private ArrayList<JSONObject> sampleList() {
        ArrayList<JSONObject> list = new ArrayList<>();
        list.add(sampleObject("1", "Summerville High School", 45));
        list.add(sampleObject("2", "Cane Bay High School", 50));
        return list;
    }

    private void ensureTxtDirExists() throws IOException {
        if (!Files.exists(TXT_DIR)) {
            Files.createDirectories(TXT_DIR);
            createdTxtDir = true;
        }
    }

    // ---------------------------------------------------------------- read()

    @Test
    void read() throws Exception {
        assertTrue(manager.writeJSON(sampleList()));

        ArrayList<?> result = manager.read();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Summerville High School", ((JSONObject) result.get(0)).get("name"));
    }

    @Test
    void readReturnsNullWhenFileIsMissing() throws Exception {
        assertNull(manager.read());
    }

    // --------------------------------------------------------------- write()

    @Test
    void write() throws Exception {
        assertTrue(manager.write(sampleList()));

        assertTrue(Files.exists(jsonPath));
        ArrayList<JSONObject> readBack = manager.readJSON();
        assertNotNull(readBack);
        assertEquals(2, readBack.size());
        assertEquals("Cane Bay High School", readBack.get(1).get("name"));
    }

    // ------------------------------------------------------------ readJSON()

    @Test
    void readJSON() {
        assertTrue(manager.writeJSON(sampleList()));

        ArrayList<JSONObject> result = manager.readJSON();

        assertNotNull(result);
        assertEquals(2, result.size());

        JSONObject first = result.get(0);
        assertEquals("1", first.get("id"));
        assertEquals("Summerville High School", first.get("name"));
        assertEquals(45L, first.get("capacity")); // json-simple parses integers as Long
        JSONArray tags = (JSONArray) first.get("tags");
        assertEquals(2, tags.size());
        assertEquals("PET", tags.get(1));
    }

    @Test
    void readJSONReturnsNullWhenFileIsMissing() {
        assertNull(manager.readJSON());
    }

    @Test
    void readJSONReturnsNullWhenFileIsNotValidJSON() throws IOException {
        Files.writeString(jsonPath, "this is { not valid json");

        assertNull(manager.readJSON());
    }

    @Test
    void readJSONReadsEmptyArray() throws IOException {
        Files.writeString(jsonPath, "[]");

        ArrayList<JSONObject> result = manager.readJSON();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ----------------------------------------------------------- writeJSON()

    @Test
    void writeJSON() throws IOException {
        assertTrue(manager.writeJSON(sampleList()));

        String content = Files.readString(jsonPath);

        // Output is pretty printed with four-space indentation
        assertTrue(content.contains("\n"));
        assertTrue(content.contains("\n    {"));
        assertTrue(content.contains("\"name\": \"Summerville High School\""));
    }

    @Test
    void writeJSONRoundTripPreservesData() {
        ArrayList<JSONObject> original = sampleList();

        assertTrue(manager.writeJSON(original));
        ArrayList<JSONObject> readBack = manager.readJSON();

        assertNotNull(readBack);
        assertEquals(original.size(), readBack.size());
        for (int i = 0; i < original.size(); i++) {
            assertEquals(original.get(i), readBack.get(i));
        }
    }

    @Test
    void writeJSONWithEmptyListWritesEmptyArray() {
        assertTrue(manager.writeJSON(new ArrayList<>()));

        ArrayList<JSONObject> readBack = manager.readJSON();

        assertNotNull(readBack);
        assertTrue(readBack.isEmpty());
    }

    @Test
    void writeJSONPreservesSpecialCharactersInStrings() {
        String tricky = "He said \"hi\", {ok}: [yes] \\ back/slash  two  spaces";
        ArrayList<JSONObject> list = new ArrayList<>();
        list.add(sampleObject("1", tricky, 1));

        assertTrue(manager.writeJSON(list));
        ArrayList<JSONObject> readBack = manager.readJSON();

        assertNotNull(readBack);
        assertEquals(tricky, readBack.get(0).get("name"));
    }

    @Test
    void writeJSONOverwritesExistingFile() {
        assertTrue(manager.writeJSON(sampleList()));

        ArrayList<JSONObject> replacement = new ArrayList<>();
        replacement.add(sampleObject("9", "Only Shelter", 5));
        assertTrue(manager.writeJSON(replacement));

        ArrayList<JSONObject> readBack = manager.readJSON();
        assertNotNull(readBack);
        assertEquals(1, readBack.size());
        assertEquals("Only Shelter", readBack.get(0).get("name"));
    }

    @Test
    void writeJSONReturnsFalseWhenPathIsNotWritable() {
        // The parent directory does not exist, so the FileWriter cannot be created
        DataManager badManager = new DataManager(
                tempDir.resolve("missing_dir").resolve("file.json").toString());

        assertFalse(badManager.writeJSON(sampleList()));
    }

    // -------------------------------------------------------- readUUIDList()

    @Test
    void readUUIDList() {
        // Not implemented yet: currently returns null.
        // Replace with real assertions when the method is implemented.
        assertNull(manager.readUUIDList());
    }

    // ------------------------------------------- write(JSONObject) "testWrite"

    @Test
    void testWrite() {
        // Single-object write is not implemented yet and must say so.
        // Replace with a round-trip assertion when the method is implemented.
        assertThrows(ExecutionControl.NotImplementedException.class,
                () -> manager.write(new JSONObject()));
    }

    // ------------------------------------------------------------- writeTxt()

    @Test
    void writeTxt() throws IOException {
        ensureTxtDirExists();
        String content = "Trip to Summerville High School\nLeave at 8:00 AM";

        assertTrue(manager.writeTxt(content, TXT_FILE));

        assertEquals(content, Files.readString(TXT_DIR.resolve(TXT_FILE)));
    }

    @Test
    void writeTxtOverwritesExistingFile() throws IOException {
        ensureTxtDirExists();

        assertTrue(manager.writeTxt("first version", TXT_FILE));
        assertTrue(manager.writeTxt("second version", TXT_FILE));

        assertEquals("second version", Files.readString(TXT_DIR.resolve(TXT_FILE)));
    }

    @Test
    void writeTxtReturnsFalseWhenExportDirectoryIsMissing() {
        // Never delete a real export directory just to run this test
        assumeFalse(Files.exists(TXT_DIR), "export directory already exists");

        assertFalse(manager.writeTxt("content", TXT_FILE));
    }
}