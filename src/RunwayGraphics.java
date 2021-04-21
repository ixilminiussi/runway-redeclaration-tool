import java.awt.event.MouseEvent;
import java.beans.EventHandler;
import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import javafx.scene.text.*;
import javafx.scene.image.Image;
import javafx.scene.shape.Line;
import javafx.scene.Scene;

public class RunwayGraphics {

    //where the graphics happen
    AnchorPane runwayDisplayAnchor;
    StackPane topView, sideView;
    Canvas topViewCanvas, sideViewCanvas;
    GraphicsContext topGc, sideGc;
    SplitPane splitView;

    //buttons
    VBox filtersVBox;
    CheckBox displacedThresholdBox, TORABox, LDABox, ASDABox, TODABox, obstacleBox, obstacleDistancesBox;
    HBox filtersVBoxContainer;
    ChoiceBox viewSelect;

    //used as reference when applying real life distances to the drawing, picked solely for clarity, each margin is taken, added from the previous margin
    final int SIDE_VIEW_THICKNESS = 30, CLEARWAY_MARGIN = 40, STOPWAY_MARGIN = 30, RESA_MARGIN = 40, BLAST_ALLOWANCE_MARGIN = 30, CENTERLINE_MARGIN = 80, THRESHOLD_MARGIN = 2, THRESHOLD_LENGTH = 30;
    AffectedRunway affectedRunway;
    final int CLEARED_AREA = 75 * 2, CANVAS_WIDTH = 800; //don't change CANVAS_WIDTH unless you really have to, not hard-coded yet

    //used as reference throughout the code, but fluid, prone to change depending on settings.
    int runwayPL;
    int posMargin = 0; //the margin before the runway starts
    int negMargin = 0; //the margin before the runway ends
    int spacing = 20;

    //color palette
    Color roadColor, stripeColor, warningColor, clearedAreaColor, hudColor, backgroundColor, outlineColor, obstacleColor1, obstacleColor2;
    Background paneBackground, hudBackground;

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

        //color palette
        roadColor = Color.LIGHTGRAY;
        stripeColor = Color.WHITE;
        warningColor = Color.GOLDENROD;
        clearedAreaColor = Color.DODGERBLUE;
        hudColor = Color.LIGHTSTEELBLUE;
        backgroundColor = Color.LIGHTCYAN;
        outlineColor = Color.BLACK;
        obstacleColor1 = Color.FIREBRICK;
        obstacleColor2 = Color.CRIMSON;
        paneBackground = new Background(new BackgroundFill(backgroundColor, CornerRadii.EMPTY, Insets.EMPTY));
        hudBackground = new Background(new BackgroundFill(hudColor, CornerRadii.EMPTY, Insets.EMPTY));

        setupSplitView();
        setupButtons();

        //where the graphics happen
        runwayDisplayAnchor.setBorder(new Border(new BorderStroke(Color.LIGHTSTEELBLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        runwayDisplayAnchor.setBackground(paneBackground);
    }

    //draws all elements from start, taking parameters/runway into account
    public void draw() {

        drawTopView(CANVAS_WIDTH, 400);
        drawSideView(CANVAS_WIDTH, 300);

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
            case 0: runwayDisplayAnchor.getChildren().addAll(topView, viewSelect, filtersVBoxContainer);
                break;
            case 1: runwayDisplayAnchor.getChildren().addAll(sideView, viewSelect, filtersVBoxContainer);
                break;
            case 3: splitView.getItems().clear();
                splitView.getItems().addAll(topView, sideView);
                runwayDisplayAnchor.getChildren().addAll(splitView, viewSelect, filtersVBoxContainer);
                break;
        }

        showMeasurements();
    }

    public void showMeasurements() {

        int spaceCount = 0;

        if (obstacleBox.isSelected()) {
            showObstacle();
        }
        if (obstacleDistancesBox.isSelected()) {
            showObstacleDistances();
        }
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

        topGc.translate(0, -spaceCount * spacing);
    }

    public void showObstacle() {
        Obstruction obs = affectedRunway.getObstruction();
        drawObstacle(obs.getDistanceFromThreshold(), obs.getDistanceFromCentre(), obs.getLength(), obs.getHeight());
    }

    public void showObstacleDistances() {
    }

    public void showTORA() {
        
        double TORA = affectedRunway.getTORA();

        drawMeasurement(topGc, String.valueOf(TORA), CANVAS_WIDTH - negMargin, 0, -getLengthRelativeToRunway(TORA), Orientation.HORIZONTAL);
        drawMeasurement(sideGc, String.valueOf(TORA), CANVAS_WIDTH - negMargin, 0, -getLengthRelativeToRunway(TORA), Orientation.HORIZONTAL);

        topGc.translate(0, spacing);
        sideGc.translate(0, spacing);
    }

    public void showLDA() {
        
        double LDA = affectedRunway.getLDA();

        drawMeasurement(topGc, String.valueOf(LDA), CANVAS_WIDTH - negMargin, 0, -getLengthRelativeToRunway(LDA), Orientation.HORIZONTAL);
        drawMeasurement(sideGc, String.valueOf(LDA), CANVAS_WIDTH - negMargin, 0, -getLengthRelativeToRunway(LDA), Orientation.HORIZONTAL);

        topGc.translate(0, spacing);
        sideGc.translate(0, spacing);
    }

    public void showASDA() {
        
        double ASDA = affectedRunway.getASDA();
        int offset = 0;
        int margin = 0;

        if (hasClearway()) { margin += CLEARWAY_MARGIN; }
        if (hasStopway()) { offset += STOPWAY_MARGIN; }

        drawMeasurement(topGc, String.valueOf(ASDA), CANVAS_WIDTH - margin, 0, -getLengthRelativeToRunway(ASDA) - offset, Orientation.HORIZONTAL);
        drawMeasurement(sideGc, String.valueOf(ASDA), CANVAS_WIDTH - margin, 0, -getLengthRelativeToRunway(ASDA) - offset, Orientation.HORIZONTAL);

        topGc.translate(0, spacing);
        sideGc.translate(0, spacing);
    }

    public void showTODA() {
        
        double TODA = affectedRunway.getTODA();
        int offset = 0; //assumes that if there is a clearway/stopway, their length is too small to be taken in count, proned to change

        if (hasClearway()) { offset += CLEARWAY_MARGIN; }
        if (hasStopway()) { offset += STOPWAY_MARGIN; }
        
        drawMeasurement(topGc, String.valueOf(TODA), CANVAS_WIDTH, 0, -getLengthRelativeToRunway(TODA) - offset, Orientation.HORIZONTAL);
        drawMeasurement(sideGc, String.valueOf(TODA), CANVAS_WIDTH, 0, -getLengthRelativeToRunway(TODA) - offset, Orientation.HORIZONTAL);

        topGc.translate(0, spacing);
        sideGc.translate(0, spacing);
    }

    public void showDisplacedThreshold() {

        double displacedThreshold = affectedRunway.getOriginalRunway().getDisplacedThreshold();

        drawMeasurement(topGc, String.valueOf(displacedThreshold), posMargin + THRESHOLD_MARGIN, 0, getLengthRelativeToRunway(displacedThreshold), Orientation.HORIZONTAL);
        drawMeasurement(sideGc, String.valueOf(displacedThreshold), posMargin + THRESHOLD_MARGIN, 0, getLengthRelativeToRunway(displacedThreshold), Orientation.HORIZONTAL);

        topGc.translate(0, spacing);
        sideGc.translate(0, spacing);
    }

    public void drawObstacle(double arg1, double arg2, double arg3, double arg4) {

        double x = getLengthRelativeToRunway(arg1);
        double y = getLengthRelativeToRunway(arg2);
        double length = getLengthRelativeToRunway(arg3);
        double height = getLengthRelativeToRunway(arg4);

        topGc.setStroke(outlineColor);
        topGc.setFill(createGrid(obstacleColor2, obstacleColor1));

        topGc.translate(posMargin + THRESHOLD_MARGIN, topGc.getCanvas().getHeight() / 2);
        topGc.fillRect(x, -y - length, length, length);
        topGc.strokeRect(x, -y - length, length, length);
        topGc.translate(-(posMargin + THRESHOLD_MARGIN), -topGc.getCanvas().getHeight() / 2);

        sideGc.setStroke(outlineColor);
        sideGc.setFill(createGrid(obstacleColor2, obstacleColor1));

        sideGc.translate(posMargin + THRESHOLD_MARGIN, (sideGc.getCanvas().getHeight() - SIDE_VIEW_THICKNESS) / 2);
        sideGc.fillRect(x, -height, length, height);
        sideGc.strokeRect(x, -height, length, height);
        sideGc.translate(-(posMargin + THRESHOLD_MARGIN), -(sideGc.getCanvas().getHeight() - SIDE_VIEW_THICKNESS) / 2);
    }

    //draws a line between 2 points with a measure/label displayed next to it
    public GraphicsContext drawMeasurement(GraphicsContext gc, String label, double x, double y, double length, Orientation o) {

        gc.translate(0, gc.getCanvas().getHeight()/2);
        gc.setStroke(outlineColor);
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
        filtersVBox = new VBox();
        ArrayList<CheckBox> filtersList = new ArrayList<CheckBox>();
        displacedThresholdBox = new CheckBox(" displaced threshold ");
        TORABox = new CheckBox(" TORA ");
        LDABox = new CheckBox(" LDA ");
        ASDABox = new CheckBox( " ASDA ");
        TODABox = new CheckBox(" TODA ");
        obstacleBox = new CheckBox(" obstacle ");
        obstacleDistancesBox = new CheckBox(" obstacle distances ");
        filtersList.add(displacedThresholdBox); filtersList.add(TORABox); filtersList.add(LDABox); filtersList.add(ASDABox); filtersList.add(TODABox);
        filtersList.add(obstacleBox); filtersList.add(obstacleDistancesBox);

        filtersVBox.getChildren().addAll(displacedThresholdBox, TORABox, LDABox, ASDABox, TODABox, obstacleBox, obstacleDistancesBox);
        filtersVBox.setSpacing(5.0);

        for(CheckBox checkBox : filtersList) {
            checkBox.selectedProperty().addListener(
                    (ObservableValue<? extends Boolean> ov, Boolean oldVal, Boolean newVal) -> {
                        draw();
                    }
            );
        }

        //only for styling purposes
        filtersVBoxContainer = new HBox();
        filtersVBoxContainer.getChildren().add(filtersVBox);
        filtersVBoxContainer.setMargin(filtersVBox, new Insets(5.0, 5.0, 5.0, 5.0));
        filtersVBoxContainer.setBackground(hudBackground);

        runwayDisplayAnchor.setTopAnchor(filtersVBoxContainer, 0.0);
        runwayDisplayAnchor.setRightAnchor(filtersVBoxContainer, 0.0);

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
                    case 0: runwayDisplayAnchor.getChildren().addAll(topView, viewSelect, filtersVBoxContainer);
                        break;
                    case 1: runwayDisplayAnchor.getChildren().addAll(sideView, viewSelect, filtersVBoxContainer);
                        break;
                    case 3: splitView.getItems().clear();
                        splitView.getItems().addAll(topView, sideView);
                        runwayDisplayAnchor.getChildren().addAll(splitView, viewSelect, filtersVBoxContainer);
                        break;
                }
            }
        });
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

        topGc.setFill(clearedAreaColor);

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

        topGc.setFill(createHatch(warningColor, roadColor));

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
        topGc.setFill(roadColor);
        topGc.fillRect(posMargin, -20, runwayPL, 40);

        //threshold
        for (int i = 0; i < 3; i ++) {
            topGc.setFill(stripeColor);
            topGc.fillRect(posMargin + THRESHOLD_MARGIN, -15 + (i * 12), THRESHOLD_LENGTH, 6);
            topGc.fillRect(canvasWidth - negMargin - THRESHOLD_LENGTH - THRESHOLD_MARGIN, -15 + (i * 12), THRESHOLD_LENGTH, 6);
        }

        topGc.setStroke(stripeColor);

        //centerline
        topGc.setLineWidth(3.0);
        topGc.setLineDashes(6, 8, 1, 8);
        topGc.strokeLine(posMargin + CENTERLINE_MARGIN, 0, canvasWidth - negMargin - CENTERLINE_MARGIN, 0);

        //outline
        topGc.translate(0, -canvasHeight / 2);
        topGc.setStroke(outlineColor);
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
        sideGc.setFill(clearedAreaColor);
        sideGc.fillRect(0, 0, canvasWidth, SIDE_VIEW_THICKNESS);

        //draws RESA if the original runway has one(simply leaves a blank space)
        if (hasRESA()) {
            posMargin += RESA_MARGIN;
        }
        
        //draws clearway if original runway has one(simply leaves a blank space)
        if (hasClearway()) {
            negMargin += CLEARWAY_MARGIN;
        }

        sideGc.setFill(createHatch(warningColor, roadColor));

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
        sideGc.setFill(roadColor);
        sideGc.fillRect(posMargin, 0, runwayPL, 20);

        //threshold
        sideGc.setFill(stripeColor);
        sideGc.fillRect(posMargin + THRESHOLD_MARGIN, 0, THRESHOLD_LENGTH, 8);
        sideGc.fillRect(canvasWidth - negMargin - THRESHOLD_LENGTH - THRESHOLD_MARGIN, 0, THRESHOLD_LENGTH, 8);

        //centerline
        sideGc.setStroke(stripeColor);
        sideGc.setLineWidth(4.0);
        sideGc.setLineDashes(8, 10, 2, 10);
        sideGc.strokeLine(posMargin + CENTERLINE_MARGIN, 2, canvasWidth - negMargin - CENTERLINE_MARGIN, 2);
        sideGc.strokeLine(posMargin + CENTERLINE_MARGIN, 4, canvasWidth - negMargin - CENTERLINE_MARGIN, 4);

        //outline
        sideGc.setStroke(outlineColor);
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
