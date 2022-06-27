package by.itacademy.zuevvlad.cardpaymentproject.controller.administrator;

import by.itacademy.zuevvlad.cardpaymentproject.configuration.ServiceConfiguration;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.*;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Client;
import by.itacademy.zuevvlad.cardpaymentproject.entity.PaymentCard;
import by.itacademy.zuevvlad.cardpaymentproject.service.PaymentCardService;
import by.itacademy.zuevvlad.cardpaymentproject.service.exception.NoSuchEntityException;
import by.itacademy.zuevvlad.cardpaymentproject.service.sortingkey.paymentcard.PaymentCardSortingKey;
import by.itacademy.zuevvlad.cardpaymentproject.service.user.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@Controller
@RequestMapping(path = {PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER})
public final class PaymentCardOperationController
{
    public static final String PATH_OF_REQUEST_MAPPING_OF_CONTROLLER = "/administrator/payment_card_operation";

    private final PaymentCardService paymentCardService;
    private final ClientService clientService;

    @Autowired
    public PaymentCardOperationController(
            @Qualifier(value = ServiceConfiguration.NAME_OF_BEAN_OF_PAYMENT_CARD_SERVICE)
            final PaymentCardService paymentCardService,
            @Qualifier(value = ServiceConfiguration.NAME_OF_BEAN_OF_CLIENT_SERVICE)
            final ClientService clientService)
    {
        super();
        this.paymentCardService = paymentCardService;
        this.clientService = clientService;
    }

    @InitBinder
    public final void initBinder(final WebDataBinder webDataBinder)
    {
        //тримит строки при binding-е, если были введены одни пробелы, то будет присвоено полю значиение null
        final StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        webDataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping(path = {PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_PAYMENT_CARDS})
    public final String returnNameOfPageWithListedPaymentCards(final Model model,
                                                               @RequestParam(name = PaymentCardOperationController.
                                                                       NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY,
                                                                             required = false)
                                                               final String descriptionOfSortingKey)
            throws OffloadingEntitiesException
    {
        final Collection<PaymentCard> listedPaymentCards = this.paymentCardService.findAllEntities();
        if(descriptionOfSortingKey == null)
        {
            model.addAttribute(PaymentCardOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_LISTED_PAYMENT_CARDS,
                    listedPaymentCards);
        }
        else
        {
            final PaymentCardSortingKey paymentCardSortingKey = PaymentCardSortingKey.valueOf(descriptionOfSortingKey);
            final Collection<PaymentCard> sortedPaymentCards = this.paymentCardService.sortPaymentCards(
                    listedPaymentCards, paymentCardSortingKey);
            model.addAttribute(PaymentCardOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_LISTED_PAYMENT_CARDS,
                    sortedPaymentCards);
        }
        return PaymentCardOperationController.NAME_OF_PAGE_WITH_LISTED_PAYMENT_CARDS;
    }

    public static final String PATH_OF_REQUEST_MAPPING_TO_LIST_PAYMENT_CARDS = "/list";
    public static final String NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY = "description_of_sorting_key";
    private static final String NAME_OF_MODEL_ATTRIBUTE_OF_LISTED_PAYMENT_CARDS = "listed_payment_cards";
    private static final String NAME_OF_PAGE_WITH_LISTED_PAYMENT_CARDS
            = "administrator/listed_entities/payment_cards";

    @GetMapping(path = {PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_TO_ADD_PAYMENT_CARD})
    public final String returnNameOfPageWithFormToAddNewPaymentCard(final Model model)
    {
        final PaymentCard addedPaymentCard = new PaymentCard();
        model.addAttribute(PaymentCardOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_ADDED_PAYMENT_CARD,
                addedPaymentCard);
        return PaymentCardOperationController.NAME_OF_PAGE_WITH_FORM_TO_ADD_PAYMENT_CARD;
    }

    public static final String PATH_OF_REQUEST_MAPPING_TO_ADD_PAYMENT_CARD = "/add";
    public static final String NAME_OF_MODEL_ATTRIBUTE_OF_ADDED_PAYMENT_CARD = "added_payment_card";
    private static final String NAME_OF_PAGE_WITH_FORM_TO_ADD_PAYMENT_CARD
            = "administrator/form_to_add_entity/form_to_add_payment_card";

    @PostMapping(path = {PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_TO_ADD_PAYMENT_CARD})
    public final String addNewPaymentCardAndReturnRedirectionToRequestMappingOfPageWithListedPaymentCards(
            @Valid
            @ModelAttribute(name = PaymentCardOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_ADDED_PAYMENT_CARD)
            final PaymentCard addedPaymentCard,
            final BindingResult bindingResult,
            @RequestParam(name = PaymentCardOperationController
                    .NAME_OF_REQUEST_PARAM_OF_EMAIL_OF_CLIENT_OF_ADDED_PAYMENT_CARD)
            final String emailOfClientOfAddedPaymentCard,
            @RequestParam(name = PaymentCardOperationController
                    .NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_EXPIRATION_DATE_OF_ADDED_PAYMENT_CARD)
            final String descriptionOfExpirationDateOfAddedPaymentCard)
            throws DefiningExistingEntityException, FindingEntityException, NoSuchEntityException, AddingEntityException
    {
        if(!addedPaymentCard.isDeleted() && this.paymentCardService.isNotDeletedPaymentCardWithGivenCardNumberExist(
                addedPaymentCard.getCardNumber()))
        {
            final String errorCode = PaymentCardOperationController.PREFIX_OF_ERROR_CODE
                    + PaymentCardOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_ADDED_PAYMENT_CARD;
            bindingResult.rejectValue(PaymentCardOperationController.NAME_OF_FIELD_OF_CARD_NUMBER_OF_PAYMENT_CARD,
                    errorCode,
                    PaymentCardOperationController.MESSAGE_OF_ALREADY_EXISTING_PAYMENT_CARD_WITH_GIVEN_CARD_NUMBER);
        }
        if(!this.clientService.isNotDeletedUserWithGivenEmailExist(emailOfClientOfAddedPaymentCard))
        {
            final String errorCode = PaymentCardOperationController.PREFIX_OF_ERROR_CODE
                    + PaymentCardOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_ADDED_PAYMENT_CARD;
            bindingResult.rejectValue(PaymentCardOperationController.NAME_OF_FIELD_OF_CLIENT_OF_PAYMENT_CARD,
                    errorCode, PaymentCardOperationController.MESSAGE_OF_NOT_EXISTING_CLIENT_WITH_GIVEN_EMAIL);
        }
        if(!bindingResult.hasErrors())
        {
            final Client clientOfAddedPaymentCard = this.clientService.findNotDeletedUserByEmail(
                    emailOfClientOfAddedPaymentCard);
            addedPaymentCard.setClient(clientOfAddedPaymentCard);

            final PaymentCard.ExpirationDate expirationDate = this.paymentCardService.parseDescriptionOfExpirationDate(
                    descriptionOfExpirationDateOfAddedPaymentCard);
            addedPaymentCard.setExpirationDate(expirationDate);

            this.paymentCardService.addEntity(addedPaymentCard);
            return    PaymentCardOperationController.PREFIX_TO_REDIRECT
                    + PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                    + PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_PAYMENT_CARDS;
        }
        else
        {
            return PaymentCardOperationController.NAME_OF_PAGE_WITH_FORM_TO_ADD_PAYMENT_CARD;
        }
    }

    private static final String NAME_OF_REQUEST_PARAM_OF_EMAIL_OF_CLIENT_OF_ADDED_PAYMENT_CARD
            = "email_of_client_of_added_payment_card";
    private static final String NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_EXPIRATION_DATE_OF_ADDED_PAYMENT_CARD
            = "description_of_expiration_date_of_added_payment_card";
    private static final String PREFIX_OF_ERROR_CODE = "error.";

    private static final String NAME_OF_FIELD_OF_CARD_NUMBER_OF_PAYMENT_CARD = "cardNumber";
    private static final String MESSAGE_OF_ALREADY_EXISTING_PAYMENT_CARD_WITH_GIVEN_CARD_NUMBER
            = "payment card with given card's number already exists";

    private static final String NAME_OF_FIELD_OF_CLIENT_OF_PAYMENT_CARD = "client";
    private static final String MESSAGE_OF_NOT_EXISTING_CLIENT_WITH_GIVEN_EMAIL
            = "client with given email doesn't exist";

    private static final String PREFIX_TO_REDIRECT = "redirect:";

    @GetMapping(path = {PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_TO_UPDATE_PAYMENT_CARD})
    public final String returnNameOfPageWithFormToUpdatePaymentCard(
            @RequestParam(name = PaymentCardOperationController.NAME_OF_REQUEST_PARAM_OF_ID_OF_UPDATED_PAYMENT_CARD)
            final long idOfUpdatedPaymentCard,
            final Model model)
            throws FindingEntityException, NoSuchEntityException
    {
        final PaymentCard updatedPaymentCard = this.paymentCardService.findEntityById(idOfUpdatedPaymentCard);
        model.addAttribute(PaymentCardOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_UPDATED_PAYMENT_CARD,
                updatedPaymentCard);
        return PaymentCardOperationController.NAME_OF_PAGE_WITH_FORM_TO_UPDATE_PAYMENT_CARD;
    }

    public static final String PATH_OF_REQUEST_MAPPING_TO_UPDATE_PAYMENT_CARD = "/update";
    public static final String NAME_OF_REQUEST_PARAM_OF_ID_OF_UPDATED_PAYMENT_CARD = "id_of_updated_payment_card";
    private static final String NAME_OF_MODEL_ATTRIBUTE_OF_UPDATED_PAYMENT_CARD = "updated_payment_card";
    private static final String NAME_OF_PAGE_WITH_FORM_TO_UPDATE_PAYMENT_CARD
            = "administrator/form_to_update_entity/form_to_update_payment_card";

    @PostMapping(path = {PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_TO_UPDATE_PAYMENT_CARD})
    public final String updatePaymentCardAndReturnRedirectionToRequestMappingOfPageWithListedPaymentCards(
            @Valid
            @ModelAttribute(name = PaymentCardOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_UPDATED_PAYMENT_CARD)
            final PaymentCard updatedPaymentCard,
            final BindingResult bindingResult,
            @RequestParam(name = PaymentCardOperationController
                    .NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_EXPIRATION_DATE_OF_UPDATED_PAYMENT_CARD)
            final String descriptionOfExpirationDateOfUpdatedPaymentCard,
            @RequestParam(name = PaymentCardOperationController
                    .NAME_OF_REQUEST_PARAM_OF_EMAIL_OF_CLIENT_OF_UPDATED_PAYMENT_CARD)
            final String emailOfClientOfUpdatedPaymentCard)
            throws DefiningExistingEntityException, FindingEntityException, NoSuchEntityException, UpdatingEntityException
    {
        final Optional<PaymentCard> optionalOfNotDeletedCardWithGivenCardNumber = this.paymentCardService
                .findOptionalOfNotDeletedPaymentCardByCardNumber(updatedPaymentCard.getCardNumber());
        if(!updatedPaymentCard.isDeleted() && optionalOfNotDeletedCardWithGivenCardNumber.isPresent())
        {
            final PaymentCard notDeletedPaymentCardWithGivenCardNumber = optionalOfNotDeletedCardWithGivenCardNumber
                    .get();
            if(notDeletedPaymentCardWithGivenCardNumber.getId() != updatedPaymentCard.getId())
            {
                final String errorCode = PaymentCardOperationController.PREFIX_OF_ERROR_CODE
                        + PaymentCardOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_UPDATED_PAYMENT_CARD;
                bindingResult.rejectValue(PaymentCardOperationController.NAME_OF_FIELD_OF_CARD_NUMBER_OF_PAYMENT_CARD,
                        errorCode, PaymentCardOperationController
                                .MESSAGE_OF_ALREADY_EXISTING_PAYMENT_CARD_WITH_GIVEN_CARD_NUMBER);
            }
        }
        if(!this.clientService.isNotDeletedUserWithGivenEmailExist(emailOfClientOfUpdatedPaymentCard))
        {
            final String errorCode = PaymentCardOperationController.PREFIX_OF_ERROR_CODE
                    + PaymentCardOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_UPDATED_PAYMENT_CARD;
            bindingResult.rejectValue(PaymentCardOperationController.NAME_OF_FIELD_OF_CLIENT_OF_PAYMENT_CARD,
                    errorCode, PaymentCardOperationController.MESSAGE_OF_NOT_EXISTING_CLIENT_WITH_GIVEN_EMAIL);
        }
        if(!bindingResult.hasErrors())
        {
            final PaymentCard.ExpirationDate newExpirationDate = this.paymentCardService
                    .parseDescriptionOfExpirationDate(descriptionOfExpirationDateOfUpdatedPaymentCard);
            updatedPaymentCard.setExpirationDate(newExpirationDate);

            final Client newClient = this.clientService.findNotDeletedUserByEmail(emailOfClientOfUpdatedPaymentCard);
            updatedPaymentCard.setClient(newClient);

            this.paymentCardService.updateEntity(updatedPaymentCard);

            return    PaymentCardOperationController.PREFIX_TO_REDIRECT
                    + PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                    + PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_PAYMENT_CARDS;
        }
        else
        {
            return PaymentCardOperationController.NAME_OF_PAGE_WITH_FORM_TO_UPDATE_PAYMENT_CARD;
        }
    }

    private static final String NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_EXPIRATION_DATE_OF_UPDATED_PAYMENT_CARD
            = "description_of_expiration_date_of_updated_payment_card";
    private static final String NAME_OF_REQUEST_PARAM_OF_EMAIL_OF_CLIENT_OF_UPDATED_PAYMENT_CARD
            = "email_of_client_of_updated_payment_card";


    @GetMapping(path = {PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_TO_DELETE_PAYMENT_CARD})
    public final String deletePaymentCardAndReturnRedirectionToRequestMappingOfPageWithListedPaymentCards(
            @RequestParam(name = PaymentCardOperationController.NAME_OF_REQUEST_PARAM_OF_ID_OF_DELETED_PAYMENT_CARD)
            final long idOfDeletedPaymentCard)
            throws DeletingEntityException
    {
        this.paymentCardService.deleteEntity(idOfDeletedPaymentCard);
        return    PaymentCardOperationController.PREFIX_TO_REDIRECT
                + PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                + PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_PAYMENT_CARDS;
    }

    public static final String PATH_OF_REQUEST_MAPPING_TO_DELETE_PAYMENT_CARD = "/delete";
    public static final String NAME_OF_REQUEST_PARAM_OF_ID_OF_DELETED_PAYMENT_CARD = "id_of_deleted_payment_card";

    @GetMapping(path = {PaymentCardOperationController.PATH_OF_REQUEST_MAPPING_TO_FIND_PAYMENT_CARDS_BY_CARD_NUMBER})
    public final String returnNameOfPageListingFoundByCardNumberPaymentCards(
            @RequestParam(name = PaymentCardOperationController.NAME_OF_REQUEST_PARAM_OF_CARD_NUMBER_OF_FOUND_PAYMENT_CARDS)
            final String cardNumberOfFoundPaymentCards,
            final Model model)
            throws OffloadingEntitiesException
    {
        final Collection<PaymentCard> foundPaymentCards = this.paymentCardService.findPaymentCardsByCardNumber(
                cardNumberOfFoundPaymentCards);
        model.addAttribute(PaymentCardOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_LISTED_PAYMENT_CARDS,
                foundPaymentCards);
        return PaymentCardOperationController.NAME_OF_PAGE_WITH_LISTED_PAYMENT_CARDS;
    }

    public static final String PATH_OF_REQUEST_MAPPING_TO_FIND_PAYMENT_CARDS_BY_CARD_NUMBER
            = "/find_payment_cards_by_card_number";
    private static final String NAME_OF_REQUEST_PARAM_OF_CARD_NUMBER_OF_FOUND_PAYMENT_CARDS
            = "card_number_of_found_payment_cards";
}
