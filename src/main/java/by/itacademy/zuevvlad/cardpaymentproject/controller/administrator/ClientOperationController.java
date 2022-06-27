package by.itacademy.zuevvlad.cardpaymentproject.controller.administrator;

import by.itacademy.zuevvlad.cardpaymentproject.configuration.ServiceConfiguration;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.*;
import by.itacademy.zuevvlad.cardpaymentproject.entity.BankAccount;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Client;
import by.itacademy.zuevvlad.cardpaymentproject.entity.User;
import by.itacademy.zuevvlad.cardpaymentproject.service.BankAccountService;
import by.itacademy.zuevvlad.cardpaymentproject.service.exception.NoSuchEntityException;
import by.itacademy.zuevvlad.cardpaymentproject.service.sortingkey.client.ClientSortingKey;
import by.itacademy.zuevvlad.cardpaymentproject.service.user.ClientService;
import by.itacademy.zuevvlad.cardpaymentproject.service.user.UserService;
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
@RequestMapping(path = {ClientOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER})
public final class ClientOperationController
{
    public static final String PATH_OF_REQUEST_MAPPING_OF_CONTROLLER = "/administrator/client_operation";

    private final ClientService clientService;
    private final UserService userService;
    private final BankAccountService bankAccountService;

    @Autowired
    public ClientOperationController(
            @Qualifier(value = ServiceConfiguration.NAME_OF_BEAN_OF_CLIENT_SERVICE)
            final ClientService clientService,
            @Qualifier(value = ServiceConfiguration.NAME_OF_BEAN_OF_USER_SERVICE)
            final UserService userService,
            @Qualifier(value = ServiceConfiguration.NAME_OF_BEAN_OF_BANK_ACCOUNT_SERVICE)
            final BankAccountService bankAccountService)
    {
        super();
        this.clientService = clientService;
        this.userService = userService;
        this.bankAccountService = bankAccountService;
    }

    @InitBinder
    public final void initBinder(final WebDataBinder webDataBinder)
    {
        //тримит строки при binding-е, если были введены одни пробелы, то будет присвоено полю значиение null
        final StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        webDataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping(path = {ClientOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_CLIENTS})
    public final String returnNameOfPageWithListedClients(final Model model,
                                                          @RequestParam(name = ClientOperationController
                                                                  .NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY,
                                                                        required = false)
                                                          final String descriptionOfSortingKey)
            throws OffloadingEntitiesException
    {
        final Collection<Client> listedClients = this.clientService.findAllEntities();
        if(descriptionOfSortingKey == null)
        {
            model.addAttribute(ClientOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_LISTED_CLIENTS, listedClients);
        }
        else
        {
            final ClientSortingKey clientSortingKey = ClientSortingKey.valueOf(descriptionOfSortingKey);
            final Collection<Client> sortedListedClients = this.clientService.sortClients(listedClients,
                    clientSortingKey);
            model.addAttribute(ClientOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_LISTED_CLIENTS,
                    sortedListedClients);
        }
        return ClientOperationController.NAME_OF_PAGE_WITH_LISTED_ALL_CLIENTS;
    }

    public static final String PATH_OF_REQUEST_MAPPING_TO_LIST_CLIENTS = "/list";
    public static final String NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY = "description_of_sorting_key";
    private static final String NAME_OF_MODEL_ATTRIBUTE_OF_LISTED_CLIENTS = "listed_clients";
    private static final String NAME_OF_PAGE_WITH_LISTED_ALL_CLIENTS = "administrator/listed_entities/clients";

    @GetMapping(path = {ClientOperationController.PATH_OF_REQUEST_MAPPING_TO_ADD_CLIENT})
    public final String returnNameOfPageWithFormToAddNewClient(final Model model)
    {
        final Client addedClient = new Client();
        model.addAttribute(ClientOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_ADDED_CLIENT, addedClient);
        return ClientOperationController.NAME_OF_PAGE_WITH_FORM_TO_ADD_CLIENT;
    }

    public static final String PATH_OF_REQUEST_MAPPING_TO_ADD_CLIENT = "/add";
    public static final String NAME_OF_MODEL_ATTRIBUTE_OF_ADDED_CLIENT = "added_client";
    private static final String NAME_OF_PAGE_WITH_FORM_TO_ADD_CLIENT
            = "administrator/form_to_add_entity/form_to_add_client";

    @PostMapping(path = {ClientOperationController.PATH_OF_REQUEST_MAPPING_TO_ADD_CLIENT})
    public final String addNewClientAndReturnRedirectionToRequestMappingOfPageWithListedClients(
            @Valid
            @ModelAttribute(name = ClientOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_ADDED_CLIENT)
            final Client addedClient,
            final BindingResult bindingResult,
            @RequestParam(name = ClientOperationController
                    .NAME_OF_REQUEST_PARAM_OF_NUMBER_OF_BANK_ACCOUNT_OF_ADDED_CLIENT)
            final String numberOfBankAccountOfAddedClient)
            throws DefiningExistingEntityException, FindingEntityException, NoSuchEntityException, AddingEntityException
    {
        if(!addedClient.isDeleted() && this.userService.isNotDeletedUserWithGivenEmailExist(addedClient.getEmail()))
        {
            final String errorCode = ClientOperationController.PREFIX_OF_ERROR_CODE
                    + ClientOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_ADDED_CLIENT;
            bindingResult.rejectValue(ClientOperationController.NAME_OF_FIELD_OF_EMAIL_OF_CLIENT, errorCode,
                    ClientOperationController.MESSAGE_OF_ALREADY_EXISTING_USER_WITH_GIVEN_EMAIL);
        }
        if(!addedClient.isDeleted() && this.clientService.isNotDeletedClientWithGivenPhoneNumberExist(
                addedClient.getPhoneNumber()))
        {
            final String errorCode = ClientOperationController.PREFIX_OF_ERROR_CODE
                    + ClientOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_ADDED_CLIENT;
            bindingResult.rejectValue(ClientOperationController.NAME_OF_FIELD_OF_PHONE_NUMBER_OF_CLIENT, errorCode,
                    ClientOperationController.MESSAGE_OF_ALREADY_EXISTING_CLIENT_WITH_GIVEN_PHONE_NUMBER);
        }
        if(!this.bankAccountService.isNotDeletedBankAccountWithGivenNumberExist(numberOfBankAccountOfAddedClient))
        {
            final String errorCode = ClientOperationController.PREFIX_OF_ERROR_CODE
                    + ClientOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_ADDED_CLIENT;
            bindingResult.rejectValue(ClientOperationController.NAME_OF_FIELD_OF_BANK_ACCOUNT_OF_CLIENT, errorCode,
                    ClientOperationController.MESSAGE_OF_NOT_EXISTING_BANK_ACCOUNT_WITH_GIVEN_NUMBER);
        }
        if(!bindingResult.hasErrors())
        {
            final BankAccount bankAccountOfAddedClient = this.bankAccountService.findNotDeletedBankAccountByNumber(
                    numberOfBankAccountOfAddedClient);
            addedClient.setBankAccount(bankAccountOfAddedClient);
            this.clientService.addEntity(addedClient);
            return    ClientOperationController.PREFIX_TO_REDIRECT
                    + ClientOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                    + ClientOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_CLIENTS;
        }
        else
        {
            return ClientOperationController.NAME_OF_PAGE_WITH_FORM_TO_ADD_CLIENT;
        }
    }

    private static final String NAME_OF_REQUEST_PARAM_OF_NUMBER_OF_BANK_ACCOUNT_OF_ADDED_CLIENT
            = "number_of_bank_account_of_added_client";

    private static final String PREFIX_OF_ERROR_CODE = "error.";
    private static final String NAME_OF_FIELD_OF_EMAIL_OF_CLIENT = "email";
    private static final String MESSAGE_OF_ALREADY_EXISTING_USER_WITH_GIVEN_EMAIL
            = "user with given email already exists";

    private static final String NAME_OF_FIELD_OF_PHONE_NUMBER_OF_CLIENT = "phoneNumber";
    private static final String MESSAGE_OF_ALREADY_EXISTING_CLIENT_WITH_GIVEN_PHONE_NUMBER
            = "client with given phone number already exists";

    private static final String NAME_OF_FIELD_OF_BANK_ACCOUNT_OF_CLIENT = "bankAccount";
    private static final String MESSAGE_OF_NOT_EXISTING_BANK_ACCOUNT_WITH_GIVEN_NUMBER
            = "bank account with given number doesn't exist";

    private static final String PREFIX_TO_REDIRECT = "redirect:";

    @GetMapping(path = {ClientOperationController.PATH_OF_REQUEST_MAPPING_TO_UPDATE_CLIENT})
    public final String returnNameOfPageWithFormToUpdateClient(
            @RequestParam(name = ClientOperationController.NAME_OF_REQUEST_PARAM_OF_ID_OF_UPDATED_CLIENT)
            final long idOfUpdatedClient,
            final Model model)
            throws FindingEntityException, NoSuchEntityException
    {
        final Client updatedClient = this.clientService.findEntityById(idOfUpdatedClient);
        model.addAttribute(ClientOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_UPDATED_CLIENT, updatedClient);
        return ClientOperationController.NAME_OF_PAGE_WITH_FORM_TO_UPDATE_CLIENT;
    }

    public static final String PATH_OF_REQUEST_MAPPING_TO_UPDATE_CLIENT = "/update";
    public static final String NAME_OF_REQUEST_PARAM_OF_ID_OF_UPDATED_CLIENT = "id_of_updated_client";
    private static final String NAME_OF_MODEL_ATTRIBUTE_OF_UPDATED_CLIENT = "updated_client";
    private static final String NAME_OF_PAGE_WITH_FORM_TO_UPDATE_CLIENT
            = "administrator/form_to_update_entity/form_to_update_client";

    @PostMapping(path = {ClientOperationController.PATH_OF_REQUEST_MAPPING_TO_UPDATE_CLIENT})
    public final String updateClientAndReturnRedirectionToRequestMappingNameOfPageWithListedClients(
            @Valid
            @ModelAttribute(name = ClientOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_UPDATED_CLIENT)
            final Client updatedClient,
            final BindingResult bindingResult,
            @RequestParam(name = ClientOperationController
                    .NAME_OF_REQUEST_PARAM_OF_NUMBER_OF_NEW_BANK_ACCOUNT_OF_UPDATED_CLIENT)
            final String numberOfNewBankAccountOfUpdatedClient)
            throws FindingEntityException, DefiningExistingEntityException, NoSuchEntityException,
                   UpdatingEntityException
    {
        final Optional<User> optionalOfNotDeletedUserWithGivenEmail = this.userService
                .findOptionalOfNotDeletedUserByEmail(updatedClient.getEmail());
        if(!updatedClient.isDeleted() && optionalOfNotDeletedUserWithGivenEmail.isPresent())
        {
            final User notDeletedUserWithGivenEmail = optionalOfNotDeletedUserWithGivenEmail.get();
            if(notDeletedUserWithGivenEmail.getId() != updatedClient.getId())
            {
                final String errorCode = ClientOperationController.PREFIX_OF_ERROR_CODE
                        + ClientOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_UPDATED_CLIENT;
                bindingResult.rejectValue(ClientOperationController.NAME_OF_FIELD_OF_EMAIL_OF_CLIENT, errorCode,
                        ClientOperationController.MESSAGE_OF_ALREADY_EXISTING_USER_WITH_GIVEN_EMAIL);
            }
        }
        final Optional<Client> optionalOfNotDeletedClientWithGivenPhoneNumber = this.clientService
                .findOptionalOfNotDeletedClientByPhoneNumber(updatedClient.getPhoneNumber());
        if(!updatedClient.isDeleted() && optionalOfNotDeletedClientWithGivenPhoneNumber.isPresent())
        {
            final Client notDeletedClientWithGivenPhoneNumber = optionalOfNotDeletedClientWithGivenPhoneNumber.get();
            if(notDeletedClientWithGivenPhoneNumber.getId() != updatedClient.getId())
            {
                final String errorCode = ClientOperationController.PREFIX_OF_ERROR_CODE
                        + ClientOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_UPDATED_CLIENT;
                bindingResult.rejectValue(ClientOperationController.NAME_OF_FIELD_OF_PHONE_NUMBER_OF_CLIENT, errorCode,
                        ClientOperationController.MESSAGE_OF_ALREADY_EXISTING_CLIENT_WITH_GIVEN_PHONE_NUMBER);
            }
        }
        if(!this.bankAccountService.isNotDeletedBankAccountWithGivenNumberExist(numberOfNewBankAccountOfUpdatedClient))
        {
            final String errorCode = ClientOperationController.PREFIX_OF_ERROR_CODE
                    + ClientOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_UPDATED_CLIENT;
            bindingResult.rejectValue(ClientOperationController.NAME_OF_FIELD_OF_BANK_ACCOUNT_OF_CLIENT, errorCode,
                    ClientOperationController.MESSAGE_OF_NOT_EXISTING_BANK_ACCOUNT_WITH_GIVEN_NUMBER);
        }
        if(!bindingResult.hasErrors())
        {
            final BankAccount newBankAccountOfUpdatedClient = this.bankAccountService.findNotDeletedBankAccountByNumber(
                    numberOfNewBankAccountOfUpdatedClient);
            updatedClient.setBankAccount(newBankAccountOfUpdatedClient);
            this.clientService.updateEntity(updatedClient);
            return    ClientOperationController.PREFIX_TO_REDIRECT
                    + ClientOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                    + ClientOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_CLIENTS;
        }
        else
        {
            return ClientOperationController.NAME_OF_PAGE_WITH_FORM_TO_UPDATE_CLIENT;
        }
    }

    private static final String NAME_OF_REQUEST_PARAM_OF_NUMBER_OF_NEW_BANK_ACCOUNT_OF_UPDATED_CLIENT
            = "number_of_new_bank_account_of_updated_client";

    @GetMapping(path = {ClientOperationController.PATH_OF_REQUEST_MAPPING_TO_DELETE_CLIENT})
    public final String deleteClientAndReturnRedirectionToRequestMappingOfPageWithListedClients(
            @RequestParam(name = ClientOperationController.NAME_OF_REQUEST_PARAM_OF_ID_OF_DELETED_CLIENT)
            final long idOfDeletedClient)
            throws DeletingEntityException
    {
        this.clientService.deleteEntity(idOfDeletedClient);
        return    ClientOperationController.PREFIX_TO_REDIRECT
                + ClientOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                + ClientOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_CLIENTS;
    }

    public static final String PATH_OF_REQUEST_MAPPING_TO_DELETE_CLIENT = "/delete";
    public static final String NAME_OF_REQUEST_PARAM_OF_ID_OF_DELETED_CLIENT = "id_of_deleted_client";

    @GetMapping(path = {ClientOperationController.PATH_OF_REQUEST_MAPPING_TO_FIND_CLIENTS_BY_EMAIL})
    public final String returnNameOfPageListingFoundByEmailClients(
            @RequestParam(name = ClientOperationController.NAME_OF_REQUEST_PARAM_OF_EMAIL_OF_FOUND_CLIENTS)
            final String emailOfFoundClients,
            final Model model)
            throws OffloadingEntitiesException
    {
        final Collection<Client> foundClients = this.clientService.findUsersByEmail(emailOfFoundClients);
        model.addAttribute(ClientOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_LISTED_CLIENTS, foundClients);
        return ClientOperationController.NAME_OF_PAGE_WITH_LISTED_ALL_CLIENTS;
    }

    public static final String PATH_OF_REQUEST_MAPPING_TO_FIND_CLIENTS_BY_EMAIL = "/find_clients_by_email";
    private static final String NAME_OF_REQUEST_PARAM_OF_EMAIL_OF_FOUND_CLIENTS = "email_of_found_clients";
}
