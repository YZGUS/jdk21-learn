package org.zengyi.plugin.interceptor.enhance;

import java.lang.reflect.Method;

public interface StaticMethodAroundInterceptor {

    void before(Class clazz, Method method, Object[] args, Class<?>[] argTypes);

    Object after(Class clazz, Method method, Object[] args, Class<?>[] argTypes, Object result);

    void handleEx(Class clazz, Method method, Object[] args, Class<?>[] argTypes, Object result, Throwable throwable);
}
