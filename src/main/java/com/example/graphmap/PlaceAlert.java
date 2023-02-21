package com.example.graphmap;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class PlaceAlert extends Alert {
    private TextField name;

    public PlaceAlert(){
        super(AlertType.CONFIRMATION);
        name = new TextField();

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        gridPane.addRow(0, new Label("Name of place: "), name);

        setTitle("Place");
        setHeaderText(null);
        getDialogPane().setContent(gridPane);
    }

    public String getName(){
        return name.getText();
    }
}