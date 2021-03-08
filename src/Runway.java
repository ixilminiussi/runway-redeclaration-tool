public class Runway {
    private String name, airport;
    private int TORA, TODA, ASDA, LDA, displacedThreshold, stripEnd, EGR, RESA, blastAllowance, stopway, clearway, slopeRatio;
    private Directions direction;

    public AffectedRunway recalculate(Obstruction obst) {
        return new AffectedRunway(this, obst);
    }

    public Runway(String name, String airport, int TORA, int TODA, int ASDA, int LDA, int displacedThreshold, int stripEnd, int EGR, int RESA, int blastAllowance, int stopway, int clearway, Directions direction) {
        this.name = name;
        this.airport = airport;
        this.TORA = TORA;
        this.TODA = TODA;
        this.ASDA = ASDA;
        this.LDA = LDA;
        this.displacedThreshold = displacedThreshold;
        this.stripEnd = stripEnd;
        this.EGR = EGR;
        this.RESA = RESA;
        this.blastAllowance = blastAllowance;
        this.stopway = stopway;
        this.clearway = clearway;
        this.direction = direction;
    }

    @Override
    public String toString() { 
        return name + " - " + airport + ": " + direction;
    } 

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
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

    public int getDisplacedThreshold() {
        return displacedThreshold;
    }

    public void setDisplacedThreshold(int displacedThreshold) {
        this.displacedThreshold = displacedThreshold;
    }

    public int getStripEnd() {
        return stripEnd;
    }

    public void setStripEnd(int stripEnd) {
        this.stripEnd = stripEnd;
    }

    public int getEGR() {
        return EGR;
    }

    public void setEGR(int EGR) {
        this.EGR = EGR;
    }

    public int getRESA() {
        return RESA;
    }

    public void setRESA(int RESA) {
        this.RESA = RESA;
    }

    public int getBlastAllowance() {
        return blastAllowance;
    }

    public void setBlastAllowance(int blastAllowance) {
        this.blastAllowance = blastAllowance;
    }

    public int getStopway() {
        return stopway;
    }

    public void setStopway(int stopway) {
        this.stopway = stopway;
    }

    public int getClearway() {
        return clearway;
    }

    public void setClearway(int clearway) {
        this.clearway = clearway;
    }

    public int getSlopeRatio() {
        return slopeRatio;
    }

    public void setSlopeRatio(int slopeRatio) {
        this.slopeRatio = slopeRatio;
    }

    public Directions getDirection() {
        return direction;
    }

    public void setDirection(Directions direction) {
        this.direction = direction;
    }
}

