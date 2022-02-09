package com.smoc.cloud.finance.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountValidator;
import com.smoc.cloud.customer.service.EnterpriseService;
import com.smoc.cloud.finance.service.FinanceAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 财务共享账户
 */
@Slf4j
@Controller
@RequestMapping("/finance")
public class FinanceShareAccountController {

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private FinanceAccountService financeAccountService;

    /**
     * 财务共享账户列表
     *
     * @return
     */
    @RequestMapping(value = "/account/share/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("finance/finance_account_share_list");

        //初始化数据
        PageParams<FinanceAccountValidator> params = new PageParams<FinanceAccountValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        FinanceAccountValidator financeAccountValidator = new FinanceAccountValidator();
        params.setParams(financeAccountValidator);

        //查询
        ResponseData<PageList<FinanceAccountValidator>> data = financeAccountService.page(params, "3");
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        ResponseData<Map<String, Object>> count = financeAccountService.count("3");

        view.addObject("financeAccountValidator", financeAccountValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("counter", count.getData());
        return view;
    }

    /**
     * 财务共享账户分页
     *
     * @return
     */
    @RequestMapping(value = "/account/share/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute FinanceAccountValidator financeAccountValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("finance/finance_account_share_list");

        //分页查询
        pageParams.setParams(financeAccountValidator);

        ResponseData<PageList<FinanceAccountValidator>> data = financeAccountService.page(pageParams, "3");
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        ResponseData<Map<String, Object>> count = financeAccountService.count("3");

        view.addObject("financeAccountValidator", financeAccountValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("counter", count.getData());

        return view;
    }

    /**
     * 财务共享账户编辑
     *
     * @return
     */
    @RequestMapping(value = "/account/share/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("finance/finance_account_share_edit");

        return view;
    }

    /**
     * 财务共享账户查看中心
     *
     * @return
     */
    @RequestMapping(value = "/account/share/view/center/*", method = RequestMethod.GET)
    public ModelAndView center() {
        ModelAndView view = new ModelAndView("finance/finance_account_share_view_center");

        return view;
    }

    /**
     * 财务共享账户基本信息明细
     *
     * @return
     */
    @RequestMapping(value = "/account/share/view/base/{id}", method = RequestMethod.GET)
    public ModelAndView view_base(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("finance/finance_account_share_view_base");

        return view;
    }

    /**
     * 财务共享账户充值列表
     *
     * @return
     */
    @RequestMapping(value = "/account/share/view/recharge/{id}", method = RequestMethod.GET)
    public ModelAndView view_recharge(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("finance/finance_account_share_view_recharge_list");
        //查询数据
        PageParams params = new PageParams<>();
        params.setPages(3);
        params.setPageSize(10);
        params.setStartRow(1);
        params.setEndRow(10);
        params.setCurrentPage(1);
        params.setTotalRows(22);

        view.addObject("pageParams", params);
        return view;
    }

    /**
     * 财务共享账户消费列表
     *
     * @return
     */
    @RequestMapping(value = "/account/share/view/consume/{id}", method = RequestMethod.GET)
    public ModelAndView view_consume(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("finance/finance_account_share_view_consume_list");
        //查询数据
        PageParams params = new PageParams<>();
        params.setPages(3);
        params.setPageSize(10);
        params.setStartRow(1);
        params.setEndRow(10);
        params.setCurrentPage(1);
        params.setTotalRows(22);

        view.addObject("pageParams", params);
        return view;
    }


}
