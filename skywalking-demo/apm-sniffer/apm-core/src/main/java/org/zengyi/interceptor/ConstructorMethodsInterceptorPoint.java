package org.zengyi.interceptor;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

public interface ConstructorMethodsInterceptorPoint {

    ElementMatcher<MethodDescription> getConstructorMatcher();

    String getConstructorInterceptor();
}
