package domain;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;
import usecases.InsertCustomerToDB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Customer {

    private String recordNumber;
    private long customerID;
    private String customerName;
    private String customerSurname;
    private String customerAddress;
    private String customerZIPCode;
    private String customerNationalID;
    private LocalDateTime customerBirthDate;

    public Customer(String recordNumber, long customerID, String customerName, String customerSurname, String customerAddress, String customerZIPCode, String customerNationalID, LocalDateTime customerBirthDate) {
        this.recordNumber = recordNumber;
        this.customerID = customerID;
        this.customerName = customerName;
        this.customerSurname = customerSurname;
        this.customerAddress = customerAddress;
        this.customerZIPCode = customerZIPCode;
        this.customerNationalID = customerNationalID;
        this.customerBirthDate = customerBirthDate;
    }

    static final Logger logger = Logger.getLogger(Customer.class);

    public static void customerReadCSV() throws Exception{

        try {

            Reader reader = Files.newBufferedReader(Paths.get("Customers.csv"));

            File file = new File("ErrorJSONFileCustomer.json");
            ObjectMapper mapper = new ObjectMapper();
            JsonGenerator g = mapper.getFactory().createGenerator(new FileOutputStream(file));

            logger.info("Reading file Customer.csv.");
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
            List<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                logger.info("Assigning values to the variables and check validation.");
                String cutomerBirthDateV = csvRecord.get("CUSTOMER_BIRTH_DATE");
                Customer customer = new Customer(csvRecord.get("RECORD_NUMBER")
                        , Long.parseLong(csvRecord.get("CUSTOMER_ID"))
                        , csvRecord.get("CUSTOMER_NAME")
                        , csvRecord.get("CUSTOMER_SURNAME")
                        , csvRecord.get("CUSTOMER_ADDRESS")
                        , csvRecord.get("CUSTOMER_ZIP_CODE")
                        , csvRecord.get("CUSTOMER_NATIONAL_ID")
                        , LocalDateTime.parse(cutomerBirthDateV, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

                // Validate accounts
                if (customer.getCustomerBirthDate().getYear()<1995) {
                    logger.error("For customer " + customer.getCustomerID() + " birthdate is less than 1995.");
                    mapper.writeValue(g, customer);
                    continue;
//                   throw new AccountBalanceLessThanAccountLimit("AccountBalanceLessThanAccountLimit");
                } else if (customer.getCustomerNationalID().length() != 10) {
                    logger.error("For customer " + customer.getCustomerID() + "NationalID is not valid");
                    mapper.writeValue(g, customer);
                    continue;
//                    throw new AccountTypeNotValid("AccountTypeNotValid");
                }

                // insert into table
                InsertCustomerToDB.InsertCustomerToTable(customer);
            }

        }
//        catch (AccountBalanceLessThanAccountLimit | AccountTypeNotValid ex){
//
//        }
        catch (IOException ex) {
            logger.error("this exception has been happened: ", ex);
            //ex.printStackTrace();
        }
    }


    public String getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(String recordNumber) {
        this.recordNumber = recordNumber;
    }

    public long getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerSurname() {
        return customerSurname;
    }

    public void setCustomerSurname(String customerSurname) {
        this.customerSurname = customerSurname;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerZIPCode() {
        return customerZIPCode;
    }

    public void setCustomerZIPCode(String customerZIPCode) {
        this.customerZIPCode = customerZIPCode;
    }

    public String getCustomerNationalID() {
        return customerNationalID;
    }

    public void setCustomerNationalID(String customerNationalID) {
        this.customerNationalID = customerNationalID;
    }

    public LocalDateTime getCustomerBirthDate() {
        return customerBirthDate;
    }

    public void setCustomerBirthDate(LocalDateTime customerBirthDate) {
        this.customerBirthDate = customerBirthDate;
    }
}
