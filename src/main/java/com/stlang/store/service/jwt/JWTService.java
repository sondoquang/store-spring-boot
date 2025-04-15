package com.stlang.store.service.jwt;

import com.stlang.store.dto.LoginDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;


@Service
public class JWTService {



//    private static final String SECRET = "SeEPo+vkklz5eTLRAqvpR0h0tqC+6N2ZSwK/4+Fyy31yqjnsFJzPmLzDGRy1fp2/\n";

    private String secretKey;

    private JWTService(){
        secretKey = generateSecretKey();
    }

    public String generateSecretKey()  {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey secretKey = keyGen.generateKey();
            System.out.println("SecretKey: " + secretKey.toString());
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateToken(UserDetails userDetail) {
        long now = System.currentTimeMillis();
        Map<String, Object> claims= new HashMap<>();
        String jti = UUID.randomUUID().toString();
        claims.put("jti", jti);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetail.getUsername())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 1000*60*60*30))
                .signWith(getKey(), SignatureAlgorithm.HS256).compact();
    }

    public String generateRefreshToken(UserDetails userDetails, LoginDTO loginDTO) {
        Map<String, Object> claims= new HashMap<>();
        claims.put("email", loginDTO.getUserLogin().getEmail());
        claims.put("fullname", loginDTO.getUserLogin().getFullName());
        claims.put("gender", loginDTO.getUserLogin().getGender());
        claims.put("username", loginDTO.getUserLogin().getUsername());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*60*24*100))
                .signWith(getKey(), SignatureAlgorithm.HS256).compact();
    }


    private Key getKey() {
        byte[] keyByte = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyByte);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return  Jwts.parser().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
    }


    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
