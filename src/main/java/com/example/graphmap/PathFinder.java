package com.example.graphmap;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PathFinder extends Application {

    private Button btnNewPlace;
    private BorderPane root;
    private Pane center = new Pane();
    private Stage stage;
    private Scene scene;
    private Graph<Place> placeGraph = new ListGraph<>();
    private Place first;
    private Place second;
    private Edge<Place> edge;
    private Image mapImage = new Image("file:europa.gif");
    private ImageView imageView = new ImageView();

    private boolean changed;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        root = new BorderPane();
        root.setPrefSize(550, 50);
        stage.setTitle("PathFinder");

        VBox vbox = new VBox();
        root.setTop(vbox);

        MenuBar menu = new MenuBar();
        vbox.getChildren().add(menu);
        Menu menuFile = new Menu("File");
        menu.getMenus().add(menuFile);

        MenuItem menuNewMap = new MenuItem("New map");
        menuNewMap.setOnAction(new NewMapHandler());

        MenuItem menuOpenFile = new MenuItem("Open");
        menuOpenFile.setOnAction(new OpenHandler());

        MenuItem menuSaveFile = new MenuItem("Save");
        menuSaveFile.setOnAction(new SaveHandler());

        MenuItem menuSaveImage = new MenuItem("Save image");
        menuSaveImage.setOnAction(new SaveImageHandler());

        MenuItem menuExit = new MenuItem("Exit");
        menuExit.setOnAction(new ExitItemHandler());

        menuFile.getItems().addAll(menuNewMap, menuOpenFile, menuSaveFile, menuSaveImage, menuExit);

        FlowPane controls = new FlowPane();
        vbox.getChildren().add(controls);

        controls.setAlignment(Pos.CENTER);
        Button btnFindPath = new Button("Find path");
        btnFindPath.setOnAction(new FindPathHandler());

        Button btnShowConnection = new Button("Show connection");
        btnShowConnection.setOnAction(new ShowConnectionHandler());

        btnNewPlace = new Button("New place");
        btnNewPlace.setOnAction(new NewPlaceHandler());

        Button btnNewConnection = new Button("New connection");
        btnNewConnection.setOnAction(new NewConnectionHandler());

        Button btnChangeConnection = new Button("Change connection");
        btnChangeConnection.setOnAction(new ChangeConnectionHandler());

        controls.getChildren().addAll(btnFindPath, btnShowConnection, btnNewPlace, btnNewConnection, btnChangeConnection);
        controls.setAlignment(Pos.CENTER);
        controls.setHgap(10);
        controls.setPadding(new Insets(10));

        root.setCenter(center);

        menu.setId("menu");
        menuFile.setId("menuFile");
        menuNewMap.setId("menuNewMap");
        menuOpenFile.setId("menuOpenFile");
        menuSaveFile.setId("menuSaveFile");
        menuSaveImage.setId("menuSaveImage");
        menuExit.setId("menuExit");
        btnNewPlace.setId("btnNewPlace");
        btnNewConnection.setId("btnNewConnection");
        btnShowConnection.setId("btnShowConnection");
        btnChangeConnection.setId("btnChangeConnection");
        btnFindPath.setId("btnFindPath");
        center.setId("outputArea");

        scene = new Scene(root);
        stage.setScene(scene);
        stage.setOnCloseRequest(new ExitHandler());
        stage.show();
    }

    class NewMapHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            if (changed) {
                continueWithoutSaving(event);
            }
            placeGraph = new ListGraph<>();
            center.getChildren().clear();
            first = null;
            second = null;

            mapImage = new Image("file:europa.gif");
            imageView.setImage(mapImage);
            center.getChildren().add(imageView);

            stage.setHeight(mapImage.getHeight());
            stage.setWidth(mapImage.getWidth());
            root.setCenter(center);

            changed = true;
        }
    }
    class OpenHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            if(changed) {
                continueWithoutSaving(event);
            }
            Map<String, Place> nodeByName = new HashMap<>();
            try {
                placeGraph=new ListGraph<>();
                center.getChildren().clear();
                first=null;
                second=null;
                center.getChildren().add(imageView);

                FileReader freader = new FileReader("europa.graph");
                BufferedReader breader = new BufferedReader(freader);
                String file = breader.readLine();

                mapImage = new Image(file);
                imageView.setImage(mapImage);
                stage.setHeight(mapImage.getHeight());
                stage.setWidth(mapImage.getWidth());
                root.setCenter(center);

                String line = breader.readLine();
                String[] split = line.split(";");

                for(int i = 0; i < split.length; i += 3){
                    String name = split[i];
                    Double x = Double.parseDouble(split[i+1]);
                    Double y = Double.parseDouble(split[i+2]);
                    Place p = new Place(x, y, name);
                    p.setOnMouseClicked(new NodeClickHandler());
                    nodeByName.put(name, p);
                    center.getChildren().add(p);
                    placeGraph.add(p);
                    p.setId(name);
                }
                while ((line = breader.readLine()) != null){
                    split = line.split(";");
                    String a = split[0];
                    String b = split[1];
                    String name = split[2];
                    Integer weight = Integer.parseInt(split[3]);

                    Place nodeA = nodeByName.get(a);
                    Place nodeB = nodeByName.get(b);
                    Line l = new Line();
                    if(placeGraph.getEdgeBetween(nodeA, nodeB) == null){
                        placeGraph.connect(nodeA, nodeB, name, weight);
                        l.setStartY(nodeA.getY());
                        l.setStartX(nodeA.getX());
                        l.setEndX(nodeB.getX());
                        l.setEndY(nodeB.getY());
                        l.setStrokeWidth(2f);
                        l.setDisable(true);
                        center.getChildren().add(l);
                    }
                }
                changed = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
    class SaveHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            StringBuilder sb = new StringBuilder();

            sb.append("file:europa.gif").append("\n");
            try (BufferedWriter bfwriter = new BufferedWriter(new FileWriter("europa.graph"))) {
                for(Place p : placeGraph.getNodes()){
                    sb.append(p.getName()).append(";").append(p.getX()).append(";").append(p.getY()).append(";");
                }
                sb.deleteCharAt(sb.length() - 1);
                sb.append("\n");
                for (Place p : placeGraph.getNodes()) {
                    for (Edge<Place> e : placeGraph.getEdgesFrom(p)) {
                        sb.append(p.getName()).append(";").append(e.getDestination().toString()).append(";").append(e.getName()).append(";").append(e.getWeight()).append("\n");
                    }
                }
                sb.deleteCharAt(sb.length() - 1);
                bfwriter.write(sb.toString());
            } catch(IOException ex){
                Alert error = new Alert(Alert.AlertType.ERROR, "IO ERROR: " + ex.getMessage());
                error.showAndWait();
            }

            changed = true;
        }
    }
    class SaveImageHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            try {
                WritableImage image = root.snapshot(null, null);
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
                ImageIO.write(bufferedImage, "png", new File("capture.png"));

            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "IO-fel " + e.getMessage());
                alert.showAndWait();
            }
        }
    }
    class ExitHandler implements EventHandler<WindowEvent> {
        @Override
        public void handle(WindowEvent event) {
            if (changed) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Warning!");
                alert.setHeaderText(null);
                alert.setContentText("Unsaved changes, exit anyway?");
                Optional<ButtonType> answer = alert.showAndWait();
                if (answer.isPresent() && answer.get() != (ButtonType.OK))
                    event.consume();
            }
        }
    }
    class ExitItemHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        }
    }
    class NewPlaceHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            scene.setCursor(Cursor.CROSSHAIR);
            btnNewPlace.setDisable(true);
            center.setOnMouseClicked(new ClickHandler());
        }
    }
    class ClickHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            double x = mouseEvent.getX();
            double y = mouseEvent.getY();

            PlaceAlert placeAlert = new PlaceAlert();
            Optional<ButtonType> result = placeAlert.showAndWait();

            if(result.get() == ButtonType.OK){
                Place place = new Place(x, y, placeAlert.getName());
                place.setOnMouseClicked(new NodeClickHandler());
                place.setId(placeAlert.getName());
                center.getChildren().add(place);
                placeGraph.add(place);
                Label namefPlace = new Label(placeAlert.getName());
                namefPlace.setLayoutX(x);
                namefPlace.setLayoutY(y);
                namefPlace.setDisable(true);
                namefPlace.setLabelFor(place);
                center.getChildren().add(namefPlace);
            }

            if(result.get() == ButtonType.CANCEL){
                scene.setCursor(Cursor.DEFAULT);
                btnNewPlace.setDisable(false);
                center.setOnMouseClicked(null);
                changed = true;
                return;
            }

            scene.setCursor(Cursor.DEFAULT);
            btnNewPlace.setDisable(false);
            center.setOnMouseClicked(null);
            changed = true;
        }
    }



    class NodeClickHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            Place place = (Place) mouseEvent.getSource();
            if (place.getSelected()) {
                place.setSelected(false);
                if (first == place) {
                    first = null;
                } else {
                    second = null;
                }
            } else {
                if (first == null) {
                    first = place;
                    place.setSelected(true);
                } else if (second == null && first != place) {
                    second = place;
                    place.setSelected(true);
                }
            }
        }
    }
    class NewConnectionHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            try {
                if (first == null || second == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error!");
                    alert.setHeaderText(null);
                    alert.setContentText("Two places must be selected!");
                    alert.showAndWait();
                } else if(placeGraph.getEdgeBetween(first, second) != null){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error!");
                    alert.setHeaderText(null);
                    alert.setContentText("A connection is already defined!");
                    alert.showAndWait();
                } else {
                    ConnectionAlert connectionAlert = new ConnectionAlert();
                    Optional<ButtonType> result = connectionAlert.showAndWait();
                    if (result.isPresent() && result.get() != ButtonType.OK) {
                        return;
                    }
                    if (connectionAlert.getName().isEmpty()){
                        Alert alertNoText = new Alert(Alert.AlertType.ERROR);
                        alertNoText.setTitle("Error!");
                        alertNoText.setHeaderText(null);
                        alertNoText.setContentText("Enter a name of the connection!");
                        alertNoText.showAndWait();
                    }

                    Line line = new Line();
                    line.setStartY(first.getY());
                    line.setStartX(first.getX());
                    line.setEndX(second.getX());
                    line.setEndY(second.getY());
                    line.setStrokeWidth(2f);
                    line.setDisable(true);
                    center.getChildren().add(line);

                    placeGraph.connect(first, second, connectionAlert.getName(), connectionAlert.getTime());
                }

            } catch (NumberFormatException e) {
                Alert formatAlert = new Alert(Alert.AlertType.ERROR, "Fel format");
                formatAlert.showAndWait();
            }
        }
    }
    class ChangeConnectionHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {

            if (first == null || second == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText(null);
                alert.setContentText("Two places must be selected!");
                alert.showAndWait();
            } else if(placeGraph.getEdgeBetween(first, second) == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText(null);
                alert.setContentText("A connection is not defined!");
                alert.showAndWait();
            } else {
                ConnectionAlert connectionAlert = new ConnectionAlert();
                connectionAlert.setHeaderText("Connection from " + first.getName() + " to " + second.getName());

                edge = placeGraph.getEdgeBetween(first, second);

                String s = edge.getName();
                connectionAlert.setName(s);

                int i = edge.getWeight();
                connectionAlert.setTime(i);

                connectionAlert.setNameEditable(false);
                connectionAlert.setTimeEditable(true);

                connectionAlert.showAndWait();

                placeGraph.setConnectionWeight(first, second, connectionAlert.getTime());             }


        }
    }
    class ShowConnectionHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {

            if (first == null || second == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText(null);
                alert.setContentText("Two places must be selected!");
                alert.showAndWait();
            } else if (placeGraph.getEdgeBetween(first, second) == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText(null);
                alert.setContentText("No existing connection!");
                alert.showAndWait();
            } else {
                ConnectionAlert connectionAlert = new ConnectionAlert();
                connectionAlert.setHeaderText("Connection from " + first.getName() + " to " + second.getName());

                edge = placeGraph.getEdgeBetween(first, second);

                String s = edge.getName();
                connectionAlert.setName(s);

                int i = edge.getWeight();
                connectionAlert.setTime(i);
                connectionAlert.setNameEditable(false);
                connectionAlert.setTimeEditable(false);
                connectionAlert.showAndWait();
            }
        }
    }
    class FindPathHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            if (first == null || second == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText(null);
                alert.setContentText("Two places must be selected!");
                alert.showAndWait();
            } else if (!placeGraph.pathExists(first, second)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText(null);
                alert.setContentText("No existing connection!");
                alert.showAndWait();
            } else {
                MyDialog dialog = new MyDialog(false);
                StringBuilder sb = new StringBuilder();
                List<Edge<Place>> path = placeGraph.getPath(first, second);

                int counter = 0;
                for(Edge e : path){
                    counter += e.getWeight();
                    sb.append(e).append("\n");
                }

                sb.append("Total ").append(counter).append("\n");
                dialog.setText(sb.toString());
                dialog.setHeader(String.format("The path from %s to %s:", first, second));
                dialog.showAndWait();
            }
        }
    }

    private void continueWithoutSaving(ActionEvent event) {
        if (changed) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Warning!");
            alert.setHeaderText(null);
            alert.setContentText("Unsaved changes, continue anyway?");
            Optional<ButtonType> answer = alert.showAndWait();
            if (answer.isPresent() && answer.get().equals(ButtonType.CANCEL)) {
                event.consume();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}