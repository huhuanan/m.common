package m.system.cache.run;

import java.util.Date;

import m.system.SystemTaskRun;
import m.system.cache.CacheHost;
import m.system.cache.CacheUtil;
import m.system.util.DateUtil;

public class CacheHostTask extends SystemTaskRun {
	private static int timeoutSeconds=20*60;
	public static int getTimeoutSeconds() {
		return timeoutSeconds;
	}


	@Override
	public void run(boolean isMain) {
		if(isMain) {
			int m=Integer.parseInt(DateUtil.format(new Date(), "mm"));
			if(m%10==0) {
				String[] ls=CacheHost.getTimeoutCaches(timeoutSeconds*1000);
				for(String key : ls) {
					CacheUtil.clear(key,true);
				}
			}
		}else {
			String[] ls=CacheHost.getTimeoutCaches(1*60*1000);
			for(String key : ls) {
				CacheUtil.clear(key,false);
			}
		}
	}

}
