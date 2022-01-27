package com.smoc.cloud.customer.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.AccountInterfaceInfoValidator;
import com.smoc.cloud.common.utils.DES;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.PasswordUtils;
import com.smoc.cloud.customer.entity.AccountBasicInfo;
import com.smoc.cloud.customer.entity.AccountInterfaceInfo;
import com.smoc.cloud.customer.repository.AccountFinanceRepository;
import com.smoc.cloud.customer.repository.AccountInterfaceRepository;
import com.smoc.cloud.customer.repository.BusinessAccountRepository;
import com.smoc.cloud.finance.entity.FinanceAccount;
import com.smoc.cloud.finance.repository.FinanceAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Optional;

/**
 * 业务账号接口管理
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AccountInterfaceService {

    @Resource
    private AccountInterfaceRepository accountInterfaceRepository;

    @Resource
    private BusinessAccountRepository businessAccountRepository;


    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    public ResponseData findById(String id) {
        Optional<AccountInterfaceInfo> data = accountInterfaceRepository.findById(id);

        if (!data.isPresent()) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_QUERY_ERROR);
        }

        AccountInterfaceInfo entity = data.get();
        AccountInterfaceInfoValidator accountInterfaceInfoValidator = new AccountInterfaceInfoValidator();
        BeanUtils.copyProperties(entity, accountInterfaceInfoValidator);

        //转换日期
        accountInterfaceInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(entity.getCreatedTime()));

        return ResponseDataUtil.buildSuccess(accountInterfaceInfoValidator);
    }

    /**
     * 保存或修改
     *
     * @param accountInterfaceInfoValidator
     * @param op  操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData<AccountInterfaceInfo> save(AccountInterfaceInfoValidator accountInterfaceInfoValidator, String op) {

        Optional<AccountInterfaceInfo> data = accountInterfaceRepository.findById(accountInterfaceInfoValidator.getAccountId());

        AccountInterfaceInfo entity = new AccountInterfaceInfo();
        BeanUtils.copyProperties(accountInterfaceInfoValidator, entity);

        if("add".equals(op)){
            String passWord = PasswordUtils.getRandomPassword(9);
            entity.setAccountPassword(DES.encrypt(passWord));//加密
        }

        //add查重
        if (data.isPresent() && "add".equals(op)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
        }
        //edit查重
        else if (data.isPresent() && "edit".equals(op)) {
            boolean status = false;
            if (!entity.getAccountId().equals(data.get().getAccountId())) {
                status = true;
            }
            if (status) {
                return ResponseDataUtil.buildError(ResponseCode.PARAM_CREATE_ERROR);
            }
            entity.setAccountPassword(data.get().getAccountPassword());
        }

        //转换日期格式
        entity.setCreatedTime(DateTimeUtils.getDateTimeFormat(accountInterfaceInfoValidator.getCreatedTime()));

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        //设置账号完成进度
        if("add".equals(op)){
            Optional<AccountBasicInfo> optional = businessAccountRepository.findById(accountInterfaceInfoValidator.getAccountId());
            if(optional.isPresent()){
                AccountBasicInfo accountBasicInfo = optional.get();
                StringBuffer accountProcess = new StringBuffer(accountBasicInfo.getAccountProcess());
                accountProcess = accountProcess.replace(2, 3, "1");
                accountBasicInfo.setAccountProcess(accountProcess.toString());
                businessAccountRepository.save(accountBasicInfo);
            }
        }

        //记录日志
        log.info("[EC业务账号管理][业务账号接口信息][{}]数据:{}", op, JSON.toJSONString(entity));
        accountInterfaceRepository.saveAndFlush(entity);

        return ResponseDataUtil.buildSuccess();
    }


}