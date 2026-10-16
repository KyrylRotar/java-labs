package lab5.task3;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

/** Виконує очищення словника від аномальних значень (завдання 3, варіант 13). */
public final class Main {
    private static final Path DEFAULT_INPUT =
            Path.of("src", "lab5", "task3", "input.txt");
    private static final Path DEFAULT_OUTPUT =
            Path.of("src", "lab5", "task3", "output.txt");

    private Main() {
    }

    /**
     * Формат файлу: перший рядок — K у відсотках; далі кожен рядок має формат key;value.
     * Шляхи до вхідного й вихідного файлів можна передати аргументами командного рядка.
     */
    public static void main(String[] args) throws IOException {
        if (args.length > 2) {
            throw new IllegalArgumentException(
                    "Вкажіть не більше двох аргументів: шлях до вхідного та вихідного файлів.");
        }

        Path inputPath = args.length >= 1 ? Path.of(args[0]) : DEFAULT_INPUT;
        Path outputPath = args.length == 2 ? Path.of(args[1]) : DEFAULT_OUTPUT;
        InputData input = readInput(inputPath);
        DictionaryManager dictionary = input.dictionary;

        System.out.println("Кількість записів до очищення: " + dictionary.size());
        System.out.println("Середнє значення: " + dictionary.getAverage().toPlainString());

        // Приклад безпечного пошуку: відсутній ключ не спричиняє помилку.
        String sampleKey = "device-001";
        OptionalDouble sampleValue = dictionary.findValue(sampleKey);
        if (sampleValue.isPresent()) {
            System.out.println("Значення для " + sampleKey + ": " + sampleValue.getAsDouble());
        } else {
            System.out.println("Ключ " + sampleKey + " відсутній у словнику.");
        }
        System.out.println("Безпечне читання невідомого ключа: "
                + dictionary.getOrDefault("unknown-key", 0.0));

        List<String> removedKeys = dictionary.removeOutliers(input.percentage);
        writeOutput(outputPath, dictionary.getEntriesSortedByValue());

        System.out.println("Вилучені аномальні ключі: "
                + (removedKeys.isEmpty() ? "немає" : removedKeys));
        System.out.println("Кількість записів після очищення: " + dictionary.size());
        System.out.println("Результат записано у файл: " + outputPath.toAbsolutePath());
    }

    /** Зчитує поріг K та пари ключ-значення з текстового файлу. */
    private static InputData readInput(Path inputPath) throws IOException {
        List<String> lines = Files.readAllLines(inputPath, StandardCharsets.UTF_8);
        int firstDataLine = 0;
        while (firstDataLine < lines.size() && lines.get(firstDataLine).trim().isEmpty()) {
            firstDataLine++;
        }
        if (firstDataLine == lines.size()) {
            throw new IllegalArgumentException("Вхідний файл порожній: " + inputPath);
        }

        String thresholdLine = lines.get(firstDataLine).trim();
        if (thresholdLine.startsWith("K=") || thresholdLine.startsWith("k=")) {
            thresholdLine = thresholdLine.substring(2).trim();
        }
        double percentage = parseFiniteDouble(thresholdLine, "поріг K");
        if (percentage < 0) {
            throw new IllegalArgumentException("Поріг K не може бути від'ємним.");
        }

        DictionaryManager dictionary = new DictionaryManager();
        for (int lineIndex = firstDataLine + 1; lineIndex < lines.size(); lineIndex++) {
            String line = lines.get(lineIndex).trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] parts = line.split(";", -1);
            if (parts.length != 2) {
                throw new IllegalArgumentException(
                        "Рядок " + (lineIndex + 1) + " має містити пару у форматі key;value.");
            }

            String key = parts[0].trim();
            double value = parseFiniteDouble(parts[1].trim(),
                    "значення у рядку " + (lineIndex + 1));
            if (!dictionary.add(key, value)) {
                throw new IllegalArgumentException(
                        "Дубльований ключ у рядку " + (lineIndex + 1) + ": " + key);
            }
        }

        if (dictionary.size() == 0) {
            throw new IllegalArgumentException("Вхідний файл не містить жодної пари key;value.");
        }
        return new InputData(percentage, dictionary);
    }

    /** Перетворює текст на скінченне число та повідомляє про помилку формату. */
    private static double parseFiniteDouble(String text, String description) {
        try {
            double value = Double.parseDouble(text);
            if (!Double.isFinite(value)) {
                throw new IllegalArgumentException(description + " має бути скінченним числом.");
            }
            return value;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "Некоректне число для " + description + ": " + text, exception);
        }
    }

    /** Записує очищений словник у файл, відсортований за значенням за зростанням. */
    private static void writeOutput(
            Path outputPath, List<Map.Entry<String, Double>> sortedEntries) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Map.Entry<String, Double> entry : sortedEntries) {
            lines.add(entry.getKey() + ";" + entry.getValue());
        }
        Files.write(outputPath, lines, StandardCharsets.UTF_8);
    }

    /** Зчитаний поріг і початковий словник. */
    private static final class InputData {
        private final double percentage;
        private final DictionaryManager dictionary;

        private InputData(double percentage, DictionaryManager dictionary) {
            this.percentage = percentage;
            this.dictionary = dictionary;
        }
    }
}
