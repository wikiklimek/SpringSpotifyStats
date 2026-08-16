package models;

public class ExplicitContent {
    private boolean filter_enabled;
    private boolean filter_locked;

    // Getters and Setters
    public boolean isFilterEnabled() {
        return filter_enabled;
    }

    public void setFilterEnabled(boolean filter_enabled) {
        this.filter_enabled = filter_enabled;
    }

    public boolean isFilterLocked() {
        return filter_locked;
    }

    public void setFilterLocked(boolean filter_locked) {
        this.filter_locked = filter_locked;
    }
}
