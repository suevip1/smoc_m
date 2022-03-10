package com.smoc.cloud.material.service;

import com.smoc.cloud.admin.security.remote.service.FlowApproveService;
import com.smoc.cloud.common.auth.validator.FlowApproveValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseDocumentInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.SystemAttachmentValidator;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.material.remote.MessageSignFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;


/**
 * 资质管理服务
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MessageSignService {

    @Autowired
    private MessageSignFeignClient messageSignFeignClient;

    @Autowired
    private SystemAttachmentService systemAttachmentService;

    @Autowired
    private FlowApproveService flowApproveService;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<EnterpriseDocumentInfoValidator>> page(PageParams<EnterpriseDocumentInfoValidator> pageParams) {
        try {
            PageList<EnterpriseDocumentInfoValidator> pageList = this.messageSignFeignClient.page(pageParams);
            return ResponseDataUtil.buildSuccess(pageList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    public ResponseData<EnterpriseDocumentInfoValidator> findById(String id) {
        try {
            ResponseData<EnterpriseDocumentInfoValidator> data = this.messageSignFeignClient.findById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    public ResponseData save(EnterpriseDocumentInfoValidator enterpriseDocumentInfoValidator, List<MultipartFile> files,String enterpriseName, String op,String userId) {

        try {

            //获取上传的文件
            List<SystemAttachmentValidator> list = systemAttachmentService.saveFile(files,"EC_DOCUMENT",enterpriseDocumentInfoValidator.getId(),enterpriseName,enterpriseDocumentInfoValidator.getCreatedBy());
            enterpriseDocumentInfoValidator.setFilesList(list);
            ResponseData data = this.messageSignFeignClient.save(enterpriseDocumentInfoValidator, op);

            //组织审核信息
            if("add".equals(op)) {
                FlowApproveValidator flowApproveValidator = new FlowApproveValidator();
                flowApproveValidator.setId(UUID.uuid32());
                flowApproveValidator.setApproveId(enterpriseDocumentInfoValidator.getId());
                flowApproveValidator.setApproveType("SIGN_INFO");
                flowApproveValidator.setSubmitTime(new Date());
                flowApproveValidator.setBusiUrl(enterpriseDocumentInfoValidator.getCreatedBy());
                flowApproveValidator.setUserId(userId);
                flowApproveValidator.setApproveAdvice("新建");
                flowApproveValidator.setApproveStatus(4);
                flowApproveValidator.setFlowStatus(0);
                flowApproveValidator.setUserApproveId("");
                flowApproveService.saveFlowApprove(flowApproveValidator,"add");
            }
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id删除系统数据
     */
    public ResponseData deleteById(String id) {
        try {
            ResponseData data = this.messageSignFeignClient.deleteById(id);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 查询签名
     * @param enterpriseDocumentInfoValidator
     * @return
     */
    public ResponseData<List<EnterpriseDocumentInfoValidator>> findMessageSign(EnterpriseDocumentInfoValidator enterpriseDocumentInfoValidator) {
        try {
            ResponseData<List<EnterpriseDocumentInfoValidator>> data = this.messageSignFeignClient.findMessageSign(enterpriseDocumentInfoValidator);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
