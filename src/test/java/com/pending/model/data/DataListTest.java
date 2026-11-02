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
     * read users.json
     * read nonexistent file
     */
    @Test
    void read() {
    }

    @Test
    void write() {
    }

    @Test
    void resolveReferences() {
    }

    /**
     * (o) -> o instanceof Object | standard
     * (o) ->
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

    @Test
    void getObjectByUUID() {
    }

    @Test
    void getAll() {
    }

    @Test
    void add() {
    }

    @Test
    void remove() {
    }

    @Test
    void save() {
    }
}