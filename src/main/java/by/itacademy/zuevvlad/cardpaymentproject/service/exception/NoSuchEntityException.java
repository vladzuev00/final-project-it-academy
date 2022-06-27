package by.itacademy.zuevvlad.cardpaymentproject.service.exception;

public final class NoSuchEntityException extends ServiceException
{
    public NoSuchEntityException()
    {
        super();
    }

    public NoSuchEntityException(final String description)
    {
        super(description);
    }

    public NoSuchEntityException(final Exception cause)
    {
        super(cause);
    }

    public NoSuchEntityException(final String description, final Exception cause)
    {
        super(description, cause);
    }
}

