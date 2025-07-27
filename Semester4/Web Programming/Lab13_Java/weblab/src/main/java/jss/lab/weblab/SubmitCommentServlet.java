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

@WebServlet("/submitComment")
public class SubmitCommentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String comment = req.getParameter("comment");
        String captcha = req.getParameter("captcha");
        HttpSession session = req.getSession();
        String captchaSession = (String) session.getAttribute("captcha");
        String message;

        if (captcha == null || !captcha.equals(captchaSession)) {
            message = "Invalid CAPTCHA. Please try again.";
            req.setAttribute("message", message);
            RequestDispatcher dispatcher = req.getRequestDispatcher("index.jsp");
            dispatcher.forward(req, resp);
        } else {

            try {
                Class.forName("org.mariadb.jdbc.Driver");

                Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/comment_system", "root", "");
                String query = "INSERT INTO comments (email, comment) VALUES (?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, comment);
                preparedStatement.executeUpdate();
                connection.close();
                message = "Comment submitted for approval.";
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                message = "Error submitting comment.";
            }

            req.setAttribute("message", message);
            RequestDispatcher dispatcher = req.getRequestDispatcher("index.jsp");
            dispatcher.forward(req, resp);
        }
    }
}
