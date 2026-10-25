package lab6.task3;

/** Точка запуску Завдання 3 — порівняльний бенчмарк валідаторів. */
public final class main {
    // Утилітний клас не створюється: запуск відбувається через статичний main.
    private main() {
    }

    public static void main(String[] args) {
        // Передаємо запуск бенчмарку, щоб мати окрему просту точку входу для пакета task3.
        ValidationBenchmark.main(args);
    }
}
