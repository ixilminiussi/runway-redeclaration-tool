import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;


public class Main extends Application {

    private ConfigPanel configPanel; // config module
    private RunwayGraphics runwayGraphics; // graphics module
    private AffectedRunway currentRunway; // calculation module

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) { // initialise window
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
                // TODO error popup here
                System.out.println("Invalid Parameters");
            } else {
                Obstruction obstruction = currentRunway.getObstruction();
                currentRunway = runway.recalculate(obstruction);
                runwayGraphics.setAffectedRunway(currentRunway);
                runwayGraphics.draw();
            }
        });

        Button obsApply = configPanel.getApplyObst();
        obsApply.setOnMouseClicked((event) -> {
            Obstruction obstruction = configPanel.getObstruction();
            if (obstruction == null) {
                // TODO error popup here
                System.out.println("Invalid Parameters");
            } else {
                currentRunway.recalculate(obstruction);
                runwayGraphics.setAffectedRunway(currentRunway);
                runwayGraphics.draw();
            }
        });
    }

    // create the main vbox and gridpane that the modules will run in
    private VBox setUpMainGUI() {
        MenuBar menuBar = new MenuBar();
        Menu file = new Menu("File");
        Menu settings = new Menu("Settings");
        menuBar.getMenus().addAll(file, settings);

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

        configPanel = new ConfigPanel();
        main.add(configPanel, 2, 0, 1, 3);
        
        runwayGraphics = new RunwayGraphics(configPanel.getAffectedRunway());
        main.add(runwayGraphics.getRunwayGraphics(), 0, 0, 2, 4);

        root.getChildren().add(main);
        VBox.setVgrow(main, Priority.ALWAYS);
        return root;
    }

}