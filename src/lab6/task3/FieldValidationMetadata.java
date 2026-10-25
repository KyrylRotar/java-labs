package lab6.task3;

import lab6.task1.annotations.InRange;
import lab6.task1.annotations.MatchesRegex;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

/** Кешовані метадані одного поля, включно зі скомпільованим регулярним виразом. */
public record FieldValidationMetadata(
        // Field повторно використовується для читання даних без повторного пошуку через Reflection.
        Field field,
        // Зберігаємо саму анотацію, щоб отримувати її межі та повідомлення про помилку.
        MatchesRegex regex,
        // Regex компілюється один раз під час підготовки метаданих, а не на кожній перевірці.
        Pattern compiledPattern,
        // Анотація з межами числового поля; значення перевіряється для кожного об'єкта окремо.
        InRange range
) {
}
