package org.zengyi;

import org.zengyi.matcher.ClassMatcher;

import static org.zengyi.matcher.ClassAnnotationNamedMatcher.byAnnotationNames;

public class SpringMVCRestControllerInstrumentation extends SpringMVCCommonInstrumentation {

    @Override
    protected ClassMatcher enhanceClass() {
        return byAnnotationNames("org.springframework.web.bind.annotation.RestController");
    }
}
