package com.smoc.cloud.message.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.message.MessageMoInfoValidator;
import com.smoc.cloud.message.rowmapper.MessageMoInfoRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MessageMoInfoRepositoryImpl extends BasePageRepository {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public  PageList<MessageMoInfoValidator> page(PageParams<MessageMoInfoValidator> pageParams) {

        //查询条件
        MessageMoInfoValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");

        sqlBuffer.append(" t.ID,");
        sqlBuffer.append(" a.ACCOUNT_NAME,");
        sqlBuffer.append(" t.MOBILE,");
        sqlBuffer.append(" t.MESSAGE_CONTENT,");
        sqlBuffer.append(" DATE_FORMAT(t.MO_DATE, '%Y-%m-%d %H:%i:%S')MO_DATE ");
        sqlBuffer.append(" from message_mo_info t left join account_base_info a on t.ACCOUNT_ID = a.ACCOUNT_ID");
        sqlBuffer.append(" where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getEnterpriseId())) {
            sqlBuffer.append(" and t.ENTERPRISE_ID = ? ");
            paramsList.add(qo.getEnterpriseId().trim() );
        }

        if (!StringUtils.isEmpty(qo.getAccountName())) {
            sqlBuffer.append(" and t.ACCOUNT_NAME like ? ");
            paramsList.add("%" + qo.getAccountName().trim()+ "%" );
        }

        if (!StringUtils.isEmpty(qo.getMobile())) {
            sqlBuffer.append(" and t.MOBILE like ? ");
            paramsList.add("%" + qo.getMobile().trim()+ "%" );
        }

        if (!StringUtils.isEmpty(qo.getMessageContent())) {
            sqlBuffer.append(" and t.MESSAGE_CONTENT like ? ");
            paramsList.add("%" + qo.getMessageContent().trim() + "%");
        }

        //时间起
        if (!StringUtils.isEmpty(qo.getStartDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.MO_DATE,'%Y-%m-%d') >=? ");
            paramsList.add(qo.getStartDate().trim());
        }
        //时间止
        if (!StringUtils.isEmpty(qo.getEndDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.MO_DATE,'%Y-%m-%d') <=? ");
            paramsList.add(qo.getEndDate().trim());
        }

        sqlBuffer.append(" order by t.MO_DATE desc");
        //log.info("[SQL]:{}",sqlBuffer);
        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);
        PageList<MessageMoInfoValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new MessageMoInfoRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }

}
