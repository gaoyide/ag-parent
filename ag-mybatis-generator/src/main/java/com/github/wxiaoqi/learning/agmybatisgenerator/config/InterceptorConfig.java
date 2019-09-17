package com.github.wxiaoqi.learning.agmybatisgenerator.config;

import com.github.wxiaoqi.learning.agmybatisgenerator.interceptor.JWTInterceptor;
import com.github.wxiaoqi.learning.agmybatisgenerator.interceptor.ServiceAuthRestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 让JWTInterceptor生效
 * 注意：
 *      /** 表示对所有的路径进行拦截，但有些请求是跟用户状态无关的
 * 处理方法：
 *     利用正则表达式对url进行匹配，从而更细粒度的进行拦截，例如：/user/**
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        //先校验服务的合法性，再校验用户
        registry.addInterceptor(getServiceAuthRestInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(getJWTInterceptor()).addPathPatterns("/**");
    }

    /**
     * 获取无状态信息的
     * @return
     */
    @Bean
    JWTInterceptor getJWTInterceptor() {
        return new JWTInterceptor();
    }

    @Bean
    ServiceAuthRestInterceptor getServiceAuthRestInterceptor(){
        return new ServiceAuthRestInterceptor();
    };
}
