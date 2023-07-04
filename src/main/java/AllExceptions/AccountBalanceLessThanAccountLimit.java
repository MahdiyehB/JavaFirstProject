package AllExceptions;

public class AccountBalanceLessThanAccountLimit extends Exception{
    public AccountBalanceLessThanAccountLimit(String message) {
        super(message);
    }
}
