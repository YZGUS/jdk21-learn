package org.zengyi.plugin.loader;

import org.apache.commons.io.IOUtils;
import org.zengyi.boot.AgentPackagePath;
import org.zengyi.plugin.PluginBootstarp;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

// 自定义类加载器
public class AgentClassLoader extends ClassLoader {

    private static ClassLoader DEFAULT_CLASS_LOADER;

    private List<File> classPath;

    private List<Jar> allJars;

    private ReentrantLock jarScanLock = new ReentrantLock();

    public AgentClassLoader(ClassLoader parent) {
        super(parent);
        final File agentJarDir = AgentPackagePath.getPath();
        this.classPath = new ArrayList<>();
        this.classPath.add(new File(agentJarDir, "plugins"));
    }

    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        final List<Jar> allJars = getAllJars();
        // . 转为 /， 获取类对应的文件路径
        final String path = className.replace(".", "/").concat(".class");
        for (Jar jar : allJars) {
            final JarEntry jarEntry = jar.jarFile.getJarEntry(path);
            if (jarEntry == null) {
                continue;
            }

            try {
                final URL url = new URL("jar:file:" + jar.sourceFile.getAbsolutePath() + "!/" + path);
                byte[] classBytes = IOUtils.toByteArray(url);
                return defineClass(className, classBytes, 0, classBytes.length);
            } catch (Throwable t) {
                t.printStackTrace();
            }
            throw new ClassNotFoundException("can't find class: " + className);
        }
        return null;
    }

    private List<Jar> getAllJars() {
        if (allJars != null) {
            return allJars;
        }

        jarScanLock.lock();
        try {
            if (allJars == null) {
                allJars = doGetJars();
            }
        } finally {
            jarScanLock.unlock();
        }
        return allJars;
    }

    private List<Jar> doGetJars() {
        final LinkedList<Jar> list = new LinkedList<>();
        for (File path : classPath) {
            if (path.exists() && path.isAbsolute()) {
                final String[] jarFilenames = path.list((dir, name) -> name.endsWith(".jar"));
                if (jarFilenames == null || jarFilenames.length == 0) {
                    continue;
                }

                for (String jarFilename : jarFilenames) {
                    try {
                        final File jarSource = new File(path, jarFilename);
                        final Jar jar = new Jar(new JarFile(jarSource), jarSource);
                        list.add(jar);
                        System.out.println("jar: " + jarSource.getAbsolutePath() + " loaded");
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        }
        return list;
    }


    public static void initDefaultClassLoader() {
        if (DEFAULT_CLASS_LOADER == null) {
            DEFAULT_CLASS_LOADER = new AgentClassLoader(PluginBootstarp.class.getClassLoader());
        }
    }

    public static class Jar {

        // jar 文件对应的 jarFile 对象
        private final JarFile jarFile;

        // jar 文件对象
        private final File sourceFile;

        public Jar(JarFile jarFile, File sourceFile) {
            this.jarFile = jarFile;
            this.sourceFile = sourceFile;
        }

        public JarFile getJarFile() {
            return jarFile;
        }

        public File getSourceFile() {
            return sourceFile;
        }
    }
}
