package com.fileoperations.security;

import java.util.Date;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.fileoperations.exception.customException.BadRequestException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenProvider {
	
   private static final String jwtSecret = "ABCDEF";
   
   @Value("${app.jwt-expiration-milliseconds}")
   private int jwtExpirationInMs;

	public String generateToken(Authentication authentication){
        String username=authentication.getName();
        Date currentDate=new Date();
        Date expirationDate= new Date(currentDate.getTime()+jwtExpirationInMs);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512,jwtSecret)
                .compact();
        
        return token;
    }
	
	 public String  getUsernameFromJWT(String token){
	        Claims claims=Jwts.parser()
	                .setSigningKey(jwtSecret)
	                .parseClaimsJws(token)
	                .getBody();

	        return claims.getSubject();
	    }


    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        }catch (SignatureException ex){
        throw new BadRequestException("Invalid JWT signature");
        }catch (MalformedJwtException ex){
            throw new BadRequestException("Invalid JWT token");
        }catch (ExpiredJwtException ex){
            throw new BadRequestException("Expired JWT token");
        }catch (UnsupportedJwtException ex){
            throw new BadRequestException("Unsupported JWT token");
        }catch (IllegalArgumentException ex){
            throw new BadRequestException("JWT claims string is empty");
        }


    }

	
}
