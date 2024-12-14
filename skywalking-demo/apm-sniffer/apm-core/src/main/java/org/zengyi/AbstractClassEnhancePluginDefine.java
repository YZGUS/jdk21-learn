package org.zengyi;

import org.zengyi.interceptor.ConstructorMethodsInterceptorPoint;
import org.zengyi.interceptor.InstanceMethodInterceptorPoint;
import org.zengyi.interceptor.StaticMethodInterceptorPoint;
import org.zengyi.matcher.ClassMatcher;

/**
 * 插件增强抽象类:
 * 1. 需要插件表明自己增强的类范围
 * 2. 需要插件表明增强类中哪些方法，以及对应的增强逻辑(拦截器逻辑)
 */
public abstract class AbstractClassEnhancePluginDefine {

    protected abstract ClassMatcher enhanceClass();

    protected abstract ConstructorMethodsInterceptorPoint[] getConstructorInterceptPoints();

    protected abstract InstanceMethodInterceptorPoint[] getInstanceMethodsInterceptPoints();

    protected abstract StaticMethodInterceptorPoint[] getStaticMethodsInterceptPoints();
}
