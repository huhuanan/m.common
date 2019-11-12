package m.common.model;

import java.util.Date;

import m.common.model.type.FieldType;

public class HostInfo extends Model {

	@FieldMeta(name="ip",type=FieldType.STRING,length=20,description="ip")
	private String ip;
	@FieldMeta(name="ipport",type=FieldType.STRING,length=20,description="ipport")
	private String ipport;
	@FieldMeta(name="total",type=FieldType.INT,description="total")
	private Integer total;//

	@FieldMeta(name="create_date",type=FieldType.DATE,description="create_date")
	private Date createDate=new Date();
	@FieldMeta(name="last_date",type=FieldType.DATE,description="last_date")
	private Date lastDate;

	@FieldMeta(name="total_memory",type=FieldType.DOUBLE,description="total_memory")
	private Double totalMemory=0.0;//JVM总内存
	@FieldMeta(name="free_memory",type=FieldType.DOUBLE,description="free_memory")
	private Double freeMemory=0.0;//JVM分配内存
	@FieldMeta(name="max_memory",type=FieldType.DOUBLE,description="max_memory")
	private Double maxMemory=0.0;//JVM最大内存
	@FieldMeta(name="db_use_link_num",type=FieldType.INT,description="db_use_link_num")
	private Integer dbUseLinkNum=0;//数据库当前连接数
	@FieldMeta(name="db_max_link_num",type=FieldType.INT,description="db_max_link_num")
	private Integer dbMaxLinkNum=0;//数据库连接数峰值
	
	public Integer getDbMaxLinkNum() {
		return dbMaxLinkNum;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public void setDbMaxLinkNum(Integer dbMaxLinkNum) {
		this.dbMaxLinkNum = dbMaxLinkNum;
	}
	public Integer getDbUseLinkNum() {
		return dbUseLinkNum;
	}
	public void setDbUseLinkNum(Integer dbUseLinkNum) {
		this.dbUseLinkNum = dbUseLinkNum;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Date getLastDate() {
		return lastDate;
	}
	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}
	public Double getTotalMemory() {
		return totalMemory;
	}
	public void setTotalMemory(Double totalMemory) {
		this.totalMemory = totalMemory;
	}
	public Double getFreeMemory() {
		return freeMemory;
	}
	public String getIpport() {
		return ipport;
	}
	public void setIpport(String ipport) {
		this.ipport = ipport;
	}
	public void setFreeMemory(Double freeMemory) {
		this.freeMemory = freeMemory;
	}
	public Double getMaxMemory() {
		return maxMemory;
	}
	public void setMaxMemory(Double maxMemory) {
		this.maxMemory = maxMemory;
	}
}
