package org.example;

import com.sun.source.util.Plugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Ваша задача написать загрузчик плагинов в ваше приложение. Допустим вы пишите свою игру, и хотите чтобы люди имели
 * возможность писать плагины для неё. Соответственно, разные разработчики могут назвать свои классы одинаковым
 * именем, ваш загрузчик должен корректно это обрабатывать.
 */
public class PluginManager {
    private final String pluginRootDirectory; // корневой каталог плагина
    private final Map<String, ClassLoader> pluginClassLoaders = new HashMap<>();

    public PluginManager(String pluginRootDirectory) {
        this.pluginRootDirectory = pluginRootDirectory;
    }

    public Plugin load(String pluginName, String pluginClassName) {
        try {
            ClassLoader classLoader = getOrCreateClassLoader(pluginName);
            Class<?> pluginClass = classLoader.loadClass(pluginClassName);
            Object pluginInstance = pluginClass.getDeclaredConstructor().newInstance();
            if (pluginInstance instanceof Plugin) {
                return (Plugin) pluginInstance;
            } else {
                throw new IllegalArgumentException("Class " + pluginClassName + " does not implement Plugin interface");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load plugin", e);
        }
    }

    private ClassLoader getOrCreateClassLoader(String pluginName) throws IOException {
        if (pluginClassLoaders.containsKey(pluginName)) {
            return pluginClassLoaders.get(pluginName);
        }

        Path pluginPath = Paths.get(pluginRootDirectory, pluginName);
        if (!Files.exists(pluginPath) || !Files.isDirectory(pluginPath)) {
            throw new IllegalArgumentException("Plugin directory not found: " + pluginPath);
        }

        File[] jarFiles = pluginPath.toFile().listFiles(file -> file.isFile() && file.getName().endsWith(".jar"));
        if (jarFiles == null || jarFiles.length == 0) {
            throw new IllegalArgumentException("No JAR files found in plugin directory: " + pluginPath);
        }

        URL[] jarUrls = new URL[jarFiles.length];
        for (int i = 0; i < jarFiles.length; i++) {
            jarUrls[i] = jarFiles[i].toURI().toURL();
        }

        ClassLoader classLoader = new PluginClassLoader(jarUrls, getClass().getClassLoader());
        pluginClassLoaders.put(pluginName, classLoader);
        return classLoader;
    }

    private static class PluginClassLoader extends ClassLoader {
        private final URL[] urls;

        public PluginClassLoader(URL[] urls, ClassLoader parent) {
            super(parent);
            this.urls = urls;
        }

        @Override
        protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            // Check if the class has already been loaded
            Class<?> loadedClass = findLoadedClass(name);
            if (loadedClass == null) {
                try {
                    // Attempt to load the class from plugin's JARs
                    for (URL url : urls) {
                        try (URLClassLoader jarClassLoader = new URLClassLoader(new URL[]{url}, null)) {
                            loadedClass = jarClassLoader.loadClass(name);
                            if (loadedClass != null) {
                                break;
                            }
                        }
                    }
                } catch (Exception ignored) {
                }

                if (loadedClass == null) {
                    // Fallback to parent class loader
                    loadedClass = super.loadClass(name, false);
                }
            }

            if (resolve) {
                resolveClass(loadedClass);
            }
            return loadedClass;
        }
    }
}
