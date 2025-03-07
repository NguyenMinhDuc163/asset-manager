package com.ptit.asset.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SchedulingTaskSystem {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Value("${spring.schema-name}")
    private String schemaName;
    @Value("${spring.device-name}")
    private String deviceName;

    // Just close to avoid create much version -> open when deploy
//    @Scheduled(cron = "00 30 21 * * MON-SAT")// mean: “At 21:45 on every day-of-week from Monday through Saturday.”
//    void backUpDatabase(){
//        logger.info("==> Starting backup database automatic processing === Current === : {}", new Date());
//        String query = "USE Master " +
//                "BACKUP DATABASE " +schemaName+ " TO " + deviceName;
//        jdbcTemplate.execute(query);
//    }

}

// template schedule
// @Scheduled(cron = "0 0 * * MON-SAT")// mean: “At 00:00 on every day-of-week from Monday through Saturday.”