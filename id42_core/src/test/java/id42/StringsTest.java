package id42;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
}