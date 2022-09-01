package in.ifarms.com.GIS;

public class GlobeActivityResponce {
   private Faultreportloclist faultreportloclist;
   private Tasklocationlist tasklocationlis;

    public Faultreportloclist getFaultreportloclist() {
        return faultreportloclist;
    }

    public void setFaultreportloclist(Faultreportloclist faultreportloclist) {
        this.faultreportloclist = faultreportloclist;
    }

    public Tasklocationlist getTasklocationlis() {
        return tasklocationlis;
    }

    public void setTasklocationlis(Tasklocationlist tasklocationlis) {
        this.tasklocationlis = tasklocationlis;
    }

    public GlobeActivityResponce(Faultreportloclist faultreportloclist, Tasklocationlist tasklocationlis) {
        this.faultreportloclist = faultreportloclist;
        this.tasklocationlis = tasklocationlis;
    }

public      class  Faultreportloclist {
    public Faultreportloclist(String lng, String frId, String location, String lat) {
        this.lng = lng;
        this.frId = frId;
        this.location = location;
        this.lat = lat;
    }

    private String lng;

    private String frId;

    private String location;

    private String lat;

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getFrId() {
        return frId;
    }

    public void setFrId(String frId) {
        this.frId = frId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}

public     class Tasklocationlist
{
    public Tasklocationlist(String tno, String lng, String location, String task_number, String lat) {
        this.tno = tno;
        this.lng = lng;
        this.location = location;
        this.task_number = task_number;
        this.lat = lat;
    }

    private String tno;

    private String lng;

    private String location;

    private String task_number;

    private String lat;

    public String getTno ()
    {
        return tno;
    }

    public void setTno (String tno)
    {
        this.tno = tno;
    }

    public String getLng ()
    {
        return lng;
    }

    public void setLng (String lng)
    {
        this.lng = lng;
    }

    public String getLocation ()
    {
        return location;
    }

    public void setLocation (String location)
    {
        this.location = location;
    }

    public String getTask_number ()
    {
        return task_number;
    }

    public void setTask_number (String task_number)
    {
        this.task_number = task_number;
    }

    public String getLat ()
    {
        return lat;
    }

    public void setLat (String lat)
    {
        this.lat = lat;
    }}
}








