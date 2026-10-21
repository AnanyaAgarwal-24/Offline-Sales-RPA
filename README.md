# OfflineSalesRPA - Corrected Project

This version fixes the missing dependency/project-configuration problem in the original ZIP and also fixes the trigger logic so a database error does not incorrectly advance S1 -> S2.

## Requirements
- JDK 16 (or newer)
- Maven 3.8+
- MySQL 8.x
- Apache Tomcat 9.x for the JSP dashboard
- Eclipse IDE for Enterprise Java and Web Developers (optional)

## 1. Database
1. Start MySQL.
2. Open `db/schema.sql` in MySQL Workbench and execute it.
3. Check `src/main/java/com/wipro/rpa/util/DBUtil.java` and change `DB_USER` / `DB_PASS` if your MySQL credentials are different.

## 2. Eclipse
Import the folder as an **Existing Maven Project**:
File -> Import -> Maven -> Existing Maven Projects -> select this folder.

Maven automatically downloads JXL and MySQL Connector/J, so the old empty `lib` folder is no longer required.

## 3. Run the RPA engine
From this project folder:

    mvn clean compile
    mvn dependency:build-classpath -Dmdep.outputFile=target/classpath.txt

Then run `com.wipro.rpa.Main` from Eclipse as a Java Application. The default Excel file is:

    sample-data/SalesData.xls

The engine checks the file every 3 seconds. It processes complete rows strictly in order: S1, then S2, then S3...

## 4. Test the sequence
For a clean test:

    USE rpa_sales_db;
    DELETE FROM sales_data;
    UPDATE trigger_state SET last_processed_sno = 0 WHERE id = 1;

Start `Main.java`. Edit/save `sample-data/SalesData.xls` so S1 is complete. The console should show S1 being inserted. Then edit/save the file with S2 complete, and so on.

## 5. Dashboard
Build the web application:

    mvn clean package

The generated file is:

    target/OfflineSalesRPA.war

Copy it to Tomcat's `webapps` folder and start Tomcat. Open:

    http://localhost:8080/OfflineSalesRPA/

The dashboard uses the same MySQL database.

## Important
This ZIP contains the project configuration and Maven dependency declarations, but it cannot contain your local MySQL server, MySQL password, JDK, Maven installation, or Tomcat installation. Maven downloads the Java libraries automatically when internet access is available.
