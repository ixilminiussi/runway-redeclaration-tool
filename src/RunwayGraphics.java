import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.canvas.*;

public class RunwayGraphics {
    
    //makes shape for runway topView depending on shape, will change to take in more parameters
    public static GraphicsContext getRunwayShape(GraphicsContext gc) {

        double xOffset = gc.getCanvas().getWidth() - 485;
        double yOffset = (gc.getCanvas().getHeight() - 180) / 2;

        gc.setStroke(Color.BLACK);
        gc.setFill(Color.DARKSEAGREEN);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gc.strokeRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        gc.translate(0, yOffset);

        gc.setFill(Color.DODGERBLUE);

        gc.beginPath();
        gc.moveTo(0, 30);
        gc.lineTo(85, 30);
        gc.lineTo(120, 0);
        gc.lineTo(360 + xOffset, 0);
        gc.lineTo(400 + xOffset, 30);
        gc.lineTo(485 + xOffset, 30);
        gc.lineTo(485 + xOffset, 150);
        gc.lineTo(400 + xOffset, 150); 
        gc.lineTo(360 + xOffset, 180); 
        gc.lineTo(120, 180);
        gc.lineTo(85, 150); 
        gc.lineTo(0, 150);
        gc.fill();
        gc.closePath();

        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(40, 70, 405 + xOffset, 40);
        gc.setFill(Color.WHITE);
        gc.fillRect(60, 75, 30, 30);
        gc.fillRect(395 + xOffset, 75, 30, 30);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(3.0);
        gc.setLineDashes(6, 5, 1, 5);
        gc.strokeLine(130, 90, 350 + xOffset, 90);

        return gc;
    }

    //make shape for runwaySideView, will also change to incorporate more parameters
    public static GraphicsContext getRunwaySlice(GraphicsContext gc) {

        double xOffset = gc.getCanvas().getWidth() - 485;
        double yOffset = (gc.getCanvas().getHeight() - 20) / 2;

        gc.translate(0, yOffset);

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.setLineDashes(1);
        gc.strokeRect(0, 0, gc.getCanvas().getWidth(), 20);

        gc.setFill(Color.DODGERBLUE);
        gc.fillRect(0, 0, 485 + xOffset, 20);

        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(40, 0, 405 + xOffset, 10);

        gc.setFill(Color.WHITE);
        gc.fillRect(60, 0, 30, 2);
        gc.fillRect(395 + xOffset, 0, 30, 2);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2.0);
        gc.setLineDashes(6, 5, 1, 5);
        gc.strokeLine(130, 1, 350 + xOffset, 1);

        return gc;
    }
    
    public static AnchorPane getAnchorPane() {

        AnchorPane runwayDisplayAnchor = new AnchorPane();

        //top view visuals (yet to add measurements

        Canvas topViewCanvas = new Canvas (800,300);
        StackPane topView = new StackPane();
        topView.setAlignment(Pos.CENTER);
        topView.getChildren().add(topViewCanvas);
        GraphicsContext topGc = topViewCanvas.getGraphicsContext2D();

        topGc = getRunwayShape(topGc);

        runwayDisplayAnchor.setTopAnchor(topView, 0.0);
        runwayDisplayAnchor.setBottomAnchor(topView, 0.0);
        runwayDisplayAnchor.setLeftAnchor(topView, 0.0);
        runwayDisplayAnchor.setRightAnchor(topView, 0.0);

        //side view visuals (yet to add measurements/VERY not done)
        Canvas sideViewCanvas = new Canvas (800,400);
        StackPane sideView = new StackPane();
        sideView.setAlignment(Pos.CENTER);
        sideView.getChildren().add(sideViewCanvas);
        GraphicsContext sideGc = sideViewCanvas.getGraphicsContext2D();

        sideGc = getRunwaySlice(sideGc);

        runwayDisplayAnchor.setTopAnchor(sideView, 0.0);
        runwayDisplayAnchor.setBottomAnchor(sideView, 0.0);
        runwayDisplayAnchor.setLeftAnchor(sideView, 0.0);
        runwayDisplayAnchor.setRightAnchor(sideView, 0.0);

        //split view
        SplitPane splitView = new SplitPane();
        splitView.setOrientation(Orientation.VERTICAL);
        splitView.setBackground(new Background(new BackgroundFill(Color.LIGHTCYAN, CornerRadii.EMPTY, Insets.EMPTY)));

        runwayDisplayAnchor.setTopAnchor(splitView, 0.0);
        runwayDisplayAnchor.setBottomAnchor(splitView, 0.0);
        runwayDisplayAnchor.setLeftAnchor(splitView, 0.0);
        runwayDisplayAnchor.setRightAnchor(splitView, 0.0);

        //changing views
        ChoiceBox viewSelect = new ChoiceBox();
        viewSelect.getItems().addAll("Top View", "Side View", new Separator(), "Split View");
        viewSelect.setValue("Top View");
        viewSelect.setBackground(new Background(new BackgroundFill(Color.CRIMSON, CornerRadii.EMPTY, Insets.EMPTY)));
        runwayDisplayAnchor.setTopAnchor(viewSelect, 0.0);
        runwayDisplayAnchor.setLeftAnchor(viewSelect, 0.0);
        runwayDisplayAnchor.getChildren().addAll(topView, viewSelect);
        viewSelect.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue ov, Number oldSelected, Number newSelected) {
                runwayDisplayAnchor.getChildren().clear();
                switch (newSelected.intValue()) {
                    case 0: runwayDisplayAnchor.getChildren().addAll(topView, viewSelect);
                            System.out.println("1");
                            break;
                    case 1: runwayDisplayAnchor.getChildren().addAll(sideView, viewSelect);
                            System.out.println("2");
                            break;
                    case 3: splitView.getItems().clear();
                            splitView.getItems().addAll(topView, sideView);
                            runwayDisplayAnchor.getChildren().addAll(splitView, viewSelect);
                            System.out.println("3");
                            break;
                }
            }
        });

        runwayDisplayAnchor.setBorder(new Border(new BorderStroke(Color.CRIMSON, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        runwayDisplayAnchor.setBackground(new Background(new BackgroundFill(Color.LIGHTCYAN, CornerRadii.EMPTY, Insets.EMPTY)));

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


        sideView.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        sideView.setOnScroll(
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

        topViewCanvas.setOnMouseDragged(e -> {
            //topViewCanvas.setTranslateX(e.getX() + 400);
            topViewCanvas.setTranslateX(e.getX() + 400);
            e.consume();
        });

        sideViewCanvas.setOnMouseDragged(e -> {
            sideViewCanvas.setTranslateX(e.getX() + 400);
            e.consume();
        });

        return runwayDisplayAnchor;
    }

    public RunwayGraphics() {
    }
}
