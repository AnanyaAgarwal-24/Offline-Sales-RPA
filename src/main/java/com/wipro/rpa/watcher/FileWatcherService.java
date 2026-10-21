package com.wipro.rpa.watcher;

import com.wipro.rpa.model.SalesRecord;
import com.wipro.rpa.reader.ExcelReader;
import com.wipro.rpa.trigger.RowTrigger;

import java.io.File;
import java.util.List;

/**
 * Polls the offline Excel file for changes (simulating an RPA "watch folder").
 * Whenever the file's last-modified time changes (i.e. someone updated S1 /
 * S2 / S3 in the sheet), it re-reads the sheet and hands the rows to the
 * RowTrigger, which enforces the S1 -> S2 -> S3 sequential push to the DB.
 */
public class FileWatcherService {

    private final String filePath;
    private final long pollIntervalMs;
    private final ExcelReader excelReader = new ExcelReader();
    private final RowTrigger trigger = new RowTrigger();

    private long lastModified = -1;

    public FileWatcherService(String filePath, long pollIntervalMs) {
        this.filePath = filePath;
        this.pollIntervalMs = pollIntervalMs;
    }

    public void start() {
        System.out.println("[WATCHER] Watching " + filePath + " every " + pollIntervalMs + "ms ...");
        File file = new File(filePath);

        while (true) {
            if (file.exists() && file.lastModified() != lastModified) {
                lastModified = file.lastModified();
                System.out.println("[WATCHER] Change detected in Excel sheet, reading rows...");

                List<SalesRecord> rows = excelReader.readAll(filePath);
                trigger.process(rows);
            }

            try {
                Thread.sleep(pollIntervalMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
