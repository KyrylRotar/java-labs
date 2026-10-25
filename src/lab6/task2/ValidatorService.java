package lab6.task2;

import lab6.task1.annotations.HighPowerZoneRule;
import lab6.task1.annotations.InRange;
import lab6.task1.annotations.MatchesRegex;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

/** Перевіряє анотації доменної моделі за допомогою Reflection. */
public class ValidatorService implements EntityValidator {
    @Override
    public ValidationResult validate(Object entity) {
        ValidationResult result = new ValidationResult();
        if (entity == null) {
            result.addError("entity", "Об'єкт для перевірки не може бути null");
            return result;
        }

        Class<?> type = entity.getClass();
        validateClassRule(entity, type, result);
        validateFields(entity, type, result);
        return result;
    }

    private void validateClassRule(Object entity, Class<?> type, ValidationResult result) {
        HighPowerZoneRule rule = type.getAnnotation(HighPowerZoneRule.class);
        if (rule == null) {
            return;
        }

        Field powerField = findField(type, rule.powerField(), result);
        Field zoneField = findField(type, rule.zoneField(), result);
        if (powerField == null || zoneField == null) {
            return;
        }

        Object powerValue = readField(powerField, entity, result);
        Object zoneValue = readField(zoneField, entity, result);
        if (powerValue == null || zoneValue == null) {
            return;
        }

        if (!(powerValue instanceof Number power)) {
            result.addError(rule.powerField(), "Поле правила має бути числовим");
            return;
        }
        if (!(zoneValue instanceof String zone)) {
            result.addError(rule.zoneField(), "Поле зони у класовому правилі має бути рядком");
            return;
        }

        if (power.doubleValue() > rule.powerThreshold() && !rule.requiredZone().equals(zone)) {
            result.addError(rule.zoneField(), rule.message());
        }
    }

    private void validateFields(Object entity, Class<?> type, ValidationResult result) {
        for (Field field : type.getDeclaredFields()) {
            MatchesRegex regex = field.getAnnotation(MatchesRegex.class);
            InRange range = field.getAnnotation(InRange.class);
            if (regex == null && range == null) {
                continue;
            }

            Object value = readField(field, entity, result);
            if (value == null) {
                result.addError(field.getName(), "Значення поля не може бути null");
                continue;
            }

            if (regex != null) {
                validateRegex(field, value, regex, result);
            }
            if (range != null) {
                validateRange(field, value, range, result);
            }
        }
    }

    private void validateRegex(Field field, Object value, MatchesRegex annotation, ValidationResult result) {
        if (!(value instanceof String text)) {
            result.addError(field.getName(), "Анотація @MatchesRegex застосовується до рядкового поля");
            return;
        }

        if (!Pattern.matches(annotation.pattern(), text)) {
            result.addError(field.getName(), annotation.message());
        }
    }

    private void validateRange(Field field, Object value, InRange annotation, ValidationResult result) {
        if (!(value instanceof Number number)) {
            result.addError(field.getName(), "Анотація @InRange застосовується до числового поля");
            return;
        }

        double numericValue = number.doubleValue();
        if (!Double.isFinite(numericValue)
                || numericValue < annotation.min()
                || numericValue > annotation.max()) {
            result.addError(field.getName(), annotation.message()
                    + " (фактичне значення: " + numericValue + ")");
        }
    }

    private Field findField(Class<?> type, String fieldName, ValidationResult result) {
        try {
            Field field = type.getDeclaredField(fieldName);
            if (!field.trySetAccessible()) {
                result.addError(fieldName, "Немає доступу до поля для Reflection-валідації");
                return null;
            }
            return field;
        } catch (NoSuchFieldException e) {
            result.addError(fieldName, "Поле, вказане у @HighPowerZoneRule, не знайдено");
            return null;
        } catch (SecurityException e) {
            result.addError(fieldName, "Доступ до поля заборонено: " + e.getMessage());
            return null;
        }
    }

    private Object readField(Field field, Object entity, ValidationResult result) {
        try {
            if (!field.trySetAccessible()) {
                result.addError(field.getName(), "Немає доступу до поля для Reflection-валідації");
                return null;
            }
            return field.get(entity);
        } catch (IllegalAccessException e) {
            result.addError(field.getName(), "Не вдалося прочитати поле: " + e.getMessage());
            return null;
        } catch (SecurityException e) {
            result.addError(field.getName(), "Доступ до поля заборонено: " + e.getMessage());
            return null;
        }
    }
}
