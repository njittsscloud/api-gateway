package com.tss.apigateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.tss.basic.common.util.JsonUtil;
import com.tss.basic.site.response.ErrorDataResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.cloud.netflix.zuul.util.ZuulRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
public class AccessFilter extends ZuulFilter {
    private static final Logger LOG = LoggerFactory.getLogger(AccessFilter.class);

    private static final String KEY_ACCESS_TOKEN = "access_token";

    // 请求路由之前执行
    @Override
    public String filterType() {
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
            LOG.info("send {} request to {}", request.getMethod(), request.getRequestURL().toString());

            Cookie[] cookies = request.getCookies();
            if (cookies != null && cookies.length > 0) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(KEY_ACCESS_TOKEN) && StringUtils.isNotBlank(cookie.getValue())) {
                        return null;
                    }
                }
            }

            this.return401(ctx);
            return null;
        } catch (Exception e) {
            throw new ZuulRuntimeException(e);
        }
    }

    private void return401(RequestContext ctx) {
        LOG.warn("access token is empty");
        ctx.setSendZuulResponse(false);
        ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        ctx.setResponseBody(this.getResponseBody(HttpStatus.UNAUTHORIZED.value(), "user not login"));
        ctx.getResponse().setContentType("application/json");
    }

    private String getResponseBody(int code, String msg) {
        ErrorDataResponse response = new ErrorDataResponse();
        response.setMsg(msg);
        response.setSuccess(false);
        response.setErrorCode(code);
        return JsonUtil.obj2json(response);
    }
}
