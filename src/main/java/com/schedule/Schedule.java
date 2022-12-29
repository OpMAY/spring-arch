package com.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
public class Schedule {
//    @Scheduled(cron = "*/600 * * * * *")
//    public void Schedule() throws InterruptedException {
//    }
}
