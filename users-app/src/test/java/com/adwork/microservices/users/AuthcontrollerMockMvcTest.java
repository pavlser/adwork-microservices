package com.adwork.microservices.users;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.adwork.microservices.users.service.KeysService;
import com.adwork.microservices.users.service.KeysService.KeyInfo;
import com.adwork.microservices.users.service.KeysService.PublicKeyInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthcontrollerMockMvcTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private KeysService keysService;
	
	@Test
	public void shouldReturnPublicKey() throws Exception {
		PublicKeyInfo key = keysService.getPublicKeyInfo();
		
		MvcResult result = this.mockMvc.perform(get("/api/auth/public-key"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("publicKey")))
			.andExpect(content().string(containsString("keyId")))
			.andExpect(jsonPath("$.publicKey").value(key.publicKey))
			.andReturn();

		PublicKeyInfo pubkey = getPublicKeyInfo(result);
		KeyInfo secret = keysService.getKeyInfo(pubkey.keyId);
		
		assertEquals(pubkey.publicKey, secret.pubKeyB64);
	}
	
	private PublicKeyInfo getPublicKeyInfo(MvcResult result) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		PublicKeyInfo pubkey = mapper.readValue(result.getResponse().getContentAsString(), PublicKeyInfo.class);
		return pubkey;
	}

}
