package com.CardMaster.exceptions.cias;

import com.CardMaster.exceptions.cias.EntityNotFoundException;
import com.CardMaster.exceptions.cias.ValidationException;
import com.CardMaster.exceptions.cias.UnauthorizedActionException;
import com.CardMaster.dto.iam.ResponseStructure; // CIAS-specific ResponseStructure
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = {
        "com.CardMaster.service.cias",        // CardIssuance module
        "com.CardMaster.service.accountsetup" // AccountSetup module
})
public class GlobalExceptionHandlerCias {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseStructure<String>> handleNotFound(EntityNotFoundException ex) {
        ResponseStructure<String> res = new ResponseStructure<>();
        res.setMsg("Entity Not Found");
        res.setData(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ResponseStructure<String>> handleValidation(ValidationException ex) {
        ResponseStructure<String> res = new ResponseStructure<>();
        res.setMsg("Validation Error");
        res.setData(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<ResponseStructure<String>> handleUnauthorized(UnauthorizedActionException ex) {
        ResponseStructure<String> res = new ResponseStructure<>();
        res.setMsg("Unauthorized Action");
        res.setData(ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(res);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseStructure<String>> handleGeneric(Exception ex) {
        ResponseStructure<String> res = new ResponseStructure<>();
        res.setMsg("Internal Server Error");
        res.setData(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
}
