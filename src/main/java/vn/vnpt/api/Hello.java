/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.vnpt.api;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Nguyen Nhu Son
 */
@Path("/")
@Produces(MediaType.TEXT_HTML)
public class Hello {

    @GET
    public String hello(@Context HttpServletRequest requestContext) {
        //System.out.println("IP:" + requestContext.getRemoteAddr());
        return "<html> " + "<title>" + "Rest Page" + "</title>"
                + "<body><h2>" + "Hello CDN api!" + "</body></h2>" + "</html> ";
    }    
}
