package by.itacademy.zuevvlad.cardpaymentproject.controller.client;

import by.itacademy.zuevvlad.cardpaymentproject.configuration.ServiceConfiguration;
import by.itacademy.zuevvlad.cardpaymentproject.controller.logon.LogOnController;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.OffloadingEntitiesException;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Client;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Payment;
import by.itacademy.zuevvlad.cardpaymentproject.entity.PaymentCard;
import by.itacademy.zuevvlad.cardpaymentproject.service.PaymentService;
import by.itacademy.zuevvlad.cardpaymentproject.service.sortingkey.payment.PaymentSortingKey;
import by.itacademy.zuevvlad.cardpaymentproject.service.user.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RequestMapping(path = {ClientOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER})
@Controller(value = "controllerOfOperationsOfClient")
@Scope(value = BeanDefinition.SCOPE_SINGLETON)
public final class ClientOperationController
{
    public static final String PATH_OF_REQUEST_MAPPING_OF_CONTROLLER = "/client/client_operation";

    private final PaymentService paymentService;
    private final ClientService clientService;

    @Autowired
    public ClientOperationController(
            @Qualifier(value = ServiceConfiguration.NAME_OF_BEAN_OF_PAYMENT_SERVICE)
            final PaymentService paymentService,
            @Qualifier(value = ServiceConfiguration.NAME_OF_BEAN_OF_CLIENT_SERVICE)
            final ClientService clientService)
    {
        super();
        this.paymentService = paymentService;
        this.clientService = clientService;
    }

    @GetMapping(path = {ClientOperationController.PATH_OF_REQUEST_PARAM_TO_LIST_PAYMENTS_OF_LOGGED_ON_CLIENT})
    public final String returnNameOfPageWithListedAllPaymentsAssociatedWithLoggedOnClient(
            @SessionAttribute(name = LogOnController.NAME_OF_MODEL_ATTRIBUTE_OF_LOGGED_ON_USER)
            final Client loggedOnClient,
            @RequestParam(name = ClientOperationController.NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY,
                          required = false)
            final String descriptionOfSortingKey,
            final Model model)
            throws OffloadingEntitiesException
    {
        final Collection<Payment> paymentsAssociatedWithLoggedOnClient = this.paymentService
                .findNotDeletedPaymentsAssociatedWithGivenClient(loggedOnClient);
        final Map<Payment, String> mapOfPaymentToItsDescriptionOfDate = this.paymentService
                .findMapOfPaymentToItsDescriptionOfDate(paymentsAssociatedWithLoggedOnClient);
        model.addAttribute(
                ClientOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_MAP_OF_PAYMENT_TO_ITS_DESCRIPTION_OF_DATE,
                mapOfPaymentToItsDescriptionOfDate);
        if(descriptionOfSortingKey == null)
        {
            model.addAttribute(ClientOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_LISTED_PAYMENTS,
                    paymentsAssociatedWithLoggedOnClient);
        }
        else
        {
            final PaymentSortingKey paymentSortingKey = PaymentSortingKey.valueOf(descriptionOfSortingKey);
            final Collection<Payment> sortedPaymentsAssociatedWithLoggedOnClient = this.paymentService.sortPayments(
                    paymentsAssociatedWithLoggedOnClient, paymentSortingKey);
            model.addAttribute(ClientOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_LISTED_PAYMENTS,
                    sortedPaymentsAssociatedWithLoggedOnClient);
        }
        return ClientOperationController.NAME_OF_PAGE_WITH_LISTED_PAYMENTS_ASSOCIATED_WITH_LOGGED_ON_CLIENT;
    }

    public static final String PATH_OF_REQUEST_PARAM_TO_LIST_PAYMENTS_OF_LOGGED_ON_CLIENT = "/list_all_payments";
    public static final String NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY = "description_of_sorting_key";
    private static final String NAME_OF_MODEL_ATTRIBUTE_OF_MAP_OF_PAYMENT_TO_ITS_DESCRIPTION_OF_DATE
            = "map_of_payment_to_its_description_of_date";
    private static final String NAME_OF_MODEL_ATTRIBUTE_OF_LISTED_PAYMENTS = "listed_payments";
    private static final String NAME_OF_PAGE_WITH_LISTED_PAYMENTS_ASSOCIATED_WITH_LOGGED_ON_CLIENT
            = "client/payments_of_logged_on_client";

    @GetMapping(path = {ClientOperationController.PATH_OF_REQUEST_PARAM_OF_MAIN_CLIENT_PAGE})
    public final String returnNameOfMainPage()
    {
        return ClientOperationController.NAME_OF_MAIN_CLIENT_PAGE;
    }

    public static final String PATH_OF_REQUEST_PARAM_OF_MAIN_CLIENT_PAGE = "/main_page";
    private static final String NAME_OF_MAIN_CLIENT_PAGE = "client/main_page";

    @GetMapping(path = {ClientOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_SENT_BY_LOGGED_ON_CLIENT_PAYMENTS})
    public final String returnNameOfPageWithListedAllSentByLoggedOnClientPayments(
            @SessionAttribute(name = LogOnController.NAME_OF_MODEL_ATTRIBUTE_OF_LOGGED_ON_USER)
            final Client loggedOnClient,
            @RequestParam(name = ClientOperationController.NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY,
                          required = false)
            final String descriptionOfSortingKey,
            final Model model)
            throws OffloadingEntitiesException
    {
        final Collection<Payment> sentByLoggedOnClientPayments = this.paymentService
                .findNotDeletedPaymentsSentByGivenClient(loggedOnClient);
        final Map<Payment, String> mapOfPaymentToItsDescriptionOfDate = this.paymentService
                .findMapOfPaymentToItsDescriptionOfDate(sentByLoggedOnClientPayments);
        model.addAttribute(
                ClientOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_MAP_OF_PAYMENT_TO_ITS_DESCRIPTION_OF_DATE,
                mapOfPaymentToItsDescriptionOfDate);
        if(descriptionOfSortingKey == null)
        {
            model.addAttribute(ClientOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_LISTED_PAYMENTS,
                    sentByLoggedOnClientPayments);
        }
        else
        {
            final PaymentSortingKey paymentSortingKey = PaymentSortingKey.valueOf(descriptionOfSortingKey);
            final Collection<Payment> sortedSentByLoggedOnClientPayments = this.paymentService.sortPayments(
                    sentByLoggedOnClientPayments, paymentSortingKey);
            model.addAttribute(ClientOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_LISTED_PAYMENTS,
                    sortedSentByLoggedOnClientPayments);
        }
        return ClientOperationController.NAME_OF_PAGE_WITH_LISTED_ALL_SENT_BY_LOGGED_ON_CLIENT_PAYMENTS;
    }

    public static final String PATH_OF_REQUEST_MAPPING_TO_LIST_SENT_BY_LOGGED_ON_CLIENT_PAYMENTS
            = "/list_sent_payments";
    private static final String NAME_OF_PAGE_WITH_LISTED_ALL_SENT_BY_LOGGED_ON_CLIENT_PAYMENTS
            = "client/sent_by_logged_on_client_payments";

    @GetMapping(path = {ClientOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_RECEIVED_BY_LOGGED_ON_CLIENT_PAYMENTS})
    public final String returnNameOfPageWithListedAllReceivedByLoggedOnClientPayments(
            @SessionAttribute(name = LogOnController.NAME_OF_MODEL_ATTRIBUTE_OF_LOGGED_ON_USER)
            final Client loggedOnClient,
            @RequestParam(name = ClientOperationController.NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY,
                          required = false)
            final String descriptionOfSortingKey,
            final Model model)
            throws OffloadingEntitiesException
    {
        final Collection<Payment> receivedByLoggedOnClientPayments = this.paymentService
                .findNotDeletedPaymentsReceivedByGivenClient(loggedOnClient);
        final Map<Payment, String> mapOfPaymentToItsDescriptionOfDate = this.paymentService
                .findMapOfPaymentToItsDescriptionOfDate(receivedByLoggedOnClientPayments);
        model.addAttribute(
                ClientOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_MAP_OF_PAYMENT_TO_ITS_DESCRIPTION_OF_DATE,
                mapOfPaymentToItsDescriptionOfDate);
        if(descriptionOfSortingKey == null)
        {
            model.addAttribute(ClientOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_LISTED_PAYMENTS,
                    receivedByLoggedOnClientPayments);
        }
        else
        {
            final PaymentSortingKey paymentSortingKey = PaymentSortingKey.valueOf(descriptionOfSortingKey);
            final Collection<Payment> sortedReceivedByLoggedOnClientPayments = this.paymentService.sortPayments(
                    receivedByLoggedOnClientPayments, paymentSortingKey);
            model.addAttribute(ClientOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_LISTED_PAYMENTS,
                    sortedReceivedByLoggedOnClientPayments);
        }
        return ClientOperationController.NAME_OF_PAGE_WITH_LISTED_ALL_RECEIVED_BY_LOGGED_ON_CLIENT_PAYMENTS;
    }

    public static final String PATH_OF_REQUEST_MAPPING_TO_LIST_RECEIVED_BY_LOGGED_ON_CLIENT_PAYMENTS
            = "/list_received_payments";
    private static final String NAME_OF_PAGE_WITH_LISTED_ALL_RECEIVED_BY_LOGGED_ON_CLIENT_PAYMENTS
            = "client/received_by_logged_on_client_payments";
}
