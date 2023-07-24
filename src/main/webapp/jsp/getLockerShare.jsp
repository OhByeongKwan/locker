<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.json.simple.*" %>
<%@ page import="org.json.simple.parser.*" %>
<%@ page import="java.io.*" %>
<%@ page import="nuc.core.*" %>
<%@ page import="nuc.util.*" %>
<%

	//사물함 신청 폼 확인
	//depCode = 학과코드
	//Num = lock# 에 본인이 신청한 num값.
	request.setCharacterEncoding("UTF-8");
	LockerDAO dao = new LockerDAO();

	String depCode = request.getParameter("depCode");
	String num = request.getParameter("num");
	out.print(dao.getLockerShare(depCode, num));
%>