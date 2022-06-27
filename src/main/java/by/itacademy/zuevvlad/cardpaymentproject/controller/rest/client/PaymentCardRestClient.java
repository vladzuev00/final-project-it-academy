package by.itacademy.zuevvlad.cardpaymentproject.controller.rest.client;

import by.itacademy.zuevvlad.cardpaymentproject.configuration.MainConfiguration;
import by.itacademy.zuevvlad.cardpaymentproject.controller.rest.PaymentCardCRUDRestController;
import by.itacademy.zuevvlad.cardpaymentproject.dto.PaymentCardDTO;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Client;
import by.itacademy.zuevvlad.cardpaymentproject.entity.PaymentCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

@Component(value = "paymentCardRestClient")
@Scope(value = BeanDefinition.SCOPE_SINGLETON)
public final class PaymentCardRestClient extends RestClient<PaymentCard, PaymentCardDTO>
{
    @Autowired
    public PaymentCardRestClient(
            @Qualifier(value = MainConfiguration.NAME_OF_BEAN_OF_REST_TEMPLATE)
            final RestTemplate restTemplate)
    {
        super(restTemplate, PaymentCardRestClient.URL, PaymentCardDTO.class);
    }

    private static final String URL = "http://localhost:8082/rest/payment_cards";

    @Override
    public final PaymentCardDTO add(final PaymentCard addedPaymentCard)
    {
        final String urlTemplate = super.getUrl() + "?"
                + PaymentCardCRUDRestController.NAME_OF_REQUEST_PARAM_OF_ID_OF_CLIENT_OF_ADDED_PAYMENT_CARD + "={"
                + PaymentCardCRUDRestController.NAME_OF_REQUEST_PARAM_OF_ID_OF_CLIENT_OF_ADDED_PAYMENT_CARD + "}";

        final Client clientOfAddedPaymentCard = addedPaymentCard.getClient();
        final long idOfClientOfAddedPaymentCard = clientOfAddedPaymentCard.getId();
        final Map<String, Long> mapOfNameToValueOfRequestParam = new LinkedHashMap<String, Long>()
        {
            {
                super.put(PaymentCardCRUDRestController.NAME_OF_REQUEST_PARAM_OF_ID_OF_CLIENT_OF_ADDED_PAYMENT_CARD,
                        idOfClientOfAddedPaymentCard);
            }
        };

        final RestTemplate restTemplate = super.getRestTemplate();
        final ResponseEntity<PaymentCardDTO> responseEntity = restTemplate.postForEntity(urlTemplate, addedPaymentCard,
                PaymentCardDTO.class, mapOfNameToValueOfRequestParam);
        return responseEntity.getBody();
    }

    @Override
    public final void update(final PaymentCard updatedPaymentCard)
    {
        final String urlTemplate = super.getUrl() + "?"
                + PaymentCardCRUDRestController.NAME_OF_REQUEST_PARAM_OF_CLIENT_ID_OF_UPDATED_PAYMENT_CARD + "={"
                + PaymentCardCRUDRestController.NAME_OF_REQUEST_PARAM_OF_CLIENT_ID_OF_UPDATED_PAYMENT_CARD + "}";

        final Client clientOfUpdatedPaymentCard = updatedPaymentCard.getClient();
        final long idOfClientOfUpdatedPaymentCard = clientOfUpdatedPaymentCard.getId();
        final Map<String, Long> mapOfNameToValueOfRequestParam = new LinkedHashMap<String, Long>()
        {
            {
                super.put(PaymentCardCRUDRestController.NAME_OF_REQUEST_PARAM_OF_CLIENT_ID_OF_UPDATED_PAYMENT_CARD,
                        idOfClientOfUpdatedPaymentCard);
            }
        };

        final RestTemplate restTemplate = super.getRestTemplate();
        restTemplate.put(urlTemplate, updatedPaymentCard, mapOfNameToValueOfRequestParam);
    }
}
