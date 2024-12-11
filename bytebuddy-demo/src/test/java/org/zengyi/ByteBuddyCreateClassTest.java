package org.zengyi;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.ModifierReviewable;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ByteBuddyCreateClassTest {

    private static final String PATH = ByteBuddyTest.class.getClassLoader().getResource("").getPath();

    /**
     * (19) 对静态方法插桩
     */
    @Test
    public void test19() throws InvocationTargetException, IllegalAccessException, IOException, NoSuchMethodException {
        DynamicType.Unloaded<SomethingClass> sayWhatUnload = new ByteBuddy().rebase(SomethingClass.class)
                // 拦截 名为 "sayWhat" 的静态方法
                .method(ElementMatchers.named("sayWhat").and(ModifierReviewable.OfByteCodeElement::isStatic))
                // 拦截后的修改/增强逻辑
                .intercept(MethodDelegation.to(new SomethingInterceptor06())).name("com.example.AshiamdTest19").make();
        // 调用类静态方法, 验证是否执行了增强逻辑
        Class<? extends SomethingClass> loadedClazz = sayWhatUnload.load(getClass().getClassLoader()).getLoaded();
        Method sayWhatMethod = loadedClazz.getMethod("sayWhat", String.class);
        sayWhatMethod.invoke(null, "hello world");
        sayWhatUnload.saveIn(new File(PATH));
    }
}