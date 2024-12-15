package org.zengyi.plugin.interceptor.enhance;

import java.lang.reflect.Method;

public interface InstanceMethodAroundInterceptor {

    void before(EnhancedInstance enhancedInstance, Method method, Object[] args, Class<?>[] argTypes);

    Object after(EnhancedInstance enhancedInstance, Method method, Object[] args, Class<?>[] argTypes, Object result);

    void handleEx(EnhancedInstance enhancedInstance, Method method, Object[] args, Class<?>[] argTypes, Object result, Throwable throwable);
}
