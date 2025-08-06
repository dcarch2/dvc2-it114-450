package Project.Common;

import java.io.Serializable;

public class PointsPayload extends Payload implements Serializable {
    private int points;

    public PointsPayload(long clientId, int points) {
        this.setPayloadType(PayloadType.POINTS);
        this.setClientId(clientId);
        this.setPoints(points);
    }

    public int getPoints() {
        return this.points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" Points: [%s]", this.getPoints());
    }
}