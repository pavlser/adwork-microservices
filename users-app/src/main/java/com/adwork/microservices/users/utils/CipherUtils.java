package com.adwork.microservices.users.utils;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

import javax.crypto.Cipher;

import static java.nio.charset.StandardCharsets.UTF_8;

public class CipherUtils {
	
	public static String decodeBase64AndDecrypt(String base64String, Key key) throws Exception {
		return decrypt(decodeBase64(base64String), key);
	}
	
	public static byte[] decodeBase64(String base64String) {
		return Base64.getDecoder().decode(base64String);
	}
	
	public static String decrypt(String text, Key key) throws Exception {
		return decrypt(text.getBytes(), key);
	}
	
	public static String decrypt(byte[] bytes, Key key) throws Exception {
		return new String(cipherEngine(key, Cipher.DECRYPT_MODE).doFinal(bytes));
	}
	
	private static Cipher cipherEngine(Key key, int mode) throws Exception {
		Cipher engine = Cipher.getInstance(key.getAlgorithm());
		engine.init(mode, key);
		return engine;
	}

	public static String encrypt(String plainText, PublicKey publicKey) throws Exception {
		Cipher encryptCipher = Cipher.getInstance("RSA");
		encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] cipherText = encryptCipher.doFinal(plainText.getBytes(UTF_8));
		return Base64.getEncoder().encodeToString(cipherText);
	}

	

	public static String sign(String plainText, PrivateKey privateKey) throws Exception {
		Signature privateSignature = Signature.getInstance("SHA256withRSA");
		privateSignature.initSign(privateKey);
		privateSignature.update(plainText.getBytes(UTF_8));
		byte[] signature = privateSignature.sign();
		return Base64.getEncoder().encodeToString(signature);
	}

	public static boolean verify(String plainText, String signature, PublicKey publicKey) throws Exception {
		Signature publicSignature = Signature.getInstance("SHA256withRSA");
		publicSignature.initVerify(publicKey);
		publicSignature.update(plainText.getBytes(UTF_8));
		byte[] signatureBytes = Base64.getDecoder().decode(signature);
		return publicSignature.verify(signatureBytes);
	}

}
