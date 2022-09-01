package in.ifarms.com.FaultReportActivity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateFaultRequest {


    @SerializedName("reqtorName")
    @Expose
    private String reqtorName;
    @SerializedName("deptId")
    @Expose
    private Integer deptId;
    @SerializedName("reqtorContactNo")
    @Expose
    private String reqtorContactNo;
    @SerializedName("reportedDate")
    @Expose
    private String reportedDate;
    @SerializedName("priorityId")
    @Expose
    private Integer priorityId;
    @SerializedName("bldgId")
    @Expose
    private Integer bldgId;
    @SerializedName("locId")
    @Expose
    private Integer locId;
    @SerializedName("locOtherDesc")
    @Expose
    private String locOtherDesc;
    @SerializedName("faultCodeId")
    @Expose
    private Integer faultCodeId;
    @SerializedName("faultDtl")
    @Expose
    private String faultDtl;
    @SerializedName("maintGrpId")
    @Expose
    private Integer maintGrpId;
    @SerializedName("reportedTime")
    @Expose
    private String reportedTime;
    @SerializedName("division")
    @Expose
    private Integer division;


    public CreateFaultRequest(String reqtorName, Integer deptId, String reqtorContactNo,
                              String reportedDate, Integer priorityId, Integer bldgId, Integer locId,
                              String locOtherDesc, Integer faultCodeId, String faultDtl, Integer maintGrpId, String reportedTime, Integer division) {
        this.reqtorName = reqtorName;
        this.deptId = deptId;
        this.reqtorContactNo = reqtorContactNo;
        this.reportedDate = reportedDate;
        this.priorityId = priorityId;
        this.bldgId = bldgId;
        this.locId = locId;
        this.locOtherDesc = locOtherDesc;
        this.faultCodeId = faultCodeId;
        this.faultDtl = faultDtl;
        this.maintGrpId = maintGrpId;
        this.reportedTime = reportedTime;
        this.division = division;
    }

    public String getReqtorName() {
        return reqtorName;
    }

    public long getDeptId() {
        return deptId;
    }

    public String getReqtorContactNo() {
        return reqtorContactNo;
    }

    public String getReportedDate() {
        return reportedDate;
    }

    public long getPriorityId() {
        return priorityId;
    }

    public long getBldgId() {
        return bldgId;
    }

    public long getLocId() {
        return locId;
    }

    public String getLocOtherDesc() {
        return locOtherDesc;
    }

    public long getFaultCodeId() {
        return faultCodeId;
    }

    public String getFaultDtl() {
        return faultDtl;
    }

    public long getMaintGrpId() {
        return maintGrpId;
    }

    public String getReportedTime() {
        return reportedTime;
    }

    public int getDivision() {
        return division;
    }
}
