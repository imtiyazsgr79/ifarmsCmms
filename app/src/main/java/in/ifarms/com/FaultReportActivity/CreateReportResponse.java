package in.ifarms.com.FaultReportActivity;

import java.util.ArrayList;

public class CreateReportResponse {

    String frId;
    ArrayList workspaceCreate;
    String reqName;

    public CreateReportResponse(String frId, ArrayList workspaceCreate, String reqName) {
        this.frId = frId;
        this.workspaceCreate = workspaceCreate;
        this.reqName = reqName;
    }

    public String getFrId() {
        return frId;
    }

    public void setFrId(String frId) {
        this.frId = frId;
    }

    public ArrayList getWorkspaceCreate() {
        return workspaceCreate;
    }

    public void setWorkspaceCreate(ArrayList workspaceCreate) {
        this.workspaceCreate = workspaceCreate;
    }

    public String getReqName() {
        return reqName;
    }

    public void setReqName(String reqName) {
        this.reqName = reqName;
    }
}
