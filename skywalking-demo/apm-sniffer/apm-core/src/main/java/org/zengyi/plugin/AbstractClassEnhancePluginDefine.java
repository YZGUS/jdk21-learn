package org.zengyi.plugin;

import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import org.zengyi.plugin.interceptor.ConstructorMethodsInterceptorPoint;
import org.zengyi.plugin.interceptor.InstanceMethodInterceptorPoint;
import org.zengyi.plugin.interceptor.StaticMethodInterceptorPoint;
import org.zengyi.plugin.interceptor.enhance.EnhanceContext;
import org.zengyi.plugin.matcher.ClassMatcher;

/**
 * 插件增强抽象类:
 * 1. 需要插件表明自己增强的类范围
 * 2. 需要插件表明增强类中哪些方法，以及对应的增强逻辑(拦截器逻辑)
 */
public abstract class AbstractClassEnhancePluginDefine {

    public static final String CONTEXT_ATTR_NAME = "_$EnhanceClassField_ws";

    protected abstract ClassMatcher enhanceClass();

    protected abstract ConstructorMethodsInterceptorPoint[] getConstructorInterceptPoints();

    protected abstract InstanceMethodInterceptorPoint[] getInstanceMethodsInterceptPoints();

    protected abstract StaticMethodInterceptorPoint[] getStaticMethodsInterceptPoints();

    public DynamicType.Builder<?> define(TypeDescription typeDescription, DynamicType.Builder<?> bulider, ClassLoader classLoader, EnhanceContext context) {
        final String pluginDefineClassName = this.getClass().getName();
        final String typeName = typeDescription.getTypeName();
        System.out.println("开始使用插件 " + pluginDefineClassName + " 增强 " + typeName);
        final DynamicType.Builder<?> newBuilder = this.enhance(typeDescription, bulider, classLoader, context);
        context.initializationStageCompleted();
        System.out.println("使用插件 " + pluginDefineClassName + " 增强 " + typeName + " 结束");
        return newBuilder;
    }

    private DynamicType.Builder<?> enhance(TypeDescription typeDescription, DynamicType.Builder<?> bulider, ClassLoader classLoader, EnhanceContext context) {
        bulider = this.enhanceClass(typeDescription, bulider, classLoader);
        bulider = this.enhanceInstance(typeDescription, bulider, classLoader, context);
        return bulider;
    }

    protected abstract DynamicType.Builder<?> enhanceClass(TypeDescription typeDescription, DynamicType.Builder<?> bulider, ClassLoader classLoader);

    protected abstract DynamicType.Builder<?> enhanceInstance(TypeDescription typeDescription, DynamicType.Builder<?> bulider, ClassLoader classLoader, EnhanceContext context);
}
