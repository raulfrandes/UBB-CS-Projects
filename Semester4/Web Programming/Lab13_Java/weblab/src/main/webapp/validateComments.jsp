<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*" %><%--
  Created by IntelliJ IDEA.
  User: Raul
  Date: 6/2/2024
  Time: 1:37 AM
  To change this template use File | Settings | File Templates.
--%>
<%
    HttpSession session1 = request.getSession(false);
    if (session1 == null || session1.getAttribute("admin") == null) {
        response.sendRedirect("adminLogin.jsp");
        return;
    }

    String message = (String) request.getAttribute("message");
%>
<html>
<head>
    <title>Validate Comments</title>
</head>
<body>
    <h2>Validate Comments</h2>

    <% if (message != null) { %>
        <p><%= message %></p>
    <% } %>

    <form action="validateComment" method="post">
        <%
            try {
                Class.forName("org.mariadb.jdbc.Driver");

                Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/comment_system", "root", "");
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM comments WHERE validated = FALSE");

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String email = resultSet.getString("email");
                    String comment = resultSet.getString("comment");

                    out.println("<input type='checkbox' name='commentId' value='" + id + "'>");
                    out.println("Email: " + email + "<br>");
                    out.println("Comment: " + comment + "<br><br>");
                }

                connection.close();
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        %>
        <input type="submit" value="Validate Selected">
    </form>
</body>
</html>
