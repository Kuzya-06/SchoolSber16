package ru.sber.task5;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class CachingProxy implements InvocationHandler {

    private final Object target;
    private final Map<String, Object> cache = new HashMap<>();

    public CachingProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String key = method.getName() + "(" + (args != null ? args[0] : "") + ")";
        if (cache.containsKey(key)) {
            System.out.println("Кэш найден для ключа: " + key);
            return cache.get(key);
        }

        Object result = method.invoke(target, args);
        cache.put(key, result); // Сохраняем результат в кэш
        System.out.println("Результат сохранён в кэш для ключа: " + key);
        return result;

    }

    @SuppressWarnings("unchecked")
    public static <T> T createProxy(T target, Class<T> interfaceType) {
        return (T) Proxy.newProxyInstance(
                interfaceType.getClassLoader(),
                new Class<?>[]{interfaceType},
                new CachingProxy(target)
        );
    }

}

