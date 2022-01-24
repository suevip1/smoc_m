package com.smoc.cloud.identification.controller;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.CodeNumberInfoValidator;
import com.smoc.cloud.common.smoc.identification.validator.IdentificationAccountInfoValidator;
import com.smoc.cloud.identification.service.IdentificationAccountInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 认账账号管理
 */
@Slf4j
@Controller
@RequestMapping("/identification/account")
public class IdentificationAccountController {

    @Autowired
    private IdentificationAccountInfoService identificationAccountInfoService;

    /**
     * 认账账号管理列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("identification/account/identification_account_list");

        //初始化数据
        PageParams<IdentificationAccountInfoValidator> params = new PageParams<IdentificationAccountInfoValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        IdentificationAccountInfoValidator identificationAccountInfoValidator = new IdentificationAccountInfoValidator();
        params.setParams(identificationAccountInfoValidator);

        //查询
        ResponseData<PageList<IdentificationAccountInfoValidator>> data = identificationAccountInfoService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("identificationAccountInfoValidator", identificationAccountInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;

    }

    /**
     * 认账账号管理列表分页
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute IdentificationAccountInfoValidator identificationAccountInfoValidator, PageParams pageParams) {

        ModelAndView view = new ModelAndView("identification/account/identification_account_list");
        //分页查询
        pageParams.setParams(identificationAccountInfoValidator);

        ResponseData<PageList<CodeNumberInfoValidator>> data = identificationAccountInfoService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("identificationAccountInfoValidator", identificationAccountInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;
    }

    /**
     * 认账账号开通
     *
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ModelAndView add() {
        ModelAndView view = new ModelAndView("identification/account/identification_account_edit");

        return view;

    }

    /**
     * 认账账号修改
     *
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit() {
        ModelAndView view = new ModelAndView("identification/account/identification_account_edit");

        return view;

    }

    /**
     * 认账账号产看
     *
     * @return
     */
    @RequestMapping(value = "/view/center/{id}", method = RequestMethod.GET)
    public ModelAndView center(@PathVariable String accountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("identification/account/identification_account_view_center");

        return view;
    }


    /**
     * 认账账号产看
     *
     * @return
     */
    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public ModelAndView view(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("identification/account/identification_account_view");

        return view;

    }

}
