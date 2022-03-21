package com.smoc.cloud.configure.advance.controller;

import com.google.gson.Gson;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelPriceValidator;
import com.smoc.cloud.common.smoc.configuate.validator.SystemHistoryPriceChangeRecordValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.configure.advance.service.SystemHistoryPriceChangeRecordService;
import com.smoc.cloud.configure.channel.service.ChannelPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;


@Slf4j
@Controller
@RequestMapping("/configure/price/history")
public class SystemHistoryPriceChangeRecordController {

    @Autowired
    private ChannelPriceService channelPriceService;

    @Autowired
    private SystemHistoryPriceChangeRecordService systemHistoryPriceChangeRecordService;

    /**
     * 列表
     *
     * @return
     */
    @RequestMapping(value = "/list/{changeType}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String changeType) {
        ModelAndView view = new ModelAndView("configure/advance/history_price_change_list");

        ///初始化数据
        PageParams<SystemHistoryPriceChangeRecordValidator> params = new PageParams<>();
        params.setPageSize(8);
        params.setCurrentPage(1);
        SystemHistoryPriceChangeRecordValidator systemHistoryPriceChangeRecordValidator = new SystemHistoryPriceChangeRecordValidator();
        systemHistoryPriceChangeRecordValidator.setChangeType(changeType);
        params.setParams(systemHistoryPriceChangeRecordValidator);

        //查询
        ResponseData<PageList<SystemHistoryPriceChangeRecordValidator>> data = systemHistoryPriceChangeRecordService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("systemHistoryPriceChangeRecordValidator", systemHistoryPriceChangeRecordValidator);
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
    public ModelAndView page(@ModelAttribute SystemHistoryPriceChangeRecordValidator systemHistoryPriceChangeRecordValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("configure/advance/history_price_change_list");

        //分页查询
        pageParams.setParams(systemHistoryPriceChangeRecordValidator);

        ResponseData<PageList<SystemHistoryPriceChangeRecordValidator>> data = systemHistoryPriceChangeRecordService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("systemHistoryPriceChangeRecordValidator", systemHistoryPriceChangeRecordValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;
    }

    /**
     * 创建 新历史价格变更记录
     *
     * @return
     */
    @RequestMapping(value = "/center/{changeType}/{businessId}", method = RequestMethod.GET)
    public ModelAndView center(@PathVariable String changeType, @PathVariable String businessId) {
        ModelAndView view = new ModelAndView("configure/advance/history_price_change_center");

        view.addObject("changeType", changeType);
        view.addObject("businessId", businessId);
        return view;
    }

    /**
     * 创建 新历史价格变更记录
     *
     * @return
     */
    @RequestMapping(value = "/edit/{changeType}/save", method = RequestMethod.POST)
    public ModelAndView save(@PathVariable String changeType, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("configure/advance/history_price_change_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        String businessId = request.getParameter("businessId");
        view.addObject("changeType", changeType);
        view.addObject("businessId", businessId);
        String priceIds = request.getParameter("priceIds");
        if (StringUtils.isEmpty(priceIds)) {
            view.setView(new RedirectView("/configure/price/history/add/" + changeType + "/" + businessId + "", true, false));
            return view;
        }

        String accountIds = request.getParameter("accountIds");
        log.info("[accountIds]:{}",accountIds);
        if (StringUtils.isEmpty(accountIds)) {
            view.setView(new RedirectView("/configure/price/history/add/" + changeType + "/" + businessId + "", true, false));
            return view;
        }

        Map<String,Boolean> accountMap = new HashMap<>();
        String[] accounts = accountIds.split(",");
        for(int i=0;i<accounts.length;i++){
            accountMap.put(accounts[i],true);
        }


        String[] ids = priceIds.split(",");
        List<SystemHistoryPriceChangeRecordValidator> recordList = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            SystemHistoryPriceChangeRecordValidator validator = new SystemHistoryPriceChangeRecordValidator();
            validator.setId(ids[i]);
            validator.setChangeType(changeType);
            validator.setBusinessId(businessId);
            validator.setChangePrice(new BigDecimal(request.getParameter(ids[i] + "_price")));
            validator.setPriceArea(request.getParameter(ids[i] + "_areaCode"));
            validator.setStartDate(request.getParameter(ids[i] + "_date"));
            validator.setCreatedBy(user.getRealName());
            if(null != accountMap.get(ids[i]) && accountMap.get(ids[i])) {
                recordList.add(validator);
            }
        }

        ResponseData data = systemHistoryPriceChangeRecordService.save(recordList, changeType);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }
        //log.info("[recordList]:{}", new Gson().toJson(recordList));
        view.setView(new RedirectView("/configure/price/history/add/" + changeType + "/" + businessId + "", true, false));
        return view;
    }

    /**
     * 创建 新历史价格变更记录
     *
     * @return
     */
    @RequestMapping(value = "/add/{changeType}/{businessId}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String changeType, @PathVariable String businessId) {
        ModelAndView view = new ModelAndView("configure/advance/history_price_change_edit");


        //根据通道id查询区域价格
        ChannelPriceValidator channelPriceValidator = new ChannelPriceValidator();
        channelPriceValidator.setChannelId(businessId);
        ResponseData<List<ChannelPriceValidator>> listData = channelPriceService.findChannelPrice(channelPriceValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(listData.getCode())) {
            view.addObject("error", listData.getCode() + ":" + listData.getMessage());
            return view;
        }

        //log.info("[数据]：{}",new Gson().toJson(listData.getData()));
        String priceIds = "";
        if (null != listData.getData() && listData.getData().size() > 0) {
            for (ChannelPriceValidator obj : listData.getData()) {
                if ("".equals(priceIds)) {
                    priceIds = obj.getId();
                } else {
                    priceIds += "," + obj.getId();
                }
            }
        }

        String endDate = DateTimeUtils.getDateFormat(new Date());
        String startDate = DateTimeUtils.checkOption(endDate, -31);


        view.addObject("priceIds", priceIds);
        view.addObject("startDate", startDate);
        view.addObject("endDate", endDate);
        view.addObject("channelPriceList", listData.getData());

        view.addObject("changeType", changeType);
        view.addObject("businessId", businessId);
        return view;
    }
}
