<%@ page import="java.sql.*" %><%--
  Created by IntelliJ IDEA.
  User: Raul
  Date: 6/2/2024
  Time: 1:05 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Submit Comment</title>
</head>
<body>
    <h2>Submit a Comment</h2>
    <form action="submitComment" method="post">
        Email: <input type="email" name="email" required><br>
        Comment: <textarea name="comment" required></textarea><br>
        <img src="captcha" alt="CAPTCHA Image"><br>
        Enter CAPTCHA: <input type="text" name="captcha" required><br>
        <input type="submit" value="Submit">
    </form>

    <%
        String message = (String) request.getAttribute("message");
        if (message != null) {
    %>
        <p><%= message%></p>
    <%
        }
    %>

    <h2>Validated Comments</h2>
    <%
        try{
            Class.forName("org.mariadb.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/comment_system", "root", "");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM comments WHERE validated = TRUE");

            while (resultSet.next()) {
                String email = resultSet.getString("email");
                String comment = resultSet.getString("comment");

                out.println("Email: " + email + "<br>");
                out.println("Comment: " + comment + "<br><br>");
            }
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    %>
</body>
</html>
