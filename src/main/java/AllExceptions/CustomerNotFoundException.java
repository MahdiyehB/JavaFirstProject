package AllExceptions;

import java.text.MessageFormat;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(int Balance){
        super(MessageFormat.format("No customer with this balance", Balance));
    }
}
