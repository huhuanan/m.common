package m.system.cache.redis;

import m.system.util.StringUtil;
import redis.clients.jedis.Jedis;

public class RedisCacheUtil {
	private static ThreadLocal<Jedis> jedis = new ThreadLocal<Jedis>();
	private RedisCacheUtil() {
	}
	private static Jedis get() {
		if(null==jedis.get()&&RedisConfig.isReady()) {
			Jedis j=new Jedis(RedisConfig.getIp(),RedisConfig.getPort());
			if(!StringUtil.isSpace(RedisConfig.getAuth())) j.auth(RedisConfig.getAuth());
			jedis.set(j);
		}
		Jedis m=jedis.get();
		if(null==m) return null;
		if(StringUtil.noSpace(m.ping()).equals("PONG")){
			return m;
		}else {
			return get();
		}
	}
	/**
	 * 获取缓存
	 * @param key
	 * @return
	 */
	public static Object get(String key) {
		Jedis m=get();
		if(null==m) return null;
		RedisObject ro=new RedisObject(m.get(key.getBytes()));
		return ro.getObj();
	}
	/**
	 * 设置缓存
	 * @param key
	 * @param obj
	 */
	public static boolean set(String key,Object obj,int second) {
		Jedis m=get();
		if(null==m) return false;
		return m.setex(key.getBytes(),second,new RedisObject(obj).serialize()).equals("OK");
	}
	/**
	 * 设置缓存时间
	 * @param key
	 * @param second
	 * @return
	 */
	public static boolean expire(String key,int second) {
		Jedis m=get();
		if(null==m) return false;
		return m.expire(key.getBytes(), second)==1l;
	}
	/**
	 * 删除缓存
	 * @param key
	 */
	public static boolean del(String key) {
		Jedis m=get();
		if(null==m) return false;
		return m.del(key.getBytes())==1l;
	}
}
