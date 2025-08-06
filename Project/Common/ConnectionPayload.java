package Project.Common;

import java.io.Serializable;

public class ConnectionPayload extends Payload implements Serializable {
    private String clientName;
    private int points; // DVC2 - 7/29/2025 - Added to support point syncing in games
    private boolean eliminated; // DVC2 - 7/29/2025 - Added to support elimination status in games

    /**
     * @return the clientName
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * @param clientName the clientName to set
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * @return the points
     */
    public int getPoints() {
        return points;
    }

    /**
     * @param points the points to set
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * @return the eliminated status
     */
    public boolean isEliminated() {
        return eliminated;
    }

    /**
     * @param eliminated the elimination status to set
     */
    public void setEliminated(boolean eliminated) {
        this.eliminated = eliminated;
    }

    @Override
    public String toString() {
        return super.toString() +
                String.format(" ClientName: [%s] Points: [%s] Eliminated: [%s]",
                        getClientName(), getPoints(), isEliminated());
    }

}