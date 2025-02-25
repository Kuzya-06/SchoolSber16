package ru.sber;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecutionManagerImpl implements ExecutionManager {

    private final ExecutorService threadPool;

    public ExecutionManagerImpl(int threadSize) {
        this.threadPool = Executors.newFixedThreadPool(threadSize);
    }

    @Override
    public Context execute(Runnable callback, Runnable... tasks) {
        TaskContext context = new TaskContext(tasks.length, threadPool);
        for (Runnable task : tasks) {
            threadPool.submit(() -> {
                if (context.isInterrupted()) {
                    context.incrementInterruptedTasks();
                    return;
                }
                try {
                    task.run();
                    context.incrementCompletedTasks();
                } catch (Exception e) {
                    context.incrementFailedTasks();
                }
            });
        }
        threadPool.submit(() -> {
            try {
                context.awaitCompletion();
                if (!context.isInterrupted()) {
                    callback.run();
                }
            } catch (Exception ignored) {
            }
        });
        return context;
    }


    private static class TaskContext implements Context {
        private final AtomicInteger completedTasks = new AtomicInteger(0);
        private final AtomicInteger failedTasks = new AtomicInteger(0);
        private final AtomicInteger interruptedTasks = new AtomicInteger(0);
        private final CountDownLatch latch;
        private volatile boolean interrupted = false;
        private final ExecutorService threadPool;

        public TaskContext(int taskCount, ExecutorService threadPool) {
            this.latch = new CountDownLatch(taskCount);
            this.threadPool = threadPool;
        }

        public void incrementCompletedTasks() {
            completedTasks.incrementAndGet();
            latch.countDown();
        }

        public void incrementFailedTasks() {
            failedTasks.incrementAndGet();
            latch.countDown();
        }

        public void incrementInterruptedTasks() {
            interruptedTasks.incrementAndGet();
            latch.countDown();
        }

        public void awaitCompletion() throws InterruptedException {
            latch.await();
        }

        @Override
        public int getCompletedTaskCount() {
            return completedTasks.get();
        }

        @Override
        public int getFailedTaskCount() {
            return failedTasks.get();
        }

        @Override
        public int getInterruptedTaskCount() {
            return interruptedTasks.get();
        }


        @Override
        public void interrupt() {
            threadPool.shutdown();
            interrupted = true;
        }

        @Override
        public boolean isFinished() {
            boolean isCount = latch.getCount() == 0 ;
            boolean terminated = threadPool.isTerminated();
            System.out.println("isCount - "+isCount+" terminated - "+terminated);
            return isCount || terminated;
        }

        public boolean isInterrupted() {
            return interrupted;
        }
    }

}
