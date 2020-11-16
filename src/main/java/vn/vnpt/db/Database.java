/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.vnpt.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import vn.vnpt.utils.Config;

/**
 *
 * @author Nguyen Nhu Son
 */
public class Database {

    private static final Logger LOGGER = Logger.getLogger(Database.class);
    private static volatile HikariDataSource hikariDataSource = null;

    public static Connection getConnection() {
        Connection connection = null;
        try {
            HikariDataSource hikariDataSource = Database.hikariDataSource;
            if (hikariDataSource == null) {
                synchronized (Database.class) {
                    hikariDataSource = Database.hikariDataSource;
                    if (hikariDataSource == null) {
                        LOGGER.info("INIT HIKARI POOL:" + Config.getPath() + "hikari.properties");
                        HikariConfig config = new HikariConfig(Config.getPath() + "hikari.properties");
                        Database.hikariDataSource = hikariDataSource = new HikariDataSource(config);
                    }
                }
            }
            connection = hikariDataSource.getConnection();           
        } catch (Exception e) {
            LOGGER.error("GET CONNECTION ERROR:" + e);
        }
        return connection;
    }
    
    public static void start() {
        Connection connection = getConnection();
        close(connection);
    }
    
    public static void shutdown() {
        if(hikariDataSource != null) {
            hikariDataSource.close();
            hikariDataSource = null;
        }
    }

    public static void rollback(Connection connection) {
        try {
            if (connection != null) {
                connection.rollback();
            }
        } catch (SQLException e) {
            LOGGER.error(e);
        }
    }

    public static void close(ResultSet resultSet, Statement statement, Connection connection) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            LOGGER.error(e);
        }
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            LOGGER.error(e);
        }
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.error(e);
        }
    }

    public static void close(Statement statement, Connection connection) {
        close(null, statement, connection);
    }

    public static void close(ResultSet resultSet, Statement statement) {
        close(resultSet, statement, null);
    }

    public static void close(Statement statement) {
        close(null, statement, null);
    }
    
    public static void close(ResultSet rs) {
        close(rs, null, null);
    }

    public static void close(Connection connection) {
        close(null, null, connection);
    }
}
