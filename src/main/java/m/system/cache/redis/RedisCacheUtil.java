package m.system.cache.redis;

import m.system.exception.MException;
import m.system.util.ObjectUtil;
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
		try {
			System.out.println("get "+key+":"+ObjectUtil.toString(ro.getObj()));
		} catch (MException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ro.getObj();
	}
	/**
	 * 设置缓存
	 * @param key
	 * @param obj
	 */
	public static void set(String key,Object obj) {
		Jedis m=get();
		if(null==m) return;
		try {
			System.out.println("set "+key+":"+ObjectUtil.toString(obj));
		} catch (MException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		m.set(key.getBytes(),new RedisObject(obj).serialize());
	}
	/**
	 * 删除缓存
	 * @param key
	 */
	public static void del(String key) {
		Jedis m=get();
		if(null==m) return;
		System.out.println("del "+key);
		m.del(key.getBytes());
	}
}
