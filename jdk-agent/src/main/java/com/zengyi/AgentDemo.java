package com.zengyi;

import java.lang.instrument.Instrumentation;

/**
 * -javaagent:/Users/cengyi/Desktop/code/jdk21-learn/jdk-agent/target/jdk-agent-1.0-SNAPSHOT-jar-with-dependencies.jar=k1=v1,k2=v2 -jar xx.jar
 */
public class AgentDemo {

    /**
     * main 方法之前执行的方法
     *
     * @param args            agent 参数, 如 k1=v1,k2=v2...
     * @param instrumentation jdk 自带工具类
     */
    public static void premain(String args, Instrumentation instrumentation) {
        System.out.println("agent 执行: " + args);
        instrumentation.addTransformer(new MyTransformer());
    }
}
