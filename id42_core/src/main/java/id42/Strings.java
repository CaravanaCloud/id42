package id42;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class Strings {
    static final Logger log = LoggerFactory.getLogger(Strings.class);

    static final DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");
    static final DateTimeFormatter dateShortTimeFmt =  new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(DateTimeFormatter.ISO_LOCAL_DATE)
            .appendLiteral('T')
            .append(timeFmt)
            .toFormatter();

    public static LocalDateTime parseTime(String date, String time) {
        if (date == null
                || time == null
                || date.isBlank()
                || time.isBlank()) {
            return null;
        }
        try {
            var dateTime = date + "T" + time;
            if(time.length() == 5){
                return LocalDateTime.parse(dateTime, dateShortTimeFmt);
            }
            return LocalDateTime.parse(dateTime, timeFmt);
        } catch (DateTimeParseException e) {
            log.warn("Failed to parse date[{}] time[{}]", date, time);
            return null;
        }
    }

    public static String formatTime(LocalDateTime ldt) {
        return ldt.format(timeFmt);
    }
}
