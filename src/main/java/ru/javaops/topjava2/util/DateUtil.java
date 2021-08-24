package ru.javaops.topjava2.util;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalTime;

public class DateUtil {

    public static final LocalDate DATE_MIN = LocalDate.of(2000,1,1);
    public static final LocalDate DATE_MAX = LocalDate.of(3000,1,1);

    //public static final LocalTime TIME_LIMIT = LocalTime.of(11,00);

    public static LocalDate startDateUtil(LocalDate startDate){
        if ((startDate == null) || (startDate.compareTo(DATE_MIN)<=0)){
            return DATE_MIN;
        }else {
            return startDate;
        }
    }
    public static LocalDate endDateUtil(LocalDate endDate){
        if ((endDate == null) || (endDate.compareTo(DATE_MIN)<=0)){
            return DATE_MAX;
        }else {
            return endDate;
        }
    }
    public static @Nullable
    LocalTime parseLocalTime(@Nullable String str) {
        return StringUtils.hasLength(str) ? LocalTime.parse(str) : null;
    }
}
