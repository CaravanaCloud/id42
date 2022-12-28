package id42;

import java.time.LocalDateTime;

public class Strings {
    public static LocalDateTime parseTime(String time) {
        return LocalDateTime.parse(time);
    }
}
