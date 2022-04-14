package com.smoc.cloud.common.smoc.customer.qo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountStatisticSendData {

    private String accountId;

    private int index;

    private String month;

    private String sendNumber;

    private String[] monthArray;

    private String[] sendNumberArray;

    //统计维度
    private String dimension;
    private String startDate;
    private String endDate;
}
