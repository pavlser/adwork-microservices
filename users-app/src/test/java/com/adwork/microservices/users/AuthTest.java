package com.adwork.microservices.users;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.adwork.microservices.users.dto.AuthInfo;
import com.adwork.microservices.users.service.KeysService.PublicKeyInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthTest {

	private static final String HostUrl = "http://localhost:8090";
	
	public static void main(String[] args) {
		try {
			// Load public key from server
			RestTemplate restTemplate = new RestTemplate();
			PublicKeyInfo key = restTemplate.getForObject(HostUrl + "/api/auth/public-key", PublicKeyInfo.class);
			System.out.println("Server public key:\n" + key + "\n");
			
			// Create auth info
			AuthInfo auth = new AuthInfo("admin@adwork-microservices.com", "admin");
			ObjectMapper mapper = new ObjectMapper();
			String authStr = mapper.writeValueAsString(auth);
			
			// Decode key to PublicKey
			byte[] keyBytes = Base64.getDecoder().decode(key.publicKey.getBytes());
			X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance(key.algorithm);
			PublicKey publicKey = keyFactory.generatePublic(spec); 
			
			// Encrypt auth info
			Cipher encryptor = Cipher.getInstance("RSA");
			encryptor.init(Cipher.ENCRYPT_MODE, publicKey);
	        byte[] encryptedAuth = encryptor.doFinal(authStr.getBytes(UTF_8));
	        String encodedAuth = Base64.getEncoder().encodeToString(encryptedAuth);
	        System.out.println("> encodedAuth=" + encodedAuth);
	        
	        // Post auth
			ResponseEntity<String> authResp = restTemplate.postForEntity(
					HostUrl + "/api/auth/create-token?kid="+key.keyId, encodedAuth, String.class);
			System.out.println(authResp);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
