package vn.vnpt.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class MyTvCDN {

    private static final Logger logger = Logger.getLogger(MyTvCDN.class);

    private static final String URL_STATUS = Constant.CDN_URL + "keystat";
    private static final String URL_GET_FILE = Constant.CDN_URL + "get";
    private static final String URL_CDN_TRANSCODE = Constant.CDN_URL + "file";
    private static final String CDN_KEY = Constant.CDN_KEY;
    private static final String CDN_SECRET = Constant.CDN_SECRET;

    private static String getURI(String url) {
        int idx = url.indexOf('/', "https://".length());
        if (idx > 0) {
            return url.substring(idx);
        }
        return "";
    }

    public static String getCDNInfo() {
        String url = URL_STATUS + "?k=" + CDN_KEY + "&nonce=" + System.nanoTime();
        String uriSign = getURI(url);
        String hmacSign = Util.hmac512(CDN_SECRET, uriSign);

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpGet request = new HttpGet(url);
            request.addHeader("apisign", hmacSign);
            HttpResponse response = httpClient.execute(request);
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = bufReader.readLine()) != null) {
                builder.append(line);
                builder.append(System.lineSeparator());
            }            
            logger.info("getCDNInfo==>REQ:" + url + "==>RES:" + builder.toString());
            return builder.toString();
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static String putFile(String baseUrl, File uploadFile, String username) {
        final String fileName = "/" + username + "/" + uploadFile.getName();
        final String url = baseUrl + fileName + "?k=" + CDN_KEY + "&nonce=" + System.nanoTime();
        String uriSign = getURI(url);
        String hmacSign = Util.hmac512(CDN_SECRET, uriSign);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPut request = new HttpPut(url);
            FileEntity fileEntity = new FileEntity(uploadFile);
            request.setEntity(fileEntity);
            request.addHeader("apisign", hmacSign);
            HttpResponse response = httpClient.execute(request);
            int response_code = response.getStatusLine().getStatusCode();
            logger.info("putFile==>REQ:" + url + "==>RES:" + response_code);
            return response_code + "";
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (Exception e) {
            }
        }

        return null;

    }

    public static String deleteFile(String baseUrl, String fileName) {
        final String url = baseUrl + fileName + "?k=" + CDN_KEY + "&nonce=" + System.nanoTime();
        String uriSign = getURI(url);
        String hmacSign = Util.hmac512(CDN_SECRET, uriSign);
        String ret = "-1";
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpDelete request = new HttpDelete(url);
            request.addHeader("apisign", hmacSign);
            HttpResponse response = httpClient.execute(request);
            int responseCode = response.getStatusLine().getStatusCode();
            ret = "" + responseCode;
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (Exception e) {
            }
        }
        logger.info("deleteFile==>REQ:" + url + "==>RES:" + ret);
        return ret;
    }

    public static String getFile(String filePath) throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        final String ip = inetAddress.getHostAddress();
        String url = URL_GET_FILE + "?k=" + CDN_KEY + "&file=" + filePath + "&ip=" + ip;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpGet request = new HttpGet(url);
            HttpResponse response = httpClient.execute(request);
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = bufReader.readLine()) != null) {
                builder.append(line);
                builder.append(System.lineSeparator());
            }

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonResponse = (JSONObject) jsonParser.parse(builder.toString());

            logger.info("getFile==>REQ:" + url + "==>RES:" + builder.toString());
            return jsonResponse.get("object").toString();
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static String transcode(String fileName, String mode) {
        final String url = URL_CDN_TRANSCODE + "?k=" + CDN_KEY + "&nonce=" + System.nanoTime() + "&file=" + fileName + "&mode=" + mode;
        String uriSign = getURI(url);
        String hmacSign = Util.hmac512(CDN_SECRET, uriSign);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpDelete request = new HttpDelete(url);
            request.addHeader("apisign", hmacSign);
            HttpResponse response = httpClient.execute(request);
            int responseCode = response.getStatusLine().getStatusCode();
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = bufReader.readLine()) != null) {
                builder.append(line);
                builder.append(System.lineSeparator());
            }

            String res = builder.toString();
            logger.info("transcode==>REQ:" + url + "==>RES:" + res);
            return res;
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (Exception e) {
            }
        }
        return null;
    }
}
