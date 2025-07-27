package jss.lab.weblab;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/validateComment")
public class ValidateCommentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            resp.sendRedirect("adminLogin.jsp");
            return;
        }

        String[] commentIds = req.getParameterValues("commentId");
        String message;
        if (commentIds != null) {
            try {
                Class.forName("org.mariadb.jdbc.Driver");

                Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/comment_system", "root", "");
                String query = "UPDATE comments SET validated = TRUE WHERE id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);

                for (String id : commentIds) {
                    preparedStatement.setInt(1, Integer.parseInt(id));
                    preparedStatement.executeUpdate();
                }
                connection.close();
                message = "Comments validated successfully.";
            } catch (ClassNotFoundException | SQLException e){
                e.printStackTrace();
                message = "Error validating comments.";
            }
        } else {
            message = "No comments selected for validation.";
        }

        req.setAttribute("message", message);
        RequestDispatcher dispatcher = req.getRequestDispatcher("validateComments.jsp");
        dispatcher.forward(req, resp);
    }
}
