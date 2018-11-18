package com.adwork.microservices.users.controller;

import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.adwork.microservices.users.dto.AuthInfo;
import com.adwork.microservices.users.service.KeysService;
import com.adwork.microservices.users.service.KeysService.PublicKeyInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.lang.Assert;

import com.adwork.microservices.users.service.KeysService.KeyInfo;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	KeysService keysService;
	
	ObjectMapper mapper = new ObjectMapper();

	@PostMapping("create-token")
	String createToken(@RequestParam("kid") String kid, @RequestBody String auth) {
		System.out.println("AuthController.createToken(): auth=" + auth);

		try {
			KeyInfo ki = keysService.getKeyInfo(kid);
			Assert.notNull(ki);

			byte[] bytes = Base64.getDecoder().decode(auth);
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, ki.keyPair.getPrivate());

			String decryptedStr = new String(cipher.doFinal(bytes));
			AuthInfo authInfo = mapper.readValue(decryptedStr, AuthInfo.class);
			
			System.out.println("authInfo: " + authInfo.email + ", " + authInfo.password);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "{\"message\":\"ok\"}";
	}

	@GetMapping("validate-token")
	void validateToken() {

	}

	@GetMapping("refresh-token")
	void refreshToken() {

	}

	@GetMapping("public-key")
	PublicKeyInfo getPublicKey() {
		return keysService.getPublicKeyInfo();
	}

}
