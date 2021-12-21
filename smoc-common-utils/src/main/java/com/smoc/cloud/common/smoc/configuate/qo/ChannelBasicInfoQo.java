package com.smoc.cloud.common.smoc.configuate.qo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ChannelBasicInfoQo {
    private String channelId;
    //通道名称
    private String channelName;
    //发送号码范围
    private String carrier;
    //通道供应商
    private String channelProvder;
    //业务类型
    private String businessType;
    //抗诉率
    private BigDecimal maxComplaintRate;
    //码号
    private String srcId;
    //通道协议类型
    private String protocol;
    //通道对接账号
    private String channelAccessAccount;
    //计价方式
    private String priceStyle;
    //通道价格
    private String channelPrice;
    //通道区域范围
    private String businessAreaType;
    //屏蔽区域
    private String maskProvince;
    //业务区域
    private String supportAreaCodes;
    //通道运行状态
    private String channelRunStatus;
    //通道状态
    private String channelStatus;
    //最大速率
    private Integer maxSendSecond;
    //通道介绍
    private String channelIntroduce;
    //信息分类
    private String infoType;
    //通道对接销售
    private String channelAccessSalse;
    //接入省份
    private String accessProvince;
}