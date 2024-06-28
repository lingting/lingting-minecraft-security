package live.lingting.minecraft.security;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author lingting 2024-06-28 11:59
 */
@SuppressWarnings("unchecked")
public class Reflection {

    public static <T> T get(String name, String field) throws ClassNotFoundException, IllegalAccessException, InvocationTargetException {
        return get(Class.forName(name), field);
    }

    public static <T> T get(Class<?> cls, String fieldName) throws IllegalAccessException, InvocationTargetException {
        return get(null, cls, fieldName);
    }

    public static <T> T get(Object target, String fieldName) throws IllegalAccessException, InvocationTargetException {
        return get(target, target.getClass(), fieldName);
    }

    public static <T> T get(Object target, Class<?> cls, String fieldName) throws IllegalAccessException, InvocationTargetException {
        Field field = field(cls, fieldName);
        Method method = method(cls, "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
        if (field != null && field.isAccessible()) {
            return (T) field.get(target);
        }
        if (method != null && method.isAccessible()) {
            return (T) method.invoke(target);
        }
        if (field != null) {
            field.setAccessible(true);
            return (T) field.get(target);
        }
        if (method != null) {
            method.setAccessible(true);
            return (T) method.invoke(target);
        }
        return null;
    }


    public static Field field(Class<?> cls, String field) {
        return deepMatchArray(cls, Class::getDeclaredFields)
                .stream().filter(it -> Objects.equals(it.getName(), field)).findAny().orElse(null);
    }

    public static <T> T method(Object obj, String methodName) throws InvocationTargetException, IllegalAccessException {
        Method method = method(obj.getClass(), methodName);
        if (method == null) {
            return null;
        }
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
        return (T) method.invoke(obj);
    }

    public static Method method(Class<?> cls, String method) {
        return deepMatchArray(cls, Class::getDeclaredMethods)
                .stream().filter(it -> Objects.equals(it.getName(), method)
                        && it.getParameterCount() == 0).findAny().orElse(null);
    }

    static <T> List<T> deepMatchArray(Class<?> cls, Function<Class<?>, T[]> function) {
        return deepMatch(cls, c -> {
            T[] ts = function.apply(c);
            return Arrays.asList(ts);
        });
    }

    static <T> List<T> deepMatch(Class<?> cls, Function<Class<?>, Collection<T>> function) {
        List<T> list = new ArrayList<>();
        deep(cls, c -> list.addAll(function.apply(c)));
        return list;
    }

    static void deep(Class<?> cls, Consumer<Class<?>> consumer) {
        if (cls == null || cls.isAssignableFrom(Object.class)) {
            return;
        }
        consumer.accept(cls);
        deep(cls.getSuperclass(), consumer);
    }
}
