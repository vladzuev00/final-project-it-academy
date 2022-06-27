package by.itacademy.zuevvlad.cardpaymentproject.configuration;

import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc      //TODO: с этим тесты не работают
@Import(value = {MainConfiguration.class})
public class WebMainConfiguration implements WebMvcConfigurer
{
    public WebMainConfiguration()
    {
        super();
    }

    @Override
    public final void addResourceHandlers(final ResourceHandlerRegistry resourceHandlerRegistry)
    {
        resourceHandlerRegistry
                .addResourceHandler(WebMainConfiguration.PATH_PATTERN_OF_RESOURCE_HANDLER)
                .addResourceLocations(WebMainConfiguration.LOCATION_OF_RESOURCE_HANDLER);
    }

    private static final String PATH_PATTERN_OF_RESOURCE_HANDLER = "/resources/**";
    private static final String LOCATION_OF_RESOURCE_HANDLER = "/resources/";
}