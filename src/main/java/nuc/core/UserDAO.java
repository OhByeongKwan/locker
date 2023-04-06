package nuc.core;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Set;

import javax.naming.NamingException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import nuc.util.ConnectionPool;
import nuc.util.MD5;
import nuc.util.SqlUtil;
import nuc.util.Util;

public class UserDAO {

    //signup - check eamil
    public String get(String mid) throws NamingException, SQLException, ParseException {
        String sql = "SELECT jsonstr FROM user where mid = '" + mid + "'";
        return SqlUtil.query(sql);
    }

    public JSONObject insert(String mid, String pass, String imgdir, String uniName, String depName) throws NamingException, SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Statement st = null;
        try {
            conn = ConnectionPool.get();
            st = conn.createStatement();

            String sql = "SELECT id FROM department WHERE uniName = '" + uniName + "'";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            rs.next();
            String depCode = String.valueOf((rs.getInt("id")));

            JSONObject jsonobj = new JSONObject();
            jsonobj.put("mid", mid);
            jsonobj.put("uniName", uniName);
            jsonobj.put("depName", depName);
            jsonobj.put("depCode", depCode);

            pass = "'"+pass+"'";

            String sql2 = "INSERT INTO user(mid, password, depCode, jsonstr) VALUES('" + mid +
                    "', '" + MD5.get(pass) +
                     "', '" + depCode +
                    "', '" + jsonobj.toJSONString() +
                    "')";
            System.out.println(sql2);
            SqlUtil.update(sql2);

            // create a directory to store images
            (new File(imgdir)).mkdirs();
            return jsonobj;
            }
        finally {
        if (rs!= null) rs.close();
        if (st!= null) st.close();
        if (conn!= null) conn.close();
    }

    }



    public String loginTest(String mid) throws NamingException, SQLException, ParseException {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = ConnectionPool.get();
            st = conn.createStatement();

            JSONParser parser = new JSONParser();

            String sql = "select mid from test where mid = '" + mid + "'";
            System.out.println(sql);
            rs = st.executeQuery(sql);

            if (!rs.next()) return "aa";
            else return "bb";

        } finally {
            if (rs!= null) rs.close();
            if (st!= null) st.close();
            if (conn!= null) conn.close();
        }
    }


    // Invoked from login.jsp
    public String login(String mid, String pass) throws NamingException, SQLException, ParseException {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = ConnectionPool.get();
            st = conn.createStatement();

            JSONParser parser = new JSONParser();

            String sql = "SELECT password, jsonstr FROM user where mid = '" + mid + "'";
            rs = st.executeQuery(sql);

            if (!rs.next()) return "NA";

            String md5str = MD5.get(pass);
            if (!md5str.equals(rs.getString("password"))) return "PS";

            // check if authentication check is required
            JSONObject jsonobj = (JSONObject) parser.parse(rs.getString("jsonstr"));
            return jsonobj.toJSONString();

        } finally {
            if (rs!= null) rs.close();
            if (st!= null) st.close();
            if (conn!= null) conn.close();
        }
    }

    public void pwcode(String mid, String pwcode) throws NamingException, SQLException, ParseException {
        String sql = "delete from passcode where mid = '" + mid + "'";
        SqlUtil.update(sql);
        sql = "INSERT INTO passcode(mid, pwcode, wrong) VALUES('" + mid + "', '" + pwcode + "', 0 )";
        SqlUtil.update(sql);
    };

    public int pwcodeC(String mid, String pwcode) throws NamingException, SQLException, ParseException {
        Connection conn = ConnectionPool.get();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        try {
            String sql = "SELECT wrong FROM passcode WHERE mid = '" + mid + "' ";
            sql += " and pwcode = '" + pwcode + "'";

            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            System.out.println(rs);
            if (!rs.next()) {
                sql = "update passcode set wrong = wrong+1 WHERE mid = '" + mid + "' ";
                SqlUtil.update(sql);
                sql = "SELECT wrong FROM passcode WHERE mid = '" + mid + "'";
                stmt = conn.prepareStatement(sql);
                rs1 = stmt.executeQuery();
                if (rs1.next()) {
                    return rs1.getInt(1);
                }
            }
            return 200;

        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    };




}
