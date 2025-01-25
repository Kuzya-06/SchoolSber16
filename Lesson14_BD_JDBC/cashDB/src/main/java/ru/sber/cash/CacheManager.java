package ru.sber.cash;

import ru.sber.source.Source;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Класс кэша, который реализует аннотацию для управления кэшированием
 */
public class CacheManager {
    public static List<Integer> getCachedResult(Object obj, Method method, int n) throws Exception {
        Cachable cachable = method.getAnnotation(Cachable.class);
        if (cachable != null) {
            Source source = cachable.value().getDeclaredConstructor().newInstance();
            String cacheKey = method.getName() + "_" + n;

            List<Integer> cachedData = source.load(cacheKey);
            if (cachedData != null) {
                return cachedData; // Возвращаем данные из кэша
            }

            // Если данных нет в кэше, вызываем метод
            List<Integer> result = (List<Integer>) method.invoke(obj, n);
            source.save(cacheKey, result); // Сохраняем результат в кэш
            return result;
        }
        return null;
    }
}
