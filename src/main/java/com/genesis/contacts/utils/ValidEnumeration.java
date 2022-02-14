package com.genesis.contacts.utils;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD) // METHOD, CONSTRUCTOR, etc.
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StringEnumerationValidator.class)
public @interface ValidEnumeration {
    String message() default "Incorrecte enum value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends Enum<?>> targetClassType();
}
