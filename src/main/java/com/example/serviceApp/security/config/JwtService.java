package com.example.serviceApp.security.config;

import com.example.serviceApp.customer.CustomerUserDetailsService;
import com.example.serviceApp.security.User.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final CustomUserDetailsService userDetailsService;
    private final CustomerUserDetailsService customerUserDetailsService;
    private static final String SECRET_KEY ="KQrSEqVRHxIPgBLuLhMRIHrPzme94ofdV9Siwsa0B1me3Hj4ZHhNM0LRZKMLacoR";
    public String extractUsername(String token) {

        return extractClaim(token,Claims::getSubject);
    }
    public String extractUserType(String token) {
        return extractClaim(token, claims -> claims.get("userType", String.class));
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSingInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

    }
    public String generateRefreshToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        claims.put("isRefreshToken", true);
        return generateToken(claims, userDetails, 1000*60*60*24L);
    }
    public String generateToken(UserDetails userDetails, String userType){
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userType", userType);
        return generateToken(extraClaims, userDetails, (100000*60L));
    }
//    public String generateToken(UserDetails userDetails){
//       return generateToken(new HashMap<>(),userDetails,(100000*60L));
//    }
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            Long expirationTime
    ){
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+expirationTime))
                .signWith(getSingInKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    private Key getSingInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
       return    extractClaim(token,Claims::getExpiration);
    }

    public boolean isRefreshToken(String token) {
         Claims claims = extractAllClaims(token);
        Object isRefresh = claims.get("isRefreshToken");
        return isRefresh != null && (Boolean) isRefresh;
    }

    public UserDetails getUserDetails(String token) {
        final String username = extractUsername(token);
        final String userType = extractUserType(token);

        UserDetails userDetails;
        if ("customer".equals(userType)) {
            userDetails = customerUserDetailsService.loadUserByUsername(username);
        } else {
            userDetails = userDetailsService.loadUserByUsername(username);
        }

        return userDetails;
    }
}
