@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)

public ResponseEntity<ResponseStructure<String>> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND) .body(new ResponseStructure<>("Entity not found", ex.getMessage()));
    }
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ResponseStructure<String>> handleValidation(ValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST) .body(new ResponseStructure<>("Validation error", ex.getMessage()));
    }
    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<ResponseStructure<String>> handleUnauthorized(UnauthorizedActionException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN) .body(new ResponseStructure<>("Unauthorized action", ex.getMessage()));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseStructure<String>> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) .body(new ResponseStructure<>("Server error", ex.getMessage()));
    }
}