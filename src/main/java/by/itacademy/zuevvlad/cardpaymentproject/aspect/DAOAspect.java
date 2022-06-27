package by.itacademy.zuevvlad.cardpaymentproject.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.function.LongToDoubleFunction;
import java.util.logging.Logger;

@Aspect
@Scope(value = BeanDefinition.SCOPE_SINGLETON)
@Component(value = "daoAspect")
public final class DAOAspect
{
    private final Logger logger;
    private final LongToDoubleFunction functionMillisToSeconds;

    public DAOAspect()
    {
        super();
        this.logger = Logger.getLogger(DAOAspect.class.getName());
        this.functionMillisToSeconds = (final long millis) ->
        {
            return millis / DAOAspect.DIVIDER_TO_CONVERT_MILLIS_TO_SECONDS;
        };
    }

    private static final double DIVIDER_TO_CONVERT_MILLIS_TO_SECONDS = 1000.0;
/*
    @Around(value = "by.itacademy.zuevvlad.cardpaymentproject.aspect.pointcut.DAOPointcut.daoMethodExceptSetterAndGetterPointcut()")
    public final Object measureAndLogTimeInSecondsOfExecutionOfDaoMethodExceptSetterAndGetter(
            final ProceedingJoinPoint proceedingJoinPoint)
            throws Throwable
    {
        final long millisAtStartExecution = System.currentTimeMillis();

        final Object returnedByMethodObject = proceedingJoinPoint.proceed();
        final long millisAtEndExecution = System.currentTimeMillis();

        final long durationInMillisOfExecution = millisAtEndExecution - millisAtStartExecution;
        final double durationInSecondsOfExecution = this.functionMillisToSeconds.applyAsDouble(
                durationInMillisOfExecution);

        final MethodSignature methodSignature = (MethodSignature)proceedingJoinPoint.getSignature();
        final String descriptionOfMethodSignature = methodSignature.toString();

        final String loggedMessage = String.format(DAOAspect.TEMPLATE_OF_LOGGED_MESSAGE,
                DAOAspect.class.getSimpleName(), descriptionOfMethodSignature, durationInSecondsOfExecution);
        this.logger.info(loggedMessage);

        return returnedByMethodObject;
    }

*/

    /*
        First %s - simple name of aspect's class
        Second %s - method's signature
        %f - time of execution in seconds
     */
    private static final String TEMPLATE_OF_LOGGED_MESSAGE = "%s: %s executed %f seconds";
}
