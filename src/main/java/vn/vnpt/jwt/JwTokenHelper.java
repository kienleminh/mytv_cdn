/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.vnpt.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Nguyen Nhu Son
 */
public class JwTokenHelper {
    
    // The JWT signature algorithm we will be using to sign the token
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
 
    // Keys used with HS256 MUST have a size >= 256 bits
    private static final String SECRET_KEY = "token-VAS-s0ftwar3-Dep@rtment-Media-DigitalEcoSystem";//"token-demotest-for-base-authentication-with-jwt-example";
 
    private static final String ISSUER = "vertical-demo";
    
    public static String createJWT(User user) {
 
        // Get the current time
        long currentTimeInMillis = System.currentTimeMillis();
        Date now = new Date(currentTimeInMillis);
  
        // Will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, SIGNATURE_ALGORITHM.getJcaName());
 
        // Sets the JWT Claims sub (subject) value
        Claims claims = Jwts.claims().setSubject(user.username);
        claims.put("roles", user.roles);
        claims.put("allow_ips", user.allowIPs);
 
        // Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder() // Configured and then used to create JWT compact serialized strings
                .setClaims(claims).setId(UUID.randomUUID().toString()) // Sets the JWT Claims jti (JWT ID) value
                .setIssuedAt(now) // Sets the JWT Claims iat (issued at) value
                .setIssuer(ISSUER) // Sets the JWT Claims iss (issuer) value
                .signWith(signingKey, SIGNATURE_ALGORITHM); 
        // Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }
    
    public static Claims decodeJWT(String jwt) {
        // This line will throw an exception if it is not a signed JWS (as expected)
        try {
            return Jwts.parser() // Configured and then used to parse JWT strings
                    .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                    .parseClaimsJws(jwt) // Parses the specified compact serialized JWS string based
                    .getBody();
        } catch (Exception e) {            
            return null;
        }
    }
    
    public static void main(String[] args) {
        //user truy cập quyền CDN không validate IP
        //news, music, movie, clip, health
        
        User user = new User();
        user.username = "mytv";
        user.roles = Arrays.asList(Role.ROLE_CDN);
        String jwtForEcosystem = JwTokenHelper.createJWT(user);
        System.out.println(jwtForEcosystem);
        
        //user truy cập quyền CDN có validate chặn theo IP
//        user = new User();
//        user.username = "clip";
//        user.roles = Arrays.asList(Role.ROLE_CDN);
//        user.allowIPs = Arrays.asList("127.0.0.1");
//        String jwtForEcosystem2 = JwTokenHelper.createJWT(user);
//        System.out.println("jwtForCDN with ip validate:" + jwtForEcosystem2);
    }
}
