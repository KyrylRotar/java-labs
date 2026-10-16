package lab5.task1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public final class StorageManager<T> {
    private final List<T> items = new ArrayList<>();

    public void add(T item) {
        items.add(Objects.requireNonNull(item, "item must not be null"));
    }

    public boolean removeFirst(Predicate<? super T> criterion) {
        Objects.requireNonNull(criterion, "criterion must not be null");
        Iterator<T> iterator = items.iterator();
        while (iterator.hasNext()) {
            if (criterion.test(iterator.next())) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    public Optional<T> findFirst(Predicate<? super T> criterion) {
        Objects.requireNonNull(criterion, "criterion must not be null");
        for (T item : items) {
            if (criterion.test(item)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

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

    public int size() {
        return items.size();
    }
}
