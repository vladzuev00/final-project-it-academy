package by.itacademy.zuevvlad.cardpaymentproject.controller.rest;

import org.springframework.http.HttpStatus;

import java.util.Objects;

public final class RestErrorResponse
{
    private final HttpStatus httpStatus;
    private final String message;
    private final long timeStamp;

    public RestErrorResponse(final HttpStatus httpStatus, final String message, final long timeStamp)
    {
        super();
        this.httpStatus = httpStatus;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public final HttpStatus getHttpStatus()
    {
        return this.httpStatus;
    }

    public final String getMessage()
    {
        return this.message;
    }

    public final long getTimeStamp()
    {
        return this.timeStamp;
    }

    @Override
    public final boolean equals(final Object otherObject)
    {
        if(this == otherObject)
        {
            return true;
        }
        if(otherObject == null)
        {
            return false;
        }
        if(this.getClass() != otherObject.getClass())
        {
            return false;
        }
        final RestErrorResponse other = (RestErrorResponse)otherObject;
        return     this.httpStatus == other.httpStatus
                && Objects.equals(this.message, other.message)
                && this.timeStamp == other.timeStamp;
    }

    @Override
    public final int hashCode()
    {
        return Objects.hash(this.httpStatus, this.message, this.timeStamp);
    }

    @Override
    public final String toString()
    {
        return this.getClass().getName() + "[httpStatus = " + this.httpStatus + ", message = " + this.message
                + ", timeStamp = " + this.timeStamp + "]";
    }
}
