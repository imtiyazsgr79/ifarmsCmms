package in.ifarms.com.OcrDashboardItem; ;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MeterSelectedResponse {

    @SerializedName("previous")
    @Expose
    private String previous;
    @SerializedName("previousDate")
    @Expose
    private String previousDate;

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public String getPreviousDate() {
        return previousDate;
    }

    public void setPreviousDate(String previousDate) {
        this.previousDate = previousDate;
    }

}