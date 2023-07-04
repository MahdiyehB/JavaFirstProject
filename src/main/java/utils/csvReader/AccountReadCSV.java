package utils.csvReader;

import domain.Account;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import usecases.InsertAccountToDB;
import utils.jsonUtil.ErrortoJSON;
import utils.jsonUtil.JSONDetails;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * To read data from Accounts.csv files
 * @author Mahdiyeh Biglari
 */
public class AccountReadCSV {
    private static String fileName = "Accounts.csv";
    static final Logger logger = Logger.getLogger(AccountReadCSV.class);

    /**
     * Method for reading data from customer.csv file
     * implemented thread with ExecutorService
     */
    public static void ReadFile(){
        try {
            Reader reader = Files.newBufferedReader(Paths.get(fileName));
            logger.info("Reading file Accounts.csv");
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

            logger.info("Reading .csv file");
            List<CSVRecord> csvRecords = csvParser.getRecords();

            logger.info("creating a JobDataMap1 to store the data");
            JobDataMap jobDataMap1 = new JobDataMap();
            jobDataMap1.put("data", csvRecords.subList(0,3));

            logger.info("creating a JobDataMap2 to store the data");
            JobDataMap jobDataMap2 = new JobDataMap();
            jobDataMap2.put("data", csvRecords.subList(3,5));

            logger.info("create the first job and pass the data");
            JobDetail job1 = JobBuilder.newJob(AccountReadJob1.class)
                    .withIdentity("job1", "group2")
                    .usingJobData(jobDataMap1)
                    .build();

            logger.info("create the second job and pass the data");
            JobDetail job2 = JobBuilder.newJob(AccountReadJob2.class)
                    .withIdentity("job2", "group2")
                    .usingJobData(jobDataMap2)
                    .build();

            logger.info("scheduling the first Job");
            Trigger trigger1 = TriggerBuilder.newTrigger()
                    .withIdentity("cronTrigger1", "group2")
                    .withSchedule(CronScheduleBuilder
                            .cronSchedule("0/10 * * * * ?")).build();

            logger.info("scheduling the second Job");
            Trigger trigger2 = TriggerBuilder.newTrigger()
                    .withIdentity("cronTrigger2", "group2")
                    .withSchedule(CronScheduleBuilder
                            .cronSchedule("0/10 * * * * ?")).build();

            logger.info("Execute Jobs");
            Scheduler scheduler =
                    new StdSchedulerFactory().getScheduler();
            scheduler.start();
            scheduler.scheduleJob(job1, trigger1);
            scheduler.scheduleJob(job2, trigger2);


//        boolean terminated = scheduler.isShutdown();
//        if (!terminated) scheduler.shutdown();

        } catch (IOException ex) {
            logger.error("this exception has been happened: ", ex);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This Method is for validating each record and put it into DB if it was valid,
     * otherwise add it into ErrorJSONFile
     * @param records
     */
    public static void AccountProcessFile(Iterable<CSVRecord> records){

        logger.info("inside AccountProcessFile");
        try {
            logger.info("iterate over the records and process them");
            for (CSVRecord record : records) {
                logger.info("Assigning values to the variables and check validation.");
                String accountOpenDateV = record.get("ACCOUNT_OPEN_DATE");
                Account account = new Account(record.get("RECORD_NUMBER")
                        , record.get("ACCOUNT_NUMBER")
                        , Integer.parseInt(record.get("ACCOUNT_TYPE"))
                        , Integer.parseInt(record.get("ACCOUNT_CUSTOMER_ID"))
                        , Long.parseLong(record.get("ACCOUNT_LIMIT"))
                        , LocalDateTime.parse(accountOpenDateV, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                        , Long.parseLong(record.get("ACCOUNT_BALANCE")));


                JSONDetails jsonDetails = new JSONDetails();

                logger.info("Validate accounts");

                logger.info("to check if the accountCusotmerID exists in Customer table");
                int count = MatchAccountToCustomer.checkAccountToCustomer(account.getAccountCustomerID());

                if (count == 0) {
                    logger.error("There is no customer with this ID: " + account.getAccountCustomerID());
                    jsonDetails.setRecordNumber(Integer.parseInt(account.getRecordNumber()));
                    jsonDetails.setFileName(fileName);
                    jsonDetails.setErrorDate(LocalDateTime.now());
                    jsonDetails.setErrorCode("0");
                    jsonDetails.setErrorClassificationCode("100");
                    jsonDetails.setErrorDescription("There is no customer with this ID");
                    ErrortoJSON.ErrortoJSONFile(jsonDetails);

                } else if (account.getAccountBalance() > account.getAccountLimit()) {
                    logger.error("For customer " + account.getAccountCustomerID() + " AccountBalance is higher than Accountlimit");
                    jsonDetails.setRecordNumber(Integer.parseInt(account.getRecordNumber()));
                    jsonDetails.setFileName(fileName);
                    jsonDetails.setErrorDate(LocalDateTime.now());
                    jsonDetails.setErrorCode("1");
                    jsonDetails.setErrorClassificationCode("101");
                    jsonDetails.setErrorDescription("AccountBalance is higher than AccountLimit");
                    ErrortoJSON.ErrortoJSONFile(jsonDetails);

                } else if (account.getAccountType() != 1 && account.getAccountType() != 2 && account.getAccountType() != 3) {
                    logger.error("For customer " + account.getAccountCustomerID() + " AccountType value is not valid");
                    jsonDetails.setRecordNumber(Integer.parseInt(account.getRecordNumber()));
                    jsonDetails.setFileName(fileName);
                    jsonDetails.setErrorDate(LocalDateTime.now());
                    jsonDetails.setErrorCode("2");
                    jsonDetails.setErrorClassificationCode("102");
                    jsonDetails.setErrorDescription("AccountType value is not valid");
                    ErrortoJSON.ErrortoJSONFile(jsonDetails);

                } else{
                    logger.info("insert into table");
                    InsertAccountToDB.InsertAccountToTable(account);
                }
            }
        }catch (IOException | SQLException e) {
            logger.error("this exception has been happened: "+e.toString());
            throw new RuntimeException(e);
        }

    }


}
