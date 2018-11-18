package com.adwork.microservices.users.service;

import java.security.KeyPair;
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

	@PostConstruct
	void initialize() {
		this.refreshKeys();
	}
	
	public void refreshKeys() {
		long now = new Date().getTime();
		for (String kid : keys.keySet()) {
			if (keys.get(kid).validUntilDate <= now) {
				keys.remove(kid);
			}
		}
		KeyInfo ki = getNewKeyInfo();
		keys.put(ki.keyId, ki);
	}
	
	private KeyInfo getNewKeyInfo() {
		KeyInfo ki = new KeyInfo();
		ki.keyId = randomStr();
		ki.keyPair = newKeyPair();
		ki.createdDate = new Date().getTime();
		ki.validUntilDate = ki.createdDate + KeyValidityMs;
		ki.privKeyB64 = TextCodec.BASE64URL.encode(ki.keyPair.getPrivate().getEncoded());
		ki.pubKeyB64 = TextCodec.BASE64URL.encode(ki.keyPair.getPublic().getEncoded());
		return ki;
	}
	
	private KeyPair newKeyPair() {
		return RsaProvider.generateKeyPair(KeyStrenght);
	}
	
	private String randomStr() {
		return UUID.randomUUID().toString().replace("-", "");
	}

}

class KeyInfo {
	String keyId;
	KeyPair keyPair;
	String pubKeyB64;
	String privKeyB64;
	long createdDate;
	long validUntilDate;
}
