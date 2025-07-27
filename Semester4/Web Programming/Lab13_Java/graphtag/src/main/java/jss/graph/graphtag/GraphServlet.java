package jss.graph.graphtag;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

@WebServlet("/graph")
public class GraphServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int[] valuesOx = {1, 2, 3, 4, 5};
        int[] valuesOy = {10, 20, 15, 25, 30};

        req.setAttribute("valuesOx", Arrays.toString(valuesOx).replaceAll("[\\[\\] ]", ""));
        req.setAttribute("valuesOy", Arrays.toString(valuesOy).replaceAll("[\\[\\] ]", ""));

        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}
