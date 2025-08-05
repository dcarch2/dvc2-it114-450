package M5.Part5;  
// PointsPayload file to fulfill part 1 requirement - dvc2 8/4/2025
public class PointsPayload extends Payload {
    private int points;

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return super.toString() +
               String.format(" Points: [%s]", getPoints());
    }
}