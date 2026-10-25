package lab6.task3;

import lab6.task1.annotations.HighPowerZoneRule;
import lab6.task1.annotations.InRange;
import lab6.task1.annotations.MatchesRegex;
import lab6.task2.ValidationError;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/** Метаструктура класу, яку створюють один раз і повторно використовують під час валідації. */
public final class ClassValidationMetadata {
    // Комплексне правило, прикріплене до класу, або null, якщо такого правила немає.
    private final HighPowerZoneRule classRule;
    // Підготовлені посилання на поля, зазначені у параметрах класового правила.
    private final Field powerField;
    private final Field zoneField;
    // Поля з атомарними правилами та зчитані для них анотації.
    private final List<FieldValidationMetadata> validatedFields;
    // Проблеми конфігурації анотацій/доступу зберігаються і повертаються при кожній перевірці.
    private final List<ValidationError> configurationErrors;

    private ClassValidationMetadata(
            HighPowerZoneRule classRule,
            Field powerField,
            Field zoneField,
            List<FieldValidationMetadata> validatedFields,
            List<ValidationError> configurationErrors
    ) {
        this.classRule = classRule;
        this.powerField = powerField;
        this.zoneField = zoneField;
        // Копії не дозволяють змінити підготовлені метадані після додавання їх до кешу.
        this.validatedFields = List.copyOf(validatedFields);
        this.configurationErrors = List.copyOf(configurationErrors);
    }

    /** Один раз читає анотації типу та готує поля для подальшого кешованого використання. */
    public static ClassValidationMetadata build(Class<?> type) {
        List<ValidationError> errors = new ArrayList<>();
        HighPowerZoneRule rule = type.getAnnotation(HighPowerZoneRule.class);
        Field powerField = null;
        Field zoneField = null;

        // Класове правило називає поля текстом, тому шукаємо їх під час побудови метаданих.
        if (rule != null) {
            powerField = findAndOpenField(type, rule.powerField(), errors);
            zoneField = findAndOpenField(type, rule.zoneField(), errors);
        }

        List<FieldValidationMetadata> fields = new ArrayList<>();
        for (Field field : type.getDeclaredFields()) {
            // Анотації зберігаються в метаданих, щоб не шукати їх знову на кожному виклику validate.
            MatchesRegex regex = field.getAnnotation(MatchesRegex.class);
            InRange range = field.getAnnotation(InRange.class);
            if (regex == null && range == null) {
                continue;
            }

            if (!openField(field, errors)) {
                continue;
            }

            Pattern compiledPattern = null;
            // Невалідний шаблон є помилкою конфігурації; її повідомимо при перевірці об'єкта.
            if (regex != null) {
                try {
                    compiledPattern = Pattern.compile(regex.pattern());
                } catch (PatternSyntaxException e) {
                    errors.add(new ValidationError(field.getName(),
                            "Некоректний регулярний вираз в @MatchesRegex: " + e.getDescription()));
                }
            }
            fields.add(new FieldValidationMetadata(field, regex, compiledPattern, range));
        }

        return new ClassValidationMetadata(rule, powerField, zoneField, fields, errors);
    }

    /** Шукає за назвою поле, вказане у класовій анотації, і готує його до читання. */
    private static Field findAndOpenField(Class<?> type, String fieldName, List<ValidationError> errors) {
        try {
            Field field = type.getDeclaredField(fieldName);
            return openField(field, errors) ? field : null;
        } catch (NoSuchFieldException e) {
            errors.add(new ValidationError(fieldName, "Поле, вказане у @HighPowerZoneRule, не знайдено"));
            return null;
        } catch (SecurityException e) {
            errors.add(new ValidationError(fieldName, "Доступ до поля заборонено: " + e.getMessage()));
            return null;
        }
    }

    /** Відкриває доступ до private-поля для Reflection і фіксує помилки доступу. */
    private static boolean openField(Field field, List<ValidationError> errors) {
        try {
            if (field.trySetAccessible()) {
                return true;
            }
            errors.add(new ValidationError(field.getName(),
                    "Немає доступу до поля для Reflection-валідації"));
            return false;
        } catch (SecurityException e) {
            errors.add(new ValidationError(field.getName(), "Доступ до поля заборонено: " + e.getMessage()));
            return false;
        }
    }

    /** Повертає анотацію комплексного правила класу. */
    public HighPowerZoneRule classRule() {
        return classRule;
    }

    /** Повертає підготовлене поле потужності для комплексного правила. */
    public Field powerField() {
        return powerField;
    }

    /** Повертає підготовлене поле зони для комплексного правила. */
    public Field zoneField() {
        return zoneField;
    }

    /** Повертає незмінний список метаданих полів, що мають анотації валідації. */
    public List<FieldValidationMetadata> validatedFields() {
        return validatedFields;
    }

    /** Повертає помилки підготовки метаданих, наприклад некоректне ім'я поля. */
    public List<ValidationError> configurationErrors() {
        return configurationErrors;
    }
}
