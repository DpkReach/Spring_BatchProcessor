package com.dpk.spring_batch_demo.config;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println(">>> Job is starting...");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println(">>> Job finished with status: " + jobExecution.getStatus());
    }
}
