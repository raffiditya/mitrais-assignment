package com.mitrais.register.controller;

import com.mitrais.register.model.entity.User;
import com.mitrais.register.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/isValidEmail")
    public ResponseEntity<?> isValidEmail(@RequestParam String email) {
        if (userService.isEmailExists(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already taken.");
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/isValidMobileNumber")
    public ResponseEntity<?> isValidMobileNumber(@RequestParam String mobileNumber) {
        if (userService.isMobileNumberExists(mobileNumber)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mobile number already taken.");
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid User user) {
        return ResponseEntity.ok(userService.insert(user));
    }
}
