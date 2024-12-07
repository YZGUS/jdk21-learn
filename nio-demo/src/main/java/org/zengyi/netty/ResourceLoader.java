package org.zengyi.netty;

import java.net.URL;
import java.util.Enumeration;

public class ResourceLoader {
    public static void main(String[] args) {
        ClassLoader classLoader = ResourceLoader.class.getClassLoader();
        try {
            Enumeration<URL> resources = classLoader.getResources("");
            while (resources.hasMoreElements()) {
                // file:/Users/cengyi/Desktop/code/jdk21-learn/nio-demo/target/classes/
                System.out.println(resources.nextElement());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}