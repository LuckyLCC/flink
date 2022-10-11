package com.liuchang.test.domain;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @Description: TODO
 * @Author: liuchang
 * @CreateTime: 2022-10-11  17:39
 */
public class EventConsumeCommand implements Runnable{

    private ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

    private int i ;

    public EventConsumeCommand(int i) {
        this.i=i;
    }


    @Override
    public void run() {
        for (int j = 0; j < 50; j++) {
            System.out.println("第"+i+"次：：："+Thread.currentThread().getName()+"内循环：：："+j);
        }


    }
}
