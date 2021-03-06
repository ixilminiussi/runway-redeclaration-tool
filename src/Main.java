import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Main extends Application {

    private ComboBox<String> runwayPresets, directionBox, obstPresets;
    private TextField nameTextField, airportTextField, TORATextField,
            TODATextField, ASDATextField, LDATextField, displacedTextField,
            stripEndTextField, egrTextField, resaTextField, blastTextField,
            stopwayTextField, clearwayTextField, obstNameTextField, obstHeightTextField,
            obstLengthTextField, distThreshTextField, distCentreTextField;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Runway Re-declaration");

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

        StackPane fillerImage = new StackPane();
        Label runwayFillerLabel = new Label("runway");
        fillerImage.getChildren().add(runwayFillerLabel);
        fillerImage.setPadding(new Insets(20, 20, 20, 20));
        fillerImage.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));


        main.add(fillerImage, 0, 0, 2, 4);

        root.getChildren().add(main);

        makeConfigPanel(main);


        VBox.setVgrow(main, Priority.ALWAYS);

        stage.setScene(new Scene(root, 1000, 600));
        stage.setMaximized(true);
        stage.show();
    }

    private void makeConfigPanel(GridPane main) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        VBox config = new VBox();
        scrollPane.setContent(config);
        scrollPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        scrollPane.setPadding(new Insets(5));

        Label runwayConfigText = new Label("Runway Configuration");
        Font defaultFont = Font.getDefault();
        runwayConfigText.setFont(Font.font(defaultFont.getFamily(), FontWeight.BOLD, 15));
        runwayConfigText.setPadding(new Insets(10));

        GridPane runwayConfigPane = new GridPane();
        runwayConfigPane.setPadding(new Insets(10));
        runwayConfigPane.setBorder(new Border(new BorderStroke(Color.SKYBLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        runwayConfigPane.setVgap(20);
        runwayConfigPane.setHgap(10);
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        ColumnConstraints col3 = new ColumnConstraints();
        ColumnConstraints col4 = new ColumnConstraints();
        col1.setPercentWidth(25);
        col1.setHalignment(HPos.RIGHT);
        col2.setPercentWidth(25);
        col3.setPercentWidth(25);
        col3.setHalignment(HPos.RIGHT);
        col4.setPercentWidth(25);
        runwayConfigPane.getColumnConstraints().addAll(col1, col2, col3, col4);


        Label runwayText = new Label("Runway: ");
        runwayConfigPane.add(runwayText, 0, 0);
        runwayPresets = new ComboBox<>();
        runwayPresets.setPrefWidth(400);
        runwayConfigPane.add(runwayPresets, 1, 0, 3, 1);
        runwayPresets.getItems().addAll("Heathrow - 09L", "Heathrow - 027R");

        Label nameText = new Label("Name: ");
        nameTextField = new TextField();
        runwayConfigPane.add(nameText, 0, 1);
        runwayConfigPane.add(nameTextField, 1, 1);
        Label airportText = new Label("Airport: ");
        runwayConfigPane.add(airportText, 2, 1);
        airportTextField = new TextField();
        runwayConfigPane.add(airportTextField, 3, 1);

        Label TORAText = new Label("TORA: ");
        runwayConfigPane.add(TORAText, 0,2);
        TORATextField = new TextField();
        runwayConfigPane.add(TORATextField, 1, 2);
        Label TODAText = new Label("TODA: ");
        runwayConfigPane.add(TODAText, 2, 2);
        TODATextField = new TextField();
        runwayConfigPane.add(TODATextField, 3, 2);

        Label ASDAText = new Label("ASDA: ");
        ASDATextField = new TextField();
        runwayConfigPane.add(ASDAText, 0, 3);
        runwayConfigPane.add(ASDATextField, 1, 3);
        Label LDAText = new Label("LDA: ");
        LDATextField = new TextField();
        runwayConfigPane.add(LDAText, 2, 3);
        runwayConfigPane.add(LDATextField, 3, 3);

        Label displacedText = new Label("Displaced Threshold: ");
        displacedTextField = new TextField();
        runwayConfigPane.add(displacedText, 0, 4);
        runwayConfigPane.add(displacedTextField, 1, 4);
        Label directionText = new Label("Direction: ");
        directionBox = new ComboBox<>();
        directionBox.getItems().addAll("Landing", "Taking Off");
        runwayConfigPane.add(directionText, 2, 4);
        runwayConfigPane.add(directionBox, 3, 4);

        GridPane optionalGridPane = new GridPane();
        optionalGridPane.getColumnConstraints().addAll(col1, col2, col3, col4);
        optionalGridPane.setPadding(new Insets(10));
        optionalGridPane.setVgap(20);
        optionalGridPane.setHgap(10);

        Label stripEndText = new Label("Strip End: ");
        stripEndTextField = new TextField();
        optionalGridPane.add(stripEndText, 0, 0);
        optionalGridPane.add(stripEndTextField, 1, 0);
        Label egrText = new Label("EGR: ");
        egrTextField = new TextField();
        optionalGridPane.add(egrText, 2, 0);
        optionalGridPane.add(egrTextField, 3, 0);

        Label resaText = new Label("RESA: ");
        resaTextField = new TextField();
        Label blastText = new Label("Blast Allowance: ");
        blastTextField = new TextField();
        optionalGridPane.add(resaText, 0, 1);
        optionalGridPane.add(resaTextField, 1, 1);
        optionalGridPane.add(blastText, 2, 1);
        optionalGridPane.add(blastTextField, 3, 1);

        Label stopwayText = new Label("Stopway: ");
        stopwayTextField = new TextField();
        Label clearwayText = new Label("Clearway: ");
        clearwayTextField = new TextField();
        optionalGridPane.add(stopwayText, 0, 2);
        optionalGridPane.add(stopwayTextField, 1, 2);
        optionalGridPane.add(clearwayText, 2, 2);
        optionalGridPane.add(clearwayTextField, 3, 2);

        TitledPane optionals = new TitledPane("Optional Parameters", optionalGridPane);
        runwayConfigPane.add(optionals, 0, 5, 4, 1);

        Button saveRunwayPreset = new Button("Save Runway as Preset");
        saveRunwayPreset.setAlignment(Pos.CENTER);
        Button applyRunway = new Button("Apply");
        applyRunway.setAlignment(Pos.CENTER);
        runwayConfigPane.add(saveRunwayPreset, 1, 6);
        runwayConfigPane.add(applyRunway, 2, 6);

        Label obstConfigText = new Label("Obstacle Configuration");
        obstConfigText.setFont(Font.font(defaultFont.getFamily(), FontWeight.BOLD, 15));
        obstConfigText.setPadding(new Insets(10));
        GridPane obstConfigPane = new GridPane();
        obstConfigPane.setPadding(new Insets(10));
        obstConfigPane.setBorder(new Border(new BorderStroke(Color.SKYBLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        obstConfigPane.setVgap(20);
        obstConfigPane.setHgap(10);
        obstConfigPane.getColumnConstraints().addAll(col1, col2, col3, col4);

        Label obstText = new Label("Obstacle: ");
        obstConfigPane.add(obstText, 0, 0);
        obstPresets = new ComboBox<>();
        obstPresets.setPrefWidth(400);
        obstConfigPane.add(obstPresets, 1, 0, 3, 1);
        obstPresets.getItems().addAll("None", "Big Boulder");

        Label obstName = new Label("Name: ");
        obstNameTextField = new TextField();
        obstConfigPane.add(obstName, 0, 1);
        obstConfigPane.add(obstNameTextField, 1, 1, 3, 1);

        Label obstHeightText = new Label("Height: ");
        obstHeightTextField = new TextField();
        Label obstLengthText = new Label("Length: ");
        obstLengthTextField = new TextField();
        obstConfigPane.add(obstHeightText, 0, 2);
        obstConfigPane.add(obstHeightTextField, 1, 2);
        obstConfigPane.add(obstLengthText, 2, 2);
        obstConfigPane.add(obstLengthTextField, 3, 2);

        Label distThreshText = new Label("Distance from Threshold: ");
        distThreshTextField = new TextField();
        Label distCentreText = new Label("Distance from Centreline: ");
        distCentreTextField = new TextField();
        obstConfigPane.add(distThreshText, 0, 3);
        obstConfigPane.add(distThreshTextField, 1, 3);
        obstConfigPane.add(distCentreText, 2, 3);
        obstConfigPane.add(distCentreTextField, 3, 3);

        Button saveObstPreset = new Button("Save Obstacle as Preset");
        saveObstPreset.setAlignment(Pos.CENTER);
        Button applyObst = new Button("Apply");
        applyObst.setAlignment(Pos.CENTER);
        obstConfigPane.add(saveObstPreset, 1, 4);
        obstConfigPane.add(applyObst, 2, 4);

        config.getChildren().addAll(runwayConfigText, runwayConfigPane, obstConfigText, obstConfigPane);
        main.add(scrollPane, 2, 0, 1, 3);
    }

}