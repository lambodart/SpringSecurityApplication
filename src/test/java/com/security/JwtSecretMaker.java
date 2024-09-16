package com.security;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Jwts;
import jakarta.xml.bind.DatatypeConverter;

public class JwtSecretMaker {
	
	@Test
	
	public void generateJwtSecret() {
		
		SecretKey secretKey 	 = Jwts.SIG.HS512.key().build();
		String    printHexBinary = DatatypeConverter.printHexBinary(secretKey.getEncoded());
		System.out.println("printHexBinary :: "+printHexBinary);
	}
	

}
