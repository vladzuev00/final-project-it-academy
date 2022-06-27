package by.itacademy.zuevvlad.cardpaymentproject.service.bindingvalidation.constraintvalidator.administrator;

import by.itacademy.zuevvlad.cardpaymentproject.entity.Administrator;
import by.itacademy.zuevvlad.cardpaymentproject.service.bindingvalidation.annotation.administrator.AdministratorLevelShouldBeDefined;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public final class AdministratorLevelShouldBeDefinedConstraintValidator
        implements ConstraintValidator<AdministratorLevelShouldBeDefined, Administrator.Level>
{
    public AdministratorLevelShouldBeDefinedConstraintValidator()
    {
        super();
    }

    @Override
    public final boolean isValid(final Administrator.Level level,
                                 final ConstraintValidatorContext constraintValidatorContext)
    {
        return level != Administrator.Level.NOT_DEFINED;
    }
}
