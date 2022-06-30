package com.smoc.cloud.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * smoc
 */
@EnableAsync
@EnableScheduling
@EnableBatchProcessing
@SpringBootApplication
public class SmocSchedulerFiltersApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmocSchedulerFiltersApplication.class, args);
    }

}
