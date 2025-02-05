package pedro.bibliotech.app.Exceptions;

public class LateLoanException extends RuntimeException {

    public LateLoanException(String message) {
        super(message);
    }
}
