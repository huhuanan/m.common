package m.system.cache.redis;

import m.system.util.StringUtil;

public class RedisConfig {
	private static String ip;
	private static Integer port;
	private static String auth;
	public static void initConfig(String ip,Integer port,String auth) {
		RedisConfig.ip=ip;
		RedisConfig.port=port;
		RedisConfig.auth=auth;
	}
	/**
	 * 是否配置完成
	 * @return
	 */
	public static boolean isReady() {
		return !StringUtil.isSpace(ip);
	}
	public static String getIp() {
		return ip;
	}
	public static Integer getPort() {
		return port;
	}
	public static String getAuth() {
		return auth;
	}

}
