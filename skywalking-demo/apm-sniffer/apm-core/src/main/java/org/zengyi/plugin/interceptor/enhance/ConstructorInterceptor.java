package org.zengyi.plugin.interceptor.enhance;

public interface ConstructorInterceptor {

    void onConstructor(Object objInstance, Object[] args);
}
