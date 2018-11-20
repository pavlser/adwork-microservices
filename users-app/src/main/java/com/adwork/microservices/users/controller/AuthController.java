package com.adwork.microservices.users.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adwork.microservices.users.auth.UsersAuthenticationProvider;
import com.adwork.microservices.users.dto.AuthData;
import com.adwork.microservices.users.jwt.JwtTokenProvider;
import com.adwork.microservices.users.service.KeysService;
import com.adwork.microservices.users.service.KeysService.PublicKeyInfo;
import com.adwork.microservices.users.service.KeysService.PublicKeyInfo.PrivateKeyInfo;
import com.adwork.microservices.users.service.UserService;
import com.adwork.microservices.users.utils.CipherUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	KeysService keysService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	UsersAuthenticationProvider authProvider;
	
	ObjectMapper mapper = new ObjectMapper();
	
	@GetMapping("public-key")
	PublicKeyInfo getPublicKey() {
		return publicKey();
	}

	@PostMapping("create-token")
	String createToken(@RequestBody String auth, HttpServletRequest request) throws Exception {
		AuthData authData = mapper.readValue(CipherUtils.decodeBase64AndDecrypt(auth, privateKey().key), AuthData.class);
		Authentication authentication = authProvider.authenticate(authData.email, authData.password);
		return jwtTokenProvider.createToken(authentication.getName(), authentication.getAuthorities().toArray(), getVerifyUrl(request));
	}

	@GetMapping("validate-token")
	boolean verifyToken(HttpServletRequest request) {
		String token = jwtTokenProvider.extractToken(request);
		return jwtTokenProvider.validateToken(token);
	}

	@GetMapping("refresh-token")
	String refreshToken(HttpServletRequest request) {
		String newToken = null;
		String token = jwtTokenProvider.extractToken(request);
		boolean valid = jwtTokenProvider.validateToken(token);
		if (valid) {
			String email = jwtTokenProvider.getSubject(token);
			List<String> roles = jwtTokenProvider.getRoles(token);
			newToken = jwtTokenProvider.createToken(email, roles.toArray(), getVerifyUrl(request));
		}
		return newToken;
	}
	
	@GetMapping("show-token")
	String showToken(HttpServletRequest request) {
		System.out.println("AuthController.showToken() 1");
		String token = jwtTokenProvider.extractToken(request);
		if (token != null) {
			String[] parts = token.split("\\.");
			if (parts.length == 3) {
				StringBuilder sb = new StringBuilder("[");
				for (int i=0; i<parts.length-1; i++) {
					sb.append(new String(CipherUtils.decodeBase64(parts[i]))).append(",\n");
				}
				sb.append("]");
				return sb.toString();
			} else {
				return "{'message':'Token not valid'}";
			}
		} else {
			return "{'message':'Token not found'}";
		}
	}
	
	private String getVerifyUrl(HttpServletRequest request) {
		return request.getRequestURL().toString().replace("create-token", "verify-token");
	}
	
	private PublicKeyInfo publicKey() {
		return keysService.getPublicKeyInfo();
	}
	
	private PrivateKeyInfo privateKey() {
		return keysService.getCurrentPrivateKey();
	}
	
}

class TokenStatusMessage {
	public String message;
	public TokenStatusMessage(String message) {
		this.message = message;
	}
}

enum TokenStatus {
	NOT_FOUND,
}
