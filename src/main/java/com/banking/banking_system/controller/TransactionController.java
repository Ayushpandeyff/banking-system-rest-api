package com.banking.banking_system.controller;


import com.banking.banking_system.dto.TransactionResponseDTO;
import com.banking.banking_system.entity.Transaction;
import com.banking.banking_system.entity.User;
import com.banking.banking_system.entity.Wallet;
import com.banking.banking_system.service.TransactionService;
import com.banking.banking_system.service.UserSrevice;
import com.banking.banking_system.service.WalletService;
import com.banking.banking_system.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserSrevice userSrevice;
    @Autowired
    private WalletService walletService;
    @Autowired
    private JwtUtil jwtUtil;
    @GetMapping("/history")
    public ResponseEntity<Map<String,Object>> getTransferHistory(@RequestHeader("Authorization")String token){
        Map<String,Object>response=new HashMap<>();
        try {
            String jwtToken = token.substring(7);
            String userEmail = jwtUtil.extractEmail(jwtToken);

            if (!jwtUtil.validateToken(jwtToken, userEmail)) {
                response.put("sucess", false);
                response.put("message", "invalid or expired token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            User user = userSrevice.findUserByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("User not found"));
            Wallet wallet =walletService.getWalletByUser(user);
            List<Transaction> transactions=transactionService.getTransferHistory(wallet);
            List<TransactionResponseDTO>transactionDTOs=transactionService.convertToDtoList(transactions);
            response.put("sucess",true);
            response.put("messsage","Transcatio histroy sucess");
            response.put("transcations",transactionDTOs);
            response.put("totalTransactios",transactions.size());
            response.put("totalBalance",wallet.getBalance());
            return ResponseEntity.ok(response);

        }catch (Exception e){
            response.put("Sucess",false);
            response.put("message","failed to retirve"+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }


    }
    @GetMapping("/history/range")
    public ResponseEntity<Map<String,Object>> getTranscationHistorybyDateRange(@RequestHeader("Authorization")String token,
                                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime endDate ){

        Map<String ,Object> response=new HashMap<>();
        try {
            String jwtToken = token.substring(7);
            String userEmail = jwtUtil.extractEmail(jwtToken);

            if (!jwtUtil.validateToken(jwtToken, userEmail)) {
                response.put("sucess", false);
                response.put("message", "invalid or expired token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            if(startDate.isAfter(endDate)){
                response.put("sucess",false);
                response.put("message","startdate cannot be after enddate");
                ResponseEntity.badRequest().body(response);
            }
            User user = userSrevice.findUserByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("User not found"));
            Wallet wallet =walletService.getWalletByUser(user);
            List<Transaction> transactions=transactionService.getTranscationbyDateRange(wallet,startDate,endDate);
            List<TransactionResponseDTO>transactionDTOs=transactionService.convertToDtoList(transactions);

            response.put("sucess",true);
            response.put("messsage","Transcatio histroy sucess by date range");
            response.put("transcations",transactionDTOs);
            response.put("totalTransactios",transactionDTOs.size());
            response.put("sartdate",startDate);
            response.put("endate",endDate);
            response.put("totalBalance",wallet.getBalance());
            return ResponseEntity.ok(response);
        }catch (Exception e){
            response.put("Sucess",false);
            response.put("message","failed to retirve"+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        }

    }

}
