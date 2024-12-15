package org.zengyi.plugin.matcher;

import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

/**
 * 所有非 NamedMatcher 的情况都要实现 IndirectMatcher
 */
public interface IndirectMatcher extends ClassMatcher {

    ElementMatcher.Junction<? extends TypeDescription> buildJunction();

    boolean isMatch(TypeDescription typeDescription);
}
