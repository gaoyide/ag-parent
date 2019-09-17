package com.github.wxiaoqi.learning.auth.rest;

import com.github.wxiaoqi.learning.auth.bean.ClientInfo;
import com.github.wxiaoqi.learning.auth.service.ClientService;
import com.github.wxiaoqi.learning.auth.util.ClientTokenUtil;
import com.github.wxiaoqi.learning.common.msg.ObjectRestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
/**
 * 服务间的鉴权，模拟Oauth的client方式来做
 *
 *
 */
@RestController
@RequestMapping("client")
public class ClientRest {
    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientTokenUtil clientTokenUtil;

    /**
     *
     *  鉴权中心开发
     *   |-服务注册(秘钥派发)
     *   clientID,secret -> token
     *
     * 申请服务鉴权的token
     * @param clientId  客户端id
     * @param secret    客户端密码
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public ObjectRestResponse getAccessToken(String clientId, String secret) throws Exception {
        ClientInfo apply = clientService.apply(clientId, secret);
        return new ObjectRestResponse<String>().data(clientTokenUtil.generateToken(apply));
    }

    /**
     * 获取授权的客户端列表：就是谁能访问我？
     * 拉取客户端列表
     *
     * @param serviceId
     * @param secret
     * @return
     */
    @RequestMapping(value = "/myClient", method = RequestMethod.POST)
    public ObjectRestResponse getAllowedClient(String serviceId, String secret) {
        return new ObjectRestResponse<List<String>>().data(clientService.getAllowedClient(serviceId,secret));
    }

}