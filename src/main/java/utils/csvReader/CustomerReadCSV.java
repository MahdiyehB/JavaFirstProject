package utils.csvReader;

import domain.Customer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import usecases.InsertCustomerToDB;
import utils.jsonUtil.ErrortoJSON;
import utils.jsonUtil.JSONDetails;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * To read data from CSV files
 * @author Mahdiyeh Biglari
 */
public class CustomerReadCSV{

    static final Logger logger = Logger.getLogger(CustomerReadCSV.class);
    private static String fileName = "Customers.csv";

    /**
     * Method for reading data from customer.csv file
     * implemented thread with ExecutorService
     */

    public static void ReadFile(){
        try {
            Reader reader = Files.newBufferedReader(Paths.get(fileName));
            logger.info("Reading file Customer.csv");
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
            List<CSVRecord> csvRecords = csvParser.getRecords();


            logger.info("create a JobDataMap to store the data");
            JobDataMap jobDataMap1 = new JobDataMap();
            jobDataMap1.put("data", csvRecords.subList(0,3));

            JobDataMap jobDataMap2 = new JobDataMap();
            jobDataMap2.put("data", csvRecords.subList(3,5));

            logger.info("create the first job and pass the data");
            JobDetail customerJob1 = JobBuilder.newJob(CustomerReadJob1.class)
                    .withIdentity("customerJob1", "group1")
                    .usingJobData(jobDataMap1)
                    .build();

            logger.info("create the second job and pass the data");
            JobDetail customerJob2 = JobBuilder.newJob(CustomerReadJob2.class)
                    .withIdentity("customerJob2", "group1")
                    .usingJobData(jobDataMap2)
                    .build();

            Trigger customerTrigger1 = TriggerBuilder.newTrigger()
                    .withIdentity("customerCronTrigger1", "group1")
                    .withSchedule(CronScheduleBuilder
                            .cronSchedule("0/10 * * * * ?")).build();

            Trigger customerTrigger2 = TriggerBuilder.newTrigger()
                    .withIdentity("customerCronTrigger2", "group1")
                    .withSchedule(CronScheduleBuilder
                            .cronSchedule("0/10 * * * * ?")).build();

            logger.info("Execute the job.");
            Scheduler scheduler =
                    new StdSchedulerFactory().getScheduler();
            scheduler.start();
            scheduler.scheduleJob(customerJob1, customerTrigger1);
            scheduler.scheduleJob(customerJob2, customerTrigger2);

        } catch (IOException | SchedulerException e) {
            logger.error("this exception has been happened: ", e);
        }
    }

    /**
     * This Method is for validating each record and put it into DB if it was valid,
     * otherwise add it into ErrorJSONFile
     * @param records
     */
    public static void CustomerProcessFile(Iterable<CSVRecord> records){

        try {
            logger.info("iterate over the records and process them");
            for (CSVRecord csvRecord : records) {
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

                JSONDetails jsonDetails = new JSONDetails();

                logger.info("Validate accounts");
                if (customer.getCustomerBirthDate().getYear() < 1995) {
                    logger.error("For customer " + customer.getCustomerID() + " birthdate is less than 1995.");
                    jsonDetails.setRecordNumber(Integer.parseInt(customer.getRecordNumber()));
                    jsonDetails.setFileName(fileName);
                    jsonDetails.setErrorDate(LocalDateTime.now());
                    jsonDetails.setErrorCode("5");
                    jsonDetails.setErrorClassificationCode("105");
                    jsonDetails.setErrorDescription("birthdate is less than 1995");
                    ErrortoJSON.ErrortoJSONFile(jsonDetails);
                } else if (customer.getCustomerNationalID().length() != 10) {
                    logger.error("For customer " + customer.getCustomerID() + "NationalID is not valid");
                    jsonDetails.setRecordNumber(Integer.parseInt(customer.getRecordNumber()));
                    jsonDetails.setFileName(fileName);
                    jsonDetails.setErrorDate(LocalDateTime.now());
                    jsonDetails.setErrorCode("6");
                    jsonDetails.setErrorClassificationCode("106");
                    jsonDetails.setErrorDescription("NationalID is not valid");
                    ErrortoJSON.ErrortoJSONFile(jsonDetails);
                } else
                    logger.info("insert into table");
                    InsertCustomerToDB.InsertCustomerToTable(customer);
            }
        } catch (IOException ex) {
            logger.error("this exception has been happened: ", ex);
        }
    }
}

