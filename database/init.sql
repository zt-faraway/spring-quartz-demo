DROP TABLE IF EXISTS schedule_job;
CREATE TABLE schedule_job(schedule_job_id BIGINT(20) NOT NULL auto_increment,
                          job_name varchar(255) DEFAULT NULL,
                          alias_name varchar(255) DEFAULT NULL,
                          job_group varchar(255) DEFAULT NULL,
                          job_class_or_interface_full_name varchar(255) DEFAULT NULL,
                          job_method_name varchar(255) DEFAULT NULL,
                          job_method_param_type varchar(255) DEFAULT NULL,
                          job_method_param_value varchar(255) DEFAULT NULL,
                          status varchar(255) DEFAULT NULL,
                          cron_expression varchar(255) DEFAULT NULL,
                          description VARCHAR(255) DEFAULT NULL,
                          gmt_create TIMESTAMP NULL DEFAULT NULL,
                          gmt_modify TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY (schedule_job_id)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
alter table schedule_job add unique key `job_group_name` (job_group,job_name);
DELETE FROM schedule_job WHERE schedule_job_id = 11;
INSERT INTO `schedule_job` (`schedule_job_id`, `job_name`, `alias_name`, `job_group`, `job_class_or_interface_full_name`, `job_method_name`, `job_method_param_type`, `job_method_param_value`, `status`, `cron_expression`, `description`, `gmt_create`, `gmt_modify`)
VALUES
  (11, 'job_name_1', 'job_alias_1', 'job_group', 'com.dexcoder.demo.service.Business', 'fetchOffer', 'com.dexcoder.demo.vo.BusinessParamVO', '{\"companyId\":1,\"userId\":1}', '1', '0/5 * * * * ?', NULL, '2017-11-09 11:43:46', NULL);

insert into schedule_job VALUES ()
