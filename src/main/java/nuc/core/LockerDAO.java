package nuc.core;

import com.mysql.cj.xdevapi.JsonString;
import nuc.util.ConnectionPool;
import nuc.util.MD5;
import nuc.util.SqlUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.naming.NamingException;
import java.io.File;
import java.sql.*;

public class LockerDAO {

    public String insert(String jsonstr) throws NamingException, SQLException {
        System.out.println(jsonstr);
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Statement st = null;
        try {
            conn = ConnectionPool.get();
            st = conn.createStatement();

            JSONParser parser = new JSONParser();
            Object obj = parser.parse( jsonstr );
            JSONObject jsonObj = (JSONObject) obj;

            String depCode = (String) jsonObj.get("depCode");

//            id int NOT NULL AUTO_INCREMENT PRIMARY KEY ,
//            depCode int(128),
//                    jsonstr VARCHAR(1024),
//                    ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP

            String sql = "INSERT INTO lockerForm(depCode, jsonstr) VALUES('" + depCode +
                    "', '" + jsonstr +
                    "')";
            System.out.println(sql);
            SqlUtil.update(sql);


            //create table for this department
            // table name = lock<depCode>
            sql = "CREATE TABLE if not exists lock"+depCode+" (";
            sql += "numCode VARCHAR(4),";
            sql += "num VARCHAR(32),";
            sql += "status VARCHAR(128) DEFAULT 'N',";
            sql += "mid VARCHAR(128),";
            sql += "password VARCHAR(32),";
            sql += ")";
            System.out.println(sql);
            SqlUtil.update(sql);

            return jsonstr;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs!= null) rs.close();
            if (st!= null) st.close();
            if (conn!= null) conn.close();
        }

    }

    public String getForm(String depCode) throws NamingException, SQLException, ParseException {
        //에러 안나면 주석 제거
//        Connection conn = null;
        try {
//            conn = ConnectionPool.get();
            String sql = "select jsonstr from lockerForm where depCode = '" + depCode +"'";
            return SqlUtil.query(sql);

        } finally {
//            if (conn!= null) conn.close();
        }
    }


}
