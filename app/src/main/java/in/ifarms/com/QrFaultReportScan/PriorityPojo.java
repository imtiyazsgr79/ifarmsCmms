package in.ifarms.com.QrFaultReportScan;

import androidx.annotation.NonNull;

public class PriorityPojo {
    int id;
    String name;

    public PriorityPojo(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
