package jss.filter.filteringweb;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/addPattern")
public class AddPatternServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pattern = req.getParameter("pattern");
        String message;

        try {
            Class.forName("org.mariadb.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/filter_system", "root", "");
            String query = "INSERT INTO regular_expressions (pattern) VALUES (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, pattern);
            preparedStatement.executeUpdate();
            connection.close();
            message = "Pattern added successfully.";
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            message = "Error adding pattern.";
        }

        req.setAttribute("message", message);
        req.getRequestDispatcher("managePatterns.jsp").forward(req, resp);
    }
}
