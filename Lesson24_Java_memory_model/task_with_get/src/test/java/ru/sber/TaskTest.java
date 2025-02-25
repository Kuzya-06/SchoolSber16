package ru.sber;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    void testSuccessfulExecution() throws Exception {
        Task<Integer> task = new Task<>(() -> 42);
        assertEquals(42, task.get());
    }

    @Test
    void testSingleExecution() throws InterruptedException, ExecutionException {
        AtomicInteger counter = new AtomicInteger(0);
        Task<Integer> task = new Task<>(counter::incrementAndGet);

        ExecutorService executor = Executors.newFixedThreadPool(10);
        Future[] futures = new Future[10];

        for (int i = 0; i < 10; i++) {
            futures[i] = executor.submit(task::get);
        }
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        assertEquals(1, counter.get());
        assertEquals(1, futures[0].get());
    }

    @Test
    void testExceptionPropagation() {
        Task<Integer> task = new Task<>(() -> {
            throw new IllegalArgumentException("Исключение");
        });

        RuntimeException thrown = assertThrows(Task.TaskExecutionException.class, task::get);
        assertTrue(thrown.getCause() instanceof IllegalArgumentException);
        assertEquals("Исключение", thrown.getCause().getMessage());
    }

    @Test
    @DisplayName("Кэширование результата")
    void testCachedResult() {
        AtomicInteger counter = new AtomicInteger(0);
        Task<Integer> task = new Task<>(counter::incrementAndGet);

        int firstResult = task.get();
        int secondResult = task.get();

        assertEquals(1, firstResult);
        assertEquals(1, secondResult);
        assertEquals(1, counter.get());
    }
}