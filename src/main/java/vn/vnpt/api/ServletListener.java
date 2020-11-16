/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.vnpt.api;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import vn.vnpt.db.Database;
import vn.vnpt.utils.Config;

/**
 * Web application lifecycle listener.
 *
 * @author Nguyen Nhu Son
 */
public class ServletListener implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(ServletListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String configPath = null;
        try {
            InitialContext initialContext = new InitialContext();
            Context environmentContext = (Context) initialContext.lookup("java:/comp/env");
            //configPath = (String) environmentContext.lookup("configPath");

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (configPath == null) {
            configPath = "C:\\Users\\admin\\Desktop\\MyTV_CDN\\MyTV_CDN\\src\\main\\java\\vn\\vnpt\\config\\";
        }

        if (configPath != null) {
            System.out.println("Environment configPath-->" + configPath);
            PropertyConfigurator.configure(configPath + "log4j.properties");
            Config.load(configPath);
            Database.start();
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("contextDestroyed!!!");
        Database.shutdown();
    }
}
