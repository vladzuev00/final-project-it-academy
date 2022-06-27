package by.itacademy.zuevvlad.cardpaymentproject.service.calendarhandler;

import by.itacademy.zuevvlad.cardpaymentproject.service.calendarhandler.exception.CalendarHandlingException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class CalendarHandler
{
    private final SimpleDateFormat simpleDateFormat;

    public static CalendarHandler createCalendarHandler()
    {
        if(CalendarHandler.calendarHandler == null)
        {
            synchronized(CalendarHandler.class)
            {
                if(CalendarHandler.calendarHandler == null)
                {
                    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                            CalendarHandler.PATTERN_OF_SIMPLE_DATE_FORMAT);
                    CalendarHandler.calendarHandler = new CalendarHandler(simpleDateFormat);
                }
            }
        }
        return CalendarHandler.calendarHandler;
    }

    private static CalendarHandler calendarHandler = null;
    private static final String PATTERN_OF_SIMPLE_DATE_FORMAT = "dd.MM.yyyy HH:mm:ss";

    private CalendarHandler(final SimpleDateFormat simpleDateFormat)
    {
        super();
        this.simpleDateFormat = simpleDateFormat;
    }

    public final String findDescriptionOfCalendar(final Calendar calendar)
    {
        final Date time = calendar.getTime();
        return this.simpleDateFormat.format(time);
    }

    public final Calendar parseDescriptionOfCalendar(final String parsedDescription)
            throws CalendarHandlingException
    {
        try
        {
            final Date date = this.simpleDateFormat.parse(parsedDescription);
            final long time = date.getTime();
            final Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            return calendar;
        }
        catch(final ParseException cause)
        {
            throw new CalendarHandlingException(cause);
        }
    }
}
