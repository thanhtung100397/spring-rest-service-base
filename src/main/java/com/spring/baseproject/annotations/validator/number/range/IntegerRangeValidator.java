package com.spring.baseproject.annotations.validator.number.range;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class IntegerRangeValidator implements ConstraintValidator<IntegerRange, Object> {
    private String end;
    private String start;

    @Override
    public void initialize(IntegerRange constraintAnnotation) {
        this.end = constraintAnnotation.end();
        this.start = constraintAnnotation.start();
    }

    @Override
    public boolean isValid(Object inputValue, ConstraintValidatorContext context) {
        Class<?> inputClass = inputValue.getClass();
        try {
            Field greaterField = inputClass.getDeclaredField(end);
            Field lessField = inputClass.getDeclaredField(start);
            greaterField.setAccessible(true);
            lessField.setAccessible(true);

            int greaterValue = (int) greaterField.get(inputValue);
            int lessValue = (int) lessField.get(inputValue);

            greaterField.setAccessible(false);
            lessField.setAccessible(false);

            return greaterValue > lessValue;
        } catch (NoSuchFieldException | IllegalAccessException | ClassCastException e) {
            return false;
        }
    }
}
