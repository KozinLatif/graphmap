module com.example.graphmap {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;


    opens com.example.graphmap to javafx.fxml;
    exports com.example.graphmap;
}