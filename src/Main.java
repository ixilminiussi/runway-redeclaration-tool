import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main extends Application {

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
        row1.setPercentHeight(100);
        main.getRowConstraints().add(row1);

        StackPane fillerImage = new StackPane();
        Image image = new Image("img/runway.jpg", main.getWidth(), main.getHeight(), false, true, true);
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        fillerImage.getChildren().add(imageView);
        fillerImage.setPadding(new Insets(20, 20, 20, 20));
        fillerImage.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));


        main.add(fillerImage, 0, 0, 2, 1);

        root.getChildren().add(main);
        VBox sideBar = new VBox();
        Pane test1 = new Pane();
        test1.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        Pane test2 = new Pane();
        test2.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        sideBar.getChildren().addAll(test1, test2);
        VBox.setVgrow(test1, Priority.ALWAYS);
        VBox.setVgrow(test2, Priority.ALWAYS);
        VBox.setVgrow(main, Priority.ALWAYS);

        main.add(sideBar, 2, 0);

        stage.setScene(new Scene(root, 1000, 600));
        stage.setMaximized(true);
        stage.show();
    }
}