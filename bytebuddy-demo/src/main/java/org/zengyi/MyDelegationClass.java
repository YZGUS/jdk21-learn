package org.zengyi;

import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class MyDelegationClass {

//    public String print() {
//        return "delegate";
//    }

    @RuntimeType //  被该注释标注的方法就是拦截方法, 此时方法签名 或 返回值可以与被拦截方法不一致
    public String diyPrint(@This Object targetObj, // 被拦截方法的对象, 只有实例方法时可用
                           @Origin Method targetMethod, //  被拦截方法, 只有拦截实例或静态方法可用
                           @AllArguments Object[] targetArgs,  // 被拦截方法的所有参数, 只有实例方法时可用
                           @Super Object targetSuper, // 被拦截方法的父类方法, 只有实例方法时可用。若确定父类，可以用父类接受
                           @SuperCall Callable<?> zuper // &#x7528;&#x4E8E;&#x8C03;&#x7528;&#x76EE;&#x6807;&#x65B9;&#x6CD5;
    ) {
        System.out.println(targetObj);
        System.out.println(targetMethod);
        System.out.println(targetArgs);
        System.out.println(targetSuper);
        System.out.println(zuper);
        return "diyPrint : ";
    }
}
