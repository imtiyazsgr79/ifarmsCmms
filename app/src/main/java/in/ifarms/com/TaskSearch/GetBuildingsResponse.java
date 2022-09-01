package in.ifarms.com.TaskSearch;

public class GetBuildingsResponse {

    private String zone_id;
    private String priority_id;
    private int id;
    private String build_desc;
    private String build_name;

    public GetBuildingsResponse() {
    }

    public GetBuildingsResponse(String zone_id, String priority_id, int id, String build_desc, String build_name) {
        this.zone_id = zone_id;
        this.priority_id = priority_id;
        this.id = id;
        this.build_desc = build_desc;
        this.build_name = build_name;
    }

    public String getZone_id() {
        return zone_id;
    }

    public void setZone_id(String zone_id) {
        this.zone_id = zone_id;
    }

    public String getPriority_id() {
        return priority_id;
    }

    public void setPriority_id(String priority_id) {
        this.priority_id = priority_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBuild_desc() {
        return build_desc;
    }

    public void setBuild_desc(String build_desc) {
        this.build_desc = build_desc;
    }

    public String getBuild_name() {
        return build_name;
    }

    public void setBuild_name(String build_name) {
        this.build_name = build_name;
    }
}