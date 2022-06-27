package by.itacademy.zuevvlad.cardpaymentproject.aspect.pointcut;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public final class DAOPointcut
{
    public DAOPointcut()
    {
        super();
    }

    @Pointcut(value = "execution(public * by.itacademy.zuevvlad.cardpaymentproject.dao.*.*(..))")
    public final void daoMethodPointcut(){}

    @Pointcut(value = "daoMethodPointcut() && !by.itacademy.zuevvlad.cardpaymentproject.aspect.pointcut.GeneralPointcut.setterOrGetterPointcut()")
    public final void daoMethodExceptSetterAndGetterPointcut(){}
}
