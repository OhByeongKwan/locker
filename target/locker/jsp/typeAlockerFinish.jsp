<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*" %>
<%@ page import="org.json.simple.*" %>
<%@ page import="nuc.core.*" %>
<% 
	request.setCharacterEncoding("UTF-8");

	String depCode = request.getParameter("depCode");

	LockerDAO dao = new LockerDAO();

	dao.typeAlockFinish(depCode);

	out.println("OK");
%>