package com.dpk.spring_batch_demo.config;

import com.dpk.spring_batch_demo.model.User;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.*;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.*;
import org.springframework.batch.item.database.*;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.*;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.*;
import org.springframework.batch.item.file.separator.DefaultRecordSeparatorPolicy;
import org.springframework.batch.item.file.transform.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    /*
    @Bean
    public FlatFileItemReader<User> reader() {
        return new FlatFileItemReaderBuilder<User>()
                .name("userItemReader")
                .resource(new ClassPathResource("users.csv"))
                .delimited()
                .names("firstName", "lastName", "email")
                .targetType(User.class)
                .linesToSkip(1)  // ✅ Skip the header row
                .build();
    }*/

    @Bean
    public FlatFileItemReader<User> reader() {
        return new FlatFileItemReaderBuilder<User>()
                .name("userItemReader")
                .resource(new ClassPathResource("users.csv"))
                .delimited()
                .names("firstName", "lastName", "email")
                .targetType(User.class)
                .linesToSkip(1)  // Skip header
                .recordSeparatorPolicy(new DefaultRecordSeparatorPolicy() {
                    @Override
                    public boolean isEndOfRecord(String line) {
                        return line != null && !line.trim().isEmpty() && super.isEndOfRecord(line);
                    }

                    @Override
                    public String postProcess(String record) {
                        return record != null ? record.trim() : record;
                    }
                })
                .build();
    }

    @Bean
    public ItemProcessor<User, User> processor() {
        return user -> {
            user.setFirstName(user.getFirstName().toUpperCase());
            user.setLastName(user.getLastName().toUpperCase());
            return user;
        };
    }

    @Bean
    public JdbcBatchItemWriter<User> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<User>()
                .sql("INSERT INTO users (first_name, last_name, email) VALUES (:firstName, :lastName, :email)")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }

    @Bean
    public Job importUserJob(JobRepository jobRepository, Step step1, JobExecutionListener listener) {
        return new JobBuilder("importUserJob", jobRepository)  // <-- pass jobRepository here
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public JobExecutionListener listener() {
        return new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {
                System.out.println("Job Started");
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                System.out.println("Job Ended with Status: " + jobExecution.getStatus());
            }
        };
    }

/*
    @Bean
    public Step step1(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager,
                      ItemReader<User> reader,
                      ItemProcessor<User, User> processor,
                      ItemWriter<User> writer) {

        return new StepBuilder("step1", jobRepository)
                .<User, User>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }*/

    @Bean
    public Step step1(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager,
                      ItemReader<User> reader,
                      ItemProcessor<User, User> processor,
                      ItemWriter<User> writer,
                      SkipPolicy skipPolicy) {

        return new StepBuilder("step1", jobRepository)
                .<User, User>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skipPolicy(skipPolicy) // ✅ Apply custom skip policy
                .build();
    }

    /*
    @Bean
    public SkipPolicy skipPolicy() {
        return new SkipPolicy() {

            @Override
            public boolean shouldSkip(Throwable t, long skipCount) throws SkipLimitExceededException {
                if (t instanceof FlatFileParseException && skipCount < 10) {
                    System.err.println("Skipping line due to parse error: " + t.getMessage());
                    return true;
                }
                return false;
            }
        };
    }*/

    @Bean
    public SkipPolicy skipPolicy() {
        return (throwable, skipCount) -> throwable instanceof FlatFileParseException && skipCount < 10;
    }
}
