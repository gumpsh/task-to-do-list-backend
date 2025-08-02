package uk.gov.hmcts.reform.dev.config;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uk.gov.hmcts.reform.dev.error.Error;

import java.util.List;

public class ErrorHandler extends ResponseEntityExceptionHandler {

    private final String ERROR_CONTENT = "Invalid request content";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        List<Error> errors = null;
        Throwable cause = e.getCause();

        if (e.hasErrors()) {
            errors = e.getFieldErrors().stream().map((ex) -> new Error(ex.getField(), ex.getDefaultMessage())).toList();
        }

        return handleInternalException(e, status.value(), ERROR_CONTENT, null, errors, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        List<Error> errors = null;
        Throwable cause = e.getRootCause();

        if (cause instanceof InvalidFormatException fex) {
            var path = String.join(".", fex.getPath().stream().map(JsonMappingException.Reference::getFieldName).toList());
            errors = List.of(new Error(path, fex.getOriginalMessage()));
        }

        return handleInternalException(e, status.value(), ERROR_CONTENT, null, errors, request);
    }

    private ResponseEntity<Object> handleInternalException(Exception e, int status, String detail, String message, List<Error> errors, WebRequest request) {
        var body = createProblemDetail(e, HttpStatusCode.valueOf(status), detail, null, null, request);

        if (message != null) {
            body.setProperty("message", message);
        }

        if (errors != null) {
            body.setProperty("errors", errors);
        }

        return handleExceptionInternal(e, body, null, HttpStatusCode.valueOf(status), request);
    }
}
