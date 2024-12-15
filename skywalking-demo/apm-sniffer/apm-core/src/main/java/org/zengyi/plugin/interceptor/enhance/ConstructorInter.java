package org.zengyi.plugin.interceptor.enhance;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;

public class ConstructorInter {

    private ConstructorInterceptor constructorInter;

    public ConstructorInter(String interceptor, ClassLoader classLoader) {
    }

    @RuntimeType
    public void intercept(@This Object object, @AllArguments Object[] args) {
        try {
            constructorInter.onConstructor(object, args);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
