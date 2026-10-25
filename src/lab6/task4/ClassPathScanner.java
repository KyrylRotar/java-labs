package lab6.task4;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/** Знаходить класи в заданому пакеті як у директорії, так і в JAR. */
public final class ClassPathScanner {
    private ClassPathScanner() {
    }

    public static List<Class<?>> scan(String packageName) throws IOException, ClassNotFoundException {
        String packagePath = packageName.replace('.', '/');
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = loader.getResources(packagePath);
        List<String> classNames = new ArrayList<>();

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            switch (resource.getProtocol()) {
                case "file" -> scanDirectory(resource, packageName, classNames);
                case "jar" -> scanJar(resource, packagePath, classNames);
                default -> throw new IOException("Непідтримуваний протокол classpath: " + resource);
            }
        }

        List<Class<?>> classes = new ArrayList<>();
        for (String className : classNames.stream().distinct().sorted().toList()) {
            // false: статичні ініціалізатори не запускаються під час сканування.
            classes.add(Class.forName(className, false, loader));
        }
        return List.copyOf(classes);
    }

    private static void scanDirectory(URL resource, String packageName, List<String> classNames)
            throws IOException {
        final Path directory;
        try {
            directory = Path.of(resource.toURI());
        } catch (URISyntaxException e) {
            throw new IOException("Некоректний шлях до пакета: " + resource, e);
        }

        try (var paths = Files.walk(directory)) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".class"))
                    .forEach(path -> {
                        String relativeName = directory.relativize(path).toString();
                        String suffix = relativeName.substring(0, relativeName.length() - ".class".length())
                                .replace('\\', '.')
                                .replace('/', '.');
                        if (!suffix.contains("$")) {
                            classNames.add(packageName + (suffix.isEmpty() ? "" : "." + suffix));
                        }
                    });
        }
    }

    private static void scanJar(URL resource, String packagePath, List<String> classNames) throws IOException {
        JarURLConnection connection = (JarURLConnection) resource.openConnection();
        String packagePrefix = packagePath.endsWith("/") ? packagePath : packagePath + "/";
        try (JarFile jar = connection.getJarFile()) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                String entryName = entries.nextElement().getName();
                if (!entryName.startsWith(packagePrefix)
                        || !entryName.endsWith(".class")
                        || entryName.contains("$")) {
                    continue;
                }
                classNames.add(entryName.substring(0, entryName.length() - ".class".length())
                        .replace('/', '.'));
            }
        }
    }
}
