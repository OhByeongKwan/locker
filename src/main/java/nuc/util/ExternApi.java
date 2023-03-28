package nuc.util;

import nuc.core.UserDAO;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.naming.NamingException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

public class ExternApi {
	public String getKakaoUserInfo(String acctoken, String reftoken) throws Exception {
		String url = "https://kapi.kakao.com/v2/user/me";
		url += "?secure_resource=true";
		
		HttpURLConnection con = (HttpURLConnection) (new URL(url)).openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Authorization", "Bearer " + acctoken);
		return Util.fixJSON(response("getKakaoUserInfo : ", con));
	}
	
	public String getKakaoUserInfoWithCode(String code) throws Exception {
		String redirectUrl = Util.URL + "/jsp/oauthKakao.jsp";
		String url = "https://kauth.kakao.com/oauth/token";
		url += "?grant_type=authorization_code";

		url += "&client_id="+Util.KAKAO_RESTAPI_KEY;
		url += "&redirect_uri=" + redirectUrl;
		url += "&code=" + code;

		//자동로그인 0506 ?
		//url += "&prompt=none";
		
		HttpURLConnection con = (HttpURLConnection) (new URL(url)).openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		con.setInstanceFollowRedirects(false);
		
		return response("getKakaoUserInfoWithCode : ", con);
	}
	
	public String response(String TAG, HttpURLConnection con) {
		return response(TAG, con, "UTF-8"); // default UTF-8
	}
	
	public String response(String TAG, HttpURLConnection con, String encoding) {
		try {
			int responseCode = con.getResponseCode();
			BufferedReader br;
			if (responseCode == 200) { 
				br = new BufferedReader(new InputStreamReader(con.getInputStream(), encoding));
			} 
			else {
				br = new BufferedReader(new InputStreamReader(con.getErrorStream(), encoding));
			}			
			
			String line;
			StringBuffer sbuf = new StringBuffer();
			while ((line = br.readLine()) != null) {
				sbuf.append(line);
			}
			br.close();		
			//System.out.println( TAG + sbuf.toString());
			return sbuf.toString();

		} catch (Exception ex) {
			ex.printStackTrace();;
			return ex.getLocalizedMessage();	
		}
	}
	
//	public JSONObject updateUserInformationByKakao(JSONObject kakaoobj) throws NamingException, SQLException, ParseException {
//		String id = kakaoobj.get("id").toString();
//		JSONObject propobj = (JSONObject)kakaoobj.get("properties");
//		JSONParser parser = new JSONParser();
//		String mid = "kakao" + id;
//		JSONObject usrobj = (JSONObject)parser.parse(new UserDAO().get(mid));
//
//		if (propobj.get("nickname") != null) {
//			String name = Util.fixJSON(propobj.get("nickname").toString());
//			usrobj.put("name", name);
//		}
//		if (propobj.get("profile_image") != null) {
//			usrobj.put("image", propobj.get("profile_image").toString());
//		}
//		(new UserDAO()).update(mid, usrobj);
//		return (JSONObject)parser.parse(new UserDAO().get(mid));
//	}
}
