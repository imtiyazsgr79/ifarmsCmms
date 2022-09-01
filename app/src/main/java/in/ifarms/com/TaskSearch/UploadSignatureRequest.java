package in.ifarms.com.TaskSearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadSignatureRequest {


    @SerializedName("tasknumber")
    @Expose
    private String tasknumber;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("status")
    @Expose
    private String status;

    public String getTasknumber() {
        return tasknumber;
    }

    public void setTasknumber(String tasknumber) {
        this.tasknumber = tasknumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UploadSignatureRequest(String tasknumber, String image, String type, String status) {
        this.tasknumber = tasknumber;
        this.image = image;
        this.type = type;
        this.status = status;
    }
}
