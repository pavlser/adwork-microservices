package com.adwork.microservices.users.jwt;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.adwork.microservices.users.entity.UserRole;
import com.adwork.microservices.users.service.KeysService;
import com.adwork.microservices.users.service.KeysService.KeyInfo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {

	@Value("${security.jwt.token.expire-length:3600000}")
	private long tokenValidityMs = 3600000; // 1 hour

	//@Autowired
	//private MyUserDetails myUserDetails;
	
	@Autowired
	KeysService keysService;

	public String createToken(String email, List<UserRole> roles) {
		KeyInfo key = keysService.getCurrentKey();
		return Jwts.builder()
				.setClaims(claims(email, roles))
				.setIssuedAt(time(0))
				.setExpiration(time(tokenValidityMs))
				.setHeaderParam("kid", key.keyId)
				.signWith(SignatureAlgorithm.RS512, key.keyPair.getPrivate())
				.compact();
	}
	
	private Claims claims(String email, List<UserRole> roles) {
		Claims claims = Jwts.claims().setSubject(email);
		claims.put("roles", roles.stream().map(s -> s.toString()).filter(Objects::nonNull)
				.collect(Collectors.toList()));
		return claims;
	}
	
	private Date time(long shift) {
		return new Date(new Date().getTime() + shift);
	}
/*
	public Authentication getAuthentication(String token) {
		UserDetails userDetails = myUserDetails.loadUserByUsername(getUsername(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	public String getUsername(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}

	public String resolveToken(HttpServletRequest req) {
		String bearerToken = req.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			throw new UserServiceException("Expired or invalid JWT token", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
*/
}
