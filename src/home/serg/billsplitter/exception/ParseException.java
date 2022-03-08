package home.serg.billsplitter.exception;

public class ParseException extends RuntimeException {
    public ParseException() {
        super();
    }
    
    public ParseException(String message) {
        super(message);
    }
}
