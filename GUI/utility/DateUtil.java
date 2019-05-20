package utility;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class DateUtil
{
    public static LocalDate addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        Date d = cal.getTime();
        Instant instant = d.toInstant();
        return instant.atZone(ZoneId.systemDefault()).toLocalDate();
    }
}