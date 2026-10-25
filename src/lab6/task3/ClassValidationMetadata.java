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
    private final HighPowerZoneRule classRule;
    private final Field powerField;
    private final Field zoneField;
    private final List<FieldValidationMetadata> validatedFields;
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
        this.validatedFields = List.copyOf(validatedFields);
        this.configurationErrors = List.copyOf(configurationErrors);
    }

    public static ClassValidationMetadata build(Class<?> type) {
        List<ValidationError> errors = new ArrayList<>();
        HighPowerZoneRule rule = type.getAnnotation(HighPowerZoneRule.class);
        Field powerField = null;
        Field zoneField = null;

        if (rule != null) {
            powerField = findAndOpenField(type, rule.powerField(), errors);
            zoneField = findAndOpenField(type, rule.zoneField(), errors);
        }

        List<FieldValidationMetadata> fields = new ArrayList<>();
        for (Field field : type.getDeclaredFields()) {
            MatchesRegex regex = field.getAnnotation(MatchesRegex.class);
            InRange range = field.getAnnotation(InRange.class);
            if (regex == null && range == null) {
                continue;
            }

            if (!openField(field, errors)) {
                continue;
            }

            Pattern compiledPattern = null;
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

    public HighPowerZoneRule classRule() {
        return classRule;
    }

    public Field powerField() {
        return powerField;
    }

    public Field zoneField() {
        return zoneField;
    }

    public List<FieldValidationMetadata> validatedFields() {
        return validatedFields;
    }

    public List<ValidationError> configurationErrors() {
        return configurationErrors;
    }
}
