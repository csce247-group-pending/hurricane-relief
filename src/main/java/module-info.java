module com.pending.hurricanerelief {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;


    opens com.pending.hurricanerelief to javafx.fxml;
    exports com.pending.hurricanerelief;
    exports com.pending.model;
    opens com.pending.model to javafx.fxml;
    exports com.pending.model.data;
    opens com.pending.model.data to javafx.fxml;
    exports com.pending.model.data.serializables;
    opens com.pending.model.data.serializables to javafx.fxml;
}