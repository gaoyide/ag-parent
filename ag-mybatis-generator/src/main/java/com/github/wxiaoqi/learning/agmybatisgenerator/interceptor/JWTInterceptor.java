package com.github.wxiaoqi.learning.agmybatisgenerator.interceptor;

import com.github.wxiaoqi.learning.agmybatisgenerator.jwt.JWTUtil;
import com.github.wxiaoqi.learning.common.context.BaseContextHandler;
import com.github.wxiaoqi.learning.common.util.jwt.IJWTInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 加上本拦截器，获取token的信息
 * 注意：必须在InterceptorConfig类(继承WebMvcConfigurerAdapter)中,addInterceptors才能生效
 * 本例子中，如果未带token直接访问:http://localhost:8001/user/hello/shuaigege，回报如下(走GlobalExceptionHandler)：
 * {
     "status": 500,
     "message": "JWT String argument cannot be null or empty."
   }
 *
 */

@Slf4j
@Component
public class JWTInterceptor extends HandlerInterceptorAdapter {

    @Value("${jwt.token-header}")
    private String tokenHeader;

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("jwt interceptor...");
        String access_token = request.getHeader(tokenHeader);
        IJWTInfo infoFromToken = jwtUtil.getInfoFromToken(access_token);
        BaseContextHandler.setUsername(infoFromToken.getUniqueName());
        BaseContextHandler.setName(infoFromToken.getName());
        BaseContextHandler.setUserID(infoFromToken.getId());
        log.info("uniqueName: "+infoFromToken.getUniqueName()+" name: "+infoFromToken.getName()+" id: "+infoFromToken.getId());
        return super.preHandle(request, response, handler);
    }
}
