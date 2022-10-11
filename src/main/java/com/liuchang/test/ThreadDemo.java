package com.liuchang.test;

import com.liuchang.test.domain.EventConsumeCommand;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @Description: TODO
 * @Author: liuchang
 * @CreateTime: 2022-10-11  17:38
 */
public class ThreadDemo {

    public static void main(String[] args) {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

        for (int i = 0; i < 100; i++) {
            service.execute(new EventConsumeCommand(i));
            System.out.println("2222:::"+Thread.currentThread().getName()+"第"+i);
        }

    }
}
