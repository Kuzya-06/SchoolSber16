package ru.sber.task6;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class PerformanceProxy implements InvocationHandler {
    private final Object target;

    public PerformanceProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (method.isAnnotationPresent(Metric.class)) {
            long start = System.nanoTime();
            Object result = method.invoke(target, args);
            long end = System.nanoTime();
            System.out.println("Время работы метода: " + (end - start) + " (в наносек)");
            return result;
        }
        return method.invoke(target, args);
    }


    @SuppressWarnings("unchecked")
    public static <T> T createProxy(T target, Class<T> interfaceType) {
        System.out.println("Target: " + target);
        System.out.println("Interface: " + interfaceType);
        System.out.println("ClassLoader: " + interfaceType.getClassLoader().getName());

        return (T) Proxy.newProxyInstance(
                interfaceType.getClassLoader(),
                new Class<?>[]{interfaceType},
                new PerformanceProxy(target)
        );
    }

}
