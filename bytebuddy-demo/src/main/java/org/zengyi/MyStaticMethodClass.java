package org.zengyi;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class MyStaticMethodClass {

    @RuntimeType
    public void test(@Origin Class<?> clazz, // 获取被拦截方法的静态类
                     @Origin Method targetMethod, //  被拦截方法, 只有拦截实例或静态方法可用
                     @AllArguments Object[] targetArgs,  // 被拦截方法的所有参数, 只有实例方法时可用
                     @SuperCall Callable<?> zuper // 用于调用目标方法
    ) throws Throwable {
        try {
            System.out.println(clazz);
            System.out.println(targetMethod);
            System.out.println(targetArgs);
            System.out.println(zuper);
            zuper.call();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
