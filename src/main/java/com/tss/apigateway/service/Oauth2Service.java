package com.tss.apigateway.service;

import com.tss.apigateway.FeignHeadConfiguration;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author: MQG
 * @date: 2018/12/6
 */
@FeignClient(value = "${auth-server.serviceId}", configuration = FeignHeadConfiguration.class)
public interface Oauth2Service {

    /**
     * 获取access_token<br>
     * 这是spring-security-oauth2底层的接口，类TokenEndpoint<br>
     *
     * @param parameters
     * @return
     * @see org.springframework.security.oauth2.provider.endpoint.TokenEndpoint
     */
    @RequestMapping(value = "/oauth/token", method = RequestMethod.POST)
    Map<String, Object> postAccessToken(@RequestParam Map<String, String> parameters);

    /**
     * 删除access_token和refresh_token<br>
     * 认证中心的OAuth2Controller方法removeToken
     *
     * @param access_token
     */
    @PostMapping(path = "/oauth2/remove_token")
    void removeToken(@RequestParam("access_token") String access_token);
}
