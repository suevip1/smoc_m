package com.smoc.cloud.customer.controller;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.AccountSignRegisterValidator;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseSignCertifyValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.customer.service.AccountSignRegisterService;
import com.smoc.cloud.customer.service.EnterpriseSignCertifyService;
import com.smoc.cloud.sequence.service.SequenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("sign/register")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class AccountSignRegisterController {

    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    @Autowired
    private AccountSignRegisterService accountSignRegisterService;

    @Autowired
    private EnterpriseSignCertifyService enterpriseSignCertifyService;

    /**
     * 查询列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("sign/register/sign_register_list");

        //初始化数据
        PageParams<AccountSignRegisterValidator> params = new PageParams<>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        AccountSignRegisterValidator accountSignRegisterValidator = new AccountSignRegisterValidator();
        params.setParams(accountSignRegisterValidator);

        //查询
        ResponseData<PageList<AccountSignRegisterValidator>> data = accountSignRegisterService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("accountSignRegisterValidator", accountSignRegisterValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;

    }

    /**
     * 分页查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute AccountSignRegisterValidator accountSignRegisterValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("sign/register/sign_register_list");

        //分页查询
        pageParams.setParams(accountSignRegisterValidator);

        ResponseData<PageList<EnterpriseBasicInfoValidator>> data = accountSignRegisterService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("accountSignRegisterValidator", accountSignRegisterValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }

    /**
     * 新建
     *
     * @return
     */
    @RequestMapping(value = "/add/{account}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String account) {
        ModelAndView view = new ModelAndView("sign/register/sign_register_edit");

        //初始化参数
        AccountSignRegisterValidator accountSignRegisterValidator = new AccountSignRegisterValidator();
        accountSignRegisterValidator.setId(UUID.uuid32());
        accountSignRegisterValidator.setAccount(account);
        accountSignRegisterValidator.setRegisterStatus("1");
        accountSignRegisterValidator.setExtendType("1");
        Integer extendNumber = sequenceService.findSequence(account);
        log.info("sequenceService extendNumber:{}",extendNumber);
        if(null == extendNumber){
            extendNumber = sequenceService.findSequence("SIGN_EXTEND_NUMBER");
        }
        accountSignRegisterValidator.setSignExtendNumber(extendNumber + "");

        view.addObject("accountSignRegisterValidator", accountSignRegisterValidator);
        view.addObject("op", "add");

        //初始化签名资质数据
        PageParams<EnterpriseSignCertifyValidator> params = new PageParams<>();
        params.setPageSize(100000);
        params.setCurrentPage(1);
        EnterpriseSignCertifyValidator enterpriseSignCertifyValidator = new EnterpriseSignCertifyValidator();
        params.setParams(enterpriseSignCertifyValidator);
        //查询
        ResponseData<PageList<EnterpriseSignCertifyValidator>> data = enterpriseSignCertifyService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("certifies", data.getData().getList());

//        //查询账号被占用的签名子扩展号
//        ResponseData<List<String>> signNumberList = this.accountSignRegisterService.findExtendDataByAccount(account, "null");
//        if (!ResponseCode.SUCCESS.getCode().equals(signNumberList.getCode())) {
//            view.addObject("error", signNumberList.getCode() + ":" + signNumberList.getMessage());
//            return view;
//        }
        //将占用的签名子扩展号 转换成map方式
//        Map<String, String> signNumbers = new HashMap<>();
//        if (null != signNumberList.getData() && signNumberList.getData().size() > 0) {
//            for (String numbers : signNumberList.getData()) {
//                String[] numberArray = numbers.split(",");
//                for (String number : numberArray) {
//                    signNumbers.put(number, number);
//                }
//            }
//        }
//        log.info("[signNumberList.getData()]:{}",new Gson().toJson(signNumberList.getData()));
//        log.info("[signNumbers]:{}",new Gson().toJson(signNumbers));
        //向前台传值的时候，要去除已经使用的签名自扩展号
        List<String> signExtendNumbers = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            String number = "";
            if (i < 10) {
                number = "0"+i;
            } else {
                number = ""+i;
            }
            signExtendNumbers.add(number);
        }
        view.addObject("signExtendNumbers", signExtendNumbers);
        //log.info("[signExtendNumbers]:{}",new Gson().toJson(signExtendNumbers));

        /**
         * 把本id所占用的签名子扩展号，放到前端
         */
        Map<String,String> thisExtendNumberMap = new HashMap<>();
        view.addObject("thisExtendNumberMap", thisExtendNumberMap);

        /**
         * 把本id所占用的服务类型，放到前端
         */
        Map<String,String> thisServiceTypeMap = new HashMap<>();
        view.addObject("thisServiceTypeMap", thisServiceTypeMap);

        return view;
    }

    /**
     * 编辑
     *
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id) {
        ModelAndView view = new ModelAndView("sign/register/sign_register_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        /**
         * 修改:查询数据
         */
        ResponseData<AccountSignRegisterValidator> responseData = accountSignRegisterService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(responseData.getCode())) {
            view.addObject("error", responseData.getCode() + ":" + responseData.getMessage());
        }

        view.addObject("accountSignRegisterValidator", responseData.getData());
        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "edit");

        /**
         * 初始化签名资质数据
         */
        PageParams<EnterpriseSignCertifyValidator> params = new PageParams<>();
        params.setPageSize(100000);
        params.setCurrentPage(1);
        EnterpriseSignCertifyValidator enterpriseSignCertifyValidator = new EnterpriseSignCertifyValidator();
        params.setParams(enterpriseSignCertifyValidator);
        //查询
        ResponseData<PageList<EnterpriseSignCertifyValidator>> data = enterpriseSignCertifyService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }
        view.addObject("certifies", data.getData().getList());

        /**
         * 查询账号被占用的签名子扩展号
         */
//        ResponseData<List<String>> signNumberList = this.accountSignRegisterService.findExtendDataByAccount(responseData.getData().getAccount(), responseData.getData().getId());
//        if (!ResponseCode.SUCCESS.getCode().equals(signNumberList.getCode())) {
//            view.addObject("error", signNumberList.getCode() + ":" + signNumberList.getMessage());
//            return view;
//        }
        //将占用的签名子扩展号 转换成map方式
//        Map<String, String> signNumbers = new HashMap<>();
//        if (null != signNumberList.getData() && signNumberList.getData().size() > 0) {
//            for (String numbers : signNumberList.getData()) {
//                String[] numberArray = numbers.split(",");
//                for (String number : numberArray) {
//                    signNumbers.put(number, number);
//                }
//            }
//        }
        //向前台传值的时候，要去除已经使用的签名自扩展号
        List<String> signExtendNumbers = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            String number = "";
            if (i < 10) {
                number = "0"+i;
            } else {
                number = ""+i;
            }
            signExtendNumbers.add(number);
        }
        view.addObject("signExtendNumbers", signExtendNumbers);

        /**
         * 把本id所占用的签名子扩展号，放到前端
         */
        String thisExtendNumbers = responseData.getData().getExtendData();
        Map<String,String> thisExtendNumberMap = new HashMap<>();
        String[] thisExtendNumbersArray = thisExtendNumbers.split(",");
        for(String number:thisExtendNumbersArray){
            thisExtendNumberMap.put(number,number);
        }
        view.addObject("thisExtendNumberMap", thisExtendNumberMap);

        /**
         * 把本id所占用的服务类型，放到前端
         */
        Map<String,String> thisServiceTypeMap = new HashMap<>();
        String[] thisServiceTypeArray = responseData.getData().getServiceType().split(",");
        for(String serviceType:thisServiceTypeArray){
            thisServiceTypeMap.put(serviceType,serviceType);
        }
        view.addObject("thisServiceTypeMap", thisServiceTypeMap);

        return view;
    }

    /**
     * 保存
     *
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated AccountSignRegisterValidator accountSignRegisterValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("sign/register/sign_register_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("accountSignRegisterValidator", accountSignRegisterValidator);
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            accountSignRegisterValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
            accountSignRegisterValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            accountSignRegisterValidator.setUpdatedTime(new Date());
            accountSignRegisterValidator.setUpdatedBy(user.getRealName());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        accountSignRegisterValidator.setAppName(accountSignRegisterValidator.getSign());
        accountSignRegisterValidator.setMainApplication(accountSignRegisterValidator.getServiceType());

        //保存数据
        ResponseData data = accountSignRegisterService.save(accountSignRegisterValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("SIGN_REGISTER", accountSignRegisterValidator.getId(), "add".equals(op) ? accountSignRegisterValidator.getCreatedBy() : accountSignRegisterValidator.getUpdatedBy(), op, "add".equals(op) ? "添加企业开户" : "修改企业开户", JSON.toJSONString(accountSignRegisterValidator));
        }

        //记录日志
        log.info("[签名报备][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(accountSignRegisterValidator));

        view.setView(new RedirectView("/sign/register/list", true, false));
        return view;

    }

    /**
     * 注销
     *
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    public ModelAndView deleteById(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("sign/register/sign_register_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询企业信息
        ResponseData<AccountSignRegisterValidator> responseData = accountSignRegisterService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(responseData.getCode())) {
            view.addObject("error", responseData.getCode() + ":" + responseData.getMessage());
        }

        //注销、启用企业业务
        ResponseData data = accountSignRegisterService.deleteById(id);

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("SIGN_REGISTER", responseData.getData().getId(), user.getRealName(), "edit", "注销签名报备", JSON.toJSONString(responseData.getData()));
        }

        //记录日志
        log.info("[签名报备][delete][{}]数据:{}", user.getUserName(), JSON.toJSONString(responseData.getData()));

        view.setView(new RedirectView("/sign/register/list", true, false));
        return view;

    }
}
