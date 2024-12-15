package org.zengyi.plugin;

import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import org.zengyi.plugin.matcher.ClassMatcher;
import org.zengyi.plugin.matcher.IndirectMatcher;
import org.zengyi.plugin.matcher.NamedMatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PluginFinder {

    private final Map<String, List<AbstractClassEnhancePluginDefine>> namedPluginDefineMap = new HashMap<>();

    private final List<AbstractClassEnhancePluginDefine> inderictPluginDefineList = new ArrayList<>();

    public PluginFinder(List<AbstractClassEnhancePluginDefine> pluginDefines) {
        for (AbstractClassEnhancePluginDefine pluginDefine : pluginDefines) {
            final ClassMatcher classMatcher = pluginDefine.enhanceClass();
            if (classMatcher == null) {
                continue;
            }

            if (classMatcher instanceof NamedMatcher) {
                NamedMatcher namedMatcher = (NamedMatcher) classMatcher;
                namedPluginDefineMap.computeIfAbsent(namedMatcher.getClassName(), k -> new ArrayList<>()).add(pluginDefine);
            } else {
                inderictPluginDefineList.add(pluginDefine);
            }
        }
    }

    public ElementMatcher<? super TypeDescription> buildMatch() {
        // 泛型的 super 和 extends : https://zhuanlan.zhihu.com/p/249187830
        final ElementMatcher.Junction<? super TypeDescription> junction = new ElementMatcher.Junction.AbstractBase<NamedElement>() {
            @Override
            public boolean matches(NamedElement target) {
                return namedPluginDefineMap.containsKey(target.getActualName());
            }
        };
        for (AbstractClassEnhancePluginDefine pluginDefine : inderictPluginDefineList) {
            final ClassMatcher classMatcher = pluginDefine.enhanceClass();
            if (classMatcher instanceof IndirectMatcher) {
                IndirectMatcher indirectMatcher = (IndirectMatcher) classMatcher;
                junction.or(indirectMatcher.buildJunction());
            }
        }
        return junction;
    }

    public List<AbstractClassEnhancePluginDefine> find(TypeDescription typeDescription) {
        final String actualName = typeDescription.getActualName();
        final List<AbstractClassEnhancePluginDefine> matchPlugins = namedPluginDefineMap.get(actualName);
        if (namedPluginDefineMap.containsKey(actualName)) {
            matchPlugins.addAll(namedPluginDefineMap.get(actualName));
        }

        for (AbstractClassEnhancePluginDefine pluginDefine : inderictPluginDefineList) {
            final IndirectMatcher indirectMatcher = (IndirectMatcher) pluginDefine.enhanceClass();
            if (indirectMatcher.isMatch(typeDescription)) {
                matchPlugins.add(pluginDefine);
            }
        }
        return matchPlugins;
    }
}
