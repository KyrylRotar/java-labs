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
        // ClassLoader очікує шлях із роздільниками '/', а не крапками.
        String packagePath = packageName.replace('.', '/');
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = loader.getResources(packagePath);
        List<String> classNames = new ArrayList<>();

        // У файловій системі та в JAR ресурси мають різний формат, тому обробляємо обидва окремо.
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            switch (resource.getProtocol()) {
                case "file" -> scanDirectory(resource, packageName, classNames);
                case "jar" -> scanJar(resource, packagePath, classNames);
                default -> throw new IOException("Непідтримуваний протокол classpath: " + resource);
            }
        }

        List<Class<?>> classes = new ArrayList<>();
        // Сортування робить порядок виводу стабільним, distinct усуває дублікати ресурсів.
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
            // Перетворення через URI коректно обробляє пробіли та спеціальні символи в шляху.
            directory = Path.of(resource.toURI());
        } catch (URISyntaxException e) {
            throw new IOException("Некоректний шлях до пакета: " + resource, e);
        }

        try (var paths = Files.walk(directory)) {
            // Рекурсивно шукаємо class-файли, зокрема у вкладених підпакетах.
            paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".class"))
                    .forEach(path -> {
                        String relativeName = directory.relativize(path).toString();
                        String suffix = relativeName.substring(0, relativeName.length() - ".class".length())
                                .replace('\\', '.')
                                .replace('/', '.');
                        // Внутрішні та анонімні класи не є окремими компонентами для сканування.
                        if (!suffix.contains("$")) {
                            classNames.add(packageName + (suffix.isEmpty() ? "" : "." + suffix));
                        }
                    });
        }
    }

    private static void scanJar(URL resource, String packagePath, List<String> classNames) throws IOException {
        JarURLConnection connection = (JarURLConnection) resource.openConnection();
        // Слеш наприкінці гарантує, що не потраплять класи з пакета зі схожим префіксом.
        String packagePrefix = packagePath.endsWith("/") ? packagePath : packagePath + "/";
        try (JarFile jar = connection.getJarFile()) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                String entryName = entries.nextElement().getName();
                // Залишаємо тільки класи потрібного пакета та пропускаємо внутрішні класи.
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
