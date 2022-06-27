package by.itacademy.zuevvlad.cardpaymentproject.aspect.pointcut;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public final class GeneralPointcut
{
    public GeneralPointcut()
    {
        super();
    }

    @Pointcut(value = "execution(public void by.itacademy.zuevvlad.cardpaymentproject.*.*.set*(*))")
    public final void setterPointcut(){}

    @Pointcut(value = "execution(public * by.itacademy.zuevvlad.cardpaymentproject.*.*.get*())")
    public final void getterPointcut(){}

    @Pointcut(value = "setterPointcut() || getterPointcut()")
    public final void setterOrGetterPointcut(){}
}
