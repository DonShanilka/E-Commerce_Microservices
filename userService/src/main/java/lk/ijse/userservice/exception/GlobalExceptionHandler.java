//package lk.ijse.userservice.exception;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import java.time.LocalDateTime;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(DataPersistException.class)
//    public ResponseEntity<ApiError> handleDataPersistException(DataPersistException ex) {
//
//        ApiError error = new ApiError(
//                LocalDateTime.now(),
//                HttpStatus.CONFLICT.value(),
//                "CONFLICT",
//                ex.getMessage()
//        );
//
//        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiError> handleException(Exception ex) {
//
//        ApiError error = new ApiError(
//                LocalDateTime.now(),
//                HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                "ERROR",
//                ex.getMessage()
//        );
//
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
//    }
//}