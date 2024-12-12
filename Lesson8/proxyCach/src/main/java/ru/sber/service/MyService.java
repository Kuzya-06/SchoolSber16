package ru.sber.service;

import ru.sber.annotations.MyCacheable;

import java.util.List;

public interface MyService {
    @MyCacheable(storageType = MyCacheable.StorageType.MEMORY
            , fileName = "hard_work_cache"
            , includeArgs = {0}
    )
    double doHardWork(String param1, int param2);

    @MyCacheable(storageType = MyCacheable.StorageType.MEMORY
            , fileName = "hard_work_cache_include"
            , includeArgs = {2}
    )
    double doHardWorkWithIncludeArgs(String param1, int param2);

    @MyCacheable(storageType = MyCacheable.StorageType.MEMORY
            , fileName = "hard_work_cache_TTL"
            , includeArgs = {1}
//            , ttl = 5000 // выполнится одна работа
//            , ttl = 3000 // выполнится две работы
            , ttl = 1000 // выполнится три работы
    )
    double doHardWorkWithTTL(String param1, int param2);

    @MyCacheable(storageType = MyCacheable.StorageType.MEMORY
            ,maxListSize = 20)
    List<String> fetchLargeDataSet(String param) ;

}
