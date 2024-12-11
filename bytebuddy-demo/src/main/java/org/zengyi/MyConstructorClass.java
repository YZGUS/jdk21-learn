package org.zengyi;

import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;

public class MyConstructorClass {

    @RuntimeType
    public void advice(@This Object target) {
        System.out.println("constructor: " + target);
    }
}
