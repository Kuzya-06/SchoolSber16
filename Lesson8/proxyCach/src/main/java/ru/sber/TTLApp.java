package ru.sber;

import ru.sber.proxy.MyCacheProxy;
import ru.sber.service.MyService;
import ru.sber.service.impl.MyServiceImpl;

public class TTLApp {

    public static void main(String[] args) throws InterruptedException {
        MyService originalService = new MyServiceImpl();
        MyCacheProxy proxy = new MyCacheProxy("temp3");

        MyService cachedService = proxy.cache(originalService);

        double r1 = cachedService.doHardWorkWithTTL("work1", 10); // Выполнится работа
        Thread.sleep(1000);
        double r2 = cachedService.doHardWorkWithTTL("work1", 10); // Результат из кеша
        Thread.sleep(2000);
        double r3 = cachedService.doHardWorkWithTTL("work1", 10); // Выполнится работа заново (TTL истек)

        System.out.println(r1 + ", " + r2 + ", " + r3);
    }
}
