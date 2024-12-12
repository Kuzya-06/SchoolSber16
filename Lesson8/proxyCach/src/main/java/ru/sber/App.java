package ru.sber;

import ru.sber.proxy.MyCacheProxy;
import ru.sber.service.MyService;
import ru.sber.service.impl.MyServiceImpl;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        MyService originalService = new MyServiceImpl();
        MyCacheProxy proxy = new MyCacheProxy("temp2");

        MyService cachedService = proxy.cache(originalService);
        long start = System.currentTimeMillis();
        double r1 = cachedService.doHardWork("work1", 10);
        double r2 = cachedService.doHardWork("work2", 5);
        double r11 = cachedService.doHardWork("work1", 10);
        double r3 = cachedService.doHardWork("work3", 36);
        double r4 = cachedService.doHardWork("work4", 100);
        double r21 = cachedService.doHardWork("work2", 5);
        double r31 = cachedService.doHardWork("work3", 36);
        double r41 = cachedService.doHardWork("work4", 100);

        System.out.println("Time lead:" + (System.currentTimeMillis() - start));
        System.out.println(r1 + " " + r2 + " " + r3 + " " + r4);
        System.out.println(r11 + " " + r21 + " " + r31 + " " + r41);

        MyService originalService2 = new MyServiceImpl();
        MyCacheProxy proxy2 = new MyCacheProxy("tempNo");
        MyService cachedService2 = proxy2.cache(originalService2);
       long start2 = System.currentTimeMillis();
        double noCach1 = cachedService2.doHardWork("work11", 1);
        double noCach2 = cachedService2.doHardWork("work12", 2);
        double noCach3 = cachedService2.doHardWork("work13", 3);
        double noCach4 = cachedService2.doHardWork("work14", 4);
        double noCach5 = cachedService2.doHardWork("work15", 5);
        double noCach6 = cachedService2.doHardWork("work16", 6);
        double noCach7 = cachedService2.doHardWork("work17", 7);
        double noCach8 = cachedService2.doHardWork("work18", 8);

        System.out.println("Time lead:" + (System.currentTimeMillis() - start2));
        System.out.println(noCach1 + " " + noCach2 + " " + noCach3 + " " + noCach4);
        System.out.println(noCach5 + " " + noCach6 + " " + noCach7 + " " + noCach8);
    }
}
