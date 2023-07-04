package utils.csvReader;

import org.apache.log4j.Logger;
import repository.DBconnection;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * this class get a query from both Accounts and Customers
 * to find each customer's accounts and balances
 * @apiNote this is considered as a separate Jar file from the Main
 * @author Mahdiyeh Biglari
 */
public class MatchAccountToCustomer {

    static final Logger logger = Logger.getLogger(MatchAccountToCustomer.class);

    /**
     * th method execute the query to find each customer's accounts and balances
     * @param customerID
     * @return
     * @throws SQLException
     */
    public static int checkAccountToCustomer(int customerID) throws SQLException {

        logger.info("get connection to the db");
        Connection connection = null;
        PreparedStatement pstmt = null;
        DataSource dataSource = DBconnection.getDataSource();
        connection = dataSource.getConnection();

        logger.info("provide sql statement");
        String SQL = "SELECT customerID  FROM test.customer a\n" +
                "where a.customerID =  ? ";

        logger.info("assign parameters to the query");
        pstmt = connection.prepareStatement(SQL);
        pstmt.setInt(1, customerID);

        logger.info("execute the query");
        ResultSet rs = pstmt.executeQuery();
        if (!rs.next())
            return 0;
        else
            return 1;
    }

}
