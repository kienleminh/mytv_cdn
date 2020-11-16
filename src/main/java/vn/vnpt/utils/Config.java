package vn.vnpt.utils;

import java.io.File;
import org.apache.log4j.Logger;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.HierarchicalINIConfiguration;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Config {

    private static final Logger LOGGER = Logger.getLogger(Config.class);
    private static String PATH = "../etc/";

    private static CompositeConfiguration configIni = null;
    public static HierarchicalINIConfiguration configHieIni = null;

    public static void load(String path) {
        PATH = path;
        LOGGER.info("[START] [loadINI] Load INI config from path:" + PATH);
        try {
            configIni = new CompositeConfiguration();
            configHieIni = new HierarchicalINIConfiguration();
            configHieIni.setDelimiterParsingDisabled(true);
            configHieIni.load(new File(PATH + "app.properties"));
            configIni.addConfiguration(configHieIni);

            Constant.UPLOAD_PATH = configIni.getString("UPLOAD_PATH", "");
            Constant.CP_PATH = configIni.getString("CP_PATH", "");
            Constant.CDN_URL = configIni.getString("CDN_URL", "");
            Constant.CDN_KEY = configIni.getString("CDN_KEY", "");
            Constant.CDN_SECRET = configIni.getString("CDN_SECRET", "");

            String cdn_info = MyTvCDN.getCDNInfo();
            if (cdn_info != null) {
                try {
                    JSONParser jsonParser = new JSONParser();
                    JSONObject jsonResponse = (JSONObject) jsonParser.parse(cdn_info.toString());
                    Constant.CDN_BASE_URL = jsonResponse.get("put").toString();
                } catch (Exception e) {
                }
            }

            String list_user_ftp = configIni.getString("LIST_USER_FTP", "");
            String path_user_ftp = configIni.getString("PATH_USER_FTP", "");
            String[] arrUser = list_user_ftp.split("#");
            String[] arrPath = path_user_ftp.split("#");
            for (int i = 0; i < arrUser.length; i++) {
                Constant.HASH_FTP.put(arrUser[i].trim(), arrPath[i].trim());
            }

        } catch (Exception e) {
            LOGGER.info("[ERROR] [loadINI] load INI config error:" + e);
        }
    }
    
    public static String getPath() {
        return PATH;
    }

}
