package m.system;

import m.common.service.HostInfoService;

public abstract class SystemInitRun {
	/**
	 * 系统初始化方法 
	 * 可用于 初始化系统之前初始化配置信息
	 * 建议修改属性 model_pack action_pack secret_field static_field
	 */
	public void init() {
	}
	/**
	 * 初始化运行方法
	 * @param isMain 是否主控服务器, 如果需要更新sql,建议只在true的情况下执行
	 */
	public abstract void run(boolean isMain);
	
	public void execute(){
		boolean isMain=false;
		//HostInfo host=RuntimeData.getHostInfo();
		if(HostInfoService.isMainHost()) isMain=true;
		run(isMain);
	}
}
