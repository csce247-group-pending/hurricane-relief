package com.pending.model.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataListTest {

    /**
     * test | reasoning
     *
     * return value is not null | the DataList should not be null
     * return value is uniform across multiple calls | the DataList should have only one instance
     */
    @Test
    void getInstance() {
    }

    /**
     * test | reasoning
     *
     * original json files | standard
     * generated isolated sample json with one entry per user | simplified test case, must verify that the users list is of the combined sizes of users, volunteers, and sheltermanagers
     * json files do not exist | proper error handling required
     * json files are improperly formatted | must abort, rather than corrupt program data
     * json files contain the wrong type of data | same as above
     */
    @Test
    void read() {
    }

    /**
     * test | reasoning
     *
     * post fresh instance | should require data to be loaded first
     * after reading corrupt data | should not change the files
     * should write the appropriate data
     */
    @Test
    void write() {
    }

    /**
     * test | reasoning
     *
     * no UUID references in any objects | standard
     * one object references another object in the same DataList | standard
     * one object references another object in a different DataList | standard
     * two objects in the same DataList reference each other | standard
     * two objects in different DataLists reference each other | standard
     * UUID is null | should not throw an error given proper null checks
     * referenced object is null | should not throw an error given proper null checks
     * referenced object with UUID not found | need proper error handling
     *
     */
    @Test
    void resolveReferences() {
    }

    /**
     * (o) -> o instanceof Object | standard
     * (o) -> true | should return the whole list
     * (o) -> false | should return an empty ArrayList
     * (o) -> o.equals(null) | should return an empty list, as null items should be skipped
     */
    @Test
    void filterByAttribute() {
    }

    /**
     * (User u) -> u.getUserContact().getEmail().equals("averagejoe@gmail.com") | standard; get user with matching email
     * null | null check must be implemented
     * (User u) -> u.getUserContact().getEmail().equals("averagejoe@gmail.com")
     */
    @Test
    void getObject() {
    }

    /**
     * object in list with UUID in table | standard
     * object in list without UUID in table | should search object list after UUID table has been exhausted
     * object not in list but with UUID in table | should return null
     * object not in list and without UUID in table | should return null
     * object is null | null check
     * UUID is null | null check
     */
    @Test
    void getObjectByUUID() {
    }

    /**
     * test | reasoning
     *
     * Contact("jsmith@outlook.com", "803-444-4444") | standard
     * null | null check
     * Serializable with null UUID | UUID null check
     */
    @Test
    void add() {
    }

    /**
     * test | reasoning
     *
     * object in list | standard
     * object not in list | make sure no error is thrown
     * object of child class
     * null | null check
     */
    @Test
    void remove() {
    }
}