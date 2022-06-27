package by.itacademy.zuevvlad.cardpaymentproject.service.bindingvalidation.annotation.administrator;

import by.itacademy.zuevvlad.cardpaymentproject.service.bindingvalidation.constraintvalidator.administrator.AdministratorLevelShouldBeDefinedConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AdministratorLevelShouldBeDefinedConstraintValidator.class)
@Target(value = {ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AdministratorLevelShouldBeDefined
{
    public String message();
    public Class<?>[] groups() default {};
    public Class<? extends Payload>[] payload() default {};
}
