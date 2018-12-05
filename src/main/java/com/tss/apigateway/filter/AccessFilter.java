package com.tss.apigateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.cloud.netflix.zuul.util.ZuulRuntimeException;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Component
public class AccessFilter extends ZuulFilter {
    private static final Logger LOG = LoggerFactory.getLogger(AccessFilter.class);

    private static final List<String> URI_PREFIX = Arrays.asList("api-a", "api-b", "api-c", "api-d");

    @Override
    public String filterType() {
        // 请求路由之前执行
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        try {
            HttpServletRequest request = ctx.getRequest();
            LOG.info("send {} request to {}, queryString={}", request.getMethod(), request.getRequestURL().toString(), request.getQueryString());

            String uri = request.getRequestURI();
            for (String prefix : URI_PREFIX) {
                if (uri.contains(prefix)) {
                    if (prefix.lastIndexOf("login") != -1) {
                        
                    } 
                }
            }
            
            System.out.println("uri：" + request.getRequestURI());

            String authorization = request.getHeader("Authorization");

            Cookie[] cookies = request.getCookies();
            if (cookies != null && cookies.length > 0) {

            }


//            Object accessToken = request.getParameter("accessToken");
//            if (accessToken == null) {
//                LOG.warn("access token is empty");
//                ctx.setSendZuulResponse(false);
//                ctx.setResponseStatusCode(401);
//                return null;
//            }
//            LOG.info("access token ok");
            return null;
        } catch (Exception e) {
            throw new ZuulRuntimeException(e);
        }
    }
}
