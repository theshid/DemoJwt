package com.example.demo.service;


import com.example.demo.RegistrationRequest;
import com.example.demo.UserRole;
import com.example.demo.domain.Client;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;


@Service
@AllArgsConstructor
public class RegistrationService {
    private final EmailValidator emailValidator;
    private final ClientServiceImpl clientService;
   // private final EmailSender emailSender;

    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if (!isValidEmail) {
            throw new IllegalStateException("Email not valid");
        }

        String token = clientService.signUpClient(new Client(
                request.getFirstName(),
                request.getLastName(),
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                UserRole.CLIENT,
                generateRandomBalance()));

        return token;
    }

    public String registerAdmin(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if (!isValidEmail) {
            throw new IllegalStateException("Email not valid");
        }

        String token = clientService.signUpClient(new Client(
                request.getFirstName(),
                request.getLastName(),
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                UserRole.ADMIN,
                generateRandomBalance()));
        return token;
    }


    private Double generateRandomBalance() {
        return Math.round(ThreadLocalRandom.current().nextDouble(1000, 10000)* 100.0) / 100.0;
    }


}

