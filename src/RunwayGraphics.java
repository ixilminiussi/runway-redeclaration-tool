import java.awt.*;
import java.awt.Label;
import java.awt.event.MouseEvent;
import java.beans.EventHandler;
import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.text.*;
import javafx.scene.image.Image;
import javafx.scene.shape.Line;
import javafx.scene.Scene;
import javafx.scene.text.Font;

import static java.lang.Math.sqrt;

public class RunwayGraphics {

    //where the graphics happen
    AnchorPane runwayDisplayAnchor;
    StackPane topView, sideView;
    Canvas topViewCanvas, sideViewCanvas;
    GraphicsContext topGc, sideGc;
    SplitPane splitView;

    //buttons
    HBox filtersGridPaneContainer;
    GridPane filtersGridPane;
    Text displacedThresholdLabel, TORALabel, LDALabel, ASDALabel, TODALabel;
    Text obstacleLabel, obstacleDistancesLabel, clearwayLabel, stopwayLabel, RESALabel;
    Text stripEndLabel, blastProtLabel, ALSLabel, TOCSLabel, runwayStripLabel;
    CheckBox displacedThresholdBox, TORABox, LDABox, ASDABox, TODABox;
    CheckBox obstacleBox, obstacleDistancesBox, clearwayBox, stopwayBox, RESABox;
    CheckBox stripEndBox, blastProtBox, ALSBox, TOCSBox, runwayStripBox;
    ChoiceBox viewSelect;
    Obstruction obstruction;

    //used as reference when applying real life distances to the drawing, picked solely for clarity, each margin is taken, added from the previous margin
    final int SIDE_VIEW_THICKNESS = 30, CLEARWAY_MARGIN = 40, STOPWAY_MARGIN = 30, RESA_MARGIN = 40;
    final int BLAST_ALLOWANCE_MARGIN = 30, CENTERLINE_MARGIN = 80, THRESHOLD_MARGIN = 2, THRESHOLD_LENGTH = 30;
    AffectedRunway affectedRunway;
    final int CLEARED_AREA = 75 * 2, CANVAS_WIDTH = 800; //don't change CANVAS_WIDTH unless you really have to, not hard-coded yet

    //used as reference throughout the code, but fluid, prone to change depending on settings.
    int runwayPL;
    int posMargin = 0; //the margin before the runway starts
    int negMargin = 0; //the margin before the runway ends
    int spacing = 20;

    //color palette
    final Color ROAD_COLOR = Color.LIGHTGRAY, STRIPE_COLOR = Color.WHITE, WARNING_COLOR = Color.GOLDENROD;
    final Color CLEARED_AREA_COLOR = Color.DODGERBLUE, HUD_COLOR = Color.LIGHTSTEELBLUE, BACKGROUND_COLOR = Color.LIGHTCYAN;
    final Color OUTLINE_COLOR = Color.BLACK, OBSTACLE_COLOR1 = Color.FIREBRICK, OBSTACLE_COLOR2 = Color.CRIMSON;
    final Color GRASS_COLOR = Color.DARKSEAGREEN, SAFE_COLOR = Color.GREEN, DANGER_COLOR = Color.RED;
    Background paneBackground, hudBackground;

    //color coding palette
    Color displacedThresholdColor = Color.NAVY, TORAColor = Color.ORANGERED, LDAColor = Color.DARKMAGENTA, ASDAColor = Color.MAROON, TODAColor = Color.SIENNA;
    Color clearwayColor, stopwayColor, RESAColor = Color.DEEPPINK;
    Color stripEndColor, blastProtColor, ALSColor, TOCSColor, runwayStripColor;

    double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;



    public AnchorPane getRunwayGraphics() {

        return runwayDisplayAnchor;
    }

    public void setAffectedRunway(AffectedRunway affectedRunway) {

        this.affectedRunway = affectedRunway;
    }

    public RunwayGraphics() {

        runwayDisplayAnchor = new AnchorPane();
        paneBackground = new Background(new BackgroundFill(BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY));
        hudBackground = new Background(new BackgroundFill(HUD_COLOR, CornerRadii.EMPTY, Insets.EMPTY));

        setupSplitView();
        setupButtons();

        //where the graphics happen
        runwayDisplayAnchor.setBorder(new Border(new BorderStroke(Color.LIGHTSTEELBLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        runwayDisplayAnchor.setBackground(paneBackground);
    }

    public void draw(AffectedRunway affectedRunway) {

        this.affectedRunway = affectedRunway;
        draw();
    }

    //draws all elements from start, taking parameters/runway into account
    public void draw() {
        obstruction = affectedRunway.getObstruction();

        drawTopView(CANVAS_WIDTH, 400);
        drawSideView(CANVAS_WIDTH, 300);

        setupButtons();

        //centers elements
        runwayDisplayAnchor.setTopAnchor(topView, 0.0);
        runwayDisplayAnchor.setBottomAnchor(topView, 0.0);
        runwayDisplayAnchor.setLeftAnchor(topView, 0.0);
        runwayDisplayAnchor.setRightAnchor(topView, 0.0);

        runwayDisplayAnchor.setTopAnchor(sideView, 0.0);
        runwayDisplayAnchor.setBottomAnchor(sideView, 0.0);
        runwayDisplayAnchor.setLeftAnchor(sideView, 0.0);
        runwayDisplayAnchor.setRightAnchor(sideView, 0.0);

        runwayDisplayAnchor.setTopAnchor(splitView, 0.0);
        runwayDisplayAnchor.setBottomAnchor(splitView, 0.0);
        runwayDisplayAnchor.setLeftAnchor(splitView, 0.0);
        runwayDisplayAnchor.setRightAnchor(splitView, 0.0);

        //puts the appropriate pane on, according to the viewSelect choicebox
        runwayDisplayAnchor.getChildren().clear();
        switch (viewSelect.getSelectionModel().getSelectedIndex()) {
            case 0: runwayDisplayAnchor.getChildren().addAll(topView, viewSelect, filtersGridPaneContainer);
                break;
            case 1: runwayDisplayAnchor.getChildren().addAll(sideView, viewSelect, filtersGridPaneContainer);
                break;
            case 3: splitView.getItems().clear();
                splitView.getItems().addAll(topView, sideView);
                runwayDisplayAnchor.getChildren().addAll(splitView, viewSelect, filtersGridPaneContainer);
                break;
        }

        showMeasurements();
    }

    public void showMeasurements() {


        if (obstacleBox.isSelected()) {
            showObstacle();
        }
        if (obstacleDistancesBox.isSelected()) {
            showObstacleDistances();
        }

        topGc.translate(0, spacing);
        sideGc.translate(0, spacing);
        int spaceCount = 1;

        if (displacedThresholdBox.isSelected()) {
            showDisplacedThreshold();
            spaceCount ++;
        }
        if (TORABox.isSelected()) {
            showTORA();
            spaceCount ++;
        }
        if (LDABox.isSelected()) {
            showLDA();
            spaceCount ++;
        }
        if (ASDABox.isSelected()) {
            showASDA();
            spaceCount ++;
        }
        if (TODABox.isSelected()) {
            showTODA();
            spaceCount ++;
        }
        if (RESABox.isSelected()) {
            showRESA();
            spaceCount++;
        }
        if (blastProtBox.isSelected()) {
            showBlastProtection();
            spaceCount++;
        }
        if (TOCSBox.isSelected()) {
            showTOCS();
            spaceCount++;
        }

        topGc.translate(0, -spaceCount * spacing);
    }

    public void showObstacle() {

        obstruction = affectedRunway.getObstruction();
        //affectedRunway.recalculate(obstruction);
        drawObstacle(obstruction.getDistanceFromThreshold(), obstruction.getDistanceFromCentre(), obstruction.getLength(), obstruction.getHeight());
    }

    public void showObstacleDistances() {

        obstruction = affectedRunway.getObstruction();
        double OrigTODA = affectedRunway.getOriginalRunway().getTODA();
        double NewTODA = affectedRunway.getTODA();
        double OrigTORA = affectedRunway.getOriginalRunway().getTORA();
        double NewTORA = affectedRunway.getTORA();
        double OrigASDA = affectedRunway.getOriginalRunway().getASDA();
        double NewASDA = affectedRunway.getASDA();
        double OrigLDA = affectedRunway.getOriginalRunway().getLDA();
        double NewLDA = affectedRunway.getLDA();

        //double red = (affectedRunway.getOriginalRunway().getTORA() - affectedRunway.getTORA());
        double red = OrigLDA - NewLDA;
        double green = OrigLDA;



        drawDangerZones(0, 200, red, 50, DANGER_COLOR, "");
        drawDangerZones(red, 200, green, 50, SAFE_COLOR, "");
        drawTriangle(obstruction.getDistanceFromThreshold(), obstruction.getHeight(), red);
    }

    public void drawTriangle(double distanceFromThreshold, double height, double unsafeArea) {

        double x1 = getLengthRelativeToRunway(distanceFromThreshold);
        double y1 = getLengthRelativeToRunway(0);
        double x2 = getLengthRelativeToRunway(distanceFromThreshold);
        double y2 = - getLengthRelativeToRunway(height);
        double x3 = getLengthRelativeToRunway(height*50);
        double y3 = getLengthRelativeToRunway(0);

        sideGc.setStroke(OUTLINE_COLOR);
        sideGc.setFill(DANGER_COLOR);
        sideGc.beginPath();
        sideGc.translate(posMargin + THRESHOLD_MARGIN, (sideGc.getCanvas().getHeight() - SIDE_VIEW_THICKNESS) / 2);
        sideGc.moveTo(x1, y1);
        sideGc.lineTo(x2, y2);
        sideGc.lineTo(x3, y3);
        sideGc.lineTo(x1, y1);
        sideGc.translate(-(posMargin + THRESHOLD_MARGIN), -(sideGc.getCanvas().getHeight() - SIDE_VIEW_THICKNESS) / 2);
        sideGc.fill();
        sideGc.closePath();
    }

    public void showTORA() {

        double TORA = affectedRunway.getTORA();

        drawMeasurement(topGc, String.valueOf(TORA), CANVAS_WIDTH - negMargin, 0, -getLengthRelativeToRunway(TORA), Orientation.HORIZONTAL, TORAColor);
        drawMeasurement(sideGc, String.valueOf(TORA), CANVAS_WIDTH - negMargin, 0, -getLengthRelativeToRunway(TORA), Orientation.HORIZONTAL, TORAColor);

        topGc.translate(0, spacing);
        sideGc.translate(0, spacing);
    }

    public void showLDA() {

        double LDA = affectedRunway.getLDA();

        drawMeasurement(topGc, String.valueOf(LDA), CANVAS_WIDTH - negMargin, 0, -getLengthRelativeToRunway(LDA), Orientation.HORIZONTAL, LDAColor);
        drawMeasurement(sideGc, String.valueOf(LDA), CANVAS_WIDTH - negMargin, 0, -getLengthRelativeToRunway(LDA), Orientation.HORIZONTAL, LDAColor);

        topGc.translate(0, spacing);
        sideGc.translate(0, spacing);
    }

    public void showASDA() {

        double ASDA = affectedRunway.getASDA();
        int offset = 0;
        int margin = 0;

        if (hasClearway()) { margin += CLEARWAY_MARGIN; }
        if (hasStopway()) { offset += STOPWAY_MARGIN; }

        drawMeasurement(topGc, String.valueOf(ASDA), CANVAS_WIDTH - margin, 0, -getLengthRelativeToRunway(ASDA) - offset, Orientation.HORIZONTAL, ASDAColor);
        drawMeasurement(sideGc, String.valueOf(ASDA), CANVAS_WIDTH - margin, 0, -getLengthRelativeToRunway(ASDA) - offset, Orientation.HORIZONTAL, ASDAColor);

        topGc.translate(0, spacing);
        sideGc.translate(0, spacing);
    }

    public void showTODA() {

        double TODA = affectedRunway.getTODA();
        int offset = 0; //assumes that if there is a clearway/stopway, their length is too small to be taken in count, proned to change

        if (hasClearway()) { offset += CLEARWAY_MARGIN; }
        if (hasStopway()) { offset += STOPWAY_MARGIN; }

        drawMeasurement(topGc, String.valueOf(TODA), CANVAS_WIDTH, 0, -getLengthRelativeToRunway(TODA) - offset, Orientation.HORIZONTAL, TODAColor);
        drawMeasurement(sideGc, String.valueOf(TODA), CANVAS_WIDTH, 0, -getLengthRelativeToRunway(TODA) - offset, Orientation.HORIZONTAL, TODAColor);

        topGc.translate(0, spacing);
        sideGc.translate(0, spacing);
    }

    public void showRESA() {

        Obstruction obst = affectedRunway.getObstruction();
        double dist = getLengthRelativeToRunway(obst.getDistanceFromThreshold());
        double NewTORA = affectedRunway.getTORA();
        double RESA = affectedRunway.getOriginalRunway().getRESA();
        double height = obstruction.getHeight();
        System.out.println("RESA: " + affectedRunway.getOriginalRunway().getRESA());
        System.out.println("strip end: " + affectedRunway.getOriginalRunway().getStripEnd());

        double a = CANVAS_WIDTH - negMargin - (getLengthRelativeToRunway(NewTORA));


        drawMeasurement(topGc, String.valueOf(RESA), a, 0, -getLengthRelativeToRunway(240), Orientation.HORIZONTAL, RESAColor);
        drawMeasurement(sideGc, String.valueOf(RESA), a, 0, -getLengthRelativeToRunway(240), Orientation.HORIZONTAL, RESAColor);

        topGc.translate(0, spacing);
        sideGc.translate(0, spacing);

    }

    public void showBlastProtection() {

    }

    public void showTOCS() {

    }

    public void showDisplacedThreshold() {

        double displacedThreshold = affectedRunway.getOriginalRunway().getDisplacedThreshold();

        drawMeasurement(topGc, String.valueOf(displacedThreshold), posMargin + THRESHOLD_MARGIN, 0, getLengthRelativeToRunway(displacedThreshold), Orientation.HORIZONTAL, displacedThresholdColor);
        drawMeasurement(sideGc, String.valueOf(displacedThreshold), posMargin + THRESHOLD_MARGIN, 0, getLengthRelativeToRunway(displacedThreshold), Orientation.HORIZONTAL, displacedThresholdColor);

        topGc.translate(0, spacing);
        sideGc.translate(0, spacing);
    }

    public void drawDangerZones(double x1, double y1, double length1, double height1, Color fill, String label) {

        double x = getLengthRelativeToRunway(x1);
        double y = getLengthRelativeToRunway(y1);
        double length = getLengthRelativeToRunway(height1*50);
        double height = getLengthRelativeToRunway(height1);
        double length2 = getLengthRelativeToRunway(length1);

        if (fill == DANGER_COLOR) {
            topGc.setStroke(OUTLINE_COLOR);
            topGc.setFill(createGrid(fill, fill));
            topGc.translate(posMargin + THRESHOLD_MARGIN, topGc.getCanvas().getHeight() / 2);
            topGc.fillRect(x, -y, length, height);
            topGc.strokeRect(x, -y, length, height);
            topGc.translate(-(posMargin + THRESHOLD_MARGIN), -topGc.getCanvas().getHeight() / 2);
        }
        if (fill == SAFE_COLOR) {
            topGc.setStroke(OUTLINE_COLOR);
            topGc.setFill(createGrid(fill, fill));
            topGc.translate(posMargin + THRESHOLD_MARGIN, topGc.getCanvas().getHeight() / 2);
            topGc.fillRect(x, -y, length2, height);
            topGc.strokeRect(x, -y, length2, height);
            topGc.translate(-(posMargin + THRESHOLD_MARGIN), -topGc.getCanvas().getHeight() / 2);
        }
    }

    public void drawObstacle(double arg1, double arg2, double arg3, double arg4) {

        double x = getLengthRelativeToRunway(arg1);
        double y = getLengthRelativeToRunway(arg2);
        double length = getLengthRelativeToRunway(arg3);
        double height = getLengthRelativeToRunway(arg4);

        topGc.setStroke(OUTLINE_COLOR);
        topGc.setFill(createGrid(OBSTACLE_COLOR2, OBSTACLE_COLOR1));

        topGc.translate(posMargin + THRESHOLD_MARGIN, topGc.getCanvas().getHeight() / 2);
        topGc.fillRect(x, -y - length, length, length);
        topGc.strokeRect(x, -y - length, length, length);
        topGc.translate(-(posMargin + THRESHOLD_MARGIN), -topGc.getCanvas().getHeight() / 2);

        sideGc.setStroke(OUTLINE_COLOR);
        sideGc.setFill(createGrid(OBSTACLE_COLOR2, OBSTACLE_COLOR1));

        sideGc.translate(posMargin + THRESHOLD_MARGIN, (sideGc.getCanvas().getHeight() - SIDE_VIEW_THICKNESS) / 2);
        sideGc.fillRect(x, -height, length, height);
        sideGc.strokeRect(x, -height, length, height);
        sideGc.translate(-(posMargin + THRESHOLD_MARGIN), -(sideGc.getCanvas().getHeight() - SIDE_VIEW_THICKNESS) / 2);
    }

    //draws a line between 2 points with a measure/label displayed next to it
    public GraphicsContext drawMeasurement(GraphicsContext gc, String label, double x, double y, double length, Orientation o, Color color) {

        gc.translate(0, gc.getCanvas().getHeight()/2);
        gc.setStroke(color);
        gc.setLineWidth(1.0);

        double delimiter = 5;

        switch (o) {

            case HORIZONTAL :
                //main line
                gc.setLineDashes(5,5);
                gc.strokeLine(x, y, x + length, y);
                //side lines
                gc.setLineDashes(1);
                if (length >= 0) {
                    gc.strokeLine(x, y, x + delimiter, y - delimiter);
                    gc.strokeLine(x, y, x + delimiter, y + delimiter);
                    gc.strokeLine(x + length, y, x + length - delimiter, y - delimiter);
                    gc.strokeLine(x + length, y, x + length - delimiter, y + delimiter);
                } else {
                    gc.strokeLine(x, y, x - delimiter, y - delimiter);
                    gc.strokeLine(x, y, x - delimiter, y + delimiter);
                    gc.strokeLine(x + length, y, x + length + delimiter, y - delimiter);
                    gc.strokeLine(x + length, y, x + length + delimiter, y + delimiter);
                }

                gc.setTextBaseline(VPos.TOP);
                gc.setTextAlign(TextAlignment.CENTER);
                gc.strokeText(label, x + (length/2), y);
                break;
            case VERTICAL :
                //main line
                gc.setLineDashes(2, 2, 1, 2);
                gc.strokeLine(x, y, x, y + length);
                //side lines
                gc.setLineDashes(1);
                gc.strokeLine(x, y, x + delimiter, y + delimiter);
                gc.strokeLine(x, y, x - delimiter, y + delimiter);
                gc.strokeLine(x, y + length, x + delimiter, y + length - delimiter);
                gc.strokeLine(x, y + length, x - delimiter, y + length - delimiter);

                Text text = new Text(label);
                gc.setTextBaseline(VPos.CENTER);
                gc.setTextAlign(TextAlignment.LEFT);
                gc.setFont(new Font(20));
                gc.strokeText(label, x + 4, y + (length/2));
                break;
        }

        gc.translate(0, -gc.getCanvas().getHeight()/2);

        return gc;
    }

    private double getLengthRelativeToRunway(double meters) {

        Runway runway = affectedRunway.getOriginalRunway();

        //simple rule of 3
        return ((meters * runwayPL) / runway.getTORA());
    }

    public void setupButtons() {

        //---------FILTERS----------
        //checkboxes to add measurements, currently for testing, eventually to give control to the user as to which information he wants to see/hide
        filtersGridPane = new GridPane();

        Text showLabel = new Text(" show ");
        showLabel.setStyle("-fx-font-weight: bold");
        Text distancesLabel = new Text(" calculated distancese ");
        distancesLabel.setStyle("-fx-font-weight: bold");

        //allows us to separate between the first call of setupButtons and the later ones
        if (affectedRunway != null) {

            displacedThresholdLabel = new Text(" displaced threshold : " + String.valueOf(affectedRunway.getOriginalRunway().getDisplacedThreshold()) + "m");
            TORALabel = new Text(" TORA : " + String.valueOf(affectedRunway.getTORA()) + "m");
            LDALabel = new Text(" LDA : " + String.valueOf(affectedRunway.getLDA()) + "m");
            ASDALabel = new Text( " ASDA : " + String.valueOf(affectedRunway.getASDA()) + "m");
            TODALabel = new Text(" TODA : " + String.valueOf(affectedRunway.getTODA()) + "m");
            obstacleLabel = new Text(" obstacle ");
            obstacleDistancesLabel = new Text(" obstacle distances ");
            RESALabel = new Text(" RESA : " + String.valueOf(affectedRunway.getOriginalRunway().getRESA()) + "m");
            blastProtLabel = new Text(" blast protection : " + String.valueOf(affectedRunway.getOriginalRunway().getBlastAllowance()) + "m");
            TOCSLabel = new Text(" TOCS ");
        } else {

            displacedThresholdLabel = new Text(" displaced threshold : " + "m");
            TORALabel = new Text(" TORA : m");
            LDALabel = new Text(" LDA : m");
            ASDALabel = new Text( " ASDA : m");
            TODALabel = new Text(" TODA : m");
            obstacleLabel = new Text(" obstacle ");
            obstacleDistancesLabel = new Text(" obstacle distances ");
            RESALabel = new Text(" RESA : m");
            blastProtLabel = new Text(" blast protection : m");
            TOCSLabel = new Text(" TOCS ");

            // checkboxes
            ArrayList<CheckBox> filtersList = new ArrayList<CheckBox>();
            displacedThresholdBox = new CheckBox();
            TORABox = new CheckBox();
            LDABox = new CheckBox();
            ASDABox = new CheckBox();
            TODABox = new CheckBox();
            obstacleBox = new CheckBox();
            obstacleDistancesBox = new CheckBox();
            RESABox = new CheckBox();
            blastProtBox = new CheckBox();
            TOCSBox = new CheckBox();

            filtersList.add(displacedThresholdBox); filtersList.add(TORABox); filtersList.add(LDABox); filtersList.add(ASDABox); filtersList.add(TODABox);
            filtersList.add(obstacleBox); filtersList.add(obstacleDistancesBox); filtersList.add(RESABox); filtersList.add(blastProtBox); filtersList.add(TOCSBox);

            for(CheckBox checkBox : filtersList) {
                checkBox.selectedProperty().addListener(
                        (ObservableValue<? extends Boolean> ov, Boolean oldVal, Boolean newVal) -> {
                            draw();
                        }
                );
            }
        }

        //colors the text with appropriate colors
        displacedThresholdLabel.setFill(displacedThresholdColor);
        TORALabel.setFill(TORAColor);
        LDALabel.setFill(LDAColor);
        ASDALabel.setFill(ASDAColor);
        TODALabel.setFill(TODAColor);
        RESALabel.setFill(RESAColor);
        obstacleLabel.setFill(DANGER_COLOR);
        obstacleDistancesLabel.setFill(DANGER_COLOR);

        //only for styling purposes
        filtersGridPaneContainer = new HBox();
        filtersGridPaneContainer.getChildren().add(filtersGridPane);
        filtersGridPaneContainer.setMargin(filtersGridPane, new Insets(5.0, 5.0, 5.0, 5.0));
        filtersGridPaneContainer.setBackground(hudBackground);

        //filler panes for styling
        Pane pane1 = new Pane();
        Pane pane2 = new Pane();
        filtersGridPane.add(distancesLabel, 0, 0, 1, 1);
        filtersGridPane.add(showLabel, 2, 0, 1, 1);
        filtersGridPane.add(pane1, 0, 1, 3, 1);
        filtersGridPane.add(pane2, 1, 0, 1, 12);

        filtersGridPane.add(displacedThresholdBox, 2, 2, 1, 1);
        filtersGridPane.add(TORABox, 2, 3, 1, 1);
        filtersGridPane.add(LDABox, 2, 4, 1, 1);
        filtersGridPane.add(ASDABox, 2, 5, 1, 1);
        filtersGridPane.add(TODABox, 2, 6, 1, 1);
        filtersGridPane.add(obstacleBox, 2, 7, 1, 1);
        filtersGridPane.add(obstacleDistancesBox, 2, 8, 1, 1);
        filtersGridPane.add(RESABox, 2, 9, 1, 1);
        filtersGridPane.add(blastProtBox, 2, 10, 1, 1);
        filtersGridPane.add(TOCSBox, 2, 11, 1, 1);

        filtersGridPane.add(displacedThresholdLabel, 0, 2, 1, 1);
        filtersGridPane.add(TORALabel, 0, 3, 1, 1);
        filtersGridPane.add(LDALabel, 0, 4, 1, 1);
        filtersGridPane.add(ASDALabel, 0, 5, 1, 1);
        filtersGridPane.add(TODALabel, 0, 6, 1, 1);
        filtersGridPane.add(obstacleLabel, 0, 7, 1, 1);
        filtersGridPane.add(obstacleDistancesLabel, 0, 8, 1, 1);
        filtersGridPane.add(RESALabel, 0, 9, 1, 1);
        filtersGridPane.add(blastProtLabel, 0, 10, 1, 1);
        filtersGridPane.add(TOCSLabel, 0, 11, 1, 1);

        pane1.minWidth(1);
        pane2.minHeight(1);
        pane1.setStyle("-fx-border-color: black");
        pane2.setStyle("-fx-border-color: black");

        runwayDisplayAnchor.setTopAnchor(filtersGridPane, 0.0);
        runwayDisplayAnchor.setRightAnchor(filtersGridPaneContainer, 0.0);

        if (affectedRunway == null) {
            //----------VIEW SELECT----------
            //changing views
            viewSelect = new ChoiceBox<String>();
            viewSelect.getItems().addAll("Top View", "Side View", new Separator(), "Split View");
            viewSelect.setValue("Top View");
            viewSelect.setBackground(hudBackground);
            runwayDisplayAnchor.setTopAnchor(viewSelect, 0.0);
            runwayDisplayAnchor.setLeftAnchor(viewSelect, 0.0);
            viewSelect.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){
                @Override
                public void changed(ObservableValue ov, Number oldSelected, Number newSelected) {
                    //required to avoid duplicates
                    runwayDisplayAnchor.getChildren().clear();
                    switch (newSelected.intValue()) {
                        case 0: runwayDisplayAnchor.getChildren().addAll(topView, viewSelect, filtersGridPaneContainer);
                            break;
                        case 1: runwayDisplayAnchor.getChildren().addAll(sideView, viewSelect, filtersGridPaneContainer);
                            break;
                        case 3: splitView.getItems().clear();
                            splitView.getItems().addAll(topView, sideView);
                            runwayDisplayAnchor.getChildren().addAll(splitView, viewSelect, filtersGridPaneContainer);
                            break;
                    }
                }
            });
        }
    }

    public void drawTopView(int canvasWidth, int canvasHeight) {

        topViewCanvas = new Canvas (canvasWidth, canvasHeight);
        topGc = topViewCanvas.getGraphicsContext2D();

        topView = new StackPane();
        topView.setAlignment(Pos.CENTER);
        topView.getChildren().add(topViewCanvas);

        //--------ZOOMING AND PANNING---------
        topViewCanvas.setOnMousePressed(e -> {
            orgSceneX = e.getSceneX();
            //orgSceneY = e.getSceneY();
            orgTranslateX = ((Canvas) (e.getSource())).getTranslateX();
            //orgTranslateY = ((Canvas)(e.getSource())).getTranslateY();
        });
        topViewCanvas.setOnMouseDragged(e -> {
            double offsetX = e.getSceneX() - orgSceneX;
            //double offsetY = e.getSceneY() - orgSceneY;
            double newTranslateX = orgTranslateX + offsetX;
            //double newTranslateY = orgTranslateY + offsetY;
            ((Canvas) (e.getSource())).setTranslateX(newTranslateX);
            //((Canvas) (e.getSource())).setTranslateY(newTranslateY);
        });

        topView.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        topView.setOnScroll(
                e -> {
                    if (e.isShortcutDown() && e.getDeltaY() != 0) {
                        if (e.getDeltaY() < 0) {
                            topView.setScaleX(Math.max(topView.getScaleX() - 0.1, 0.5));
                        } else {
                            topView.setScaleX(Math.min(topView.getScaleX() + 0.1, 5.0));
                        }
                        topView.setScaleY(topView.getScaleX());
                        e.consume(); // prevents ScrollEvent from reaching ScrollPane
                    }
                });

        //-------DRAW CANVAS-------

        posMargin = 0;
        negMargin = 0;

        //main square
        topGc.setFill(Color.DARKSEAGREEN);
        topGc.fillRect(0, 0, canvasWidth, canvasHeight);

        //cleared area
        topGc.translate(0, canvasHeight / 2);

        topGc.setFill(CLEARED_AREA_COLOR);

        //keep it so that the clearedAreaWidth = 120p;
        topGc.beginPath();
        topGc.moveTo(0, -60);
        topGc.lineTo(85, -60);
        topGc.lineTo(120, -90);
        topGc.lineTo(canvasWidth - 125, -90);
        topGc.lineTo(canvasWidth - 85, -60);
        topGc.lineTo(canvasWidth, -60);
        topGc.lineTo(canvasWidth, 60);
        topGc.lineTo(canvasWidth - 85, 60);
        topGc.lineTo(canvasWidth - 125, 90);
        topGc.lineTo(120, 90);
        topGc.lineTo(85, 60);
        topGc.lineTo(0, 60);
        topGc.fill();
        topGc.closePath();

        //draws RESA if the original runway has one(simply leaves a blank space)
        if (hasRESA()) {
            posMargin += RESA_MARGIN;
        }

        //draws clearway if original runway has one(simply leaves a blank space)
        if (hasClearway()) {
            negMargin += CLEARWAY_MARGIN;
        }

        topGc.setFill(createHatch(WARNING_COLOR, ROAD_COLOR));

        //draws blast allowance if original runway has one
        if (hasBlastAllowance()) {
            topGc.fillRect(posMargin, -25, BLAST_ALLOWANCE_MARGIN, 50);
            posMargin += BLAST_ALLOWANCE_MARGIN;
        }

        //draws stopway if original runway has one
        if (hasStopway()) {
            topGc.fillRect(canvasWidth - negMargin - CLEARWAY_MARGIN, -25, CLEARWAY_MARGIN, 50);
            negMargin += CLEARWAY_MARGIN;
        }

        //drawsRunway
        runwayPL = canvasWidth - (posMargin + negMargin);
        topGc.setFill(ROAD_COLOR);
        topGc.fillRect(posMargin, -20, runwayPL, 40);

        //threshold
        for (int i = 0; i < 3; i ++) {
            topGc.setFill(STRIPE_COLOR);
            topGc.fillRect(posMargin + THRESHOLD_MARGIN, -15 + (i * 12), THRESHOLD_LENGTH, 6);
            topGc.fillRect(canvasWidth - negMargin - THRESHOLD_LENGTH - THRESHOLD_MARGIN, -15 + (i * 12), THRESHOLD_LENGTH, 6);
        }

        topGc.setStroke(STRIPE_COLOR);

        //centerline
        topGc.setLineWidth(3.0);
        topGc.setLineDashes(6, 8, 1, 8);
        topGc.strokeLine(posMargin + CENTERLINE_MARGIN, 0, canvasWidth - negMargin - CENTERLINE_MARGIN, 0);

        //outline
        topGc.translate(0, -canvasHeight / 2);
        topGc.setStroke(OUTLINE_COLOR);
        topGc.setLineWidth(1);
        topGc.setLineDashes(1);
        topGc.strokeRect(0, 0, canvasWidth, canvasHeight);

        topView.setScaleX(1);
        topView.setScaleY(1);
    }

    public void drawSideView(int canvasWidth, int canvasHeight) {

        sideViewCanvas = new Canvas (canvasWidth, canvasHeight);
        sideGc = sideViewCanvas.getGraphicsContext2D();

        sideViewCanvas.setOnMousePressed(e -> {
            orgSceneX = e.getSceneX();
            //orgSceneY = e.getSceneY();
            orgTranslateX = ((Canvas) (e.getSource())).getTranslateX();
            //orgTranslateY = ((Canvas)(e.getSource())).getTranslateY();
        });
        sideViewCanvas.setOnMouseDragged(e -> {
            double offsetX = e.getSceneX() - orgSceneX;
            //double offsetY = e.getSceneY() - orgSceneY;
            double newTranslateX = orgTranslateX + offsetX;
            //double newTranslateY = orgTranslateY + offsetY;
            ((Canvas) (e.getSource())).setTranslateX(newTranslateX);
            //((Canvas) (e.getSource())).setTranslateY(newTranslateY);
        });

        sideView = new StackPane();
        sideView.setAlignment(Pos.CENTER);
        sideView.getChildren().add(sideViewCanvas);

        sideView.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        // OR sideView
        sideViewCanvas.setOnScroll(
                e -> {
                    if (e.isShortcutDown() && e.getDeltaY() != 0) {
                        if (e.getDeltaY() < 0) {
                            sideView.setScaleX(Math.max(sideView.getScaleX() - 0.1, 0.5));
                        } else {
                            sideView.setScaleX(Math.min(sideView.getScaleX() + 0.1, 5.0));
                        }
                        sideView.setScaleY(sideView.getScaleX());
                        e.consume(); // prevents ScrollEvent from reaching ScrollPane
                    }
                });

        //-------DRAW CANVAS-------

        posMargin = 0;
        negMargin = 0;

        sideGc.translate(0, (canvasHeight - SIDE_VIEW_THICKNESS) / 2);

        //cleared area
        sideGc.setFill(CLEARED_AREA_COLOR);
        sideGc.fillRect(0, 0, canvasWidth, SIDE_VIEW_THICKNESS);

        //draws RESA if the original runway has one(simply leaves a blank space)
        if (hasRESA()) {
            posMargin += RESA_MARGIN;
        }

        //draws clearway if original runway has one(simply leaves a blank space)
        if (hasClearway()) {
            negMargin += CLEARWAY_MARGIN;
        }

        sideGc.setFill(createHatch(WARNING_COLOR, ROAD_COLOR));

        //draws blast allowance if original runway has one
        if (hasBlastAllowance()) {
            sideGc.fillRect(posMargin, 0, BLAST_ALLOWANCE_MARGIN, 10);
            posMargin += BLAST_ALLOWANCE_MARGIN;
        }

        //draws stopway if original runway has one
        if (hasStopway()) {
            sideGc.fillRect(canvasWidth - negMargin - CLEARWAY_MARGIN, 0, CLEARWAY_MARGIN, 10);
            negMargin += CLEARWAY_MARGIN;
        }

        //draws runway
        runwayPL = canvasWidth - (posMargin + negMargin);
        sideGc.setFill(ROAD_COLOR);
        sideGc.fillRect(posMargin, 0, runwayPL, 20);

        //threshold
        sideGc.setFill(STRIPE_COLOR);
        sideGc.fillRect(posMargin + THRESHOLD_MARGIN, 0, THRESHOLD_LENGTH, 8);
        sideGc.fillRect(canvasWidth - negMargin - THRESHOLD_LENGTH - THRESHOLD_MARGIN, 0, THRESHOLD_LENGTH, 8);

        //centerline
        sideGc.setStroke(STRIPE_COLOR);
        sideGc.setLineWidth(4.0);
        sideGc.setLineDashes(8, 10, 2, 10);
        sideGc.strokeLine(posMargin + CENTERLINE_MARGIN, 2, canvasWidth - negMargin - CENTERLINE_MARGIN, 2);
        sideGc.strokeLine(posMargin + CENTERLINE_MARGIN, 4, canvasWidth - negMargin - CENTERLINE_MARGIN, 4);

        //outline
        sideGc.setStroke(OUTLINE_COLOR);
        sideGc.setLineWidth(1);
        sideGc.setLineDashes(1);
        sideGc.strokeRect(0, 0, canvasWidth, SIDE_VIEW_THICKNESS);

        sideGc.translate(0, -(canvasHeight - SIDE_VIEW_THICKNESS) / 2);

        sideView.setScaleX(1);
        sideView.setScaleY(1);
    }

    public void setupSplitView() {

        splitView = new SplitPane();
        splitView.setOrientation(Orientation.VERTICAL);
        splitView.setBackground(paneBackground);
    }

    //taken from https://stackoverflow.com/questions/39297719/javafx-create-hatchstyles-something-like-of-c-sharp-net
    private ImagePattern createHatch(Color hashColor, Color backColor) {

        Canvas canvas = new Canvas(20, 20);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(backColor);
        gc.fillRect(0, 0, 20, 20);
        gc.setStroke(hashColor);
        gc.setLineWidth(3.0);
        gc.strokeLine(-5, -5, 25, 25);
        gc.strokeLine(-5, 25, 25, -5);

        Image snapshot = canvas.snapshot(null, null);
        ImagePattern imagePattern = new ImagePattern(snapshot, 0, 0, 20, 20, false);

        return imagePattern;
    }

    private ImagePattern createGrid(Color gridColor, Color backColor) {

        Canvas canvas = new Canvas(10, 10);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(backColor);
        gc.fillRect(0, 0, 10, 10);
        gc.setFill(gridColor);
        gc.fillRect(0, 0, 5, 5);
        gc.fillRect(5, 5, 5, 5);

        Image snapshot = canvas.snapshot(null, null);
        ImagePattern imagePattern = new ImagePattern(snapshot, 0, 0, 10, 10, false);

        return imagePattern;
    }

    //important boolean functions
    boolean hasClearway() { return (affectedRunway.getOriginalRunway().getClearway() != 0); }

    boolean hasStopway() { return (affectedRunway.getOriginalRunway().getStopway() != 0); }

    boolean hasBlastAllowance() { return (affectedRunway.getOriginalRunway().getBlastAllowance() != 0); }

    boolean hasRESA() { return (affectedRunway.getOriginalRunway().getRESA() != 0); }
}
