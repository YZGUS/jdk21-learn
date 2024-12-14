package org.zengyi;

import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

import static java.lang.System.currentTimeMillis;

public class MyInterceptor {

    @RuntimeType
    public Object intercept(@This Object object,
                            @Origin Method method,
                            @AllArguments Object[] args,
                            @SuperCall Callable<?> zuper) {
        Object call = null;
        final long startMillis = currentTimeMillis();
        System.out.println("intercept before: methd=" + method.getName() + ", args=" + Arrays.toString(args));
        try {
            call = zuper.call();
            System.out.println("intercept after: methd=" + method.getName() + ", args=" + Arrays.toString(args));
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            System.out.println("intercept end: methd=" + method.getName() + ", args=" + Arrays.toString(args) +
                    ", cost=" + (currentTimeMillis() - startMillis));
        }
        return call;
    }
}
