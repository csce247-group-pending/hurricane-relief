package com.pending.model.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataManagerTest {
    // Note: in the project's current state, read() and write() practically behave as wrappers for readJSON() and writeJSON()

    /**
     * assert that readJSON is called and no errors are thrown
     */
    @Test
    void read() {
    }

    /**
     * assert that writeJSON is called and no errors are thrown
     */
    @Test
    void write() {
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
    void readJSON() {
    }

    /**
     * test | reasoning
     *
     * post fresh instance | should require data to be loaded first
     * after reading corrupt data | should not change the files
     * after reading proper data without making changes | should do nothing
     * after reading proper data and making changes | should write the appropriate data
     */
    @Test
    void writeJSON() {
    }
}