package com.smoc.cloud.statistic.data.service;


import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.spss.model.StatisticModel;
import com.smoc.cloud.common.smoc.spss.qo.ManagerStatisticQo;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.statistic.data.remote.ManagerStatisticsFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * 运营数据统计分析
 */
@Slf4j
@Service
public class ManagerStatisticsService {

    @Autowired
    private ManagerStatisticsFeignClient managerStatisticsFeignClient;

    /**
     * 运营管理综合日统计
     * @param managerStatisticQo
     * @return
     */
    public StatisticModel managerDailyStatistic(ManagerStatisticQo managerStatisticQo) {

        ResponseData<List<ManagerStatisticQo>> responseData = this.managerStatisticsFeignClient.managerDailyStatistic(managerStatisticQo);
        List<ManagerStatisticQo> list = responseData.getData();

        //每一天日期
        String[] day = list.stream().map(ManagerStatisticQo::getMessageDate).toArray(String[]::new);
        //发送量
        BigDecimal[] sendAmount = list.stream().map(ManagerStatisticQo::getSendAmount).toArray(BigDecimal[]::new);
        //营收
        BigDecimal[] incomeAmount = list.stream().map(ManagerStatisticQo::getIncomeAmount).toArray(BigDecimal[]::new);
        //净利润
        BigDecimal[] profitAmount = list.stream().map(ManagerStatisticQo::getProfitAmount).toArray(BigDecimal[]::new);

        StatisticModel model = new StatisticModel();
        model.setDate(day);
        model.setSendAmount(sendAmount);
        model.setIncomeAmount(incomeAmount);
        model.setProfitAmount(profitAmount);

        return model;
    }

    /**
     * 运营管理综合月统计
     * @param managerStatisticQo
     * @return
     */
    public StatisticModel managerMonthStatistic(ManagerStatisticQo managerStatisticQo) {
        ResponseData<List<ManagerStatisticQo>> responseData = this.managerStatisticsFeignClient.managerMonthStatistic(managerStatisticQo);
        List<ManagerStatisticQo> list = responseData.getData();

        //封装月份
        Map<String, ManagerStatisticQo> monthMap = buildMonthStatistics(managerStatisticQo.getEndDate(),24);
        if(!StringUtils.isEmpty(list) && list.size()>0){
            for(int i=0;i<list.size();i++){
                ManagerStatisticQo info = list.get(i);
                monthMap.put(info.getMessageDate(),info);
            }
        }

        //月份
        String[] month = new String[monthMap.size()];
        //发送量
        BigDecimal[] sendAmount = new BigDecimal[monthMap.size()];
        //营收
        BigDecimal[] incomeAmount = new BigDecimal[monthMap.size()];
        //净利润
        BigDecimal[] profitAmount = new BigDecimal[monthMap.size()];
        int i = 0;
        for (String key : monthMap.keySet()) {
            ManagerStatisticQo info = monthMap.get(key);
            month[i] = key;
            sendAmount[i] = info.getSendAmount();
            incomeAmount[i] = info.getIncomeAmount();
            profitAmount[i] = info.getProfitAmount();
            if(StringUtils.isEmpty(info.getSendAmount())){
                sendAmount[i] = new BigDecimal(0);
            }
            if(StringUtils.isEmpty(info.getIncomeAmount())){
                incomeAmount[i] = new BigDecimal(0);
            }
            if(StringUtils.isEmpty(info.getProfitAmount())){
                profitAmount[i] = new BigDecimal(0);
            }
            i++;
        }

        StatisticModel model = new StatisticModel();
        model.setDate(month);
        model.setSendAmount(sendAmount);
        model.setIncomeAmount(incomeAmount);
        model.setProfitAmount(profitAmount);

        return model;
    }

    /**
     * 查询当前月份和向后推N个月
     *
     * @return
     */
    public static Map<String, ManagerStatisticQo> buildMonthStatistics(String startDate, int a) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        // 当前月份
        Date nowDate;
        try {
            nowDate = sdf.parse(startDate);
            Map<String, ManagerStatisticQo> map = new TreeMap<String, ManagerStatisticQo>();
            map.put(startDate, new ManagerStatisticQo());
            int z = 1;
            for (int i = 1; i < a; i++) {
                Date subtract = DateTimeUtils.dateAddMonths(nowDate, -i);
                map.put(sdf.format(subtract), new ManagerStatisticQo());
            }

            return map;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
