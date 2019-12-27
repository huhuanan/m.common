package m.common.netty;

import java.util.Date;

import m.common.model.HostInfo;
import m.common.service.HostInfoService;
import m.system.RuntimeData;
import m.system.cache.CacheUtil;
import m.system.netty.NettyEvent;
import m.system.netty.NettyMessage;
import m.system.netty.NettyServer;
import m.system.util.StringUtil;

public class HostNettyServerEvent extends NettyEvent<NettyMessage> {

	@Override
	public NettyMessage readOrReturn(String ipport, NettyMessage msg) {
		System.out.println("server read: "+msg);
		String auth=msg.get(String.class,"server_auth");
		if(!StringUtil.noSpace(auth).equals(RuntimeData.getServerAuth())) {
			return null;//认证串错误,不答复
		}
		//System.out.println("server readOrReturn:"+msg);
		HostInfo host=msg.get(HostInfo.class,"host_host");
		if(null!=host) {
			//String ip=HostNettyUtil.getIp(ipport);
			host.setIpport(ipport);
			host.setLastDate(new Date());
			HostInfoService.setHostInfo(ipport, host);
			if(ipport.indexOf(RuntimeData.getServerIp()+":")>=0) {
				NettyMessage result=new NettyMessage();
				result.push("host_main", true);
				return result;
			}else {
				return hostMapMessage();
			}
		}
		//缓存处理
		CacheUtil.readNettyServerMessage(ipport, msg);
		return null;
	}
	public void closeCallback(String ipport) {
		//掉线主机清除，并通知所有主机
		if(ipport.indexOf(RuntimeData.getServerIp()+":")<0) {
			HostInfoService.removeHost(ipport);
			NettyServer<NettyMessage> server=HostNettyUtil.getServer();
			if(null!=server) {
				server.sendAll(hostMapMessage());
			}
		}
	}
	private NettyMessage hostMapMessage() {
		NettyMessage result=new NettyMessage();
		result.push("host_hostMap", HostInfoService.getHostMap());
		return result;
	}

	public void sendBefore(String ipport, NettyMessage msg) {
		msg.push("server_auth", RuntimeData.getServerAuth());
		super.sendBefore(ipport, msg);
	}
	public void sendCallback(String ipport, NettyMessage msg) {
		super.sendCallback(ipport, msg);
		//System.out.println(ipport+msg);
	}

//	public void openCallback(String ipport) {
//		// TODO Auto-generated method stub
//		super.openCallback(ipport);
//	}

	
}
