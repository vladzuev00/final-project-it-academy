package by.itacademy.zuevvlad.cardpaymentproject.dto.modifier.exception;

public final class UpdatingEntityByDTOException extends Exception
{
    public UpdatingEntityByDTOException()
    {
        super();
    }

    public UpdatingEntityByDTOException(final String description)
    {
        super(description);
    }

    public UpdatingEntityByDTOException(final Exception cause)
    {
        super(cause);
    }

    public UpdatingEntityByDTOException(final String description, final Exception cause)
    {
        super(description, cause);
    }
}
