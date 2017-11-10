package com.dexcoder.demo.service;

import java.util.List;

import com.dexcoder.demo.vo.ScheduleJobVO;

/**
 * 定时任务service
 *
 * Created by liyd on 12/19/14.
 */
public interface ScheduleJobService {

    /**
     * 初始化定时任务
     */
    void initScheduleJob();

    /**
     * 新增
     * 
     * @param scheduleJobVO
		*/
    Long insert(ScheduleJobVO scheduleJobVO);

    /**
     * 直接修改 只能修改运行的时间，参数、同异步等无法修改
     * 
     * @param scheduleJobVO
     */
    void update(ScheduleJobVO scheduleJobVO);

    /**
     * 删除重新创建方式
     * 
     * @param scheduleJobVO
     */
    void delUpdate(ScheduleJobVO scheduleJobVO);

    /**
     * 删除
     * 
     * @param scheduleJobId
     */
    void delete(Long scheduleJobId);

    /**
     * 运行一次任务
     *
     * @param scheduleJobId the schedule job id
		*/
    void runOnce(Long scheduleJobId);

    /**
     * 暂停任务
     *
     * @param scheduleJobId the schedule job id
		*/
    void pauseJob(Long scheduleJobId);

    /**
     * 恢复任务
     *
     * @param scheduleJobId the schedule job id
		*/
    void resumeJob(Long scheduleJobId);

    /**
     * 获取任务对象
     * 
     * @param scheduleJobId
		*/
    ScheduleJobVO get(Long scheduleJobId);

    /**
     * 查询任务列表
     * 
     * @param scheduleJobVO
		*/
    List<ScheduleJobVO> queryList(ScheduleJobVO scheduleJobVO);

    /**
     * 获取运行中的任务列表
     */
    List<ScheduleJobVO> queryExecutingJobList();

}
