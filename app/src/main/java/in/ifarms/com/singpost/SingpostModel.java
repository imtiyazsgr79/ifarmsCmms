package in.ifarms.com.singpost;

public class SingpostModel {

    Long equipId;
    String nameOfCondo;
    String condoAddress;
    String serialNumberMain;
    String simCardRegNo;
    String lockerSize;
    String logCard;

    public Long getEquipId() {
        return equipId;
    }

    public String getNameOfCondo() {
        return nameOfCondo;
    }

    public String getCondoAddress() {
        return condoAddress;
    }

    public String getSerialNumberMain() {
        return serialNumberMain;
    }

    public String getSimCardRegNo() {
        return simCardRegNo;
    }

    public String getLockerSize() {
        return lockerSize;
    }

    public String getLogCard() {
        return logCard;
    }

    public SingpostModel(Long equipId, String nameOfCondo, String condoAddress,
                         String serialNumberMain, String simCardRegNo, String lockerSize, String logCard) {
        this.equipId = equipId;
        this.nameOfCondo = nameOfCondo;
        this.condoAddress = condoAddress;
        this.serialNumberMain = serialNumberMain;
        this.simCardRegNo = simCardRegNo;
        this.lockerSize = lockerSize;
        this.logCard = logCard;
    }

    public SingpostModel(String logCard) {
        this.logCard = logCard;
    }
}
