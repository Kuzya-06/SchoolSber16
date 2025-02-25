package ru.sber;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Task<T> {

    private final Callable<? extends T> callable;
    private T result;
    private volatile boolean isCalculated = false; //Флаг, указывающий, был ли результат уже вычислен
    private volatile ExecutionException exception = null;
    private final Lock lock = new ReentrantLock();

    public Task(Callable<? extends T> callable) {
        this.callable = callable;
    }

    public T get() {
        if (!isCalculated) {
            lock.lock();
            try {
                if (!isCalculated) {
                    try {
                        result = callable.call();
                        isCalculated = true;
                    } catch (Exception e) {
                        exception = new ExecutionException(e);
                        throw new TaskExecutionException("Исключение во время выполнения задачи", e);
                    }
                }
            } finally {
                lock.unlock();
            }
        }

        if (exception != null) {
            throw new TaskExecutionException("Исключение во время выполнения задачи", exception.getCause());
        }

        return result;
    }

    public static class TaskExecutionException extends RuntimeException {
        public TaskExecutionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
