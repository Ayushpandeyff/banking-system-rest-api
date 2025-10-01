package com.banking.banking_system.controller;

import com.banking.banking_system.dto.UserRegistrationDTO;
import com.banking.banking_system.dto.UserResponseDTO;
import com.banking.banking_system.entity.User;
import com.banking.banking_system.service.UserSrevice;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserSrevice userSrevice;



    @PostMapping("/register")
    public ResponseEntity<?>registerUser(@Valid @RequestBody UserRegistrationDTO registrationDTO){
        try{
            User user=userSrevice.resgisterUser(
                    registrationDTO.getFirstName(),
                    registrationDTO.getLastName(),
                    registrationDTO.getEmail(),
                    registrationDTO.getPassword(),
                    registrationDTO.getPhoneNumber()
            );
            UserResponseDTO response=convertToResponseDTO(user);
            return ResponseEntity.ok(response);
        }
        catch (RuntimeException e){
            System.out.println("registraing failed"+e.getMessage());
            return ResponseEntity.badRequest().body("error"+e.getMessage());
        }
    }

    private UserResponseDTO convertToResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                user.getPhonenumber(),
                user.getActive(),
                user.getCreatedAt()
        );
    }
}
