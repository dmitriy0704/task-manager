package dev.folomkin.taskmanager.domain.dto.task.emailvalidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Pattern(regexp = Constants.PATTERN, flags = Pattern.Flag.CASE_INSENSITIVE)
public @interface EmailWithTld {
    String message() default "Wrong email";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}

interface Constants {
    static final String ATOM = "[a-z0-9!#$%&'*+/=?^_`{|}~-]";
    static final String DOMAIN = "(" + ATOM + "+(\\." + ATOM + "+)+";
    static final String IP_DOMAIN = "\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\]";

    static final String PATTERN =
            "^" + ATOM + "+(\\." + ATOM + "+)*@"
                    + DOMAIN
                    + "|"
                    + IP_DOMAIN
                    + ")$";
}