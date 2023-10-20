package com.example.marketspring.controllers;

import com.example.marketspring.configurations.JWTCore;
import com.example.marketspring.exceptions.*;
import com.example.marketspring.models.ExceptionResponse;
import com.example.marketspring.models.LoginBody;
import com.example.marketspring.models.RegBody;
import com.example.marketspring.models.User;
import com.example.marketspring.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTCore jwtCore;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginBody loginBody) {
        Authentication authentication;
        User user = userService.getUserByEmail(loginBody.getEmail());
        if (user.getBlocking()) {
            return ResponseEntity.ok(new ExceptionResponse(new AccountIsLockedException().getMessage()));
        }
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginBody.getEmail(), loginBody.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Unauthorized");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtCore.generateToken(authentication);
        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/reg")
    public ResponseEntity<?> registration(@RequestBody RegBody regBody) throws BadCredentialsException, BadEmailException, BadNameSizeException, BadSurnameSizeException, ForbiddenSymbolsException {
        try {
            userService.registration(regBody);
        } catch (Exception e) {
            return ResponseEntity.ok(new ExceptionResponse(e.getMessage()));
        }
        User user = userService.getUserByEmail(regBody.getEmail());
        return ResponseEntity.ok(user);
    }
}
