package com.alishangtian.blogspider.service;

import com.alishangtian.blogspider.cluster.BrokerConfig;
import com.alishangtian.blogspider.cluster.Node;
import com.alishangtian.blogspider.remoting.Remoting;
import com.alishangtian.blogspider.util.JSONUtils;
import com.google.gson.reflect.TypeToken;
import io.netty.util.HashedWheelTimer;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Description TopicService
 * @Date 2020/4/28 下午2:25
 * @Author maoxiaobing
 **/
@Service
@Log4j2
public class BrokerService {
    @Autowired
    private BrokerConfig brokerConfig;

    private ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
        AtomicLong num = new AtomicLong();

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "broker-schedule-thread-" + num.getAndIncrement());
        }
    });

    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(8, 10, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100), new ThreadFactory() {
        AtomicLong num = new AtomicLong();

        @Override
        public Thread newThread(@NotNull Runnable r) {
            return new Thread(r, "broker-ping-thread-" + num.getAndIncrement());
        }
    });
    private ConcurrentMap<String, Node> activeNodes = new ConcurrentHashMap<>();
    private ConcurrentMap<String, Node> outNodes = new ConcurrentHashMap<>();
    private HashedWheelTimer timer = new HashedWheelTimer(new ThreadFactory() {
        AtomicLong num = new AtomicLong();

        @Override
        public Thread newThread(@NotNull Runnable r) {
            return new Thread(r, "hashed-wheel-timer-thread-" + num.getAndIncrement());
        }
    });

    @PostConstruct
    public void init() {
        brokerConfig.getNodeList().forEach(s -> activeNodes.put(s, new Node(s)));
        /**
         * ping schedule
         */
        if (brokerConfig.isPingEnabled()) {
            scheduledExecutorService.scheduleWithFixedDelay(() -> ping(), 0, 10000, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 执行ping
     *
     * @Author maoxiaobing
     * @Description ping
     * @Date 2020/4/28
     * @Param []
     * @Return void
     */
    public void ping() {
        activeNodes.forEach((s, node) -> {
            pingServer(s, node);
        });
    }

    private void pingServer(String s, Node node) {
        if (!s.equals(brokerConfig.getSelfServer())) {
            log.info("ping {}", s);
            threadPoolExecutor.submit(() -> {
                if (!Remoting.ping(s, activeNodes.values(), brokerConfig.getSelfServer(), brokerConfig.getPort())) {
                    activeNodes.remove(s);
                    outNodes.putIfAbsent(s, node);
                    timer.newTimeout(timeout -> pingServer(s, node), 5, TimeUnit.SECONDS);
                } else {
                    activeNodes.putIfAbsent(s, node);
                    outNodes.remove(s);
                }
            });
        }
    }

    /**
     * 回复ping
     *
     * @Author maoxiaobing
     * @Description pong
     * @Date 2020/4/30
     * @Param [server, port, body]
     * @Return java.lang.String
     */
    public String pong(String server, int port, String body) {
        List<Node> nodes = JSONUtils.parseObject(body, new TypeToken<List<Node>>() {
        }.getType());
        nodes.add(new Node(server, port));
        nodes.forEach(node -> {
            activeNodes.putIfAbsent(node.getServer(), node);
            outNodes.remove(node.getServer());
        });
        return "pong";
    }
}
