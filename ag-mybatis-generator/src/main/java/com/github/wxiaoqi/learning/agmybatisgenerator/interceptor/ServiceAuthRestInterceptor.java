package com.github.wxiaoqi.learning.agmybatisgenerator.interceptor;

import com.github.wxiaoqi.learning.agmybatisgenerator.client.ClientTokenUtil;
import com.github.wxiaoqi.learning.agmybatisgenerator.client.ServiceAuthUtil;
import com.github.wxiaoqi.learning.common.exception.auth.ClientForbiddenException;
import com.github.wxiaoqi.learning.common.util.jwt.IJWTInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@Component
public class ServiceAuthRestInterceptor extends HandlerInterceptorAdapter {

    @Value("${client.token-header}")
    private String tokenHeader;

    @Autowired
    private ClientTokenUtil clientTokenUtil;

    @Autowired
    private ServiceAuthUtil serviceAuthUtil;

    private List<String> allowedClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("client interceptor...");
        //tokenHeader是上游服务传递过来的
        String token = request.getHeader(tokenHeader);
        //1、验证上游服务的合法性，解析token
        IJWTInfo infoFromToken = clientTokenUtil.getInfoFromToken(token);
        String uniqueName = infoFromToken.getUniqueName(); // clientName（code）

        //2、判断客户端是否被授权
        // 获取授权的客户端列表
        //优化：定时拉取可授权的客户端列表
        allowedClient = serviceAuthUtil.getAllowedClient();
        for(String client : allowedClient){
            if(client.equals(uniqueName)){
                return super.preHandle(request, response, handler);
            }
        }
        throw new ClientForbiddenException("Client is Forbidden!");
    }
}

