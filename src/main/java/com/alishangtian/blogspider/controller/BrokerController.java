package com.alishangtian.blogspider.controller;

import com.alishangtian.blogspider.service.BrokerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description MessageController
 * @Date 2020/4/26 下午6:34
 * @Author maoxiaobing
 **/
@RestController
public class BrokerController {
    @Autowired
    private BrokerService brokerService;

    /**
     * @Author maoxiaobing
     * @Description publish
     * @Date 2020/4/26
     * @Param []
     * @Return java.lang.String
     */
    @PostMapping("/ping/{server}/{port}")
    public String ping(@PathVariable int port, @PathVariable String server, @RequestBody String body) {
        return brokerService.pong(server, port, body);
    }
}
