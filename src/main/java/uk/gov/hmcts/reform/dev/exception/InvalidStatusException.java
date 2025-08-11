package uk.gov.hmcts.reform.dev.exception;

public class InvalidStatusException extends RuntimeException {

    public InvalidStatusException(String message) {
        super(message);
    }
}
