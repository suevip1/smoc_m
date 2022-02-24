package com.smoc.cloud.filter.repository;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.filter.FilterKeyWordsInfoValidator;
import com.smoc.cloud.filter.entity.FilterKeyWordsInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * 关键词管理
 */
public interface KeywordsRepository extends CrudRepository<FilterKeyWordsInfo, String>, JpaRepository<FilterKeyWordsInfo, String> {


    /**
     *  查询
     * @param pageParams
     * @return
     */
    PageList<FilterKeyWordsInfoValidator> page(PageParams<FilterKeyWordsInfoValidator> pageParams);

    /**
     * 批量保存
     * @param filterKeyWordsInfoValidator
     */
    void bathSave(FilterKeyWordsInfoValidator filterKeyWordsInfoValidator);

    /**
     * 查重
     * @param keyWordsBusinessType
     * @param businessId
     * @param keyWordsType
     * @param keyWords
     * @return
     */
    List<FilterKeyWordsInfo> findByKeyWordsBusinessTypeAndBusinessIdAndKeyWordsTypeAndKeyWords(String keyWordsBusinessType, String businessId, String keyWordsType, String keyWords);

    /**
     * 关键字导入
     * @param filterKeyWordsInfoValidator
     */
    void expBatchSave(FilterKeyWordsInfoValidator filterKeyWordsInfoValidator);
}