package com.example.application.validators;

import com.example.application.model.DateRange;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Custom Validator for checking if the start and end date are valid.
 * Validate if the start is not greater than end date
 * */
public class DateBoundValidator implements ConstraintValidator<ValidBoundDate, DateRange> {

    private final SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");

    private ValidBoundDate constraintAnnotation;

    @Override
    public void initialize(ValidBoundDate constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(DateRange range, ConstraintValidatorContext context) {
        try {
            var min = dateParser.parse(constraintAnnotation.min());
            var max = dateParser.parse(constraintAnnotation.max());

            if (range == null) {
                constraintAnnotation.message();
                return false;
            }
            if (range.getStart() == null || range.getStart().after(max) || range.getStart().before(min))
                return false;
            if (range.getEnd() == null || range.getEnd().after(max) || range.getEnd().before(min))
                return false;

            return !range.getStart().after(range.getEnd());

        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }
}
