package org.zengyi;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;

public class MyListener implements AgentBuilder.Listener {

    // 当某个类要加载时调用该方法
    @Override
    public void onDiscovery(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
        System.out.println("onDiscovery: " + typeName);
    }

    // 当某个类完成 transform 后回调
    @Override
    public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded, DynamicType dynamicType) {
        System.out.println("onTransformation: " + typeDescription);
    }

    // 当某个类要加载, 但是被 Bytebuddy 配置忽略(或本身没有配置被拦截)时回调
    @Override
    public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded) {
        System.out.println("onIgnored: " + typeDescription);
    }

    // 当 Byte Buddy 在 transform 过程中 发生异常, 则执行该方法
    @Override
    public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded, Throwable throwable) {
        System.out.println("onError: " + typeName);
    }

    // 某个类处理结束后(transform/ignore/error都算), 回调该方法
    @Override
    public void onComplete(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
        System.out.println("onComplete: " + typeName);
    }
}
