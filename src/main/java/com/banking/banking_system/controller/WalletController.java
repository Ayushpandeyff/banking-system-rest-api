package com.banking.banking_system.controller;

import com.banking.banking_system.entity.User;
import com.banking.banking_system.entity.Wallet;
import com.banking.banking_system.service.UserSrevice;
import com.banking.banking_system.service.WalletService;
import com.banking.banking_system.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {
    @Autowired
    private WalletService walletService;
    @Autowired
    private UserSrevice userSrevice;
    @Autowired
    private JwtUtil jwtUtil;
    @GetMapping("/balance")
    public ResponseEntity<?> getBalance(HttpServletRequest request){
        try {
            String email=getUserEmailFromToken(request);
            User user=userSrevice.findUserByEmail(email).get();
            BigDecimal balance=walletService.getBalance(user);
            Map<String,Object> response=new HashMap<>();
            response.put("balance",balance);
            response.put("message","Balance retireved sucesssfully");
            return ResponseEntity.ok(response);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }
    }
    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody Map<String,Object>request,HttpServletRequest httpRequest){
        try {
            String email=getUserEmailFromToken(httpRequest);
            User user=userSrevice.findUserByEmail(email).get();
            BigDecimal amount=new BigDecimal(request.get("amount").toString());
            Wallet wallet=walletService.deposit(user,amount);
            Map<String,Object> response=new HashMap<>();
            response.put("balace",wallet.getBalance());
            response.put("meesage","amount deposited");
            return  ResponseEntity.ok(response);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }
    }
    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody Map<String,Object>request,HttpServletRequest httpRequest){
        try {
            String email=getUserEmailFromToken(httpRequest);
            User user=userSrevice.findUserByEmail(email).get();
            BigDecimal amount=new BigDecimal(request.get("amount").toString());
            Wallet wallet=walletService.withdraw(user,amount);
            Map<String,Object> response=new HashMap<>();
            response.put("balance",wallet.getBalance());
            response.put("meesage","Withdraw deposited");
            return  ResponseEntity.ok(response);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }
    }
    private String getUserEmailFromToken(HttpServletRequest request){
        String authHeader=request.getHeader("Authorization");
        if(authHeader!=null&&authHeader.startsWith("Bearer ")){
            String token=authHeader.substring(7);
            String email=jwtUtil.extractEmail(token);
            System.out.println("debug extractee email from token"+email);
            return email;

        }
        throw new RuntimeException("no valid token found");
    }
}
