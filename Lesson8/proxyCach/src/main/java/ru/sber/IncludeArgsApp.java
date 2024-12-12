package ru.sber;

import ru.sber.proxy.MyCacheProxy;
import ru.sber.service.MyService;
import ru.sber.service.impl.MyServiceImpl;

public class IncludeArgsApp {
    public static void main(String[] args) {
        MyService originalService = new MyServiceImpl();
        MyCacheProxy proxy = new MyCacheProxy("tempList");

        MyService cachedService = proxy.cache(originalService);

        double r1 = cachedService.doHardWorkWithIncludeArgs("work1", 10);
        double r2 = cachedService.doHardWorkWithIncludeArgs("work2", 5);
        double r11 = cachedService.doHardWorkWithIncludeArgs("work1", 20);
        double r3 = cachedService.doHardWorkWithIncludeArgs("work3", 36);
        double r4 = cachedService.doHardWorkWithIncludeArgs("work1", 10);
        double r21 = cachedService.doHardWorkWithIncludeArgs("work2", 5);
        double r31 = cachedService.doHardWorkWithIncludeArgs("work2", 6);
        double r41 = cachedService.doHardWorkWithIncludeArgs("work1", 20);

        System.out.println(r1 + " " + r2 + " " + r11 + " " + r3 + " " + r4);
        System.out.println(r21 + " " + r31 + " " + r41 );
    }
}
