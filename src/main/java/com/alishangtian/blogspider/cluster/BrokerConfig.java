package com.alishangtian.blogspider.cluster;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

/**
 * @Description BrokerProperties
 * @Date 2020/4/29 下午4:46
 * @Author maoxiaobing
 **/
@Configuration
@Getter
public class BrokerConfig {
    @Value("${server.port}")
    private int port;
    @Value("${brokers.nodes}")
    private String nodes;
    @Value("${brokers.ping.enabled}")
    private boolean pingEnabled;
    private List<String> nodeList;
    private String selfServer;

    @PostConstruct
    public void init() {
        Assert.notNull(nodes, "brokers.nodes is empty");
        nodeList = Arrays.asList(nodes.split(","));
        try {
            selfServer = InetAddress.getLocalHost().getHostAddress() + ":" + port;
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
