package ru.sber;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Количество потоков задается в конструкторе и не меняется.
 */
public class FixedThreadPool implements ThreadPool{
    private final int threadCount;
    private final Queue<Runnable> taskQueue = new LinkedList<>();
    private final Thread[] threads;
    private boolean isRunning = true;

    public FixedThreadPool(int threadCount) {
        this.threadCount = threadCount;
        this.threads = new Thread[threadCount];
    }
    @Override
    public void start() {
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                while (isRunning) {
                    Runnable task;
                    synchronized (taskQueue) {
                        while (taskQueue.isEmpty() && isRunning) {
                            try {
                                taskQueue.wait();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                return;
                            }
                        }
                        task = taskQueue.poll();
                    }
                    if (task != null) {
                        task.run();
                    }

                }
            });
            threads[i].start();
        }
    }

    @Override
    public void execute(Runnable runnable) {
        synchronized (taskQueue) {
            taskQueue.add(runnable);
            taskQueue.notify();
        }
    }

    public void shutdown() {
//        isRunning = false;
        synchronized (taskQueue) {
            taskQueue.notifyAll();
        }
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }
}
