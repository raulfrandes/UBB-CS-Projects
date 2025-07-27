package jss.filter.filteringweb;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/testFilter")
public class TestFilterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String testText = req.getParameter("testText");

        req.setAttribute("message", testText);

        RequestDispatcher dispatcher = req.getRequestDispatcher("testFilter.jsp");
        dispatcher.forward(req, resp);
    }
}
