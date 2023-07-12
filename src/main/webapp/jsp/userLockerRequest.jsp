<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.json.simple.*" %>
<%@ page import="org.json.simple.parser.*" %>
<%@ page import="java.io.*" %>
<%@ page import="nuc.core.*" %>
<%@ page import="nuc.util.*" %>
<%
	//유저가 사물함 신청 - userLocekrRequest.html

	request.setCharacterEncoding("UTF-8");
	LockerDAO dao = new LockerDAO();

	String jsonstr = request.getParameter("jsonstr");

	//선착순의 경우 typeAUserLockerRequest로 지정
	out.print(dao.typeAUserLockerRequest(jsonstr));
%>