package com.smoc.examples.filters.example;

import com.google.gson.Gson;
import com.smoc.examples.utils.DateTimeUtils;
import com.smoc.examples.utils.Okhttp3Utils;
import com.smoc.examples.utils.Utils;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 短信验证过滤 示例
 */
public class FilterExample {

    public static void main(String[] args) throws Exception {
        System.out.println(System.currentTimeMillis());
        for (int i = 0; i < 10; i++) {

            //请求路径（具体参见技术文档）
            String url = "http://localhost:18090/smoc-filters/full-filter/filters";

            //自定义header协议
            Map<String, String> header = new HashMap<>();
            //
            Map<String, Object> data = new HashMap<>();
            data.put("phone", "13910816771");
            data.put("account", "YQT124");
            data.put("carrier", "CMCC");
            String message = "事情不大，还是要处理，奖,禁止1bp2p贷款的";
            data.put("message", message);
            data.put("provinceCode", "11");
            data.put("numbers", 1);

            //转JSON请求数据
            String requestJsonData = new Gson().toJson(data);
            //System.out.println(requestJsonData);
            String result = Okhttp3Utils.postJson(url, requestJsonData, header);
            //Thread.currentThread().sleep(1000);
            System.out.println(System.currentTimeMillis()+"[请求响应]数据:" + result);
        }
        System.out.println(System.currentTimeMillis());
    }
}
