package m.common.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import m.common.model.HostInfo;
import m.system.RuntimeData;
import m.system.db.DBConnection;
import m.system.util.NumberUtil;
import m.system.util.StringUtil;

public class HostInfoService extends Service {
	private static int currentOid=0;
	private static HostInfo currentHost=new HostInfo();
	public static HostInfo getCurrentHost(){
		return currentHost;
	}
	public static void setCurrentHost(HostInfo host) {
		currentHost=host;
		RuntimeData.setHostInfo(currentHost);
		currentOid=Integer.parseInt(host.getOid());
	}
	
	private static Map<String,HostInfo> hostMap=new LinkedHashMap<String,HostInfo>();
//	public void clearList(){
//		hostMap=new LinkedHashMap<String,HostInfo>();
//	}
	public List<HostInfo> getList(){
		resetCurrentHostOtherInfo();
		List<HostInfo> list= new ArrayList<HostInfo>(hostMap.values());
		return list;
	}
	private static long lastLong=0l;
	private static String[] ips=new String[] {};
	private static Random random=new Random();
	/**
	 * 获取当前主机列表的随机ip
	 * @return
	 */
	public static String getRandomIP(String other) {
		if(System.currentTimeMillis()-lastLong>5000) {
			lastLong=System.currentTimeMillis();
			List<String> ls=new ArrayList<String>();
			for(HostInfo hi : hostMap.values()) {
				if(!hi.getIpport().equals(".")) ls.add("http://"+getIp(hi.getIpport())+"/");
			}
			if(!StringUtil.isSpace(other)) ls.add(other);
			ips=ls.toArray(new String[] {});
		}
		if(ips.length>0) return ips[random.nextInt(ips.length)];
		else return "";
	}
	/**
	 * 获取ip部分
	 * @param ipport
	 * @return
	 */
	private static String getIp(String ipport) {
		String ip=ipport;
		if(ip.indexOf("/")==0) ip=ip.substring(1);
		return ip.split(":")[0];
	}
	public static void resetCurrentHostOtherInfo(){
		if(null!=currentHost){
			double mb = 1024 * 1024 * 1.0;
			int totalMemory = NumberUtil.toInt(Runtime.getRuntime().totalMemory() / mb *100);
			int freeMemory = NumberUtil.toInt(Runtime.getRuntime().freeMemory() / mb *100);
			int maxMemory = NumberUtil.toInt(Runtime.getRuntime().maxMemory() / mb *100);
			int dbUseLinkNum = DBConnection.getUseLinkNum();
			int dbMaxLinkNum = DBConnection.getMaxLinkNum();
			HostInfo hi=hostMap.get(currentHost.getIpport());
			if(null!=hi){
				hi.setTotalMemory(totalMemory/100.0);
				hi.setFreeMemory(freeMemory/100.0);
				hi.setMaxMemory(maxMemory/100.0);
				hi.setDbUseLinkNum(dbUseLinkNum);
				hi.setDbMaxLinkNum(dbMaxLinkNum);
			}
		}
	}
	private static Map<String,Integer> hostOidMap=new HashMap<String, Integer>();
	private static synchronized int getHostOid(String ip) {
		if(null==hostOidMap.get(ip)) {
			int oid=hostOidMap.size();
			hostOidMap.put(ip, oid);
		}
		return hostOidMap.get(ip);
	}
	/**
	 * 主控初始化自己
	 * @param ipport
	 */
	public static void setMainHost(String ipport) {
		HostInfo host=hostMap.get(ipport);
		setHostInfo(ipport, host);
		setCurrentHost(host);
	}
	public static boolean isMainHost() {
		if(currentHost.getIpport().equals(".")) return true;
		if(currentHost.getIpport().indexOf(RuntimeData.getServerIp()+":")>=0) {
			return true;
		}else {
			return false;
		}
	}
	/**
	 * 添加主机信息 服务端调用
	 * @param ip
	 * @param host
	 */
	public static void setHostInfo(String ipport,HostInfo host) {
		host.setOid(String.valueOf(getHostOid(ipport)));
		hostMap.put(ipport, host);
	}
	/**
	 * 返回主机map 服务端调用
	 * @return
	 */
	public static Map<String, HostInfo> getHostMap() {
		return hostMap;
	}
	/**
	 * 清除超时主机  服务端调用
	 */
	public static List<HostInfo> getTimeoutHost(){
		List<HostInfo> list=new ArrayList<HostInfo>();
		long time=new Date().getTime();
		for(HostInfo hi : hostMap.values()){
			if(isMainHost()) continue;
			if(hi.getLastDate().getTime()<time-31*1000){
				list.add(hi);
			}
		}
		return list;
	}
	/**
	 * 移除指定ip主机信息 服务器调用
	 * @param ip
	 */
	public static void removeHost(String ipport) {
		hostMap.remove(ipport);
	}
	/**
	 * 设置主机map 客户端调用
	 * @param ipport
	 * @param hostMap
	 */
	public static void setHostMap(String ipport,Map<String, HostInfo> hostMap) {
		for(HostInfo host : hostMap.values()) {
			if(host.getIpport().equals(ipport)) {
				setCurrentHost(host);
			}
		}
		HostInfoService.hostMap = hostMap;
		resetCurrentHostOtherInfo();
	}
	
	public static int getCurrentOid() {
		return currentOid;
	}
}
