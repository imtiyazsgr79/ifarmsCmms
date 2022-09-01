package in.ifarms.com.OcrDashboardItem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaveUtilityRequest {

    @SerializedName("meterid")
    @Expose
    private Integer meterid;
    @SerializedName("currentReading")
    @Expose
    private String currentReading;
    @SerializedName("previousReading")
    @Expose
    private String previousReading;
    @SerializedName("readingDate")
    @Expose
    private String readingDate;
    @SerializedName("remarks")
    @Expose
    private String remarks;

    @SerializedName("ocrImage")
    @Expose
    private String ocrImage;

    public Integer getMeterid() {
        return meterid;
    }

    public void setMeterid(Integer meterid) {
        this.meterid = meterid;
    }

    public String getCurrentReading() {
        return currentReading;
    }

    public void setCurrentReading(String currentReading) {
        this.currentReading = currentReading;
    }

    public String getPreviousReading() {
        return previousReading;
    }

    public void setPreviousReading(String previousReading) {
        this.previousReading = previousReading;
    }

    public String getReadingDate() {
        return readingDate;
    }

    public void setReadingDate(String readingDate) {
        this.readingDate = readingDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getOcrImage() {
        return ocrImage;
    }

    public void setOcrImage(String ocrImage) {
        this.ocrImage = ocrImage;
    }

    public SaveUtilityRequest(Integer meterid, String currentReading, String previousReading, String readingDate, String remarks, String ocrImage) {
        this.meterid = meterid;
        this.currentReading = currentReading;
        this.previousReading = previousReading;
        this.readingDate = readingDate;
        this.remarks = remarks;
        this.ocrImage = ocrImage;
    }
}
