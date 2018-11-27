package ua.nure.vraniuk.usermanagement;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class UserTest{

    private static final Long ID = 1L;
    private static final String MY_FIRST_NAME = "Vladyslav";
    private static final String MY_LAST_NAME  = "Raniuk";
    private static final String MY_DATE_BIRTH_STRING = "29-Jan-1999";
    private static final String MY_DATE_PATTERN = "dd-MMM-yyyy";
    private static int MY_BIRTH_YEAR = 1999;
    private static int MY_BIRTH_DAY = 26;
    private static Date myDate;
    private User user;
    private Calendar calendar;

    @Before
    public void setUp() throws Exception {
        myDate = new SimpleDateFormat(MY_DATE_PATTERN).parse(MY_DATE_BIRTH_STRING);
        user = new User(ID,MY_FIRST_NAME, MY_LAST_NAME, myDate);
        calendar = Calendar.getInstance();
    }

    @Test
    public void testAgeNow() {
        int ageExpected = 19;

        calendar.set(MY_BIRTH_YEAR,calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

        user.setDateOfBirth(calendar.getTime());

        int ageActual = user.getAge();

        assertEquals(ageExpected, ageActual);
    }

    @Test
    public void testAgeBirthdayOneDayAfterToday() {
        int ageExpected = 18;

        calendar.set(MY_BIRTH_YEAR,calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        user.setDateOfBirth(calendar.getTime());

        int ageActual = user.getAge();
        assertEquals(ageExpected, ageActual);
    }

    @Test
    public void testAgeOneMonthAheadFromCurrentMonth() {
        int ageExpected = 18;

        calendar.set(MY_BIRTH_YEAR,calendar.get(Calendar.MONTH),MY_BIRTH_DAY);
        calendar.add(Calendar.MONTH, 1);

        user.setDateOfBirth(calendar.getTime());

        int ageActual = user.getAge();
        assertEquals(ageExpected, ageActual);
    }

    @Test
    public void testAgeOneYearBeforeCurrentMonth() {
        int ageExpected = 20;

        calendar.set(MY_BIRTH_YEAR,calendar.get(Calendar.MONTH),MY_BIRTH_DAY);
        calendar.add(Calendar.YEAR, -1);

        user.setDateOfBirth(calendar.getTime());

        int ageActual = user.getAge();
        assertEquals(ageExpected, ageActual);
    }

    @Test
    public void testNewbornAge() {
        int ageExpected = 0;

        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

        user.setDateOfBirth(calendar.getTime());

        int ageActual = user.getAge();
        assertEquals(ageExpected, ageActual);
    }


    @Test
    public void testFullName() {
        String resultExpected = MY_LAST_NAME + ", " + MY_FIRST_NAME;
        assertEquals(resultExpected,user.getFullName());
    }
}