package com.alishangtian.demo.tree;

import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * 限制qps为固定数字
 *
 * @Description TODO
 * @ClassName QPSLimiter
 * @Author alishangtian
 * @Date 2020/5/10 20:53
 * @Version 0.0.1
 */
@Log4j2
public class QPSLimiter {
    static final List<Long> windowList = new CopyOnWriteArrayList<>();
    static Object lock = new Object();
    final static int MAX_COUNT = 10;
    static final ThreadPoolExecutor pool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), 8, 0L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));

    public static void main(String[] args) {
        accept();
    }

    static void accept() {
        while (true) {
            if (windowCount() >= MAX_COUNT) {
                continue;
            }
            pool.submit(() -> log.info("called"));
            windowList.add(System.currentTimeMillis());
        }
    }

    /**
     * 时间窗口，计算最近一秒的请求数
     *
     * @return
     */
    static int windowCount() {
        synchronized (lock) {
            AtomicInteger count = new AtomicInteger();
            windowList.forEach(aLong -> {
                if (System.currentTimeMillis() - aLong >= 1000) {
                    windowList.remove(aLong);
                } else {
                    count.incrementAndGet();
                }
            });
            return count.get();
        }
    }
}
