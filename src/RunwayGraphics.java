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

public class RunwayGraphics {

    //where the graphics happen
    AnchorPane runwayDisplayAnchor;
    StackPane topView, sideView;
    Canvas topViewCanvas, sideViewCanvas;
    GraphicsContext topGc, sideGc;
    SplitPane splitView;

    //buttons
    VBox filtersBox;
    CheckBox displacedThresholdBox;
    HBox filtersBoxContainer;
    ChoiceBox viewSelect;

    //used as reference when applying real life distances to the drawing
    double runwayPixelLength;
    AffectedRunway affectedRunway;

    //color palette
    Background background, hudElement;


    public AnchorPane getRunwayGraphics() {

        return runwayDisplayAnchor;
    }

    public void setAffectedRunway(AffectedRunway affectedRunway) {

        this.affectedRunway = affectedRunway;
    }


    public RunwayGraphics(AffectedRunway affectedRunway) {

        this.affectedRunway = affectedRunway;
        runwayDisplayAnchor = new AnchorPane();

        //color palette
        background = new Background(new BackgroundFill(Color.LIGHTCYAN, CornerRadii.EMPTY, Insets.EMPTY));
        hudElement = new Background(new BackgroundFill(Color.LIGHTSTEELBLUE, CornerRadii.EMPTY, Insets.EMPTY));

        setupSplitView();
        setupButtons();
        draw();

        //where the graphics happen
        runwayDisplayAnchor.setBorder(new Border(new BorderStroke(Color.LIGHTSTEELBLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        runwayDisplayAnchor.setBackground(background);
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
            case 0: runwayDisplayAnchor.getChildren().addAll(topView, viewSelect, filtersBoxContainer);
                    break;
            case 1: runwayDisplayAnchor.getChildren().addAll(sideView, viewSelect, filtersBoxContainer);
                    break;
            case 3: splitView.getItems().clear();
                    splitView.getItems().addAll(topView, sideView);
                    runwayDisplayAnchor.getChildren().addAll(splitView, viewSelect, filtersBoxContainer);
                    break;
        }

        if (displacedThresholdBox.isSelected()) {

            showDisplacedThreshold();
        }
    }

    public void showDisplacedThreshold() {
        double displacedThresholdLength = 200;

        topGc = drawMeasurement(topGc, String.valueOf(displacedThresholdLength), 60, 20, getLengthRelativeToRunway(displacedThresholdLength), Orientation.HORIZONTAL);
        sideGc = drawMeasurement(sideGc, String.valueOf(displacedThresholdLength), 60, 0, getLengthRelativeToRunway(displacedThresholdLength), Orientation.HORIZONTAL);
    }
    
    //draws a line between 2 points with a measure/label displayed next to it
    public GraphicsContext drawMeasurement(GraphicsContext gc, String label, double x, double y, double length, Orientation o) {

        gc.translate(0, gc.getCanvas().getHeight()/2);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1.0);

        double delimiter = 5;

        switch (o) {
            case HORIZONTAL :
                //main line
                gc.setLineDashes(5,5);
                gc.strokeLine(x, y, x + length, y);
                //side lines
                gc.setLineDashes(1);
                gc.strokeLine(x, y-delimiter, x, y+delimiter);
                gc.strokeLine(x + length, y-delimiter, x + length, y+delimiter);

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
                gc.strokeLine(x-delimiter, y, x+delimiter, y);
                gc.strokeLine(x-delimiter, y + length, x+delimiter, y + length);

                gc.setTextBaseline(VPos.CENTER);
                gc.setTextAlign(TextAlignment.LEFT);
                gc.strokeText(label, x + 4, y + (length/2));
            break;
        }

        return gc;
    }

    private double getLengthRelativeToRunway(double meters) {

        //simple rule of 3
        return ((meters * runwayPixelLength) / 1000);
    }

    public void setupButtons() {

        //---------FILTERS----------
        //checkboxes to add measurements, currently for testing, eventually to give control to the user as to which information he wants to see/hide
        filtersBox = new VBox();
        displacedThresholdBox = new CheckBox(" displaced threshold ");
        filtersBox.getChildren().addAll(displacedThresholdBox);
        filtersBox.setSpacing(5.0);

        displacedThresholdBox.selectedProperty().addListener(
            (ObservableValue<? extends Boolean> ov, Boolean oldVal, Boolean newVal) -> {
                if (newVal) {
                    showDisplacedThreshold();
                } else {
                    //clears the canvas,
                    draw();
                }
            }
        );

        //only for styling purposes
        filtersBoxContainer = new HBox();
        filtersBoxContainer.getChildren().add(filtersBox);
        filtersBoxContainer.setMargin(filtersBox, new Insets(5.0, 5.0, 5.0, 5.0));
        filtersBoxContainer.setBackground(hudElement);

        runwayDisplayAnchor.setTopAnchor(filtersBoxContainer, 0.0);
        runwayDisplayAnchor.setRightAnchor(filtersBoxContainer, 0.0);

        //----------VIEW SELECT----------
        //changing views
        viewSelect = new ChoiceBox<String>();
        viewSelect.getItems().addAll("Top View", "Side View", new Separator(), "Split View");
        viewSelect.setValue("Top View");
        viewSelect.setBackground(hudElement);
        runwayDisplayAnchor.setTopAnchor(viewSelect, 0.0);
        runwayDisplayAnchor.setLeftAnchor(viewSelect, 0.0);
        viewSelect.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue ov, Number oldSelected, Number newSelected) {
                //required to avoid duplicates
                runwayDisplayAnchor.getChildren().clear();
                switch (newSelected.intValue()) {
                    case 0: runwayDisplayAnchor.getChildren().addAll(topView, viewSelect, filtersBoxContainer);
                            break;
                    case 1: runwayDisplayAnchor.getChildren().addAll(sideView, viewSelect, filtersBoxContainer);
                            break;
                    case 3: splitView.getItems().clear();
                            splitView.getItems().addAll(topView, sideView);
                            runwayDisplayAnchor.getChildren().addAll(splitView, viewSelect, filtersBoxContainer);
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

        //-------DRAW CANVAS-------
        double xOffset = canvasWidth - 485;
        double yOffset = (canvasHeight - 180) / 2;

        //main square
        topGc.setFill(Color.DARKSEAGREEN);
        topGc.fillRect(0, 0, canvasWidth, canvasHeight);

        //cleared area
        topGc.translate(0, yOffset);

        topGc.setFill(Color.DODGERBLUE);

        topGc.beginPath();
        topGc.moveTo(0, 30);
        topGc.lineTo(85, 30);
        topGc.lineTo(120, 0);
        topGc.lineTo(360 + xOffset, 0);
        topGc.lineTo(400 + xOffset, 30);
        topGc.lineTo(485 + xOffset, 30);
        topGc.lineTo(485 + xOffset, 150);
        topGc.lineTo(400 + xOffset, 150); 
        topGc.lineTo(360 + xOffset, 180); 
        topGc.lineTo(120, 180);
        topGc.lineTo(85, 150); 
        topGc.lineTo(0, 150);
        topGc.fill();
        topGc.closePath();

        //runway
        topGc.setFill(Color.LIGHTGRAY);
        topGc.fillRect(40, 70, 405 + xOffset, 40);
        runwayPixelLength = 405 + xOffset - 40;

        //threshold
        for (int i = 0; i < 3; i ++) {
            topGc.setFill(Color.WHITE);
            topGc.fillRect(60, 75 + (i * 12), 30, 6);
            topGc.fillRect(395 + xOffset, 75 + (i * 12), 30, 6);
            topGc.setStroke(Color.WHITE);
        }

        //centerline
        topGc.setLineWidth(3.0);
        topGc.setLineDashes(6, 6, 1, 6);
        topGc.strokeLine(130, 90, 350 + xOffset, 90);

        //contour
        topGc.translate(0, -yOffset);
        topGc.setStroke(Color.BLACK);
        topGc.setLineWidth(1);
        topGc.setLineDashes(1);
        topGc.strokeRect(0, 0, canvasWidth, canvasHeight);

        topView.setScaleX(0.8);
        topView.setScaleY(0.8);
    }

    public void drawSideView(double canvasWidth, double canvasHeight) {

        sideViewCanvas = new Canvas (800,200);
        sideGc = sideViewCanvas.getGraphicsContext2D();

        sideView = new StackPane();
        sideView.setAlignment(Pos.CENTER);
        sideView.getChildren().add(sideViewCanvas);

        //-------DRAW CANVAS-------
        double xOffset = canvasWidth - 485;
        double yOffset = (canvasHeight - 20) / 2;

        sideGc.translate(0, yOffset);

        //cleared are
        sideGc.setFill(Color.DODGERBLUE);
        sideGc.fillRect(0, 0, 485 + xOffset, 20);

        //runway
        sideGc.setFill(Color.LIGHTGRAY);
        sideGc.fillRect(40, 0, 405 + xOffset, 10);
        runwayPixelLength = 405 + xOffset - 40;

        //threshold
        sideGc.setFill(Color.WHITE);
        sideGc.fillRect(60, 0, 30, 4);
        sideGc.fillRect(395 + xOffset, 0, 30, 4);

        //centerline
        sideGc.setStroke(Color.WHITE);
        sideGc.setLineWidth(4.0);
        sideGc.setLineDashes(5, 8, 1, 8);
        sideGc.strokeLine(130, 2, 350 + xOffset, 2);

        //contour
        sideGc.setStroke(Color.BLACK);
        sideGc.setLineWidth(1);
        sideGc.setLineDashes(1);
        sideGc.strokeRect(0, 0, canvasWidth, 20);

        sideView.setScaleX(0.8);
        sideView.setScaleY(0.8);
    }

    public void setupSplitView() {

        splitView = new SplitPane();
        splitView.setOrientation(Orientation.VERTICAL);
        splitView.setBackground(background);
    }
}