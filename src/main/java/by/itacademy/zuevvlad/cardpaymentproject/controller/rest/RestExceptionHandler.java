package by.itacademy.zuevvlad.cardpaymentproject.controller.rest;

import by.itacademy.zuevvlad.cardpaymentproject.controller.rest.exception.ConstraintException;
import by.itacademy.zuevvlad.cardpaymentproject.service.exception.NoSuchEntityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public final class RestExceptionHandler
{
    public RestExceptionHandler()
    {
        super();
    }

    @ExceptionHandler
    public final ResponseEntity<RestErrorResponse> handleException(final NoSuchEntityException noSuchEntityException)
    {
        final HttpStatus httpStatusOfRestErrorResponse = HttpStatus.NOT_FOUND;
        final String messageOfRestErrorResponse = noSuchEntityException.getMessage();
        final long timeStampOfRestErrorResponse = System.currentTimeMillis();
        final RestErrorResponse restErrorResponse = new RestErrorResponse(httpStatusOfRestErrorResponse,
                messageOfRestErrorResponse, timeStampOfRestErrorResponse);
        return new ResponseEntity<RestErrorResponse>(restErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public final ResponseEntity<RestErrorResponse> handleException(
            final ConstraintException constraintException)
    {
        final HttpStatus httpStatusOfRestErrorResponse = HttpStatus.NOT_ACCEPTABLE;

        final Errors errors = constraintException.getErrors();
        final String messageOfRestErrorResponse = errors == null ? constraintException.getMessage()
                : this.findMessageOfRestErrorResponse(errors);

        final long timeStampOfRestErrorResponse = System.currentTimeMillis();
        final RestErrorResponse restErrorResponse = new RestErrorResponse(httpStatusOfRestErrorResponse,
                messageOfRestErrorResponse, timeStampOfRestErrorResponse);
        return new ResponseEntity<RestErrorResponse>(restErrorResponse, HttpStatus.NOT_ACCEPTABLE);
    }

    private String findMessageOfRestErrorResponse(final Errors errors)
    {
        return errors.getFieldErrors().stream().map((final FieldError fieldError) ->
        {
            final String nameOfField = fieldError.getField();
            final String messageOfError = fieldError.getDefaultMessage();
            final StringBuilder descriptionOfError = new StringBuilder();
            descriptionOfError.append(nameOfField);
            descriptionOfError.append(RestExceptionHandler.SEPARATOR_OF_NAME_OF_FIELD_AND_MESSAGE_OF_ERROR);
            descriptionOfError.append(messageOfError);
            return descriptionOfError;
        }).reduce(new StringBuilder(), (final StringBuilder accumulator, final StringBuilder descriptionOfError) ->
        {
            accumulator.append(descriptionOfError);
            accumulator.append(RestExceptionHandler.SEPARATOR_OF_DESCRIPTIONS_OF_ERRORS);
            return accumulator;
        }).toString();  //TODO: убрать последнее ', '
    }

    private static final String SEPARATOR_OF_NAME_OF_FIELD_AND_MESSAGE_OF_ERROR = " : ";
    private static final String SEPARATOR_OF_DESCRIPTIONS_OF_ERRORS = ", ";

    @ExceptionHandler
    public final ResponseEntity<RestErrorResponse> handleException(final Exception exception)
    {
        final HttpStatus httpStatusOfRestErrorResponse = HttpStatus.BAD_REQUEST;
        final String messageOfRestErrorResponse = exception.getMessage();
        final long timeStampOfRestErrorResponse = System.currentTimeMillis();
        final RestErrorResponse restErrorResponse = new RestErrorResponse(httpStatusOfRestErrorResponse,
                messageOfRestErrorResponse, timeStampOfRestErrorResponse);
        return new ResponseEntity<RestErrorResponse>(restErrorResponse, HttpStatus.BAD_REQUEST);
    }
}
