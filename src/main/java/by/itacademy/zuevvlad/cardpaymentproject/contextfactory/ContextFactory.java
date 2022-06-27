package by.itacademy.zuevvlad.cardpaymentproject.contextfactory;

import by.itacademy.zuevvlad.cardpaymentproject.configuration.MainConfiguration;
import by.itacademy.zuevvlad.cardpaymentproject.configuration.ServiceConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public final class ContextFactory
{
    private final AnnotationConfigApplicationContext annotationConfigApplicationContext;

    public static ContextFactory createContextFactory()
    {
        if(ContextFactory.contextFactory == null)
        {
            synchronized(ContextFactory.class)
            {
                if(ContextFactory.contextFactory == null)
                {
                    final AnnotationConfigApplicationContext annotationConfigApplicationContext
                            = new AnnotationConfigApplicationContext(MainConfiguration.class);  //если подставить WebMainConfiguration не работает
                    ContextFactory.contextFactory = new ContextFactory(annotationConfigApplicationContext);
                }
            }
        }
        return ContextFactory.contextFactory;
    }

    private static ContextFactory contextFactory = null;

    private ContextFactory(final AnnotationConfigApplicationContext annotationConfigApplicationContext)
    {
        this.annotationConfigApplicationContext = annotationConfigApplicationContext;
    }

    public final AnnotationConfigApplicationContext createContext()
    {
        return this.annotationConfigApplicationContext;
    }
}
