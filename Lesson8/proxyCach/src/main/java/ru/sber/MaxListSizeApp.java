package ru.sber;

import ru.sber.proxy.MyCacheProxy;
import ru.sber.service.MyService;
import ru.sber.service.impl.MyServiceImpl;

import java.util.List;

public class MaxListSizeApp {
    public static void main(String[] args) {
        MyService originalService = new MyServiceImpl();
        MyCacheProxy proxy = new MyCacheProxy("tempList");

        MyService cachedService = proxy.cache(originalService);

        List<String> strings = cachedService.fetchLargeDataSet("one");
        List<String> strings1 = cachedService.fetchLargeDataSet("two");
        List<String> strings2 = cachedService.fetchLargeDataSet("one");
        System.out.println(strings);
        System.out.println(strings1);
        System.out.println(strings2);
    }
}
