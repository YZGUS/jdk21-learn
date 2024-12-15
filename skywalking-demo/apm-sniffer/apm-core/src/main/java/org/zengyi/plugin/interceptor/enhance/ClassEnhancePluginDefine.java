package org.zengyi.plugin.interceptor.enhance;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.matcher.ElementMatcher;
import org.zengyi.plugin.AbstractClassEnhancePluginDefine;
import org.zengyi.plugin.interceptor.ConstructorMethodsInterceptorPoint;
import org.zengyi.plugin.interceptor.InstanceMethodInterceptorPoint;
import org.zengyi.plugin.interceptor.StaticMethodInterceptorPoint;

import static net.bytebuddy.implementation.FieldAccessor.ofField;
import static net.bytebuddy.implementation.MethodDelegation.withDefaultConfiguration;
import static net.bytebuddy.jar.asm.Opcodes.ACC_PRIVATE;
import static net.bytebuddy.jar.asm.Opcodes.ACC_VOLATILE;
import static net.bytebuddy.matcher.ElementMatchers.isStatic;

public abstract class ClassEnhancePluginDefine extends AbstractClassEnhancePluginDefine {

    // 增强静态方法
    @Override
    protected DynamicType.Builder<?> enhanceClass(TypeDescription typeDescription, DynamicType.Builder<?> bulider, ClassLoader classLoader) {
        final StaticMethodInterceptorPoint[] staticMethodsInterceptPoints = getStaticMethodsInterceptPoints();
        if (staticMethodsInterceptPoints == null || staticMethodsInterceptPoints.length == 0) {
            return bulider;
        }

        for (StaticMethodInterceptorPoint point : staticMethodsInterceptPoints) {
            final String interceptor = point.getStaticInterceptor();
            final ElementMatcher<MethodDescription> matcher = point.getStaticMethodMatcher();
            bulider = bulider.method(isStatic().and(matcher)).intercept(withDefaultConfiguration().to(new StaticMethodInter(interceptor, classLoader)));
        }
        return bulider;
    }

    // 增强动态 和 构造方法
    @Override
    protected DynamicType.Builder<?> enhanceInstance(TypeDescription typeDescription, DynamicType.Builder<?> bulider, ClassLoader classLoader, EnhanceContext context) {
        final ConstructorMethodsInterceptorPoint[] constructorInterceptPoints = getConstructorInterceptPoints();
        final InstanceMethodInterceptorPoint[] instanceMethodsInterceptPoints = getInstanceMethodsInterceptPoints();
        if (constructorInterceptPoints == null || instanceMethodsInterceptPoints == null) {
            return bulider;
        }

        if (!typeDescription.isAssignableTo(EnhanceContext.class) && !context.isObjectEnhanced()) {
            bulider.defineField(CONTEXT_ATTR_NAME, Object.class, ACC_PRIVATE | ACC_VOLATILE).implement(EnhancedInstance.class).intercept(ofField(CONTEXT_ATTR_NAME));
            context.objectEnhancedCompleted();
        }

        for (ConstructorMethodsInterceptorPoint point : constructorInterceptPoints) {
            final String interceptor = point.getConstructorInterceptor();
            final ElementMatcher<MethodDescription> matcher = point.getConstructorMatcher();
            bulider = bulider.constructor(matcher).intercept(withDefaultConfiguration().to(SuperMethodCall.INSTANCE.andThen(withDefaultConfiguration().to(new ConstructorInter(interceptor, classLoader)))));
        }

        for (InstanceMethodInterceptorPoint point : instanceMethodsInterceptPoints) {
            final String interceptor = point.getInstanceInterceptor();
            final ElementMatcher<MethodDescription> matcher = point.getInstanceMethodsMatcher();
            bulider = bulider.method(matcher).intercept(withDefaultConfiguration().to(new InstanceMethodInter(interceptor, classLoader)));
        }
        return bulider;
    }
}
