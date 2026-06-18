package com.app.financemanager.security;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtils {

//	private final AuthenticationManager authenticationManager;
	private static final String SECRET = "MoneyBuddyFinanceTrackerSecretKey123456789";

//	private final Key key = new SecretKeySpec(SECRET.getBytes(), SignatureAlgorithm.HS256.getJcaName());
	private final SecretKey key =
	        new SecretKeySpec(
	                SECRET.getBytes(),
	                SignatureAlgorithm.HS256.getJcaName()
	        );

//	JwtUtils(AuthenticationManager authenticationManager) {
//		this.authenticationManager = authenticationManager;
//	}

	public String generateToken(String email) {
		return Jwts.builder().subject(email).issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + 3600000)).signWith(key).compact();
	}

	public String extractUsername(String token) {

		return Jwts.parser().verifyWith((javax.crypto.SecretKey) key).build().parseSignedClaims(token).getPayload()
				.getSubject();
	}

	public boolean validateToken(String token) {
		try {

			Jwts.parser().verifyWith(key).build().parseSignedClaims(token);

			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
