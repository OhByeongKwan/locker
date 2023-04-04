package nuc.core;

import nuc.util.ConnectionPool;
import nuc.util.SqlUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.naming.NamingException;
import java.sql.*;

public class UniDAO {

    public String addUni(String uniName, String email) throws NamingException, SQLException, ParseException {
        Connection conn = null;
        PreparedStatement stmt = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = ConnectionPool.get();
            st = conn.createStatement();

            JSONParser parser = new JSONParser();

            String sql = "select status from uni where uniName = " + uniName;
            System.out.println(sql);
            rs = st.executeQuery(sql);

            if (rs.next()) {
                if(rs.getString("status").equals("1")){
                    return "RE";
                }else{
                    return "EX";
                }
            }
            else {
                sql = "INSERT INTO uni(uniName,status,jsonstr) VALUES("+uniName+",1,"+email+")";
                stmt = conn.prepareStatement(sql);
//                stmt.setString(1, uniName);
//                stmt.setString(2, "1");
//                stmt.setString(3, email);

                System.out.println(sql);
                int count = stmt.executeUpdate();
                return (count == 1) ? "OK" : "ER";
            }

        } finally {
            if (rs!= null) rs.close();
            if (st!= null) st.close();
            if (conn!= null) conn.close();
        }
    }

    public String adminAddUni(String id) throws NamingException, SQLException, ParseException {
        Connection conn = null;
        PreparedStatement stmt = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = ConnectionPool.get();
            st = conn.createStatement();


            String sql = "update uni set status = 2 where id = " + id;
            System.out.println(sql);

            stmt = conn.prepareStatement(sql);
            int count = stmt.executeUpdate();
            if(count != 1) return "ER";
            else{
                sql = "update uni set jsonstr = \"\" where id = " + id;
                stmt = conn.prepareStatement(sql);
                count = stmt.executeUpdate();
                return (count == 1) ? "OK" : "ER";
            }

        } finally {
            if (rs!= null) rs.close();
            if (st!= null) st.close();
            if (conn!= null) conn.close();
        }
    }


    public int addDepartment(String uniName,String email, String department) throws NamingException, SQLException, ParseException {
        Connection conn = null;
        PreparedStatement stmt = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = ConnectionPool.get();
            st = conn.createStatement();
            JSONParser parser = new JSONParser();

            System.out.println("1");
            String sql = "select id from uni where uniName = " + uniName;
            System.out.println(sql);
            rs = st.executeQuery(sql);

            // not exist uni
            if (!rs.next()) return 1;

            // exist uni
            else {
                Integer uid = rs.getInt("id");
                System.out.println("uid = " + uid);

                // is exist department ?
                sql = "select jsonstr from uni where id = "+ uid;
                System.out.println("is exist? " + sql);
                rs = st.executeQuery(sql);

                // request department
                if (rs.next()) {

                    String departments = rs.getString("jsonstr");

                    String[] departmentList = departments.split(",");

                    for (int i = 0; i < departmentList.length; i++) {
                        if (department.equals("'" + departmentList[i] + "'")) {
                            return 2; //exist department
                        }
                    }
                }

                //not exist depart
                // is request department ?
                sql = "select * from addDepartment where uid = "+ uid + " and department = " + department;
                rs = st.executeQuery(sql);
                // request department
                if (rs.next()){
                    return 3;
                }else{
                    //not exist and not request department
                    //insert data
                    sql = "INSERT INTO addDepartment(uid,department,email) VALUES("+uid+","+ department+ "," + email+")";
                    System.out.println(sql);
                    stmt = conn.prepareStatement(sql);

                    int count = stmt.executeUpdate();
                    return (count == 1) ? 4 : 5;
                    // 5: complete requesting department
                    // 6: fail requesting department
                }

            }

        } finally {
            if (rs!= null) rs.close();
            if (st!= null) st.close();
            if (conn!= null) conn.close();
        }
    }

    public String adminAddDep(String id,String newDep) throws NamingException, SQLException, ParseException {
        Connection conn = null;
        PreparedStatement stmt = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = ConnectionPool.get();
            st = conn.createStatement();

            //check uid
            String sql = "SELECT uid FROM addDepartment WHERE id = " + id;
            System.out.println(sql);
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            rs.next();
            String uid = rs.getString("uid");

            //check is exist another department
            sql = "select jsonstr from uni where id = " + uid;
            System.out.println(sql);
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            rs.next();
            String department = rs.getString("jsonstr");
            if(department.length()<2){
                //dep is null
                sql = "update uni set jsonstr = '" + newDep + "' where id = " + uid;
                System.out.println(sql);
                stmt = conn.prepareStatement(sql);
                int count = stmt.executeUpdate();
                if(count == 0) return "ER";
            }else{
                //dep is not null
                sql = "update uni set jsonstr = '" + department + "," + newDep + "' where id = " + uid;
                System.out.println(sql);
                stmt = conn.prepareStatement(sql);
                int count = stmt.executeUpdate();
                if(count == 0) return "ER";
            }

            sql = "delete from addDepartment where id = " + id;
            stmt = conn.prepareStatement(sql);
            int count = stmt.executeUpdate();
            return (count == 1) ? "OK" : "ER";

        } finally {
            if (rs!= null) rs.close();
            if (st!= null) st.close();
            if (conn!= null) conn.close();
        }
    }


    public String getUniList() throws NamingException, SQLException, ParseException {
        Connection conn = null;
        PreparedStatement stmt = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = ConnectionPool.get();
            st = conn.createStatement();

            JSONParser parser = new JSONParser();

            String sql = "select uniName from uni";
            System.out.println(SqlUtil.queryList(sql).toString());
            //SqlUtil.queryList(sql).toString();
            return SqlUtil.queryList(sql).toString();

        } finally {
            if (rs!= null) rs.close();
            if (st!= null) st.close();
            if (conn!= null) conn.close();
        }
    }

    public String getDepList(String uniName) throws NamingException, SQLException, ParseException {
        Connection conn = null;
        PreparedStatement stmt = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = ConnectionPool.get();
            st = conn.createStatement();

            JSONParser parser = new JSONParser();

            String sql = "select jsonstr from uni where uniName = '" + uniName +"'";
            System.out.println(SqlUtil.queryList(sql));
            //SqlUtil.queryList(sql).toString();
            return SqlUtil.queryList(sql).toString();

        } finally {
            if (rs!= null) rs.close();
            if (st!= null) st.close();
            if (conn!= null) conn.close();
        }
    }

    public String adminGetDepList() throws NamingException, SQLException, ParseException {
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt1 = null;
        Statement st = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        try {
            conn = ConnectionPool.get();
            st = conn.createStatement();


            String sql = "select * from addDepartment";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();


            JSONArray arr = new JSONArray();

            int cnt = 0;
            while(rs.next()) {

                JSONObject data = new JSONObject();

                data.put("id", rs.getString("id"));
                data.put("department", rs.getString("department"));

                String sql1 = "SELECT uniName FROM uni WHERE id = '" + rs.getString("uid") + "'";
                stmt1 = conn.prepareStatement(sql1);
                rs1 = stmt1.executeQuery();
                rs1.next();

                data.put("uniName", rs1.getString("uniName"));

                arr.add(data);

                cnt++;
            }

            JSONObject dep = new JSONObject();
            dep.put("dep", arr);
            //파싱할 데이터 저장
            String json;
            json = dep.toJSONString();
            return json;

        } finally {
            if (rs!= null) rs.close();
            if (st!= null) st.close();
            if (conn!= null) conn.close();
        }
    }


    public String adminGetUniList() throws NamingException, SQLException, ParseException {
        Connection conn = null;
        PreparedStatement stmt = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = ConnectionPool.get();
            st = conn.createStatement();

            String sql = "select * from uni where status = '1'";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            JSONArray arr = new JSONArray();

            int cnt = 0;
            while(rs.next()) {

                JSONObject data = new JSONObject();
                data.put("id", rs.getString("id"));
                data.put("uniName", rs.getString("uniName"));
                arr.add(data);

                cnt++;
            }

            JSONObject univ = new JSONObject();
            univ.put("univ", arr);
            //파싱할 데이터 저장
            String json;
            json = univ.toJSONString();
            return json;

        } finally {
            if (rs!= null) rs.close();
            if (st!= null) st.close();
            if (conn!= null) conn.close();
        }
    }


}
