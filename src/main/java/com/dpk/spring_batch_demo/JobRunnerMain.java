package com.dpk.spring_batch_demo;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class JobRunnerMain {

    public static void main(String[] args) {
        // Build context manually (non-web app)
        ConfigurableApplicationContext context =
                new SpringApplicationBuilder(SpringBatchDemoApplication.class)
                        .web(WebApplicationType.NONE)
                        .run(args);

        try {
            // Get beans
            JobLauncher jobLauncher = context.getBean(JobLauncher.class);
            Job job = context.getBean("importUserJob", Job.class);

            // Launch the job with unique parameters
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution execution = jobLauncher.run(job, jobParameters);

            System.out.println(">>> Job Status: " + execution.getStatus());
            System.out.println(">>> Job finished successfully: " + (execution.getStatus() == BatchStatus.COMPLETED));

        } catch (Exception e) {
            System.err.println(">>> Job failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            context.close();
        }
    }
}
