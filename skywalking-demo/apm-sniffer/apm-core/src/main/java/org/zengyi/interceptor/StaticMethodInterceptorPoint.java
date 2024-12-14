package org.zengyi.interceptor;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

public interface StaticMethodInterceptorPoint {

    ElementMatcher<MethodDescription> getStaticMethodMatcher();

    String getStaticInterceptor();
}
