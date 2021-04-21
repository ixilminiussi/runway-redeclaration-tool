import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;


public class Main extends Application {

    private ConfigPanel configPanel; // config module
    private RunwayGraphics runwayGraphics; // graphics module
    private AffectedRunway currentRunway; // calculation module
    private HistoryPanel historyPanel; // history module
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
        currentRunway = configPanel.getAffectedRunway();
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
                historyPanel.addHistoryEntry(compareChanges(runway));
                Obstruction obstruction = currentRunway.getObstruction();
                currentRunway = runway.recalculate(obstruction);
                System.out.println(currentRunway.getOriginalRunway());
                runwayGraphics.setAffectedRunway(currentRunway);
                runwayGraphics.draw();
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
                historyPanel.addHistoryEntry(compareChanges(obstruction));
                currentRunway.recalculate(obstruction);
                System.out.println(currentRunway.getOriginalRunway());
                runwayGraphics.setAffectedRunway(currentRunway);
                runwayGraphics.draw();

            }
        });
    }

    private String compareChanges(Runway newRunway) {
        Runway oldRunway = currentRunway.getOriginalRunway();
        if(oldRunway == null) {
            return "RUNWAY: ";
        }
        ArrayList<String> changes = new ArrayList<>();
        if(!oldRunway.getName().equals(newRunway.getName())) {
            changes.add("Name changed to " + newRunway.getName());
        }

        if(!oldRunway.getAirport().equals(newRunway.getAirport())) {
            changes.add("Airport changed to " + newRunway.getAirport());
        }
        if(oldRunway.getTORA() != (newRunway.getTORA())) {
            changes.add("TORA changed to " + newRunway.getTORA());
        }
        if(oldRunway.getTODA() != (newRunway.getTODA())) {
            changes.add("TODA changed to " + newRunway.getTODA());
        }
        if(oldRunway.getASDA() != (newRunway.getASDA())) {
            changes.add("ASDA changed to " + newRunway.getASDA());
        }
        if(oldRunway.getLDA() != (newRunway.getLDA())) {
            changes.add("LDA changed to " + newRunway.getLDA());
        }
        if(oldRunway.getDisplacedThreshold() != newRunway.getDisplacedThreshold()) {
            changes.add("Displaced Threshold changed to " + newRunway.getDisplacedThreshold());
        }
        if(oldRunway.getStripEnd() != newRunway.getStripEnd()) {
            changes.add("Strip End changed to " + newRunway.getStripEnd());
        }

        return "RUNWAY: " + String.join(", ", changes);

    }

    public String compareChanges(Obstruction obst) {
        Obstruction oldObst = currentRunway.getObstruction();
        if(oldObst == null) {
            return "OBSTRUCTION: ";
        }
        ArrayList<String> changes = new ArrayList<>();
        if(oldObst.getName() != obst.getName()) {
            changes.add("Name changed to " + obst.getName());
        }

        if(oldObst.getHeight() != obst.getHeight()) {
            changes.add("Height changed to " + obst.getHeight());
        }

        if(oldObst.getLength() != obst.getLength()) {
            changes.add("Length changed to " + obst.getLength());
        }

        if(oldObst.getDistanceFromThreshold() != obst.getDistanceFromThreshold()) {
            changes.add("Distance from threshold changed to " + obst.getDistanceFromThreshold());
        }

        if(oldObst.getDistanceFromCentre() != obst.getDistanceFromCentre()) {
            changes.add("Distance from centre changed to " + obst.getDistanceFromCentre());
        }

        return "OBSTRUCTION: " + String.join(", ", changes);

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


        historyPanel = new HistoryPanel();
        configPanel = new ConfigPanel(historyPanel);
        currentRunway = configPanel.getAffectedRunway();
        main.add(configPanel, 2, 0, 1, 3);

        runwayGraphics = new RunwayGraphics();
        runwayGraphics.setAffectedRunway(currentRunway);
        runwayGraphics.draw();
        main.add(runwayGraphics.getRunwayGraphics(), 0, 0, 2, 4);


        main.add(historyPanel, 2, 3);

//        runwayGraphics = new RunwayGraphics();
//        runwayGraphics.setAffectedRunway(currentRunway);
//        main.add(runwayGraphics.getRunwayGraphics(), 0, 0, 2, 4);

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
                historyPanel.addHistoryEntry("PRESETS: Imported " + path);
            } catch (Exception e) {
                // error dialogue
            }
        });

        MenuItem clearPresets = new MenuItem("Clear All Presets");
        clearPresets.setOnAction((event) -> {
            configPanel.clearPresets();
            historyPanel.addHistoryEntry("PRESETS: Cleared all");
            // show "all presets cleared" dialogue
        });

        MenuItem showCalculations = new MenuItem("Show Calculations");
        showCalculations.setOnAction((event) -> {
            Stage stage2 = new Stage();
            Scene scene2 = new Scene(currentRunway.getCalculationDisplay(), 720, 480);
            stage2.setScene(scene2);
            stage2.show();

        });

        file.getItems().addAll(importNewPresets, clearPresets, showCalculations);
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