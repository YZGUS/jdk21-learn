package org.zengyi.matcher;

import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.matcher.ElementMatcher;

/**
 * 所有非 NamedMatcher 的情况都要实现 IndirectMatcher
 */
public interface IndirectMatcher extends ClassMatcher {

    ElementMatcher.Junction<? extends TypeDefinition> buildJunction();

}
