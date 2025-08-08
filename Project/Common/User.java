package Project.Common;

import java.io.Serializable;

/**
 * DVC2 - 8/6/2025 - Added `isAway` and `isSpectator` fields to the User class for Milestone 3.
 */
public class User implements Serializable {
    private long clientId = Constants.DEFAULT_CLIENT_ID;
    private String clientName;
    private int points;
    private boolean eliminated;
    private boolean isAway = false; // DVC2 - 8/6/2025 - Added to support away status
    private boolean isSpectator = false; // DVC2 - 8/6/2025 - Added to support spectator status

    // existing getters and setters...
    
    // DVC2 - 8/6/2025 - New getters and setters for the new fields.
    public boolean isAway() {
        return isAway;
    }

    public void setAway(boolean isAway) {
        this.isAway = isAway;
    }
    
    public boolean isSpectator() {
        return isSpectator;
    }

    public void setSpectator(boolean isSpectator) {
        this.isSpectator = isSpectator;
    }

    public String getDisplayName() {
        return String.format("%s#%s", this.clientName, this.clientId);
    }

    public void reset() {
        this.clientId = Constants.DEFAULT_CLIENT_ID;
        this.clientName = null;
        this.points = 0;
        this.eliminated = false;
        this.isAway = false;
        this.isSpectator = false;
    }
}