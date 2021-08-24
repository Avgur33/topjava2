package ru.javaops.topjava2.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
//https://stackoverflow.com/questions/40080999/custom-type-conversion-of-property-values
@Component
@ConfigurationProperties(prefix = "limittime")
public class TimeConfig {
    private LocalTime time;

    public LocalTime getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = LocalTime.ofSecondOfDay(time);
    }
}
