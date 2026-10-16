package lab5.task2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/** Виконує завдання 2 для варіанта 13: групує цілі числа відносно меж a та b. */
public final class Main {
    private static final Path DEFAULT_INPUT =
            Path.of("src", "lab5", "task2", "input.txt");
    private static final Path DEFAULT_OUTPUT =
            Path.of("src", "lab5", "task2", "output.txt");
    private static final int NUMBERS_PER_LINE = 8;

    private Main() {
    }

    /**
     * Формат вхідного файлу: перші два цілі числа — a і b, решта — елементи списку L.
     * Шляхи до вхідного та вихідного файлів можна передати аргументами командного рядка.
     */
    public static void main(String[] args) throws IOException {
        if (args.length > 2) {
            throw new IllegalArgumentException(
                    "Вкажіть не більше двох аргументів: шлях до вхідного та вихідного файлів.");
        }

        Path inputPath = args.length >= 1 ? Path.of(args[0]) : DEFAULT_INPUT;
        Path outputPath = args.length == 2 ? Path.of(args[1]) : DEFAULT_OUTPUT;
        InputData input = readInput(inputPath);

        if (input.a >= input.b) {
            throw new IllegalArgumentException(
                    "Некоректні межі: потрібно, щоб a < b (отримано a="
                            + input.a + ", b=" + input.b + ").");
        }

        List<Integer> result = groupByRange(input.numbers, input.a, input.b);
        writeOutput(outputPath, result);

        System.out.println("Межі: a = " + input.a + ", b = " + input.b);
        System.out.println("Список після групування: " + result);
        System.out.println("Результат записано у файл: " + outputPath.toAbsolutePath());
    }

    /** Зчитує межі a, b та список цілих чисел із текстового файлу. */
    private static InputData readInput(Path inputPath) throws IOException {
        String content = Files.readString(inputPath, StandardCharsets.UTF_8).trim();
        if (content.isEmpty()) {
            throw new IllegalArgumentException("Вхідний файл порожній: " + inputPath);
        }

        String[] tokens = content.split("\\s+");
        if (tokens.length < 2) {
            throw new IllegalArgumentException(
                    "У файлі мають бути задані межі a і b та, за потреби, числа списку L.");
        }

        int a = parseInteger(tokens[0], "a");
        int b = parseInteger(tokens[1], "b");
        List<Integer> numbers = new ArrayList<>();
        for (int i = 2; i < tokens.length; i++) {
            numbers.add(parseInteger(tokens[i], "елемент списку L"));
        }
        return new InputData(a, b, numbers);
    }

    /** Перетворює текстовий токен на ціле число з повідомленням про некоректні дані. */
    private static int parseInteger(String token, String valueDescription) {
        try {
            return Integer.parseInt(token);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "Очікувалося ціле число для " + valueDescription + ", отримано: " + token,
                    exception);
        }
    }

    /**
     * Переглядає початковий список один раз і зберігає порядок чисел
     * у кожній із трьох груп: менші за a, у діапазоні [a, b], більші за b.
     */
    private static List<Integer> groupByRange(List<Integer> numbers, int a, int b) {
        List<Integer> belowA = new ArrayList<>();
        List<Integer> withinRange = new ArrayList<>();
        List<Integer> aboveB = new ArrayList<>();

        for (int number : numbers) {
            if (number < a) {
                belowA.add(number);
            } else if (number <= b) {
                withinRange.add(number);
            } else {
                aboveB.add(number);
            }
        }

        List<Integer> result = new ArrayList<>(numbers.size());
        result.addAll(belowA);
        result.addAll(withinRange);
        result.addAll(aboveB);
        return result;
    }

    /** Записує числа у файл, розміщуючи не більше восьми чисел у кожному рядку. */
    private static void writeOutput(Path outputPath, List<Integer> numbers) throws IOException {
        List<String> lines = new ArrayList<>();
        for (int start = 0; start < numbers.size(); start += NUMBERS_PER_LINE) {
            int end = Math.min(start + NUMBERS_PER_LINE, numbers.size());
            StringBuilder line = new StringBuilder();
            for (int i = start; i < end; i++) {
                if (i > start) {
                    line.append(' ');
                }
                line.append(numbers.get(i));
            }
            lines.add(line.toString());
        }
        Files.write(outputPath, lines, StandardCharsets.UTF_8);
    }

    /** Зчитані межі та елементи вхідного списку. */
    private static final class InputData {
        private final int a;
        private final int b;
        private final List<Integer> numbers;

        private InputData(int a, int b, List<Integer> numbers) {
            this.a = a;
            this.b = b;
            this.numbers = numbers;
        }
    }
}
