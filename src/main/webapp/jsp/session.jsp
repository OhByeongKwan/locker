<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.json.simple.*" %>
<%
	JSONObject sesobj = (JSONObject) session.getAttribute("sesobj"); 
	out.println((sesobj == null) ? "NA" : sesobj.toString());
%>