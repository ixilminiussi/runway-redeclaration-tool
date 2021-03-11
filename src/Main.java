import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Paths;


public class Main extends Application {

    private ConfigPanel configPanel; // config module
    private RunwayGraphics runwayGraphics; // graphics module
    private AffectedRunway currentRunway; // calculation module
    private Stage stage;

    public static void main(String[] args) {
        System.out.println("If you're reading this I compiled and ran");
        launch(args);
    }

    @Override
    public void start(Stage stage) { // initialise window
        this.stage = stage;
        stage.setTitle("Runway Re-declaration");
        VBox root = setUpMainGUI();
        setupConfigButtons();
        stage.setScene(new Scene(root, 1000, 600));
        stage.setMaximized(true);
        stage.show();
    }

    // add functionality to buttons in config panel to update the graphical view
    private void setupConfigButtons() {
        Button runwayApply = configPanel.getApplyRunway();
        runwayApply.setOnMouseClicked((event) -> {
            Runway runway = configPanel.getRunway();
            if (runway == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Configuration Error");
                alert.setContentText("One of the parameters in the runway or obstruction was invalid!");
                alert.showAndWait();
            } else {
                Obstruction obstruction = currentRunway.getObstruction();
                currentRunway = runway.recalculate(obstruction);
                // TODO runwayGraphics.draw();
            }
        });

        Button obsApply = configPanel.getApplyObst();
        obsApply.setOnMouseClicked((event) -> {
            Obstruction obstruction = configPanel.getObstruction();
            if (obstruction == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Configuration Error");
                alert.setContentText("One of the parameters in the runway or obstruction was invalid!");
                alert.showAndWait();
            } else {
                currentRunway.recalculate(obstruction);
                // TODO runwayGraphics.draw();
            }
        });
    }

    // create the main vbox and gridpane that the modules will run in
    private VBox setUpMainGUI() {
        MenuBar menuBar = new MenuBar();
        setupMenus(menuBar);
        VBox root = new VBox(menuBar);

        GridPane main = new GridPane();
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        ColumnConstraints col3 = new ColumnConstraints();
        col1.setPercentWidth(33.3);
        col2.setPercentWidth(33.3);
        col3.setPercentWidth(33.4);
        main.getColumnConstraints().addAll(col1, col2, col3);

        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();
        RowConstraints row3 = new RowConstraints();
        RowConstraints row4 = new RowConstraints();
        row1.setPercentHeight(25);
        row2.setPercentHeight(25);
        row3.setPercentHeight(25);
        row4.setPercentHeight(25);
        main.getRowConstraints().addAll(row1, row2, row3, row4);

        runwayGraphics = new RunwayGraphics();
        main.add(runwayGraphics.getAnchorPane(), 0, 0, 2, 4);

        configPanel = new ConfigPanel();
        main.add(configPanel, 2, 0, 1, 3);

        root.getChildren().add(main);
        VBox.setVgrow(main, Priority.ALWAYS);
        return root;
    }

    private void setupMenus(MenuBar menuBar) {
        Menu file = new Menu("File");
        Menu settings = new Menu("Settings");
        menuBar.getMenus().addAll(file, settings);
        MenuItem importNewPresets = new MenuItem("Import New Presets");
        importNewPresets.setOnAction((event) -> {
            String path = fileChooserGetPath();
            try {
                importXML importXML = new importXML(path);
                configPanel.addRunwaysFromXML(importXML.importRunwaysFromXML());
                configPanel.addObstructionsFromXML(importXML.importObstructionsFromXML());
            } catch (Exception e) {
                // error dialogue
            }
        });

        MenuItem clearPresets = new MenuItem("Clear All Presets");
        clearPresets.setOnAction((event) -> {
            configPanel.clearPresets();
            // show "all presets cleared" dialogue
        });

        file.getItems().addAll(importNewPresets, clearPresets);
    }

    private String fileChooserGetPath() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("Runway/Obstruction Files", "*.xml"));
        String currentPath = Paths.get("src/xml").toAbsolutePath().normalize().toString();
        fileChooser.setInitialDirectory(new File(currentPath));
        File selectedFile = fileChooser.showOpenDialog(stage);
        return selectedFile.getAbsolutePath();
    }

}