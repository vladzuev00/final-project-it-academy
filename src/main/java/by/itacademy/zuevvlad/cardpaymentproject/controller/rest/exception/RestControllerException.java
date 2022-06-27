package by.itacademy.zuevvlad.cardpaymentproject.controller.rest.exception;

public class RestControllerException extends Exception
{
    public RestControllerException()
    {
        super();
    }

    public RestControllerException(final String description)
    {
        super(description);
    }

    public RestControllerException(final Exception cause)
    {
        super(cause);
    }

    public RestControllerException(final String description, final Exception cause)
    {
        super(description, cause);
    }
}
