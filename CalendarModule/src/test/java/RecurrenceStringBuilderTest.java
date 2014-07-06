import android.test.InstrumentationTestCase;


import java.util.Calendar;
import java.util.GregorianCalendar;

import fr.unice.polytech.calendarmodule.IndexDayOutOfBoundException;
import fr.unice.polytech.calendarmodule.IndexMonthOutOfBoundException;
import fr.unice.polytech.calendarmodule.RecurrenceStringBuilder;

/**
 * Created by Yoann on 11/06/2014.
 */
public class RecurrenceStringBuilderTest extends InstrumentationTestCase {
    public void testRecurrenceBuilder(){
        GregorianCalendar g1 = new GregorianCalendar(1997,11,24,0,0,0);
        GregorianCalendar g2 = new GregorianCalendar(1997,11,24,14,0,0);


        RecurrenceStringBuilder rb1 = new RecurrenceStringBuilder().freqByDay().repetition(10);

        RecurrenceStringBuilder rb2 = new RecurrenceStringBuilder().freqByDay().until(g1);

        RecurrenceStringBuilder rb3 = new RecurrenceStringBuilder().freqByDay().interval(2);

        RecurrenceStringBuilder rb4 = new RecurrenceStringBuilder().freqByDay().interval(2).repetition(5);

        RecurrenceStringBuilder rb5=null;
        try {
            rb5 = new RecurrenceStringBuilder().freqByYear().until(g2).byMonth(Calendar.JANUARY)
                    .byDay(Calendar.SUNDAY).byDay(Calendar.MONDAY)
                    .byDay(Calendar.TUESDAY).byDay(Calendar.WEDNESDAY)
                    .byDay(Calendar.THURSDAY)
                    .byDay(Calendar.FRIDAY).byDay(Calendar.SATURDAY);
        } catch (IndexDayOutOfBoundException e) {
            e.printStackTrace();
        } catch (IndexMonthOutOfBoundException e) {
            e.printStackTrace();
        }

        RecurrenceStringBuilder rb6 = null;
        try {
            rb6 = new RecurrenceStringBuilder().freqByDay().until(g2).byMonth(Calendar.JANUARY);
        } catch (IndexMonthOutOfBoundException e) {
            e.printStackTrace();
        }

        RecurrenceStringBuilder rb7 = new RecurrenceStringBuilder().freqByWeek().repetition(10);

        RecurrenceStringBuilder rb8 = new RecurrenceStringBuilder().freqByWeek().until(g1);

        RecurrenceStringBuilder rb9 = null;
        try {
            rb9 = new RecurrenceStringBuilder().freqByDay().interval(2).weekStart(Calendar.SUNDAY);
        } catch (IndexDayOutOfBoundException e) {
            e.printStackTrace();
        }

        RecurrenceStringBuilder rb10 = null;
        try {
            rb10 = new RecurrenceStringBuilder().freqByWeek().until(g1)
                    .byDay(Calendar.TUESDAY).byDay(Calendar.THURSDAY);
        } catch (IndexDayOutOfBoundException e) {
            e.printStackTrace();
        }

        RecurrenceStringBuilder rb11 = null;
        try {
            rb11 = new RecurrenceStringBuilder().freqByWeek().repetition(10).weekStart(Calendar.SUNDAY)
                    .byDay(Calendar.TUESDAY).byDay(Calendar.THURSDAY);
        } catch (IndexDayOutOfBoundException e) {
            e.printStackTrace();
        }

        RecurrenceStringBuilder rb12 = null;
        try {
            rb12 = new RecurrenceStringBuilder().freqByWeek().interval(2).until(g1)
                    .weekStart(Calendar.SUNDAY)
                    .byDay(Calendar.MONDAY).byDay(Calendar.WEDNESDAY)
                    .byDay(Calendar.FRIDAY);
        } catch (IndexDayOutOfBoundException e) {
            e.printStackTrace();
        }
        RecurrenceStringBuilder rb13 = new RecurrenceStringBuilder();
        rb13.freqByDay().byHour(9).byHour(10).byHour(11).byHour(12)
                .byHour(13).byHour(14).byHour(15).byHour(16)
                .byMinute(0).byMinute(20).byMinute(40);

        RecurrenceStringBuilder rb14 = new RecurrenceStringBuilder();
        rb14.freqByMinute().interval(20).byHour(9).byHour(10).byHour(11)
                .byHour(12).byHour(13).byHour(14).byHour(15).byHour(16);

        assertEquals("COUNT=10;FREQ=DAILY", rb1.getRRule());
        assertEquals("FREQ=DAILY;UNTIL=19971224T000000Z", rb2.getRRule());
        assertEquals("INTERVAL=2;FREQ=DAILY", rb3.getRRule());
        assertEquals("COUNT=5;INTERVAL=2;FREQ=DAILY", rb4.getRRule());
        assertEquals("FREQ=YEARLY;BYMONTH=01;UNTIL=19971224T140000Z;BYDAY=SU,MO,TU,WE,TH,FR,SA", rb5.getRRule());
        assertEquals("FREQ=DAILY;BYMONTH=01;UNTIL=19971224T140000Z", rb6.getRRule());
        assertEquals("COUNT=10;FREQ=WEEKLY", rb7.getRRule());
        assertEquals("FREQ=WEEKLY;UNTIL=19971224T000000Z", rb8.getRRule());
        assertEquals("INTERVAL=2;FREQ=DAILY;WKST=SU", rb9.getRRule());
        assertEquals("FREQ=WEEKLY;UNTIL=19971224T000000Z;BYDAY=TU,TH", rb10.getRRule());
        assertEquals("COUNT=10;FREQ=WEEKLY;WKST=SU;BYDAY=TU,TH", rb11.getRRule());
        assertEquals("WKST=SU;UNTIL=19971224T000000Z;BYDAY=MO,WE,FR;INTERVAL=2;FREQ=WEEKLY", rb12.getRRule());
        assertEquals("BYHOUR=9,10,11,12,13,14,15,16;FREQ=DAILY;BYMINUTE=0,20,40",rb13.getRRule());
        assertEquals("BYHOUR=9,10,11,12,13,14,15,16;INTERVAL=20;FREQ=MINUTELY",rb14.getRRule());
    }

    public void testOthersSamples(){
        RecurrenceStringBuilder rb1 = new RecurrenceStringBuilder();
        rb1.freqByMonth();
        assertEquals("FREQ=MONTHLY",rb1.getRRule());

        RecurrenceStringBuilder rb2 = new RecurrenceStringBuilder();
        rb2.freqByMinute();
        assertEquals("FREQ=MINUTELY",rb2.getRRule());

        RecurrenceStringBuilder rb3 = new RecurrenceStringBuilder();
        rb3.freqByHour();
        assertEquals("FREQ=HOURLY",rb3.getRRule());

        RecurrenceStringBuilder rb4 = new RecurrenceStringBuilder();
        rb4.byMonthDay(10);
        assertEquals("BYMONTHDAY=10",rb4.getRRule());

        RecurrenceStringBuilder rb5 = new RecurrenceStringBuilder();
        rb5.byYearDay(30);
        assertEquals("BYYEARDAY=30",rb5.getRRule());

        RecurrenceStringBuilder rb6 = new RecurrenceStringBuilder();
        rb6.byWeekNumber(30);
        assertEquals("BYWEEKNO=30",rb6.getRRule());

    }

    public void testGetterSetter(){
        RecurrenceStringBuilder rb = new RecurrenceStringBuilder();
        rb.repetition(5);
        RecurrenceStringBuilder rbT = new RecurrenceStringBuilder();
        rbT.setRruleArgs(rb.getRruleArgs());

        assertEquals(rb.getRruleArgs(),rbT.getRruleArgs());
    }

    //@Test(expected = IndexDayOutOfBoundException.class)
    public void testIndexDayOutOfBoundException(){
        RecurrenceStringBuilder rb = new RecurrenceStringBuilder();
        boolean exception = false;
        try {
            rb.byDay(Calendar.SUNDAY+10);
        } catch (IndexDayOutOfBoundException e) {
            exception = true;
        }
        assertTrue(exception);
    }
    //@Test(expected = IndexMonthOutOfBoundException.class)
    public void testIndexMonthOutOfBoundException(){
        RecurrenceStringBuilder rb = new RecurrenceStringBuilder();
        boolean exception  = false;
        try {
            rb.byMonth(12);
        } catch (IndexMonthOutOfBoundException e) {
            exception = true;
        }
        assertTrue(exception);
    }

    public void testTimeToDuration(){
        GregorianCalendar g1 = new GregorianCalendar(1997,11,24,0,0,0);
        GregorianCalendar g2 = new GregorianCalendar(1997,11,24,2,14,30);
        GregorianCalendar g3 = new GregorianCalendar(1997,11,24,12,4,3);

        assertEquals("PT02H14M30S",RecurrenceStringBuilder.timeToDuration(g2.getTimeInMillis()-g1.getTimeInMillis()));
        assertEquals("PT12H04M03S",RecurrenceStringBuilder.timeToDuration(g3.getTimeInMillis()-g1.getTimeInMillis()));
    }

}
