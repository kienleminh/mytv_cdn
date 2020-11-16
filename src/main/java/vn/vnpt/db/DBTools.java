package vn.vnpt.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class DBTools {

    private static final Logger LOGGER = Logger.getLogger(DBTools.class);

    public int insertFile(String file_name) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        String sSql = "";
        int counts = 0;
        int result = -1;
        try {
            connection = Database.getConnection();

            sSql = "select count(1) as total from CDN_UPLOAD_FTP where FILE_NAME=?";
            statement = connection.prepareStatement(sSql);
            statement.setString(1, file_name);

            rs = statement.executeQuery();
            if (rs.next()) {
                counts = rs.getInt("total");
            }

            try {
                if (rs != null) {
                    rs.close();
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

            if (counts == 0) {

                sSql = "insert into CDN_UPLOAD_FTP (FILE_NAME) values (?)";
                statement = connection.prepareStatement(sSql);
                statement.setString(1, file_name);
                result = statement.executeUpdate();

            }
            else if (counts > 0) {
                sSql = "update CDN_UPLOAD_FTP set STATUS=0, DATE_CREATE=sysdate, LAST_UPDATE=sysdate where FILE_NAME=?";
                statement = connection.prepareStatement(sSql);
                statement.setString(1, file_name);
                result = statement.executeUpdate();
            }

        } catch (Exception e) {
            result = -1;
            e.printStackTrace();
            LOGGER.error(e);
        } finally {
            Database.close(rs, statement, connection);
        }
        return result;
    }

    public int getStatusFile(String file_name) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        String sSql = "";
        int result = -1;
        try {
            connection = Database.getConnection();

            sSql = "select STATUS from CDN_UPLOAD_FTP where FILE_NAME=?";
            statement = connection.prepareStatement(sSql);
            statement.setString(1, file_name);

            rs = statement.executeQuery();
            if (rs.next()) {
                result = rs.getInt("STATUS");
            }

        } catch (Exception e) {
            result = -2;
            e.printStackTrace();
            LOGGER.error(e);
        } finally {
            Database.close(rs, statement, connection);
        }
        return result;
    }

}
