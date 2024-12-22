package ru.sber;

import java.util.LinkedList;
import java.util.Queue;

/**
 * В конструкторе задается минимальное и максимальное(int min, int max) число потоков,
 * количество запущенных потоков может быть увеличено от минимального к максимальному, если при добавлении нового
 * задания в очередь нет свободного потока для исполнения этого задания. При отсутствии задания в очереди, количество потоков опять должно быть уменьшено до значения min
 */
public class ScalableThreadPool implements ThreadPool{

    private final int minThreads;
    private final int maxThreads;
    private final Queue<Runnable> taskQueue = new LinkedList<>();
    private final LinkedList<Thread> threadPool = new LinkedList<>();
    private boolean isRunning = true;

    public ScalableThreadPool(int minThreads, int maxThreads) {
        this.minThreads = minThreads;
        this.maxThreads = maxThreads;
    }

    @Override
    public void start() {
        for (int i = 0; i < minThreads; i++) {
            addThread();
        }
    }

    @Override
    public void execute(Runnable runnable) {
        synchronized (taskQueue) {
            taskQueue.add(runnable);
            taskQueue.notify();
        }
        synchronized (threadPool) {
            //Если текущий размер пула меньше maxThreads и все потоки заняты (нет потоков в состоянии WAITING),
            // вызывается метод addThread() для добавления нового потока в пул.
            if (threadPool.size() < maxThreads &&
                    threadPool.stream()
                            .noneMatch(thread -> thread.getState() == Thread.State.WAITING)) {
                addThread();
            }
        }
    }

    /**
     * Для добавления нового потока в пул
     */
    private void addThread() {
        Thread thread = new Thread(() -> {
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

                synchronized (threadPool) {
                    if (taskQueue.isEmpty() && threadPool.size() > minThreads) {
                        threadPool.remove(Thread.currentThread());
                        return;
                    }
                }
            }
        });
        threadPool.add(thread);
        thread.start();
    }

    public void shutdown() {

        synchronized (taskQueue) {
            taskQueue.notifyAll();
        }
        synchronized (threadPool) {
            for (Thread thread : threadPool) {
                thread.interrupt();
            }
        }
//        isRunning = false;
    }
}
