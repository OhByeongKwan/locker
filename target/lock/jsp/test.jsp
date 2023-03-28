<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.json.simple.*" %>
<%@ page import="org.json.simple.parser.*" %>
<%@ page import="java.io.*" %>
<%@ page import="core.*" %>
<%@ page import="util.*" %>
<%
//	System.out.println("123");
	request.setCharacterEncoding("UTF-8");
	UserDAO dao = new UserDAO();
	String mid = request.getParameter("mid");
//	System.out.println("mid = " + mid);

//	String a =  dao.test();
//	out.print(dao.test());

		out.print(dao.loginTest(mid));

%>