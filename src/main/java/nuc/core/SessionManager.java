package nuc.core;

import org.json.simple.JSONObject;

import javax.servlet.http.HttpSession;

public class SessionManager {

	// Invoked from sessionSet.jsp
	public static void set(HttpSession session, JSONObject jsonobj) throws Exception {
		session.setAttribute("sesobj", jsonobj);
		session.setMaxInactiveInterval(30 * 24 * 60 * 60);
		//System.out.println("session.set: " + get(session).toJSONString());
	}
	
	// Invoked from session.jsp
	public static JSONObject get(HttpSession session) throws Exception {
		session.setMaxInactiveInterval(30 * 24 * 60 * 60);
		return (JSONObject) session.getAttribute("sesobj");
	}
	
	// Invoked from signup.jsp
	public static void put(HttpSession session, String name, JSONObject jsonobj) throws Exception {
		if (jsonobj == null) return;
		
		JSONObject sesobj = get(session);
		if (sesobj == null) {
			sesobj = new JSONObject();
		}
		sesobj.put(name, jsonobj);
		set(session, sesobj);
	}
	
	public static void remove(HttpSession session, String name) throws Exception {
		JSONObject sesobj = get(session);
		if (sesobj != null) {
			sesobj.remove(name);
			set(session, sesobj);
		}
	}
	
	public static void remove(HttpSession session) throws Exception {
		session.invalidate();
	}
}
