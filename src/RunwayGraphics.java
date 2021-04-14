package sample;

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

    //used as reference when applying real life distances to the drawing
    double runwayPixelLength, sideViewPixelHeight, topViewPixelHeight;
    AffectedRunway affectedRunway;
    double runwayMargin, thresholdMargin, stopwayMargin, centerlineMargin;
    final double CLEARED_AREA = 75 * 2;

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

        //used as reference for the drawing (purely aesthetic choices, might change down the line)
        runwayMargin = 70.0;
        stopwayMargin = 40.0;
        thresholdMargin = 72.0;
        centerlineMargin = 150.0;
        sideViewPixelHeight = 30.0;

        setupSplitView();
        setupButtons();
        draw();

        //where the graphics happen
        runwayDisplayAnchor.setBorder(new Border(new BorderStroke(Color.LIGHTSTEELBLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        runwayDisplayAnchor.setBackground(paneBackground);
    }

    //draws all elements from start, taking parameters/runway into account
    public void draw() {

        drawTopView(800, 400);
        drawSideView(800, 300);

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
        if (displacedThresholdBox.isSelected()) {
            showDisplacedThreshold();
        }
        if (TORABox.isSelected()) {
            showTORA();
        }
        if (LDABox.isSelected()) {
            showLDA();
        }
        if (ASDABox.isSelected()) {
            showASDA();
        }
        if (TODABox.isSelected()) {
            showTODA();
        }
        if (obstacleBox.isSelected()) {
            showObstacle();
        }
        if (obstacleDistancesBox.isSelected()) {
            showObstacleDistances();
        }
    }

    public void showObstacle() {
        drawObstacle(affectedRunway.getObstruction().getDistanceFromThreshold(), affectedRunway.getObstruction().getDistanceFromCentre(),
                affectedRunway.getObstruction().getLength(), affectedRunway.getObstruction().getHeight());
    }

    public void showObstacleDistances() {
    }

    public void showTORA() {

        double TORALength = affectedRunway.getTORA();

        sideGc = drawMeasurement(sideGc, String.valueOf(TORALength), thresholdMargin, 20 , getLengthRelativeToRunway(TORALength), Orientation.HORIZONTAL);
        topGc = drawMeasurement(topGc, String.valueOf(TORALength), thresholdMargin, 25, getLengthRelativeToRunway(TORALength), Orientation.HORIZONTAL);
    }

    public void showLDA() {

    }

    public void showASDA() {

    }

    //compared to getLengthRelativeToRunway(), is used rather for Y axis, and to make certain distances slightly longer and more visible
    public double getLengthRelativeToClearway(double arg1) {
        return (arg1 * 120) / CLEARED_AREA;
    }

    public void showTODA() {

        double TODALength = affectedRunway.getTODA();

        //represented by a line going from first threshold to the end, regardless of length relative to TODA (aesthetic / clarity choice)
        sideGc = drawMeasurement(sideGc, String.valueOf(TODALength), thresholdMargin, 20 , sideGc.getCanvas().getWidth() - thresholdMargin - 2, Orientation.HORIZONTAL);
        topGc = drawMeasurement(topGc, String.valueOf(TODALength), thresholdMargin, 25, topGc.getCanvas().getWidth() - thresholdMargin - 2, Orientation.HORIZONTAL);
    }

    public void showDisplacedThreshold() {

        double displacedThresholdLength = affectedRunway.getOriginalRunway().getDisplacedThreshold();

        sideGc = drawMeasurement(sideGc, String.valueOf(displacedThresholdLength), thresholdMargin, 20 , getLengthRelativeToRunway(displacedThresholdLength), Orientation.HORIZONTAL);
        topGc = drawMeasurement(topGc, String.valueOf(displacedThresholdLength), thresholdMargin, 25, getLengthRelativeToRunway(displacedThresholdLength), Orientation.HORIZONTAL);
    }

    public void drawObstacle(double arg1, double arg2, double arg3, double arg4) {

        double x = getLengthRelativeToClearway(arg1);
        double y = getLengthRelativeToClearway(arg2);
        double length = getLengthRelativeToClearway(arg3);
        double height = getLengthRelativeToClearway(arg4);

        topGc.setStroke(outlineColor);
        topGc.setFill(createGrid(obstacleColor2, obstacleColor1));

        topGc.translate(thresholdMargin, topGc.getCanvas().getHeight() / 2);
        topGc.fillRect(x, -y - length, length, length);
        topGc.strokeRect(x, -y - length, length, length);
        topGc.translate(-thresholdMargin, -topGc.getCanvas().getHeight() / 2);

        sideGc.setStroke(outlineColor);
        sideGc.setFill(createGrid(obstacleColor2, obstacleColor1));

        sideGc.translate(thresholdMargin, (sideGc.getCanvas().getHeight() - sideViewPixelHeight) / 2);
        sideGc.fillRect(x, -height, length, height);
        sideGc.strokeRect(x, -height, length, height);
        sideGc.translate(-thresholdMargin, -(sideGc.getCanvas().getHeight() - sideViewPixelHeight) / 2);
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
                gc.strokeLine(x, y, x+delimiter, y-delimiter);
                gc.strokeLine(x, y, x+delimiter, y+delimiter);
                gc.strokeLine(x + length, y, x + length - delimiter, y-delimiter);
                gc.strokeLine(x + length, y, x + length - delimiter, y+delimiter);

                gc.setTextBaseline(VPos.TOP);
                gc.setTextAlign(TextAlignment.CENTER);
                gc.strokeText(label, x + (length/2), y + 4);
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

                gc.setTextBaseline(VPos.CENTER);
                gc.setTextAlign(TextAlignment.LEFT);
                gc.strokeText(label, x + 4, y + (length/2));
                break;
        }

        gc.translate(0, -gc.getCanvas().getHeight()/2);

        return gc;
    }

    private double getLengthRelativeToRunway(double meters) {

        //simple rule of 3
        return ((meters * (runwayPixelLength - ((thresholdMargin - runwayMargin) * 2))) / affectedRunway.getOriginalRunway().getTORA());
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

    public void drawTopView(double canvasWidth, double canvasHeight) {

        topViewCanvas = new Canvas (canvasWidth, canvasHeight);
        topGc = topViewCanvas.getGraphicsContext2D();

        topView = new StackPane();
        topView.setAlignment(Pos.CENTER);
        topView.getChildren().add(topViewCanvas);

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
        double yOffset = (canvasHeight - 180) / 2;

        //main square
        topGc.setFill(Color.DARKSEAGREEN);
        topGc.fillRect(0, 0, canvasWidth, canvasHeight);

        //cleared area
        topGc.translate(0, yOffset);

        topGc.setFill(clearedAreaColor);

        //keep it so that the clearedAreaWidth = 120p;
        topGc.beginPath();
        topGc.moveTo(0, 30);
        topGc.lineTo(85, 30);
        topGc.lineTo(120, 0);
        topGc.lineTo(canvasWidth - 125, 0);
        topGc.lineTo(canvasWidth - 85, 30);
        topGc.lineTo(canvasWidth, 30);
        topGc.lineTo(canvasWidth, 150);
        topGc.lineTo(canvasWidth - 85, 150);
        topGc.lineTo(canvasWidth - 125, 180);
        topGc.lineTo(120, 180);
        topGc.lineTo(85, 150);
        topGc.lineTo(0, 150);
        topGc.fill();
        topGc.closePath();

        //runway
        runwayPixelLength = canvasWidth - (runwayMargin * 2);
        topGc.setFill(roadColor);
        topGc.fillRect(runwayMargin, 70, runwayPixelLength, 40);

        //stopway
        topGc.setFill(createHatch(warningColor, roadColor));
        topGc.fillRect(stopwayMargin, 65, (runwayMargin - stopwayMargin), 50);
        topGc.fillRect(runwayMargin + runwayPixelLength, 65, (runwayMargin - stopwayMargin), 50);

        //threshold
        for (int i = 0; i < 3; i ++) {
            topGc.setFill(stripeColor);
            topGc.fillRect(thresholdMargin, 75 + (i * 12), 30, 6);
            topGc.fillRect(canvasWidth - thresholdMargin - 30, 75 + (i * 12), 30, 6);
            topGc.setStroke(stripeColor);
        }

        //centerline
        topGc.setLineWidth(3.0);
        topGc.setLineDashes(6, 8, 1, 8);
        topGc.strokeLine(centerlineMargin, 90, canvasWidth - centerlineMargin, 90);

        //outline
        topGc.translate(0, -yOffset);
        topGc.setStroke(outlineColor);
        topGc.setLineWidth(1);
        topGc.setLineDashes(1);
        topGc.strokeRect(0, 0, canvasWidth, canvasHeight);

        topView.setScaleX(1);
        topView.setScaleY(1);

    }

    public void drawSideView(double canvasWidth, double canvasHeight) {

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
        sideGc.translate(0, (canvasHeight - sideViewPixelHeight) / 2);

        //cleared area
        sideGc.setFill(clearedAreaColor);
        sideGc.fillRect(0, 0, canvasWidth, sideViewPixelHeight);

        //runway
        runwayPixelLength = canvasWidth - (runwayMargin * 2);
        sideGc.setFill(roadColor);
        sideGc.fillRect(runwayMargin, 0, runwayPixelLength, 20);

        //stopway
        sideGc.setFill(createHatch(warningColor, roadColor));
        sideGc.fillRect(stopwayMargin, 0, (runwayMargin - stopwayMargin), 10);
        sideGc.fillRect(runwayMargin + runwayPixelLength, 0, (runwayMargin - stopwayMargin), 10);

        //threshold
        sideGc.setFill(stripeColor);
        sideGc.fillRect(thresholdMargin, 0, 30, 8);
        sideGc.fillRect(canvasWidth - thresholdMargin - 30, 0, 30, 8);

        //centerline
        sideGc.setStroke(stripeColor);
        sideGc.setLineWidth(4.0);
        sideGc.setLineDashes(8, 10, 2, 10);
        sideGc.strokeLine(centerlineMargin, 2, canvasWidth - centerlineMargin, 2);
        sideGc.strokeLine(centerlineMargin, 4, canvasWidth - centerlineMargin, 4);

        //outline
        sideGc.setStroke(outlineColor);
        sideGc.setLineWidth(1);
        sideGc.setLineDashes(1);
        sideGc.strokeRect(0, 0, canvasWidth, sideViewPixelHeight);

        sideGc.translate(0, -(canvasHeight - sideViewPixelHeight) / 2);

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
}
