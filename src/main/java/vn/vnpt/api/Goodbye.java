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
 * @author Kien Le Minh
 */
@Path("/goodbye")
@Produces(MediaType.TEXT_HTML)
public class Goodbye {
    @GET
    public String goodbye(@Context HttpServletRequest requestContext) {
        return "<html> "+"<title>"+"Dynamic Page"+"</title>"
                +"<body>"+"HAHAHA!"+"</body>"+"</html>";
    }
}
