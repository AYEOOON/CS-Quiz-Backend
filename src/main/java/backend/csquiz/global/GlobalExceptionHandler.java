package backend.csquiz.global;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 Bad Request 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(IllegalArgumentException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage());
    }

    // 500 Internal Server Error 처리
    public ResponseEntity<Map<String, Object>> handleServerError(Exception ex){
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "서버 내부 오류가 발생하였습니다.");
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus httpStatus, String error, String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", httpStatus.value());
        errorResponse.put("error", error);
        errorResponse.put("message", message);

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}