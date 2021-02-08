package io.homo_efficio.scratchpad.springbatch.sample;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author homo.efficio@gmail.com
 * created on 2021-02-08
 */
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
@Slf4j
public class BatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Bean
    public Step step() {
        return stepBuilderFactory
                .get("Sample Step")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        log.info("Sample TaskLet executed");
                        return RepeatStatus.FINISHED;
                    }
                })
                .listener(new StepExecutionListener() {
                    @Override
                    public void beforeStep(StepExecution stepExecution) {

                    }

                    @Override
                    public ExitStatus afterStep(StepExecution stepExecution) {
                        log.info("stepName: " + stepExecution.getStepName());
                        log.info("stepStartedAt: " + stepExecution.getStartTime());
                        log.info("stepEndedAt: " + stepExecution.getEndTime());  // getEndTime() returns null
                        return stepExecution.getExitStatus();
                    }
                })
                .build();
    }

    @Bean
    public Job job() {
        return jobBuilderFactory
                .get("Sample Job")
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {

                    }

                    @Override
                    public void afterJob(JobExecution jobExecution) {
                        log.info("jobName: " + jobExecution.getJobInstance().getJobName());
                        log.info("jobStartedAt: " + jobExecution.getStartTime());
                        log.info("jobEndedAt: " + jobExecution.getEndTime());
                    }
                })
                .start(step())
                .build();
    }
}
