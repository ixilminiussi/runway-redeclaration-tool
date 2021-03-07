import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class RunwayGraphics {
    
    public static AnchorPane getAnchorPane() {

        AnchorPane runwayDisplayAnchor = new AnchorPane();

        //top view visuals (yet to add measurements)
        Rectangle paneRoad = new Rectangle();
        paneRoad.setWidth(470);
        paneRoad.setHeight(50);
        paneRoad.setFill(Color.LIGHTGRAY);
        Path clearedField = new Path();
        MoveTo moveTo = new MoveTo(); moveTo.setX(0.0f); moveTo.setY(0.0f);
        LineTo line0 = new LineTo(); LineTo line1 = new LineTo(); LineTo line2 = new LineTo(); 
        LineTo line3 = new LineTo(); LineTo line4 = new LineTo(); LineTo line5 = new LineTo(); 
        LineTo line6 = new LineTo(); LineTo line7 = new LineTo(); LineTo line8 = new LineTo(); 
        LineTo line9 = new LineTo(); LineTo line10 = new LineTo(); LineTo line11 = new LineTo();
        line0.setX(0f); line0.setY(-55f); line1.setX(80f); line1.setY(-55f); line2.setX(150f); line2.setY(-105f);
        line3.setX(400f); line3.setY(-105f); line4.setX(470f); line4.setY(-55f); line5.setX(550f); line5.setY(-55f); 
        line6.setX(550f); line6.setY(55f); line7.setX(470f); line7.setY(55f); line8.setX(400f); line8.setY(105f); 
        line9.setX(150f); line9.setY(105f); line10.setX(80f); line10.setY(55f); line11.setX(0f); line11.setY(55f); 
        ClosePath closePath = new ClosePath();
        clearedField.getElements().addAll(moveTo, line0, line1, line2, line3, line4, line5, line6, line7, line8, line9, line10, line11, closePath);
        clearedField.setFill(Color.DODGERBLUE);
        clearedField.setStrokeWidth(0.0);
        Line line = new Line();
        line.setStartX(-180); line.setStartY(0.0);
        line.setEndX(180); line.setEndY(0);
        line.setStroke(Color.WHITE); line.setStrokeWidth(2.0);

        StackPane topView = new StackPane();
        topView.setAlignment(Pos.CENTER);
        topView.getChildren().addAll(clearedField, paneRoad, line);
        topView.setScaleX(2);
        topView.setScaleY(2);

        runwayDisplayAnchor.setTopAnchor(topView, 0.0);
        runwayDisplayAnchor.setBottomAnchor(topView, 0.0);
        runwayDisplayAnchor.setLeftAnchor(topView, 0.0);
        runwayDisplayAnchor.setRightAnchor(topView, 0.0);

        //side view visuals (yet to add measurements/VERY not done)
        Line clearedField2 = new Line();
        clearedField2.setStartX(-275); clearedField2.setStartY(0.0);
        clearedField2.setEndX(275); clearedField2.setEndY(0.0);
        clearedField2.setStroke(Color.DODGERBLUE); clearedField2.setStrokeWidth(4.0);
        Line paneRoad2 = new Line();
        paneRoad2.setStartX(-235); paneRoad2.setStartY(0);
        paneRoad2.setEndX(235); paneRoad2.setEndY(0);
        paneRoad2.setStroke(Color.LIGHTGRAY); paneRoad2.setStrokeWidth(4.0);

        StackPane sideView = new StackPane();
        sideView.setAlignment(Pos.CENTER);
        sideView.getChildren().addAll(clearedField2, paneRoad2);
        sideView.setScaleX(2);
        sideView.setScaleY(2);

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

        return runwayDisplayAnchor;
    }

    public RunwayGraphics() {
    }
}