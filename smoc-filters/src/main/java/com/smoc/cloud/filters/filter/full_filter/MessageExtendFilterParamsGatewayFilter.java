package com.smoc.cloud.filters.filter.full_filter;

import com.google.gson.Gson;
import com.smoc.cloud.common.filters.utils.RedisConstant;
import com.smoc.cloud.filters.filter.BaseGatewayFilter;
import com.smoc.cloud.filters.request.model.RequestFullParams;
import com.smoc.cloud.filters.service.FiltersRedisDataService;
import com.smoc.cloud.filters.service.FiltersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;


/**
 * MESSAGE_高级扩展参数过滤；
 * 这些过滤参数，可以根据参数规则进行配置，可以扩展；具体功能参照文档
 */
@Slf4j
@Component
public class MessageExtendFilterParamsGatewayFilter extends BaseGatewayFilter implements Ordered, GatewayFilter {


    @Autowired
    private FiltersService filtersService;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //获取body内容
        String requestBody = exchange.getAttribute("cachedRequestBodyObject");

        //校验数据请求的数据结构
        RequestFullParams model = new Gson().fromJson(requestBody, RequestFullParams.class);
        if (StringUtils.isEmpty(model.getAccount()) || StringUtils.isEmpty(model.getMessage())) {
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                //被执行后调用 post
            }));
        }

        /**
         * 查询业务账号配置的MESSAGE_BLACK_级别配置参数
         */
        Object blackPatten = filtersService.get(RedisConstant.FILTERS_CONFIG_ACCOUNT_MESSAGE + "black:"+ model.getAccount() );
        if (null != blackPatten && !StringUtils.isEmpty(blackPatten.toString())) {
            log.info("[内容_黑_过滤参数]{}:{}", model.getAccount(), new Gson().toJson(blackPatten));
        }

        /**
         * 查询业务账号配置的MESSAGE_WHITE_级别配置参数
         */
        Object whitePatten = filtersService.get(RedisConstant.FILTERS_CONFIG_ACCOUNT_MESSAGE+ "white:" + model.getAccount() );
        if (null != whitePatten && !StringUtils.isEmpty(whitePatten.toString())) {
            log.info("[内容_白_过滤参数]{}:{}", model.getAccount(), new Gson().toJson(whitePatten));
        }

        /**
         * 查询业务账号配置的MESSAGE_REGULAR_级别配置参数
         */
        Object regularPatten = filtersService.get(RedisConstant.FILTERS_CONFIG_ACCOUNT_MESSAGE+ "regular:" + model.getAccount() );
        if (null != regularPatten && !StringUtils.isEmpty(regularPatten.toString())) {
            log.info("[内容_正则_过滤参数]{}:{}", model.getAccount(), new Gson().toJson(regularPatten));
        }


        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            //被执行后调用 post
        }));
    }

    @Override
    public int getOrder() {
        return 40;
    }


}