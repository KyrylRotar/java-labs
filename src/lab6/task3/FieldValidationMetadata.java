package lab6.task3;

import lab6.task1.annotations.InRange;
import lab6.task1.annotations.MatchesRegex;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

/** Кешовані метадані одного поля, включно зі скомпільованим регулярним виразом. */
public record FieldValidationMetadata(
        Field field,
        MatchesRegex regex,
        Pattern compiledPattern,
        InRange range
) {
}
