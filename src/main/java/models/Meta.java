package models;

//Meta Class
public class Meta {
    private String analyzerVersion;
    private String platform;
    private String detailedStatus;
    private int statusCode;
    private long timestamp;
    private double analysisTime;
    private String inputProcess;

    // Getters and Setters
    public String getAnalyzerVersion() {
        return analyzerVersion;
    }

    public void setAnalyzerVersion(String analyzerVersion) {
        this.analyzerVersion = analyzerVersion;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getDetailedStatus() {
        return detailedStatus;
    }

    public void setDetailedStatus(String detailedStatus) {
        this.detailedStatus = detailedStatus;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getAnalysisTime() {
        return analysisTime;
    }

    public void setAnalysisTime(double analysisTime) {
        this.analysisTime = analysisTime;
    }

    public String getInputProcess() {
        return inputProcess;
    }

    public void setInputProcess(String inputProcess) {
        this.inputProcess = inputProcess;
    }
}
