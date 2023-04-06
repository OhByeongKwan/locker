<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*" %>
<%@ page import="org.json.simple.*" %>
<%@ page import="nuc.core.*" %>
<% 
	request.setCharacterEncoding("UTF-8");
	
	String mid = request.getParameter("id");
	String pass = request.getParameter("pass");
	String uniName = request.getParameter("uniName");
	String depName = request.getParameter("department");
	
	UserDAO dao = new UserDAO();

	if (dao.get(mid) != null) {
		out.println("EX");
		return;
	}
	
	JSONObject jsonobj = dao.insert(mid, pass, Config.getImageDir(application.getRealPath(File.separator), mid), uniName, depName);
	if (jsonobj == null) {
		out.println("ER");
		return;
	}

	SessionManager.put(session, "usrobj", jsonobj);
	out.println(jsonobj.toJSONString());
%>