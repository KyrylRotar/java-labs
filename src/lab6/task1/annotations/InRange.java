package lab6.task1.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Метадані: числове значення має лежати в діапазоні [min; max].
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InRange {
    double min();
    double max();
    String message() default "Значення виходить за межі допустимого діапазону";
}
