/**
 * Defines required modules and exported packages for the Hurricane Relief
 * application.
 */
module com.pending.hurricanerelief {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires jdk.jsobject;
    requires javafx.web;
    requires json.simple;
    requires jdk.jshell;
    requires java.desktop;

    opens com.pending.hurricanerelief to javafx.fxml;
    exports com.pending.hurricanerelief;

    exports com.pending.model;
    opens com.pending.model to javafx.fxml;

    exports com.pending.model.data;
    opens com.pending.model.data to javafx.fxml;

    exports com.pending.model.data.serializables;
    opens com.pending.model.data.serializables to javafx.fxml;

    exports com.pending.model.enums;
    opens com.pending.model.enums to javafx.fxml;

    exports com.pending.model.data.serializables.behaviors;
    opens com.pending.model.data.serializables.behaviors to javafx.fxml;
}