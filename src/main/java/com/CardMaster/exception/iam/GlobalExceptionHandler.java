package com.CardMaster.exception.iam;


import com.CardMaster.dto.iam.ResponseStructure;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseStructure<String>> handleUserNotFound(UserNotFoundException ex) {
        ResponseStructure<String> res = new ResponseStructure<>();
        res.setMsg("User Not Found");
        res.setData(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ResponseStructure<String>> handleInvalidCredentials(InvalidCredentialsException ex) {
        ResponseStructure<String> res = new ResponseStructure<>();
        res.setMsg("Login Failed");
        res.setData(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseStructure<String>> handleGeneric(Exception ex) {
        ResponseStructure<String> res = new ResponseStructure<>();
        res.setMsg("Internal Server Error");
        res.setData(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
}
