package com.wipro.rpa.model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * POJO representing a single row of the offline Excel sheet:
 * S.No | Date | Product Sold
 */
public class SalesRecord {

    private String sNo;          // e.g. "S1", "S2", "S3"
    private Date saleDate;
    private String productSold;
    private Timestamp insertedOn;

    public SalesRecord() { }

    public SalesRecord(String sNo, Date saleDate, String productSold) {
        this.sNo = sNo;
        this.saleDate = saleDate;
        this.productSold = productSold;
    }

    public String getsNo() { return sNo; }
    public void setsNo(String sNo) { this.sNo = sNo; }

    public Date getSaleDate() { return saleDate; }
    public void setSaleDate(Date saleDate) { this.saleDate = saleDate; }

    public String getProductSold() { return productSold; }
    public void setProductSold(String productSold) { this.productSold = productSold; }

    public Timestamp getInsertedOn() { return insertedOn; }
    public void setInsertedOn(Timestamp insertedOn) { this.insertedOn = insertedOn; }

    /** Numeric part of s_no, e.g. "S3" -> 3. Used for sequential trigger ordering. */
    public int getSerialNumber() {
        return Integer.parseInt(sNo.replaceAll("[^0-9]", ""));
    }

    @Override
    public String toString() {
        return sNo + " | " + saleDate + " | " + productSold;
    }
}
