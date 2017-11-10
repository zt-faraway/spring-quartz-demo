package com.dexcoder.demo.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.codrim.common.utils.bean.BeanUtils;
import com.dexcoder.demo.vo.ScheduleJobVO;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dexcoder.commons.bean.BeanConverter;
import com.dexcoder.dal.JdbcDao;
import com.dexcoder.dal.build.Criteria;
import com.dexcoder.demo.model.ScheduleJob;
import com.dexcoder.demo.service.ScheduleJobService;
import com.dexcoder.demo.utils.ScheduleUtils;

import javax.annotation.PostConstruct;

@Service
public class ScheduleJobServiceImpl implements ScheduleJobService {

    /** 调度工厂Bean */
    @Autowired
    private Scheduler scheduler;

    /** 通用dao */
    @Autowired
    private JdbcDao   jdbcDao;

    @PostConstruct
    public void initScheduleJob() {
        List<ScheduleJob> scheduleJobList = jdbcDao.queryList(Criteria.select(ScheduleJob.class));
        if (CollectionUtils.isEmpty(scheduleJobList)) {
            return;
        }
        for (ScheduleJob scheduleJob : scheduleJobList) {
            //不存在，创建一个
            if (ScheduleUtils.getCronTrigger(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup()) == null) {
                ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
            } else {
                //已存在，那么更新相应的定时设置
                ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
            }
        }
    }

    public Long insert(ScheduleJobVO scheduleJobVO) {
        ScheduleJob scheduleJob = BeanUtils.copyProperties(scheduleJobVO, ScheduleJob.class);
        ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
        return jdbcDao.insert(scheduleJob);
    }

    public void update(ScheduleJobVO scheduleJobVO) {
        ScheduleJob scheduleJob = BeanUtils.copyProperties(scheduleJobVO, ScheduleJob.class);
        ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
        jdbcDao.update(scheduleJob);
    }

    public void delUpdate(ScheduleJobVO scheduleJobVO) {
        ScheduleJob scheduleJob = BeanUtils.copyProperties(scheduleJobVO, ScheduleJob.class);
        //先删除
        ScheduleUtils.deleteScheduleJob(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup());
        //再创建
        ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
        //数据库直接更新即可
        jdbcDao.update(scheduleJob);
    }

    public void delete(Long scheduleJobId) {
        ScheduleJob scheduleJob = jdbcDao.get(ScheduleJob.class, scheduleJobId);
        //删除运行的任务
        ScheduleUtils.deleteScheduleJob(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup());
        //删除数据
        jdbcDao.delete(ScheduleJob.class, scheduleJobId);
    }

    public void runOnce(Long scheduleJobId) {
        ScheduleJob scheduleJob = jdbcDao.get(ScheduleJob.class, scheduleJobId);
        ScheduleUtils.runOnce(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup());
    }

    public void pauseJob(Long scheduleJobId) {
        ScheduleJob scheduleJob = jdbcDao.get(ScheduleJob.class, scheduleJobId);
        ScheduleUtils.pauseJob(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup());

        //演示数据库就不更新了
    }

    public void resumeJob(Long scheduleJobId) {
        ScheduleJob scheduleJob = jdbcDao.get(ScheduleJob.class, scheduleJobId);
        ScheduleUtils.resumeJob(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup());

        //演示数据库就不更新了
    }

    public ScheduleJobVO get(Long scheduleJobId) {
        ScheduleJob scheduleJob = jdbcDao.get(ScheduleJob.class, scheduleJobId);
        return BeanUtils.copyProperties(scheduleJob, ScheduleJobVO.class);
    }

    public List<ScheduleJobVO> queryList(ScheduleJobVO scheduleJobVO) {

        List<ScheduleJob> scheduleJobs = jdbcDao.queryList(BeanUtils.copyProperties(scheduleJobVO, ScheduleJob.class));

        List<ScheduleJobVO> scheduleJobVOList = BeanConverter.convert(ScheduleJobVO.class, scheduleJobs);
        try {
            for (ScheduleJobVO vo : scheduleJobVOList) {

                JobKey jobKey = JobKey.jobKey(vo.getJobName(), vo.getJobGroup());
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                if (CollectionUtils.isEmpty(triggers)) {
                    continue;
                }
                //这里一个任务可以有多个触发器， 但是我们一个任务对应一个触发器，所以只取第一个即可，清晰明了
                Trigger trigger = triggers.iterator().next();
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                vo.setStatus(triggerState.name());

                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    vo.setCronExpression(cronExpression);
                }
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return scheduleJobVOList;
    }

    public List<ScheduleJobVO> queryExecutingJobList() {
        List<ScheduleJobVO> jobList = null;
        try {
            List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
            jobList = new ArrayList<ScheduleJobVO>(executingJobs.size());
            for (JobExecutionContext executingJob : executingJobs) {
                ScheduleJobVO job = new ScheduleJobVO();
                JobDetail jobDetail = executingJob.getJobDetail();
                JobKey jobKey = jobDetail.getKey();
                Trigger trigger = executingJob.getTrigger();
                job.setJobName(jobKey.getName());
                job.setJobGroup(jobKey.getGroup());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                job.setStatus(triggerState.name());
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    job.setCronExpression(cronExpression);
                }
                jobList.add(job);
            }
        } catch (SchedulerException e) {
           e.printStackTrace();
        }
        return jobList;
    }
}
