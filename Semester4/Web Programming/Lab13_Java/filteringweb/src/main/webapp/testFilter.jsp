<%--
  Created by IntelliJ IDEA.
  User: Raul
  Date: 6/2/2024
  Time: 2:16 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Test Content Filter</title>
</head>
<body>
    <h2>Test the Content Filter</h2>
    <form action="testFilter" method="post">
        Enter text: <textarea name="testText" required></textarea><br>
        <input type="submit" value="Submit">
    </form>

    <%
        String message = (String) request.getAttribute("message");
        if (message != null) {
    %>
        <h3>Filtered Output:</h3>
        <p><%= message %></p>
    <%
        }
    %>
</body>
</html>
