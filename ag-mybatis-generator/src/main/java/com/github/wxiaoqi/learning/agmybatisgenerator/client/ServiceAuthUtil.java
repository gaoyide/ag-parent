package com.github.wxiaoqi.learning.agmybatisgenerator.client;

import com.github.wxiaoqi.learning.agmybatisgenerator.feign.ServiceAuthFeign;
import com.github.wxiaoqi.learning.common.msg.BaseResponse;
import com.github.wxiaoqi.learning.common.msg.ObjectRestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Slf4j
@Configuration
public class ServiceAuthUtil {

    @Value("${client.token-header}")
    private String tokenHeader;

    @Value("${client.id}")
    private String clientId;

    @Value("${client.secret}")
    private String clientSecret;

    @Autowired
    private ServiceAuthFeign serviceAuthFeign;

    private List<String> allowedClient;

    private String clientToken;


    public void refreshClientToken() {
        log.debug("refresh client token.....");
        BaseResponse resp = serviceAuthFeign.getAccessToken(clientId, clientSecret);
        if (resp.getStatus() == 200) {
            ObjectRestResponse<String> clientToken = (ObjectRestResponse<String>) resp;
            this.clientToken = clientToken.getData();
        }
    }

    public void refreshAllowedClient() {
        log.debug("refresh allowedClient.....");
        BaseResponse resp = serviceAuthFeign.getAllowedClient(clientId,clientSecret);
        if (resp.getStatus() == 200) {
            ObjectRestResponse<List<String>> allowedClient = (ObjectRestResponse<List<String>>) resp;
            this.allowedClient = allowedClient.getData();
        }
    }


    /**
     * 申请当前service的client-token
     * @return
     */
    public String getClientToken() {
        if (this.clientToken == null) {
            this.refreshClientToken();
        }
        return clientToken;
    }

    /**
     * 获取能够访问服务的服务列表
     * @return
     */
    public List<String> getAllowedClient() {
        if (this.allowedClient == null) {
            this.refreshAllowedClient();
        }
        return allowedClient;
    }
}
