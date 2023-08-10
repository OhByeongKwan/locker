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
import java.util.Collections;
import java.util.List;
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
            sql += "password VARCHAR(32),";
            sql += "ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP";
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
        Connection conn = null;
        try {
            String sql = "select jsonstr from lockerForm where depCode = '" + depCode +"'";
            return SqlUtil.query(sql);

        } finally {
            if (conn!= null) conn.close();
        }
    }

    public String getFormStatus(String depCode) throws NamingException, SQLException, ParseException {
        Connection conn = null;
        try {
            String sql = "select status from lockerForm where depCode = '" + depCode +"'";
            return SqlUtil.query(sql);

        } finally {
            if (conn!= null) conn.close();
        }
    }


    public String getLockerShare(String depCode, String num) throws NamingException, SQLException, ParseException {


            String sql = "select jsonstr from user inner join lock"+depCode+" on user.mid = lock"+depCode+".mid where num ="+num;
            System.out.println(sql);
            return SqlUtil.queryList(sql);
    }

    public String changeLockerPassword(String depCode, String num, String password) throws NamingException, SQLException, ParseException {
        String sql = "update lock"+depCode+" set password ="+password+" where num = " + num;
        System.out.println(sql);
        return SqlUtil.queryList(sql);
    }


    public String getUserListLockRequest(String depCode) throws NamingException, SQLException, ParseException {

        Connection conn = ConnectionPool.get();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql1 = "select *,JSON_EXTRACT(jsonstr,'$.name') as name from user inner join lock"+depCode+" on user.mid = lock"+depCode+".mid";
            System.out.println(sql1);
            stmt = conn.prepareStatement(sql1);
            rs = stmt.executeQuery();

            String str = "[";
            int cnt = 0;
            while(rs.next()) {
                if (cnt++ > 0) str += ",";

                str += "{\"mid\":\"";
                str += rs.getString("mid");
                str += "\",";
                str += "\"name\":";
                str += rs.getString("name");
                str += ",";
                str += "\"num\":\"";
                str += rs.getString("num");
                str += "\",";
                str += "\"numCode\":\"";
                str += rs.getString("numCode");
                str += "\"}";
            }
            str += "]";
            if(str.length()<5){
                return "NO";
            }
            return str;

//            String sql1 = "select jsonstr from user inner join lock"+depCode+" on user.mid = lock"+depCode+".mid";
//            String str1 =  SqlUtil.queryList(sql1).toString();
//            String sql2 = "select num from user inner join lock"+depCode+" on user.mid = lock"+depCode+".mid";
//            String str2 =  SqlUtil.queryList(sql2).toString();
//            String sql3 = "select numCode from user inner join lock"+depCode+" on user.mid = lock"+depCode+".mid";
//            String str3 =  SqlUtil.queryList(sql3).toString();
//
//            List<String> joined = new ArrayList<>();
//            joined.addAll(Collections.singleton(str1));
//            joined.addAll(Collections.singleton(str2));
//            joined.addAll(Collections.singleton(str3));
//
//            return joined.toString();

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
            String jsonstr = "";
            if (rs.next()) {
                JSONObject jo = new JSONObject();
                jo.put("numCode", rs.getString("numCode"));
                jo.put("num", rs.getString("num"));
                jo.put("status", rs.getString("status"));
                jo.put("password", rs.getString("password"));
                jo.put("ts", rs.getString("ts"));

                System.out.println(jo.toString());
                jsonstr = jo.toJSONString();
            }
            if(jsonstr.length()<5){
                return "NO";
            }else
                return jsonstr;
        } finally {
            if (rs!= null) rs.close();
            if (st!= null) st.close();
            if (conn!= null) conn.close();
        }
    }
    public String UserLockerRequest(String jsonstr, String type) throws NamingException, SQLException {
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
            System.out.println("test!!!!!!!->"+type);

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

            //count user
            sql = "Select COUNT(*) from lock"+depCode;
            int thisCount = SqlUtil.queryInt(sql);

            if(type.equals("A")){
                if(thisCount>=totalCount){
                    return "FULL";
                }
            }else if(type.equals("B")){{
                if(thisCount>=totalCount){
                    sql = "INSERT INTO lock"+depCode+"(numCode, num, mid, password) VALUES('" + 'A' +
                            "', '" + '0' +
                            "', '" + mid +
                            "', '" + pass +
                            "')";
                    System.out.println(sql);
                    SqlUtil.update(sql);
                    return "OK";
                }
            }}

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
            String sql = "update lockerForm set status = 'A' where depCode = '" + depCode + "'";
            SqlUtil.update(sql);
            sql =  "update lock"+depCode+" set status = 'A' where status = 'N'";
            SqlUtil.update(sql);
            return "OK";
        } finally {
            if (conn!= null) conn.close();
        }
    }

    public String typeBlockFinish(String depCode) throws NamingException, SQLException, ParseException {
        Connection conn = null;
        PreparedStatement stmt = null;
        Statement st = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        //선착순 배정의 경우 단순 status만 옮기면 끝이난다.
        try {
            conn = ConnectionPool.get();

            String sql = "select count(*) from lock"+depCode + " where numCode ='A' and num = 0 and status = 'N'";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            rs.next();
            int cnt = rs.getInt("count(*)");
            System.out.println(sql + ", cnt = " + cnt);
            if(cnt == 0){
                return typeAlockFinish(depCode);
            }else{
                //1 최대 카운트 수 구하기.
                //2 신청 카운트 수 구하기
                //3 랜덤 값 추출 후 탈락 시킬 유저 구하기
                //4 탈락시킬 유저가 A , 0인경우 그냥 삭제
                //5 탈락시킬 유저가 N인경우 A 0인 유저와 스위치 후 삭제.
                sql = "select JSON_EXTRACT(jsonstr,'$.lockerSumNum') * JSON_EXTRACT(jsonstr,'$.oneLockerMaxNum') as cnt from lockerForm where depCode = "+depCode;
                stmt = conn.prepareStatement(sql);
                rs = stmt.executeQuery();
                rs.next();
                int maxCnt = rs.getInt("cnt");

                int ranCnt = cnt-maxCnt;
                while(ranCnt>0){
                    sql = "SELECT * FROM lock" + depCode + " ORDER BY RAND() limit 1";
                    System.out.println(sql);
                    stmt = conn.prepareStatement(sql);
                    rs = stmt.executeQuery();
                    rs.next();
                    int num = ((rs.getInt("num")));
                    String mid =rs.getString("mid");
                    System.out.println(num + ",,," + mid);
                    if(num == 0){
                        sql = "delete from lock" + depCode + " where mid = '"+ mid+"'";
                        System.out.println(sql);
                        SqlUtil.update(sql);
                    }else{
                        sql = "SELECT * FROM lock" + depCode + " where num = 0 ORDER BY RAND() limit 1";
                        stmt = conn.prepareStatement(sql);
                        rs2 = stmt.executeQuery();
                        rs2.next();
                        String mid2 = rs2.getString("mid");
                        String ts2 = rs2.getString("ts");
                        sql = "delete from lock" + depCode + " where mid = '"+ mid2+"'";
                        SqlUtil.update(sql);
//                        sql = "update lock"+depCode+" set mid = '"+mid2+"' where mid = '" + mid + "'";
//                        SqlUtil.update(sql);
                        sql = "update lock"+depCode+" set mid = '"+mid2+"' and ts ="+ts2+" where mid = '" + mid + "'";
                        SqlUtil.update(sql);
                    }
                    ranCnt--;
                }

            }

            sql =  "update lock"+depCode+" set status = 'A' where status = 'N'";
            SqlUtil.update(sql);
            sql = "update lockerForm set status = 'A' where depCode = '" + depCode + "'";
            SqlUtil.update(sql);
            return "OK";
        } finally {
            if (conn!= null) conn.close();
        }
    }


}
