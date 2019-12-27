package m.common.netty;

import java.util.Map;

import m.common.model.HostInfo;
import m.common.service.HostInfoService;
import m.system.RuntimeData;
import m.system.cache.CacheUtil;
import m.system.netty.NettyEvent;
import m.system.netty.NettyMessage;
import m.system.util.StringUtil;

public class HostNettyClientEvent extends NettyEvent<NettyMessage> {
	public NettyMessage readOrReturn(String ipport, NettyMessage msg) {
		//System.out.println("client read: "+msg);
		String auth=msg.get(String.class,"server_auth");
		if(!StringUtil.noSpace(auth).equals(RuntimeData.getServerAuth())) {
			return null;//认证串错误,不答复
		}
		//System.out.println("client readOrReturn:"+msg);
		Boolean main=msg.get(Boolean.class,"host_main");
		if(null!=main&&main) {
			//String ip=HostNettyUtil.getIp(ipport);
			HostInfoService.setMainHost(ipport);
			HostNettyUtil.closeClient(false);
		}else {
			Map<String, HostInfo> hostMap=msg.get(Map.class, "host_hostMap");
			if(null!=hostMap) {
				//String ip=HostNettyUtil.getIp(ipport);
				HostInfoService.setHostMap(ipport,hostMap);
				HostNettyUtil.closeServer(false);
			}
		}
		//缓存处理
		CacheUtil.readNettyClientMessage(ipport, msg);
		return null;
	}
	public void exceptionCallback(String ipport, Throwable cause) {
		HostNettyUtil.reopenClient();//发生异常重启客户端
	}
	public void closeCallback(String ipport) {
		HostNettyUtil.reopenClient();
	}

	public void sendBefore(String ipport, NettyMessage msg) {
		msg.push("server_auth", RuntimeData.getServerAuth());
		super.sendBefore(ipport, msg);
	}
	public void sendCallback(String ipport, NettyMessage msg) {
		super.sendCallback(ipport, msg);
		//System.out.println(ipport+msg);
	}
//
//	public void openCallback(String ipport) {
//		// TODO Auto-generated method stub
//		super.openCallback(ipport);
//	}



}
