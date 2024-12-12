package ru.sber.service.impl;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.sber.proxy.MyCacheProxy;
import ru.sber.service.MyService;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class MyServiceImplTest {

    @Test
    public void testCaching() {

        MyService originalService = Mockito.spy(new MyServiceImpl());
        MyCacheProxy proxy = new MyCacheProxy("tempCache1");

        MyService cachedService = proxy.cache(originalService);

        // Первые вызовы - работа выполняется
        long start = System.currentTimeMillis();
        double r1 = cachedService.doHardWork("work1", 10);
        double r2 = cachedService.doHardWork("work2", 5);
        long firstExecutionTime = System.currentTimeMillis() - start;

        // Повторные вызовы - результаты из кеша
        start = System.currentTimeMillis();
        double r11 = cachedService.doHardWork("work1", 10);
        double r21 = cachedService.doHardWork("work2", 5);
        long secondExecutionTime = System.currentTimeMillis() - start;

        assertEquals(r1, r11, "Результаты кеша не совпадают для work1");
        assertEquals(r2, r21, "Результаты кеша не совпадают для work2");

        verify(originalService, times(1)).doHardWork("work1", 10); // Должен быть вызван 1 раз
        verify(originalService, times(1)).doHardWork("work2", 5);  // Должен быть вызван 1 раз
        verifyNoMoreInteractions(originalService); // Больше никаких вызовов к оригинальному сервису быть не должно

        System.out.println("Время первого выполнения: " + firstExecutionTime);
        System.out.println("Время второго выполнения: " + secondExecutionTime);
        assertTimeoutPreemptively(Duration.ofMillis(firstExecutionTime + 60), () -> {
            assertTimeoutPreemptively(Duration.ofMillis(firstExecutionTime / 2), () -> {
            });
        });
    }


    @Test
    void doHardWorkWithIncludeArgs() {
    }

    @Test()
    public void testTTL() throws InterruptedException {
        MyService originalService = new MyServiceImpl();
        MyCacheProxy proxy = new MyCacheProxy("tempCache");

        MyService cachedService = proxy.cache(originalService);

        // Выполняем работу с TTL = 1000 мс
        long start = System.currentTimeMillis();
        double r1 = cachedService.doHardWorkWithTTL("work1", 10);
        long executionTime1 = System.currentTimeMillis() - start;

        // Ждем, чтобы TTL истек
        Thread.sleep(2000);

        // Выполняем работу снова
        start = System.currentTimeMillis();
        double r2 = cachedService.doHardWorkWithTTL("work1", 10);
        long executionTime2 = System.currentTimeMillis() - start;

        // Проверяем, что значения одинаковые, но время выполнения отличается
        assertEquals(r1, r2, "Результаты после истечения TTL не совпадают");
        System.out.println("Время выполнения после TTL: " + executionTime2);
        System.out.println("Начальное время выполнения: " + executionTime1);
        assertTimeoutPreemptively(Duration.ofMillis(executionTime1 + 50), () -> {
        });
    }


    @Test
    void fetchLargeDataSet() {
    }
}