package pl.mbalcer.luxmedreservation.error.exception;

public class InvalidDateRangeException extends RuntimeException {
    public InvalidDateRangeException() {
        super("dateTo cannot be before dateFrom");
    }

    public InvalidDateRangeException(String message) {
        super(message);
    }
}
