package com.dexcoder.demo.controller;

import com.dexcoder.demo.service.ScheduleJobService;
import com.dexcoder.demo.vo.ScheduleJobVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;
import java.util.List;

@Controller
public class ScheduleJobController {

    @Autowired
    private ScheduleJobService scheduleJobService;

    /**
     * 新增任务页面
     */
    @RequestMapping(value = "input-schedule-job", method = RequestMethod.GET)
    public String inputScheduleJob(ScheduleJobVO scheduleJobVO, ModelMap modelMap) {
        if (scheduleJobVO.getScheduleJobId() != null) {
            ScheduleJobVO scheduleJob = scheduleJobService.get(scheduleJobVO.getScheduleJobId());
            modelMap.put("scheduleJobVO", scheduleJob);
        }
        return "input-schedule-job";
    }

    /**
     * 删除任务
     */
    @RequestMapping(value = "delete-schedule-job", method = RequestMethod.GET)
    public String deleteScheduleJob(Long scheduleJobId) {
        scheduleJobService.delete(scheduleJobId);
        return "redirect:list-schedule-job.shtml";
    }

    /**
     * 运行一次
     */
    @RequestMapping(value = "run-once-schedule-job", method = RequestMethod.GET)
    public String runOnceScheduleJob(Long scheduleJobId) {
        scheduleJobService.runOnce(scheduleJobId);
        return "redirect:list-schedule-job.shtml";
    }

    /**
     * 暂停
     */
    @RequestMapping(value = "pause-schedule-job", method = RequestMethod.GET)
    public String pauseScheduleJob(Long scheduleJobId) {
        scheduleJobService.pauseJob(scheduleJobId);
        return "redirect:list-schedule-job.shtml";
    }

    /**
     * 恢复
     */
    @RequestMapping(value = "resume-schedule-job", method = RequestMethod.GET)
    public String resumeScheduleJob(Long scheduleJobId) {
        scheduleJobService.resumeJob(scheduleJobId);
        return "redirect:list-schedule-job.shtml";
    }

    /**
     * 保存任务
     */
    @RequestMapping(value = "save-schedule-job", method = RequestMethod.POST)
    public String saveScheduleJob(ScheduleJobVO scheduleJobVO) {
        scheduleJobVO.setGmtCreate(new Date());
        scheduleJobVO.setJobClassOrInterfaceFullName("com.dexcoder.demo.service.Business");
        scheduleJobVO.setJobMethodName("fetchOffer");
        scheduleJobVO.setJobMethodParamType("com.dexcoder.demo.vo.BusinessParamVO");
        scheduleJobVO.setJobMethodParamValue("{\"companyId\":1,\"userId\":1}");
        //测试用随便设个状态
        scheduleJobVO.setStatus("1");
        if (scheduleJobVO.getScheduleJobId() == null) {
            scheduleJobService.insert(scheduleJobVO);
        } else {
            scheduleJobService.update(scheduleJobVO);
        }
        return "redirect:list-schedule-job.shtml";
    }

    /**
     * 任务列表页
     */
    @RequestMapping(value = "list-schedule-job", method = RequestMethod.GET)
    public String listScheduleJob(ScheduleJobVO scheduleJobVO, ModelMap modelMap) {

        List<ScheduleJobVO> scheduleJobVOList = scheduleJobService.queryList(scheduleJobVO);
        modelMap.put("scheduleJobVOList", scheduleJobVOList);

        List<ScheduleJobVO> executingJobList = scheduleJobService.queryExecutingJobList();
        modelMap.put("executingJobList", executingJobList);

        return "list-schedule-job";
    }

}
