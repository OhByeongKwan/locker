<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*" %>
<%@ page import="org.json.simple.*" %>
<%@ page import="nuc.core.*" %>
<% 
	request.setCharacterEncoding("UTF-8");

	String mid = request.getParameter("id");
	String pass = request.getParameter("pw");
	String type = request.getParameter("type");
	String uniName = request.getParameter("uniName");
	String depName = request.getParameter("department");
	String name = request.getParameter("name");
	String phoneNum = request.getParameter("phoneNum");
	String studentId = request.getParameter("studentId");
	String grade = request.getParameter("grade");
	String gender = request.getParameter("gender");
	String addr = request.getParameter("addr");

	
	UserDAO dao = new UserDAO();

	if (dao.get(mid) != null) {
		out.println("EX");
		return;
	}
	
	JSONObject jsonobj = dao.insert(mid, pass,type, uniName, depName,name phoneNum,studentId,grade,gender,addr, Config.getImageDir(application.getRealPath(File.separator), mid));
	if (jsonobj == null) {
		out.println("ER");
		return;
	}

	SessionManager.put(session, "usrobj", jsonobj);
//	out.println(jsonobj.toJSONString());
	out.println("OK");
%>