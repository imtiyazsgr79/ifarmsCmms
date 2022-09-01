package in.ifarms.com.QrFaultReportScan;

public class CreateFaultQrReport {

    String equipmentCode;
    int priorityId;
    String faultDtl;

    public CreateFaultQrReport(String equipcode, int priorityId, String faultDescriptionName) {
        this.equipmentCode = equipcode;
        this.faultDtl = faultDescriptionName;
        this.priorityId = priorityId;
    }
}
