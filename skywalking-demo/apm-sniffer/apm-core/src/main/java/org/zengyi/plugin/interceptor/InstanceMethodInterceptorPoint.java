package org.zengyi.plugin.interceptor;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

public interface InstanceMethodInterceptorPoint {

    ElementMatcher<MethodDescription> getInstanceMethodsMatcher();

    String getInstanceInterceptor();
}
