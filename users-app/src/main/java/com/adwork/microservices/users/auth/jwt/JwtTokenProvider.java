package com.adwork.microservices.users.auth.jwt;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.assertj.core.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.adwork.microservices.users.service.KeysService;
import com.adwork.microservices.users.service.KeysService.PublicKeyInfo.PrivateKeyInfo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SigningKeyResolver;
import io.jsonwebtoken.SigningKeyResolverAdapter;

@Component
public class JwtTokenProvider {

	@Value("${spring.application.name}")
	private String applicationName;

	@Value("${security.jwt.token.expire-length:3600000}") // 1 hour
	private long tokenValidityMs;

	@Autowired
	KeysService keysService;

	public String createToken(String email, Object[] roles, String verifyUrl) {
		PrivateKeyInfo key = keysService.getCurrentPrivateKey();
		return Jwts.builder()
				.setClaims(claims(email, Arrays.asList(roles)))
				.setIssuedAt(time(0))
				.setIssuer(applicationName)
				.setExpiration(time(tokenValidityMs))
				.setHeaderParam("kid", key.keyId)
				.setHeaderParam("url", verifyUrl)
				.signWith(SignatureAlgorithm.RS512, key.key)
				.compact();
	}

	private Claims claims(String email, List<Object> roles) {
		Claims claims = Jwts.claims().setSubject(email);
		claims.put("roles", roles.stream()
				.map(r -> r.toString())
				.filter(Objects::nonNull)
				.collect(Collectors.toList()));
		return claims;
	}

	private Date time(long shift) {
		return new Date(new Date().getTime() + shift);
	}
	
	public String extractToken(HttpServletRequest request) {
		String token = null;
		String authHeader = request.getHeader("Authorization");
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7, authHeader.length());
		}
		return token;
	}

	public boolean validateToken(String token) {
		boolean result = false;
		try {
			Claims claims = Jwts.parser()
				.setSigningKeyResolver(signingKeyResolver)
				.parseClaimsJws(token)
				.getBody();
			System.out.println(" > subject: " + claims.getSubject() + ", roles: " + claims.get("roles", ArrayList.class));
			String issuer = claims.getIssuer();
			String email = claims.getSubject();
			@SuppressWarnings("unchecked")
			ArrayList<String> roles = claims.get("roles", ArrayList.class);
			result = issuedByMe(issuer) && userHasRoles(email, roles);
		} catch (Exception ex) {
			System.out.println("Token validation error:" + ex);
		}
		return result;
	}
	
	public Authentication getAuthentication(String token) {
		return new UsernamePasswordAuthenticationToken(getSubject(token), null, getAuthorities(token));
	}
	
	public String getSubject(String token) {
		return Jwts.parser()
				.setSigningKeyResolver(signingKeyResolver)
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
	
	@SuppressWarnings({ "unchecked" })
	public List<String> getRoles(String token) {
		Claims claims = Jwts.parser()
				.setSigningKeyResolver(signingKeyResolver)
				.parseClaimsJws(token)
				.getBody();
		 List<String> roles = claims.get("roles", ArrayList.class);
		 return roles;
	}
	
	public List<GrantedAuthority> getAuthorities(String token) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		getRoles(token).stream()
			.map(role -> new SimpleGrantedAuthority(role.toString()))
			.forEach(authorities::add);
		return authorities;
	}
	
	private SigningKeyResolver signingKeyResolver = new SigningKeyResolverAdapter() {
		@Override
		public Key resolveSigningKey(@SuppressWarnings("rawtypes") JwsHeader header, Claims claims) {
			String keyId = (String) header.get("kid");
			return keyId != null ? publicKey(keyId) : null;
		}
	};
	
	private Key publicKey(String keyId) {
		return keysService.getKeyInfo(keyId).keyPair.getPublic();
	}
	
	private boolean issuedByMe(String issuer) {
		return applicationName.equals(issuer);
	}
	
	private boolean userHasRoles(String email, List<String> roles) {
		return true; //TODO: check user roles
	}
	 
}
