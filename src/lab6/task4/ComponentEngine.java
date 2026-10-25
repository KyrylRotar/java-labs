package lab6.task4;

import lab6.task4.annotations.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

/** Динамічно створює, налаштовує та запускає позначені @Component класи. */
public final class ComponentEngine {
    private ComponentEngine() {
    }

    public static void run(String componentsPackage) throws Exception {
        // Сканер повертає класи пакета; обробляємо лише позначені анотацією @Component.
        List<Class<?>> classes = ClassPathScanner.scan(componentsPackage);
        int processed = 0;
        System.out.println("Знайдено класів: " + classes.size());

        for (Class<?> type : classes) {
            Component component = type.getAnnotation(Component.class);
            if (component == null) {
                System.out.println("- " + type.getSimpleName() + ": без @Component, пропущено");
                continue;
            }

            // Створюємо об'єкт без знання його конкретного типу на етапі компіляції.
            Object instance = createInstance(type);
            // Для демонстрації встановлюємо значення єдиного поля екземпляра, враховуючи його тип.
            Field field = findInstanceField(type);
            Object value = sampleValue(field.getType(), component.name());

            if (!field.trySetAccessible()) {
                throw new IllegalAccessException("Немає доступу до поля " + field.getName()
                        + " у " + type.getName());
            }

            System.out.println("\n== " + type.getSimpleName()
                    + " (name=" + component.name() + ", version=" + component.version() + ") ==");
            System.out.println("  Створено:        " + instance);
            // Reflection дозволяє записати значення у private-поле компонента.
            field.set(instance, value);
            System.out.println("  Встановлено поле " + field.getName() + " = " + value);

            // Метод дії знаходиться та викликається динамічно для кожного компонента.
            Method action = type.getDeclaredMethod("execute");
            if (!action.trySetAccessible()) {
                throw new IllegalAccessException("Немає доступу до методу execute() у " + type.getName());
            }
            invokeAction(action, instance);
            System.out.println("  Підсумковий стан: " + instance);
            processed++;
        }

        System.out.println("\nОброблено компонентів: " + processed + " із " + classes.size());
    }

    private static Object createInstance(Class<?> type) throws ReflectiveOperationException {
        // За умовою кожен компонент має конструктор без параметрів.
        Constructor<?> constructor = type.getDeclaredConstructor();
        if (!constructor.trySetAccessible()) {
            throw new IllegalAccessException("Немає доступу до конструктора " + type.getName());
        }
        return constructor.newInstance();
    }

    private static Field findInstanceField(Class<?> type) throws NoSuchFieldException {
        // Ігноруємо статичні та синтетичні поля, щоб змінювати саме стан об'єкта.
        for (Field field : type.getDeclaredFields()) {
            if (!field.isSynthetic() && !Modifier.isStatic(field.getModifiers())) {
                return field;
            }
        }
        throw new NoSuchFieldException("Не знайдено екземплярне поле в " + type.getName());
    }

    private static Object sampleValue(Class<?> fieldType, String componentName) {
        // Підбираємо тестові значення для типів, використаних у прикладах компонентів.
        if (fieldType == String.class) {
            return componentName + "-demo";
        }
        if (fieldType == int.class || fieldType == Integer.class) {
            return 75;
        }
        if (fieldType == double.class || fieldType == Double.class) {
            return 22.5;
        }
        if (fieldType == boolean.class || fieldType == Boolean.class) {
            return true;
        }
        throw new IllegalArgumentException("Не підтримується тип поля: " + fieldType.getName());
    }

    private static void invokeAction(Method action, Object instance) throws Exception {
        try {
            action.invoke(instance);
        } catch (InvocationTargetException e) {
            // Reflection обгортає виняток з execute(); повторно кидаємо початкову причину.
            Throwable cause = e.getCause();
            if (cause instanceof Exception exception) {
                throw exception;
            }
            if (cause instanceof Error error) {
                throw error;
            }
            throw new IllegalStateException("Метод execute() завершився помилкою", cause);
        }
    }
}
