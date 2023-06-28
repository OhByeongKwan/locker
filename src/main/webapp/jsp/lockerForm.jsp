<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.json.simple.*" %>
<%@ page import="org.json.simple.parser.*" %>
<%@ page import="java.io.*" %>
<%@ page import="nuc.core.*" %>
<%@ page import="nuc.util.*" %>
<%
	//사물함 신청 폼 생성
	//jsonstr에는 기존 생성한 데이터에 + 학과코드 넣기(pagectx.usrobj 내부에 있음)
	//출력값 x
	request.setCharacterEncoding("UTF-8");
	LockerDAO dao = new LockerDAO();
	String jsonstr = request.getParameter("jsontr");

	out.print(dao.insert(jsonstr));

	//이후 데이터베이스 생성해야함 -> locekr
%>