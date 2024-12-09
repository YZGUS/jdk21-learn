package org.zengyi;

import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class MyDelegationClass {

//    public String print() {
//        return "delegate";
//    }

    // @RuntimeType:
    // @This: 被拦截方法的对象, 只有实例方法时可用
    // @Origin: 被拦截方法, 只有拦截实例或静态方法可用
    // @AllArguments: 被拦截方法的所有参数, 只有实例方法时可用
    // @Super: 被拦截方法的父类方法, 只有实例方法时可用, 若确定父类，也可用具体父类类型接受
    // @SuperCall: 用于调用目标方法
    @RuntimeType //  被该注释标注的方法就是拦截方法, 此时方法签名 或 返回值可以与被拦截方法不一致
    public String diyPrint(@This Object targetObj, // 被拦截方法的对象, 只有实例方法时可用
                           @Origin Method targetMethod, //  被拦截方法, 只有拦截实例或静态方法可用
                           @AllArguments Object[] targetArgs,  // 被拦截方法的所有参数, 只有实例方法时可用
                           @Super Object targetSuper, // 被拦截方法的父类方法, 只有实例方法时可用。若确定父类，可以用父类接受
                           @SuperCall Callable<?> zuper // 用于调用目标方法
    ) throws Throwable {
        System.out.println(targetObj);
        System.out.println(targetMethod);
        System.out.println(targetArgs);
        System.out.println(targetSuper);
        System.out.println(zuper);
        return "diyPrint : ";
    }
}
