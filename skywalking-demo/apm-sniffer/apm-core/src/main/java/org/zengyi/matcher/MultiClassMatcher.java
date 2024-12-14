package org.zengyi.matcher;

import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.Arrays;
import java.util.List;

public class MultiClassMatcher implements IndirectMatcher {

    private List<String> needMatchClassNames;

    public MultiClassMatcher(String[] needMatchClassNames) {
        if (needMatchClassNames == null || needMatchClassNames.length == 0) {
            throw new IllegalArgumentException("needMatchClassNames is null");
        }
        this.needMatchClassNames = Arrays.asList(needMatchClassNames);
    }

    @Override
    public ElementMatcher.Junction<? extends TypeDefinition> buildJunction() {
        ElementMatcher.Junction<? extends TypeDefinition> junction = null;
        for (String className : needMatchClassNames) {
            if (junction == null) {
                junction = ElementMatchers.named(className);
            } else {
                junction = junction.or(ElementMatchers.named(className));
            }
        }
        return junction;
    }

    public static IndirectMatcher byMultiClassNames(String[] classNames) {
        return new MultiClassMatcher(classNames);
    }
}
