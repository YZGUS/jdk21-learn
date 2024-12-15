package org.zengyi;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import org.zengyi.plugin.interceptor.ConstructorMethodsInterceptorPoint;
import org.zengyi.plugin.interceptor.InstanceMethodInterceptorPoint;
import org.zengyi.plugin.interceptor.StaticMethodInterceptorPoint;
import org.zengyi.plugin.interceptor.enhance.ClassEnhancePluginDefine;
import org.zengyi.plugin.matcher.ClassMatcher;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static org.zengyi.plugin.matcher.MultiClassMatcher.byMultiClassNames;

public class MySQLInstrumentation extends ClassEnhancePluginDefine {

    @Override
    protected ClassMatcher enhanceClass() {
        return byMultiClassNames("com.mysql.cj.jdbc.ClientPreparedStatement", "com.mysql.cj.jdbc.ServerPreparedStatement");
    }

    @Override
    protected ConstructorMethodsInterceptorPoint[] getConstructorInterceptPoints() {
        return null;
    }

    @Override
    protected InstanceMethodInterceptorPoint[] getInstanceMethodsInterceptPoints() {
        return new InstanceMethodInterceptorPoint[]{new InstanceMethodInterceptorPoint() {
            @Override
            public ElementMatcher<MethodDescription> getInstanceMethodsMatcher() {
                return named("execute").or(named("executeQuery")).or(named("executeUpdate"));
            }

            @Override
            public String getInstanceInterceptor() {
                return "org.zengyi.plugin.interceptor.MySQLInterceptor";
            }
        }};
    }

    @Override
    protected StaticMethodInterceptorPoint[] getStaticMethodsInterceptPoints() {
        return null;
    }
}
