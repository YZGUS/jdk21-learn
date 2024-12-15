package org.zengyi.plugin.interceptor.enhance;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class StaticMethodInter {

    private StaticMethodAroundInterceptor aroundInterceptor;

    public StaticMethodInter(String interceptor, ClassLoader classLoader) {
    }

    @RuntimeType
    public Object intercept(@Origin Class<?> clazz, @Origin Method method, @AllArguments Object[] args, @SuperCall Callable<?> zuper) {
        Object call = null;
        try {
            aroundInterceptor.before(clazz, method, args, method.getParameterTypes());
        } catch (Throwable t) {
            System.out.println("before failed: " + t);
        }

        try {
            call = zuper.call();
        } catch (Throwable t) {
            aroundInterceptor.handleEx(clazz, method, args, method.getParameterTypes(), call, t);
            t.printStackTrace();
        } finally {
            try {
                aroundInterceptor.after(clazz, method, args, method.getParameterTypes(), call);
            } catch (Throwable t) {
                System.out.println("after failed: " + t);
            }
        }
        return call;
    }
}
