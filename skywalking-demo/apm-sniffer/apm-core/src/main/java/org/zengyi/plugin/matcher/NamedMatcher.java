package org.zengyi.plugin.matcher;

/**
 * 专门使用与类的全路径名匹配, 类似 named
 */
public class NamedMatcher implements ClassMatcher {

    private String className;

    public NamedMatcher(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public static NamedMatcher byClassName(String className) {
        return new NamedMatcher(className);
    }
}
