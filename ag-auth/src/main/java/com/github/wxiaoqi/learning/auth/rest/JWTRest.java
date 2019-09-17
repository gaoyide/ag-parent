package com.github.wxiaoqi.learning.auth.rest;

import com.github.wxiaoqi.learning.auth.bean.UserInfo;
import com.github.wxiaoqi.learning.auth.service.UserService;
import com.github.wxiaoqi.learning.auth.util.JWTTokenUtil;
import com.github.wxiaoqi.learning.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.learning.common.util.jwt.IJWTInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * 生成access token
 * Created by ace on 2017/9/10.
 */
@RestController
@RequestMapping("jwt")
public class JWTRest {
    @Autowired
    private JWTTokenUtil jwtUtil;
    @Autowired
    private UserService userService;

    /**
     * 根据password方式生成access token
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public ObjectRestResponse authorize(String username, String password) throws Exception {
        UserInfo info = userService.login(username, password);
        return new ObjectRestResponse<String>().data(jwtUtil.generateToken(info));
    }

    /**
     * gaoyide by 2019-03-05
     * 校验token
     * @param token
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/verificationToken", method = RequestMethod.POST)
    public ObjectRestResponse verificationToken(String token)throws Exception {
        IJWTInfo iJWTInfo = jwtUtil.getInfoFromToken(token);
        System.out.println("getUniqueName : "+iJWTInfo.getUniqueName());
        return new ObjectRestResponse<IJWTInfo>().data(iJWTInfo);
    }
}