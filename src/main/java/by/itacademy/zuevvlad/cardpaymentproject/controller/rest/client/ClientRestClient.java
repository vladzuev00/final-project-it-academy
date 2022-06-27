package by.itacademy.zuevvlad.cardpaymentproject.controller.rest.client;

import by.itacademy.zuevvlad.cardpaymentproject.configuration.MainConfiguration;
import by.itacademy.zuevvlad.cardpaymentproject.controller.rest.ClientCRUDRestController;
import by.itacademy.zuevvlad.cardpaymentproject.dto.ClientDTO;
import by.itacademy.zuevvlad.cardpaymentproject.entity.BankAccount;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

@Component(value = "clientRestClient")
@Scope(value = BeanDefinition.SCOPE_SINGLETON)
public final class ClientRestClient extends RestClient<Client, ClientDTO>
{
    @Autowired
    public ClientRestClient(
            @Qualifier(value = MainConfiguration.NAME_OF_BEAN_OF_REST_TEMPLATE)
            final RestTemplate restTemplate)
    {
        super(restTemplate, ClientRestClient.URL, ClientDTO.class);
    }

    private static final String URL = "http://localhost:8082/rest/clients";

    @Override
    public final ClientDTO add(final Client addedClient)
    {
        final String urlTemplate = super.getUrl() + "?"
                + ClientCRUDRestController.NAME_OF_REQUEST_PARAM_OF_BANK_ACCOUNT_ID_OF_ADDED_CLIENT + "={"
                + ClientCRUDRestController.NAME_OF_REQUEST_PARAM_OF_BANK_ACCOUNT_ID_OF_ADDED_CLIENT + "}";

        final BankAccount bankAccountOfAddedClient = addedClient.getBankAccount();
        final long idOfBankAccountOfAddedClient = bankAccountOfAddedClient.getId();
        final Map<String, Long> mapOfNameToValueOfRequestParam = new LinkedHashMap<String, Long>()
        {
            {
                this.put(ClientCRUDRestController.NAME_OF_REQUEST_PARAM_OF_BANK_ACCOUNT_ID_OF_ADDED_CLIENT,
                        idOfBankAccountOfAddedClient);
            }
        };

        final RestTemplate restTemplate = super.getRestTemplate();
        final ResponseEntity<ClientDTO> responseEntity = restTemplate.postForEntity(urlTemplate, addedClient,
                ClientDTO.class, mapOfNameToValueOfRequestParam);
        return responseEntity.getBody();
    }

    @Override
    public final void update(final Client updatedClient)
    {
        final String urlTemplate = super.getUrl() + "?"
                + ClientCRUDRestController.NAME_OF_REQUEST_PARAM_OF_BANK_ACCOUNT_ID_OF_UPDATED_CLIENT + "={"
                + ClientCRUDRestController.NAME_OF_REQUEST_PARAM_OF_BANK_ACCOUNT_ID_OF_UPDATED_CLIENT + "}";

        final BankAccount bankAccountOfUpdatedClient = updatedClient.getBankAccount();
        final long idOfBankAccountOfUpdatedClient = bankAccountOfUpdatedClient.getId();
        final Map<String, Long> mapOfNameToValueOfRequestParam = new LinkedHashMap<String, Long>()
        {
            {
                this.put(ClientCRUDRestController.NAME_OF_REQUEST_PARAM_OF_BANK_ACCOUNT_ID_OF_UPDATED_CLIENT,
                        idOfBankAccountOfUpdatedClient);
            }
        };

        final RestTemplate restTemplate = super.getRestTemplate();
        restTemplate.put(urlTemplate, updatedClient, mapOfNameToValueOfRequestParam);
    }
}
