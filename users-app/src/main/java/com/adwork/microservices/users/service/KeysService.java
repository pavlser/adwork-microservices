package com.adwork.microservices.users.service;

import java.security.KeyPair;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.impl.crypto.RsaProvider;

@Component
public class KeysService {
	
	private final int KeyStrenght = 1024;
	private final long KeyValidityMs = 1000 * 60 * 60 * 24;
	
	private Map<String, KeyInfo> keys = new HashMap<>();
	private String lastKeyId;

	@PostConstruct
	void initialize() {
		this.refreshKeys();
	}
	
	public void refreshKeys() {
		long now = now();
		for (String kid : keys.keySet()) {
			if (keys.get(kid).validUntilDate <= now) {
				keys.remove(kid);
			}
		}
		KeyInfo ki = getNewKeyInfo();
		keys.put(ki.keyId, ki);
		lastKeyId = ki.keyId;
	}
	
	public KeyInfo getKeyInfo(String kid) {
		return keys.get(kid);
	}
	
	public String getKeyId() {
		return lastKeyId;
	}
	
	public PublicKeyInfo getPublicKeyInfo() {
		KeyInfo ki = keys.get(lastKeyId);
		PublicKeyInfo info = new PublicKeyInfo(ki.keyId, ki.pubKeyB64, ki.createdDate, ki.validUntilDate);
		info.algorithm = ki.algorithm;
		return info;
	}
	
	public byte[] getPublicKey() {
		return keys.get(lastKeyId).keyPair.getPublic().getEncoded();
	}
	
	public byte[] getPrivateKey() {
		return keys.get(lastKeyId).keyPair.getPrivate().getEncoded();
	}
	
	private KeyInfo getNewKeyInfo() {
		KeyInfo ki = new KeyInfo();
		ki.keyId = randomStr();
		ki.keyPair = newKeyPair();
		ki.createdDate = now();
		ki.algorithm = ki.keyPair.getPublic().getAlgorithm();
		ki.validUntilDate = ki.createdDate + KeyValidityMs;
		ki.privKeyB64 = getBase64(ki.keyPair.getPrivate().getEncoded()); //TextCodec.BASE64URL.encode(ki.keyPair.getPrivate().getEncoded());
		ki.pubKeyB64 =  getBase64(ki.keyPair.getPublic().getEncoded());  //TextCodec.BASE64URL.encode(ki.keyPair.getPublic().getEncoded());
		return ki;
	}
	
	private KeyPair newKeyPair() {
		return RsaProvider.generateKeyPair(KeyStrenght);
	}
	
	private String randomStr() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	private long now() {
		return new Date().getTime();
	}
	
	private String getBase64(byte[] value) {
		return Base64.getEncoder().encodeToString(value);
	}
	
	public static class PublicKeyInfo {
		public String keyId;
		public String publicKey;
		public String algorithm = "RSA";
		public long createdDate;
		public long validUntilDate;
		
		public PublicKeyInfo() {}
		
		public PublicKeyInfo(String id, String keyB64, long created, long valid) {
			this.keyId = id;
			this.publicKey = keyB64;
			this.createdDate = created;
			this.validUntilDate = valid;
		}

		@Override
		public String toString() {
			return "PublicKeyInfo [keyId=" + keyId + ", publicKey=" + publicKey + ", createdDate=" + createdDate
					+ ", validUntilDate=" + validUntilDate + "]";
		}
		
	}
	
	public static class KeyInfo {
		public String algorithm;
		public String keyId;
		public KeyPair keyPair;
		public String pubKeyB64;
		public String privKeyB64;
		public long createdDate;
		public long validUntilDate;
	}

}

