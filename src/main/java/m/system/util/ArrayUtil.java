package m.system.util;

import java.util.Arrays;
import java.util.List;

import m.system.exception.MException;

public class ArrayUtil {

	/**
	 * 数组连接成字符串
	 * @param arrays 要连接的字符串数组
	 * @param split 中间的分隔符
	 * @return
	 */
	public static String connection(Object[] arrays,String split){
		StringBuffer stringBuffer=new StringBuffer();
		for(int i=0;i<arrays.length;i++){
			if(i!=0) stringBuffer.append(split);
			stringBuffer.append(arrays[i]);
		}
		return stringBuffer.toString();
	}
	/**
	 * 数组中是否包含对象
	 * @param arrays
	 * @param obj
	 * @return
	 */
	public static <T> boolean isContain(T[] arrays,T obj){
		for(int i=0;i<arrays.length;i++){
			if(obj.equals(arrays[i])){
				return true;
			}
		}
		return false;
	}
	/**
	 * 数组合并
	 * @param <T>
	 * @param first
	 * @param second
	 * @return
	 */
	public static <T> T[] concat(T[] first, T[] second) {
		T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}  
	/**
	 * 转换对象为字符串的展现形式.
	 * @param list
	 * @return
	 */
	public static String toString(List<Object> list){
		return toString(list.toArray(new Object[]{}));
	}
	/**
	 * 转换对象为字符串的展现形式.
	 * @param objs
	 * @return
	 */
	public static String toString(Object[] objs){
		StringBuffer sb=new StringBuffer();
		for(Object value : objs){
			sb.append(",");
			try {
				sb.append(ObjectUtil.toString(value));
			} catch (MException e) {
				sb.append("null");
			}
		}
		return new StringBuffer("[").append(sb.length()>0?sb.substring(1):"").append("]").toString();
	}
	public static void main(String[] args) {
		Object[] a=new Object[]{1,"1",2,"3"};
		Object[] aa=concat(a, a);
//		a=ArrayUtil.removeObject(a, 2);
		System.out.println(ArrayUtil.connection(a, ","));
		System.out.println(ArrayUtil.connection(aa, ","));
	}
}
