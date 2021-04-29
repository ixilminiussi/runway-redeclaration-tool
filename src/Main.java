import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.event.KeyEvent;
import java.beans.EventHandler;
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


        /** keyboard shortcuts
         * v - iterate through view modes
         * e - export menu
         * i - import menu
         * l - clear all presets
         */
        stage.getScene().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.V) {
                int currentView = runwayGraphics.viewSelect.getSelectionModel().getSelectedIndex();
                switch (currentView) {
                    case 0:
                        runwayGraphics.viewSelect.setValue("Side View");
                        break;
                    case 1:
                        runwayGraphics.viewSelect.setValue("Split View");
                        break;
                    case 3:
                        runwayGraphics.viewSelect.setValue("Top View");
                        break;
                }
            }
            if (e.getCode() == KeyCode.I) {
                importNewXML();
            }
            if (e.getCode() == KeyCode.E) {
                exportToXML();
            }
            if (e.getCode() == KeyCode.L) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to clear all presets?",
                        ButtonType.YES, ButtonType.NO);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {
                    clearPresets();
                }
            }
        });
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
                alert.setContentText("One of the parameters in the runway or obstruction was invalid: " + configPanel.getInvalidText());
                alert.showAndWait();
            } else {
                if(currentRunway != null) {
                    historyPanel.addHistoryEntry(compareChanges(runway));
                }
                Obstruction obstruction = configPanel.getObstruction();
                currentRunway = runway.recalculate(obstruction);
                System.out.println(currentRunway.getOriginalRunway());
                runwayGraphics.draw(currentRunway);
            }
        });

        Button obsApply = configPanel.getApplyObst();
        obsApply.setOnMouseClicked((event) -> {
            Obstruction obstruction = configPanel.getObstruction();
            if (obstruction == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Configuration Error");
                alert.setContentText("One of the parameters in the runway or obstruction was invalid: "  + configPanel.getInvalidText());
                alert.showAndWait();
            } else {
                historyPanel.addHistoryEntry(compareChanges(obstruction));
                currentRunway.recalculate(obstruction);
                System.out.println(currentRunway.getOriginalRunway());
                runwayGraphics.draw(currentRunway);
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
        if(!oldObst.getName().equals(obst.getName())) {
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
        historyPanel.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        configPanel = new ConfigPanel(historyPanel);
        configPanel.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        currentRunway = configPanel.getAffectedRunway();
        main.add(configPanel, 2, 0, 1, 3);

        runwayGraphics = new RunwayGraphics(stage);
        if(currentRunway != null)
            runwayGraphics.draw(currentRunway);
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
        menuBar.getMenus().addAll(file);
        MenuItem importNewPresets = new MenuItem("Import New Presets");
        importNewPresets.setOnAction((event) -> {
            importNewXML();
        });

        // export all runways and obstructions
        MenuItem exportRunwaysAndObstructions = new MenuItem("Export Config");
        exportRunwaysAndObstructions.setOnAction((event) -> {
            exportToXML();
        });

        MenuItem clearPresets = new MenuItem("Clear All Presets");
        clearPresets.setOnAction((event) -> {
            clearPresets();
        });

//        MenuItem showCalculations = new MenuItem("Show Calculations");
//        showCalculations

//        MenuItem toggleCompass = new MenuItem("Toggle Compass");
//        toggleCompass.setOnAction((event) -> {
//            System.out.println("test");
//            runwayGraphics.drawRotated(currentRunway);
//        });

        file.getItems().addAll(importNewPresets, exportRunwaysAndObstructions, clearPresets);
    }

    private String fileChooserGetPath() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("Runway/Obstruction Files", "*.xml"));
        String currentPath = Paths.get("src/xml").toAbsolutePath().normalize().toString();
        fileChooser.setInitialDirectory(new File(currentPath));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if(selectedFile == null) {
            return null;
        }
        return selectedFile.getAbsolutePath();
    }

    private void importNewXML() {
        String path = fileChooserGetPath();
        if(path == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Import Error");
            alert.setContentText("Please select a file");
            alert.showAndWait();
        } else {
            try {
                importXML importXML = new importXML(path);
                configPanel.addRunwaysFromXML(importXML.importRunwaysFromXML());
                configPanel.addObstructionsFromXML(importXML.importObstructionsFromXML());
                historyPanel.addHistoryEntry("PRESETS: Imported " + path);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Import Error");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    private void exportToXML() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(Paths.get("src/xml").toAbsolutePath().normalize().toString()));
        fileChooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("Runway/Obstruction Files", "*.xml"));
        fileChooser.setTitle("Export runways and obstructions");
        File exporttarget = fileChooser.showSaveDialog(stage);
        if(exporttarget == null) {
            Alert errorMessage = new Alert(Alert.AlertType.ERROR);
            errorMessage.setContentText("Create a file to save config into.");
            errorMessage.show();
        } else {

            String filename = exporttarget.getAbsolutePath();

            // ensure filename has .xml extension
            if (!filename.substring(filename.length() - 4).equals(".xml")) {
                filename = filename.concat(".xml");
            }

            try {
                exportXML.exportBothToXML(filename, configPanel.getPresetObstructions(), configPanel.getPresetRunways());
            } catch (Exception e) {
                Alert errorMessage = new Alert(Alert.AlertType.ERROR);
                errorMessage.setHeaderText("An error occurred exporting the file");
                errorMessage.setContentText(e.getMessage());
                errorMessage.show();
            }
        }
    }

    private void clearPresets() {
        configPanel.clearPresets();
        historyPanel.addHistoryEntry("PRESETS: Cleared all");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("INFO");
        alert.setHeaderText("Presets Cleared");
        alert.showAndWait();
    }
}
