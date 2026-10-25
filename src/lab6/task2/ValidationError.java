package lab6.task2;

/** Описує помилку валідації одного поля або класового правила. */
public record ValidationError(String fieldName, String message) {
}
