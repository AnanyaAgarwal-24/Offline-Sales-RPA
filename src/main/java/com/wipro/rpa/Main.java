package com.wipro.rpa;

import com.wipro.rpa.watcher.FileWatcherService;

/**
 * Entry point. Run this as a standalone Java application (the "RPA engine").
 * It keeps watching the offline SalesData.xls file and pushes new rows
 * to the centralized MySQL database in S1 -> S2 -> S3 order.
 *
 * Usage:
 *   java -cp "bin;lib/*" com.wipro.rpa.Main "sample-data/SalesData.xls"
 */
public class Main {
    public static void main(String[] args) {
        String filePath = args.length > 0 ? args[0] : "sample-data/SalesData.xls";
        long pollInterval = 3000; // check every 3 seconds

        FileWatcherService watcher = new FileWatcherService(filePath, pollInterval);
        watcher.start();
    }
}
