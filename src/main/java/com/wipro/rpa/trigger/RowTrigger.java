package com.wipro.rpa.trigger;

import com.wipro.rpa.dao.SalesDAO;
import com.wipro.rpa.model.SalesRecord;

import java.util.Comparator;
import java.util.List;

public class RowTrigger {
    private final SalesDAO dao = new SalesDAO();

    public void process(List<SalesRecord> excelRows) {
        excelRows.sort(Comparator.comparingInt(SalesRecord::getSerialNumber));
        int lastProcessed = dao.getLastProcessedSerial();

        for (SalesRecord record : excelRows) {
            int currentSerial = record.getSerialNumber();
            if (currentSerial != lastProcessed + 1) continue;

            System.out.println("[TRIGGER] Processing " + record.getsNo());
            boolean inserted = dao.insertRecord(record);

            if (inserted || dao.recordExists(record.getsNo())) {
                dao.updateLastProcessedSerial(currentSerial);
                lastProcessed = currentSerial;
                System.out.println("[TRIGGER] " + record.getsNo() + " complete. Next expected: S" + (currentSerial + 1));
            } else {
                System.err.println("[TRIGGER] " + record.getsNo() + " was NOT written. Will retry on next file change.");
                break;
            }
        }
    }
}
