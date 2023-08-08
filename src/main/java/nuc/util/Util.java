package nuc.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Util {
	public final static String URL = "http://localhost:8087"; // local
//	public final static String URL = "http://15.164.134.14:8080";
	public final static String KAKAO_RESTAPI_KEY = "";
	public static final ArrayList<String> ADMINS = new ArrayList<String>() {{
//		/*add("");
//		*/add("");
	}};
	
	
	// fix the malformed JSON strings fetched from external sources
		public static String fixJSON(String str) {
			if (str == null) return str;
				 
			str = str.trim().replace("{\"","@$lb"); // for {"
			str = str.replace("\"}","@$rb"); // for "}
			
			str = str.replace("\",\"","@$cm"); // for ","
			str = str.replace("\":\"","@$cl"); // for ":"
			
			str = str.replace(":\"", "@$rdqc"); // for :"
			str = str.replace("\":", "@$ldqc"); // for ":
			str = str.replace(",\"", "@$rdqo"); // for ,"
			str = str.replace("\",", "@$ldqo"); // for ",
				 
			str = str.replace("'", "").replace("\"", "").replace("\\","");
			
			str = str.replace("@$rdqo",",\"" ); // for ,"
			str = str.replace("@$ldqo", "\"," ); // for ",
			str = str.replace("@$rdqc", ":\""); // for :"
			str = str.replace("@$ldqc","\":"); // for ":
				 
			str = str.replace("@$lb", "{\"");
			str = str.replace("@$rb","\"}");
				 
			str = str.replace("@$cm","\",\"");
			str = str.replace("@$cl","\":\"");
				 
			return str;
		}
		
	public static String getFilename(String path) {
		return (new File(path)).getName();
	}
	
	public static String shuffle(String str) {
		List<Character> chars = new ArrayList<Character>();
		for(char c: str.toCharArray()) {
			chars.add(c);
		}
		StringBuilder output = new StringBuilder(str.length());
		while(chars.size() != 0) {
			int rand = (int)(Math.random()*chars.size());
			output.append(chars.remove(rand));
		}
		
		return output.toString();
	}
	
	public static String stripSpecialChar(String str) {
		String[] spChars = {"`", "-", "=", ";", "'", "~", "!", "@", "#", "[$]", "%", "\\^", "&", "[+]", "\\{", "\\}", "\\(", "\\)", ",", " "}; //except wild char for window-> \ / : * ? " < > |
		for(int i=0;i<spChars.length;i++){
			str = str.replaceAll(spChars[i], ""); 
		}
		return str;
	}

	public static String saveImage(String root, String homedir, String file, byte[] data) throws IOException {
		String path = root + "/users/" + homedir + "/images/" + file;
		Util.write(path, data);
		return path;
	}
	
	public static void write(String path, byte[] data) throws IOException {
		FileOutputStream out = new FileOutputStream(path);
		out.write(data);
		out.close();
	}
}
