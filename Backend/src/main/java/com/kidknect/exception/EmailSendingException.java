package com.kidknect.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when there is an issue with sending an email.
 * Inherits from {@link GenericException}.
 *
 * @see GenericException
 */
public class EmailSendingException extends GenericException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs an EmailSendingException with the specified field name, HTTP status, and message.
     *
     * @param fieldName The name of the field associated with the exception.
     * @param message   A descriptive message providing more information about the exception.
     */
    public EmailSendingException(String fieldName, String message) {
        super(fieldName, HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}

