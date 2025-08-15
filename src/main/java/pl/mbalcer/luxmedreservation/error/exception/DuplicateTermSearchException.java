package pl.mbalcer.luxmedreservation.error.exception;

public class DuplicateTermSearchException extends RuntimeException {
    public DuplicateTermSearchException() {
        super("Such search already exists for this user");
    }

    public DuplicateTermSearchException(String message) {
        super(message);
    }
}
