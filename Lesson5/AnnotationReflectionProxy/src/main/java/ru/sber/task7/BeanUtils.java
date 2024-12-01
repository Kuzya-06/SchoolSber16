package ru.sber.task7;

import java.lang.reflect.Method;

public class BeanUtils {
    /**
     * Scans object "from" for all getters. If object "to"
     * contains correspondent setter, it will invoke it
     * to set property value for "to" which equals to the property
     * of "from".
     * <p/>
     * The type in setter should be compatible to the value returned
     * by getter (if not, no invocation performed).
     * Compatible means that parameter type in setter should
     * be the same or be superclass of the return type of the getter.
     * <p/>
     * The method takes care only about public methods.
     *
     * @param to   Object which properties will be set.
     * @param from Object which properties will be used to get values.
     */
    public static void assign(Object to, Object from) {
        if (to == null || from == null) {
            throw new IllegalArgumentException("Аргументы 'to' и 'from' не должны быть null.");
        }

        Class<?> fromClass = from.getClass();
        Class<?> toClass = to.getClass();

        Method[] fromMethods = fromClass.getMethods();
        Method[] toMethods = toClass.getMethods();

        for (Method getter : fromMethods) {
            if (isGetter(getter)) {
                String propertyName = getter.getName().substring(3);
                Class<?> returnType = getter.getReturnType();

                for (Method setter : toMethods) {
                    if (isSetter(setter) && setter.getName().equals("set" + propertyName)) {
                        Class<?>[] parameterTypes = setter.getParameterTypes();
                        if (parameterTypes.length == 1 && parameterTypes[0].isAssignableFrom(returnType)) {
                            try {
                                Object value = getter.invoke(from);
                                setter.invoke(to, value);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean isGetter(Method method) {
        return method.getName().startsWith("get")
                && method.getName().length() > 3
                && method.getReturnType() != void.class
                && method.getParameterCount() == 0;
    }

    private static boolean isSetter(Method method) {
        return method.getName().startsWith("set")
                && method.getName().length() > 3
                && method.getParameterCount() == 1;
    }

}
