<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*" %>
<%@ page import="org.json.simple.*" %>
<%@ page import="nuc.core.*" %>
<% 
	request.setCharacterEncoding("UTF-8");

	String mid = request.getParameter("id");

	UserDAO dao = new UserDAO();

	if (dao.get(mid) != null) {
		out.println("EX");
		return;
	}

	out.println("OK");
	return;
%>