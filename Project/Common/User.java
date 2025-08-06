package Project.Common;

import java.io.Serializable;

/**
 * DVC2 - 7/29/2025 - Added points and eliminated status to track game progress for a client.
 */
public class User implements Serializable {
    private long clientId = Constants.DEFAULT_CLIENT_ID;
    private String clientName;
    private int points;
    private boolean eliminated;

    /**
     * @return the clientId
     */
    public long getClientId() {
        return clientId;
    }

    /**
     * @param clientId the clientId to set
     */
    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    /**
     * @return the username
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * @param username the username to set
     */
    public void setClientName(String username) {
        this.clientName = username;
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

    public String getDisplayName() {
        return String.format("%s#%s", this.clientName, this.clientId);
    }

    public void reset() {
        this.clientId = Constants.DEFAULT_CLIENT_ID;
        this.clientName = null;
        this.points = 0;
        this.eliminated = false;
    }
}