package business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


final public class CheckoutRecord implements Serializable {

    private String recordID = UUID.randomUUID().toString();

    public String getRecordID() {
        return recordID;
    }

    private List<CheckoutRecordEntry> recordEntries = new ArrayList<>();

    public List<CheckoutRecordEntry> getRecordEntries() {
        return recordEntries;
    }

    public void addRecordEntry(CheckoutRecordEntry recordEntry) {
        recordEntries.add(recordEntry);
    }

    @Override
    public String toString() {
        return recordEntries.toString();
    }

    private static final long serialVersionUID = -2226197306790714013L;
}
