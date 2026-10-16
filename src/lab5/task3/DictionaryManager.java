package lab5.task3;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalDouble;

/** Керує словником значень і видаляє аномалії відносно середнього. */
public final class DictionaryManager {
    private static final MathContext MATH_CONTEXT = MathContext.DECIMAL128;

    private final Map<String, Double> values = new HashMap<>();

    /** Додає нову пару, якщо ключ ще не існує. */
    public boolean add(String key, double value) {
        validateEntry(key, value);
        if (values.containsKey(key)) {
            return false;
        }
        values.put(key, value);
        return true;
    }

    /** Оновлює значення наявного ключа. */
    public boolean update(String key, double value) {
        validateEntry(key, value);
        if (!values.containsKey(key)) {
            return false;
        }
        values.put(key, value);
        return true;
    }

    /** Видаляє пару за ключем і повідомляє, чи існував такий ключ. */
    public boolean remove(String key) {
        Objects.requireNonNull(key, "key must not be null");
        return values.remove(key) != null;
    }

    /** Безпечно отримує значення або повертає задане значення за замовчуванням. */
    public double getOrDefault(String key, double defaultValue) {
        Objects.requireNonNull(key, "key must not be null");
        return values.getOrDefault(key, defaultValue);
    }

    /** Перевіряє, чи міститься ключ у словнику. */
    public boolean containsKey(String key) {
        Objects.requireNonNull(key, "key must not be null");
        return values.containsKey(key);
    }

    /** Повертає значення за ключем як OptionalDouble. */
    public OptionalDouble findValue(String key) {
        Objects.requireNonNull(key, "key must not be null");
        Double value = values.get(key);
        return value == null ? OptionalDouble.empty() : OptionalDouble.of(value);
    }

    /**
     * Видаляє значення, відхилення яких від середнього перевищує заданий відсоток.
     * Повертає список вилучених ключів.
     */
    public List<String> removeOutliers(double percentage) {
        if (!Double.isFinite(percentage) || percentage < 0) {
            throw new IllegalArgumentException("K має бути скінченним невід'ємним числом.");
        }
        if (values.isEmpty()) {
            throw new IllegalStateException("Неможливо обчислити середнє порожнього словника.");
        }

        BigDecimal average = calculateAverage();
        BigDecimal threshold = average.abs()
                .multiply(BigDecimal.valueOf(percentage), MATH_CONTEXT)
                .divide(BigDecimal.valueOf(100), MATH_CONTEXT);

        List<String> removedKeys = new ArrayList<>();
        Iterator<Map.Entry<String, Double>> iterator = values.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Double> entry = iterator.next();
            BigDecimal deviation = BigDecimal.valueOf(entry.getValue())
                    .subtract(average)
                    .abs();
            if (deviation.compareTo(threshold) > 0) {
                removedKeys.add(entry.getKey());
                iterator.remove();
            }
        }
        return Collections.unmodifiableList(removedKeys);
    }

    /** Повертає середнє значення поточного словника. */
    public BigDecimal getAverage() {
        if (values.isEmpty()) {
            throw new IllegalStateException("Неможливо обчислити середнє порожнього словника.");
        }
        return calculateAverage();
    }

    /** Повертає копію пар, упорядкованих за значенням за зростанням, потім за ключем. */
    public List<Map.Entry<String, Double>> getEntriesSortedByValue() {
        List<Map.Entry<String, Double>> sortedEntries = new ArrayList<>();
        for (Map.Entry<String, Double> entry : values.entrySet()) {
            sortedEntries.add(Map.entry(entry.getKey(), entry.getValue()));
        }
        sortedEntries.sort(
                Comparator.comparing(Map.Entry<String, Double>::getValue)
                        .thenComparing(Map.Entry::getKey));
        return Collections.unmodifiableList(sortedEntries);
    }

    /** Повертає кількість пар у словнику. */
    public int size() {
        return values.size();
    }

    private BigDecimal calculateAverage() {
        BigDecimal sum = BigDecimal.ZERO;
        for (double value : values.values()) {
            sum = sum.add(BigDecimal.valueOf(value));
        }
        return sum.divide(BigDecimal.valueOf(values.size()), MATH_CONTEXT);
    }

    private static void validateEntry(String key, double value) {
        Objects.requireNonNull(key, "key must not be null");
        if (key.trim().isEmpty()) {
            throw new IllegalArgumentException("Ключ не може бути порожнім.");
        }
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Значення має бути скінченним числом.");
        }
    }
}
