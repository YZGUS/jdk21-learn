package org.zengyi.plugin.interceptor;

import org.zengyi.plugin.interceptor.enhance.EnhancedInstance;
import org.zengyi.plugin.interceptor.enhance.InstanceMethodAroundInterceptor;

import java.lang.reflect.Method;

public class SpringMVCInterceptor implements InstanceMethodAroundInterceptor {

    @Override
    public void before(EnhancedInstance enhancedInstance, Method method, Object[] args, Class<?>[] argTypes) {
        System.out.println("SpringMVCInterceptor before: " + method.getName());
    }

    @Override
    public Object after(EnhancedInstance enhancedInstance, Method method, Object[] args, Class<?>[] argTypes, Object result) {
        System.out.println("SpringMVCInterceptor after: " + method.getName());
        return result;
    }

    @Override
    public void handleEx(EnhancedInstance enhancedInstance, Method method, Object[] args, Class<?>[] argTypes, Object result, Throwable throwable) {
        System.out.println("SpringMVCInterceptor handleEx: " + method.getName());
    }
}
