package com.banking.banking_system.controller;

import com.banking.banking_system.dto.TransactionResponseDTO;
import com.banking.banking_system.entity.User;
import com.banking.banking_system.entity.Wallet;
import com.banking.banking_system.service.TransactionService;
import com.banking.banking_system.service.UserSrevice;
import com.banking.banking_system.service.WalletService;
import com.banking.banking_system.util.JwtUtil;
import org.apache.catalina.webresources.JarWarResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statement")
public class StatementController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserSrevice userSrevice;
    @Autowired
    private WalletService walletService;
    @Autowired
    private JwtUtil jwtUtil;
    @GetMapping("/monthly")
    public ResponseEntity<Map<String,Object>> getMonthlyStatment(@RequestHeader("Authorization")String token,
    @RequestParam int year ,@RequestParam int month){
        Map<String,Object> response=new HashMap<>();
        try {
            String jwtToken = token.substring(7);
            String userEmail = jwtUtil.extractEmail(jwtToken);

            if (!jwtUtil.validateToken(jwtToken, userEmail)) {
                response.put("sucess", false);
                response.put("message", "invalid or expired token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            User user=userSrevice.findUserByEmail(userEmail).orElseThrow(()->new IllegalArgumentException("User not found"));
            Wallet wallet=walletService.getWalletByUser(user);
            LocalDateTime startDate=LocalDateTime.of(year,month,1,0,0);
            LocalDateTime endDate=startDate.plusMonths(1).minusSeconds(0);
            List<TransactionResponseDTO> transactions=transactionService.convertToDtoList(
                    transactionService.getTranscationbyDateRange(wallet,startDate,endDate)
            );
            BigDecimal totalDeposits=BigDecimal.ZERO;
            BigDecimal totalWithdrawls=BigDecimal.ZERO;
            BigDecimal totaltransferSent=BigDecimal.ZERO;
            BigDecimal totalTransferRecived=BigDecimal.ZERO;
            for(TransactionResponseDTO transacation:transactions){
                switch (transacation.getType()){
                    case DEPOSIT :
                        totalDeposits=totalDeposits.add(transacation.getAmount());
                        break;
                    case WITHDRAWL:
                        totalWithdrawls=totalWithdrawls.add(transacation.getAmount());
                        break;
                    case TRANSFER:
                        if(transacation.getFromUserEmail().equals(userEmail)){
                            totaltransferSent=totaltransferSent.add(transacation.getAmount());
                        }else{
                            totalTransferRecived=totalTransferRecived.add(transacation.getAmount());
                        }break;
                }
            }
            response.put("sucess",true);
            response.put("message","monthly Statment recived");
            response.put("period",month+"/"+year);
            response.put("user",userSrevice.convertToDtO(user));
            response.put("currentBalance", wallet.getBalance());
            response.put("transcation",transactions);
            response.put("summary",Map.of(
                    "totalTransaction",transactions.size(),
                    "totalDeposit",totalDeposits,
                    "tota;Withdrawl",totalWithdrawls,
                    "totalTransferSent",totaltransferSent,
                    "totalTransferRecived",totalTransferRecived,
                    "netAmount",totalDeposits.add(totalTransferRecived).subtract(totalWithdrawls).subtract(totaltransferSent)
            ));
            response.put("genratedAt",LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            return ResponseEntity.ok(response);
        }catch (Exception e){
            response.put("sucess",false);
            response.put("message","failed to retruve waller"+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
