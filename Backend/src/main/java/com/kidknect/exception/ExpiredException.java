package com.kidknect.exception;

import org.springframework.http.HttpStatus;


/**
 * Exception thrown when an operation is attempted on an expired entity.
 * Inherits from {@link GenericException}.
 *
 * @see GenericException
 */
public class ExpiredException extends GenericException {

    private static final long serialVersionUID = 1L;
    private static final HttpStatus httpStatusCode = HttpStatus.CONFLICT;

    /**
     * Constructs an ExpiredException with the specified field name and message.
     *
     * @param fieldName The name of the field associated with the exception.
     * @param message   A descriptive message providing more information about the exception.
     */
    public ExpiredException(final String fieldName, final String message) {
        super(fieldName, httpStatusCode, String.format("%s", message));
    }
}

