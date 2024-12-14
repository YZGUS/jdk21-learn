package org.zengyi;

import net.bytebuddy.agent.builder.AgentBuilder;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.*;

public class MyAgent {

    public static void premain(String args, Instrumentation instrumentation) {
        System.out.println("premain: " + args);
        new AgentBuilder
                .Default()
                .ignore(nameStartsWith("org.springframework.web")) // 忽略加载指定类
                .type(isAnnotatedWith(named("org.springframework.stereotype.Controller")
                        .or(named("org.springframework.web.bind.annotation.RestController")))
                        .or(named("org.zengyi.springdemo.demos.web.User")))// 配置加载类
                .transform(new MyTransformer())
                .transform(new MyStaticTransformer())
                .transform(new MyConstructorTransformer())
                .with(new MyListener())
                .installOn(instrumentation); // 将当前的 Agent 安装到 JVM 中
    }
}
