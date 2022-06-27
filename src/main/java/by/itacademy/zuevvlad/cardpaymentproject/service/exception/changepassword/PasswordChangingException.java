package by.itacademy.zuevvlad.cardpaymentproject.service.exception.changepassword;

public class PasswordChangingException extends Exception
{
    public PasswordChangingException()
    {
        super();
    }

    public PasswordChangingException(final String description)
    {
        super(description);
    }

    public PasswordChangingException(final Exception cause)
    {
        super(cause);
    }

    public PasswordChangingException(final String description, final Exception cause)
    {
        super(description, cause);
    }
}
