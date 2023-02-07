package id42;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.Month;

@QuarkusTest
class StringsTest {

    @Test
    void testParseTimeShort() {
        var ldt = Strings.parseTime("2020-12-30", "03:33");
        assertNotNull(ldt);
        assertEquals(2020, ldt.getYear());
        assertEquals(12, ldt.getMonthValue());
        assertEquals(30, ldt.getDayOfMonth());
        assertEquals(3, ldt.getHour());
        assertEquals(33, ldt.getMinute());
    }

    @Test//test if the parameter is null
    public void formatTimeNullTest(){
        try{
            String formatTime = Strings.formatTime(null);
        }
        catch(NullPointerException e){
            
        }
        
        // assertThrows(NullPointerException.class, () -> {
        //     String formatTime = Strings.formatTime(null);
        // });
    }

    @Test
    public void formatTimeTest(){
        LocalDateTime localDateTime = LocalDateTime.of(2023, Month.JANUARY, 23, 14, 26);
        String formatTime = Strings.formatTime(localDateTime);
        assertEquals("14:26", formatTime);
    }

    @Test
    public void parseTimeDateNullTest(){
        var localDateTime = Strings.parseTime(null, "13:23");
        assertEquals(null, localDateTime);
    }

    @Test
    public void parseTime_TimeNullTest(){
        var localDateTime = Strings.parseTime("2023-01-23", null);
        assertEquals(null, localDateTime);
    }
}