public class Obstruction {
    private String name;
    private int height, length, distanceFromCentre, distanceFromThreshold;

    Obstruction(String name, int height, int length, int distThresh, int distCentre) {
        this.name = name;
        this.height = height;
        this.length = length;
        this.distanceFromThreshold = distThresh;
        this.distanceFromCentre = distCentre;
    }

    @Override
    public String toString() { 
        return name;
    } 

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
