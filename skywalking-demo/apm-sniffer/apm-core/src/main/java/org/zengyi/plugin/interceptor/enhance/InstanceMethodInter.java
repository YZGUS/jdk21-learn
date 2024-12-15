package org.zengyi.plugin.interceptor.enhance;

import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class InstanceMethodInter {

    private InstanceMethodAroundInterceptor aroundInterceptor;

    public InstanceMethodInter(String interceptor, ClassLoader classLoader) {
    }


    @RuntimeType
    public Object intercept(@This Object obj, @Origin Method method, @AllArguments Object[] args, @SuperCall Callable<?> zuper) {
        final EnhancedInstance enhancedInstance = (EnhancedInstance) obj;
        Object call = null;
        try {
            aroundInterceptor.before(enhancedInstance, method, args, method.getParameterTypes());
        } catch (Throwable t) {
            System.out.println("before failed: " + t);
        }

        try {
            call = zuper.call();
        } catch (Throwable t) {
            aroundInterceptor.handleEx(enhancedInstance, method, args, method.getParameterTypes(), call, t);
            t.printStackTrace();
        } finally {
            try {
                aroundInterceptor.after(enhancedInstance, method, args, method.getParameterTypes(), call);
            } catch (Throwable t) {
                System.out.println("after failed: " + t);
            }
        }
        return call;
    }
}
