package by.itacademy.zuevvlad.cardpaymentproject.controller.rest.client;

import by.itacademy.zuevvlad.cardpaymentproject.configuration.MainConfiguration;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Administrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component(value = "administratorRestClient")
@Scope(value = BeanDefinition.SCOPE_SINGLETON)
public final class AdministratorRestClient extends RestClient<Administrator, Administrator>
{
    @Autowired
    public AdministratorRestClient(
            @Qualifier(value = MainConfiguration.NAME_OF_BEAN_OF_REST_TEMPLATE)
            final RestTemplate restTemplate)
    {
        super(restTemplate, AdministratorRestClient.URL, Administrator.class);
    }

    private static final String URL = "http://localhost:8082/rest/administrators";
}
