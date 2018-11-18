package users;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.adwork.microservices.users.dto.AuthInfo;
import com.adwork.microservices.users.service.KeysService.PublicKeyInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthTest {

	public static void main(String[] args) {
		try {
			Client client = ClientBuilder.newClient();
			
			// load public key from server
			WebTarget target = client.target("http://localhost:8090/api/auth/public-key");
			PublicKeyInfo key = target.request(MediaType.APPLICATION_JSON).get(PublicKeyInfo.class);
			System.out.println(key);

			// create auth info
			AuthInfo auth = new AuthInfo("admin@adwork-microservices.com", "admin");
			ObjectMapper mapper = new ObjectMapper();
			String authStr = mapper.writeValueAsString(auth);
			System.out.println(authStr);
			
			// convert base64 str key to rsa public key
			byte[] keyBytes = Base64.getDecoder().decode(key.publicKey.getBytes());
			X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey publicKey = keyFactory.generatePublic(spec); 
			
			// encrypt auth info
			Cipher encryptor = Cipher.getInstance("RSA");
			encryptor.init(Cipher.ENCRYPT_MODE, publicKey);
	        byte[] encryptedAuth = encryptor.doFinal(authStr.getBytes(UTF_8));
	        String encodedAuth = Base64.getEncoder().encodeToString(encryptedAuth);
	        
	        System.out.println("> encodedAuth=" + encodedAuth);
	        
	        // post auth
	        target = client.target("http://localhost:8090/api/auth/create-token?kid="+key.keyId);
	        Map<String, String> resp = target.request(MediaType.APPLICATION_JSON)
	        	.post(Entity.entity(encodedAuth, MediaType.APPLICATION_JSON), HashMap.class);
			System.out.println(resp);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
