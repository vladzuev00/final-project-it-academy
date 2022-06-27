package by.itacademy.zuevvlad.cardpaymentproject.service.exception.changepassword;

public final class WrongOldPasswordException extends PasswordChangingException
{
    public WrongOldPasswordException()
    {
        super();
    }

    public WrongOldPasswordException(final String description)
    {
        super(description);
    }

    public WrongOldPasswordException(final Exception cause)
    {
        super(cause);
    }

    public WrongOldPasswordException(final String description, final Exception cause)
    {
        super(description, cause);
    }
}
