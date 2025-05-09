package com.bsuir.aviatours.service.business.adapter;

import org.springframework.stereotype.Component;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class IsoDateTimeAdapter implements DateTimeAdapter {
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter HTML_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private static final DateTimeFormatter HTML_FORMATTER_WITH_SECONDS = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final DateTimeFormatter SQL_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final ZoneId ZONE_ID = ZoneId.systemDefault();

    @Override
    public Instant parse(String dateTime) {
        if (dateTime == null) return null;

        try {
            return LocalDateTime.parse(dateTime, ISO_FORMATTER)
                    .atZone(ZONE_ID)
                    .toInstant();
        } catch (DateTimeParseException e1) {
            try {
                return LocalDateTime.parse(dateTime, HTML_FORMATTER)
                        .atZone(ZONE_ID)
                        .toInstant();
            } catch (DateTimeParseException e2) {
                return LocalDateTime.parse(dateTime, SQL_FORMATTER)
                        .atZone(ZONE_ID)
                        .toInstant();
            }
        }
    }

    @Override
    public String format(Instant instant) {
        if (instant == null) return null;
        return ISO_FORMATTER.format(instant.atZone(ZONE_ID));
    }

    @Override
    public String toFrontendFormat(Instant instant) {
        if (instant == null) return null;
        return HTML_FORMATTER.format(instant.atZone(ZONE_ID));
    }

    @Override
    public Instant fromFrontendFormat(String dateTime) {
        if (dateTime == null) return null;
        try {
            return LocalDateTime.parse(dateTime, HTML_FORMATTER_WITH_SECONDS)
                    .atZone(ZONE_ID)
                    .toInstant();
        } catch (DateTimeParseException e) {
            return LocalDateTime.parse(dateTime, HTML_FORMATTER)
                    .atZone(ZONE_ID)
                    .toInstant();
        }
    }
}