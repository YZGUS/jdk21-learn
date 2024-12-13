package com.zengyi;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class MyTransformer implements ClassFileTransformer {

    /**
     * 在某个类的字节码 被加载到JVM之前 都会先进入该方法. 如果对字节码进行修改则返回修改后的字节码, 否则直接返回null即可
     */
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        byte[] bytes = null;
        try {
            if (!"org/zengyi/springdemo/demos/web/BasicController".equals(className)) {
                return bytes;
            }

            final ClassPool classPool = ClassPool.getDefault();
            final CtClass ctClass = classPool.get("org.zengyi.springdemo.demos.web.BasicController");
            final CtMethod hello = ctClass.getDeclaredMethod("hello", new CtClass[]{classPool.get("java.lang.String")});
            hello.insertBefore("{System.out.println(\"before hello\");}");
            bytes = ctClass.toBytecode();
            System.out.println("transform end, className=" + className);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return bytes;
    }
}
