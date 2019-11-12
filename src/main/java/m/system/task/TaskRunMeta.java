package m.system.task;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // 注解会在class字节码文件中存在，在运行时可以通过反射获取到  
@Target({ElementType.METHOD})//定义注解的作用目标**作用范围字段、枚举的常量/方法  
@Documented//说明该注解将被包含在javadoc中 
public @interface TaskRunMeta {
	/** 添加到配置的定时器类里才生效
	 * Quartz的cronSchedule  每天0点0分0秒执行: 秒0 分钟0 小时0 日期* 月* 星期? 年
	 * @return
	 */
	String cron();
}
