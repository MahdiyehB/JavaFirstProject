package utils.csvReader;

import domain.Customer;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * implements the first Job for reading Customer.csv file
 * @author Mahdiyeh Biglari
 */
public class CustomerReadJob1 implements Job {

    static final Logger logger = Logger.getLogger(CustomerReadJob1.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        logger.info("get the data from the JobDataMap");
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        Iterable<CSVRecord> records = (Iterable<CSVRecord>) jobDataMap.get("data");

        logger.info("Validating each record and add to DB or in case of any issue add in JSON file");
        CustomerReadCSV.CustomerProcessFile(records);
    }
}
