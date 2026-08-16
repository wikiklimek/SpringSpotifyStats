package models;

//Track Class
public class TrackData {
    private int numSamples;
    private double duration;
    private String sampleMd5;
    private double offsetSeconds;
    private double windowSeconds;
    private int analysisSampleRate;
    private int analysisChannels;
    private double endOfFadeIn;
    private double startOfFadeOut;
    private double loudness;
    private double tempo;
    private double tempoConfidence;
    private int timeSignature;
    private double timeSignatureConfidence;
    private int key;
    private double keyConfidence;
    private int mode;
    private double modeConfidence;
    private String codestring;
    private double codeVersion;
    private String echoprintstring;
    private double echoprintVersion;
    private String synchstring;
    private int synchVersion;
    private String rhythmstring;
    private int rhythmVersion;

    // Getters and Setters
    public int getNumSamples() {
        return numSamples;
    }

    public void setNumSamples(int numSamples) {
        this.numSamples = numSamples;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String getSampleMd5() {
        return sampleMd5;
    }

    public void setSampleMd5(String sampleMd5) {
        this.sampleMd5 = sampleMd5;
    }

    public double getOffsetSeconds() {
        return offsetSeconds;
    }

    public void setOffsetSeconds(double offsetSeconds) {
        this.offsetSeconds = offsetSeconds;
    }

    public double getWindowSeconds() {
        return windowSeconds;
    }

    public void setWindowSeconds(double windowSeconds) {
        this.windowSeconds = windowSeconds;
    }

    public int getAnalysisSampleRate() {
        return analysisSampleRate;
    }

    public void setAnalysisSampleRate(int analysisSampleRate) {
        this.analysisSampleRate = analysisSampleRate;
    }

    public int getAnalysisChannels() {
        return analysisChannels;
    }

    public void setAnalysisChannels(int analysisChannels) {
        this.analysisChannels = analysisChannels;
    }

    public double getEndOfFadeIn() {
        return endOfFadeIn;
    }

    public void setEndOfFadeIn(double endOfFadeIn) {
        this.endOfFadeIn = endOfFadeIn;
    }

    public double getStartOfFadeOut() {
        return startOfFadeOut;
    }

    public void setStartOfFadeOut(double startOfFadeOut) {
        this.startOfFadeOut = startOfFadeOut;
    }

    public double getLoudness() {
        return loudness;
    }

    public void setLoudness(double loudness) {
        this.loudness = loudness;
    }

    public double getTempo() {
        return tempo;
    }

    public void setTempo(double tempo) {
        this.tempo = tempo;
    }

    public double getTempoConfidence() {
        return tempoConfidence;
    }

    public void setTempoConfidence(double tempoConfidence) {
        this.tempoConfidence = tempoConfidence;
    }

    public int getTimeSignature() {
        return timeSignature;
    }

    public void setTimeSignature(int timeSignature) {
        this.timeSignature = timeSignature;
    }

    public double getTimeSignatureConfidence() {
        return timeSignatureConfidence;
    }

    public void setTimeSignatureConfidence(double timeSignatureConfidence) {
        this.timeSignatureConfidence = timeSignatureConfidence;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public double getKeyConfidence() {
        return keyConfidence;
    }

    public void setKeyConfidence(double keyConfidence) {
        this.keyConfidence = keyConfidence;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public double getModeConfidence() {
        return modeConfidence;
    }

    public void setModeConfidence(double modeConfidence) {
        this.modeConfidence = modeConfidence;
    }

    public String getCodestring() {
        return codestring;
    }

    public void setCodestring(String codestring) {
        this.codestring = codestring;
    }

    public double getCodeVersion() {
        return codeVersion;
    }

    public void setCodeVersion(double codeVersion) {
        this.codeVersion = codeVersion;
    }

    public String getEchoprintstring() {
        return echoprintstring;
    }

    public void setEchoprintstring(String echoprintstring) {
        this.echoprintstring = echoprintstring;
    }

    public double getEchoprintVersion() {
        return echoprintVersion;
    }

    public void setEchoprintVersion(double echoprintVersion) {
        this.echoprintVersion = echoprintVersion;
    }

    public String getSynchstring() {
        return synchstring;
    }

    public void setSynchstring(String synchstring) {
        this.synchstring = synchstring;
    }

    public int getSynchVersion() {
        return synchVersion;
    }

    public void setSynchVersion(int synchVersion) {
        this.synchVersion = synchVersion;
    }

    public String getRhythmstring() {
        return rhythmstring;
    }

    public void setRhythmstring(String rhythmstring) {
        this.rhythmstring = rhythmstring;
    }

    public int getRhythmVersion() {
        return rhythmVersion;
    }

    public void setRhythmVersion(int rhythmVersion) {
        this.rhythmVersion = rhythmVersion;
    }
}

