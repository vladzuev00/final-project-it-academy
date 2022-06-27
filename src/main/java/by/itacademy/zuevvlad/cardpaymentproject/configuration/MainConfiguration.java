package by.itacademy.zuevvlad.cardpaymentproject.configuration;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "by.itacademy.zuevvlad.cardpaymentproject")
@Import(value = {ServiceConfiguration.class})
public class MainConfiguration
{
    public MainConfiguration()
    {
        super();
    }

    @Bean(name = "viewResolver")
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    public ViewResolver createViewResolver()
    {
        final InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();

        internalResourceViewResolver.setPrefix(MainConfiguration.PREFIX_OF_VIEW_RESOLVER);
        internalResourceViewResolver.setSuffix(MainConfiguration.SUFFIX_OF_VIEW_RESOLVER);

        return internalResourceViewResolver;
    }

    private static final String PREFIX_OF_VIEW_RESOLVER = "/WEB-INF/view/";
    private static final String SUFFIX_OF_VIEW_RESOLVER = ".jsp";

    @Bean(name = MainConfiguration.NAME_OF_BEAN_OF_REST_TEMPLATE)
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    public RestTemplate createRestTemplate()
    {
        return new RestTemplate();
    }

    public static final String NAME_OF_BEAN_OF_REST_TEMPLATE = "restTemplate";
}
