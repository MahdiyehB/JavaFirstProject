package utils.jsonUtil;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Customer;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Class to insert records which are not matched with the conditions to the JSON file.
 */
public class ErrortoJSON {

    static final Logger logger = Logger.getLogger(ErrortoJSON.class);
    private static ObjectMapper mapper = new ObjectMapper();

    private static final File file = new File("ErrorJSONFile.json");

    private static JsonGenerator g;

    static {
        try {
            g = new ObjectMapper().getFactory().createGenerator(new FileOutputStream(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    ;


    /**
     * Writes invalid records into the JSON file
     * @param obj
     * @throws IOException
     */
    public static void ErrortoJSONFile(Object obj) throws IOException {

        mapper.writeValue(g, obj);

    }

}
