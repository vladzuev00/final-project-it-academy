package by.itacademy.zuevvlad.cardpaymentproject.service.exception.changepassword;

public final class NewPasswordAndItsRepetitionNotEqualException extends PasswordChangingException
{
    public NewPasswordAndItsRepetitionNotEqualException()
    {
        super();
    }

    public NewPasswordAndItsRepetitionNotEqualException(final String description)
    {
        super(description);
    }

    public NewPasswordAndItsRepetitionNotEqualException(final Exception cause)
    {
        super(cause);
    }

    public NewPasswordAndItsRepetitionNotEqualException(final String description, final Exception cause)
    {
        super(description, cause);
    }
}
