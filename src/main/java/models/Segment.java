package models;

import java.util.List;

//Segment Class
public class Segment {
    private double start;
    private double duration;
    private double confidence;
    private double loudnessStart;
    private double loudnessMax;
    private double loudnessMaxTime;
    private double loudnessEnd;
    private List<Double> pitches;
    private List<Double> timbre;

    // Getters and Setters
    public double getStart() {
        return start;
    }

    public void setStart(double start) {
        this.start = start;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public double getLoudnessStart() {
        return loudnessStart;
    }

    public void setLoudnessStart(double loudnessStart) {
        this.loudnessStart = loudnessStart;
    }

    public double getLoudnessMax() {
        return loudnessMax;
    }

    public void setLoudnessMax(double loudnessMax) {
        this.loudnessMax = loudnessMax;
    }

    public double getLoudnessMaxTime() {
        return loudnessMaxTime;
    }

    public void setLoudnessMaxTime(double loudnessMaxTime) {
        this.loudnessMaxTime = loudnessMaxTime;
    }

    public double getLoudnessEnd() {
        return loudnessEnd;
    }

    public void setLoudnessEnd(double loudnessEnd) {
        this.loudnessEnd = loudnessEnd;
    }

    public List<Double> getPitches() {
        return pitches;
    }

    public void setPitches(List<Double> pitches) {
        this.pitches = pitches;
    }

    public List<Double> getTimbre() {
        return timbre;
    }

    public void setTimbre(List<Double> timbre) {
        this.timbre = timbre;
    }
}
