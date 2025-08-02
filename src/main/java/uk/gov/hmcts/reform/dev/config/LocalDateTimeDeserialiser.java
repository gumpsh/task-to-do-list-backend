package uk.gov.hmcts.reform.dev.config;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class LocalDateTimeDeserialiser extends JsonDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        String dateTimeString = p.getText();

        try {
            return LocalDateTime.parse(dateTimeString);
        } catch (DateTimeParseException e) {
            throw new InvalidFormatException(p, "Invalid date-time format", dateTimeString, LocalDateTime.class);
        }
    }
}
