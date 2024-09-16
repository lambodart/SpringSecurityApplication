package com.security.webtoken;

import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService { 
	
	private static final String SECRET = "8D951953482668502E0472B3CD1BE620F97A177EFD34C1FBA1722D1B7B4EB9C124D3C3677FC15398851E56D979636176DB06681E978366F884AADEF12F4E5E84";
	
	private static final long validity = TimeUnit.MINUTES.toMillis(30); //convert 30 min to milisec
	public String generateToken(UserDetails userDetails ) {
		Map<String, String > claims =  new HashMap<String, String>();
		claims.put("iss", "https://anywebsite.com");
	//	claims.put("name", "");
		return Jwts.builder()
			.claims(claims)
			.subject(userDetails.getUsername())
			.issuedAt(Date.from(Instant.now()))
			.expiration(Date.from(Instant.now().plusMillis(validity)))
			.signWith(geSecretKey())
			.compact(); //covert to string
			
			
		//return null;
		
	}
	
	private SecretKey geSecretKey() {
		//convert SECRET obj 
		byte[] decode = Base64.getDecoder().decode(SECRET);
		SecretKey hmacShaKeyFor = Keys.hmacShaKeyFor(decode);
		return hmacShaKeyFor;
	}

	public String extractUserName(String jwt) {
		Claims payload = claims(jwt);
		
		String usename = payload.getSubject();		//we get username in this
		Date issuedAt 	= payload.getIssuedAt(); 	//issuere timr start
		Date expiration = payload.getExpiration(); 	//expires time of jwt 
		
		System.out.println(usename+" :: "+issuedAt+" :: "+expiration);
		/*{
		  "iss": "https://anywebsite.com",
		  "sub": "user",
		  "iat": 1726469766,
		  "exp": 1726471566
		}*/
		return usename;
	}

	private Claims claims(String jwt) {
		Claims claim = Jwts.parser().verifyWith(geSecretKey())
				.build()
				.parseSignedClaims(jwt)
				.getPayload()
				;
		return claim;
	}
	public boolean isTokenValid(String jwt) {
		Claims payload = claims(jwt);
		
		String usename = payload.getSubject();		//we get username in this
		Date issuedAt 	= payload.getIssuedAt(); 	//issuere timr start
		Date expiration = payload.getExpiration(); 	//expires time of jwt 
		
		return expiration.after(Date.from(Instant.now()));
	}
	
	

}
