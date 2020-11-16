/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.vnpt.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author Nguyen Nhu Son
 */
public class Util {

    private static final Logger LOGGER = Logger.getLogger(Util.class);
    //private static final String pattern = "dd/MM/yyyy HH:mm:ss";
    //private static final SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.US);
    private static final Pattern VINAPHONE = Pattern.compile("^(91|94|123|124|125|127|129|88)\\d{7}");

    public static String removeAccent(String s) {
        if (s == null) {
            return "";
        }
        return Unicode2Nosign.convert(s);
    }

    public static String convertAscii(String s) {
        if (s == null) {
            return "";
        }
        s = Unicode2Nosign.convert(s);
        s = s.replaceAll("[^a-zA-Z0-9]", "");
        s = s.toLowerCase();
        return s;
    }

    public static boolean checkAuthKey(String apiKey, String msisdn, String trans) {
        if (trans.length() <= 10 && apiKey.length() <= 10) {
            return false;
        }
        String string = trans.substring(0, 9) + msisdn + "je<Nt5s6Wd6k)";
        String tempKey = DigestUtils.md5Hex(string);
        boolean result = apiKey.equals(tempKey);
        LOGGER.info("checkAuthKey apiKey->" + apiKey + ", msisdn->" + msisdn + ", trans->" + trans + ", res:" + result);
        return result;
    }

    public static String httpGet(String httpUrl) {
        URLConnection connection;
        StringBuilder buffer = new StringBuilder();
        //LOGGER.info(httpUrl);
        try {
            URL url = new URL(httpUrl);
            connection = url.openConnection();
            connection.setConnectTimeout(20000);
            connection.setReadTimeout(20000);
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    buffer.append(line);
                }
            }
        } catch (Exception e) {
            LOGGER.error("http get error:" + e);
            return "";
        }
        return buffer.toString();
    }

    public static String getExtensionFile(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        int lastIndex = fileName.lastIndexOf(".");
        if (lastIndex <= 0) {
            return "";
        }
        String ext = fileName.substring(lastIndex);
        return ext;
    }

    public static String hmac512(String key, String data) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA512");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA512");
            sha256_HMAC.init(secret_key);
            return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return "";
    }

    private static final Pattern PHONE_PATTERN = Pattern.compile("\\d{9}||\\d{10}");

    public static boolean checkPhone(String string) {
        Matcher matcher = PHONE_PATTERN.matcher(string.trim());
        return matcher.matches();
    }

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    public static boolean checkEmail(String string) {
        Matcher matcher = EMAIL_PATTERN.matcher(string.trim());
        return matcher.matches();
    }

    public static boolean rename(String oldName, String newName) {
        boolean success = false;        
        // Renaming a File or Directory
        // File (or directory) with old name
        File file = new File(oldName);

        // File (or directory) with new name
        File file2 = new File(newName);
        // Rename file (or directory)
        success = file.renameTo(file2);

        return success;
    }

    public static void main(String[] ars) {
//        System.out.println(Util.checkGroup("giadinh"));
//        String time = ALERT_TIME_FORMAT.format(new Date());
//        System.out.println(checkPhone("0014598999"));      
        //alert("Test alert SMS", "Test alert SMS");
        String[] strs = "MO|Dk|84915553588|1599".split(Pattern.quote("|"));
        System.out.println(strs[1]);

//        System.out.println(encrypt("100"));
    }
}
