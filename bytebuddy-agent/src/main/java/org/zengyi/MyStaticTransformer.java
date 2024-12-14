package org.zengyi;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;

import static net.bytebuddy.matcher.ElementMatchers.isStatic;

public class MyStaticTransformer implements AgentBuilder.Transformer {

    @Override
    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder,
                                            TypeDescription typeDescription,
                                            ClassLoader classLoader,
                                            JavaModule module,
                                            ProtectionDomain protectionDomain) {
        final DynamicType.Builder.MethodDefinition.ReceiverTypeDefinition<?> intercept = builder
                .method(isStatic().and(ElementMatchers.named("info")))
                .intercept(MethodDelegation.to(new MyStaticInterceptor()));
        return intercept;
    }
}
