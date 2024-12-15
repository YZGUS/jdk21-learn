package org.zengyi.plugin.matcher;

import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.annotation.AnnotationList;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.isAnnotatedWith;
import static net.bytebuddy.matcher.ElementMatchers.named;

public class ClassAnnotationNamedMatcher implements IndirectMatcher {

    private List<String> annotationNames;

    public ClassAnnotationNamedMatcher(String[] annotationNames) {
        this.annotationNames = Arrays.asList(annotationNames);
    }

    @Override
    public ElementMatcher.Junction<? super TypeDescription> buildJunction() {
        ElementMatcher.Junction<? super TypeDescription> junction = null;
        for (String annotationName : annotationNames) {
            if (junction == null) {
                junction = isAnnotatedWith(named(annotationName));
            } else {
                junction = junction.and(isAnnotatedWith(named(annotationName)));
            }
        }
        return junction;
    }

    @Override
    public boolean isMatch(TypeDescription typeDescription) {
        final AnnotationList annotations = typeDescription.getInheritedAnnotations();
        final ArrayList<String> tmpAnnationNames = new ArrayList<>(annotationNames);
        for (AnnotationDescription annotation : annotations) {
            tmpAnnationNames.remove(annotation.getAnnotationType().getActualName());
        }
        return tmpAnnationNames.isEmpty();
    }

    public static IndirectMatcher byAnnotationNames(String... annotationNames) {
        return new ClassAnnotationNamedMatcher(annotationNames);
    }
}
