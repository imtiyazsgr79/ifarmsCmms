package in.ifarms.com.General;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UpdateFaultReportRequest {

    @SerializedName("frId")
    @Expose
    private String frId;
    @SerializedName("bldgId")
    @Expose
    private Integer bldgId;
    @SerializedName("locId")
    @Expose
    private Integer locId;
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
    @SerializedName("reportedTime")
    @Expose
    private String reportedTime;
    @SerializedName("responseDate")
    @Expose
    private String responseDate;
    @SerializedName("responseTime")
    @Expose
    private String responseTime;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("endTime")
    @Expose
    private String endTime;
    @SerializedName("locOtherDesc")
    @Expose
    private String locOtherDesc;
    @SerializedName("faultCodeId")
    @Expose
    private Integer faultCodeId;
    @SerializedName("faultDtl")
    @Expose
    private String faultDtl;
    @SerializedName("priorityId")
    @Expose
    private Integer priorityId;
    @SerializedName("maintGrpId")
    @Expose
    private Integer maintGrpId;
    @SerializedName("division")
    @Expose
    private Object division;
    @SerializedName("observe")
    @Expose
    private String observe;
    @SerializedName("diagnosis")
    @Expose
    private String diagnosis;
    @SerializedName("actionTaken")
    @Expose
    private String actionTaken;
    @SerializedName("costCenter")
    @Expose
    private Integer costCenter;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("equipment")
    @Expose
    private Object equipment;
    @SerializedName("remarks")
    @Expose
    private List<String> remarks = null;
    @SerializedName("attendedBy")
    @Expose
    private Integer attendedBy;

    public String getFrId() {
        return frId;
    }

    public void setFrId(String frId) {
        this.frId = frId;
    }

    public Integer getBldgId() {
        return bldgId;
    }

    public void setBldgId(Integer bldgId) {
        this.bldgId = bldgId;
    }

    public Integer getLocId() {
        return locId;
    }

    public void setLocId(Integer locId) {
        this.locId = locId;
    }

    public String getReqtorName() {
        return reqtorName;
    }

    public void setReqtorName(String reqtorName) {
        this.reqtorName = reqtorName;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getReqtorContactNo() {
        return reqtorContactNo;
    }

    public void setReqtorContactNo(String reqtorContactNo) {
        this.reqtorContactNo = reqtorContactNo;
    }

    public String getReportedDate() {
        return reportedDate;
    }

    public void setReportedDate(String reportedDate) {
        this.reportedDate = reportedDate;
    }

    public String getReportedTime() {
        return reportedTime;
    }

    public void setReportedTime(String reportedTime) {
        this.reportedTime = reportedTime;
    }

    public String getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(String responseDate) {
        this.responseDate = responseDate;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLocOtherDesc() {
        return locOtherDesc;
    }

    public void setLocOtherDesc(String locOtherDesc) {
        this.locOtherDesc = locOtherDesc;
    }

    public Integer getFaultCodeId() {
        return faultCodeId;
    }

    public void setFaultCodeId(Integer faultCodeId) {
        this.faultCodeId = faultCodeId;
    }

    public String getFaultDtl() {
        return faultDtl;
    }

    public void setFaultDtl(String faultDtl) {
        this.faultDtl = faultDtl;
    }

    public Integer getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(Integer priorityId) {
        this.priorityId = priorityId;
    }

    public Integer getMaintGrpId() {
        return maintGrpId;
    }

    public void setMaintGrpId(Integer maintGrpId) {
        this.maintGrpId = maintGrpId;
    }

    public Object getDivision() {
        return division;
    }

    public void setDivision(Object division) {
        this.division = division;
    }

    public String getObserve() {
        return observe;
    }

    public void setObserve(String observe) {
        this.observe = observe;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getActionTaken() {
        return actionTaken;
    }

    public void setActionTaken(String actionTaken) {
        this.actionTaken = actionTaken;
    }

    public Integer getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(Integer costCenter) {
        this.costCenter = costCenter;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getEquipment() {
        return equipment;
    }

    public void setEquipment(Object equipment) {
        this.equipment = equipment;
    }

    public List<String> getRemarks() {
        return remarks;
    }

    public void setRemarks(List<String> remarks) {
        this.remarks = remarks;
    }

    public Integer getAttendedBy() {
        return attendedBy;
    }

    public void setAttendedBy(Integer attendedBy) {
        this.attendedBy = attendedBy;
    }

}
