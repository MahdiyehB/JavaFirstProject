import org.apache.log4j.Logger;
import utils.csvReader.AccountReadCSV;
import utils.csvReader.CustomerReadCSV;

/**
 * Java Course first project to read data from CSV files and insert them in DB and log the process and put errors in JSON files
 * @author Mahdiyeh Biglari
 * @date 2023/06/25
 */
public class Main {

    static final Logger logger = Logger.getLogger(Main.class);
    /**
     * The main class which is running methods to read CSV files and insert them to database.
     * @param args
     * @throws Exception
     */

    public static void main(String[] args) throws Exception {

        logger.info("read customer.csv file");
        CustomerReadCSV.ReadFile();

        logger.info("read account.csv file");
        AccountReadCSV.ReadFile();
    }
}