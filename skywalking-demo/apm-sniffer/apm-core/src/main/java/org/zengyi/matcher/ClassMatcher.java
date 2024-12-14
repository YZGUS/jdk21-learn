package org.zengyi.matcher;

/**
 * 所有类匹配器的顶级接口，本身空实现
 * 1. 单个类的全限定名匹配
 * 2. 所有非NameMatch的匹配器的顶级接口
 * 2.1 MultiClassNameMatch：匹配多个类名的匹配器（多个全限制类名之间是or的关系）
 * 2.2 ClassAnnotationNameMatch：匹配同时带有多个指定注解的类匹配器（多个注解之间是and的关系）
 */
public interface ClassMatcher {
    
}
