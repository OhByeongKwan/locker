<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.json.simple.*" %>
<%@ page import="org.json.simple.parser.*" %>
<%@ page import="java.io.*" %>
<%@ page import="nuc.core.*" %>
<%@ page import="nuc.util.*" %>
<%
	request.setCharacterEncoding("UTF-8");
	UserDAO dao = new UserDAO();

	String type = request.getParameter("type");

	out.print(dao.getUserList(type));
%>