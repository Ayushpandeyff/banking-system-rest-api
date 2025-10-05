package com.banking.banking_system.controller;

import com.banking.banking_system.dto.UserResponseDTO;
import com.banking.banking_system.dto.WalletResponseDTO;
import com.banking.banking_system.entity.User;
import com.banking.banking_system.entity.Wallet;
import com.banking.banking_system.enums.Role;
import com.banking.banking_system.service.UserSrevice;
import com.banking.banking_system.service.WalletService;
import com.banking.banking_system.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private UserSrevice userSrevice;
    @Autowired
    private WalletService walletService;
    @Autowired
    private JwtUtil jwtUtil;
    private  boolean isAdmin(String token){
        try{
            String jwtToken=token.substring(7);
            String userEmail=jwtUtil.extractEmail(jwtToken);
            User user=userSrevice.findUserByEmail(userEmail).orElse(null);
            return user!=null&&user.getRole()== Role.ADMIN;
        }catch (Exception e){
            return false;
        }
    }
    @GetMapping("/users")
    public ResponseEntity<Map<String ,Object>> getAllUsers(@RequestHeader("Authorization")String token){
        Map<String,Object> response=new HashMap<>();
        try{
            if(!isAdmin(token)){
                response.put("sucess",false);
                response.put("mesaage","acess denied admin required");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
            List<User>users=userSrevice.getAllUsers();
            List<UserResponseDTO> userDTOs=userSrevice.convertToList(users);
            response.put("sucess",true);
            response.put("message","user retrived scuessfully");
            response.put("users",userDTOs);
            response.put("totalUsers",userDTOs.size());
            return ResponseEntity.ok(response);
        }catch (Exception e){
            response.put("sucess",false);
            response.put("message","failed to retiv user"+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }
    @GetMapping("/wallets")
    public ResponseEntity<Map<String ,Object>> getAllWallets(@RequestHeader("Authorization")String token){
        Map<String ,Object> response=new HashMap<>();
        try {
            if(!isAdmin(token)){
                response.put("sucess",false);
                response.put("message","Acess denied.Admin privelgies required");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
            List<WalletResponseDTO> wallets=walletService.getAllWalletDto();
            response.put("sucesss",true);
            response.put("message","all wallet retrived");
            response.put("wallets",wallets);
            response.put("walletcount",wallets.size());
            return ResponseEntity.ok(response);
        }catch (Exception e){
            response.put("sucess",false);
            response.put("message","failed to retruve waller"+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String,Object>> getUserDetails(@RequestHeader("Authorization")String token,
                                                        @PathVariable Long userId){
        Map<String ,Object> response=new HashMap<>();
        try {
            if (!isAdmin(token)) {
                response.put("sucess", false);
                response.put("message", "Acess denied.Admin privelgies required");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
            User user=userSrevice.findUserById(userId).orElse(null);
            if(user==null){
                response.put("sucess", false);
                response.put("message", "User not found");
                return ResponseEntity.notFound().build();
            }
            UserResponseDTO userDTO=userSrevice.convertToDtO(user);
            Wallet wallet=walletService.getWalletByUser(user);
            WalletResponseDTO walletDto=new WalletResponseDTO();
            walletDto.setId(wallet.getId());
            walletDto.setUserEmail(wallet.getUser().getEmail());
            walletDto.setBalance(wallet.getBalance());
            walletDto.setUserId(wallet.getUser().getId());
            response.put("sucesss",true);
            response.put("message","user detail retrived sucessfully");
            response.put("wallets",walletDto);
            response.put("user",userDTO);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            response.put("sucess",false);
            response.put("message","failed to retruve waller"+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        }
    }
}
