<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.wipro.rpa.dao.SalesDAO" %>
<%@ page import="com.wipro.rpa.model.SalesRecord" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Offline Sales RPA - Full History</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<jsp:include page="navbar.jsp" />

<div class="container">
    <h2>All Sales - Full History (Date &amp; Time)</h2>

    <%
        SalesDAO dao = new SalesDAO();
        List<SalesRecord> allSales = dao.getAllSales();
    %>

    <% if (allSales.isEmpty()) { %>
        <p class="empty-msg">No data pushed to the database yet.</p>
    <% } else { %>
        <table>
            <tr>
                <th>S.No</th>
                <th>Date</th>
                <th>Product Sold</th>
                <th>Pushed to DB At (Date &amp; Time)</th>
            </tr>
            <% for (SalesRecord r : allSales) { %>
                <tr>
                    <td><%= r.getsNo() %></td>
                    <td><%= r.getSaleDate() %></td>
                    <td><%= r.getProductSold() %></td>
                    <td><%= r.getInsertedOn() %></td>
                </tr>
            <% } %>
        </table>
    <% } %>
</div>
</body>
</html>
