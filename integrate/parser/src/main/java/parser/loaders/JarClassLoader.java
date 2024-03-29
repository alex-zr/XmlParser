package parser.loaders;

import parser.common.LogicException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by IntelliJ IDEA.
 * User: al1
 * Date: 01.03.12
 */
public class JarClassLoader {
    private final ZipFile file;
    private static String CLASS_EXTENSION = ".class";
    private static String SLASH = "/";
    private static final String DOT = ".";

    public JarClassLoader(String filename) {
        if(filename == null) {
            throw new LogicException("Jar file name is null");
        }

        try {
            this.file = new ZipFile(filename);
        } catch (IOException e) {
            throw new LogicException(e);
        }
    }

    @Deprecated
    public List<Class> getClasses() {
        List<Class> classes = new ArrayList<Class>();
        try {
            URL[] urls = {new URL("jar:file:" + file.getName() + "!/")};
            URLClassLoader urlClassLoader = URLClassLoader.newInstance(urls);
            List<String> classNamesFromJar = getClassNamesFromJar();
            for (String name : classNamesFromJar) {
                Class<?> aClass = urlClassLoader.loadClass(classPathToClassName(name));
                classes.add(aClass);
            }
        } catch (MalformedURLException e) {
            throw new LogicException("Wrong file name [" +file.getName()+ "]", e);
        } catch (ClassNotFoundException e) {
            throw new LogicException("There is no file in Jar", e);
        }

        return classes;
    }

    public Map<String, Class> getClassesMap() {
        Map<String, Class> classesMap = new HashMap<String, Class>();
        try {
            URL[] urls = {new URL("jar:file:" + file.getName() + "!/")};
            URLClassLoader urlClassLoader = URLClassLoader.newInstance(urls);
            List<String> classNamesFromJar = getClassNamesFromJar();
            for (String name : classNamesFromJar) {
                String className = classPathToClassName(name);
                Class<?> aClass = urlClassLoader.loadClass(className);
                classesMap.put(classNameToSimpleName(name), aClass);
            }
        } catch (MalformedURLException e) {
            throw new LogicException("Wrong file name [" +file.getName()+ "]", e);
        } catch (ClassNotFoundException e) {
            throw new LogicException("There is no file in Jar", e);
        }

        return classesMap;
    }

    private static String classPathToClassName(String entry) {
        String name = entry.replace(CLASS_EXTENSION, "");
        return name.replace("/", ".");
    }

    private List<String> getClassNamesFromJar() {
        List<String> names = new ArrayList<String>();
        Enumeration<? extends ZipEntry> entries = this.file.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (entry.getName().contains(CLASS_EXTENSION)) {
                names.add(entry.getName());
            }
        }
        return names;
    }

    private String classNameToSimpleName(String name) {
        int start = 0;

        if(name.contains(SLASH)) {
            start = name.lastIndexOf(SLASH) + 1;
        }
        if(!name.contains(DOT)) {
            throw new IllegalArgumentException("class name should contains .class");
        }
        return name.substring(start, name.lastIndexOf(DOT));
    }
}
