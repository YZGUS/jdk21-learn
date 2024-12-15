package org.zengyi;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import org.zengyi.plugin.interceptor.ConstructorMethodsInterceptorPoint;
import org.zengyi.plugin.interceptor.InstanceMethodInterceptorPoint;
import org.zengyi.plugin.interceptor.StaticMethodInterceptorPoint;
import org.zengyi.plugin.interceptor.enhance.ClassEnhancePluginDefine;

import static net.bytebuddy.matcher.ElementMatchers.*;

public abstract class SpringMVCCommonInstrumentation extends ClassEnhancePluginDefine {

    @Override
    protected ConstructorMethodsInterceptorPoint[] getConstructorInterceptPoints() {
        return null;
    }

    @Override
    protected InstanceMethodInterceptorPoint[] getInstanceMethodsInterceptPoints() {
        return new InstanceMethodInterceptorPoint[]{
                new InstanceMethodInterceptorPoint() {
                    @Override
                    public ElementMatcher<MethodDescription> getInstanceMethodsMatcher() {
                        return not(isStatic())
                                .and(isAnnotatedWith(named("org.springframework.web.bind.annotation.RequestMapping")));
                    }

                    @Override
                    public String getInstanceInterceptor() {
                        return "org.zengyi.plugin.interceptor.SpringMVCInterceptor";
                    }
                }
        };
    }

    @Override
    protected StaticMethodInterceptorPoint[] getStaticMethodsInterceptPoints() {
        return null;
    }
}
