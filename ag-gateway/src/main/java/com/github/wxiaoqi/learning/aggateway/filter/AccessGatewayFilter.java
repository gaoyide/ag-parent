package com.github.wxiaoqi.learning.aggateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.learning.aggateway.jwt.JWTUtil;
import com.github.wxiaoqi.learning.common.context.BaseContextHandler;
import com.github.wxiaoqi.learning.common.msg.BaseResponse;
import com.github.wxiaoqi.learning.common.msg.auth.TokenForbiddenResponse;
import com.github.wxiaoqi.learning.common.util.jwt.IJWTInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.jetbrains.annotations.NotNull;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

@Slf4j
@Configuration
public class AccessGatewayFilter implements GlobalFilter {

    private static final String GATE_WAY_PREFIX = "/api";

    //放行(ignore)的url
    @Value("${gate.ignore.startWith}")
    private String startWith;

    //token的key
    @Value("${jwt.token-header}")
    private String tokenHeader;

    @Autowired
    JWTUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, GatewayFilterChain gatewayFilterChain) {

        log.info("check token and user permission....");

        /**
         * GATEWAY_ORIGINAL_REQUEST_URL_ATTR  记录请求url变更的历史
         * 例如：我们访问 http://localhost:8765/api/generator/user/hello/shuaigege
         * requiredAttribute有2个长度，
         * 值分别是：http://localhost:8765/api/generator/user/hello/shuaigege
         *          lb://ag-mybatis-generator/user/hello/shuaigege
         *
         */
        LinkedHashSet requiredAttribute  = serverWebExchange.getRequiredAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        ServerHttpRequest request = serverWebExchange.getRequest();
        String requestUri = request.getPath().pathWithinApplication().value();
        log.info("服务的真实路径： "+requestUri);
        if (requiredAttribute != null) {
            Iterator<URI> iterator = requiredAttribute.iterator();
            while (iterator.hasNext()){
                URI next = iterator.next();
                if(next.getPath().startsWith(GATE_WAY_PREFIX)){
                    requestUri = next.getPath().substring(GATE_WAY_PREFIX.length());
                }
            }
        }
        /**
         * 过滤完后
         * requestUri:
         *
         * http://localhost:8765/api/generator/user/hello/gaoyide
         *  /generator/user/hello/gaoyide
         *
         * http://localhost:8765/api/auth/jwt/token
         *  /auth/jwt/token
         */
        log.info("加上网关，去掉api的路径: "+requestUri);

        final String method = request.getMethod().toString();
        BaseContextHandler.setToken(null);
        ServerHttpRequest.Builder mutate = request.mutate();

        // 不进行拦截的地址
        if (isStartWith(requestUri)) {
            ServerHttpRequest build = mutate.build();
            return gatewayFilterChain.filter(serverWebExchange.mutate().request(build).build());
        }

        //获取token，并转成用户信息
        IJWTInfo user = null;
        try {
            user = getJWTUser(request, mutate);
            log.info("name: "+user.getName());
        } catch (Exception e) {
            log.error("用户Token过期异常", e);
            return getVoidMono(serverWebExchange, new TokenForbiddenResponse("User Token Forbidden or Expired!"));
        }


//        return gatewayFilterChain.filter(serverWebExchange);
        ServerHttpRequest build = mutate.build();
        return gatewayFilterChain.filter(serverWebExchange.mutate().request(build).build());
    }




    /**
     * URI是否以什么打头
     *
     * @param requestUri
     * @return
     */
    private boolean isStartWith(String requestUri) {
        boolean flag = false;
        for (String s : startWith.split(",")) {
            if (requestUri.startsWith(s)) {
                return true;
            }
        }
        return flag;
    }


    /**
     * 返回session中的用户信息
     *
     * @param request
     * @param ctx
     * @return
     */
    private IJWTInfo getJWTUser(ServerHttpRequest request, ServerHttpRequest.Builder ctx) throws Exception {
        List<String> strings = request.getHeaders().get(tokenHeader);
        String authToken = null;
        if (strings != null) {
            authToken = strings.get(0);
        }
        if (StringUtils.isBlank(authToken)) {
            strings = request.getQueryParams().get("token");
            if (strings != null) {
                authToken = strings.get(0);
            }
        }
        ctx.header(tokenHeader, authToken);
        BaseContextHandler.setToken(authToken);
        //进行token解密，如果token被串改，解密失败，会抛异常
        return jwtUtil.getInfoFromToken(authToken);
    }


    /**
     * 网关抛异常
     *
     * @param body
     */
    @NotNull
    private Mono<Void> getVoidMono(ServerWebExchange serverWebExchange, BaseResponse body) {
        serverWebExchange.getResponse().setStatusCode(HttpStatus.OK);
        byte[] bytes = JSONObject.toJSONString(body).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = serverWebExchange.getResponse().bufferFactory().wrap(bytes);
        return serverWebExchange.getResponse().writeWith(Flux.just(buffer));
    }
}
