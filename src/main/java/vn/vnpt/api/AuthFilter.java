/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.vnpt.api;

import io.jsonwebtoken.Claims;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import vn.vnpt.jwt.JwTokenHelper;
import vn.vnpt.jwt.User;
import vn.vnpt.jwt.UserRoles;

/**
 *
 * @author Nguyen Nhu Son
 */
@PreMatching
@Provider
public class AuthFilter implements ContainerRequestFilter {

    private final Logger LOGGER = Logger.getLogger(AuthFilter.class);
    private static final String AUTHENTICATION_SCHEME = "Bearer";
    final String PREFIX_PATH = "";

    @Context
    private HttpServletRequest httpServletRequest;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
//        LOGGER.info("IP:" + servletRequest.getRemoteAddr());
//        String msisdn = requestContext.getUriInfo().getQueryParameters().getFirst("msisdn");
//        LOGGER.info("msisdn:" + msisdn);
//        Response ACCESS_DENIED = Response.status(Response.Status.UNAUTHORIZED).entity("You cannot access this resource").build();
//        requestContext.abortWith(ACCESS_DENIED);
//        if (requestContext.getMethod().equals("GET")) {
//            requestContext.setMethod("POST");
//        }

        final URI absolutePath = requestContext.getUriInfo().getAbsolutePath();
        String requestPath = absolutePath.getPath();
        //requestPath = requestPath.substring(PREFIX_PATH.length());
        System.out.println("Resource path:" + requestPath);
        
        String ipAddress = httpServletRequest.getRemoteAddr();
        System.out.println("ipAddress:" + ipAddress);
        
        // (1) Get Token Authorization from the header
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        
        // (2) Validate the Authorization header
//        if (!isTokenBasedAuthentication(authorizationHeader)) {
//            abortWithUnauthorized(requestContext);
//            return;
//        }
//        // (3) Extract the token from the Authorization header
//        String token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();
//        try {
//            final Claims claims = JwTokenHelper.decodeJWT(token);
//            if (claims == null) {
//                abortWithUnauthorized(requestContext);
//                return;
//            }
//            User user = new User();
//            user.username = claims.getSubject();
//            user.roles = (List<String>) claims.get("roles");
//            user.allowIPs = (List<String>) claims.get("allow_ips");
//            
//            //LOGGER.info(claims.toString());
//            boolean ipCheck = UserRoles.checkAllowIPs(user.allowIPs, ipAddress);
//            if (!ipCheck) {
//                abortWithInvalidIP(requestContext);
//                return;
//            }
//            boolean authorized = UserRoles.checkRolesByUser(user);            
//            if (!authorized) {
//                abortWithUnauthorized(requestContext);
//            }            
//            requestContext.setProperty("username", user.username);
//        } catch (Exception e) {
//            e.printStackTrace();
//            abortWithUnauthorized(requestContext);
//        }
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext) {
        // Abort the filter chain with a 401 status code response
        // The WWW-Authenticate header is sent along with the response
        JSONObject jObject = new JSONObject();
        jObject.put("error_code", "401");
        jObject.put("message", "UNAUTHORIZED"); 
        Response respone = Response.status(Response.Status.UNAUTHORIZED) // 401 Unauthorized
                .entity(jObject.toString()) // the response entity
                .build();
        requestContext.abortWith(respone);
    }
    
    private void abortWithInvalidIP(ContainerRequestContext requestContext) {
        // Abort the filter chain with a 401 status code response
        // The WWW-Authenticate header is sent along with the response
        JSONObject jObject = new JSONObject();
        jObject.put("error_code", "401");
        jObject.put("message", "INVALID IP ADDRESS"); 
        Response respone = Response.status(Response.Status.UNAUTHORIZED) // 401 Unauthorized
                .entity(jObject.toString()) // the response entity
                .build();
        requestContext.abortWith(respone);
    }
    
    private boolean isTokenBasedAuthentication(String authorizationHeader) {
        // Check if the Authorization header is valid
        // It must not be null and must be prefixed with "Bearer" plus a whitespace
        // The authentication scheme comparison must be case-insensitive
        return authorizationHeader != null
                && authorizationHeader.toLowerCase().startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
    }
}
