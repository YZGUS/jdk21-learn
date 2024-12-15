package org.zengyi.plugin.interceptor.enhance;

public class EnhanceContext {

    // 是否被增强了
    private boolean isEnhanced = false;

    // 是否新增了属性
    private boolean objectEnhanced = false;

    public boolean isEnhanced() {
        return isEnhanced;
    }

    public void initializationStageCompleted() {
        isEnhanced = true;
    }

    public boolean isObjectEnhanced() {
        return objectEnhanced;
    }

    public void objectEnhancedCompleted() {
        this.objectEnhanced = true;
    }
}
