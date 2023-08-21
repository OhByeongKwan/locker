<%@ page contentType="text/html" pageEncoding="utf-8" %>
<%@ page import="nuc.core.*" %>
<%@ page import="nuc.util.Mail" %>
<%
    String mid = request.getParameter("mid");
    String pwcode = request.getParameter("pwcode");
    String type = request.getParameter("type");


    (new Mail()).send(mid, pwcode);
//    (new UserDAO()).pwcode(mid, pwcode);

%>
