package in.ifarms.com.FaultReportActivity;

import java.util.ArrayList;

public class FaultReportResponse {

    public ArrayList buildingList;
    public ArrayList faultCodeList;
    public ArrayList deptList;
    public ArrayList priorityList;
    public ArrayList maintGrpList;
    public ArrayList costCenterList;
    public ArrayList divisions;


    public ArrayList getBuildingList() {
        return buildingList;
    }

    public ArrayList getFaultCodeList() {
        return faultCodeList;
    }

    public ArrayList getDeptList() {
        return deptList;
    }

    public ArrayList getPriorityList() {
        return priorityList;
    }

    public ArrayList getMaintGrpList() {
        return maintGrpList;
    }

    public ArrayList getCostCenterList() {
        return costCenterList;
    }

    public ArrayList getDivisions() {
        return divisions;
    }
}
