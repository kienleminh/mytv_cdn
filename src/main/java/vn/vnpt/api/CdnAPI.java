/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.vnpt.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import vn.vnpt.db.DBTools;
import vn.vnpt.utils.Constant;
import vn.vnpt.utils.MyTvCDN;
import vn.vnpt.utils.Util;
import java.util.regex.Pattern;

/**
 *
 * @author Nguyen Nhu Son
 */
@Path("/")
public class CdnAPI {

    private static final Logger LOGGER = Logger.getLogger(CdnAPI.class);
    private static final String FILE_NAME_CDN = "^[\\w-/.]*$";

//    DBTools objDB = new DBTools();

    @Context
    private ContainerRequestContext requestContext;

    private String getContextProperty(String name) {
        Object object = requestContext.getProperty(name);
        if (object == null) {
            return "";
        }
        return (String) object;
    }

    @GET
    @Path("hello")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String hello() {
        JSONObject jObject = new JSONObject();
        jObject.put("error_code", "0");
        jObject.put("message", "Hello success");
        return jObject.toString();
    }
    public String hi() {
        JSONObject jObject = new JSONObject();
        jObject.put("code", "1387");
        jObject.put("mess", "Hi");
        return jObject.toString();
    }
    
    @GET
    @Path("sayhi")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String sayhi() {
        JSONObject jObject = new JSONObject();
        jObject.put("name", "Kien");
        jObject.put("birth", 1997);
        jObject.put("address", "Hanoi");
        jObject.put("score", 8 );
        
        System.out.println("Func sayhi is called");
        System.out.println("Status OK");
        
        return jObject.toString();
    }

    @POST
    @Path("upload_file")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String upload_file(@FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition fileMetaData,
            @DefaultValue("") @FormDataParam("file_name") String file_name) {

        JSONObject jObject = new JSONObject();
        String error_code = "", message = "";

        String username = getContextProperty("username");
        final String UPLOAD_PATH = Constant.UPLOAD_PATH;
        String fileName = fileMetaData.getFileName();
        if (file_name.length() > 0) {
            fileName = file_name;
        }

        if (Pattern.matches(FILE_NAME_CDN, fileName)) {

            String path_cp = UPLOAD_PATH + "/" + username;
            File file_path_cp = new File(path_cp);
            if (!file_path_cp.exists()) {
                file_path_cp.mkdir();
            }

            final String fullFilePath = UPLOAD_PATH + "/" + username + "/" + fileName;
            if (fileName.split("/").length > 1) {
                String[] arrFile = fileName.split("/");
                String tmp = UPLOAD_PATH + "/" + username;
                File tmp_folder = null;
                for (int i = 0; i < arrFile.length - 1; i++) {
                    tmp = tmp + "/" + arrFile[i];
                    tmp_folder = new File(tmp);
                    if (!tmp_folder.exists()) {
                        tmp_folder.mkdir();
                    }
                }
            }

            final File file = new File(fullFilePath);
            try {
                int read = 0;
                byte[] bytes = new byte[1024];
                OutputStream out = new FileOutputStream(file);
                while ((read = fileInputStream.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                out.flush();
                out.close();
            } catch (Exception e) {
                LOGGER.error(e);

                error_code = "-99";
                message = "Upload file to server error:" + e.toString();

                jObject.put("error_code", error_code);
                jObject.put("message", message);

                return jObject.toString();
            }

            //Ket noi den CDN MyTV            
            String errCode = MyTvCDN.putFile(Constant.CDN_BASE_URL, file, username);
            if (errCode == null || errCode.startsWith("5") || errCode.startsWith("4")) {
                error_code = errCode;
                message = "Upload file to CDN error";
            } else {
                error_code = "0";
                message = "Upload file to CDN success";
            }

            //Xoa file tren server
            if (file.exists()) {
                if (!file.delete()) {
                    LOGGER.error("Delete file server error:" + fullFilePath);
                }
            }
        } else {
            error_code = "500";
            message = " Ten file co ky tu dac biet";
        }

        jObject.put("error_code", error_code);
        jObject.put("message", message);
        LOGGER.info("upload_file : REQ:" + file_name + "==>RES:" + jObject.toString());
        return jObject.toString();
    }


    @POST
    @Path("get_file")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String get_file(String body) {
        String username = getContextProperty("username");
        JSONObject jObject = new JSONObject();
        try {
            jObject = new JSONObject(body);
        } catch (Exception e) {
        }
        String file_name = jObject.optString("file_name");

        if (file_name.isEmpty()) {
            return Json.invalidParam("file_name").toString();
        }

        String cdnFileName = "/" + username + "/" + file_name;
        if (file_name.contains("hls/") || file_name.contains("dash/")) {
            cdnFileName = file_name;
        }
        if (username.equals("movie") && file_name.contains("mytv_bhd/")) {
            cdnFileName = file_name;
        }
        //Lay file tren CDN
        String url_file_cdn = "", error_code = "", message = "";
        try {
            url_file_cdn = MyTvCDN.getFile(cdnFileName);
            if (url_file_cdn == null) {
                error_code = "-1";
                message = "CDN can not get file from CDN";
            } else {
                error_code = "0";
                message = "Get file success";
            }
        } catch (Exception e) {
            LOGGER.error(e);
            error_code = "500";
            message = "System busy";
            url_file_cdn = "";
        }

        JSONObject resObject = new JSONObject();
        resObject.put("error_code", error_code);
        resObject.put("message", message);
        resObject.put("result", url_file_cdn);
        LOGGER.info("get_file: REQ:" + body + "==>RES:" + resObject.toString());
        return resObject.toString();
    }
}
