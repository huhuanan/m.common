package m.common.model;

import m.system.cache.FlushCache;

public interface SessionModel extends FlushCache {

	public String getOid();
	public void setOid(String oid);
	public String getRealname();
	public void setRealname(String realname);
	public String getUsername();
	public void setUsername(String username);
	public String getToken();
	public void setToken(String token);
}
