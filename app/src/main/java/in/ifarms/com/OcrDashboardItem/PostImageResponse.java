package in.ifarms.com.OcrDashboardItem;

public class PostImageResponse {
    String ocrImage;

    public PostImageResponse(String ocrImage) {
        this.ocrImage = ocrImage;
    }

    public String getOcrImage() {
        return ocrImage;
    }

    public void setOcrImage(String ocrImage) {
        this.ocrImage = ocrImage;
    }
}
