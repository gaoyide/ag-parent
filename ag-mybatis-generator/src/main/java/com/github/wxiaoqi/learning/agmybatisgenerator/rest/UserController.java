package com.github.wxiaoqi.learning.agmybatisgenerator.rest;

import com.github.wxiaoqi.learning.agmybatisgenerator.biz.BaseUserBiz;
import com.github.wxiaoqi.learning.agmybatisgenerator.entity.BaseUser;
import com.github.wxiaoqi.learning.common.exception.BaseException;
import com.github.wxiaoqi.learning.common.rest.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 测试访问：
 * http://172.24.102.10:8001/user/page?name=%E6%B5%8B%E8%AF%95
 * http://172.24.102.10:8001/user/page?page=1&limit=1
 */
@Slf4j
@RestController
@RequestMapping("user")
public class UserController extends BaseController<BaseUserBiz,BaseUser>{

    @RequestMapping("/hello/{name}")
    public String home(@PathVariable String name) {

       return "hello "+name;
    }
}
