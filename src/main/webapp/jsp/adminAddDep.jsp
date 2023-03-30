<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.json.simple.*" %>
<%@ page import="org.json.simple.parser.*" %>
<%@ page import="java.io.*" %>
<%@ page import="nuc.core.*" %>
<%@ page import="nuc.util.*" %>
<%
	//signupSchool.html - 회원가입 시 본인 학교 추가
	//학교가 있을 시 code EX , 학교가 없을 시 OK, 그 외 ER
	request.setCharacterEncoding("UTF-8");
	UniDAO dao = new UniDAO();
	String id = request.getParameter("id");

	out.print(dao.adminAddUni(id));
%>