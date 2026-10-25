package lab6.task4.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Маркує компонент і зберігає його конфігураційні метадані. */
// Анотація застосовується лише до класів і доступна Reflection під час виконання.
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
    /** Конфігураційне ім'я компонента. */
    String name();

    /** Версія компонента; якщо її не вказано, використовується 1.0. */
    String version() default "1.0";
}
