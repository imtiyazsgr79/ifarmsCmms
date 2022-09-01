package in.ifarms.com.FaultReportSearch;

public class RowList {
    private String frId, status, building, location,token,workspace;
    private Long reporteddate, createdDate;
    private String user;

    public RowList(String frId, String status, String building, String location, Long reporteddate, Long createdDate,String token, String workspace, String user) {
        this.frId = frId;
        this.status = status;
        this.building = building;
        this.location = location;
        this.reporteddate = reporteddate;
        this.createdDate = createdDate;
        this.token = token;
        this.workspace = workspace;
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getFrId() {
        return frId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    public void setFrId(String frId) {
        this.frId = frId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getReporteddate() {
        return reporteddate;
    }

    public void setReporteddate(Long reporteddate) {
        this.reporteddate = reporteddate;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }
}
