<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.wipro.rpa.dao.SalesDAO" %>
<%@ page import="com.wipro.rpa.model.SalesRecord" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Calendar" %>
<!DOCTYPE html>
<html>
<head>
    <title>Offline Sales RPA - Month Wise</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<jsp:include page="navbar.jsp" />

<div class="container">
    <h2>Month-wise Sales</h2>

    <%
        Calendar cal = Calendar.getInstance();
        int defaultMonth = cal.get(Calendar.MONTH) + 1;
        int defaultYear  = cal.get(Calendar.YEAR);

        String monthParam = request.getParameter("month");
        String yearParam  = request.getParameter("year");

        int month = (monthParam != null && !monthParam.isEmpty()) ? Integer.parseInt(monthParam) : defaultMonth;
        int year  = (yearParam  != null && !yearParam.isEmpty())  ? Integer.parseInt(yearParam)  : defaultYear;
    %>

    <form class="filter-form" method="get" action="monthWise.jsp">
        <label>Month:
            <select name="month">
                <% String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
                   for (int m = 1; m <= 12; m++) { %>
                    <option value="<%= m %>" <%= (m == month) ? "selected" : "" %>><%= months[m-1] %></option>
                <% } %>
            </select>
        </label>
        <label>Year:
            <input type="number" name="year" value="<%= year %>" style="width:80px;">
        </label>
        <button type="submit">Filter</button>
    </form>

    <%
        SalesDAO dao = new SalesDAO();
        List<SalesRecord> monthSales = dao.getMonthSales(month, year);
    %>

    <% if (monthSales.isEmpty()) { %>
        <p class="empty-msg">No sales found for the selected month.</p>
    <% } else { %>
        <table>
            <tr>
                <th>S.No</th>
                <th>Date</th>
                <th>Product Sold</th>
                <th>Pushed to DB At</th>
            </tr>
            <% for (SalesRecord r : monthSales) { %>
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
