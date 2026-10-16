package lab5.task1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/** Узагальнене сховище для зберігання та пошуку елементів будь-якого типу. */
public final class StorageManager<T> {
    /** Внутрішній список елементів сховища. */
    private final List<T> items = new ArrayList<>();

    /** Додає елемент до сховища; null не допускається. */
    public void add(T item) {
        items.add(Objects.requireNonNull(item, "item must not be null"));
    }

    /** Видаляє перший елемент, який відповідає критерію, і повертає результат операції. */
    public boolean removeFirst(Predicate<? super T> criterion) {
        Objects.requireNonNull(criterion, "criterion must not be null");
        // Ітератор дає змогу безпечно видалити елемент під час обходу списку.
        Iterator<T> iterator = items.iterator();
        while (iterator.hasNext()) {
            if (criterion.test(iterator.next())) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    /** Повертає перший елемент, який відповідає критерію, або порожній результат. */
    public Optional<T> findFirst(Predicate<? super T> criterion) {
        Objects.requireNonNull(criterion, "criterion must not be null");
        for (T item : items) {
            if (criterion.test(item)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    /**
     * Відбирає елементи за предикатом звичайним циклом і повертає
     * незмінний список результатів.
     */
    public List<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate must not be null");
        List<T> matches = new ArrayList<>();
        for (T item : items) {
            if (predicate.test(item)) {
                matches.add(item);
            }
        }
        return Collections.unmodifiableList(matches);
    }

    /** Повертає кількість елементів у сховищі. */
    public int size() {
        return items.size();
    }
}
