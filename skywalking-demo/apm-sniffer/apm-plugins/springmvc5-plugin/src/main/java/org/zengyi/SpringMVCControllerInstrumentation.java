package org.zengyi;

import org.zengyi.plugin.matcher.ClassMatcher;

import static org.zengyi.plugin.matcher.ClassAnnotationNamedMatcher.byAnnotationNames;

public class SpringMVCControllerInstrumentation extends SpringMVCCommonInstrumentation {

    @Override
    protected ClassMatcher enhanceClass() {
        return byAnnotationNames("org.springframework.stereotype.Controller");
    }
}
