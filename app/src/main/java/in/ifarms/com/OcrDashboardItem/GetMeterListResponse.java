package in.ifarms.com.OcrDashboardItem; ;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetMeterListResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("meterName")
    @Expose
    private String meterName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMeterName() {
        return meterName;
    }

    public void setMeterName(String meterName) {
        this.meterName = meterName;
    }

}