package m.system.task;

import java.util.List;
import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import m.system.SystemTaskRun;
import m.system.cache.run.CacheHostTask;
import m.system.util.AnnotationUtil;
import m.system.util.ClassUtil;
import m.system.util.StringUtil;

public class TaskUtil {
	private static Scheduler scheduler;
	public static void initTask(List<String[]> list){
		try {
			SchedulerFactory sFactory = new StdSchedulerFactory();
			scheduler = sFactory.getScheduler();
			JobDetail job=null;
			//初始化缓存定时器
			job=JobBuilder.newJob(CacheHostTask.class).build();
			job.getJobDataMap().put("method", "run");
			scheduler.scheduleJob(job,
				TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule("1 * * * * ?")).build());
			for(String[] strs : list){
				System.out.println(strs[0]+"--"+strs[1]);
				Class<? extends SystemTaskRun> runClass=ClassUtil.getClass(SystemTaskRun.class,strs[0].trim());
				if(!StringUtil.isSpace(strs[1].trim())) {
					job=JobBuilder.newJob(runClass).build();
					job.getJobDataMap().put("method", "run");
					scheduler.scheduleJob(job,TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule(strs[1].trim())).build());
				}
				Map<String,TaskRunMeta> map=AnnotationUtil.getAnnotationMap4Method(TaskRunMeta.class, runClass);
				for(String method : map.keySet()) {
					String cron=map.get(method).cron().trim();
					if(!StringUtil.isSpace(cron)) {
						job=JobBuilder.newJob(runClass).build();
						job.getJobDataMap().put("method", method);
						scheduler.scheduleJob(job,TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule(cron)).build());
					}
				}
			}
			scheduler.start();
			System.out.println("定时任务已初始化完毕!");
		} catch (SchedulerException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	public static void closeTask() {
		try {
			if(null!=scheduler) {
				scheduler.shutdown(true);
				System.out.println("定时任务已关闭!");
			}
		} catch (SchedulerException e) {
			System.out.println("定时任务关闭失败!");
			e.printStackTrace();
		}
	}
	
}
