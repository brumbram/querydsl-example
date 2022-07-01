package com.opentable.sampleapplication.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateBoundValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBoundDate {

    // min date value
    String min() default "1900-01-01";
    // max date value
    String max() default "9999-12-31";

    String message() default "Date is out of bound. Please provide a valid date range where start is not greater than end";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}
