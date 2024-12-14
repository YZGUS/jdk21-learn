package org.zengyi;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

import static java.lang.System.currentTimeMillis;

public class MyStaticInterceptor {

    @RuntimeType
    public Object intercept(@Origin Class<?> clazz, @Origin Method method, @AllArguments Object[] args, @SuperCall Callable<?> zuper) {
        Object call = null;
        final long startMillis = currentTimeMillis();
        System.out.println("intercept before: methd=" + method.getName() + ", args=" + Arrays.toString(args));
        try {
            call = zuper.call();
            System.out.println("intercept after: methd=" + method.getName() + ", args=" + Arrays.toString(args) + ", call = " + call);
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            System.out.println("intercept end: methd=" + method.getName() + ", args=" + Arrays.toString(args) + ", cost=" + (currentTimeMillis() - startMillis));
        }
        return call;
    }
}
