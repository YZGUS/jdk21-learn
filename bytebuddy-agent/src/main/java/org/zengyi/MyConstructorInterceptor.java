package org.zengyi;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;

import java.util.Arrays;

public class MyConstructorInterceptor {

    @RuntimeType
    public void intercept(@This Object object, @AllArguments Object[] args) {
        System.out.println("intercept: args=" + Arrays.toString(args) + ", object=" + object);
    }
}
