package com.ing.productmng_tool.exception;

import java.time.LocalDateTime;

/**
 * Standardized error response returned by the API.
 *
 * <p>This record defines the structure of all error responses
 * produced by the application.</p>
 *
 * @param timestamp time when the error occurred
 * @param status    HTTP status code
 * @param error     HTTP reason phrase
 * @param message   human-readable error description
 * @param path      request URI where the error occurred
 */
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {}