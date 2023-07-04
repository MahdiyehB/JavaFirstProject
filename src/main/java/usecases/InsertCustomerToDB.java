package usecases;

import repository.DBconnection;
import org.apache.log4j.Logger;
import domain.Customer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Mahdiyeh Biglari
 */

public class InsertCustomerToDB {
    static final Logger logger = Logger.getLogger(InsertCustomerToDB.class);

    /**
     * Inserts validated customers into database table
     * @param customer
     */
    public static void InsertCustomerToTable(Customer customer){
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            DataSource dataSource = DBconnection.getDataSource();
            connection = dataSource.getConnection();

            String SQL = "INSERT INTO customer (customerID, customerName, customerSurname, customerAddress, customerZIPCode, customerNationalID, customerBirthDate) " +
                    "VALUES(?,?,?,?,?,?,?)";

            pstmt = connection.prepareStatement(SQL);
            pstmt.setLong(1,customer.getCustomerID());
            pstmt.setString(2,customer.getCustomerName());
            pstmt.setString(3,customer.getCustomerSurname());
            pstmt.setString(4,customer.getCustomerAddress());
            pstmt.setString(5,customer.getCustomerZIPCode());
            pstmt.setString(6,customer.getCustomerNationalID());
            pstmt.setTimestamp(7,java.sql.Timestamp.valueOf(customer.getCustomerBirthDate()));


            pstmt.addBatch();
            int[] count = pstmt.executeBatch();
            connection.commit();
            logger.info("one record has been add to the table Account");

        } catch (SQLException e) {
            logger.error("this is SQL Exception: ",e);
            throw new RuntimeException(e);
        }


    }
}
