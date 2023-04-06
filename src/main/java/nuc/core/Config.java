package nuc.core;

public class Config {
	public static String getImageDir(String root, String homedir) {
		return root + "/users/" + homedir + "/images/";
	}
}
