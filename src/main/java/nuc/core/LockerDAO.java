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
import java.util.ArrayList;
import java.util.Vector;

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
            sql += "password VARCHAR(32)";
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
        Connection conn = null;
        try {
            conn = ConnectionPool.get();
            String sql = "select jsonstr from lockerForm where depCode = '" + depCode +"'";
            return SqlUtil.query(sql);

        } finally {
            if (conn!= null) conn.close();
        }
    }
    public String getLockerUser(String depCode, String mid) throws NamingException, SQLException, ParseException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Statement st = null;
        try {
            conn = ConnectionPool.get();
            st = conn.createStatement();

            JSONParser parser = new JSONParser();

            String sql = "Select * from lock"+depCode+" where status != 'C' and mid = '"+mid+"'";
            System.out.println(sql);

            rs = st.executeQuery(sql);
            if (rs.next()) {
                JSONObject jo = new JSONObject();
                jo.put("numCode", rs.getString("numCode"));
                jo.put("num", rs.getString("num"));
                jo.put("status", rs.getString("status"));
                jo.put("password", rs.getString("password"));

                System.out.println(jo.toString());
                String jsonstr = jo.toJSONString();
                return jsonstr;

            }else{
                return "NO";
            }
        } finally {
            if (rs!= null) rs.close();
            if (st!= null) st.close();
            if (conn!= null) conn.close();
        }
    }
    public String typeAUserLockerRequest(String jsonstr) throws NamingException, SQLException {
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
            System.out.println(jsonObj);

            String depCode = (String) jsonObj.get("depCode");
            String mid = (String) jsonObj.get("mid");
            String pass = (String) jsonObj.get("pass");
            int lockerSumNum  = Integer.parseInt((String) jsonObj.get("SumNum"));
            int oneLockerMaxNum = Integer.parseInt((String) jsonObj.get("oneLockerMaxNum"));
            int totalCount = lockerSumNum*oneLockerMaxNum;

            //우선 취소한 것이 있는지 체크. -> 취소된 것이 있다면 mid값만 바꾸어 설정.
            String sql = "Select mid from lock"+depCode+" where status = 'C'";
            String changeMid = SqlUtil.query(sql);
            if(changeMid != null) {
                System.out.println(changeMid);
                sql = "update lock"+depCode+" set mid = '"+mid+"', status ='N' where mid = '" + changeMid + "'";
                SqlUtil.update(sql);
                return "OK";
            }


            Object sobj = jsonObj.get("startEndObj");
            ArrayList al1 = new ArrayList();
            al1 = (ArrayList) sobj;
            int arrayCnt = ((ArrayList) sobj).size();
            System.out.println(arrayCnt + " = arracyCnt");

            Vector<Integer> startEndV = new Vector<Integer>();

            for(int i=0; i<arrayCnt; i++){
                int temp =  Integer.valueOf((String) ((JSONObject)al1.get(i)).get("startNum"));
                startEndV.add(temp);
                temp =  Integer.valueOf((String) ((JSONObject)al1.get(i)).get("endNum"));
                startEndV.add(temp);
            }

            for(int i=0; i<startEndV.size(); i++){
                System.out.println("i  =" + i + "data = " + startEndV.get(i));
            }

            sql = "Select COUNT(*) from lock"+depCode;
            int thisCount = SqlUtil.queryInt(sql);

            char numCode = 'A';
            int num = thisCount;
            if(num == 0){
                num = 1;
            }else{
                System.out.println("thisC="+thisCount);
                if(thisCount >= lockerSumNum){

                        int plusCode = thisCount/lockerSumNum;
                        numCode = (char) (numCode + plusCode);
                        num = num-lockerSumNum*plusCode;

                }

                int i=-2;
                while(num >=0){
                    i+=2;
                    num -= (startEndV.get(i+1)-startEndV.get(i)+1);
                }
                System.out.println("i="+ i+", num = "+num);
                num = startEndV.get(i+1)+num+1;
                System.out.println("i="+ i+", num = "+num);
            }

            sql = "INSERT INTO lock"+depCode+"(numCode, num, mid, password) VALUES('" + numCode +
                    "', '" + num +
                    "', '" + mid +
                    "', '" + pass +
                    "')";
            System.out.println(sql);
            SqlUtil.update(sql);

            return "OK";
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs!= null) rs.close();
            if (st!= null) st.close();
            if (conn!= null) conn.close();
        }

    }

    public String userLockerCancel(String mid, String depCode) throws NamingException, SQLException {
        String sql = "update lock"+depCode+" set status = 'C' where mid = '" + mid + "'";
        SqlUtil.update(sql);
        return "OK";

    }

    public String typeAlockFinish(String depCode) throws NamingException, SQLException, ParseException {
        Connection conn = null;
        //선착순 배정의 경우 단순 status만 옮기면 끝이난다.
        try {
            conn = ConnectionPool.get();
            String sql = "update lockerForm set status = 'A' where depCode = '" + depCode +"'";
            SqlUtil.query(sql);
            sql =  "update lock"+depCode+" set status = 'A' where status = 'N'";
            return SqlUtil.query(sql);
        } finally {
            if (conn!= null) conn.close();
        }
    }


}
