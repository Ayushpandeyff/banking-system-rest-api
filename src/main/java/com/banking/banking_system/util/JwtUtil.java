package com.banking.banking_system.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private  Long expiration;

    public String generateToken(String email){
        Map<String,Object> claims=new HashMap<>();
        return createToken(claims,email);
    }
    private String createToken(Map<String ,Object> claims,String subject){
        return Jwts.builder().setClaims(claims).setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(SignatureAlgorithm.HS512,secret.getBytes())
                .compact();
    }
    public String extractEmail(String token){
        return extractClaim(token, Claims::getSubject);
    }
    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims=extractAllClaim(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaim(String token){
        return Jwts.parserBuilder().setSigningKey(secret.getBytes())
                .build().parseClaimsJws(token).getBody();

    }
    public Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }
    public Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }
    public Boolean validateToken(String token,String email){
        final String extractedEmail=extractEmail(token);
        return (extractedEmail.equals(email)&&!isTokenExpired(token));
    }
}
