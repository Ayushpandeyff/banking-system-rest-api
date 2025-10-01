package com.banking.banking_system.service;

import com.banking.banking_system.entity.User;
import com.banking.banking_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.StyledEditorKit;
import java.util.List;
import java.util.Optional;

@Service
public class UserSrevice {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
     public User saveUser(User user){
         String encryptedPassword=passwordEncoder.encode(user.getPassword());
         user.setPassword(encryptedPassword);
         return userRepository.save(user);
     }
     public Optional<User> findUserById(Long id){
         return userRepository.findById(id);
     }
    public Optional<User> findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }
    public List<User> getAllUsers(){
         return userRepository.findAll();
    }
    public boolean emailExists(String email){
         return userRepository.existsByEmail(email);
    }
    public List<User> getAllActiveUser(){
        return userRepository.findAllActiveUsers();
    }
    public User resgisterUser(String firstName,String lastName,String email,String password,String phoneNumber){
         if(emailExists(email)){
             throw new RuntimeException("email already exist");
         }else{
             User user=new User(firstName,lastName,email,password,phoneNumber);
             return saveUser(user);
         }
    }
 public boolean validatePassowrd(String rawPassword,String encodedPassword){
         return passwordEncoder.matches(rawPassword,encodedPassword);
 }

}
