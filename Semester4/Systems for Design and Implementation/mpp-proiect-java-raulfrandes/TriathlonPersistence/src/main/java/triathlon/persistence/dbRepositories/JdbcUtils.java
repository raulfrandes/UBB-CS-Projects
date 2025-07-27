package triathlon.persistence.dbRepositories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import triathlon.persistence.RepositoryException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {
    private Properties jdbcProps;
    private static final Logger logger = LogManager.getLogger();
    private Connection instance = null;
    public JdbcUtils(Properties jdbcProps) {
        this.jdbcProps = jdbcProps;
    }

    private Connection getNewConnection(){
        logger.traceEntry();

        String url = jdbcProps.getProperty("triathlon.jdbc.url");
        String user = jdbcProps.getProperty("triathlon.jdbc.user");
        String password = jdbcProps.getProperty("triathlon.jdbc.pass");
        logger.info("trying to connect to database ... {}", url);
        logger.info("user: {}", user);
        logger.info("password: {}", password);

        Connection connection = null;
        try {
            if (user != null && password != null) {
                connection = DriverManager.getConnection(url, user, password);
            }
            else {
                connection = DriverManager.getConnection(url);
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new RepositoryException("Error getting connection " + e);
        }
        logger.traceExit(connection);
        return connection;
    }

    public Connection getConnection() {
        logger.traceEntry();
        try {
            if (instance == null || instance.isClosed()) {
                instance = getNewConnection();
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new RepositoryException("Error DB " + e);
        }
        logger.traceExit(instance);
        return instance;
    }
}
