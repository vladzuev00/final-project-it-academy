package by.itacademy.zuevvlad.cardpaymentproject.controller.rest.client;

import by.itacademy.zuevvlad.cardpaymentproject.configuration.MainConfiguration;
import by.itacademy.zuevvlad.cardpaymentproject.controller.rest.PaymentCRUDRestController;
import by.itacademy.zuevvlad.cardpaymentproject.dto.PaymentDTO;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Payment;
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

@Component(value = "paymentRestClient")
@Scope(value = BeanDefinition.SCOPE_SINGLETON)
public final class PaymentRestClient extends RestClient<Payment, PaymentDTO>
{
    @Autowired
    public PaymentRestClient(
            @Qualifier(value = MainConfiguration.NAME_OF_BEAN_OF_REST_TEMPLATE)
            final RestTemplate restTemplate)
    {
        super(restTemplate, PaymentRestClient.URL, PaymentDTO.class);
    }

    private static final String URL = "http://localhost:8082/rest/payments";

    @Override
    public final PaymentDTO add(final Payment addedPayment)
    {
        final String urlTemplate = super.getUrl() + "?"
                + PaymentCRUDRestController.NAME_OF_REQUEST_PARAM_OF_ID_OF_PAYMENT_CARD_OF_SENDER_OF_ADDED_PAYMENT
                + "={"
                + PaymentCRUDRestController.NAME_OF_REQUEST_PARAM_OF_ID_OF_PAYMENT_CARD_OF_SENDER_OF_ADDED_PAYMENT
                + "}&"
                + PaymentCRUDRestController.NAME_OF_REQUEST_PARAM_OF_ID_OF_PAYMENT_CARD_OF_RECEIVER_OF_ADDED_PAYMENT
                + "={"
                + PaymentCRUDRestController.NAME_OF_REQUEST_PARAM_OF_ID_OF_PAYMENT_CARD_OF_RECEIVER_OF_ADDED_PAYMENT
                + "}";

        final PaymentCard paymentCardOfSenderOfAddedPaymentCard = addedPayment.getCardOfSender();
        final long idOfPaymentCardOfSenderOfAddedPaymentCard = paymentCardOfSenderOfAddedPaymentCard.getId();

        final PaymentCard paymentCardOfReceiverOfAddedPaymentCard = addedPayment.getCardOfReceiver();
        final long idOfPaymentCardOfReceiverOfAddedPaymentCard = paymentCardOfReceiverOfAddedPaymentCard.getId();

        final Map<String, Long> mapOfNameToValueOfRequestParam = new LinkedHashMap<String, Long>()
        {
            {
                super.put(PaymentCRUDRestController.NAME_OF_REQUEST_PARAM_OF_ID_OF_PAYMENT_CARD_OF_SENDER_OF_ADDED_PAYMENT,
                        idOfPaymentCardOfSenderOfAddedPaymentCard);
                super.put(PaymentCRUDRestController.NAME_OF_REQUEST_PARAM_OF_ID_OF_PAYMENT_CARD_OF_RECEIVER_OF_ADDED_PAYMENT,
                        idOfPaymentCardOfReceiverOfAddedPaymentCard);
            }
        };

        final RestTemplate restTemplate = super.getRestTemplate();
        final ResponseEntity<PaymentDTO> responseEntity = restTemplate.postForEntity(urlTemplate, addedPayment,
                PaymentDTO.class, mapOfNameToValueOfRequestParam);
        return responseEntity.getBody();
    }

    @Override
    public final void update(final Payment updatedPayment)
    {
        final String urlTemplate = super.getUrl() + "?"
                + PaymentCRUDRestController.NAME_OF_REQUEST_PARAM_OF_ID_OF_PAYMENT_CARD_OF_SENDER_OF_UPDATED_PAYMENT
                + "={"
                + PaymentCRUDRestController.NAME_OF_REQUEST_PARAM_OF_ID_OF_PAYMENT_CARD_OF_SENDER_OF_UPDATED_PAYMENT
                + "}&"
                + PaymentCRUDRestController.NAME_OF_REQUEST_PARAM_OF_ID_OF_PAYMENT_CARD_OF_RECEIVER_OF_UPDATED_PAYMENT
                + "={"
                + PaymentCRUDRestController.NAME_OF_REQUEST_PARAM_OF_ID_OF_PAYMENT_CARD_OF_RECEIVER_OF_UPDATED_PAYMENT
                + "}";

        final PaymentCard paymentCardOfSenderOfUpdatedPaymentCard = updatedPayment.getCardOfSender();
        final long idOfPaymentCardOfSenderOfUpdatedPaymentCard = paymentCardOfSenderOfUpdatedPaymentCard.getId();

        final PaymentCard paymentCardOfReceiverOfUpdatedPaymentCard = updatedPayment.getCardOfReceiver();
        final long idOfPaymentCardOfReceiverOfUpdatedPaymentCard = paymentCardOfReceiverOfUpdatedPaymentCard.getId();

        final Map<String, Long> mapOfNameToValueOfRequestParam = new LinkedHashMap<String, Long>()
        {
            {
                super.put(PaymentCRUDRestController.NAME_OF_REQUEST_PARAM_OF_ID_OF_PAYMENT_CARD_OF_SENDER_OF_UPDATED_PAYMENT,
                        idOfPaymentCardOfSenderOfUpdatedPaymentCard);
                super.put(PaymentCRUDRestController.NAME_OF_REQUEST_PARAM_OF_ID_OF_PAYMENT_CARD_OF_RECEIVER_OF_UPDATED_PAYMENT,
                        idOfPaymentCardOfReceiverOfUpdatedPaymentCard);
            }
        };

        final RestTemplate restTemplate = super.getRestTemplate();
        restTemplate.put(urlTemplate, updatedPayment, mapOfNameToValueOfRequestParam);
    }
}
