<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.json.simple.*" %>
<%@ page import="org.json.simple.parser.*" %>
<%@ page import="java.io.*" %>
<%@ page import="nuc.core.*" %>
<%@ page import="nuc.util.*" %>
<%
	//사물함 신청 취소 - userLocekrCancel.html ,, 관리자도 취소할 수 있게 관리자 관리 페이지에도 해당 로직 생성
	//사물함 신청 폼 취소는 ManagerLockerCancel.jsp로 만들 예정

	request.setCharacterEncoding("UTF-8");
	LockerDAO dao = new LockerDAO();

	String mid = request.getParameter("mid");
	String depCode = request.getParameter("depCode");


	out.print(dao.userLockerCancel(mid,depCode));
%>