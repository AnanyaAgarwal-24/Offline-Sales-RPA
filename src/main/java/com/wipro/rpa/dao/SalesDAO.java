package com.wipro.rpa.dao;

import com.wipro.rpa.model.SalesRecord;
import com.wipro.rpa.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalesDAO {

    public boolean insertRecord(SalesRecord record) {
        String sql = "INSERT INTO sales_data (s_no, sale_date, product_sold) VALUES (?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, record.getsNo());
            ps.setDate(2, record.getSaleDate());
            ps.setString(3, record.getProductSold());
            ps.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException dup) {
            System.out.println("[DAO] " + record.getsNo() + " already exists in DB, skipping insert.");
            return false;
        } catch (SQLException e) {
            System.err.println("[DAO] Insert failed for " + record.getsNo() + ": " + e.getMessage());
            return false;
        }
    }

    public boolean recordExists(String sNo) {
        String sql = "SELECT 1 FROM sales_data WHERE s_no = ? LIMIT 1";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, sNo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("[DAO] Could not check " + sNo + ": " + e.getMessage());
            return false;
        }
    }

    public int getLastProcessedSerial() {
        String sql = "SELECT last_processed_sno FROM trigger_state WHERE id = 1";
        try (Connection con = DBUtil.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt("last_processed_sno");
        } catch (SQLException e) {
            System.err.println("[DAO] Could not read trigger state: " + e.getMessage());
        }
        return 0;
    }

    public void updateLastProcessedSerial(int serial) {
        String sql = "UPDATE trigger_state SET last_processed_sno = ? WHERE id = 1";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, serial);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[DAO] Could not update trigger state: " + e.getMessage());
        }
    }

    public List<SalesRecord> getTodaySales() {
        return runQuery("SELECT * FROM sales_data WHERE sale_date = CURDATE() ORDER BY id");
    }

    public List<SalesRecord> getMonthSales(int month, int year) {
        String sql = "SELECT * FROM sales_data WHERE MONTH(sale_date)=? AND YEAR(sale_date)=? ORDER BY sale_date, id";
        List<SalesRecord> list = new ArrayList<>();
        try (Connection con = DBUtil.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, month);
            ps.setInt(2, year);
            try (ResultSet rs = ps.executeQuery()) {
                addRows(rs, list);
            }
        } catch (SQLException e) {
            System.err.println("[DAO] Month query failed: " + e.getMessage());
        }
        return list;
    }

    public List<SalesRecord> getAllSales() {
        return runQuery("SELECT * FROM sales_data ORDER BY sale_date DESC, id DESC");
    }

    private List<SalesRecord> runQuery(String sql) {
        List<SalesRecord> list = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            addRows(rs, list);
        } catch (SQLException e) {
            System.err.println("[DAO] Query failed: " + e.getMessage());
        }
        return list;
    }

    private void addRows(ResultSet rs, List<SalesRecord> list) throws SQLException {
        while (rs.next()) {
            SalesRecord r = new SalesRecord();
            r.setsNo(rs.getString("s_no"));
            r.setSaleDate(rs.getDate("sale_date"));
            r.setProductSold(rs.getString("product_sold"));
            r.setInsertedOn(rs.getTimestamp("inserted_on"));
            list.add(r);
        }
    }
}
