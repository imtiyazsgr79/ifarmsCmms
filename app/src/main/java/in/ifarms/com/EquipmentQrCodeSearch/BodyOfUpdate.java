package in.ifarms.com.EquipmentQrCodeSearch;

public class BodyOfUpdate {

    String task_number,status,completedBy,compDate,compTime;

    public String getTask_number() {
        return task_number;
    }

    public void setTask_number(String task_number) {
        this.task_number = task_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(String completedBy) {
        this.completedBy = completedBy;
    }

    public String getCompDate() {
        return compDate;
    }

    public void setCompDate(String compDate) {
        this.compDate = compDate;
    }

    public String getCompTime() {
        return compTime;
    }

    public void setCompTime(String compTime) {
        this.compTime = compTime;
    }

    public BodyOfUpdate(String task_number, String status, String completedBy, String compDate, String compTime) {
        this.task_number = task_number;
        this.status = status;
        this.completedBy = completedBy;
        this.compDate = compDate;
        this.compTime = compTime;
    }
}
