package ru.sber.service.impl;

import ru.sber.service.MyService;

import java.util.ArrayList;
import java.util.List;

public class MyServiceImpl implements MyService {
    @Override
    /* Здесь не будет работать, т.к. в данном случае используется Proxy.newProxyInstance, который создает
     прокси-объект только для интерфейсов. Если аннотация @MyCacheable находится на методах реализации
     (MyServiceImpl),  прокси не видит эту аннотацию, так как он проксирует вызовы методов интерфейса (MyService),
     а не реализации. */
//    @MyCacheable(storageType = MyCacheable.StorageType.MEMORY
//            , fileName = "hard_work_cache"
//            ,maxListSize = 2
//            , includeArgs = {0}
//    )
    public double doHardWork(String param1, int param2) {
        // Симуляция тяжелой работы
        return extracted(param1, param2);
    }

    @Override
    public double doHardWorkWithIncludeArgs(String param1, int param2) {
        // Симуляция тяжелой работы
        return extracted(param1, param2);
    }

    @Override
    public double doHardWorkWithTTL(String param1, int param2) {
        // Симуляция тяжелой работы
        return extracted(param1, param2);
    }


    @Override
    public List<String> fetchLargeDataSet(String param) {
        try {
            int param2 = 100;
            System.out.println("Выполняется работа для: " + param + ", где " + param2 + " элементов");
            Thread.sleep(2000); // Симуляция задержки
            // Возвращает большой список
            List<String> data = new ArrayList<>();
            for (int i = 0; i < param2; i++) {
                data.add(param + "_" + i);
            }
            return data;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }



    private static double extracted(String param1, int param2) {
        try {
            System.out.println("Выполняется работа для: " + param1 + ", " + param2);
            Thread.sleep(2000); // Симуляция задержки
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Ошибка во время выполнения задачи", e);
        }
        return param1.length() * Math.pow(param2, 2);
    }
}
