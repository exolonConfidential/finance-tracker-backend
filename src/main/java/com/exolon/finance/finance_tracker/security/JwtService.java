package com.exolon.finance.finance_tracker.security;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;


@Service

public class JwtService {
	
	@Value("${spring.jwt.secret}")
	private String secret;
		
	private JWTVerifier verifier() {
		return JWT.require(Algorithm.HMAC256(secret)).withIssuer("finance-tracker").build();
	}
	
	private DecodedJWT decodeToken(String token) throws JWTVerificationException {
		return verifier().verify(token);
	}
	public String generateToken(UUID userId) {
		Instant now = Instant.now();
		return JWT.create()
				.withIssuer("finance-tracker")
				.withSubject(userId.toString())
				.withIssuedAt(Date.from(now))
				.sign(Algorithm.HMAC256(secret));
	}
	
	public String extractUserId(String token)  {
		return decodeToken(token).getSubject();
	}
	
	public boolean verifyToken(String token) {
		 try {
	            decodeToken(token);
	            return true;
	        } catch (JWTVerificationException e) {
	        		
	            return false;
	        }
	}
	
}
