package lab6.task2;

/** Спільний контракт базового та кешованого валідаторів. */
public interface EntityValidator {
    ValidationResult validate(Object entity);
}
