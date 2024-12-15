package org.zengyi;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.utility.JavaModule;
import org.zengyi.plugin.AbstractClassEnhancePluginDefine;
import org.zengyi.plugin.PluginFinder;
import org.zengyi.plugin.interceptor.enhance.EnhanceContext;

import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.List;

public class SkyWalkingAgent {

    public static void premain(String args, Instrumentation inst) {
        System.out.println("skywalking agent premain, args=" + args);
        PluginFinder pluginFinder = null;
        try {
            pluginFinder = new PluginFinder(null);
        } catch (Throwable t) {
            System.out.println("初始化失败");
            t.printStackTrace();
        }

        final ByteBuddy byteBuddy = new ByteBuddy().with(TypeValidation.of(true));
        final AgentBuilder agentBuilder = new AgentBuilder.Default(byteBuddy);
        agentBuilder.type(pluginFinder.buildMatch()).transform((builder, typeDescription, classLoader, module, protectionDomain) -> null).installOn(inst);
    }

    public static class MyTransform implements AgentBuilder.Transformer {

        private final PluginFinder pluginFinder;

        public MyTransform(PluginFinder pluginFinder) {
            this.pluginFinder = pluginFinder;
        }

        @Override
        public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, ProtectionDomain protectionDomain) {
            final String actualName = typeDescription.getActualName();
            System.out.println("transform " + actualName);

            final List<AbstractClassEnhancePluginDefine> pluginDefines = pluginFinder.find(typeDescription);
            if (pluginDefines.size() > 0) {
                final EnhanceContext context = new EnhanceContext();
                DynamicType.Builder<?> newBulider = builder;
                for (AbstractClassEnhancePluginDefine pluginDefine : pluginDefines) {
                    final DynamicType.Builder<?> possibleNewBulider = pluginDefine.define(typeDescription, newBulider, classLoader, context);
                    if (possibleNewBulider != null) {
                        newBulider = possibleNewBulider;
                    }
                }

                if (context.isEnhanced()) {
                    System.out.println("匹配到了 " + actualName + ", 增强成功");
                }
                return newBulider;
            }
            System.out.println("匹配到了 " + actualName + ", 但是无相关插件");
            return builder;
        }
    }
}
