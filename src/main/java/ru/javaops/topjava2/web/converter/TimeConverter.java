package ru.javaops.topjava2.web.converter;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
@ConfigurationPropertiesBinding
public class TimeConverter implements Converter<String, LocalTime> {
    @Override
    public LocalTime convert(String from)
    {
        return LocalTime.parse(from, DateTimeFormatter.ISO_LOCAL_TIME);
    }
}
