<%@ page import="java.sql.*" %><%--
  Created by IntelliJ IDEA.
  User: Raul
  Date: 6/2/2024
  Time: 1:43 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    HttpSession session1 = request.getSession(false);
    if (session1 == null || session1.getAttribute("admin") == null) {
        response.sendRedirect("adminLogin.jsp");
    }

    String message = (String) request.getAttribute("message");

    if (message != null) {
%>
        <p><%= message %></p>
<%
    }
%>
<html>
<head>
    <title>Manage Patterns</title>
</head>
<body>
    <h2>Add New Pattern</h2>
    <form action="addPattern" method="post">
        Pattern: <input type="text" name="pattern" required><br>
        <input type="submit" value="Add Pattern">
    </form>

    <h2>Existing Patterns</h2>
    <ul>
        <%
            try {
                Class.forName("org.mariadb.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/filter_system", "root", "");
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM regular_expressions");

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String pattern = resultSet.getString("pattern");

                    out.println("<li>" + pattern + " <a href='deletePattern?id=" + id + "'>Delete</a><li>");
                }
                connection.close();
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        %>
    </ul>
</body>
</html>
