package org.zengyi.netty;

import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

public class ResourceLoader {
    public static void main(String[] args) {
        ClassLoader classLoader = ResourceLoader.class.getClassLoader();
        try (final InputStream in = classLoader.getResourceAsStream("/index.html")) {
            if (in == null) {
                System.out.println("not found");
            } else {
                System.out.println("available: " + in.available());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}