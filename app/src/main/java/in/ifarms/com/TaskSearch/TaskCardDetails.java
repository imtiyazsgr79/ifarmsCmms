package in.ifarms.com.TaskSearch;

public class TaskCardDetails {
    private String tNoString;
    private String locationString;

    public TaskCardDetails(String tNoString, String locationString) {
        this.tNoString = tNoString;
        this.locationString = locationString;
    }

    public String gettNoString() {
        return tNoString;
    }

    public void settNoString(String tNoString) {
        this.tNoString = tNoString;
    }

    public String getLocationString() {
        return locationString;
    }

    public void setLocationString(String locationString) {
        this.locationString = locationString;
    }
}
