public class AffectedRunway {
    private int TORA, TODA, ASDA, LDA;
    private Runway originalRunway;
    private Obstruction obstruction;

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


        if (obstruction.getDistanceFromThreshold() < (originalRunway.getTORA() + originalRunway.getDisplacedThreshold())/2) {
            TORA = Math.min(originalRunway.getTORA() - originalRunway.getBlastAllowance() - obstruction.getDistanceFromThreshold() - originalRunway.getDisplacedThreshold(),
                    originalRunway.getTORA() - originalRunway.getStripEnd() - originalRunway.getRESA() - obstruction.getDistanceFromThreshold());
            ASDA = TORA + originalRunway.getStopway();
            TODA = TORA + originalRunway.getClearway();
            LDA = originalRunway.getLDA() - obstruction.getDistanceFromThreshold() - originalRunway.getStripEnd() - (obstruction.getHeight() * 50);

        }
        //landing towards, take off towards
        else{
            TORA = obstruction.getDistanceFromThreshold() + originalRunway.getDisplacedThreshold() - (obstruction.getHeight() * originalRunway.getSlopeRatio()) - originalRunway.getStripEnd();
            ASDA = TORA;
            TODA = TORA;
            LDA = obstruction.getDistanceFromThreshold() - originalRunway.getRESA() - originalRunway.getStripEnd();
            System.out.println("NEW TORA: " + originalRunway.getTORA());
        }
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
