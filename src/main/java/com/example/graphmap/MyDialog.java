package com.example.graphmap;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

public class MyDialog extends Alert {
    private final GridPane grid = new GridPane();
    private TextArea textArea = new TextArea();
    private boolean editable;

    public MyDialog(boolean editable) {
        super(AlertType.INFORMATION);
        grid.setAlignment(Pos.CENTER);
        grid.addRow(0, textArea);
        getDialogPane().setContent(grid);
        if(editable){
            this.editable = true;
        }
    }

    public void setHeader(String header){
        this.setHeaderText(header);
    }

    public GridPane getGrid() {
        return grid;
    }
    public void setText(String text) {
        textArea.setText(text);
    }
}
