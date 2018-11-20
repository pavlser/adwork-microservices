package com.adwork.microservices.users;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.adwork.microservices.users.dto.AuthData;
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
			AuthData auth = new AuthData("admin@adwork-microservices.com", "admin");
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
					HostUrl + "/api/auth/create-token", encodedAuth, String.class);
			String token = authResp.getBody();
			System.out.println("\nToken:\n" + token + "\n");
			
			// Create authentication header
			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(token);
			HttpEntity<String> tokenEntity = new HttpEntity<>(null, headers);
			
			// Show token details
			String tokenDetails = restTemplate.exchange(
					HostUrl + "/api/auth/show-token", HttpMethod.GET, tokenEntity, String.class).getBody();
			System.out.println("\nParsed token:\n" + tokenDetails + "\n");
			
			// Validate token
			String tokenValid = restTemplate.exchange(
					HostUrl + "/api/auth/validate-token", HttpMethod.GET, tokenEntity, String.class).getBody();
			System.out.println("\nToken is valid:\n" + tokenValid + "\n");
			
			// Refresh token
			String newToken = restTemplate.exchange(
					HostUrl + "/api/auth/refresh-token", HttpMethod.GET, tokenEntity, String.class).getBody();
			System.out.println("\nNew token:\n" + newToken + "\n");
			
			// Validate new token
			headers = new HttpHeaders();
			headers.setBearerAuth(newToken);
			tokenEntity = new HttpEntity<>(null, headers);
			String newTokenValid = restTemplate.exchange(
					HostUrl + "/api/auth/validate-token", HttpMethod.GET, tokenEntity, String.class).getBody();
			System.out.println("\nNew token is valid:\n" + newTokenValid + "\n");
			
			// Show new token details
			String newTokenDetails = restTemplate.exchange(
					HostUrl + "/api/auth/show-token", HttpMethod.GET, tokenEntity, String.class).getBody();
			System.out.println("\nNew token details:\n" + newTokenDetails + "\n");
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
