package in.ifarms.com.Search;

import java.util.ArrayList;
import java.util.List;


public class EditFaultReportResponse {
    public String frId;
    public Object inProgressDate;
    public Object closedDate;
    public String clientFrId;
    public Object customerRefId;
    public Workspace workspace;
    public String reqtorName;
    public DeptId deptId;
    public String reqtorContactNo;
    public long reportedDate;
    public LocId locId;
    public BldgId bldgId;
    public String locOtherDesc;
    public FaultCodeId faultCodeId;
    public PriorityId priorityId;
    public String faultCodeOtherDesc;
    public MaintGrpId maintGrpId;
    public String createdBy;
    public String status;
    public double partCost;
    public Equipment equipment;
    public CostCenter costCenter;
    public Object observe;
    public Object diagnosis;
    public String actionTaken;
    public String faultDtl;
    public long createdDate;
    public long responseDate;
    public long startDate;
    public long endDate;
    public Object labourHrs;
    public Object otherCost;
    public Object inProgressBy;
    public String responseTime;
    public String startTime;
    public String endTime;
    public Object reasonForOutstanding;
    public String reportedTime;
    public AttendedBy attendedBy;
    public List<Remarks> remarks = new ArrayList<>();
    public Object beforeImage;
    public List<Object> beforeImagesList;
    public Object afterImage;
    public Object beforeImageDate;
    public Object afterImageDate;
    public Division division;
    public List<Object> afterImagesList;
    public Object faultBoundary;
    public Object updatedBy;
    public Object signatureImage;
    public Object breakdownCost;
    public String allRemarks;

    class Remarks{
        int id;
        String remarks;

        public Remarks(int id, String remarks) {
            this.id = id;
            this.remarks = remarks;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }
    }

    class AttendedBy{
        String name;
        int id;

        public AttendedBy(String name, int id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    class Equipment{
        String equipmentCode;

        public String getEquipmentCode() {
            return equipmentCode;
        }

        public Equipment(String equipmentCode) {
            this.equipmentCode = equipmentCode;
        }
    }

    static class CostCenter{
        int id;
        String costCenterID;

        public CostCenter(int id, String costCenterID) {
            this.id = id;
            this.costCenterID = costCenterID;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCostCenterID() {
            return costCenterID;
        }

        public void setCostCenterID(String costCenterID) {
            this.costCenterID = costCenterID;
        }
    }

    static class Division{
        int id;
        String division_id;

        public Division(int id, String division_id) {
            this.id = id;
            this.division_id = division_id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDivision_id() {
            return division_id;
        }

        public void setDivision_id(String division_id) {
            this.division_id = division_id;
        }
    }

    public String getFrId() {
        return frId;
    }

    public void setFrId(String frId) {
        this.frId = frId;
    }

    public Object getInProgressDate() {
        return inProgressDate;
    }

    public void setInProgressDate(Object inProgressDate) {
        this.inProgressDate = inProgressDate;
    }

    public Object getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(Object closedDate) {
        this.closedDate = closedDate;
    }

    public String getClientFrId() {
        return clientFrId;
    }

    public void setClientFrId(String clientFrId) {
        this.clientFrId = clientFrId;
    }

    public Object getCustomerRefId() {
        return customerRefId;
    }

    public void setCustomerRefId(Object customerRefId) {
        this.customerRefId = customerRefId;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public String getReqtorName() {
        return reqtorName;
    }

    public void setReqtorName(String reqtorName) {
        this.reqtorName = reqtorName;
    }

    public DeptId getDeptId() {
        return deptId;
    }

    public void setDeptId(DeptId deptId) {
        this.deptId = deptId;
    }

    public String getReqtorContactNo() {
        return reqtorContactNo;
    }

    public void setReqtorContactNo(String reqtorContactNo) {
        this.reqtorContactNo = reqtorContactNo;
    }

    public long getReportedDate() {
        return reportedDate;
    }

    public void setReportedDate(long reportedDate) {
        this.reportedDate = reportedDate;
    }

    public LocId getLocId() {
        return locId;
    }

    public void setLocId(LocId locId) {
        this.locId = locId;
    }

    public BldgId getBldgId() {
        return bldgId;
    }

    public void setBldgId(BldgId bldgId) {
        this.bldgId = bldgId;
    }

    public String getLocOtherDesc() {
        return locOtherDesc;
    }

    public void setLocOtherDesc(String locOtherDesc) {
        this.locOtherDesc = locOtherDesc;
    }

    public FaultCodeId getFaultCodeId() {
        return faultCodeId;
    }

    public void setFaultCodeId(FaultCodeId faultCodeId) {
        this.faultCodeId = faultCodeId;
    }

    public PriorityId getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(PriorityId priorityId) {
        this.priorityId = priorityId;
    }

    public String getFaultCodeOtherDesc() {
        return faultCodeOtherDesc;
    }

    public void setFaultCodeOtherDesc(String faultCodeOtherDesc) {
        this.faultCodeOtherDesc = faultCodeOtherDesc;
    }

    public MaintGrpId getMaintGrpId() {
        return maintGrpId;
    }

    public void setMaintGrpId(MaintGrpId maintGrpId) {
        this.maintGrpId = maintGrpId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPartCost() {
        return partCost;
    }

    public void setPartCost(double partCost) {
        this.partCost = partCost;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public Object getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(CostCenter costCenter) {
        this.costCenter = costCenter;
    }

    public Object getObserve() {
        return observe;
    }

    public void setObserve(Object observe) {
        this.observe = observe;
    }

    public Object getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(Object diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getActionTaken() {
        return actionTaken;
    }

    public void setActionTaken(String actionTaken) {
        this.actionTaken = actionTaken;
    }

    public String getFaultDtl() {
        return faultDtl;
    }

    public void setFaultDtl(String faultDtl) {
        this.faultDtl = faultDtl;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public long getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(long responseDate) {
        this.responseDate = responseDate;
    }

    public Object getStartDate() {
        return startDate;
    }

    public void setStartDate(Object startDate) {
        this.startDate = (long) startDate;
    }

    public Object getEndDate() {
        return endDate;
    }

    public void setEndDate(Object endDate) {
        this.endDate = (long) endDate;
    }

    public Object getLabourHrs() {
        return labourHrs;
    }

    public void setLabourHrs(Object labourHrs) {
        this.labourHrs = labourHrs;
    }

    public Object getOtherCost() {
        return otherCost;
    }

    public void setOtherCost(Object otherCost) {
        this.otherCost = otherCost;
    }

    public Object getInProgressBy() {
        return inProgressBy;
    }

    public void setInProgressBy(Object inProgressBy) {
        this.inProgressBy = inProgressBy;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime =  startTime;
    }

    public Object getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime =  endTime;
    }

    public Object getReasonForOutstanding() {
        return reasonForOutstanding;
    }

    public void setReasonForOutstanding(Object reasonForOutstanding) {
        this.reasonForOutstanding = reasonForOutstanding;
    }

    public String getReportedTime() {
        return reportedTime;
    }

    public void setReportedTime(String reportedTime) {
        this.reportedTime = reportedTime;
    }

    public AttendedBy getAttendedBy() {
        return attendedBy;
    }

    public void setAttendedBy(AttendedBy attendedBy) {
        this.attendedBy = attendedBy;
    }

    public List<Remarks> getRemarks() {
        return remarks;
    }

    public void setRemarks(List<Remarks> remarks) {
        this.remarks = remarks;
    }

    public Object getBeforeImage() {
        return beforeImage;
    }

    public void setBeforeImage(Object beforeImage) {
        this.beforeImage = beforeImage;
    }

    public List<Object> getBeforeImagesList() {
        return beforeImagesList;
    }

    public void setBeforeImagesList(List<Object> beforeImagesList) {
        this.beforeImagesList = beforeImagesList;
    }

    public Object getAfterImage() {
        return afterImage;
    }

    public void setAfterImage(Object afterImage) {
        this.afterImage = afterImage;
    }

    public Object getBeforeImageDate() {
        return beforeImageDate;
    }

    public void setBeforeImageDate(Object beforeImageDate) {
        this.beforeImageDate = beforeImageDate;
    }

    public Object getAfterImageDate() {
        return afterImageDate;
    }

    public void setAfterImageDate(Object afterImageDate) {
        this.afterImageDate = afterImageDate;
    }

    public Object getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public List<Object> getAfterImagesList() {
        return afterImagesList;
    }

    public void setAfterImagesList(List<Object> afterImagesList) {
        this.afterImagesList = afterImagesList;
    }

    public Object getFaultBoundary() {
        return faultBoundary;
    }

    public void setFaultBoundary(Object faultBoundary) {
        this.faultBoundary = faultBoundary;
    }

    public Object getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Object updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Object getSignatureImage() {
        return signatureImage;
    }

    public void setSignatureImage(Object signatureImage) {
        this.signatureImage = signatureImage;
    }

    public Object getBreakdownCost() {
        return breakdownCost;
    }

    public void setBreakdownCost(Object breakdownCost) {
        this.breakdownCost = breakdownCost;
    }

    public String getAllRemarks() {
        return allRemarks;
    }

    public void setAllRemarks(String allRemarks) {
        this.allRemarks = allRemarks;
    }

    public EditFaultReportResponse(String frId, Object inProgressDate, Object closedDate, String clientFrId,
                                   Object customerRefId, Workspace workspace, String reqtorName, DeptId deptId,
                                   String reqtorContactNo, long reportedDate, LocId locId, BldgId bldgId, String locOtherDesc,
                                   FaultCodeId faultCodeId, PriorityId priorityId, String faultCodeOtherDesc, MaintGrpId maintGrpId,
                                   String createdBy, String status, double partCost, Equipment equipment, CostCenter costCenter,
                                   Object observe, Object diagnosis, String actionTaken, String faultDtl, long createdDate,
                                   long responseDate, Object startDate, Object endDate, Object labourHrs, Object otherCost,
                                   Object inProgressBy, String responseTime, String startTime, String endTime, Object reasonForOutstanding,
                                   String reportedTime, AttendedBy attendedBy, List<Remarks> remarks, Object beforeImage,
                                   List<Object> beforeImagesList, Object afterImage, Object beforeImageDate, Object afterImageDate,
                                   Division division, List<Object> afterImagesList,
                                   Object faultBoundary, Object updatedBy, Object signatureImage, Object breakdownCost, String allRemarks) {
        this.frId = frId;
        this.inProgressDate = inProgressDate;
        this.closedDate = closedDate;
        this.clientFrId = clientFrId;
        this.customerRefId = customerRefId;
        this.workspace = workspace;
        this.reqtorName = reqtorName;
        this.deptId = deptId;
        this.reqtorContactNo = reqtorContactNo;
        this.reportedDate = reportedDate;
        this.locId = locId;
        this.bldgId = bldgId;
        this.locOtherDesc = locOtherDesc;
        this.faultCodeId = faultCodeId;
        this.priorityId = priorityId;
        this.faultCodeOtherDesc = faultCodeOtherDesc;
        this.maintGrpId = maintGrpId;
        this.createdBy = createdBy;
        this.status = status;
        this.partCost = partCost;
        this.equipment = equipment;
        this.costCenter = costCenter;
        this.observe = observe;
        this.diagnosis = diagnosis;
        this.actionTaken = actionTaken;
        this.faultDtl = faultDtl;
        this.createdDate = createdDate;
        this.responseDate = responseDate;
        this.startDate = (long) startDate;
        this.endDate = (long) endDate;
        this.labourHrs = labourHrs;
        this.otherCost = otherCost;
        this.inProgressBy = inProgressBy;
        this.responseTime = responseTime;
        this.startTime =  startTime;
        this.endTime =  endTime;
        this.reasonForOutstanding = reasonForOutstanding;
        this.reportedTime = reportedTime;
        this.attendedBy = attendedBy;
        this.remarks = remarks;
        this.beforeImage = beforeImage;
        this.beforeImagesList = beforeImagesList;
        this.afterImage = afterImage;
        this.beforeImageDate = beforeImageDate;
        this.afterImageDate = afterImageDate;
        this.division = division;
        this.afterImagesList = afterImagesList;
        this.faultBoundary = faultBoundary;
        this.updatedBy = updatedBy;
        this.signatureImage = signatureImage;
        this.breakdownCost = breakdownCost;
        this.allRemarks = allRemarks;
    }

    public class Workspace{
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProject_desc() {
            return project_desc;
        }

        public void setProject_desc(String project_desc) {
            this.project_desc = project_desc;
        }

        public String getBuild_desc() {
            return build_desc;
        }

        public void setBuild_desc(String build_desc) {
            this.build_desc = build_desc;
        }

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public String getContractor() {
            return contractor;
        }

        public void setContractor(String contractor) {
            this.contractor = contractor;
        }

        public String getBldg_owner_pay_amt() {
            return bldg_owner_pay_amt;
        }

        public void setBldg_owner_pay_amt(String bldg_owner_pay_amt) {
            this.bldg_owner_pay_amt = bldg_owner_pay_amt;
        }

        public Object getContractor_pay_amt() {
            return contractor_pay_amt;
        }

        public void setContractor_pay_amt(Object contractor_pay_amt) {
            this.contractor_pay_amt = contractor_pay_amt;
        }

        public long getStart_date() {
            return start_date;
        }

        public void setStart_date(long start_date) {
            this.start_date = start_date;
        }

        public Object getEnd_date() {
            return end_date;
        }

        public void setEnd_date(Object end_date) {
            this.end_date = end_date;
        }

        public Object getEmailsetting() {
            return emailsetting;
        }

        public void setEmailsetting(Object emailsetting) {
            this.emailsetting = emailsetting;
        }

        public String getProj_IMAGE() {
            return proj_IMAGE;
        }

        public void setProj_IMAGE(String proj_IMAGE) {
            this.proj_IMAGE = proj_IMAGE;
        }

        public Workspace(String id, String project_desc, String build_desc, String owner, String contractor,
                         String bldg_owner_pay_amt, Object contractor_pay_amt, long start_date,
                         Object end_date, Object emailsetting, String proj_IMAGE) {
            this.id = id;
            this.project_desc = project_desc;
            this.build_desc = build_desc;
            this.owner = owner;
            this.contractor = contractor;
            this.bldg_owner_pay_amt = bldg_owner_pay_amt;
            this.contractor_pay_amt = contractor_pay_amt;
            this.start_date = start_date;
            this.end_date = end_date;
            this.emailsetting = emailsetting;
            this.proj_IMAGE = proj_IMAGE;
        }

        public String id;
    public String project_desc;
    public String build_desc;
    public String owner;
    public String contractor;
    public String bldg_owner_pay_amt;
    public Object contractor_pay_amt;
    public long start_date;
    public Object end_date;
    public Object emailsetting;
    public String proj_IMAGE;
}

public class Workspace2{
    public String id;
    public String project_desc;
    public String build_desc;
    public String owner;
    public String contractor;
    public String bldg_owner_pay_amt;
    public Object contractor_pay_amt;
    public long start_date;
    public Object end_date;
    public Object emailsetting;
    public String proj_IMAGE;
}

public class DeptId{
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDept_id() {
        return dept_id;
    }

    public void setDept_id(String dept_id) {
        this.dept_id = dept_id;
    }

    public Workspace2 getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace2 workspace) {
        this.workspace = workspace;
    }

    public String getDept_desc() {
        return dept_desc;
    }

    public void setDept_desc(String dept_desc) {
        this.dept_desc = dept_desc;
    }

    public Object getDeptAdd() {
        return deptAdd;
    }

    public void setDeptAdd(Object deptAdd) {
        this.deptAdd = deptAdd;
    }

    public DeptId(int id, String dept_id, Workspace2 workspace,
                  String dept_desc, Object deptAdd) {
        this.id = id;
        this.dept_id = dept_id;
        this.workspace = workspace;
        this.dept_desc = dept_desc;
        this.deptAdd = deptAdd;
    }

    public int id;
    public String dept_id;
    public Workspace2 workspace;
    public String dept_desc;
    public Object deptAdd;
}

public class LocId{
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoc_id() {
        return loc_id;
    }

    public void setLoc_id(String loc_id) {
        this.loc_id = loc_id;
    }

    public String getLoc_desc() {
        return loc_desc;
    }

    public void setLoc_desc(String loc_desc) {
        this.loc_desc = loc_desc;
    }

    public String getPriorityID() {
        return priorityID;
    }

    public void setPriorityID(String priorityID) {
        this.priorityID = priorityID;
    }

    public String getCostCenterID() {
        return costCenterID;
    }

    public void setCostCenterID(String costCenterID) {
        this.costCenterID = costCenterID;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public Object getLat() {
        return lat;
    }

    public void setLat(Object lat) {
        this.lat = lat;
    }

    public Object getLng() {
        return lng;
    }

    public void setLng(Object lng) {
        this.lng = lng;
    }

    public LocId(int id, String loc_id, String loc_desc, String priorityID,
                 String costCenterID, String streetName, Object lat, Object lng) {
        this.id = id;
        this.loc_id = loc_id;
        this.loc_desc = loc_desc;
        this.priorityID = priorityID;
        this.costCenterID = costCenterID;
        this.streetName = streetName;
        this.lat = lat;
        this.lng = lng;
    }

    public int id;
    public String loc_id;
    public String loc_desc;
    public String priorityID;
    public String costCenterID;
    public String streetName;
    public Object lat;
    public Object lng;
}

public class BldgId{
    public int id;
    public String build_name;
    public String build_desc;
    public Object priority_id;
    public Object zone_id;
}

public class Workspace3{
    public String id;
    public String project_desc;
    public String build_desc;
    public String owner;
    public String contractor;
    public String bldg_owner_pay_amt;
    public Object contractor_pay_amt;
    public long start_date;
    public Object end_date;
    public Object emailsetting;
    public String proj_IMAGE;
}

public class FaultCodeId{
    public int id;
    public String faultcode_id;
    public Workspace3 workspace;
    public String faultCodeDesc;
    public String priority;
    public Object faultId;
}

public static class Workspace4{
    public String id;
    public String project_desc;
    public String build_desc;
    public String owner;
    public String contractor;
    public String bldg_owner_pay_amt;
    public Object contractor_pay_amt;
    public long start_date;
    public Object end_date;
    public Object emailsetting;
    public String proj_IMAGE;
}

public class PriorityId{
    public int id;
    public String priority_id;
    public Workspace4 workspace;
    public String priDesc;
}

public static class Workspace5{
    public String id;
    public String project_desc;
    public String build_desc;
    public String owner;
    public String contractor;
    public String bldg_owner_pay_amt;
    public Object contractor_pay_amt;
    public long start_date;
    public Object end_date;
    public Object emailsetting;
    public String proj_IMAGE;
}

public static class MaintGrpId{
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaint_id() {
        return maint_id;
    }

    public void setMaint_id(String maint_id) {
        this.maint_id = maint_id;
    }

    public Workspace5 getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace5 workspace) {
        this.workspace = workspace;
    }

    public String getmGrpDesc() {
        return mGrpDesc;
    }

    public void setmGrpDesc(String mGrpDesc) {
        this.mGrpDesc = mGrpDesc;
    }

    public String getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(String activeFlag) {
        this.activeFlag = activeFlag;
    }

    public MaintGrpId(int id, String maint_id, Workspace5 workspace, String mGrpDesc, String activeFlag) {
        this.id = id;
        this.maint_id = maint_id;
        this.workspace = workspace;
        this.mGrpDesc = mGrpDesc;
        this.activeFlag = activeFlag;
    }

    public int id;
    public String maint_id;
    public Workspace5 workspace;
    public String mGrpDesc;
    public String activeFlag;
}

}
