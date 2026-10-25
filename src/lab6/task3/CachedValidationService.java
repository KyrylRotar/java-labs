package lab6.task3;

import lab6.task1.annotations.HighPowerZoneRule;
import lab6.task1.annotations.InRange;
import lab6.task1.annotations.MatchesRegex;
import lab6.task2.EntityValidator;
import lab6.task2.ValidationError;
import lab6.task2.ValidationResult;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

/** Валідатор, який кешує reflection-метадані для кожного типу. */
public final class CachedValidationService implements EntityValidator {
    private final ConcurrentMap<Class<?>, ClassValidationMetadata> metadataCache = new ConcurrentHashMap<>();

    @Override
    public ValidationResult validate(Object entity) {
        ValidationResult result = new ValidationResult();
        if (entity == null) {
            result.addError("entity", "Об'єкт для перевірки не може бути null");
            return result;
        }

        ClassValidationMetadata metadata = metadataCache.computeIfAbsent(
                entity.getClass(), ClassValidationMetadata::build);
        for (ValidationError error : metadata.configurationErrors()) {
            result.addError(error.fieldName(), error.message());
        }

        validateClassRule(entity, metadata, result);
        validateFields(entity, metadata, result);
        return result;
    }

    /** Повертає кількість типів, метадані яких уже збережено в кеші. */
    public int cachedTypeCount() {
        return metadataCache.size();
    }

    private void validateClassRule(Object entity, ClassValidationMetadata metadata, ValidationResult result) {
        HighPowerZoneRule rule = metadata.classRule();
        Field powerField = metadata.powerField();
        Field zoneField = metadata.zoneField();
        if (rule == null || powerField == null || zoneField == null) {
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

    private void validateFields(Object entity, ClassValidationMetadata metadata, ValidationResult result) {
        for (FieldValidationMetadata fieldMetadata : metadata.validatedFields()) {
            Field field = fieldMetadata.field();
            Object value = readField(field, entity, result);
            if (value == null) {
                result.addError(field.getName(), "Значення поля не може бути null");
                continue;
            }

            MatchesRegex regex = fieldMetadata.regex();
            if (regex != null) {
                validateRegex(field, value, regex, fieldMetadata.compiledPattern(), result);
            }

            InRange range = fieldMetadata.range();
            if (range != null) {
                validateRange(field, value, range, result);
            }
        }
    }

    private void validateRegex(Field field, Object value, MatchesRegex annotation,
                               Pattern compiledPattern, ValidationResult result) {
        if (!(value instanceof String text)) {
            result.addError(field.getName(), "Анотація @MatchesRegex застосовується до рядкового поля");
            return;
        }

        if (compiledPattern == null || !compiledPattern.matcher(text).matches()) {
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

    private Object readField(Field field, Object entity, ValidationResult result) {
        try {
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
