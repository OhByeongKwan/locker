<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.json.simple.*" %>
<%@ page import="org.json.simple.parser.*" %>
<%@ page import="java.io.*" %>
<%@ page import="core.*" %>
<%@ page import="util.*" %>
<% 
	request.setCharacterEncoding("UTF-8");
	UserDAO dao = new UserDAO();
	JSONObject usrobj = null;
	String mid = null, code = null, pass = null;

//	// invoked from login.html
	if(request.getParameter("platform") == null) {
		mid = request.getParameter("mid");
		pass = request.getParameter("pass");

		int a = dao.loginTest(mid, pass);
		out.print("NA");
		out.print("'"+a+"'");}

//		String res = dao.login(mid, pass);
//		if (res.equals("NA") || res.equals("PS")) {
//			out.print(res);
//			return;
//		}
//
//		usrobj = (JSONObject)(new JSONParser()).parse(res);
//		code = "OK";
//
//	} else if(request.getParameter("platform").equals("kakao")) {
//		String result = (new ExternApi().getKakaoUserInfo(request.getParameter("acctoken"), request.getParameter("reftoken")));
//		String urltoken = request.getParameter("urltoken");
//		JSONObject jsonobj = (JSONObject) (new JSONParser().parse(result));
//		if (jsonobj.get("id") != null) {
//			code = "OK";
//			mid = "kakao" + (jsonobj.get("id"));
//			pass = mid;
//
//			// insert the new user if not stored in our database
//			String _usrobj = dao.get(mid);
//			if (_usrobj == null) {
//				usrobj = dao.insert(mid, pass, Config.getImageDir(application.getRealPath(File.separator), mid));
//				if (usrobj == null) {
//					out.print("ER");
//					return;
//				}
//
//				usrobj = (new ExternApi()).updateUserInformationByKakao(jsonobj);
//
//				if(urltoken != null) {
//					//(new ParkDAO()).updateToken(mid, urltoken); // 링크타고 들어오는 추천이벤트 할때 넣을것
//				}
//			} else {
//				usrobj = (JSONObject)(new JSONParser()).parse(_usrobj);
//			}
//		} else {
//			code = "NA";
//		}
//	}
//
//	if(code.startsWith("OK")) {
//		SessionManager.put(session, "usrobj", usrobj);
//		if (usrobj.get("admin") != null && usrobj.get("admin").toString().equals("T")) {
//			code = "AD";
//		}
//	}
//	out.print(code);

%>