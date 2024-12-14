package org.zengyi;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;

import static net.bytebuddy.matcher.ElementMatchers.*;

public class MyTransformer implements AgentBuilder.Transformer {

    // 当要被拦截的 type 第一次加载时会进入该方法
    @Override
    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, ProtectionDomain protectionDomain) {
        System.out.println("actual transform: " + typeDescription.getActualName());
        final DynamicType.Builder.MethodDefinition.ReceiverTypeDefinition<?> intercept = builder
                .method(not(isStatic())
                        .and(isAnnotatedWith(named("org.springframework.web.bind.annotation.RequestMapping"))))
                .intercept(MethodDelegation.to(new MyInterceptor()));
        return intercept;
    }
}
