package com.smoc.cloud.message.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.message.MessageDetailInfoValidator;
import com.smoc.cloud.common.smoc.message.TableStoreMessageDetailInfoValidator;
import com.smoc.cloud.common.smoc.message.model.MessageTaskDetail;
import com.smoc.cloud.message.repository.MessageDetailInfoRepository;
import com.smoc.cloud.parameter.errorcode.service.SystemErrorCodeService;
import com.smoc.cloud.tablestore.repository.TableStoreMessageDetailInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 短信明细
 */
@Slf4j
@Service
public class MessageDetailInfoService {

    @Resource
    private MessageDetailInfoRepository messageDetailInfoRepository;

    @Resource
    private SystemErrorCodeService systemErrorCodeService;

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<MessageDetailInfoValidator>> page(PageParams<MessageDetailInfoValidator> pageParams){

        PageList<MessageDetailInfoValidator> page = messageDetailInfoRepository.page(pageParams);

        //匹配错误码描述
        List<MessageDetailInfoValidator> list = page.getList();
        if(!StringUtils.isEmpty(list) && list.size()>0){
            for(MessageDetailInfoValidator info :list){
                if(!StringUtils.isEmpty(info.getCustomerStatus())){
                    String remark = systemErrorCodeService.findErrorRemark(info.getCustomerStatus(),info.getCarrier());
                    if(!StringUtils.isEmpty(remark)){
                        info.setCustomerStatus(info.getCustomerStatus()+"("+remark+")");
                    }
                }
            }
        }

        return ResponseDataUtil.buildSuccess(page);

    }

    /**
     * 统计自服务平台短信明细列表
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<MessageDetailInfoValidator>> servicerPage(PageParams<MessageDetailInfoValidator> pageParams) {
        PageList<MessageDetailInfoValidator> page = messageDetailInfoRepository.servicerPage(pageParams);

        return ResponseDataUtil.buildSuccess(page);
    }

    /**
     * 查询自服务web短信明细列表
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<MessageTaskDetail>> webTaskDetailList(PageParams<MessageTaskDetail> pageParams) {
        PageList<MessageTaskDetail> page = messageDetailInfoRepository.webTaskDetailList(pageParams);
        return ResponseDataUtil.buildSuccess(page);
    }

    /**
     * 查询自服务http短信明细列表
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<MessageTaskDetail>> httpTaskDetailList(PageParams<MessageTaskDetail> pageParams) {
        PageList<MessageTaskDetail> page = messageDetailInfoRepository.httpTaskDetailList(pageParams);
        return ResponseDataUtil.buildSuccess(page);
    }

    /**
     * 单条短信发送记录
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<MessageDetailInfoValidator>> sendMessageList(PageParams<MessageDetailInfoValidator> pageParams) {
        PageList<MessageDetailInfoValidator> page = messageDetailInfoRepository.sendMessageList(pageParams);

        //匹配错误码描述
        List<MessageDetailInfoValidator> list = page.getList();
        if(!StringUtils.isEmpty(list) && list.size()>0){
            for(MessageDetailInfoValidator info :list){
                if(!StringUtils.isEmpty(info.getCustomerStatus())){
                    String remark = systemErrorCodeService.findErrorRemark(info.getCustomerStatus(),info.getCarrier());
                    if(!StringUtils.isEmpty(remark)){
                        info.setCustomerStatus(info.getCustomerStatus()+"("+remark+")");
                    }
                }
            }
        }

        return ResponseDataUtil.buildSuccess(page);
    }

}
