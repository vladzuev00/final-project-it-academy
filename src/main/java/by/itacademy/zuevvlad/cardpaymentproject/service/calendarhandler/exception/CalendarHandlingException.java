package by.itacademy.zuevvlad.cardpaymentproject.service.calendarhandler.exception;

public final class CalendarHandlingException extends Exception
{
    public CalendarHandlingException()
    {
        super();
    }

    public CalendarHandlingException(final String description)
    {
        super(description);
    }

    public CalendarHandlingException(final Exception cause)
    {
        super(cause);
    }

    public CalendarHandlingException(final String description, final Exception cause)
    {
        super(description, cause);
    }
}
