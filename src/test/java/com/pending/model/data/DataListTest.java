package com.pending.model.data;

import static com.pending.model.data.TestSupport.MODEL_CLASSES;
import static com.pending.model.data.TestSupport.childUser;
import static com.pending.model.data.TestSupport.clearId;
import static com.pending.model.data.TestSupport.contact;
import static com.pending.model.data.TestSupport.entryCount;
import static com.pending.model.data.TestSupport.fileOf;
import static com.pending.model.data.TestSupport.firstEntryOnly;
import static com.pending.model.data.TestSupport.nullOutObjectReferences;
import static com.pending.model.data.TestSupport.readText;
import static com.pending.model.data.TestSupport.referencesOf;
import static com.pending.model.data.TestSupport.reloadAll;
import static com.pending.model.data.TestSupport.restoreFiles;
import static com.pending.model.data.TestSupport.snapshotFiles;
import static com.pending.model.data.TestSupport.user;
import static com.pending.model.data.TestSupport.uuidReferenceFields;
import static com.pending.model.data.TestSupport.withFreshSingletons;
import static com.pending.model.data.TestSupport.writeText;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import com.pending.model.data.serializables.Contact;
import com.pending.model.data.serializables.Serializable;
import com.pending.model.data.serializables.User;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DataListTest {
    private static final String JOE = "averagejoe@gmail.com";

    private Map<Class<? extends Serializable>, byte[]> snapshot;

    @BeforeEach
    void setUp() {
        snapshot = snapshotFiles();
        reloadAll();
    }

    @AfterEach
    void tearDown() {
        restoreFiles(snapshot);
        reloadAll();
    }

    // ------------------------------------------------------------------
    // helpers
    // ------------------------------------------------------------------

    private static DataList<Contact> contacts() {
        return DataList.getInstance(Contact.class);
    }

    private static DataList<User> users() {
        return DataList.getInstance(User.class);
    }

    private static <T extends Serializable> void clear(DataList<T> list) {
        for (T object : new ArrayList<>(list.getAll())) {
            list.remove(object);
        }
    }

    private static <T extends Serializable> List<UUID> idsOf(
            DataList<T> list) {

        List<UUID> ids = new ArrayList<>();

        for (T object : list.getAll()) {
            ids.add(object.getId());
        }

        return ids;
    }


    /**
     * Loads good data, replaces the Contact file with {@code corrupt}, reads
     * again and checks that the read was aborted: no exception, in-memory
     * data retained and the file left untouched.
     */
    private static void assertReadAborts(String corrupt) {
        DataList<Contact> list = contacts();
        Path file = fileOf(Contact.class);

        list.read();
        List<UUID> before = idsOf(list);
        assumeFalse(before.isEmpty(), "sample data has no contacts");

        writeText(file, corrupt);

        assertDoesNotThrow(list::read);

        assertEquals(before, idsOf(list),
                "a failed read must not corrupt the program data");
        assertEquals(corrupt, readText(file), "a read must not alter the file");
    }

    // ------------------------------------------------------------------
    // getInstance
    // ------------------------------------------------------------------

    /**
     * +-----------------------------------------------+--------------------------------------------+
     * | test                                          | reasoning                                  |
     * +-----------------------------------------------+--------------------------------------------+
     * | return value is not null                      | the DataList should not be null            |
     * | return value is uniform across multiple calls | the DataList should have only one instance |
     * +-----------------------------------------------+--------------------------------------------+
     */
    @Test
    @DisplayName("getInstance: return value is not null")
    void getInstance_notNull() {
        assertNotNull(DataList.getInstance(Contact.class));
    }

    @Test
    @DisplayName("getInstance: return value is uniform across multiple calls")
    void getInstance_sameInstanceEveryCall() {
        assertSame(DataList.getInstance(Contact.class),
                DataList.getInstance(Contact.class));
    }

    // ------------------------------------------------------------------
    // read
    // ------------------------------------------------------------------

    /**
     * +--------------------------------------------------------+--------------------------------------------------------------------------------------------------------------------------+
     * | test                                                   | reasoning                                                                                                                |
     * +--------------------------------------------------------+--------------------------------------------------------------------------------------------------------------------------+
     * | original json files                                    | standard                                                                                                                 |
     * | generated isolated sample json with one entry per user | simplified test case, must verify that the users list is of the combined sizes of users, volunteers, and sheltermanagers |
     * | json files do not exist                                | proper error handling required                                                                                           |
     * | json files are improperly formatted                    | must abort, rather than corrupt program data                                                                             |
     * | json files contain the wrong type of data              | same as above                                                                                                            |
     * +--------------------------------------------------------+--------------------------------------------------------------------------------------------------------------------------+
     */
    @Test
    @DisplayName("read: original json files")
    void read_originalJsonFiles() {
        DataList<Contact> list = contacts();
        clear(list);

        list.read();

        assertEquals(entryCount(fileOf(Contact.class)), list.getAll().size());

        for (Contact contact : list.getAll()) {
            assertSame(contact, list.getObjectByUUID(contact.getId()));
        }
    }

    @Test
    @DisplayName("read: generated isolated sample json with one entry per user")
    void read_isolatedSampleWithOneEntry() {
        // The combined users + volunteers + shelterManagers size is checked
        // in DatabaseTest; here a one-entry file must give a one-entry list.
        Path file = fileOf(Contact.class);
        assumeTrue(entryCount(file) > 0, "sample data has no contacts");
        writeText(file, firstEntryOnly(file).toJSONString());

        DataList<Contact> list = contacts();
        list.read();

        assertEquals(1, list.getAll().size());
    }

    @Test
    @DisplayName("read: json files do not exist")
    void read_missingFile() {
        TestSupport.deleteIfExists(fileOf(Contact.class));

        assertDoesNotThrow(contacts()::read);
    }

    @Test
    @DisplayName("read: json files are improperly formatted")
    void read_malformedJson() {
        assertReadAborts("[{\"id\":");
    }

    @Test
    @DisplayName("read: json files contain the wrong type of data (object instead of array)")
    void read_wrongTypeRootObject() {
        assertReadAborts("{\"id\":\"1\"}");
    }

    @Test
    @DisplayName("read: json files contain the wrong type of data (array of non-objects)")
    void read_wrongTypeNonObjectEntries() {
        assertReadAborts("[1, 2, 3]");
    }

    // ------------------------------------------------------------------
    // write
    // ------------------------------------------------------------------

    /**
     * +-----------------------------------+----------------------------------------+
     * | test                              | reasoning                              |
     * +-----------------------------------+----------------------------------------+
     * | post fresh instance               | should require data to be loaded first |
     * | after reading corrupt data        | should not change the files            |
     * | should write the appropriate data |                                        |
     * +-----------------------------------+----------------------------------------+
     */
    @Test
    @DisplayName("write: post fresh instance")
    void write_freshInstance() {
        String before = readText(fileOf(Contact.class));

        withFreshSingletons(
                () -> DataList.getInstance(Contact.class).write());

        assertEquals(before, readText(fileOf(Contact.class)),
                "a never-read list must not overwrite the data file");
    }

    @Test
    @DisplayName("write: after reading corrupt data")
    void write_afterReadingCorruptData() {
        String corrupt = "[{\"id\":";
        Path file = fileOf(Contact.class);
        DataList<Contact> list = contacts();

        writeText(file, corrupt);
        list.read();
        list.write();

        assertEquals(corrupt, readText(file));
    }

    @Test
    @DisplayName("write: should write the appropriate data")
    void write_writesAppropriateData() {
        DataList<Contact> list = contacts();
        list.read();

        Contact added = contact("new.person@example.com", "803-555-0100");
        assertTrue(list.add(added));

        List<UUID> expected = idsOf(list);
        list.write();
        list.read();

        assertEquals(expected, idsOf(list));
        assertNotNull(list.getObjectByUUID(added.getId()));
    }

    // ------------------------------------------------------------------
    // resolveReferences
    // ------------------------------------------------------------------

    private static final class Ref {
        final Serializable from;
        final Serializable to;

        Ref(Serializable from, Serializable to) {
            this.from = from;
            this.to = to;
        }
    }

    private static List<Ref> allReferences() {
        List<Ref> refs = new ArrayList<>();
        Database db = Database.getInstance();

        for (Class<? extends Serializable> cls : MODEL_CLASSES) {
            for (Serializable from : db.get(cls).getAll()) {
                if (from == null) {
                    continue;
                }

                for (Serializable to : referencesOf(from)) {
                    refs.add(new Ref(from, to));
                }
            }
        }

        return refs;
    }

    /** References between objects of the same / different DataLists. */
    private static List<Ref> references(boolean sameList, boolean mutual) {
        List<Ref> all = allReferences();
        List<Ref> out = new ArrayList<>();

        for (Ref ref : all) {
            if (ref.from == ref.to
                    || (ref.from.getClass() == ref.to.getClass())
                    != sameList) {
                continue;
            }

            boolean isMutual = false;

            for (Ref other : all) {
                if (other.from == ref.to && other.to == ref.from) {
                    isMutual = true;
                    break;
                }
            }

            if (isMutual == mutual) {
                out.add(ref);
            }
        }

        return out;
    }

    /** Every referenced object must be the instance held by its list. */
    private static void assertResolved(List<Ref> refs) {
        assumeFalse(refs.isEmpty(),
                "sample data contains no reference of this kind");

        Database db = Database.getInstance();

        for (Ref ref : refs) {
            DataList<? extends Serializable> targets =
                    db.get(ref.to.getClass());

            if (targets != null) {
                assertSame(ref.to, targets.getObjectByUUID(ref.to.getId()),
                        "unresolved reference to " + ref.to.getId());
            }
        }
    }

    private static Serializable objectWithUuidReference() {
        for (Class<? extends Serializable> cls : MODEL_CLASSES) {
            for (Serializable object
                    : Database.getInstance().get(cls).getAll()) {

                if (object != null && !uuidReferenceFields(object).isEmpty()) {
                    return object;
                }
            }
        }

        assumeTrue(false, "no object stores a UUID reference");
        return null;
    }

    /**
     * +--------------------------------------------------------------+----------------------------------------------------+
     * | test                                                         | reasoning                                          |
     * +--------------------------------------------------------------+----------------------------------------------------+
     * | no UUID references in any objects                            | standard                                           |
     * | one object references another object in the same DataList    | standard                                           |
     * | one object references another object in a different DataList | standard                                           |
     * | two objects in the same DataList reference each other        | standard                                           |
     * | two objects in different DataLists reference each other      | standard                                           |
     * | UUID is null                                                 | should not throw an error given proper null checks |
     * | referenced object is null                                    | should not throw an error given proper null checks |
     * | referenced object with UUID not found                        | need proper error handling                         |
     * +--------------------------------------------------------------+----------------------------------------------------+
     */
    @Test
    @DisplayName("resolveReferences: no UUID references in any objects")
    void resolveReferences_noUuidReferences() {
        Database db = Database.getInstance();

        for (Class<? extends Serializable> cls : MODEL_CLASSES) {
            DataList<? extends Serializable> list = db.get(cls);
            boolean referenceFree = !list.getAll().isEmpty();

            for (Serializable object : list.getAll()) {
                if (!referencesOf(object).isEmpty()
                        || !uuidReferenceFields(object).isEmpty()) {
                    referenceFree = false;
                    break;
                }
            }

            if (referenceFree) {
                assertDoesNotThrow(list::resolveReferences);
                return;
            }
        }

        assumeTrue(false, "every data list in the sample data has references");
    }

    @Test
    @DisplayName("resolveReferences: one object references another object in the same DataList")
    void resolveReferences_oneReferenceSameList() {
        assertResolved(references(true, false));
    }

    @Test
    @DisplayName("resolveReferences: one object references another object in a different DataList")
    void resolveReferences_oneReferenceDifferentList() {
        assertResolved(references(false, false));
    }

    @Test
    @DisplayName("resolveReferences: two objects in the same DataList reference each other")
    void resolveReferences_mutualReferenceSameList() {
        assertResolved(references(true, true));
    }

    @Test
    @DisplayName("resolveReferences: two objects in different DataLists reference each other")
    void resolveReferences_mutualReferenceDifferentLists() {
        assertResolved(references(false, true));
    }

    @Test
    @DisplayName("resolveReferences: UUID is null")
    void resolveReferences_nullUuid() throws IllegalAccessException {
        Serializable object = objectWithUuidReference();

        for (Field field : uuidReferenceFields(object)) {
            field.set(object, null);
        }

        assertDoesNotThrow(() -> Database.getInstance()
                .get(object.getClass()).resolveReferences());
    }

    @Test
    @DisplayName("resolveReferences: referenced object is null")
    void resolveReferences_nullReferencedObject() {
        Serializable object = objectWithUuidReference();
        assumeTrue(nullOutObjectReferences(object) > 0,
                "object holds no resolved reference to null out");

        assertDoesNotThrow(() -> Database.getInstance()
                .get(object.getClass()).resolveReferences());
    }

    @Test
    @DisplayName("resolveReferences: referenced object with UUID not found")
    void resolveReferences_uuidNotFound() throws IllegalAccessException {
        Serializable object = objectWithUuidReference();

        for (Field field : uuidReferenceFields(object)) {
            field.set(object, UUID.randomUUID());
        }

        assertDoesNotThrow(() -> Database.getInstance()
                .get(object.getClass()).resolveReferences());
    }

    // ------------------------------------------------------------------
    // filterByAttribute
    // ------------------------------------------------------------------

    private static DataList<Contact> populatedContacts() {
        DataList<Contact> list = contacts();
        clear(list);

        list.add(contact("a@example.com", "803-000-0001"));
        list.add(contact("b@example.com", "803-000-0002"));
        list.add(contact("c@example.com", "803-000-0003"));

        return list;
    }

    /**
     * +----------------------------+--------------------------------------------------------------+
     * | test                       | reasoning                                                    |
     * +----------------------------+--------------------------------------------------------------+
     * | (o) -> o instanceof Object | standard                                                     |
     * | (o) -> true                | should return the whole list                                 |
     * | (o) -> false               | should return an empty ArrayList                             |
     * | (o) -> o.equals(null)      | should return an empty list, as null items should be skipped |
     * +----------------------------+--------------------------------------------------------------+
     */
    @Test
    @DisplayName("filterByAttribute: (o) -> o instanceof Object")
    void filterByAttribute_instanceOfObject() {
        DataList<Contact> list = populatedContacts();

        ArrayList<Contact> result =
                list.filterByAttribute(o -> o instanceof Object);

        assertEquals(list.getAll(), result);
    }

    @Test
    @DisplayName("filterByAttribute: (o) -> true")
    void filterByAttribute_alwaysTrue() {
        DataList<Contact> list = populatedContacts();

        ArrayList<Contact> result = list.filterByAttribute(o -> true);

        assertEquals(list.getAll(), result);
    }

    @Test
    @DisplayName("filterByAttribute: (o) -> false")
    void filterByAttribute_alwaysFalse() {
        DataList<Contact> list = populatedContacts();

        ArrayList<Contact> result = list.filterByAttribute(o -> false);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("filterByAttribute: (o) -> o.equals(null)")
    void filterByAttribute_equalsNullSkipsNullItems() {
        DataList<Contact> list = populatedContacts();
        list.getAll().add(null);

        try {
            ArrayList<Contact> result = assertDoesNotThrow(
                    () -> list.filterByAttribute(o -> o.equals(null)));

            assertTrue(result.isEmpty());
        } finally {
            list.getAll().removeIf(Objects::isNull);
        }
    }

    // ------------------------------------------------------------------
    // getObject
    // ------------------------------------------------------------------

    /**
     * +--------------------------------------------------------------------------+-----------------------------------------------------------------------+
     * | test                                                                     | reasoning                                                             |
     * +--------------------------------------------------------------------------+-----------------------------------------------------------------------+
     * | (User u) -> u.getUserContact().getEmail().equals("averagejoe@gmail.com") | standard; get user with matching email                                |
     * | null                                                                     | null check must be implemented                                        |
     * | (User u) -> u.getUserContact().getEmail().equals("averagejoe@gmail.com") | several matching users: the first match is returned (reasoning added) |
     * | (User u) -> u.getUserContact().getEmail().equals("averagejoe@gmail.com") | no matching user: null is returned (row added)                        |
     * +--------------------------------------------------------------------------+-----------------------------------------------------------------------+
     */
    @Test
    @DisplayName("getObject: email filter, standard")
    void getObject_matchingEmail() {
        DataList<User> list = users();
        clear(list);
        User other = user("other@example.com");
        User joe = user(JOE);
        list.add(other);
        list.add(joe);

        User found = list.getObject(
                (User u) -> u.getUserContact().getEmail().equals(JOE));

        assertSame(joe, found);
    }

    @Test
    @DisplayName("getObject: null filter")
    void getObject_nullFilter() {
        DataList<User> list = users();
        clear(list);
        list.add(user(JOE));

        User found = assertDoesNotThrow(() -> list.getObject(null));

        assertNull(found);
    }

    @Test
    @DisplayName("getObject: email filter, several matches return the first")
    void getObject_firstOfSeveralMatches() {
        DataList<User> list = users();
        clear(list);
        User first = user(JOE);
        User second = user(JOE);
        list.add(first);
        list.add(second);

        User found = list.getObject(
                (User u) -> u.getUserContact().getEmail().equals(JOE));

        assertSame(first, found);
    }

    @Test
    @DisplayName("getObject: email filter, no match")
    void getObject_noMatch() {
        DataList<User> list = users();
        clear(list);
        list.add(user("other@example.com"));

        User found = list.getObject(
                (User u) -> u.getUserContact().getEmail().equals(JOE));

        assertNull(found);
    }

    // ------------------------------------------------------------------
    // getObjectByUUID
    // ------------------------------------------------------------------

    /**
     * +----------------------------------------------+---------------------------------------------------------------+
     * | test                                         | reasoning                                                     |
     * +----------------------------------------------+---------------------------------------------------------------+
     * | object in list with UUID in table            | standard                                                      |
     * | object in list without UUID in table         | should search object list after UUID table has been exhausted |
     * | object not in list but with UUID in table    | should return null                                            |
     * | object not in list and without UUID in table | should return null                                            |
     * | object is null                               | null check                                                    |
     * | UUID is null                                 | null check                                                    |
     * +----------------------------------------------+---------------------------------------------------------------+
     */
    @Test
    @DisplayName("getObjectByUUID: object in list with UUID in table")
    void getObjectByUUID_inListAndInTable() {
        DataList<Contact> list = contacts();
        Contact contact = contact("a@example.com", "803-000-0001");

        assertTrue(list.add(contact));

        assertSame(contact, list.getObjectByUUID(contact.getId()));
    }

    @Test
    @DisplayName("getObjectByUUID: object in list without UUID in table")
    void getObjectByUUID_inListNotInTable() {
        DataList<Contact> list = contacts();
        Contact contact = contact("a@example.com", "803-000-0001");

        list.getAll().add(contact);

        assertSame(contact, list.getObjectByUUID(contact.getId()),
                "the object list must be searched after the table");
    }

    @Test
    @DisplayName("getObjectByUUID: object not in list but with UUID in table")
    void getObjectByUUID_notInListButInTable() {
        DataList<Contact> list = contacts();
        Contact contact = contact("a@example.com", "803-000-0001");

        list.add(contact);
        list.getAll().remove(contact);

        assertNull(list.getObjectByUUID(contact.getId()));
    }

    @Test
    @DisplayName("getObjectByUUID: object not in list and without UUID in table")
    void getObjectByUUID_notInListNotInTable() {
        DataList<Contact> list = contacts();
        Contact contact = contact("a@example.com", "803-000-0001");

        assertNull(list.getObjectByUUID(contact.getId()));
    }

    @Test
    @DisplayName("getObjectByUUID: object is null")
    void getObjectByUUID_nullObjectInList() {
        DataList<Contact> list = contacts();
        list.getAll().add(null);

        try {
            Contact found = assertDoesNotThrow(
                    () -> list.getObjectByUUID(UUID.randomUUID()));

            assertNull(found);
        } finally {
            list.getAll().removeIf(Objects::isNull);
        }
    }

    @Test
    @DisplayName("getObjectByUUID: UUID is null")
    void getObjectByUUID_nullUuid() {
        DataList<Contact> list = contacts();

        Contact found = assertDoesNotThrow(() -> list.getObjectByUUID(null));

        assertNull(found);
    }

    // ------------------------------------------------------------------
    // add
    // ------------------------------------------------------------------

    /**
     * +-----------------------------------------------+-----------------+
     * | test                                          | reasoning       |
     * +-----------------------------------------------+-----------------+
     * | Contact("jsmith@outlook.com", "803-444-4444") | standard        |
     * | null                                          | null check      |
     * | Serializable with null UUID                   | UUID null check |
     * +-----------------------------------------------+-----------------+
     */
    @Test
    @DisplayName("add: Contact(\"jsmith@outlook.com\", \"803-444-4444\")")
    void add_contact() {
        DataList<Contact> list = contacts();
        clear(list);
        Contact contact = contact("jsmith@outlook.com", "803-444-4444");

        assertTrue(list.add(contact));

        assertEquals(1, list.getAll().size());
        assertTrue(list.getAll().contains(contact));
        assertSame(contact, list.getObjectByUUID(contact.getId()));
    }

    @Test
    @DisplayName("add: null")
    void add_null() {
        DataList<Contact> list = contacts();
        int sizeBefore = list.getAll().size();

        boolean added = assertDoesNotThrow(() -> list.add(null));

        assertFalse(added);
        assertEquals(sizeBefore, list.getAll().size());
    }

    @Test
    @DisplayName("add: Serializable with null UUID")
    void add_serializableWithNullUuid() {
        DataList<Contact> list = contacts();
        Contact contact = contact("jsmith@outlook.com", "803-444-4444");
        assumeTrue(clearId(contact), "could not locate the UUID field");
        int sizeBefore = list.getAll().size();

        boolean added = assertDoesNotThrow(() -> list.add(contact));

        assertFalse(added, "an object without a UUID must be rejected");
        assertEquals(sizeBefore, list.getAll().size());
    }

    // ------------------------------------------------------------------
    // remove
    // ------------------------------------------------------------------

    /**
     * +-----------------------+---------------------------------------------------------------------+
     * | test                  | reasoning                                                           |
     * +-----------------------+---------------------------------------------------------------------+
     * | object in list        | standard                                                            |
     * | object not in list    | make sure no error is thrown                                        |
     * | object of child class | a subclass instance can be removed like any other (reasoning added) |
     * | null                  | null check                                                          |
     * +-----------------------+---------------------------------------------------------------------+
     */
    @Test
    @DisplayName("remove: object in list")
    void remove_objectInList() {
        DataList<Contact> list = contacts();
        Contact contact = contact("a@example.com", "803-000-0001");
        list.add(contact);

        assertTrue(list.remove(contact));

        assertFalse(list.getAll().contains(contact));
        assertNull(list.getObjectByUUID(contact.getId()));
    }

    @Test
    @DisplayName("remove: object not in list")
    void remove_objectNotInList() {
        DataList<Contact> list = contacts();
        Contact contact = contact("a@example.com", "803-000-0001");
        int sizeBefore = list.getAll().size();

        boolean removed = assertDoesNotThrow(() -> list.remove(contact));

        assertFalse(removed);
        assertEquals(sizeBefore, list.getAll().size());
    }

    @Test
    @DisplayName("remove: object of child class")
    void remove_objectOfChildClass() {
        DataList<User> list = users();
        User child = childUser(JOE);
        assertTrue(child.getClass() != User.class);
        list.add(child);

        assertTrue(list.remove(child));

        assertFalse(list.getAll().contains(child));
        assertNull(list.getObjectByUUID(child.getId()));
    }

    @Test
    @DisplayName("remove: null")
    void remove_null() {
        DataList<Contact> list = contacts();
        int sizeBefore = list.getAll().size();

        boolean removed = assertDoesNotThrow(() -> list.remove(null));

        assertFalse(removed);
        assertEquals(sizeBefore, list.getAll().size());
    }
}