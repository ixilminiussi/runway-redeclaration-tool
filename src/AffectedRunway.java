import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class AffectedRunway {
    private int TORA, TODA, ASDA, LDA;
    private Runway originalRunway;
    private Obstruction obstruction;
    private Boolean unchanged;

    AffectedRunway(Runway originalRunway, Obstruction obstruction) {
        this.originalRunway = originalRunway;
        System.out.println("Affected Runway: " + this.originalRunway);
        this.obstruction = obstruction;
        recalculate(obstruction);
    }

    public void recalculate (Obstruction obstruction) {

        // replace obstruction and change all values but original runway is the same
        // landing over / take off away
        this.obstruction = obstruction;
        if(obstruction.getDistanceFromCentre() > 75 || obstruction.getDistanceFromThreshold() > originalRunway.getTODA() + 60 || obstruction.getDistanceFromThreshold() < -60) {
            TORA = originalRunway.getTORA();
            ASDA = originalRunway.getASDA();
            TODA = originalRunway.getTODA();
            LDA = originalRunway.getLDA();
            unchanged = true;
        } else {
            unchanged = false;
            if (obstruction.getDistanceFromThreshold() < (originalRunway.getTORA() + originalRunway.getDisplacedThreshold()) / 2) {
                TORA = Math.min(originalRunway.getTORA() - originalRunway.getBlastAllowance() - obstruction.getDistanceFromThreshold() - originalRunway.getDisplacedThreshold(),
                        originalRunway.getTORA() - originalRunway.getStripEnd() - originalRunway.getRESA() - obstruction.getDistanceFromThreshold());
                ASDA = TORA + originalRunway.getStopway();
                TODA = TORA + originalRunway.getClearway();
                LDA = originalRunway.getLDA() - obstruction.getDistanceFromThreshold() - originalRunway.getStripEnd() - (obstruction.getHeight() * 50);

            }
            //landing towards, take off towards
            else {
                TORA = obstruction.getDistanceFromThreshold() + originalRunway.getDisplacedThreshold() - (obstruction.getHeight() * originalRunway.getSlopeRatio()) - originalRunway.getStripEnd();
                ASDA = TORA;
                TODA = TORA;
                LDA = obstruction.getDistanceFromThreshold() - originalRunway.getRESA() - originalRunway.getStripEnd();
                System.out.println("NEW TORA: " + originalRunway.getTORA());
            }
        }
    }

    public GridPane getCalculationDisplay(){
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        //gridPane.setMinWidth(1650);
        gridPane.setPadding(new Insets(10));


        ColumnConstraints col0 = new ColumnConstraints();
        col0.setPrefWidth(40);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPrefWidth(20);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPrefWidth(150);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPrefWidth(20);
        ColumnConstraints col4 = new ColumnConstraints();
        col4.setPrefWidth(150);
        ColumnConstraints col5 = new ColumnConstraints();
        col5.setPrefWidth(20);
        ColumnConstraints col6 = new ColumnConstraints();
        col6.setPrefWidth(150);
        ColumnConstraints col7 = new ColumnConstraints();
        col7.setPrefWidth(20);
        ColumnConstraints col8 = new ColumnConstraints();
        col8.setPrefWidth(150);

        gridPane.getColumnConstraints().addAll(col0,col1,col2,col3,col4,col5,col6,col7,col8);

        if (obstruction.getDistanceFromThreshold() < (originalRunway.getTORA() + originalRunway.getDisplacedThreshold())/2) {
            if (originalRunway.getTORA() - originalRunway.getBlastAllowance() - obstruction.getDistanceFromThreshold() - originalRunway.getDisplacedThreshold() <=originalRunway.getTORA() - originalRunway.getStripEnd() - originalRunway.getRESA() - obstruction.getDistanceFromThreshold()) {
                gridPane.add(new Label("TORA"), 0, 0);
                gridPane.add(new Label("="), 1, 0);
                gridPane.add(new Label("Original TORA"), 2, 0);
                gridPane.add(new Label("-"), 3, 0);
                gridPane.add(new Label("Blast Protection"), 4, 0);
                gridPane.add(new Label("-"), 5, 0);
                gridPane.add(new Label("Distance from Threshold"), 6, 0);
                gridPane.add(new Label("-"), 7, 0);
                gridPane.add(new Label("Displaced Threshold"), 8, 0);

                gridPane.add(new Label("="), 1, 1);
                gridPane.add(new Label(Integer.toString(originalRunway.getTORA())), 2, 1);
                gridPane.add(new Label("-"), 3, 1);
                gridPane.add(new Label(Integer.toString(originalRunway.getBlastAllowance())), 4, 1);
                gridPane.add(new Label("-"), 5, 1);
                gridPane.add(new Label(Integer.toString(obstruction.getDistanceFromThreshold())), 6, 1);
                gridPane.add(new Label("-"), 7, 1);
                gridPane.add(new Label(Integer.toString(originalRunway.getDisplacedThreshold())), 8, 1);

                gridPane.add(new Label("="), 1, 2);
                gridPane.add(new Label(Integer.toString(TORA)), 2, 2);
            }
            else{
                gridPane.add(new Label("TORA"), 0, 0);
                gridPane.add(new Label("="), 1, 0);
                gridPane.add(new Label("Original TORA"), 2, 0);
                gridPane.add(new Label("-"), 3, 0);
                gridPane.add(new Label("Strip End"), 4, 0);
                gridPane.add(new Label("-"), 5, 0);
                gridPane.add(new Label("RESA"), 6, 0);
                gridPane.add(new Label("-"), 7, 0);
                gridPane.add(new Label("Displaced Threshold"), 8, 0);

                gridPane.add(new Label("="), 1, 1);
                gridPane.add(new Label(Integer.toString(originalRunway.getTORA())), 2, 1);
                gridPane.add(new Label("-"), 3, 1);
                gridPane.add(new Label(Integer.toString(originalRunway.getStripEnd())), 4, 1);
                gridPane.add(new Label("-"), 5, 1);
                gridPane.add(new Label(Integer.toString(originalRunway.getRESA())), 6, 1);
                gridPane.add(new Label("-"), 7, 1);
                gridPane.add(new Label(Integer.toString(obstruction.getDistanceFromThreshold())), 8, 1);

                gridPane.add(new Label("="), 1, 2);
                gridPane.add(new Label(Integer.toString(TORA)), 2, 2);
            }


                gridPane.add(new Label("ASDA"), 0, 3);
                gridPane.add(new Label("="), 1, 3);
                gridPane.add(new Label("(R) TORA"), 2, 3);
                gridPane.add(new Label("+"), 3, 3);
                gridPane.add(new Label("Stopway"), 4, 3);

                gridPane.add(new Label("="), 1, 4);
                gridPane.add(new Label(Integer.toString(ASDA)),2,4);


                gridPane.add(new Label("TODA"), 0, 5);
                gridPane.add(new Label("="), 1, 5);
                gridPane.add(new Label("(R) TORA"), 2, 5);
                gridPane.add(new Label("+"), 3, 5);
                gridPane.add(new Label("Clearway"), 4, 5);

                gridPane.add(new Label("="), 1, 6);
                gridPane.add(new Label(Integer.toString(TODA)),2,6);


                gridPane.add(new Label("LDA"), 0, 7);
                gridPane.add(new Label("="), 1, 7);
                gridPane.add(new Label("Original LDA"), 2, 7);
                gridPane.add(new Label("-"), 3, 7);
                gridPane.add(new Label("Distance from Threshold"), 4, 7);
                gridPane.add(new Label("-"), 5, 7);
                gridPane.add(new Label("Strip End"), 6, 7);
                gridPane.add(new Label("-"), 7, 7);
                gridPane.add(new Label("Slope Calculation"), 8, 7);

                gridPane.add(new Label("="), 1, 8);
                gridPane.add(new Label(Integer.toString(originalRunway.getLDA())), 2, 8);
                gridPane.add(new Label("-"), 3, 8);
                gridPane.add(new Label(Integer.toString(obstruction.getDistanceFromThreshold())), 4, 8);
                gridPane.add(new Label("-"), 5, 8);
                gridPane.add(new Label(Integer.toString(originalRunway.getStripEnd())), 6, 8);
                gridPane.add(new Label("-"), 7, 8);
                gridPane.add(new Label(obstruction.getHeight() + "*" + originalRunway.getSlopeRatio()), 8, 8);

                gridPane.add(new Label("="), 1, 9);
                gridPane.add(new Label(Integer.toString(LDA)),2,9);
            }
        else{
            gridPane.add(new Label("TORA"), 0, 10);
            gridPane.add(new Label("="), 1, 10);
            gridPane.add(new Label("Distance from Threshold"), 2, 10);
            gridPane.add(new Label("+"), 3, 10);
            gridPane.add(new Label("Displaced Threshold"), 4, 10);
            gridPane.add(new Label("-"), 5, 10);
            gridPane.add(new Label("Slope Calculation"), 6, 10);
            gridPane.add(new Label("-"), 7, 10);
            gridPane.add(new Label("Strip End"), 8, 10);

            gridPane.add(new Label("="), 1, 11);
            gridPane.add(new Label(Integer.toString(obstruction.getDistanceFromThreshold())), 2, 11);
            gridPane.add(new Label("+"), 3, 11);
            gridPane.add(new Label(Integer.toString(originalRunway.getDisplacedThreshold())), 4, 11);
            gridPane.add(new Label("-"), 5, 11);
            gridPane.add(new Label(obstruction.getHeight() + "*" + originalRunway.getSlopeRatio()), 6, 11);
            gridPane.add(new Label("-"), 7, 11);
            gridPane.add(new Label(Integer.toString(originalRunway.getStripEnd())), 8, 11);

            gridPane.add(new Label("="), 1, 12);
            gridPane.add(new Label(Integer.toString(TORA)), 2, 12);


            gridPane.add(new Label("ASDA"), 0, 13);
            gridPane.add(new Label("="), 1, 13);
            gridPane.add(new Label("(R) TORA"), 2, 13);

            gridPane.add(new Label("="), 1, 14);
            gridPane.add(new Label(Integer.toString(ASDA)),2,14);


            gridPane.add(new Label("TODA"), 0, 15);
            gridPane.add(new Label("="), 1, 15);
            gridPane.add(new Label("(R) TORA"), 2, 15);

            gridPane.add(new Label("="), 1, 16);
            gridPane.add(new Label(Integer.toString(TODA)),2,16);


            gridPane.add(new Label("LDA"), 0, 17);
            gridPane.add(new Label("="), 1, 17);
            gridPane.add(new Label("Distance from Threshold"), 2, 17);
            gridPane.add(new Label("-"), 3, 17);
            gridPane.add(new Label("RESA"), 4, 17);
            gridPane.add(new Label("-"), 5, 17);
            gridPane.add(new Label("Strip End"), 6, 17);

            gridPane.add(new Label("="), 1, 18);
            gridPane.add(new Label(Integer.toString(obstruction.getDistanceFromThreshold())), 2, 18);
            gridPane.add(new Label("-"), 3, 18);
            gridPane.add(new Label(Integer.toString(originalRunway.getRESA())), 4, 18);
            gridPane.add(new Label("-"), 5, 18);
            gridPane.add(new Label(Integer.toString(originalRunway.getStripEnd())), 6, 18);
        }

        return gridPane;
    }



    public int getTORA() {
        return TORA;
    }

    public void setTORA(int TORA) {
        this.TORA = TORA;
    }

    public int getTODA() {
        return TODA;
    }

    public void setTODA(int TODA) {
        this.TODA = TODA;
    }

    public int getASDA() {
        return ASDA;
    }

    public void setASDA(int ASDA) {
        this.ASDA = ASDA;
    }

    public int getLDA() {
        return LDA;
    }

    public void setLDA(int LDA) {
        this.LDA = LDA;
    }

    public boolean isUnchanged() {
        return unchanged;
    }

    public Runway getOriginalRunway() {
        return originalRunway;
    }

    public void setOriginalRunway(Runway originalRunway) {
        this.originalRunway = originalRunway;
    }

    public Obstruction getObstruction() {
        return obstruction;
    }

    public void setObstruction(Obstruction obstruction) {
        this.obstruction = obstruction;
    }
}
