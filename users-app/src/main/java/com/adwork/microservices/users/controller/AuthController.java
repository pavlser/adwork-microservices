package com.adwork.microservices.users.controller;

import java.security.Key;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.adwork.microservices.users.dto.AuthData;
import com.adwork.microservices.users.service.KeysService;
import com.adwork.microservices.users.service.KeysService.PublicKeyInfo;
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
	
	ObjectMapper mapper = new ObjectMapper();
	
	@GetMapping("public-key")
	PublicKeyInfo getPublicKey() {
		return keysService.getPublicKeyInfo();
	}

	@PostMapping("create-token")
	String createToken(@RequestParam("kid") String keyId, @RequestBody String auth, HttpServletRequest request) throws Exception {
		AuthData authData = mapper.readValue(CipherUtils.decodeBase64AndDecrypt(auth, privateKey(keyId)), AuthData.class);
		authData.referer = getVerifyUrl(request);
		return userService.authenticate(authData);
	}

	@GetMapping()
	void verifyToken() {

	}

	@GetMapping("verify-token")
	void refreshToken() {

	}
	
	private String getVerifyUrl(HttpServletRequest request) {
		return request.getRequestURL().toString().replace("create-token", "verify-token");
	}
	
	private Key privateKey(String keyId) {
		return keysService.getKeyInfo(keyId).keyPair.getPrivate();
	}
	
}
