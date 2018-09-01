package com.example.demo.handlers;

import com.example.demo.dto.TextResponse;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.EmailExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.sql.SQLException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private TextResponse textResponse;

    @PostConstruct
    public void onInit() {
        this.textResponse = new TextResponse();
    }

    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<TextResponse> handleEmailExistsException() {
        textResponse.setMessage("Email address already in use");
        return new ResponseEntity<>(textResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<TextResponse> handleBadRequestException() {
        textResponse.setMessage("Bad request");
        return new ResponseEntity<>(textResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<TextResponse> handleBadCredentialsException() {
        textResponse.setMessage("Invalid email or password");
        return new ResponseEntity<>(textResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({IOException.class, SQLException.class})
    public ResponseEntity<TextResponse> handleImagesavingExceptions() {
        textResponse.setMessage("Image saving failed");
        return new ResponseEntity<>(textResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
