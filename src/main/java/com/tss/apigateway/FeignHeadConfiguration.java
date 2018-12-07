package com.tss.apigateway;

import feign.RequestInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: MQG
 * @date: 2018/12/7
 */
public class FeignHeadConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(FeignHeadConfiguration.class);
    
    private static final String HEAD_AUTHORIZATION = "Authorization";
    
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest request = attrs.getRequest();
                String authorization = request.getHeader(HEAD_AUTHORIZATION);
                if (StringUtils.isNotBlank(authorization)) {
                    LOG.info("添加请求头key={}, value={}", HEAD_AUTHORIZATION, authorization);
                    requestTemplate.header(HEAD_AUTHORIZATION, authorization);
                }
            }
        };
    }
}
