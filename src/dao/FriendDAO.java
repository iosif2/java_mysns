package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import javax.naming.NamingException;
import util.ConnectionPool;

public class FriendDAO {
    public FriendDAO() {
    }

    public String insert(String uid, String frid) throws NamingException, SQLException, ParseException {
        Connection conn = ConnectionPool.get();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String result;
        try {
            String sql = "SELECT id FROM friend WHERE id = ? AND frid = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, uid);
            stmt.setString(2, frid);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return "EX";
            }

            stmt.close();
            sql = "INSERT INTO friend VALUES(?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, uid);
            stmt.setString(2, frid);
            int count = stmt.executeUpdate();
            result = count == 1 ? "OK" : "ER";
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (stmt != null) {
                stmt.close();
            }

            if (conn != null) {
                conn.close();
            }

        }

        return result;
    }

    public String remove(String uid, String frid) throws NamingException, SQLException, ParseException {
        Connection conn = ConnectionPool.get();
        PreparedStatement stmt = null;

        String result;
        try {
            String sql = "DELETE FROM friend WHERE id = ? AND frid = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, uid);
            stmt.setString(2, frid);
            int count = stmt.executeUpdate();
            result = count == 1 ? "OK" : "ER";
        } finally {
            if (stmt != null) {
                stmt.close();
            }

            if (conn != null) {
                conn.close();
            }

        }

        return result;
    }

    public String getList(String uid) throws NamingException, SQLException, ParseException {
        Connection conn = ConnectionPool.get();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT frid FROM friend WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, uid);
            rs = stmt.executeQuery();
            String str = "";

            int cnt;
            for(cnt = 0; rs.next(); str = str + "\"" + rs.getString("frid") + "\"") {
                if (cnt++ > 0) {
                    str = str + ",";
                }
            }

            if (cnt != 0) {
                rs.close();
                stmt.close();
                sql = "SELECT jsonstr FROM user WHERE id IN (" + str + ")";
                stmt = conn.prepareStatement(sql);
                rs = stmt.executeQuery();
                str = "[";

                for(cnt = 0; rs.next(); str = str + rs.getString("jsonstr")) {
                    if (cnt++ > 0) {
                        str = str + ",";
                    }
                }

                str = str + "]";
                return str;
            }
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (stmt != null) {
                stmt.close();
            }

            if (conn != null) {
                conn.close();
            }

        }

        return "[]";
    }
}
