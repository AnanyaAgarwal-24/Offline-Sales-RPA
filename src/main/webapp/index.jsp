<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.wipro.rpa.dao.SalesDAO" %>
<%@ page import="com.wipro.rpa.model.SalesRecord" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Offline Sales RPA - Today</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<jsp:include page="navbar.jsp" />

<div class="container">
    <h2>Today's Sales (<%= new java.text.SimpleDateFormat("dd-MM-yyyy").format(new java.util.Date()) %>)</h2>

    <%
        SalesDAO dao = new SalesDAO();
        List<SalesRecord> todaySales = dao.getTodaySales();
    %>

    <% if (todaySales.isEmpty()) { %>
        <p class="empty-msg">No sales pushed by the RPA engine yet today.</p>
    <% } else { %>
        <table>
            <tr>
                <th>S.No</th>
                <th>Date</th>
                <th>Product Sold</th>
                <th>Pushed to DB At</th>
            </tr>
            <% for (SalesRecord r : todaySales) { %>
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
