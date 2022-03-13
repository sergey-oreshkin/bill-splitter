package home.serg.billsplitter.exception;

public class SplitException extends RuntimeException {
    
    public SplitException() {
    }
    
    public SplitException(String message) {
        super(message);
    }
    
    public SplitException(String message, Throwable cause) {
        super(message, cause);
    }
}
