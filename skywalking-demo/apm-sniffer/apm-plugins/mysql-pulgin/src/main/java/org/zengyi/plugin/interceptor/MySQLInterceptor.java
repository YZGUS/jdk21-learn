package org.zengyi.plugin.interceptor;

import org.zengyi.plugin.interceptor.enhance.EnhancedInstance;
import org.zengyi.plugin.interceptor.enhance.InstanceMethodAroundInterceptor;

import java.lang.reflect.Method;

public class MySQLInterceptor implements InstanceMethodAroundInterceptor {

    @Override
    public void before(EnhancedInstance enhancedInstance, Method method, Object[] args, Class<?>[] argTypes) {
        enhancedInstance.setSkywalkingDynamicField("aaa");
        System.out.println("MySQLInterceptor before ");
    }

    @Override
    public Object after(EnhancedInstance enhancedInstance, Method method, Object[] args, Class<?>[] argTypes, Object result) {
        System.out.println("MySQLInterceptor after" + enhancedInstance.getSkywalkingDynamicField());
        return result;
    }

    @Override
    public void handleEx(EnhancedInstance enhancedInstance, Method method, Object[] args, Class<?>[] argTypes, Object result, Throwable throwable) {
        System.out.println("MySQLInterceptor handleEx");
    }
}
