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
	String phoneNum = request.getParameter("phoneNum");
	String studentId = request.getParameter("studentId");
	String grade = request.getParameter("grade");
	String gender = request.getParameter("gender");
	String addr = request.getParameter("addr");
//	String mid = "1111";
//	String pass = "1111";
//	String type = "1111";
//	String uniName = "삼육대학교";
//	String depName = "컴퓨터공학";
//	String phoneNum = "1111";
//	String studentId = "1111";
//	String grade = "1111";
//	String gender = "1111";
//	String addr = "1111";

	
	UserDAO dao = new UserDAO();

	if (dao.get(mid) != null) {
		out.println("EX");
		return;
	}
	
	JSONObject jsonobj = dao.insert(mid, pass,type, uniName, depName, phoneNum,studentId,grade,gender,addr, Config.getImageDir(application.getRealPath(File.separator), mid));
	if (jsonobj == null) {
		out.println("ER");
		return;
	}

	SessionManager.put(session, "usrobj", jsonobj);
//	out.println(jsonobj.toJSONString());
	out.println("OK");
%>