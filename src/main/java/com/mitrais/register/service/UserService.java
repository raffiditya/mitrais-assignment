package com.mitrais.register.service;

import com.mitrais.register.model.entity.User;
import com.mitrais.register.repo.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isEmailExists(String email) {
        return userRepository.existsByEmailEqualsIgnoreCase(email);
    }

    public boolean isMobileNumberExists(String mobileNumber) {
        return userRepository.existsByMobileNumberEquals(mobileNumber);
    }

    public User insert(User user) {
        if (isEmailExists(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already taken.");
        }
        if (isMobileNumberExists(user.getMobileNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mobile number already taken.");
        }

        return userRepository.save(user);
    }
}
