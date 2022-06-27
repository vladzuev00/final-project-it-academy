package by.itacademy.zuevvlad.cardpaymentproject.controller.logon.exception;

public final class NotBindingUserDataException extends Exception
{
    public NotBindingUserDataException()
    {
        super();
    }

    public NotBindingUserDataException(final String description)
    {
        super(description);
    }

    public NotBindingUserDataException(final Exception cause)
    {
        super(cause);
    }

    public NotBindingUserDataException(final String description, final Exception cause)
    {
        super(description, cause);
    }
}
