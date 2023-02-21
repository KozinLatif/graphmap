package com.example.graphmap;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class MyAlert extends Alert {
    private TextField textField = new TextField();

    public MyAlert() {
        super(AlertType.CONFIRMATION);
        GridPane gridPane = new GridPane();
        gridPane.addRow(0, new Label("Name of place:"), textField);
        setTitle("Name");
        setHeaderText(null);
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(5);
        getDialogPane().setContent(gridPane);
    }

    public void setName(String string){
        textField.setText(string);
    }

    public String getPlace(){
        return textField.getText();
    }
}