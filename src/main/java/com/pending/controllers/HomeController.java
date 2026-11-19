package com.pending.controllers;

import com.pending.hurricanerelief.App;
import com.pending.model.Application;
import com.pending.model.data.serializables.ReliefRequest;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private Button btn_details;

    @FXML
    private Label btn_request;

    @FXML
    private ImageView btn_shelter;

    @FXML
    private HBox vbox_requests;

    private Application application;

    private ArrayList<VBox> getReliefRequestCards() {
        ArrayList<ReliefRequest> reliefRequests = application.getReliefRequests();
        ArrayList<VBox> output = new ArrayList<>();
        for (ReliefRequest reliefRequest: reliefRequests) {
            VBox body = new VBox();
            HBox header = new HBox();
            header.getChildren().addAll(
                    new Label("Description"),
                    new Region(),
                    new Label(reliefRequest.getStatus().name())
            );
            Text description = new Text(reliefRequest.getDescription());
            GridPane data = new GridPane();
            data.add(new Label("Assigned to:"), 0, 0);
            String assignedTo = reliefRequest.getAssignedTo() == null? "Nobody" : reliefRequest.getAssignedTo().getFullName();
            data.add(new Label(assignedTo), 1, 0);
            Button details = new Button("Details");
            details.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    System.out.println(reliefRequest.toStringDetailed());
                }
            });
            body.getChildren().addAll(header, description, data, details);
            output.add(body);
        }
        return output;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        application = Application.getInstance();
        vbox_requests.getChildren().addAll(getReliefRequestCards());
    }
}
