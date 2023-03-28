package nuc.util;

import org.json.simple.JSONObject;

import javax.naming.NamingException;
import java.sql.*;

public class SqlUtil {
	
	public static void update(String sql) throws NamingException, SQLException {
		Connection conn = null;
		Statement st = null;
		try {
			conn = ConnectionPool.get();
			st = conn.createStatement();
			st.executeUpdate(sql);
			
		} finally {
			if (st!= null) st.close();
			if (conn!= null) conn.close();
		}
	}
	
	public static String query(String sql) throws NamingException, SQLException {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = ConnectionPool.get();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			
			if (!rs.next()) return null;
			return rs.getString(1);
			
		} catch(SQLException ex) {
			System.out.println("ERROR: " + sql);
			return null;
			
		} finally {
			if (rs!= null) rs.close();
			if (st!= null) st.close();
			if (conn!= null) conn.close();
		}
	}
	
	public static String queryList(String sql) throws NamingException, SQLException {
		return queryList(sql, false);
	}
	
	public static String queryList(String sql, boolean flag) throws NamingException, SQLException {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = ConnectionPool.get();
			st = conn.createStatement();
			rs = st.executeQuery(sql);

			String list = "";
			int cnt = 0;
			while (rs.next()) {
				if (cnt++ > 0) list += ",";
				list += rs.getString(1);
			}
			return flag ? list : "[" + list + "]";
			
		} finally {
			if (conn != null) conn.close();
			if (st != null) st.close();
			if (rs != null) rs.close();
		}
	}

	public static int queryInt(String sql) throws NamingException, SQLException {
		String res = query(sql);
		return (res != null) ? Integer.parseInt(res) : -1;
	}

	public static JSONObject queryStar(String sql) throws NamingException, SQLException {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;

		try {
			conn = ConnectionPool.get();
			st = conn.createStatement();
			rs = st.executeQuery(sql);

			if (!rs.next()) return null;
			ResultSetMetaData metadata = rs.getMetaData();
			int columns = metadata.getColumnCount();

			JSONObject jsonobj = new JSONObject();
			for(int i=1; i<= columns; i++) {
				String key = metadata.getColumnLabel(i);
				String val = rs.getString(i);
				jsonobj.put(key, val);
			}

			return jsonobj;
		} finally {
			if (conn != null) conn.close();
			if (st != null) st.close();
			if (rs != null) rs.close();
		}
	}
}
