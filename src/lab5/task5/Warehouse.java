package lab5.task5;

import java.util.Objects;

/** Склад, з яким пов'язані пристрої. */
public final class Warehouse {
    private final String id;
    private final String name;

    public Warehouse(String id, String name) {
        this.id = requireText(id, "id");
        this.name = requireText(name, "name");
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " не може бути null.");
        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " не може бути порожнім.");
        }
        return value;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Warehouse{id='" + id + "', name='" + name + "'}";
    }
}
