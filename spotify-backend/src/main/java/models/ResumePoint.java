package models;

public class ResumePoint {
    private boolean fullyPlayed;
    private long resumePositionMs;

    // Getters and Setters
    public boolean isFullyPlayed() {
        return fullyPlayed;
    }

    public void setFullyPlayed(boolean fullyPlayed) {
        this.fullyPlayed = fullyPlayed;
    }

    public long getResumePositionMs() {
        return resumePositionMs;
    }

    public void setResumePositionMs(long resumePositionMs) {
        this.resumePositionMs = resumePositionMs;
    }
}
