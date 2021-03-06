public class AffectedRunway {
    int TORA;
    int TODA;
    int ASDA;
    int LDA;
    Runway originalRunway;
    Obstruction obstruction;

    public AffectedRunway(Runway originalRunway, Obstruction obstruction) {
        this.originalRunway = originalRunway;
        this.obstruction = obstruction;
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
            LDA = originalRunway.getLDA() - obstruction.getDistanceFromThreshold() - originalRunway.getStripEnd() - (obstruction.getHeight() * originalRunway.getSlopeRatio());
        }
        //landing towards, take off towards
        else{
            TORA = obstruction.getDistanceFromThreshold() + originalRunway.getDisplacedThreshold() - (obstruction.getHeight() * originalRunway.getSlopeRatio()) - originalRunway.getStripEnd();
            ASDA = TORA;
            TODA = TORA;
            LDA = obstruction.getDistanceFromThreshold() - originalRunway.getRESA() - originalRunway.getStripEnd();
        }
    }

}
