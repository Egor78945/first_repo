package com.example.headhunter.configurations.securityConfigurations;

import com.example.headhunter.services.UserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JWTCore {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.lifetime}")
    private String lifetime;

    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Map<String, Object> claims = new HashMap<>();
        List<String> roleList = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", roleList);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getPhoneNumber())
                .setIssuedAt(new Date(new Date().getTime() + lifetime))
                .signWith(getSigningKey(secret), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getPhoneNumberFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public List<String> getRoleFromToken(String token) {
        return getAllClaimsFromToken(token).get("roles", List.class);
    }

    private Key getSigningKey(String secret) {
        byte[] key = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(key);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey(secret))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}