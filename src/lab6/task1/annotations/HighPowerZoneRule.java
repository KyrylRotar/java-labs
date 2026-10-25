package lab6.task1.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Метадані комплексного правила: якщо значення поля {@code powerField}
 * перевищує {@code powerThreshold}, то поле {@code zoneField}
 * повинне дорівнювати {@code requiredZone}.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HighPowerZoneRule {
    String powerField();
    String zoneField();
    double powerThreshold();
    String requiredZone();
    String message() default "Високопотужний пристрій дозволено зберігати лише у спеціалізованій зоні";
}
