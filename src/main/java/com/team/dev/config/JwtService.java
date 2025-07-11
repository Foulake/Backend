package com.team.dev.config;

import com.team.dev.utils.ExtendedUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    public Object extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //Two new method
    //public String extractEntrepriseId(String token){return  extractClaim(token, Claims::getSubject);}
    public Long extractIdEntreprise(String token){
        final Claims claims = extractAllClaim(token);

        return  claims.get("entrepriseId", Long.class) ;
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsTResolver){
        final Claims claims = extractAllClaim(token);
        return  claimsTResolver.apply(claims);
    }

    public  String generateToken(ExtendedUser userDetails){

        return generateToken(new HashMap<>(), userDetails);
    }

    public  String generateRefreshToken(
            ExtendedUser userDetails
    ){
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }
    public  String generateToken(
            Map<String, Object> extactClaims,
            ExtendedUser userDetails
    ){
        return buildToken(extactClaims, userDetails, jwtExpiration);
    }

    public String buildToken(Map<String, Object> extactClaims,
                             ExtendedUser userDetails,
                             long expiration){
        return  Jwts
                .builder()
                .setClaims(extactClaims)
                .setSubject(userDetails.getUsername())
                .claim("entrepriseId", userDetails.getEntrepriseId())
                .claim("roles", userDetails.getAuthorities())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean tokenValid(String token, UserDetails userDetails){
        final String username = (String) extractUsername(token);
        return  (username.equals(userDetails.getUsername())) && !isTokenExpired(token);

    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaim(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
