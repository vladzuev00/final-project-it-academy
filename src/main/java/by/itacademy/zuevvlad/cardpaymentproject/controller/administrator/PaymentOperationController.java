package by.itacademy.zuevvlad.cardpaymentproject.controller.administrator;

import by.itacademy.zuevvlad.cardpaymentproject.configuration.ServiceConfiguration;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.*;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Payment;
import by.itacademy.zuevvlad.cardpaymentproject.entity.PaymentCard;
import by.itacademy.zuevvlad.cardpaymentproject.service.PaymentCardService;
import by.itacademy.zuevvlad.cardpaymentproject.service.PaymentService;
import by.itacademy.zuevvlad.cardpaymentproject.service.calendarhandler.exception.CalendarHandlingException;
import by.itacademy.zuevvlad.cardpaymentproject.service.exception.NoSuchEntityException;
import by.itacademy.zuevvlad.cardpaymentproject.service.sortingkey.payment.PaymentSortingKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;

@Controller
@RequestMapping(path = {PaymentOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER})
public class PaymentOperationController
{
    public static final String PATH_OF_REQUEST_MAPPING_OF_CONTROLLER = "/administrator_payment_operation";

    private final PaymentService paymentService;
    private final PaymentCardService paymentCardService;

    @Autowired
    public PaymentOperationController(
            @Qualifier(value = ServiceConfiguration.NAME_OF_BEAN_OF_PAYMENT_SERVICE)
            final PaymentService paymentService,
            @Qualifier(value = ServiceConfiguration.NAME_OF_BEAN_OF_PAYMENT_CARD_SERVICE)
            final PaymentCardService paymentCardService)
    {
        super();
        this.paymentService = paymentService;
        this.paymentCardService = paymentCardService;
    }

    @InitBinder
    public final void initBinder(final WebDataBinder webDataBinder)
    {
        //тримит строки при binding-е, если были введены одни пробелы, то будет присвоено полю значиение null
        final StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        webDataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping(path = {PaymentOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_PAYMENTS})
    public final String returnNameOfPageWithListedPayments(final Model model,
                                                           @RequestParam(name = PaymentOperationController
                                                                   .NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY,
                                                                         required = false)
                                                           final String descriptionOfSortingKey)
            throws OffloadingEntitiesException
    {
        final Collection<Payment> listedPayments = this.paymentService.findAllEntities();
        final Map<Payment, String> mapOfPaymentToItsDescriptionOfDate = this.paymentService
                .findMapOfPaymentToItsDescriptionOfDate(listedPayments);
        model.addAttribute(
                PaymentOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_MAP_OF_PAYMENT_TO_ITS_DESCRIPTION_OF_DATE,
                mapOfPaymentToItsDescriptionOfDate);
        if(descriptionOfSortingKey == null)
        {
            model.addAttribute(PaymentOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_LISTED_PAYMENTS, listedPayments);
        }
        else
        {
            final PaymentSortingKey paymentSortingKey = PaymentSortingKey.valueOf(descriptionOfSortingKey);
            final Collection<Payment> sortedPayments = this.paymentService.sortPayments(listedPayments,
                    paymentSortingKey);
            model.addAttribute(PaymentOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_LISTED_PAYMENTS, sortedPayments);
        }
        return PaymentOperationController.NAME_OF_PAGE_WITH_LISTED_PAYMENTS;
    }

    public static final String PATH_OF_REQUEST_MAPPING_TO_LIST_PAYMENTS = "/list";
    public static final String NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY = "description_of_sorting_key";
    private static final String NAME_OF_MODEL_ATTRIBUTE_OF_MAP_OF_PAYMENT_TO_ITS_DESCRIPTION_OF_DATE
            = "map_of_payment_to_its_description_of_date";
    private static final String NAME_OF_MODEL_ATTRIBUTE_OF_LISTED_PAYMENTS = "listed_payments";
    private static final String NAME_OF_PAGE_WITH_LISTED_PAYMENTS = "administrator/listed_entities/payments";

    @GetMapping(path = {PaymentOperationController.PATH_OF_REQUEST_MAPPING_TO_ADD_PAYMENT})
    public final String returnNameOfPageWithFormToAddNewPayment(final Model model)
    {
        final Payment addedPayment = new Payment();
        model.addAttribute(PaymentOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_ADDED_PAYMENT, addedPayment);
        return PaymentOperationController.NAME_OF_PAGE_WITH_FORM_TO_ADD_PAYMENT;
    }

    public static final String PATH_OF_REQUEST_MAPPING_TO_ADD_PAYMENT = "/add";
    public static final String NAME_OF_MODEL_ATTRIBUTE_OF_ADDED_PAYMENT = "added_payment";
    private static final String NAME_OF_PAGE_WITH_FORM_TO_ADD_PAYMENT
            = "administrator/form_to_add_entity/form_to_add_payment";

    @PostMapping(path = {PaymentOperationController.PATH_OF_REQUEST_MAPPING_TO_ADD_PAYMENT})
    public final String addNewPaymentAndReturnRedirectionToRequestMappingOfPageWithListedPayments(
            @Valid
            @ModelAttribute(name = PaymentOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_ADDED_PAYMENT)
            final Payment addedPayment,
            final BindingResult bindingResult,
            @RequestParam(name = PaymentOperationController
                    .NAME_OF_REQUEST_PARAM_OF_CARD_NUMBER_OF_SENDER_OF_ADDED_PAYMENT)
            final String cardNumberOfSenderOfAddedPayment,
            @RequestParam(name = PaymentOperationController
                    .NAME_OF_REQUEST_PARAM_OF_CARD_NUMBER_OF_RECEIVER_OF_ADDED_PAYMENT)
            final String cardNumberOfReceiverOfAddedPayment,
            @RequestParam(name = PaymentOperationController.NAME_OF_REQUEST_PARAM_OF_DATE_OF_ADDED_PAYMENT)
            final String descriptionOfDateOfAddedPayment)
            throws DefiningExistingEntityException, FindingEntityException, NoSuchEntityException,
                   CalendarHandlingException, AddingEntityException
    {
        if(!this.paymentCardService.isNotDeletedPaymentCardWithGivenCardNumberExist(cardNumberOfSenderOfAddedPayment))
        {
            final String errorCode = PaymentOperationController.PREFIX_OF_ERROR_CODE
                    + PaymentOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_ADDED_PAYMENT;
            bindingResult.rejectValue(PaymentOperationController.NAME_OF_FIELD_OF_CARD_OF_SENDER_OF_PAYMENT, errorCode,
                    PaymentOperationController.MESSAGE_OF_NOT_EXISTING_PAYMENT_CARD_WITH_GIVEN_CARD_NUMBER);
        }
        if(!this.paymentCardService.isNotDeletedPaymentCardWithGivenCardNumberExist(cardNumberOfReceiverOfAddedPayment))
        {
            final String errorCode = PaymentOperationController.PREFIX_OF_ERROR_CODE
                    + PaymentOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_ADDED_PAYMENT;
            bindingResult.rejectValue(PaymentOperationController.NAME_OF_FIELD_OF_CARD_OF_RECEIVER_OF_PAYMENT,
                    errorCode, PaymentOperationController.MESSAGE_OF_NOT_EXISTING_PAYMENT_CARD_WITH_GIVEN_CARD_NUMBER);
        }
        if(!bindingResult.hasErrors())
        {
            final PaymentCard paymentCardOfSenderOfAddedPayment = this.paymentCardService
                    .findNotDeletedPaymentCardByCardNumber(cardNumberOfSenderOfAddedPayment);
            addedPayment.setCardOfSender(paymentCardOfSenderOfAddedPayment);

            final PaymentCard paymentCardOfReceiverOfAddedPayment = this.paymentCardService
                    .findNotDeletedPaymentCardByCardNumber(cardNumberOfReceiverOfAddedPayment);
            addedPayment.setCardOfReceiver(paymentCardOfReceiverOfAddedPayment);

            final Calendar dateOfAddedPayment = this.paymentService.parseDescriptionOfDateOfPayment(
                    descriptionOfDateOfAddedPayment);
            addedPayment.setDate(dateOfAddedPayment);

            this.paymentService.addEntity(addedPayment);

            return    PaymentOperationController.PREFIX_TO_REDIRECT
                    + PaymentOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                    + PaymentOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_PAYMENTS;
        }
        else
        {
            return PaymentOperationController.NAME_OF_PAGE_WITH_FORM_TO_ADD_PAYMENT;
        }
    }

    private static final String NAME_OF_REQUEST_PARAM_OF_CARD_NUMBER_OF_SENDER_OF_ADDED_PAYMENT
            = "card_number_of_sender";
    private static final String NAME_OF_REQUEST_PARAM_OF_CARD_NUMBER_OF_RECEIVER_OF_ADDED_PAYMENT
            = "card_number_of_receiver";
    private static final String NAME_OF_REQUEST_PARAM_OF_DATE_OF_ADDED_PAYMENT = "date_of_added_payment";

    private static final String PREFIX_OF_ERROR_CODE = "error.";
    private static final String NAME_OF_FIELD_OF_CARD_OF_SENDER_OF_PAYMENT = "cardOfSender";
    private static final String NAME_OF_FIELD_OF_CARD_OF_RECEIVER_OF_PAYMENT = "cardOfReceiver";
    private static final String MESSAGE_OF_NOT_EXISTING_PAYMENT_CARD_WITH_GIVEN_CARD_NUMBER
            = "Payment card with given card number doesn't exist";

    private static final String PREFIX_TO_REDIRECT = "redirect:";

    @GetMapping(path = {PaymentOperationController.PATH_OF_REQUEST_MAPPING_TO_UPDATE_PAYMENT})
    public final String returnNameOfPageWithFormToUpdatePayment(
            @RequestParam(name = PaymentOperationController.NAME_OF_REQUEST_PARAM_OF_ID_OF_UPDATED_PAYMENT)
            final long idOfUpdatedPayment,
            final Model model)
            throws FindingEntityException, NoSuchEntityException
    {
        final Payment updatedPayment = this.paymentService.findEntityById(idOfUpdatedPayment);
        model.addAttribute(PaymentOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_UPDATED_PAYMENT, updatedPayment);
        return PaymentOperationController.NAME_OF_PAGE_WITH_FORM_TO_UPDATE_PAYMENT;
    }

    public static final String PATH_OF_REQUEST_MAPPING_TO_UPDATE_PAYMENT = "/update";
    public static final String NAME_OF_REQUEST_PARAM_OF_ID_OF_UPDATED_PAYMENT = "id_of_updated_payment";
    private static final String NAME_OF_MODEL_ATTRIBUTE_OF_UPDATED_PAYMENT = "updated_payment";
    private static final String NAME_OF_PAGE_WITH_FORM_TO_UPDATE_PAYMENT
            = "administrator/form_to_update_entity/form_to_update_payment";

    @PostMapping(path = {PaymentOperationController.PATH_OF_REQUEST_MAPPING_TO_UPDATE_PAYMENT})
    public final String updatePaymentAndReturnRedirectionToRequestMappingOfPageWithListedPayments(
            @Valid
            @ModelAttribute(name = PaymentOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_UPDATED_PAYMENT)
            final Payment updatedPayment,
            final BindingResult bindingResult,
            @RequestParam(name = PaymentOperationController
                    .NAME_OF_REQUEST_PARAM_OF_CARD_NUMBER_OF_SENDER_OF_UPDATED_PAYMENT)
            final String cardNumberOfSenderOfUpdatedPayment,
            @RequestParam(name = PaymentOperationController
                    .NAME_OF_REQUEST_PARAM_OF_CARD_NUMBER_OF_RECEIVER_OF_UPDATED_PAYMENT)
            final String cardNumberOfReceiverOfUpdatedPayment,
            @RequestParam(name = PaymentOperationController
                    .NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_DATE_PF_UPDATED_PAYMENT)
            final String descriptionOfDateOfUpdatedPayment)
            throws DefiningExistingEntityException, FindingEntityException, NoSuchEntityException,
                   CalendarHandlingException, UpdatingEntityException
    {
        if(!this.paymentCardService.isNotDeletedPaymentCardWithGivenCardNumberExist(cardNumberOfSenderOfUpdatedPayment))
        {
            final String errorCode = PaymentOperationController.PREFIX_OF_ERROR_CODE
                    + PaymentOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_UPDATED_PAYMENT;
            bindingResult.rejectValue(PaymentOperationController.NAME_OF_FIELD_OF_CARD_OF_SENDER_OF_PAYMENT, errorCode,
                    PaymentOperationController.MESSAGE_OF_NOT_EXISTING_PAYMENT_CARD_WITH_GIVEN_CARD_NUMBER);
        }
        if(!this.paymentCardService.isNotDeletedPaymentCardWithGivenCardNumberExist(
                cardNumberOfReceiverOfUpdatedPayment))
        {
            final String errorCode = PaymentOperationController.PREFIX_OF_ERROR_CODE
                    + PaymentOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_UPDATED_PAYMENT;
            bindingResult.rejectValue(PaymentOperationController.NAME_OF_FIELD_OF_CARD_OF_RECEIVER_OF_PAYMENT,
                    errorCode, PaymentOperationController.MESSAGE_OF_NOT_EXISTING_PAYMENT_CARD_WITH_GIVEN_CARD_NUMBER);
        }
        if(!bindingResult.hasErrors())
        {
            final PaymentCard paymentCardOfSenderOfUpdatedPayment = this.paymentCardService
                    .findNotDeletedPaymentCardByCardNumber(cardNumberOfSenderOfUpdatedPayment);
            updatedPayment.setCardOfSender(paymentCardOfSenderOfUpdatedPayment);

            final PaymentCard paymentCardOfReceiverOfUpdatedPayment = this.paymentCardService
                    .findNotDeletedPaymentCardByCardNumber(cardNumberOfReceiverOfUpdatedPayment);
            updatedPayment.setCardOfReceiver(paymentCardOfReceiverOfUpdatedPayment);

            final Calendar dateOfUpdatedPayment = this.paymentService.parseDescriptionOfDateOfPayment(
                    descriptionOfDateOfUpdatedPayment);
            updatedPayment.setDate(dateOfUpdatedPayment);

            this.paymentService.updateEntity(updatedPayment);

            return    PaymentOperationController.PREFIX_TO_REDIRECT
                    + PaymentOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                    + PaymentOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_PAYMENTS;
        }
        else
        {
            return PaymentOperationController.NAME_OF_PAGE_WITH_FORM_TO_UPDATE_PAYMENT;
        }
    }

    private static final String NAME_OF_REQUEST_PARAM_OF_CARD_NUMBER_OF_SENDER_OF_UPDATED_PAYMENT
            = "card_number_of_sender";
    private static final String NAME_OF_REQUEST_PARAM_OF_CARD_NUMBER_OF_RECEIVER_OF_UPDATED_PAYMENT
            = "card_number_of_receiver";
    private static final String NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_DATE_PF_UPDATED_PAYMENT
            = "date_of_updated_payment";

    @GetMapping(path = {PaymentOperationController.PATH_OF_REQUEST_MAPPING_TO_DELETE_PAYMENT})
    public final String deletePaymentAndReturnRedirectionToRequestMappingOfPageWithListedPayments(
            @RequestParam(name = PaymentOperationController.NAME_OF_REQUEST_PARAM_OF_ID_OF_DELETED_PAYMENT)
            final long idOfDeletedPayment)
            throws DeletingEntityException
    {
        this.paymentService.deleteEntity(idOfDeletedPayment);
        return    PaymentOperationController.PREFIX_TO_REDIRECT
                + PaymentOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                + PaymentOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_PAYMENTS;
    }

    public static final String PATH_OF_REQUEST_MAPPING_TO_DELETE_PAYMENT = "/delete";
    public static final String NAME_OF_REQUEST_PARAM_OF_ID_OF_DELETED_PAYMENT = "id_of_deleted_payment";

    @GetMapping(path = {PaymentOperationController.PATH_OF_REQUEST_MAPPING_TO_FIND_PAYMENTS_BY_RANGE_OF_DATES})
    public final String returnNameOfPageListingFoundByRangeOfDatesPayments(
            @RequestParam(name = PaymentOperationController
                    .NAME_OF_REQUEST_PARAM_OF_MINIMUM_DATE_TO_FIND_PAYMENTS_BY_DATE)
            final String descriptionOfMinimumDate,
            @RequestParam(name = PaymentOperationController
                    .NAME_OF_REQUEST_PARAM_OF_MAXIMUM_DATE_TO_FIND_PAYMENTS_BY_DATE)
            final String descriptionOfMaximumDate,
            final Model model)
            throws CalendarHandlingException, OffloadingEntitiesException
    {
        final Calendar minimumDateOfFoundPayments = this.paymentService.parseDescriptionOfDateOfPayment(
                descriptionOfMinimumDate);
        final Calendar maximumDateOfFoundPayments = this.paymentService.parseDescriptionOfDateOfPayment(
                descriptionOfMaximumDate);

        final Collection<Payment> foundPayments = this.paymentService.findPaymentsByRangeOfDates(
                minimumDateOfFoundPayments, maximumDateOfFoundPayments);
        model.addAttribute(PaymentOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_LISTED_PAYMENTS, foundPayments);

        final Map<Payment, String> mapOfPaymentToItsDescriptionOfDate = this.paymentService
                .findMapOfPaymentToItsDescriptionOfDate(foundPayments);
        model.addAttribute(
                PaymentOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_MAP_OF_PAYMENT_TO_ITS_DESCRIPTION_OF_DATE,
                mapOfPaymentToItsDescriptionOfDate);

        return PaymentOperationController.NAME_OF_PAGE_WITH_LISTED_PAYMENTS;
    }

    public static final String PATH_OF_REQUEST_MAPPING_TO_FIND_PAYMENTS_BY_RANGE_OF_DATES
            = "/find_payments_by_range_of_dates";
    private static final String NAME_OF_REQUEST_PARAM_OF_MINIMUM_DATE_TO_FIND_PAYMENTS_BY_DATE
            = "minimum_date_to_find_payments_by_date";
    private static final String NAME_OF_REQUEST_PARAM_OF_MAXIMUM_DATE_TO_FIND_PAYMENTS_BY_DATE
            = "maximum_date_to_find_payments_by_date";
}
