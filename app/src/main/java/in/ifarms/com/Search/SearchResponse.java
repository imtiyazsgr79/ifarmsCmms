package in.ifarms.com.Search;

import java.nio.charset.Charset;

public class SearchResponse {

    public void setTokenGen(String tokenGen) {
        this.tokenGen = tokenGen;
    }

    public SearchResponse() {
    }

    public SearchResponse(String user, String frId, long reportedDate, long createdDate, String status,
                          String building, String location, String tokenGen, String workspaceSearch) {
        this.frId = frId;
        this.user = user;
        this.reportedDate = reportedDate;
        this.createdDate = createdDate;
        this.status = status;
        this.building = building;
        this.location = location;
        this.tokenGen = tokenGen;
        this.workspaceSearch = workspaceSearch;
    }

    public String getUser() { return user; }

    public void setUser(String user) {
        this.user = user;
    }

    private String user;
    private long createdDate;
    private String status;
    private String building;
    private String location;
    private String frId;
    private long reportedDate;
    private String tokenGen;
    private String workspaceSearch;

    public String getWorkspaceSearch() {
        return workspaceSearch;
    }

    public void setWorkspaceSearch(String workspaceSearch) {
        this.workspaceSearch = workspaceSearch;
    }

    public String getTokenGen() {
        return tokenGen;
    }

    public String getFrId() {
        return frId;
    }

    public void setFrId(String frId) {
        this.frId = frId;
    }

    public long getReportedDate() {
        return reportedDate;
    }

    public void setReportedDate(long reportedDate) {
        this.reportedDate = reportedDate;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
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

    public Charset toLowerCase() {
        return null;
    }
}