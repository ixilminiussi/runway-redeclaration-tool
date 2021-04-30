import java.awt.*;
import java.awt.Label;
import java.awt.event.MouseEvent;
import java.beans.EventHandler;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
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
import javafx.scene.SnapshotParameters;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;

import static java.lang.Math.sqrt;

public class RunwayGraphics {

    Theme theme;

    //used for when we clear the current runway, will draw a runway which is clearly not meant to be looked at
    AffectedRunway placeHolderRunway;

    //where the graphics happen
    Stage stage;
    AnchorPane runwayDisplayAnchor;
    StackPane topView, sideView;
    Canvas topViewCanvas, sideViewCanvas;
    GraphicsContext topGc, sideGc;
    SplitPane splitView;
    Canvas currentView;

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
    Text olddt, oldtora, oldlda, oldasda, oldtoda;

    //used as reference when applying real life distances to the drawing, picked solely for clarity, each margin is taken, added from the previous margin
    final int SIDE_VIEW_THICKNESS = 30, CLEARWAY_MARGIN = 40, STOPWAY_MARGIN = 30, RESA_MARGIN = 40;
    final int BLAST_ALLOWANCE_MARGIN = 30, CENTERLINE_MARGIN = 80, THRESHOLD_MARGIN = 2, THRESHOLD_LENGTH = 30;
    AffectedRunway affectedRunway;
    final int CLEARED_AREA = 75 * 2, CANVAS_WIDTH = 800; //don't change CANVAS_WIDTH unless you really have to, not hard-coded yet

    //used as reference throughout the code, but fluid, prone to change depending on settings.
    int runwayPL;
    int posMargin = 0; //the margin before the runway starts
    int negMargin = 0; //the margin before the runway ends
    int spacing = 25;

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

    public RunwayGraphics(Stage stage, Theme theme) {
        this.theme = theme;
        this.stage = stage;
        placeHolderRunway = new AffectedRunway(new Runway("N/A","N/A",10,0,0,0,0,0,0,1,1,1,1),new Obstruction("N/A", 0, 0, 0, 0));
        affectedRunway = placeHolderRunway;
        runwayDisplayAnchor = new AnchorPane();
        paneBackground = new Background(new BackgroundFill(BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY));
        hudBackground = new Background(new BackgroundFill(HUD_COLOR, CornerRadii.EMPTY, Insets.EMPTY));
        currentView = topViewCanvas;
        setupSplitView();
        setupButtons();

        //where the graphics happen
        runwayDisplayAnchor.setBorder(new Border(new BorderStroke(theme.getHUDColor(), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        runwayDisplayAnchor.setBackground(new Background(new BackgroundFill(theme.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public void draw(AffectedRunway affectedRunway) {

        this.affectedRunway = affectedRunway;
        draw();
    }

    public void clearRunway() {
        this.affectedRunway = placeHolderRunway;
        draw();
    }

    public void drawRotated(AffectedRunway affectedRunway) {
        this.affectedRunway = affectedRunway;
//        draw();
        System.out.println(topViewCanvas.rotateProperty().getValue());
        if(topViewCanvas.rotateProperty().getValue() == 0.0) {
            int degrees;
            String name = affectedRunway.getOriginalRunway().getName();

            if(name.length() > 2) {
                degrees = Integer.parseInt(name.substring(0, 2) + "0") + 90;
            } else {
                degrees = Integer.parseInt(name + "0") + 90;
            }
            System.out.println(degrees);
            topViewCanvas.rotateProperty().setValue(degrees);
        } else {
            System.out.println(topViewCanvas.rotateProperty().getValue());
            topViewCanvas.rotateProperty().setValue(0.0);
            System.out.println(topViewCanvas.rotateProperty().getValue());
        }

    }

    public void saveCanvasImage(String exporttarget) {
        /**FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath()));
        fileChooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("PNG Files", "*.png"));
        fileChooser.setTitle("Export runway calculations to PNG");
        File exporttarget = fileChooser.showSaveDialog(stage); **/
        if(exporttarget == null) {
            Alert errorMessage = new Alert(Alert.AlertType.ERROR);
            errorMessage.setContentText("Create a file to save runway into.");
            errorMessage.show();
        } else {
            String filename = exporttarget; //String filename = exporttarget.getAbsolutePath();
            String fileType = filename.substring(filename.length() - 3);
            File out = new File(filename);
            try {
                if(currentView == null) {
                    System.out.println(currentView);
                    System.out.println(topViewCanvas);
                }
                WritableImage wim = new WritableImage((int) Math.round(currentView.getWidth()), (int) Math.round(currentView.getHeight()));
                currentView.snapshot(null, wim);
                ImageIO.write(SwingFXUtils.fromFXImage(wim, null), fileType, out);
                /**Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Image Exported Successfully");
                success.setContentText("Image has been saved to " + filename);
                success.show();**/
            } catch (IOException e) {
                Alert errorMessage = new Alert(Alert.AlertType.ERROR);
                errorMessage.setTitle("Error Creating File");
                errorMessage.setContentText(e.getMessage());
                errorMessage.show();
            }

        }
    }


    public void saveCanvasImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(FileSystemView.getFileSystemView().getDefaultDirectory().getPath()));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG Files", "*.png"), new FileChooser.ExtensionFilter("JPG Files", "*.jpg"), new FileChooser.ExtensionFilter("GIF Files", "*.gif"));
        fileChooser.setTitle("Export runway calculations");
        File exporttarget = fileChooser.showSaveDialog(stage);
        if(exporttarget == null) {
            Alert errorMessage = new Alert(Alert.AlertType.ERROR);
            errorMessage.setContentText("Create a file to save runway into.");
            errorMessage.show();
        } else {
            String filename = exporttarget.getAbsolutePath();
            /**if (!filename.substring(filename.length() - 4).equals(".png")) {
                filename = filename.concat(".png");
            }**/
            String fileType = filename.substring(filename.length() - 3);
            File out = new File(filename);
            try {
                if(currentView == null) {
                    System.out.println(currentView);
                    System.out.println(topViewCanvas);
                }
                WritableImage wim = new WritableImage((int) Math.round(currentView.getWidth()), (int) Math.round(currentView.getHeight()));
                SnapshotParameters params = new SnapshotParameters();
                params.setFill(theme.getHUDColor());
                currentView.snapshot(params, wim);
                ImageIO.write(SwingFXUtils.fromFXImage(wim, null), fileType, out);
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Image Exported Successfully");
                success.setContentText("Image has been saved to " + filename);
                success.show();
            } catch (IOException e) {
                Alert errorMessage = new Alert(Alert.AlertType.ERROR);
                errorMessage.setTitle("Error Creating File");
                errorMessage.setContentText(e.getMessage());
                errorMessage.show();
            }

        }
    }

    //draws all elements from start, taking parameters/runway into account
    public void draw() {
        obstruction = affectedRunway.getObstruction();

        filtersGridPaneContainer.setBackground(new Background(new BackgroundFill(theme.getHUDColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        runwayDisplayAnchor.setBackground(new Background(new BackgroundFill(theme.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        splitView.setBackground(new Background(new BackgroundFill(theme.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));

        drawTopView(CANVAS_WIDTH, 400, false);
        drawSideView(CANVAS_WIDTH, 400);

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
                currentView = topViewCanvas;
                break;
            case 1: runwayDisplayAnchor.getChildren().addAll(sideView, viewSelect, filtersGridPaneContainer);
                currentView = sideViewCanvas;
                break;
            case 3: splitView.getItems().clear();
            	setupSplitView();
            	runwayDisplayAnchor.setTopAnchor(splitView, 0.0);
            	runwayDisplayAnchor.setBottomAnchor(splitView, 0.0);
            	runwayDisplayAnchor.setLeftAnchor(splitView, 0.0);
            	runwayDisplayAnchor.setRightAnchor(splitView, 0.0);
                splitView.getItems().addAll(topView, sideView);
                currentView = topViewCanvas;
                runwayDisplayAnchor.getChildren().addAll(splitView, viewSelect, filtersGridPaneContainer);
                break;
        }



        showMeasurements();
    }

    public void showMeasurements() {
    	
    	showTitle();

        if (obstacleBox.isSelected()) {
            showObstacle();
        }
//        if (obstacleDistancesBox.isSelected()) {
//            showObstacleDistances();
//        }

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
        if (RESABox.isSelected() && !affectedRunway.isUnchanged()) {
            showRESA();
            spaceCount++;
        }

        topGc.translate(0, -spaceCount * spacing);
    }

    public void showObstacle() {

        obstruction = affectedRunway.getObstruction();
        //affectedRunway.recalculate(obstruction);
        drawObstacle(obstruction.getDistanceFromThreshold(), obstruction.getDistanceFromCentre(), obstruction.getLength(), obstruction.getHeight());
        double OrigTODA = affectedRunway.getOriginalRunway().getTODA();
        double NewTODA = affectedRunway.getTODA();
        double OrigTORA = affectedRunway.getOriginalRunway().getTORA();
        double NewTORA = affectedRunway.getTORA();
        double OrigASDA = affectedRunway.getOriginalRunway().getASDA();
        double NewASDA = affectedRunway.getASDA();
        double OrigLDA = affectedRunway.getOriginalRunway().getLDA();
        double NewLDA = affectedRunway.getLDA();

        int runwayLength = affectedRunway.getOriginalRunway().getTORA();
        int obDistance = obstruction.getDistanceFromThreshold();
        int height = obstruction.getHeight();

        double red = runwayLength - NewLDA;
        double green = NewLDA;

        if (obDistance > (runwayLength/2)) {
            drawDangerZones(0, 200 ,green, 50, theme.getSafeColor(), "LDA", obDistance);
            drawDangerZones(green, 200, red, 50, theme.getDangerColor(), "ALS/TOCS", obDistance);
            drawTriangle(obstruction.getDistanceFromThreshold(), obstruction.getHeight(), 2);
        }
        if (obDistance <= (runwayLength/2)) {
            drawDangerZones(0, 200, red, 50, theme.getDangerColor(), "ALS/TOCS", obDistance);
            drawDangerZones(red, 200, green, 50, theme.getSafeColor(), "LDA", obDistance);
            drawTriangle(obstruction.getDistanceFromThreshold(), obstruction.getHeight(), 1);
        }
    }

//    public void showObstacleDistances() {
//
//        obstruction = affectedRunway.getObstruction();
//
//    }

    public void drawTriangle(double distanceFromThreshold, double height, double unsafeArea) {

        double x1 = getLengthRelativeToRunway(distanceFromThreshold);
        double y1 = getLengthRelativeToRunway(0);
        double x2 = getLengthRelativeToRunway(distanceFromThreshold);
        double y2 = - getLengthRelativeToRunway(height);
        double x3 = getLengthRelativeToRunway(distanceFromThreshold + (height*50));
        double y3 = getLengthRelativeToRunway(0);

        if (unsafeArea == 1) {
            sideGc.setStroke(theme.getOutlineColor());
            sideGc.setFill(theme.getDangerColor());
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
        if (unsafeArea == 2) {
            sideGc.setStroke(theme.getOutlineColor());
            sideGc.setFill(theme.getDangerColor());
            sideGc.beginPath();
            sideGc.translate(posMargin + THRESHOLD_MARGIN, (sideGc.getCanvas().getHeight() - SIDE_VIEW_THICKNESS) / 2);
            sideGc.moveTo(x1, y1);
            sideGc.lineTo(x2, y2);
            double x4 = getLengthRelativeToRunway(distanceFromThreshold - (height*50));
            sideGc.lineTo(x4, y3);
            sideGc.lineTo(x1, y1);
            sideGc.translate(-(posMargin + THRESHOLD_MARGIN), -(sideGc.getCanvas().getHeight() - SIDE_VIEW_THICKNESS) / 2);
            sideGc.fill();
            sideGc.closePath();
        }

    }
    
    public void showTitle() {

        String title = affectedRunway.getOriginalRunway().getName() + " - " + affectedRunway.getOriginalRunway().getAirport();

        Text text = new Text(title);
        
        topGc.setFont(new Font(20));
        topGc.setTextAlign(TextAlignment.CENTER);
        topGc.setLineWidth(1.7);
        //topGc.setStroke(theme.getRESAColor());
        
        topGc.strokeText(title, CANVAS_WIDTH / 2, 50);

        topGc.setFont(new Font(15));
        topGc.setTextAlign(TextAlignment.LEFT);
        topGc.setLineWidth(1.2);
         
        sideGc.setFont(new Font(20));
        sideGc.setTextAlign(TextAlignment.CENTER);
        sideGc.setLineWidth(1.7);
//      sideGc.setStroke(theme.getRESAColor());
        
        sideGc.strokeText(title, CANVAS_WIDTH / 2, 50);
        
        sideGc.setFont(new Font(15));
        sideGc.setTextAlign(TextAlignment.LEFT);
        sideGc.setLineWidth(1.2);
    }

    public void showTORA() {

        double TORA = affectedRunway.getTORA();
        int runwayLength = affectedRunway.getOriginalRunway().getTORA();
        int obDistance = obstruction.getDistanceFromThreshold();

        if (obDistance > (runwayLength/2)) {
            drawMeasurement(topGc, "TORA: " + TORA, posMargin + THRESHOLD_MARGIN, 0, getLengthRelativeToRunway(TORA), Orientation.HORIZONTAL, theme.getTORAColor());
            drawMeasurement(sideGc, "TORA: " + TORA, posMargin + THRESHOLD_MARGIN, 0, getLengthRelativeToRunway(TORA), Orientation.HORIZONTAL, theme.getTORAColor());
        }
        if (obDistance <= (runwayLength/2)) {
            drawMeasurement(topGc, "TORA: " + TORA, CANVAS_WIDTH - negMargin, 0, -getLengthRelativeToRunway(TORA), Orientation.HORIZONTAL, theme.getTORAColor());
            drawMeasurement(sideGc, "TORA: " + TORA, CANVAS_WIDTH - negMargin, 0, -getLengthRelativeToRunway(TORA), Orientation.HORIZONTAL, theme.getTORAColor());
        }

        topGc.translate(0, spacing);
        sideGc.translate(0, spacing);
    }

    public void showLDA() {

        double LDA = affectedRunway.getLDA();
        int runwayLength = affectedRunway.getOriginalRunway().getTORA();
        int obDistance = obstruction.getDistanceFromThreshold();

        if (obDistance > (runwayLength/2)) {
            drawMeasurement(topGc, "LDA: " + LDA, posMargin + THRESHOLD_MARGIN, 0, getLengthRelativeToRunway(LDA), Orientation.HORIZONTAL, theme.getLDAColor());
            drawMeasurement(sideGc, "LDA: " + LDA, posMargin + THRESHOLD_MARGIN, 0, getLengthRelativeToRunway(LDA), Orientation.HORIZONTAL, theme.getLDAColor());
        }
        if (obDistance <= (runwayLength/2)) {
            drawMeasurement(topGc, "LDA: " + LDA, CANVAS_WIDTH - negMargin, 0, -getLengthRelativeToRunway(LDA), Orientation.HORIZONTAL, theme.getLDAColor());
            drawMeasurement(sideGc, "LDA: " + LDA, CANVAS_WIDTH - negMargin, 0, -getLengthRelativeToRunway(LDA), Orientation.HORIZONTAL, theme.getLDAColor());
        }

        topGc.translate(0, spacing);
        sideGc.translate(0, spacing);
    }

    public void showASDA() {

        double ASDA = affectedRunway.getASDA();
        int offset = 0;
        int margin = 0;

        if (hasClearway()) { margin += CLEARWAY_MARGIN; }
        if (hasStopway()) { offset += STOPWAY_MARGIN; }
        int runwayLength = affectedRunway.getOriginalRunway().getTORA();
        int obDistance = obstruction.getDistanceFromThreshold();

        if (obDistance > (runwayLength/2)) {
            drawMeasurement(topGc, "ASDA: " + ASDA, posMargin + THRESHOLD_MARGIN, 0, getLengthRelativeToRunway(ASDA) , Orientation.HORIZONTAL, theme.getASDAColor());
            drawMeasurement(sideGc, "ASDA: " + ASDA, posMargin + THRESHOLD_MARGIN, 0, getLengthRelativeToRunway(ASDA) , Orientation.HORIZONTAL, theme.getASDAColor());
        }
        if (obDistance <= (runwayLength/2)) {
            drawMeasurement(topGc, "ASDA: " + ASDA, CANVAS_WIDTH - margin, 0, -getLengthRelativeToRunway(ASDA) - offset , Orientation.HORIZONTAL, theme.getASDAColor());
            drawMeasurement(sideGc, "ASDA: " + ASDA, CANVAS_WIDTH - margin, 0, -getLengthRelativeToRunway(ASDA) - offset, Orientation.HORIZONTAL, theme.getASDAColor());
        }

        topGc.translate(0, spacing);
        sideGc.translate(0, spacing);
    }

    public void showTODA() {

        double TODA = affectedRunway.getTODA();
        int offset = 0; //assumes that if there is a clearway/stopway, their length is too small to be taken in count, proned to change

        if (hasClearway()) { offset += CLEARWAY_MARGIN; }
        if (hasStopway()) { offset += STOPWAY_MARGIN; }
        int runwayLength = affectedRunway.getOriginalRunway().getTORA();
        int obDistance = obstruction.getDistanceFromThreshold();

        if (obDistance > (runwayLength/2)) {
            drawMeasurement(topGc, "TODA: " + TODA, posMargin + THRESHOLD_MARGIN, 0, getLengthRelativeToRunway(TODA), Orientation.HORIZONTAL, theme.getTODAColor());
            drawMeasurement(sideGc, "TODA: " + TODA, posMargin + THRESHOLD_MARGIN, 0, getLengthRelativeToRunway(TODA), Orientation.HORIZONTAL, theme.getTODAColor());
        }
        if (obDistance <= (runwayLength/2)) {
            drawMeasurement(topGc, "TODA: " + TODA, CANVAS_WIDTH, 0, -getLengthRelativeToRunway(TODA) - offset, Orientation.HORIZONTAL, theme.getTODAColor());
            drawMeasurement(sideGc, "TODA: " + TODA, CANVAS_WIDTH, 0, -getLengthRelativeToRunway(TODA) - offset, Orientation.HORIZONTAL, theme.getTODAColor());
        }

        topGc.translate(0, spacing);
        sideGc.translate(0, spacing);
    }

    public void showRESA() {

        Obstruction obst = affectedRunway.getObstruction();
        double dist = getLengthRelativeToRunway(obst.getDistanceFromThreshold());
        double NewTORA = affectedRunway.getTORA();
        double NewLDA = affectedRunway.getLDA();
        double RESA = affectedRunway.getOriginalRunway().getRESA();
        /*
        double height = obstruction.getHeight();
        System.out.println("RESA: " + affectedRunway.getOriginalRunway().getRESA());
        System.out.println("strip end: " + affectedRunway.getOriginalRunway().getStripEnd());
        */

        double a = CANVAS_WIDTH - negMargin - (getLengthRelativeToRunway(NewTORA));
        double b = getLengthRelativeToRunway(NewLDA + 60);

        int runwayLength = affectedRunway.getOriginalRunway().getTORA();
        int obDistance = obstruction.getDistanceFromThreshold();

        if (obDistance > (runwayLength/2)) {
            drawMeasurement(topGc, "RESA: " + RESA, posMargin + THRESHOLD_MARGIN + b, 0, getLengthRelativeToRunway(RESA), Orientation.HORIZONTAL, theme.getRESAColor());
            drawMeasurement(sideGc, "RESA: " + RESA, posMargin + THRESHOLD_MARGIN + b, 0, getLengthRelativeToRunway(RESA), Orientation.HORIZONTAL, theme.getRESAColor());
        }
        if (obDistance <= (runwayLength/2)) {
            drawMeasurement(topGc, "RESA: " + RESA, CANVAS_WIDTH - b, 0, -getLengthRelativeToRunway(RESA), Orientation.HORIZONTAL, theme.getRESAColor());
            drawMeasurement(sideGc, "RESA: " + RESA, CANVAS_WIDTH - b, 0, -getLengthRelativeToRunway(RESA), Orientation.HORIZONTAL, theme.getRESAColor());
        }

        topGc.translate(0, spacing);
        sideGc.translate(0, spacing);

    }

    public void showDisplacedThreshold() {

        double displacedThreshold = affectedRunway.getOriginalRunway().getDisplacedThreshold();

        drawMeasurement(topGc, "DT: " + displacedThreshold, posMargin + THRESHOLD_MARGIN, 0, getLengthRelativeToRunway(displacedThreshold), Orientation.HORIZONTAL, theme.getDisplacedThresholdColor());
        drawMeasurement(sideGc, "DT: " + displacedThreshold, posMargin + THRESHOLD_MARGIN, 0, getLengthRelativeToRunway(displacedThreshold), Orientation.HORIZONTAL, theme.getDisplacedThresholdColor());

        topGc.translate(0, spacing);
        sideGc.translate(0, spacing);
    }

    public void drawDangerZones(double xStart, double yStart, double length, double height, Color fill, String label, int distance) {

        double x = getLengthRelativeToRunway(xStart);
        double y = getLengthRelativeToRunway(yStart);
        double newLength = getLengthRelativeToRunway(height*50);
        double newHeight = getLengthRelativeToRunway(height);
        double length2 = getLengthRelativeToRunway(length);

        int runwayLength = affectedRunway.getOriginalRunway().getTORA();

        if (fill == theme.getDangerColor()) {
        	topGc.setTextAlign(TextAlignment.CENTER);
        	if (length2 > 0) topGc.strokeText(label, x + posMargin + THRESHOLD_MARGIN + (length2 / 2), (topGc.getCanvas().getHeight() / 2) -y - 5);
            topGc.setStroke(theme.getOutlineColor());
            topGc.setFill(createGrid(fill, fill));
            topGc.translate(posMargin + THRESHOLD_MARGIN, topGc.getCanvas().getHeight() / 2);
            topGc.fillRect(x, -y, newLength, newHeight);
            topGc.strokeRect(x, -y, newLength, newHeight);
            topGc.translate(-(posMargin + THRESHOLD_MARGIN), -topGc.getCanvas().getHeight() / 2);
        }
        if (fill == theme.getSafeColor()) {
            topGc.setTextAlign(TextAlignment.CENTER);
            if (length2 > 0) topGc.strokeText(label, x + posMargin + THRESHOLD_MARGIN + (length2 / 2), (topGc.getCanvas().getHeight() / 2) -y - 5);
            topGc.setStroke(theme.getOutlineColor());
            topGc.setFill(createGrid(fill, fill));
            topGc.translate(posMargin + THRESHOLD_MARGIN, topGc.getCanvas().getHeight() / 2);
            topGc.fillRect(x, -y, length2, newHeight);
            topGc.strokeRect(x, -y, length2, newHeight);
            topGc.translate(-(posMargin + THRESHOLD_MARGIN), -topGc.getCanvas().getHeight() / 2);
        }
    }

    public void drawObstacle(double arg1, double arg2, double arg3, double arg4) {

        double x = getLengthRelativeToRunway(arg1);
        double y = getLengthRelativeToRunway(arg2);
        double length = getLengthRelativeToRunway(arg3);
        double height = getLengthRelativeToRunway(arg4);

        topGc.setStroke(theme.getOutlineColor());
        topGc.setFill(createGrid(theme.getObstacleColor2(), theme.getObstacleColor1()));

        topGc.translate(posMargin + THRESHOLD_MARGIN, topGc.getCanvas().getHeight() / 2);
        topGc.fillRect(x, -y - length, length, length);
        topGc.strokeRect(x, -y - length, length, length);
        topGc.translate(-(posMargin + THRESHOLD_MARGIN), -topGc.getCanvas().getHeight() / 2);

        sideGc.setStroke(theme.getOutlineColor());
        sideGc.setFill(createGrid(theme.getObstacleColor2(), theme.getObstacleColor1()));

        sideGc.translate(posMargin + THRESHOLD_MARGIN, (sideGc.getCanvas().getHeight() - SIDE_VIEW_THICKNESS) / 2);
        sideGc.fillRect(x, -height, length, height);
        sideGc.strokeRect(x, -height, length, height);
        sideGc.translate(-(posMargin + THRESHOLD_MARGIN), -(sideGc.getCanvas().getHeight() - SIDE_VIEW_THICKNESS) / 2);
    }

    //draws a line between 2 points with a measure/label displayed next to it
    public GraphicsContext drawMeasurement(GraphicsContext gc, String label, double x, double y, double length, Orientation o, Color color) {

        gc.translate(0, gc.getCanvas().getHeight()/2);
        gc.setStroke(color);
        gc.setLineWidth(2.0);

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
                gc.setLineWidth(1.0);
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
                gc.setLineWidth(1.0);
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

        filtersGridPane = new GridPane();

        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        ColumnConstraints col3 = new ColumnConstraints();
        ColumnConstraints col4 = new ColumnConstraints();
        col1.setPercentWidth(35);
        col2.setPercentWidth(25);
        col3.setPercentWidth(20);
        col4.setPercentWidth(20);
        filtersGridPane.getColumnConstraints().addAll(col1, col2, col3, col4);
        filtersGridPane.setVgap(10);
        filtersGridPane.setHgap(40);

        Text showLabel = new Text("Show");
        Text oldLabel = new Text("Original Values");
        Text newLabel = new Text("New Values");
        Text typeLabel = new Text("Measurement");

        showLabel.setFill(theme.getOutlineColor());
        oldLabel.setFill(theme.getOutlineColor());
        newLabel.setFill(theme.getOutlineColor());
        typeLabel.setFill(theme.getOutlineColor());

        if(affectedRunway != null && affectedRunway != placeHolderRunway) {
            displacedThresholdLabel = new Text(String.valueOf(affectedRunway.getOriginalRunway().getDisplacedThreshold()) + "m");
            TORALabel = new Text(String.valueOf(affectedRunway.getTORA()) + "m");
            LDALabel = new Text(String.valueOf(affectedRunway.getLDA()) + "m");
            ASDALabel = new Text(String.valueOf(affectedRunway.getASDA()) + "m");
            TODALabel = new Text(String.valueOf(affectedRunway.getTODA()) + "m");
            obstacleLabel = new Text(" obstacle ");
            obstacleDistancesLabel = new Text(" obstacle distances ");
            RESALabel = new Text(String.valueOf(affectedRunway.getOriginalRunway().getRESA()) + "m");
            oldtoda = new Text(affectedRunway.getOriginalRunway().getTODA() + "m");
            oldtora = new Text(affectedRunway.getOriginalRunway().getTORA() + "m");
            oldlda = new Text(affectedRunway.getOriginalRunway().getLDA() + "m");
            oldasda = new Text(affectedRunway.getOriginalRunway().getASDA() + "m");

        } else {

            displacedThresholdLabel = new Text("N/A");
            TORALabel = new Text("N/A");
            LDALabel = new Text("N/A");
            ASDALabel = new Text("N/A");
            TODALabel = new Text("N/A");
            obstacleLabel = new Text(" obstacle ");
            obstacleDistancesLabel = new Text(" obstacle distances ");
            RESALabel = new Text("N/A");
            oldtoda = new Text("N/A");
            oldtora = new Text("N/A");
            oldlda = new Text("N/A");
            oldasda = new Text("N/A");

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

            filtersList.add(displacedThresholdBox); filtersList.add(TORABox); filtersList.add(LDABox); filtersList.add(ASDABox); filtersList.add(TODABox);
            filtersList.add(obstacleBox); filtersList.add(RESABox);

            for(CheckBox checkBox : filtersList) {
                checkBox.setSelected(true);
                checkBox.selectedProperty().addListener(
                        (ObservableValue<? extends Boolean> ov, Boolean oldVal, Boolean newVal) -> {
                            draw();
                        }
                );
            }

        }

        //colors the text with appropriate colors
        displacedThresholdLabel.setFill(theme.getDisplacedThresholdColor());
        TORALabel.setFill(theme.getTORAColor());
        oldtora.setFill(theme.getTORAColor());
        LDALabel.setFill(theme.getLDAColor());
        oldlda.setFill(theme.getLDAColor());
        ASDALabel.setFill(theme.getASDAColor());
        oldasda.setFill(theme.getASDAColor());
        TODALabel.setFill(theme.getTODAColor());
        oldtoda.setFill(theme.getTODAColor());
        RESALabel.setFill(theme.getRESAColor());
        obstacleLabel.setFill(theme.getDangerColor());
//        obstacleDistancesLabel.setFill(dangerColor);

        filtersGridPane.add(typeLabel, 0, 0);
        filtersGridPane.add(oldLabel, 1, 0);
        filtersGridPane.add(newLabel, 2, 0);
        filtersGridPane.add(showLabel, 3, 0);

        Text TODAText = new Text("TODA:");
        TODAText.setFill(theme.getOutlineColor());
        filtersGridPane.add(TODAText, 0, 1);
        Text TORAText = new Text("TORA:");
        TORAText.setFill(theme.getOutlineColor());
        filtersGridPane.add(TORAText, 0, 2);
        Text LDAText = new Text("LDA:");
        LDAText.setFill(theme.getOutlineColor());
        filtersGridPane.add(LDAText, 0, 3);
        Text ASDAText = new Text("ASDA");
        ASDAText.setFill(theme.getOutlineColor());
        filtersGridPane.add(ASDAText, 0, 4);
        Text displacedThresholdText = new Text("Displaced Threshold:");
        displacedThresholdText.setFill(theme.getOutlineColor());
        filtersGridPane.add(displacedThresholdText, 0, 5);
        Text RESAText = new Text("RESA:");
        RESAText.setFill(theme.getOutlineColor());
        filtersGridPane.add(RESAText, 0, 6);
        Text obstacleText = new Text("obstacle:");
        obstacleText.setFill(theme.getOutlineColor());
        filtersGridPane.add(obstacleText, 0, 7);

        filtersGridPane.add(oldtoda, 1, 1);
        filtersGridPane.add(oldtora, 1, 2);
        filtersGridPane.add(oldlda, 1, 3);
        filtersGridPane.add(oldasda, 1, 4);
        filtersGridPane.add(displacedThresholdLabel, 1, 5);
        filtersGridPane.add(RESALabel, 1, 6);

        filtersGridPane.add(TODALabel, 2, 1);
        filtersGridPane.add(TORALabel, 2, 2);
        filtersGridPane.add(LDALabel, 2, 3);
        filtersGridPane.add(ASDALabel, 2, 4);

        filtersGridPane.add(TODABox, 3, 1);
        filtersGridPane.add(TORABox, 3, 2);
        filtersGridPane.add(LDABox, 3, 3);
        filtersGridPane.add(ASDABox, 3, 4);
        filtersGridPane.add(displacedThresholdBox, 3, 5);
        filtersGridPane.add(RESABox, 3, 6);
        filtersGridPane.add(obstacleBox, 3, 7);

        Button compass = new Button("Match Bearing Rotation");
        compass.setOnAction((event) -> {
            drawRotated(affectedRunway);
        });

        filtersGridPane.add(compass, 0, 8, 2, 1);
        
        Button resetView = new Button("Reset View");
        resetView.setOnAction((event) -> {
            draw();
        });

        filtersGridPane.add(resetView, 2, 8, 2, 1);

        Button export = new Button("Export");
        export.setOnAction((actionEvent -> {
            draw();
            saveCanvasImage();
        }));

        filtersGridPane.add(export, 3, 8, 1, 1);

        Button showCalc = new Button("View Calculations");
        showCalc.setOnAction((event) -> {
            Stage stage2 = new Stage();
            GridPane calcs = affectedRunway.getCalculationDisplay();
            Scene scene2 = new Scene(calcs, 720, 300);
            stage2.setScene(scene2);
            stage2.show();
        });

        filtersGridPane.add(showCalc, 1, 8, 2, 1);


        //        //only for styling purposes
        filtersGridPaneContainer = new HBox();
        filtersGridPaneContainer.getChildren().add(filtersGridPane);
        filtersGridPaneContainer.setMargin(filtersGridPane, new Insets(10.0, 10.0, 10.0, 10.0));
        filtersGridPaneContainer.setBackground(new Background(new BackgroundFill(theme.getHUDColor(), CornerRadii.EMPTY, Insets.EMPTY)));

        runwayDisplayAnchor.setTopAnchor(filtersGridPane, 0.0);
        runwayDisplayAnchor.setRightAnchor(filtersGridPaneContainer, 0.0);

        if (affectedRunway == null || affectedRunway == placeHolderRunway) {
            //----------VIEW SELECT----------
            //changing views
            viewSelect = new ChoiceBox<String>();
            viewSelect.getItems().addAll("Top View", "Side View", new Separator(), "Split View");
            viewSelect.setValue("Top View");
            viewSelect.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
            runwayDisplayAnchor.setTopAnchor(viewSelect, 0.0);
            runwayDisplayAnchor.setLeftAnchor(viewSelect, 0.0);
            viewSelect.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){
                @Override
                public void changed(ObservableValue ov, Number oldSelected, Number newSelected) {
                    //required to avoid duplicates
                    runwayDisplayAnchor.getChildren().clear();
                    switch (newSelected.intValue()) {
                        case 0: runwayDisplayAnchor.getChildren().addAll(topView, viewSelect, filtersGridPaneContainer);
                            currentView = topViewCanvas;
                            break;
                        case 1: runwayDisplayAnchor.getChildren().addAll(sideView, viewSelect, filtersGridPaneContainer);
                            currentView = sideViewCanvas;
                            break;
                        case 3: splitView.getItems().clear();
                        	setupSplitView();
                        	runwayDisplayAnchor.setTopAnchor(splitView, 0.0);
                        	runwayDisplayAnchor.setBottomAnchor(splitView, 0.0);
                        	runwayDisplayAnchor.setLeftAnchor(splitView, 0.0);
                        	runwayDisplayAnchor.setRightAnchor(splitView, 0.0);
                            splitView.getItems().addAll(topView, sideView);
                            currentView = topViewCanvas;
                            runwayDisplayAnchor.getChildren().addAll(splitView, viewSelect, filtersGridPaneContainer);
                            break;
                    }
                }
            });
        }
    }

    public void drawTopView(int canvasWidth, int canvasHeight, boolean rotate) {

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
            runwayDisplayAnchor.toBack();
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
                        this.runwayDisplayAnchor.toBack();
                        e.consume(); // prevents ScrollEvent from reaching ScrollPane
                    }
                });

        //-------DRAW CANVAS-------

        posMargin = 0;
        negMargin = 0;

        //main square
        topGc.setFill(theme.getGrassColor());
        topGc.fillRect(0, 0, canvasWidth, canvasHeight);

        //cleared area
        topGc.translate(0, canvasHeight / 2);

        topGc.setFill(theme.getClearedAreaColor());

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

        topGc.setFill(createHatch(theme.getWarningColor(), theme.getRoadColor()));

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
        topGc.setFill(theme.getRoadColor());
        topGc.fillRect(posMargin, -20, runwayPL, 40);

        if(affectedRunway != null) {
            topGc.setFill(theme.getStripeColor());
            topGc.rotate(90);
            topGc.setTextAlign(TextAlignment.CENTER);
            topGc.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 20));
            topGc.fillText(affectedRunway.getOriginalRunway().getName(), 0, -100);
            topGc.rotate(-90);
            topGc.setFont(new Font(12));
        }

        //threshold
//        for (int i = 0; i < 3; i ++) {
//            topGc.setFill(stripeColor);
//            topGc.fillRect(posMargin + THRESHOLD_MARGIN, -15 + (i * 12), THRESHOLD_LENGTH, 6);
//            topGc.fillRect(canvasWidth - negMargin - THRESHOLD_LENGTH - THRESHOLD_MARGIN, -15 + (i * 12), THRESHOLD_LENGTH, 6);
//        }

        topGc.setStroke(theme.getStripeColor());

        //centerline
        topGc.setLineWidth(3.0);
        topGc.setLineDashes(6, 8, 1, 8);
        topGc.strokeLine(posMargin + CENTERLINE_MARGIN, 0, canvasWidth - negMargin - CENTERLINE_MARGIN, 0);

        //outline
        topGc.translate(0, -canvasHeight / 2);
        topGc.setStroke(theme.getOutlineColor());
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
            runwayDisplayAnchor.toBack();
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
                        this.runwayDisplayAnchor.toBack();
                        e.consume(); // prevents ScrollEvent from reaching ScrollPane
                    }
                });

        //-------DRAW CANVAS-------

        posMargin = 0;
        negMargin = 0;

        sideGc.translate(0, (canvasHeight - SIDE_VIEW_THICKNESS) / 2);

        //cleared area
        sideGc.setFill(theme.getClearedAreaColor());
        sideGc.fillRect(0, 0, canvasWidth, SIDE_VIEW_THICKNESS);

        //draws RESA if the original runway has one(simply leaves a blank space)
        if (hasRESA()) {
            posMargin += RESA_MARGIN;
        }

        //draws clearway if original runway has one(simply leaves a blank space)
        if (hasClearway()) {
            negMargin += CLEARWAY_MARGIN;
        }

        sideGc.setFill(createHatch(theme.getWarningColor(), theme.getRoadColor()));

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
        sideGc.setFill(theme.getRoadColor());
        sideGc.fillRect(posMargin, 0, runwayPL, 20);

        //threshold
        sideGc.setFill(theme.getStripeColor());
        sideGc.fillRect(posMargin + THRESHOLD_MARGIN, 0, THRESHOLD_LENGTH, 8);
        sideGc.fillRect(canvasWidth - negMargin - THRESHOLD_LENGTH - THRESHOLD_MARGIN, 0, THRESHOLD_LENGTH, 8);

        //centerline
        sideGc.setStroke(theme.getStripeColor());
        sideGc.setLineWidth(4.0);
        sideGc.setLineDashes(8, 10, 2, 10);
        sideGc.strokeLine(posMargin + CENTERLINE_MARGIN, 2, canvasWidth - negMargin - CENTERLINE_MARGIN, 2);
        sideGc.strokeLine(posMargin + CENTERLINE_MARGIN, 4, canvasWidth - negMargin - CENTERLINE_MARGIN, 4);

        //outline
        sideGc.setStroke(theme.getOutlineColor());
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
        splitView.setBackground(new Background(new BackgroundFill(theme.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));
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

    public void changeTheme(Theme theme) {
        this.theme = theme;
        draw();
    }

    //important boolean functions
    boolean hasClearway() { return (affectedRunway.getOriginalRunway().getClearway() != 0); }

    boolean hasStopway() { return (affectedRunway.getOriginalRunway().getStopway() != 0); }

    boolean hasBlastAllowance() { return (affectedRunway.getOriginalRunway().getBlastAllowance() != 0); }

    boolean hasRESA() { return (affectedRunway.getOriginalRunway().getRESA() != 0); }
}
