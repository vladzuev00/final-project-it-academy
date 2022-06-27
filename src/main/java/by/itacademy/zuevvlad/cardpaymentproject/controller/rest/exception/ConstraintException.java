package by.itacademy.zuevvlad.cardpaymentproject.controller.rest.exception;

import org.springframework.validation.Errors;

public final class ConstraintException extends RestControllerException
{
    private final Errors errors;

    public ConstraintException()
    {
        super();
        this.errors = null;
    }

    public ConstraintException(final String description)
    {
        super(description);
        this.errors = null;
    }

    public ConstraintException(final Exception cause)
    {
        super(cause);
        this.errors = null;
    }

    public ConstraintException(final String description, final Exception cause)
    {
        super(description, cause);
        this.errors = null;
    }

    public ConstraintException(final Errors errors)
    {
        super();
        this.errors = errors;
    }

    public final Errors getErrors()
    {
        return this.errors;
    }
}
