package by.itacademy.zuevvlad.cardpaymentproject.controller.rest.client;

import by.itacademy.zuevvlad.cardpaymentproject.configuration.MainConfiguration;
import by.itacademy.zuevvlad.cardpaymentproject.dto.BankAccountDTO;
import by.itacademy.zuevvlad.cardpaymentproject.entity.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component(value = "bankAccountRestClient")
@Scope(value = BeanDefinition.SCOPE_SINGLETON)
public final class BankAccountRestClient extends RestClient<BankAccount, BankAccountDTO>
{
    @Autowired
    public BankAccountRestClient(
            @Qualifier(value = MainConfiguration.NAME_OF_BEAN_OF_REST_TEMPLATE)
            final RestTemplate restTemplate)
    {
        super(restTemplate, BankAccountRestClient.URL, BankAccountDTO.class);
    }

    private static final String URL = "http://localhost:8082/rest/bank_accounts";
}
