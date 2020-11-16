/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.vnpt.api;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.json.JSONObject;

/**
 *
 * @author admin
 */
@Path("/a")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class aHello {

    @POST
    public String ahello(String body) {
        JSONObject jObject = new JSONObject(body);
        String file = jObject.optString("file_name");
        System.out.println("file: " + file);
        if (file.equalsIgnoreCase("Kien")) {
            return "<html> " + "<title>" + "Rest Page" + "</title>"
                    + "<body><h2>" + "Okie " + file + "</body></h2>" + "</html> ";
        } else {
//            return "<html> " + "<title>" + "Rest Page" + "</title>"
//                    + "<body><h2>" + "aHello đó" + "</body></h2>" + "</html> ";
            return (String) jObject.toString();
            //return body;
        }
    }
}
