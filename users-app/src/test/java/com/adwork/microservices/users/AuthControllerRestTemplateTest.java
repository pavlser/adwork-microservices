package com.adwork.microservices.users;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.adwork.microservices.users.service.KeysService;
import com.adwork.microservices.users.service.KeysService.KeyInfo;
import com.adwork.microservices.users.service.KeysService.PublicKeyInfo;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UsersApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerRestTemplateTest {

	@LocalServerPort
	private int port;

	TestRestTemplate restTemplate = new TestRestTemplate();

	@Autowired
	private KeysService keysService;

	@Test
	public void shouldReturnPublicKey() throws Exception {
		// load public key from server
		PublicKeyInfo pubkey = restTemplate.getForObject(url("/api/auth/public-key"), PublicKeyInfo.class);
		
		assertNotNull(pubkey);
		
		// obtain keys from service
		KeyInfo key = keysService.getKeyInfo(pubkey.keyId);
		
		// compare both keys
		assertNotNull(key);
		assertEquals(pubkey.keyId, key.keyId);
		assertEquals(pubkey.publicKey, key.pubKeyB64);
	}
	
	private String url(String path) {
		return "http://localhost:" + port + path;
	}

}
