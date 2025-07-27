package jss.filter.filteringweb;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@WebFilter("/*")
public class ContentFilter implements Filter {
    private List<Pattern> patterns;
    private long lastRefreshTime;
    private static final long REFRESH_INTERVAL = 30000;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        patterns = new ArrayList<>();
        lastRefreshTime = 0;
        refreshPatterns();
    }

    private synchronized void refreshPatterns() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastRefreshTime > REFRESH_INTERVAL) {
            patterns.clear();
            try {
                Class.forName("org.mariadb.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/filter_system", "root", "");
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT pattern FROM regular_expressions");

                while (resultSet.next()) {
                    String regex = resultSet.getString("pattern");
                    patterns.add(Pattern.compile(regex));
                }
                connection.close();
                lastRefreshTime = currentTime;
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpServletRequest.getRequestURI();

        if (requestURI.contains("/managePatterns.jsp") || requestURI.contains("/addPattern") || requestURI.contains("/deletePattern")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        refreshPatterns();

        CharResponseWrapper responseWrapper = new CharResponseWrapper((HttpServletResponse) servletResponse);
        filterChain.doFilter(servletRequest, responseWrapper);

        String output = responseWrapper.toString();
        for (Pattern pattern : patterns) {
            output = pattern.matcher(output).replaceAll("***");
        }

        PrintWriter out = servletResponse.getWriter();
        out.write(output);
    }

    @Override
    public void destroy() {
    }
}

class CharResponseWrapper extends HttpServletResponseWrapper {
    private CharArrayWriter charArrayWriter;

    public CharResponseWrapper(HttpServletResponse response) {
        super(response);
        charArrayWriter = new CharArrayWriter();
    }

    @Override
    public PrintWriter getWriter(){
        return new PrintWriter(charArrayWriter);
    }

    @Override
    public String toString() {
        return charArrayWriter.toString();
    }
}
