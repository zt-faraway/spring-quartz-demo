package com.dexcoder.demo.quartz;

import com.codrim.common.utils.json.JsonMapper;
import com.dexcoder.demo.model.ScheduleJob;
import com.dexcoder.demo.utils.SpringContextHolder;
import com.dexcoder.demo.vo.ScheduleJobVO;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;


@DisallowConcurrentExecution
public class ExecutalbeJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(ExecutalbeJob.class);

    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("ExecutalbeJob execute");
        ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get(
            ScheduleJobVO.JOB_PARAM_KEY);
    try {

        Class clazz = getClass(scheduleJob.getJobClassOrInterfaceFullName());
        Object jobInstance = SpringContextHolder.getBean(clazz);
        Class paramType = getClass(scheduleJob.getJobMethodParamType());

        Method method = ReflectionUtils.findMethod(clazz, scheduleJob.getJobMethodName(), paramType);
        method.setAccessible(true);
        method.invoke(jobInstance, JsonMapper.nonEmptyMapper().fromJson(scheduleJob.getJobMethodParamValue(), paramType));

        System.out.println("jobName:" + scheduleJob.getJobName() + "  " + scheduleJob);
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

    private static Class getClass(String fullClassName) {
        Class clazz = null;
        try {
            clazz = Class.forName(fullClassName);
        } catch (Exception e) {
            logger.error("class not found:{}",fullClassName);
        }
        return clazz;
    }
}
