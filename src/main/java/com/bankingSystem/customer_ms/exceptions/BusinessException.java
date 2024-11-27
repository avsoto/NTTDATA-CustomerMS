package com.bankingSystem.customer_ms.exceptions;

/**
 * Custom exception class for handling business logic errors in the application.
 * <p>
 * This exception is a subclass of {@link RuntimeException}, allowing it to be thrown
 * during the runtime of the application. It is used to represent errors that occur due to
 * business logic violations, such as invalid data or operational constraints.
 * </p>
 */
public class BusinessException extends RuntimeException{

    /**
     * Constructs a new {@link BusinessException} with the specified detail message.
     *
     * @param message the detail message which will be saved for later retrieval by the {@link Throwable#getMessage()} method.
     */
    public BusinessException(String message) {
        super(message);
    }

}
