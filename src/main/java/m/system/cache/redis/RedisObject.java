package m.system.cache.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class RedisObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6379L;
	private Object obj;
	public RedisObject() {
		
	}
	public RedisObject(Object obj) {
		this.obj=obj;
	}
	public RedisObject(byte[] bytes) {
		this.obj=deserialize(bytes);
	}
	// 序列化
	public byte[] serialize() {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		byte[] bytes = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(this);
			bytes = baos.toByteArray();
		} catch (Exception e) {
			System.err.println("序列化失败" + e.getMessage());
		}
		return bytes;
	}
	 
	// 反序列化
	private static Object deserialize(byte[] bytes) {
		if(null==bytes) return null;
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		try {
			bais = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bais);
			Object n=ois.readObject();
			if(null==n) return null;
			return ((RedisObject) n).getObj();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("反序列化失败" + e.getMessage());
		}
		return null;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
	
	
}
