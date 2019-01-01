package com.spring.baseproject.annotations.validator.number.range;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class LongRangeValidator implements ConstraintValidator<LongRange, Object> {
    private String end;
    private String start;

    @Override
    public void initialize(LongRange constraintAnnotation) {
        this.end = constraintAnnotation.end();
        this.start = constraintAnnotation.start();
    }

    @Override
    public boolean isValid(Object inputValue, ConstraintValidatorContext context) {
        Class<?> inputClass = inputValue.getClass();
        while (true) {
            try {
                Field greaterField = inputClass.getDeclaredField(end);
                Field lessField = inputClass.getDeclaredField(start);

                greaterField.setAccessible(true);
                lessField.setAccessible(true);

                long greaterValue = (long) greaterField.get(inputValue);
                long lessValue = (long) lessField.get(inputValue);

                greaterField.setAccessible(false);
                lessField.setAccessible(false);

                return greaterValue > lessValue;
            } catch (NoSuchFieldException e) {
                if (inputClass.getSuperclass() == null) {
                    return false;
                } else {
                    inputClass = inputClass.getSuperclass();
                }
            } catch (IllegalAccessException | ClassCastException e) {
                return false;
            }
        }
    }
}
