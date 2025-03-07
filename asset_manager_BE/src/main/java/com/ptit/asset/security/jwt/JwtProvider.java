package com.ptit.asset.security.jwt;

import com.ptit.asset.security.UserPrinciple;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.util.Date;
/*
 *** Created by DuyKhanh on : 01/11/2020
 */
@Component
public class JwtProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${asset.app.jwtSecret}")
    private String jwtSecret;

    @Value("${asset.app.jwtExpiration}")
    private int jwtExpiration;

    public String generateJwtToken(Authentication authentication) {

        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        return Jwts.builder()
            .setSubject(userPrinciple.getUsername())
            .setIssuedAt(new Date(new Date().getTime() + jwtExpiration ))
            .signWith(SignatureAlgorithm.HS512 , jwtSecret)
            .compact();
    }

    public String getUserNameFromJwtToken(String token) {

        return Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {

        try {
            // TODO : Confirm JWT is generated correct with Secret
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature -> Message: {} ", e);
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token -> Message: {}", e);
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token -> Message: {}", e);
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token -> Message: {}", e);
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty -> Message: {}", e);
        }

        return false;
    }

}
