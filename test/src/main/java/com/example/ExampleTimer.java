package com.example;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ExampleTimer {
    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 10000)
    public void timerRate() {
        System.out.println(dateFormat.format(new Date()));
    }

    //第一次延迟1秒执行，当执行完后2秒再执行
    @Scheduled(initialDelay = 1000, fixedDelay = 2000)
    public void timerInit() {
        System.out.println("init : "+dateFormat.format(new Date()));
    }

    //每天20点16分50秒时执行
    @Scheduled(cron = "50 16 20 * * ?")
    public void timerCron() {
        System.out.println("current time : "+ dateFormat.format(new Date()));
    }
}