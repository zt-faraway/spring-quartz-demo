package com.dexcoder.demo.model;

import lombok.Data;

import java.util.Date;

@Data
public class ScheduleJob  {

    /**
     * 任务id
     */
    private Long scheduleJobId;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务别名
     */
    private String aliasName;

    /**
     * 任务分组
     */
    private String jobGroup;

    /*  任务类/接口全名 */
    private String jobClassOrInterfaceFullName;
    /* 任务类的执行方法 */
    private String jobMethodName;
    /*  执行方法的参数类型  */
    private String jobMethodParamType;
    /*  执行方法参数的json值  */
    private String jobMethodParamValue;
    /**
     * 任务状态
     */
    private String status;

    /**
     * 任务运行时间表达式
     */
    private String cronExpression;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModify;

}
