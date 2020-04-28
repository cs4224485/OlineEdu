package com.online.edu.statistics.service;

import com.online.edu.statistics.entity.StatisticsDaily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface StatisticsDailyService extends IService<StatisticsDaily> {
    void createStatisticsByDay(String day);

    Map<String, Object> getChartData(String begin, String end, String type);

}
