package lk.ijse.aiservice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@RestControllerAdvice
public class AiExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(AiExceptionHandler.class);

    public record ApiError(Instant timestamp, int status, String error, String message, String path) {
    }

    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<ApiError> handleStatus(ResponseStatusException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        return response(status, ex.getReason() == null ? "Request failed." : ex.getReason(), request);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, HandlerMethodValidationException.class, ConstraintViolationException.class, HttpMessageNotReadableException.class})
    ResponseEntity<ApiError> handleValidation(Exception ex, HttpServletRequest request) {
        return response(HttpStatus.BAD_REQUEST, "Request validation failed. Check required fields and value limits.", request);
    }

    @ExceptionHandler(ErrorResponseException.class)
    ResponseEntity<ApiError> handleFrameworkError(ErrorResponseException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        return response(status, "Request could not be processed.", request);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiError> handleUnexpected(Exception ex, HttpServletRequest request) {
        log.error("Unhandled AI service error for {}", request.getRequestURI(), ex);
        return response(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.", request);
    }

    private ResponseEntity<ApiError> response(HttpStatus status, String message, HttpServletRequest request) {
        return ResponseEntity.status(status).body(new ApiError(Instant.now(), status.value(), status.getReasonPhrase(), message, request.getRequestURI()));
    }
}
