import AllExceptions.CustomerNotFoundException;
import org.json.JSONArray;
import org.json.JSONObject;
import repository.DBconnection;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This is a class with an independent main method to query all cutomers
 * which their accountBalance is more than a spedial ammount
 * @author Mahdiyeh Biglari
 */

public class QueryOnCustomer {

    static final Logger logger = Logger.getLogger(QueryOnCustomer.class);

    /**
     * this is the main method which calls the method to get list of cutomers
     * with more than special accountBalance in JSON file
     * @param args
     */
    public static void main(String args[]){
        new QueryOnCustomer(1000);
    }

    /**
     * This method adds the result of the query to the JSON file
     * @param accountBalance
     */
    public  QueryOnCustomer(int accountBalance){
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            DataSource dataSource = DBconnection.getDataSource();
            connection = dataSource.getConnection();

            String SQL = "SELECT customerID,customerName,customerSurname,customerNationalID," +
                    "accountNumber,accountOpenDate,accountBalance  FROM test.customer a\n" +
                    "inner join test.account b\n" +
                    "on a.customerID = b.accountCustomerID\n" +
                    "and b.accountBalance > ? ";

            pstmt = connection.prepareStatement(SQL);
            pstmt.setLong(1,accountBalance);

            ResultSet rs = pstmt.executeQuery();

            logger.info("query has been executed successfully");

            logger.info("if there is no result in throws into CusotmerNotFoundException exception");
            if (!rs.next()) throw new CustomerNotFoundException(accountBalance);

            logger.info("Create a JSON array to store the data from the result set");
            JSONArray jsonArray = new JSONArray();

            logger.info("Loop through the result set and add each row to the JSON array");
            while (rs.next()) {
                // Create a JSON object to store the data from each column
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("customerID", rs.getInt("customerID"));
                jsonObject.put("customerName", rs.getString("customerName"));
                jsonObject.put("customerSurname", rs.getString("customerSurname"));
                jsonObject.put("customerNationalID", rs.getString("customerNationalID"));
                jsonObject.put("accountNumber", rs.getString("accountNumber"));
                jsonObject.put("accountOpenDate", rs.getTimestamp("accountOpenDate"));
                jsonObject.put("accountBalance", rs.getLong("accountBalance"));

                logger.info("Add the JSON object to the JSON array");
                jsonArray.put(jsonObject);
            }


            logger.info("Close the result set, prepared statement and connection");
            rs.close();
            pstmt.close();
            connection.close();

            logger.info("Write the JSON array to a file");
            FileWriter fileWriter = new FileWriter("CustomerAccountReport.json");
            fileWriter.write(jsonArray.toString());
            fileWriter.flush();
            fileWriter.close();

        } catch (SQLException e) {
            logger.error("this is SQL Exception: ",e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
