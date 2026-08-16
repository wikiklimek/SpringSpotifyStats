package models;

import java.util.List;

public class MusicAnalysis {
    
    private Meta meta;
    private TrackData track;
    private List<Bar> bars;
    private List<Beat> beats;
    private List<Section> sections;
    private List<Segment> segments;
    private List<Tatum> tatums;

    // Getters and Setters

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public TrackData getTrack() {
        return track;
    }

    public void setTrack(TrackData track) {
        this.track = track;
    }

    public List<Bar> getBars() {
        return bars;
    }

    public void setBars(List<Bar> bars) {
        this.bars = bars;
    }

    public List<Beat> getBeats() {
        return beats;
    }

    public void setBeats(List<Beat> beats) {
        this.beats = beats;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    public List<Tatum> getTatums() {
        return tatums;
    }

    public void setTatums(List<Tatum> tatums) {
        this.tatums = tatums;
    }

    
    

    
}
