package m.system.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import m.system.lang.IJsonOuter;
import m.system.util.ClassUtil;
import m.system.util.JSONMessage;

public class CacheMapList<T extends FlushCacheList> implements IJsonOuter {
	private static Map<Class<?>, CacheMapList<?>> cacheMap=new HashMap<Class<?>,CacheMapList<?>>();
	public static CacheMapList<?>[] getAll(){
		return cacheMap.values().toArray(new CacheMapList<?>[] {});
	}
	@SuppressWarnings("unchecked")
	protected static <T extends FlushCacheList> CacheMapList<T> instance(Class<T> clazz){
		CacheMapList<T> cache=(CacheMapList<T>) cacheMap.get(clazz);
		if(null==cache) {
			synchronized(CacheMapList.class) {
				cache=(CacheMapList<T>) cacheMap.get(clazz);
				if(null==cache) {
					cacheMap.put(clazz, new CacheMapList<T>(clazz));
					cache=(CacheMapList<T>) cacheMap.get(clazz);
				}
			}
		}
		return cache;
	}
	
	private Class<T> clazz;
	private Map<String,List<T>> data;
	private CacheMapList(Class<T> claz) {
		clazz=claz;
		data=new HashMap<String,List<T>>();
	}
	

	protected Class<T> getClazz() {
		return clazz;
	}
	protected void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}
	/**
	 * 获取缓存数据
	 * @param key
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	protected List<T> getList(String key) {
		List<T> t=this.data.get(key);
		if(null==t) {
			synchronized(CacheMap.class) {
				t=this.data.get(key);
				if(null==t) {
					try {
						t=(List<T>) ((T)ClassUtil.newInstance(clazz)).getCacheList(key);
						System.out.println(new StringBuffer("加载缓存成功:").append(clazz).append(", key=").append(key));
						this.data.put(key, t);
					} catch (Exception e) {
						System.out.println(new StringBuffer("加载缓存异常:").append(clazz).append(", key=").append(key).append(", error:").append(e.getMessage()));
						e.printStackTrace();
					}
				}
			}
		}
		return t;
	}
	/**
	 * 清除缓存
	 * @param key
	 */
	protected void clear(String key) {
		this.data.remove(key);
		System.out.println("清除缓存成功:"+key);
	}
	protected void clear() {
		this.data=new HashMap<String,List<T>>();
	}
	@Override
	public String toJSONString() {
		JSONMessage json=new JSONMessage();
		json.push("key", this.clazz);
		json.push("value", this.data);
		return json.toJSONString();
	}

}
