package com.banking.banking_system.controller;

import com.banking.banking_system.dto.TransferRequestDto;
import com.banking.banking_system.entity.Transaction;
import com.banking.banking_system.service.MoneyTransferService;
import com.banking.banking_system.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/transfer")
public class TransferController {
    @Autowired
    private MoneyTransferService moneyTransferService;
    @Autowired
    private JwtUtil jwtUtil;
    @PostMapping("/send")
    public ResponseEntity<Map<String,Object>>tranferMoney(@RequestHeader("Authorization")String token,
                                                          @RequestBody TransferRequestDto request){
        Map<String,Object> response=new HashMap<>();
        try {
            String jwtToken=token.substring(7);
            String senderEmail=jwtUtil.extractEmail(jwtToken);

            if(!jwtUtil.validateToken(jwtToken,senderEmail)){
                response.put("sucess",false);
                response.put("message","invalid or expired token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

              Transaction transaction=moneyTransferService.transferMoney(
                    senderEmail,request.getReciverEmail(),
                    request.getAmount(),
                    request.getDescription()
            );
            response.put("Sucess",true);
            response.put("message","transcation scuesfull");
            response.put("transction id",transaction.getId());
            response.put("amount",transaction.getAmount());
            response.put("reciver email",request.getReciverEmail());
            response.put("timestamp",transaction.getTimestamp());
            return ResponseEntity.ok(response);

        }catch (IllegalArgumentException e){
            response.put("sucess",false);
            response.put("message",e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
        catch (Exception e){
            response.put("Sucess",false);
            response.put("message","Transfer failed"+e.getMessage());
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        }
    }

}
