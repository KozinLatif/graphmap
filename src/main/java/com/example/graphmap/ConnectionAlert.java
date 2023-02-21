package com.example.graphmap;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class ConnectionAlert extends Alert {
    private GridPane gridPane;
    private TextField name;
    private TextField time;

    public ConnectionAlert(){
        super(AlertType.CONFIRMATION);
        name = new TextField();
        time = new TextField();

        gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        gridPane.addRow(0, new Label("Name: "), name);
        gridPane.addRow(1, new Label("Time: "), time);

        setTitle("Connection");
        setHeaderText(null);
        setContentText(null);
        getDialogPane().setContent(gridPane);
    }

    public void setName(String string) {
        name.setText(string);
    }

    public void setTime(int integer) {
        time.setText(String.valueOf(integer));
    }

    public String getName() {
        return name.getText();
    }

    public int getTime() {return Integer.parseInt(time.getText());}

    public void setNameEditable(boolean b){
        name.setEditable(b);
    }


    public void setTimeEditable(boolean b){
        time.setEditable(b);
    }
}