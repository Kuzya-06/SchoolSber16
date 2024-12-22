package ru.sber;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Lesson11Main {
    public static void main(String[] args) {

        ThreadPool fixedThreadPool = new FixedThreadPool(4);
        fixedThreadPool.start();

        for (int i = 0; i < 11; i++) {
            int taskNumber = i;
            fixedThreadPool.execute(() -> {
                System.out.println("FixedThreadPool: Задача " + taskNumber + " выполняется потоком " + Thread.currentThread().getName());
            });
        }



        ThreadPool scalableThreadPool = new ScalableThreadPool(2, 4);
        scalableThreadPool.start();

        for (int i = 0; i < 10; i++) {
            int taskNumber = i;
            scalableThreadPool.execute(() -> {
                System.out.println("ScalableThreadPool: Задача " + taskNumber + " выполняется потоком " + Thread.currentThread().getName());
            });
        }

        fixedThreadPool.shutdown();
        scalableThreadPool.shutdown();


//        ExecutorService executorService = Executors.newFixedThreadPool(3);
//
//        // Отправляем задачи на выполнение
//        for (int i = 0; i < 10; i++) {
//            int taskNumber = i;
//            executorService.submit(() -> {
//                System.out.println("ExecutorService: Задача " + taskNumber + " выполняется потоком " + Thread.currentThread().getName());
//            });
//        }
//
//        // Останавливаем пул потоков
//        executorService.shutdown();
    }

}
