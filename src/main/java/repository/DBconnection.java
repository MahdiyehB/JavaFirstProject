package repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.util.ResourceBundle;

/**
 * @author Mahdiyeh Biglari
 */

public class DBconnection {

    static final Logger logger = Logger.getLogger(DBconnection.class);
    private static DataSource datasource;

    /**
     * Method to establish connection to MYSQL database
     * @return datasource
     */
    public static DataSource getDataSource() {
        logger.info("Providing datasource connection");
        if (datasource == null) {
            ResourceBundle bundle = ResourceBundle.getBundle("DB");
            String DB_URL = bundle.getString("DB_URL");
            String USER = bundle.getString("USER");
            String PASS = bundle.getString("PASS");

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(DB_URL);
            config.setUsername(USER);
            config.setPassword(PASS);

            config.setMaximumPoolSize(10);
            config.setAutoCommit(false);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            datasource = new HikariDataSource(config);
        }
        return datasource;
    }
}
