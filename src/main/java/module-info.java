/**
 * Defines required modules and exported packages for the Hurricane Relief
 * application.
 */
module com.pending.hurricanerelief {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    // requires jdk.jsobject;
    // requires javafx.web;
    requires json.simple;
    requires jdk.jshell;
    requires java.desktop;
    requires org.junit.jupiter.api;

    opens com.pending.hurricanerelief to javafx.fxml, org.junit.platform.commons;
    exports com.pending.hurricanerelief;

    exports com.pending.model;
    opens com.pending.model to javafx.fxml, org.junit.platform.commons;

    exports com.pending.model.data;
    opens com.pending.model.data to javafx.fxml, org.junit.platform.commons;

    exports com.pending.model.data.serializables;
    opens com.pending.model.data.serializables to javafx.fxml, org.junit.platform.commons;

    exports com.pending.model.enums;
    opens com.pending.model.enums to javafx.fxml, org.junit.platform.commons;

    exports com.pending.model.data.serializables.behaviors;
    opens com.pending.model.data.serializables.behaviors to javafx.fxml, org.junit.platform.commons;
    exports com.pending.controllers;
    opens com.pending.controllers to javafx.fxml, org.junit.platform.commons;
}