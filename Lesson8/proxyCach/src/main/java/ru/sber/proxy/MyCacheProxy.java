package ru.sber.proxy;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.sber.annotations.MyCacheable;
import ru.sber.cache.CacheEntry;
import ru.sber.cache.MemoryCache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


public class MyCacheProxy {

    private final Log log = LogFactory.getLog(MyCacheProxy.class);

    /**
     * Рутовая папка в которой хранятся файлы
     */
    private final String rootFolder;

    public MyCacheProxy(String rootFolder) {
        this.rootFolder = rootFolder;
    }

    /**
     * Объект, который хранит:
     * ключ - cacheKeyTTL - строковое имя, образующееся из имени метода, параметров
     * значение - CacheEntry<Object> - которое внутри себя хранит значение и время его создания
     */
    private static final Map<String, CacheEntry<Object>> cacheTTL = new ConcurrentHashMap<>();

    /**
     * Принимает ссылку на сервис и возвращает кешированную версию этого сервиса
     *
     * @param service
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T cache(T service) {
        return (T) Proxy.newProxyInstance(
                service.getClass().getClassLoader(),
                service.getClass().getInterfaces(),
                (proxy, method, args) -> {
                    MyCacheable cacheable = method.getAnnotation(MyCacheable.class);
                    if (cacheable == null) {
                        return method.invoke(service, args);
                    }

                    String cacheKey = generateCacheKey(method, args, cacheable.includeArgs());

                    String cacheKeyTTL = createCacheKey(method, args);
                    CacheEntry<Object> cacheEntry = cacheTTL.get(cacheKeyTTL);

                    log.debug("cacheEntry => " + cacheEntry);

                    //---------------------------------MEMORY
                    if (cacheable.storageType().equals(MyCacheable.StorageType.MEMORY)) {
                        Object result = MemoryCache.getInstance().get(cacheKey);
                        // Проверяем TTL
                        if (cacheEntry != null && isCacheValid(cacheEntry, cacheable.ttl())) {
//                            System.out.println("Результат из кеша: " + cacheKeyTTL);
//                            System.out.println("Результат из кеша: " + cacheEntry.getCreationTime() + " " + cacheable.ttl());
//                            System.out.println("Результат из кеша: " + result);
//                            return cacheEntry.getValue();
                            return result;
                        }
                        if (result != null && isCacheValid(cacheEntry, cacheable.ttl())) {
                            return result;
                        }
                        // Если кеш не валиден или отсутствует, выполняем работу
                        result = method.invoke(service, args);
                        // Сохраняем в
                        cacheTTL.put(cacheKeyTTL, new CacheEntry<>(result));

                        if (result instanceof List) {
                            result = truncateList((List<?>) result, cacheable.maxListSize());
                        }

                        MemoryCache.getInstance().put(cacheKey, result);
                        return result;
                    }

                    //---------------------------------DISK
                    else if (cacheable.storageType().equals(MyCacheable.StorageType.DISK)) {
                        File file = new File(rootFolder, cacheKey + ".cache");
                        if (file.exists()) {
                            return readFromFile(file, cacheable.zip());
                        }

                        Object result = method.invoke(service, args);
                        if (result instanceof List) {
                            result = truncateList((List<?>) result, cacheable.maxListSize());
                        }
                        saveToFile(file, result, cacheable.zip());
                        return result;
                    }
                    //---------------------------------
                    return method.invoke(service, args);
                }
        );
    }

    private String generateCacheKey(Method method, Object[] args, int[] ignoreArgs) {
        StringBuilder key = new StringBuilder(method.getName());
        for (int i = 0; i < args.length; i++) {
            int finalI = i;
            if (Arrays.stream(ignoreArgs).noneMatch(index -> index == finalI)) {
                key.append("_").append(args[i]);
            }
        }
        return key.toString();
    }

    private List<?> truncateList(List<?> list, int maxSize) {
        if (list.size() > maxSize) {
            return list.subList(0, maxSize);
        }
        return list;
    }

    private void saveToFile(File file, Object result, boolean zip) throws IOException {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try (OutputStream os = zip
                ? new GZIPOutputStream(new FileOutputStream(file))
                : new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(result);
        }
    }

    private Object readFromFile(File file, boolean zip) throws IOException, ClassNotFoundException {
        try (InputStream is = zip
                ? new GZIPInputStream(new FileInputStream(file))
                : new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(is)) {
            return ois.readObject();
        }
    }

    private boolean isCacheValid(CacheEntry<Object> entry, long ttl) {
        if (ttl <= 0) {
            return true; // TTL бесконечный
        }
        return (System.currentTimeMillis() - entry.getCreationTime()) < ttl;
    }

    private String createCacheKey(Method method, Object[] args) {
        StringBuilder keyBuilder = new StringBuilder(method.getName());
        for (Object arg : args) {
            keyBuilder.append("_").append(arg);
        }
        return keyBuilder.toString();
    }
}

