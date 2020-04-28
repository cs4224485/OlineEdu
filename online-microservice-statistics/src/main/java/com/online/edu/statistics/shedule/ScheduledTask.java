package com.online.edu.statistics.shedule;
import com.online.edu.statistics.service.StatisticsDailyService;
import com.online.edu.statistics.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduledTask {
    @Autowired
    StatisticsDailyService statisticsDailyService;
    /**
     8
     * 测试
     9
     * 每天七点到二十三点每五秒执行一次
     10
     */

    @Scheduled(cron = "0/5 * * * * ?")
    public void task1() {
        System.out.println("*********++++++++++++*****执行了");
    }

        /**
         17
         * 每天凌晨1点执行定时
         18
         */
    @Scheduled(cron = "0 0 1 * * ?")
    public void task2() {
        //获取上一天的日期
        String day = DateUtil.formatDate(DateUtil.addDays(new Date(), -1));
        statisticsDailyService.createStatisticsByDay(day);
    }
}

