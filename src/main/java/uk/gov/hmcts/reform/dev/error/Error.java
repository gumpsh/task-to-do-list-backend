package uk.gov.hmcts.reform.dev.error;

import lombok.Data;

@Data
public class Error {
    private final String location;
    private final String message;
}
