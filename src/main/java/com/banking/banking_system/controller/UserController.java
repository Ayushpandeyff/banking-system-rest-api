package com.banking.banking_system.controller;

import com.banking.banking_system.dto.UserLoginDTO;
import com.banking.banking_system.dto.UserRegistrationDTO;
import com.banking.banking_system.dto.UserResponseDTO;
import com.banking.banking_system.entity.User;
import com.banking.banking_system.service.UserSrevice;
import com.banking.banking_system.util.JwtUtil;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserSrevice userSrevice;
@Autowired
private JwtUtil jwtUtil;
@Autowired
private AuthenticationManager authenticationManager;


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
            System.out.println("registraion failed"+e.getMessage());
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
    @PostMapping("/login")
    public ResponseEntity<?>login (@RequestBody UserLoginDTO loginDTO){
        try {
            Authentication authentication=authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getEmail(),
                            loginDTO.getPassword()
                    )
            );
            String email=authentication.getName();
            String token= jwtUtil.generateToken(email);
            User user=userSrevice.findUserByEmail(email).get();
            UserResponseDTO userResponseDTO=new UserResponseDTO();
            userResponseDTO.setEmail(user.getEmail());
            userResponseDTO.setFirstName(user.getFirstName());
            userResponseDTO.setLastname(user.getLastName());
            userResponseDTO.setPhoneNumber(user.getPhonenumber());
            userResponseDTO.setRole(user.getRole());
            userResponseDTO.setActive(user.getActive());
            userResponseDTO.setCreatedAt(user.getCreatedAt());
            userResponseDTO.setId(user.getId());
            Map<String, Object> response=new HashMap<>();
            response.put("token",token);
            response.put("user",userResponseDTO);
            response.put("message","Login sucessfull");
            return ResponseEntity.ok(response);
        }
        catch (BadCredentialsException e){
            Map<String,Object> errorResponse=new HashMap<>();
            errorResponse.put("error","invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);

        }catch (Exception e){
            Map<String ,Object> errorresposne=new HashMap<>();
            errorresposne.put("error","Login failed"+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorresposne);
        }
    }
}
