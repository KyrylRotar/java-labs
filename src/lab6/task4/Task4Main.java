package lab6.task4;

/** Точка входу для Завдання 4. */
public final class Task4Main {
    // Пакет, який сканується для пошуку компонентів розумного дому.
    private static final String COMPONENTS_PACKAGE = "lab6.task4.components";

    private Task4Main() {
    }

    public static void main(String[] args) throws Exception {
        // Запускаємо сканування, динамічне створення, налаштування та виклик компонентів.
        ComponentEngine.run(COMPONENTS_PACKAGE);
    }
}
