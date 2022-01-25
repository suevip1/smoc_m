package com.smoc.cloud.identification.controller;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.customer.service.EnterpriseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/identification/enterprise")
public class EnterpriseSearchController {

    @Autowired
    private EnterpriseService enterpriseService;

    /**
     * 认证开户企业检索
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ModelAndView search() {
        ModelAndView view = new ModelAndView("identification/account/identification_enterprise_search");

        //初始化数据
        PageParams<EnterpriseBasicInfoValidator> params = new PageParams<EnterpriseBasicInfoValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        EnterpriseBasicInfoValidator enterpriseBasicInfoValidator = new EnterpriseBasicInfoValidator();
        enterpriseBasicInfoValidator.setEnterpriseType("IDENTIFICATION");
        params.setParams(enterpriseBasicInfoValidator);

        //查询
        ResponseData<PageList<EnterpriseBasicInfoValidator>> data = enterpriseService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("enterpriseBasicInfoValidator", enterpriseBasicInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;
    }

    /**
     * 认证开户企业检索
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute EnterpriseBasicInfoValidator enterpriseBasicInfoValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("identification/account/identification_enterprise_search");

        //分页查询
        pageParams.setParams(enterpriseBasicInfoValidator);

        ResponseData<PageList<EnterpriseBasicInfoValidator>> data = enterpriseService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("enterpriseBasicInfoValidator", enterpriseBasicInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;
    }
}
