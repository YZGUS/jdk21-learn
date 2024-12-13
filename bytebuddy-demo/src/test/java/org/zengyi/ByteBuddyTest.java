package org.zengyi;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.NamingStrategy;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.*;
import net.bytebuddy.implementation.bind.annotation.Morph;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.pool.TypePool;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Modifier;

import static net.bytebuddy.matcher.ElementMatchers.*;

public class ByteBuddyTest {

    private static final String PATH = ByteBuddyTest.class.getClassLoader().getResource("").getPath();

    @Test
    public void testInitClass() throws Throwable {
        // 默认 new NamingStrategy.SuffixingRandom("ByteBuddy")
        final NamingStrategy.SuffixingRandom suffixingRandom = new NamingStrategy.SuffixingRandom("my");
        final DynamicType.Unloaded<MyClass> unloaded = new ByteBuddy()
                // 设置是否进行类校验, 默认为开启
                // .with(TypeValidation.of(false))
                /**
                 * 1. 不指定命名策略:
                 *  a.JDK 类: net.bytebuddy.renamed.java.lang.Object$ByteBuddy$bltwnQ5S
                 *  b.非 JDK 类: org.zengyi.MyClass$ByteBuddy$j0h7QKzx
                 * 2. 指定命名策略:
                 *  a.JDK 类: net.bytebuddy.renamed.java.lang.Object$my$hk5TXn64
                 *  b.非 JDK 类: org.zengyi.MyClass$my$hv8N9p3v
                 */.with(suffixingRandom) // 设置命名策略
                .subclass(MyClass.class) // 设置继承的子类
                /**
                 * 指定类名, 指定的类名会进行校验,
                 * a.b.c.MyNameClass : 正常
                 * a.b.c.1MyNameClass : java.lang.IllegalStateException: Illegal type name: a.b.c.1MyNameClass for class a.b.c.1MyNameClass
                 */.name("a.b.c.MyNameClass").make();
        unloaded.saveIn(new File(PATH));
    }

    public void testInjectJar() throws Throwable {
        final DynamicType.Unloaded<MyClass> unloaded = new ByteBuddy().subclass(MyClass.class).make();
        // unloaded.inject() //  将生成的字节码注入到指定的 jar 包中
    }

    @Test
    public void testInstanceMethod() throws Throwable {
        final DynamicType.Unloaded<MyClass> unloaded = new ByteBuddy().subclass(MyClass.class).method(named("print")) // 指定要拦截的方法
                .intercept(FixedValue.value("intercept")) // 拦截处理逻辑
                .make(); // class 尚未加载到 JVM 中
        final DynamicType.Loaded<MyClass> loaded = unloaded.load(getClass().getClassLoader()); // 加载到 JVM 中
        final Class<? extends MyClass> clazz = loaded.getLoaded(); // 获取加载类
        System.out.printf(clazz.newInstance().print()); // 初始化类并调用方法
        // unloaded.saveIn(new File(PATH)); // 注意当该类方在 make 之后指定会发导致方法报错: java.lang.IllegalStateException: Class already loaded: class org.zengyi.MyClass$ByteBuddy$NRbjwOSP
        loaded.saveIn(new File(PATH)); // 同样拥有 saveIn, getBytes, inject, 该类和 unloaded 都继承自 DynamicType
    }

    @Test
    public void testDynamicEnhanceThreeWay() throws Throwable {
        /**
         * 动态增强的三种方式:
         * 1. subclass: 生成子类
         * 2. rebase: 保留原方法并重新命名为 xx$original, xx为拦截后的逻辑, 需要查看字节码
         * 3. redefine: 重新定义, 和 rebase 类似, 但是不会保留原方法
         */
        final DynamicType.Unloaded<MyClass> unloaded = new ByteBuddy()
                // .subclass(MyClass.class)
                // .rebase(MyClass.class)
                .redefine(MyClass.class).name("EnhanceMyClass").method(named("print").and(returns(TypeDescription.class).or(returns(TypeDescription.OBJECT)).or(returns(TypeDescription.STRING)))).intercept(FixedValue.value("intercept")).make();
        unloaded.saveIn(new File(PATH));
    }

    @Test
    public void testInsertNewMethod() throws Throwable {
        final DynamicType.Unloaded<MyClass> unloaded = new ByteBuddy().redefine(MyClass.class).name("InsertMethodClass").defineMethod("print2", String.class, Modifier.PUBLIC) // 定义方法
                .withParameter(String[].class, "args") // 制定方法参数
                .intercept(FixedValue.value("new method")) // 拦截处理逻辑
                .make();
        unloaded.saveIn(new File(PATH));
    }

    @Test
    public void testInsertNewField() throws Throwable {
        final DynamicType.Unloaded<MyClass> unloaded = new ByteBuddy().redefine(MyClass.class).name("InsertFieldClass").defineField("newField", String.class, Modifier.PRIVATE) // 定义方法
                .implement(NewFieldInterface.class) // 指定字段的 Get 和 Set 所在的接口, 注意接口必须可见
                .intercept(FieldAccessor.ofField("newField")).make();
        unloaded.saveIn(new File(PATH));
    }

    @Test
    public void testMethodDelegation() throws Throwable {
        final DynamicType.Unloaded<MyClass> unloaded = new ByteBuddy().subclass(MyClass.class).method(named("print"))
                // 默认为委托给签名(方法名)相同的静态方法！！！
                // .intercept(MethodDelegation.to(MyDelegationClass.class))
                // 默认委托给同签名的成员方法, 如果为 static 则存在问题: org.zengyi.MyDelegationClass@1a38c59b
                // 当拦截类中指定了 @RuntimeType 注解则会委托到该注解标注的方法, 注意当有同名方法时优先同名？？
                .intercept(MethodDelegation.to(new MyDelegationClass())).make();
        final DynamicType.Loaded<MyClass> loaded = unloaded.load(getClass().getClassLoader());
        final MyClass myClass = loaded.getLoaded().newInstance();
        System.out.println(myClass.print());
        loaded.saveIn(new File(PATH));
    }

    @Test
    public void testModifyArgs() throws Throwable {
        final DynamicType.Unloaded<MyClass> unloaded = new ByteBuddy().subclass(MyClass.class).method(named("printAge")).intercept(MethodDelegation.withDefaultConfiguration().withBinders(Morph.Binder.install(MyCallable.class)).to(new MyModifyArgsClass())).make();
        final DynamicType.Loaded<MyClass> loaded = unloaded.load(getClass().getClassLoader());
        final MyClass myClass = loaded.getLoaded().newInstance();
        System.out.printf(myClass.printAge(100));
        loaded.saveIn(new File(PATH));
    }

    @Test
    public void testConstructorAdvice() throws Throwable {
        final DynamicType.Unloaded<MyClass> unloaded = new ByteBuddy().subclass(MyClass.class).constructor(any()).intercept(SuperMethodCall.INSTANCE.andThen( // 指定构造方法在执行完成后再委托给拦截器
                MethodDelegation.to(new MyConstructorClass()))).make();
        final DynamicType.Loaded<MyClass> loaded = unloaded.load(getClass().getClassLoader());
        final MyClass myClass = loaded.getLoaded().newInstance();
        loaded.saveIn(new File(PATH));
    }

    @Test
    public void testStaticMethodAdvice() throws Throwable {
        final DynamicType.Unloaded<MyClass> unloaded = new ByteBuddy()
                // .subclass(MyClass.class) // 会增强失败: 静态方法不能被继承
                // .redefine(MyClass.class) // 会增强失败: redefine 不会保留原始方法 @SuperCall 不会生效
                .rebase(MyClass.class).name("a.b.StaticMethodAdvice").method(ElementMatchers.named("sayWhat")).intercept(MethodDelegation.to(new MyStaticMethodClass())).make();
        final DynamicType.Loaded<MyClass> loaded = unloaded.load(getClass().getClassLoader());
        loaded.getLoaded().getMethod("sayWhat", String.class).invoke(null, "hello world");
        loaded.saveIn(new File(PATH));
    }

    @Test
    public void testLoadClassForJar() throws Throwable {
        final String path = "/Users/cengyi/.m2/repository/org/springframework/spring-core/5.3.24/spring-core-5.3.24.jar";
        final ClassFileLocator springCoreLocator = ClassFileLocator.ForJarFile.of(new File(path));
        final ClassFileLocator systemLoader = ClassFileLocator.ForClassLoader.ofSystemLoader();
        final ClassFileLocator.Compound compound = new ClassFileLocator.Compound(springCoreLocator, systemLoader);
        final TypePool typePool = TypePool.Default.of(compound);
        final TypeDescription description = typePool.describe("org.springframework.util.StopWatch").resolve();
        new ByteBuddy()
                .redefine(description, compound)
                .method(named("getId"))
                .intercept(FixedValue.nullValue())
                .make()
                .saveIn(new File(PATH));
    }

    @Test
    public void testLoadClassForFolder() throws Throwable {
        final String path = "/Users/cengyi/Desktop/code/spring5-demo/target/classes";
        final ClassFileLocator.ForFolder folderLocator = new ClassFileLocator.ForFolder(new File(path));
        final ClassFileLocator systemLoader = ClassFileLocator.ForClassLoader.ofSystemLoader();
        final ClassFileLocator.Compound compound = new ClassFileLocator.Compound(folderLocator, systemLoader);
        final TypePool typePool = TypePool.Default.of(compound);
        final TypeDescription describe = typePool.describe("org.zengyi.spring5demo.demos.web.BasicController").resolve();
        new ByteBuddy()
                .redefine(describe, compound)
                .method(named("hello"))
                .intercept(FixedValue.nullValue())
                .make()
                .saveIn(new File(PATH));
    }

    @Test
    public void testClearMethod() throws Throwable {
        new ByteBuddy()
                .redefine(MyClass.class)
                .method(any())
                .intercept(StubMethod.INSTANCE)
                .make()
                .saveIn(new File(PATH));
    }
}
