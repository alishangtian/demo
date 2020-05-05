package com.alishangtian.blogspider.test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description TODO
 * @ClassName TestThreadPool
 * @Author alishangtian
 * @Date 2020/5/1 10:15
 * @Version 0.0.1
 */
public class TestThreadPool {
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 8, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100));

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            threadPoolExecutor.submit(() -> {
                System.out.println("add task");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
            });
        }
        threadPoolExecutor.submit(() -> System.out.println("ninhao"));
    }
}
