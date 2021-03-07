public class Obstruction {
    String name;
    int height;
    int length;

    Obstruction(String name, int height, int length, int distThresh, int distCentre) {
        this.name = name;
        this.height = height;
        this.length = length;
        this.distanceFromThreshold = distThresh;
        this.distanceFromCentre = distCentre;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getDistanceFromCentre() {
        return distanceFromCentre;
    }

    public void setDistanceFromCentre(int distanceFromCentre) {
        this.distanceFromCentre = distanceFromCentre;
    }

    public int getDistanceFromThreshold() {
        return distanceFromThreshold;
    }

    public void setDistanceFromThreshold(int distanceFromThreshold) {
        this.distanceFromThreshold = distanceFromThreshold;
    }

    int distanceFromCentre;
    int distanceFromThreshold;

}
