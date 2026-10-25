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
        // Null неможливо перевірити через Reflection, тому повертаємо помилку одразу.
        if (entity == null) {
            result.addError("entity", "Об'єкт для перевірки не може бути null");
            return result;
        }

        // Спершу застосовуємо комплексне правило класу, потім — анотації окремих полів.
        Class<?> type = entity.getClass();
        validateClassRule(entity, type, result);
        validateFields(entity, type, result);
        return result;
    }

    /** Перевіряє правило зони: потужний пристрій має зберігатися у визначеній зоні. */
    private void validateClassRule(Object entity, Class<?> type, ValidationResult result) {
        // Параметри правила (імена полів, поріг, зона й повідомлення) читаємо з анотації класу.
        HighPowerZoneRule rule = type.getAnnotation(HighPowerZoneRule.class);
        if (rule == null) {
            return;
        }

        // Анотація задає імена полів, а Reflection дає змогу отримати їх без прив'язки до Device.
        Field powerField = findField(type, rule.powerField(), result);
        Field zoneField = findField(type, rule.zoneField(), result);
        if (powerField == null || zoneField == null) {
            return;
        }

        // Значення приватних полів зчитуються через допоміжний метод із перевіркою доступу.
        Object powerValue = readField(powerField, entity, result);
        Object zoneValue = readField(zoneField, entity, result);
        if (powerValue == null || zoneValue == null) {
            return;
        }

        // Перевіряємо типи значень, щоб некоректно налаштована анотація не спричинила помилку приведення.
        if (!(powerValue instanceof Number power)) {
            result.addError(rule.powerField(), "Поле правила має бути числовим");
            return;
        }
        if (!(zoneValue instanceof String zone)) {
            result.addError(rule.zoneField(), "Поле зони у класовому правилі має бути рядком");
            return;
        }

        // За правилом пристрій має бути у спеціальній зоні лише коли потужність більша за поріг.
        if (power.doubleValue() > rule.powerThreshold() && !rule.requiredZone().equals(zone)) {
            result.addError(rule.zoneField(), rule.message());
        }
    }

    /** Перебирає поля класу та запускає перевірки, описані їхніми анотаціями. */
    private void validateFields(Object entity, Class<?> type, ValidationResult result) {
        for (Field field : type.getDeclaredFields()) {
            MatchesRegex regex = field.getAnnotation(MatchesRegex.class);
            InRange range = field.getAnnotation(InRange.class);
            // Не читаємо значення полів, для яких не оголошено правил валідації.
            if (regex == null && range == null) {
                continue;
            }

            Object value = readField(field, entity, result);
            // Обидві анотації тут описують значення, які мають бути задані.
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

    /** Перевіряє, що значення є рядком і повністю відповідає regex з анотації. */
    private void validateRegex(Field field, Object value, MatchesRegex annotation, ValidationResult result) {
        if (!(value instanceof String text)) {
            result.addError(field.getName(), "Анотація @MatchesRegex застосовується до рядкового поля");
            return;
        }

        // Pattern.matches вимагає відповідності всього рядка шаблону.
        if (!Pattern.matches(annotation.pattern(), text)) {
            result.addError(field.getName(), annotation.message());
        }
    }

    /** Перевіряє числове значення на включення в межі [min; max]. */
    private void validateRange(Field field, Object value, InRange annotation, ValidationResult result) {
        if (!(value instanceof Number number)) {
            result.addError(field.getName(), "Анотація @InRange застосовується до числового поля");
            return;
        }

        double numericValue = number.doubleValue();
        // NaN та нескінченності не вважаються допустимими числовими значеннями.
        if (!Double.isFinite(numericValue)
                || numericValue < annotation.min()
                || numericValue > annotation.max()) {
            result.addError(field.getName(), annotation.message()
                    + " (фактичне значення: " + numericValue + ")");
        }
    }

    /** Знаходить поле за назвою з анотації та відкриває доступ до нього для Reflection. */
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

    /** Зчитує значення поля та перетворює проблеми доступу на помилки валідації. */
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
