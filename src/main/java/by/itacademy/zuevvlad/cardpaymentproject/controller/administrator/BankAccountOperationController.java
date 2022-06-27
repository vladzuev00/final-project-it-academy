package by.itacademy.zuevvlad.cardpaymentproject.controller.administrator;

import by.itacademy.zuevvlad.cardpaymentproject.configuration.ServiceConfiguration;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.*;
import by.itacademy.zuevvlad.cardpaymentproject.service.exception.NoSuchEntityException;
import by.itacademy.zuevvlad.cardpaymentproject.entity.BankAccount;
import by.itacademy.zuevvlad.cardpaymentproject.service.BankAccountService;
import by.itacademy.zuevvlad.cardpaymentproject.service.sortingkey.bankaccount.BankAccountSortingKey;
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
@RequestMapping(path = {BankAccountOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER})
public final class BankAccountOperationController
{
    public static final String PATH_OF_REQUEST_MAPPING_OF_CONTROLLER = "/administrator/bank_account_operation";

    private final BankAccountService bankAccountService;

    @Autowired
    public BankAccountOperationController(
            @Qualifier(value = ServiceConfiguration.NAME_OF_BEAN_OF_BANK_ACCOUNT_SERVICE)
            final BankAccountService bankAccountService)
    {
        super();
        this.bankAccountService = bankAccountService;
    }

    @InitBinder
    public final void initBinder(final WebDataBinder webDataBinder)
    {
        //тримит строки при binding-е, если были введены одни пробелы, то будет присвоено полю значиение null
        final StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        webDataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping(path = {BankAccountOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_BANK_ACCOUNTS})
    public final String returnNameOfPageWithListedBankAccounts(final Model model,
                                                               @RequestParam(name = BankAccountOperationController.
                                                                       NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY,
                                                                             required = false)
                                                               final String descriptionOfSortingKey)
            throws OffloadingEntitiesException
    {
        final Collection<BankAccount> listedBankAccounts = this.bankAccountService.findAllEntities();
        if(descriptionOfSortingKey == null)
        {
            model.addAttribute(BankAccountOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_LISTED_BANK_ACCOUNTS,
                    listedBankAccounts);
        }
        else
        {
            final BankAccountSortingKey bankAccountSortingKey = BankAccountSortingKey.valueOf(descriptionOfSortingKey);
            final Collection<BankAccount> sortedBankAccounts = this.bankAccountService.sortBankAccounts(
                    listedBankAccounts, bankAccountSortingKey);
            model.addAttribute(BankAccountOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_LISTED_BANK_ACCOUNTS,
                    sortedBankAccounts);
        }
        return BankAccountOperationController.NAME_OF_PAGE_WITH_LISTED_BANK_ACCOUNTS;
    }

    public static final String PATH_OF_REQUEST_MAPPING_TO_LIST_BANK_ACCOUNTS = "/list";
    public static final String NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY = "description_of_sorting_key";
    private static final String NAME_OF_MODEL_ATTRIBUTE_OF_LISTED_BANK_ACCOUNTS = "listed_bank_accounts";
    private static final String NAME_OF_PAGE_WITH_LISTED_BANK_ACCOUNTS = "administrator/listed_entities/bank_accounts";

    @GetMapping(path = {BankAccountOperationController.PATH_OF_REQUEST_MAPPING_TO_ADD_BANK_ACCOUNT})
    public final String returnNameOfPageWithFormToAddNewBankAccount(final Model model)
    {
        final BankAccount addedBankAccount = new BankAccount();
        model.addAttribute(BankAccountOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_ADDED_BANK_ACCOUNT,
                addedBankAccount);
        return BankAccountOperationController.NAME_OF_PAGE_WITH_FORM_TO_ADD_BANK_ACCOUNT;
    }

    public static final String PATH_OF_REQUEST_MAPPING_TO_ADD_BANK_ACCOUNT = "/add";
    public static final String NAME_OF_MODEL_ATTRIBUTE_OF_ADDED_BANK_ACCOUNT = "added_bank_account";
    private static final String NAME_OF_PAGE_WITH_FORM_TO_ADD_BANK_ACCOUNT
            = "administrator/form_to_add_entity/form_to_add_bank_account";

    @PostMapping(path = {BankAccountOperationController.PATH_OF_REQUEST_MAPPING_TO_ADD_BANK_ACCOUNT})
    public final String addNewBankAccountAndReturnRedirectionToRequestMappingOfPageWithListedBankAccounts(
            @Valid
            @ModelAttribute(name = BankAccountOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_ADDED_BANK_ACCOUNT)
            final BankAccount addedBankAccount,
            final BindingResult bindingResult)
            throws DefiningExistingEntityException, AddingEntityException
    {
        if(!addedBankAccount.isDeleted() && this.bankAccountService.isNotDeletedBankAccountWithGivenNumberExist(
                addedBankAccount.getNumber()))
        {
            final String errorCode = BankAccountOperationController.PREFIX_OF_ERROR_CODE
                    + BankAccountOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_ADDED_BANK_ACCOUNT;
            bindingResult.rejectValue(BankAccountOperationController.NAME_OF_FIELD_OF_NUMBER_OF_BANK_ACCOUNT, errorCode,
                    BankAccountOperationController.MESSAGE_OF_ALREADY_EXISTING_BANK_ACCOUNT_WITH_GIVEN_NUMBER);
        }
        if(!bindingResult.hasErrors())
        {
            this.bankAccountService.addEntity(addedBankAccount);
            return    BankAccountOperationController.PREFIX_TO_REDIRECT
                    + BankAccountOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                    + BankAccountOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_BANK_ACCOUNTS;
        }
        else
        {
            return BankAccountOperationController.NAME_OF_PAGE_WITH_FORM_TO_ADD_BANK_ACCOUNT;
        }
    }

    private static final String PREFIX_OF_ERROR_CODE = "error.";
    private static final String NAME_OF_FIELD_OF_NUMBER_OF_BANK_ACCOUNT = "number";
    private static final String MESSAGE_OF_ALREADY_EXISTING_BANK_ACCOUNT_WITH_GIVEN_NUMBER
            = "bank account with given number already exists";
    private static final String PREFIX_TO_REDIRECT = "redirect:";

    @GetMapping(path = {BankAccountOperationController.PATH_OF_REQUEST_MAPPING_TO_UPDATE_BANK_ACCOUNT})
    public final String returnNameOfPageWithFormToUpdateBankAccount(
            @RequestParam(name = BankAccountOperationController.NAME_OF_REQUEST_PARAM_OF_ID_OF_UPDATED_BANK_ACCOUNT)
            final long idOfUpdatedBankAccount,
            final Model model)
            throws FindingEntityException, NoSuchEntityException
    {
        final BankAccount updatedBankAccount = this.bankAccountService.findEntityById(idOfUpdatedBankAccount);
        model.addAttribute(BankAccountOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_UPDATED_BANK_ACCOUNT,
                updatedBankAccount);
        return BankAccountOperationController.NAME_OF_PAGE_WITH_FORM_TO_UPDATE_BANK_ACCOUNT;
    }

    public static final String PATH_OF_REQUEST_MAPPING_TO_UPDATE_BANK_ACCOUNT = "/update";
    public static final String NAME_OF_REQUEST_PARAM_OF_ID_OF_UPDATED_BANK_ACCOUNT = "id_of_updated_bank_account";
    private static final String NAME_OF_MODEL_ATTRIBUTE_OF_UPDATED_BANK_ACCOUNT = "updated_bank_account";
    private static final String NAME_OF_PAGE_WITH_FORM_TO_UPDATE_BANK_ACCOUNT
            = "administrator/form_to_update_entity/form_to_update_bank_account";

    @PostMapping(path = {BankAccountOperationController.PATH_OF_REQUEST_MAPPING_TO_UPDATE_BANK_ACCOUNT})
    public final String updateBankAccountAndReturnRedirectionToRequestMappingOfPageWithListedBankAccounts(
            @Valid
            @ModelAttribute(name = BankAccountOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_UPDATED_BANK_ACCOUNT)
            final BankAccount updatedBankAccount,
            final BindingResult bindingResult)
            throws FindingEntityException, UpdatingEntityException
    {
        final Optional<BankAccount> optionalOfNotDeletedBankAccountWithGivenNumber = this.bankAccountService
                .findOptionalOfNotDeletedBankAccountByNumber(updatedBankAccount.getNumber());
        if(!updatedBankAccount.isDeleted() && optionalOfNotDeletedBankAccountWithGivenNumber.isPresent())
        {
            final BankAccount notDeletedBankAccountWithGivenNumber
                    = optionalOfNotDeletedBankAccountWithGivenNumber.get();
            if(notDeletedBankAccountWithGivenNumber.getId() != updatedBankAccount.getId())
            {
                final String errorCode = BankAccountOperationController.PREFIX_OF_ERROR_CODE
                        + BankAccountOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_UPDATED_BANK_ACCOUNT;
                bindingResult.rejectValue(BankAccountOperationController.NAME_OF_FIELD_OF_NUMBER_OF_BANK_ACCOUNT,
                        errorCode,
                        BankAccountOperationController.MESSAGE_OF_ALREADY_EXISTING_BANK_ACCOUNT_WITH_GIVEN_NUMBER);
            }
        }
        if(!bindingResult.hasErrors())
        {
            this.bankAccountService.updateEntity(updatedBankAccount);
            return    BankAccountOperationController.PREFIX_TO_REDIRECT
                    + BankAccountOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                    + BankAccountOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_BANK_ACCOUNTS;
        }
        else
        {
            return BankAccountOperationController.NAME_OF_PAGE_WITH_FORM_TO_UPDATE_BANK_ACCOUNT;
        }
    }

    @GetMapping(path = {BankAccountOperationController.PATH_OF_REQUEST_MAPPING_TO_DELETE_BANK_ACCOUNT})
    public final String deleteBankAccountAndReturnRedirectionToRequestMappingOfPageWithListedBankAccounts(
            @RequestParam(name = BankAccountOperationController.NAME_OF_REQUEST_PARAM_OF_ID_OF_DELETED_BANK_ACCOUNT)
            final long idOfDeletedBankAccount)
            throws DeletingEntityException
    {
        this.bankAccountService.deleteEntity(idOfDeletedBankAccount);
        return    BankAccountOperationController.PREFIX_TO_REDIRECT
                + BankAccountOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                + BankAccountOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_BANK_ACCOUNTS;
    }

    public static final String PATH_OF_REQUEST_MAPPING_TO_DELETE_BANK_ACCOUNT = "/delete";
    public static final String NAME_OF_REQUEST_PARAM_OF_ID_OF_DELETED_BANK_ACCOUNT = "id_of_deleted_bank_account";

    @GetMapping(path = {BankAccountOperationController.PATH_OF_REQUEST_MAPPING_TO_FIND_BANK_ACCOUNTS_BY_NUMBER})
    public final String returnNameOfPageListingFoundByNumberBankAccounts(
            @RequestParam(name = BankAccountOperationController.NAME_OF_REQUEST_PARAM_OF_NUMBER_OF_FOUND_BANK_ACCOUNTS)
            final String numberOfFoundBankAccounts,
            final Model model)
            throws OffloadingEntitiesException
    {
        final Collection<BankAccount> foundBankAccounts = this.bankAccountService
                .findBankAccountsByNumber(numberOfFoundBankAccounts);
        model.addAttribute(BankAccountOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_LISTED_BANK_ACCOUNTS,
                foundBankAccounts);
        return BankAccountOperationController.NAME_OF_PAGE_WITH_LISTED_BANK_ACCOUNTS;
    }

    public static final String PATH_OF_REQUEST_MAPPING_TO_FIND_BANK_ACCOUNTS_BY_NUMBER = "/find_bank_accounts_by_number";
    private static final String NAME_OF_REQUEST_PARAM_OF_NUMBER_OF_FOUND_BANK_ACCOUNTS = "number_of_found_bank_accounts";
}
