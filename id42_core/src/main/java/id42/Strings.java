package id42;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Strings {
    static final DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");
    public static LocalDateTime parseTime(String date, String time) {
        return LocalDateTime.parse(date + "T" + time);
    }

    public static String formatTime(LocalDateTime ldt) {
        return ldt.format(timeFmt);
    }
}
