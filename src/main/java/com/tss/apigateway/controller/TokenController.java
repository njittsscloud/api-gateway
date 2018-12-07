package com.tss.apigateway.controller;

import com.tss.apigateway.controller.vo.UserLoginReqVO;
import com.tss.apigateway.properties.Oauth2Properties;
import com.tss.apigateway.service.Oauth2Service;
import com.tss.basic.site.argumentresolver.JsonParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 登陆、刷新token、退出
 *
 * @author MQG
 * @date 2018/12/06
 */
@RestController
public class TokenController {
    private static final Logger LOG = LoggerFactory.getLogger(TokenController.class);

    @Autowired
    private Oauth2Service oauth2Service;
    @Autowired
    private Oauth2Properties oauth2Properties;

    /**
     * 系统登陆<br>
     * 根据用户名登录<br>
     * 采用oauth2密码模式获取access_token和refresh_token
     *
     * @param param
     * @return
     */
    @PostMapping("/sys/login")
    public Map<String, Object> login(HttpServletRequest request, @JsonParam(validation = true) UserLoginReqVO param) {
        String authorization = request.getHeader("Authorization");

        Map<String, String> parameters = new HashMap<>();
        parameters.put(OAuth2Utils.GRANT_TYPE, "password");
        parameters.put(OAuth2Utils.CLIENT_ID, oauth2Properties.getClientId());
        parameters.put("client_secret", oauth2Properties.getClientSecret());
        parameters.put(OAuth2Utils.SCOPE, oauth2Properties.getScope());
        parameters.put("username", param.getUserAcc() + "|" + param.getType());
        parameters.put("password", param.getPassword());

        Map<String, Object> tokenInfo = oauth2Service.postAccessToken(parameters);
        return tokenInfo;
    }


    /**
     * 系统刷新refresh_token
     *
     * @param refresh_token
     * @return
     */
    @PostMapping("/sys/refresh_token")
    public Map<String, Object> refresh_token(@RequestParam("refresh_token") String refresh_token) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OAuth2Utils.GRANT_TYPE, "refresh_token");
        parameters.put(OAuth2Utils.CLIENT_ID, oauth2Properties.getClientId());
        parameters.put("client_secret", oauth2Properties.getClientSecret());
        parameters.put(OAuth2Utils.SCOPE, oauth2Properties.getScope());
        parameters.put("refresh_token", refresh_token);

        return oauth2Service.postAccessToken(parameters);
    }

    /**
     * 退出
     *
     * @param access_token
     */
    @GetMapping("/sys/logout")
    public void logout(@RequestParam("access_token") String access_token,
                       @RequestHeader(required = false, value = "Authorization") String token) {
        if (StringUtils.isBlank(access_token)) {
            if (StringUtils.isNotBlank(token)) {
                access_token = token.substring(OAuth2AccessToken.BEARER_TYPE.length() + 1);
            }
        }
        oauth2Service.removeToken(access_token);
    }
}
