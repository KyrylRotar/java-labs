package lab6.task2;

import java.util.ArrayList;
import java.util.List;

/** Зберігає результат перевірки та список помилок. */
public final class ValidationResult {
    private final List<ValidationError> errors = new ArrayList<>();

    public boolean isValid() {
        return errors.isEmpty();
    }

    /** Повертає незмінний знімок списку помилок. */
    public List<ValidationError> getErrors() {
        return List.copyOf(errors);
    }

    public void addError(String fieldName, String message) {
        errors.add(new ValidationError(fieldName, message));
    }
}
