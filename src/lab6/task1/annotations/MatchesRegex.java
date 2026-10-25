package lab6.task1.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Метадані: рядкове значення має відповідати регулярному виразу.
 * Містить лише декларацію параметрів, без логіки перевірки.
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MatchesRegex {
    String pattern();
    String message() default "Значення не відповідає очікуваному формату";
}
