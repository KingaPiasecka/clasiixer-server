package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.exceptions.EmailExistsException;
import com.example.demo.providers.JwtTokenProvider;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @PostMapping(path = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<TextResponse> registerUser(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) throws EmailExistsException {
        TextResponse textResponse = new TextResponse();
        if (bindingResult.hasErrors()) {
            textResponse.setMessage("Bad request");
            return new ResponseEntity<>(textResponse, HttpStatus.BAD_REQUEST);
        }
        userService.createUser(userDTO);
        textResponse.setMessage("Registration successful");
        return new ResponseEntity<>(textResponse, HttpStatus.OK);
    }

    @PostMapping(path = "/login")
    public ResponseEntity loginUser(@RequestBody AuthUser authUser) {
        final Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authUser.getEmail(), authUser.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtTokenProvider.generateToken(authenticate);
        AuthToken authToken = new AuthToken();
        authToken.setToken(token);
        return ResponseEntity.ok(authToken);
    }

    @GetMapping(path =  "/currentUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CurrentUser> getCurrentUser() {
        CurrentUser currentUser = userService.getCurrentUser();
        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }

}
