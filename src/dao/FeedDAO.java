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

public class FeedDAO {
    public boolean insert(String jsonstr) throws NamingException, SQLException, ParseException {
        Connection conn = ConnectionPool.get();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        boolean result;
        try {
            synchronized (this) {
                String sql = "SELECT no FROM feed ORDER BY no DESC LIMIT 1";
                stmt = conn.prepareStatement(sql);
                rs = stmt.executeQuery();
                int max = !rs.next() ? 0 : rs.getInt("no");
                JSONParser parser = new JSONParser();
                JSONObject jsonobj = (JSONObject) parser.parse(jsonstr);
                jsonobj.put("no", max + 1);
                String uid = jsonobj.get("id").toString();

                stmt.close();
                rs.close();
                sql = "INSERT INTO feed(no, id, jsonstr) VALUES(?, ?, ?)";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, max + 1);
                stmt.setString(2, uid);
                stmt.setString(3, jsonobj.toJSONString());
                int count = stmt.executeUpdate();
                result = count == 1;
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

        return result;
    }

    public String getList() throws NamingException, SQLException, ParseException {
        Connection conn = ConnectionPool.get();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String result;
        try {
            String sql = "SELECT * FROM feed ORDER BY no DESC";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            String str = "[";
            for (int cnt = 0; rs.next();) {
                if (cnt++ > 0) str += ", ";
                String tmp = rs.getString("jsonstr");
                JSONParser parser = new JSONParser();
                JSONObject feedobj = (JSONObject) parser.parse(tmp);
                String uid = feedobj.get("id").toString();
                String userstr = new UserDAO().get(uid);
                JSONObject userobj = (JSONObject) parser.parse(userstr);
                userobj.remove("password");
                userobj.remove("ts");
                feedobj.put("user", userobj);
                str += feedobj.toJSONString();
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
    public String getGroup(String frids, String maxNo) throws NamingException, SQLException, ParseException {
        Connection conn = ConnectionPool.get();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT jsonstr FROM feed WHERE id IN (" + frids + ")";
            if (maxNo != null) {
                sql = sql + " AND no < " + maxNo;
            }

            sql = sql + " ORDER BY no DESC LIMIT 3";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            String str = "[";

            for (int cnt = 0; rs.next();) {
                if (cnt++ > 0) str += ", ";
                String tmp = rs.getString("jsonstr");
                JSONParser parser = new JSONParser();
                JSONObject feedobj = (JSONObject) parser.parse(tmp);
                String uid = feedobj.get("id").toString();
                String userstr = new UserDAO().get(uid);
                JSONObject userobj = (JSONObject) parser.parse(userstr);
                userobj.remove("password");
                userobj.remove("ts");
                feedobj.put("user", userobj);
                str += feedobj.toJSONString();
            }

            return str + "]";
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
    }
}

