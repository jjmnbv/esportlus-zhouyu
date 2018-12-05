package com.kaihei.esportingplus.payment.config.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author: tangtao
 **/
public class StringToDateTimeConverter implements Converter<String, LocalDateTime> {

    private static final String DATE_FORMATTER = "yyyy-MM-dd HH:mm:ss";

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);

    @Override
    public LocalDateTime convert(String source) {
        return LocalDateTime.parse(source, dateTimeFormatter);
    }

}
