import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextFlow;

public class HistoryPanel extends VBox {

    VBox history;
    ScrollPane scroll;
    HBox currentLabel;

    HistoryPanel() {
        this.setFillWidth(true);
        this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        this.setPadding(new Insets(5));
        Label historyText = new Label("History:");
        Font defaultFont = Font.getDefault();
        historyText.setFont(Font.font(defaultFont.getFamily(), FontWeight.BOLD, 15));
        historyText.setPadding(new Insets(10));
        scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        history = new VBox();
        VBox.setVgrow(scroll, Priority.ALWAYS);
        scroll.setContent(history);
        this.getChildren().addAll(historyText, scroll);
    }

    public void addHistoryEntry(String entry) {
        if(!entry.equals("RUNWAY: ") && !entry.equals("OBSTRUCTION: ") && !entry.equals("PRESETS: ")) {
            HBox hbox = new HBox();
            hbox.setPrefWidth(scroll.getPrefWidth());
            if(currentLabel != null) {
                currentLabel.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
                currentLabel.setBorder(Border.EMPTY);
            }
            Label entryLabel = new Label(entry);
            entryLabel.setPadding(new Insets(5));
            entryLabel.setWrapText(true);
            entryLabel.setPrefWidth(scroll.getPrefWidth());
            currentLabel = hbox;
            hbox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            hbox.getChildren().add(entryLabel);
            history.getChildren().add(0, hbox);

            if(history.getChildren().size() > 30) {
                history.getChildren().remove(history.getChildren().size() - 1);
            }
        }
    }

}
