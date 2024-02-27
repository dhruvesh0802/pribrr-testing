package com.pb.security;

import com.pb.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;

@Component
public class JwtTokenUtils {
    
    private static final String PB = "PB";
    private static final String PB_SECRET = "f39fe5bf0e243cf1938dbe7c78dbdcbd6188ca123d1edb29b61e2f80c91e5664";
    
    private UserDetailsService userDetailsService;

    public JwtTokenUtils(UserDetailsService userDetailsService){
        this.userDetailsService = userDetailsService;
    }
    
    
    public String getAuthenticationToken(UserEntity userEntity,Boolean isRemember) {
        return doGenerateToken(userEntity,false,isRemember);
    }
    public String getAuthenticationTokenEmailVerification(UserEntity userEntity) {
        return doGenerateToken(userEntity,true,false);
    }

    private String doGenerateToken(UserEntity userEntity,Boolean isForVerify,Boolean isRemember) {
        Claims claims = Jwts.claims().setSubject(userEntity.getEmail());
        claims.put("scopes", null);
        return Jwts.builder()
                .setClaims(claims)
                .setId(isForVerify ? userEntity.getId().toString():userEntity.getEmail())
                .setIssuer(PB)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(isRemember ? new Date(System.currentTimeMillis() + 2592000000L):new Date(System.currentTimeMillis() + 1000*60*60*24))
                .signWith(SignatureAlgorithm.HS256, PB_SECRET)
                .setHeaderParam("typ", "JWT")
                .compact();
    }


    
    public String getUsernameFromToken(String token) throws ExpiredJwtException,IllegalArgumentException {
        try {
            Map<String, Object> dataMap = getAllClaimsMapFromToken(token);
            
            if (Objects.isNull(dataMap))
                return null;
            if (dataMap.containsKey("sub")) {
                return  (String) dataMap.get("sub");
            } else {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
    }
    public String getUserIdFromToken(String token) throws ExpiredJwtException,IllegalArgumentException {
        try {
            Map<String, Object> dataMap = getAllClaimsMapFromToken(token);

            if (Objects.isNull(dataMap))
                return null;
            if (dataMap.containsKey("jti")) {
                return  (String) dataMap.get("jti");
            } else {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
    }
    public Map<String, Object> getAllClaimsMapFromToken(String token) {
        if (isTokenExpired(token))
            return Collections.emptyMap();
        return getAllClaimsFromToken(token);
    }
    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    
    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) throws ExpiredJwtException {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    
    private Claims getAllClaimsFromToken(String token) throws ExpiredJwtException {
        return Jwts.parser()
                .setSigningKey(PB_SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
    
    public String getUserId(String token) {
        return Jwts.parser().setSigningKey(PB_SECRET).parseClaimsJws(token).getBody().getSubject();
    }
    
    public Authentication getAuthentication(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
    }
}
