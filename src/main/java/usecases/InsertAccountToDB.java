package usecases;

import repository.DBconnection;

import org.apache.log4j.Logger;
import domain.Account;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Mahdiyeh Biglari
 */
public class InsertAccountToDB {
    static final Logger logger = Logger.getLogger(InsertAccountToDB.class);

    /**
     * Inserts validated accounts to the database table
     * @param account
     */
    public static void InsertAccountToTable(Account account){
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            logger.info("set a connection to DB");
            DataSource dataSource = DBconnection.getDataSource();
            connection = dataSource.getConnection();

            logger.info("Provide the SQL statement");
            String SQL = "INSERT INTO account (accountNumber,accountType,accountCustomerID,accountLimit,accountOpenDate,accountBalance) " +
                    "VALUES(?,?,?,?,?,?)";

            pstmt = connection.prepareStatement(SQL);
            pstmt.setString(1,account.getAccountNumber());
            pstmt.setInt(2,account.getAccountType());
            pstmt.setInt(3,account.getAccountCustomerID());
            pstmt.setLong(4,account.getAccountLimit());
            pstmt.setTimestamp(5,java.sql.Timestamp.valueOf(account.getAccountOpenDate()));
            pstmt.setLong(6,account.getAccountBalance());

            pstmt.addBatch();
            logger.info("execute the query");
            int[] count = pstmt.executeBatch();
            connection.commit();
            logger.info("one record has been add to the table Account");

        } catch (SQLException e) {
            logger.error("this is SQL Exception: ",e);
            throw new RuntimeException(e);
        }


    }

}
