-- ============================================================
-- OfflineSalesRPA - Centralized Database Schema
-- Wipro Java TalentNext - RPA Project
-- ============================================================

CREATE DATABASE IF NOT EXISTS rpa_sales_db;
USE rpa_sales_db;

DROP TABLE IF EXISTS sales_data;

CREATE TABLE sales_data (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    s_no          VARCHAR(10)   NOT NULL,      -- S1, S2, S3 ... as read from Excel
    sale_date     DATE          NOT NULL,       -- Date column from Excel
    product_sold  VARCHAR(150)  NOT NULL,       -- Product Sold column from Excel
    inserted_on   TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,  -- when RPA pushed it to DB
    UNIQUE KEY uq_s_no (s_no)
);

-- Keeps track of which serial number the RPA engine has processed so far.
-- This is what enables the "S1 done -> triggers S2" sequential behaviour.
CREATE TABLE IF NOT EXISTS trigger_state (
    id                  INT PRIMARY KEY DEFAULT 1,
    last_processed_sno  INT DEFAULT 0
);

INSERT INTO trigger_state (id, last_processed_sno) VALUES (1, 0)
    ON DUPLICATE KEY UPDATE id = id;

-- Handy views used by the JSP dashboard
CREATE OR REPLACE VIEW v_today_sales AS
    SELECT * FROM sales_data WHERE sale_date = CURDATE();

CREATE OR REPLACE VIEW v_month_sales AS
    SELECT * FROM sales_data
    WHERE MONTH(sale_date) = MONTH(CURDATE()) AND YEAR(sale_date) = YEAR(CURDATE());
