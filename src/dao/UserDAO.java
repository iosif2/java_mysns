package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.NamingException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import util.ConnectionPool;

public class UserDAO {
    public UserDAO() {
    }

    public boolean insert(String uid, String jsonstr) throws NamingException, SQLException {
        Connection conn = ConnectionPool.get();
        PreparedStatement stmt = null;

        boolean result;
        try {
            String sql = "INSERT INTO user(id, jsonstr) VALUES(?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, uid);
            stmt.setString(2, jsonstr);
            int count = stmt.executeUpdate();
            result = count == 1;
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

    public boolean exists(String uid) throws NamingException, SQLException {
        Connection conn = ConnectionPool.get();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        boolean result;
        try {
            String sql = "SELECT id FROM user WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, uid);
            rs = stmt.executeQuery();
            result = rs.next();
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

    public boolean delete(String uid) throws NamingException, SQLException {
        Connection conn = ConnectionPool.get();
        PreparedStatement stmt = null;

        boolean result;
        try {
            String sql = "DELETE FROM user WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, uid);
            int count = stmt.executeUpdate();
            result = count == 1;
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

    public int login(String uid, String upass) throws NamingException, SQLException, ParseException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT jsonstr FROM user WHERE id = ?";
            conn = ConnectionPool.get();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, uid);
            rs = stmt.executeQuery();
            if (!rs.next()) {
                return 1;
            }

            String jsonstr = rs.getString("jsonstr");
            JSONObject obj = (JSONObject)(new JSONParser()).parse(jsonstr);
            String pass = obj.get("password").toString();
            if (!upass.equals(pass)) {
                return 2;
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

        return 0;
    }

    public String getList() throws NamingException, SQLException {
        Connection conn = ConnectionPool.get();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String result;
        try {
            String sql = "SELECT jsonstr FROM user";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            String str = "[";

            for(int cnt = 0; rs.next(); str = str + rs.getString("jsonstr")) {
                if (cnt++ > 0) {
                    str = str + ", ";
                }
            }

            result = str + "]";
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

    public String get(String uid) throws NamingException, SQLException {
        Connection conn = ConnectionPool.get();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String result;
        try {
            String sql = "SELECT jsonstr FROM user WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, uid);
            rs = stmt.executeQuery();
            result = rs.next() ? rs.getString("jsonstr") : "{}";
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

    public boolean update(String uid, String jsonstr) throws NamingException, SQLException {
        Connection conn = ConnectionPool.get();
        PreparedStatement stmt = null;

        boolean var8;
        try {
            String sql = "UPDATE user SET jsonstr = ? WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, jsonstr);
            stmt.setString(2, uid);
            int count = stmt.executeUpdate();
            var8 = count == 1;
        } finally {
            if (stmt != null) {
                stmt.close();
            }

            if (conn != null) {
                conn.close();
            }

        }

        return var8;
    }
}
