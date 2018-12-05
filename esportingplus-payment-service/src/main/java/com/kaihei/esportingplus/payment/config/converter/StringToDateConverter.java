package com.kaihei.esportingplus.payment.config.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author: tangtao
 **/
public class StringToDateConverter implements Converter<String, LocalDate> {

    private static final String DATE_FORMATTER = "yyyy-MM-dd";

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);

    @Override
    public LocalDate convert(String source) {
        return LocalDate.parse(source, dateTimeFormatter);
    }

}
