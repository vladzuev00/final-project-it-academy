package by.itacademy.zuevvlad.cardpaymentproject.controller.administrator;

import by.itacademy.zuevvlad.cardpaymentproject.controller.logon.LogOnController;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.*;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Administrator;
import by.itacademy.zuevvlad.cardpaymentproject.entity.User;
import by.itacademy.zuevvlad.cardpaymentproject.service.exception.NoSuchEntityException;
import by.itacademy.zuevvlad.cardpaymentproject.service.sortingkey.administrator.AdministratorSortingKey;
import by.itacademy.zuevvlad.cardpaymentproject.service.user.AdministratorService;
import by.itacademy.zuevvlad.cardpaymentproject.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping(path = {AdministratorOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER})
public final class AdministratorOperationController
{
    public static final String PATH_OF_REQUEST_MAPPING_OF_CONTROLLER = "/administrator/administrator_operation";

    private final AdministratorService administratorService;
    private final UserService userService;

    @Autowired
    public AdministratorOperationController(final AdministratorService administratorService,
                                            final UserService userService)
    {
        super();
        this.administratorService = administratorService;
        this.userService = userService;
    }

    @InitBinder
    public final void initBinder(final WebDataBinder webDataBinder)
    {
        //тримит строки при binding-е, если были введены одни пробелы, то будет присвоено полю значиение null
        final StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        webDataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping(path = {AdministratorOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_ADMINISTRATORS})
    public final String returnNameOfPageWithListedAdministrators(final Model model,
                                                                 @RequestParam(name = AdministratorOperationController
                                                                         .NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY,
                                                                 required = false)
                                                                 final String descriptionOfSortingKey)
            throws OffloadingEntitiesException
    {
        final Collection<Administrator> listedAdministrators = this.administratorService.findAllEntities();
        if(descriptionOfSortingKey == null)
        {
            model.addAttribute(AdministratorOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_LISTED_ADMINISTRATORS,
                    listedAdministrators);
        }
        else
        {
            final AdministratorSortingKey administratorSortingKey = AdministratorSortingKey.valueOf(
                    descriptionOfSortingKey);
            final Collection<Administrator> sortedAdministrators = this.administratorService.sortAdministrators(
                    listedAdministrators, administratorSortingKey);
            model.addAttribute(AdministratorOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_LISTED_ADMINISTRATORS,
                    sortedAdministrators);
        }
        return AdministratorOperationController.NAME_OF_PAGE_WITH_LISTED_ADMINISTRATORS;
    }

    public static final String PATH_OF_REQUEST_MAPPING_TO_LIST_ADMINISTRATORS = "/list";
    public static final String NAME_OF_REQUEST_PARAM_OF_DESCRIPTION_OF_SORTING_KEY = "description_of_sorting_key";
    private static final String NAME_OF_MODEL_ATTRIBUTE_OF_LISTED_ADMINISTRATORS = "listed_administrators";
    private static final String NAME_OF_PAGE_WITH_LISTED_ADMINISTRATORS
            = "administrator/listed_entities/administrators";

    @GetMapping(path = {AdministratorOperationController.PATH_OF_REQUEST_MAPPING_TO_ADD_ADMINISTRATOR})
    public final String returnNameOfPageWithFormToAddNewAdministrator(final Model model)
    {
        final Administrator addedAdministrator = new Administrator();
        model.addAttribute(AdministratorOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_ADDED_ADMINISTRATOR,
                addedAdministrator);

        final Map<Administrator.Level, String> administratorLevelsAndTheirDescriptions = this.administratorService
                .findAdministratorLevelsAndTheirDescriptions();
        model.addAttribute(
                AdministratorOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_ADMINISTRATORS_LEVEL_AND_THEIR_DESCRIPTIONS,
                administratorLevelsAndTheirDescriptions);

        return AdministratorOperationController.NAME_OF_PAGE_WITH_FORM_TO_ADD_ADMINISTRATOR;
    }

    public static final String PATH_OF_REQUEST_MAPPING_TO_ADD_ADMINISTRATOR = "/add";
    public static final String NAME_OF_MODEL_ATTRIBUTE_OF_ADDED_ADMINISTRATOR = "added_administrator";
    private static final String NAME_OF_MODEL_ATTRIBUTE_OF_ADMINISTRATORS_LEVEL_AND_THEIR_DESCRIPTIONS
            = "administrator_levels_and_their_descriptions";
    private static final String NAME_OF_PAGE_WITH_FORM_TO_ADD_ADMINISTRATOR
            = "administrator/form_to_add_entity/form_to_add_administrator";

    @PostMapping(path = {AdministratorOperationController.PATH_OF_REQUEST_MAPPING_TO_ADD_ADMINISTRATOR})
    public final String addNewAdministratorAndReturnRedirectionToRequestMappingOfPageWithListedAdministrators(
            @Valid
            @ModelAttribute(name = AdministratorOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_ADDED_ADMINISTRATOR)
            final Administrator addedAdministrator,
            final BindingResult bindingResult)
            throws DefiningExistingEntityException, AddingEntityException
    {
        if(!addedAdministrator.isDeleted() && this.userService.isNotDeletedUserWithGivenEmailExist(
                addedAdministrator.getEmail()))
        {
            final String errorCode = AdministratorOperationController.PREFIX_OF_ERROR_CODE
                    + AdministratorOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_ADDED_ADMINISTRATOR;
            bindingResult.rejectValue(AdministratorOperationController.NAME_OF_FIELD_OF_EMAIL_OF_USER, errorCode,
                    AdministratorOperationController.MESSAGE_OF_ALREADY_EXISTING_USER_WITH_GIVEN_EMAIL);
        }
        if(!bindingResult.hasErrors())
        {
            this.administratorService.addEntity(addedAdministrator);
            return    AdministratorOperationController.PREFIX_TO_REDIRECT
                    + AdministratorOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                    + AdministratorOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_ADMINISTRATORS;
        }
        else
        {
            return AdministratorOperationController.NAME_OF_PAGE_WITH_FORM_TO_ADD_ADMINISTRATOR;
        }
    }

    private static final String PREFIX_OF_ERROR_CODE = "error.";
    private static final String NAME_OF_FIELD_OF_EMAIL_OF_USER = "email";
    private static final String MESSAGE_OF_ALREADY_EXISTING_USER_WITH_GIVEN_EMAIL
            = "user with given email already exist";
    private static final String PREFIX_TO_REDIRECT = "redirect:";

    @GetMapping(path = {AdministratorOperationController.PATH_OF_REQUEST_MAPPING_TO_UPDATE_ADMINISTRATOR})
    public final String returnNameOfPageWithFormToUpdateAdministrator(
            @RequestParam(name = AdministratorOperationController.NAME_OF_REQUEST_PARAM_OF_ID_OF_UPDATED_ADMINISTRATOR)
            final long idOfUpdatedAdministrator,
            final Model model)
            throws FindingEntityException, NoSuchEntityException
    {
        final Administrator updatedAdministrator = this.administratorService.findEntityById(idOfUpdatedAdministrator);
        model.addAttribute(AdministratorOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_UPDATED_ADMINISTRATOR,
                updatedAdministrator);

        final Map<Administrator.Level, String> administratorLevelsAndTheirDescriptions = this.administratorService
                .findAdministratorLevelsAndTheirDescriptions();
        model.addAttribute(
                AdministratorOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_ADMINISTRATORS_LEVEL_AND_THEIR_DESCRIPTIONS,
                administratorLevelsAndTheirDescriptions);

        return AdministratorOperationController.NAME_OF_PAGE_WITH_FORM_TO_UPDATE_ADMINISTRATOR;
    }

    public static final String PATH_OF_REQUEST_MAPPING_TO_UPDATE_ADMINISTRATOR = "/update";
    public static final String NAME_OF_REQUEST_PARAM_OF_ID_OF_UPDATED_ADMINISTRATOR = "id_of_updated_administrator";
    private static final String NAME_OF_MODEL_ATTRIBUTE_OF_UPDATED_ADMINISTRATOR = "updated_administrator";
    private static final String NAME_OF_PAGE_WITH_FORM_TO_UPDATE_ADMINISTRATOR
            = "administrator/form_to_update_entity/form_to_update_administrator";

    @PostMapping(path = {AdministratorOperationController.PATH_OF_REQUEST_MAPPING_TO_UPDATE_ADMINISTRATOR})
    public final String updateAdministratorAndReturnRedirectionToRequestMappingOfPageWithListedAdministrators(
            final HttpServletRequest httpServletRequest,
            @Valid
            @ModelAttribute(name = AdministratorOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_UPDATED_ADMINISTRATOR)
            final Administrator updatedAdministrator,
            final BindingResult bindingResult)
            throws FindingEntityException, UpdatingEntityException
    {
        final HttpSession httpSession = httpServletRequest.getSession();
        final User loggedOnUser = (User)httpSession.getAttribute(
                LogOnController.NAME_OF_MODEL_ATTRIBUTE_OF_LOGGED_ON_USER);
        if(loggedOnUser.getId() == updatedAdministrator.getId() && updatedAdministrator.isDeleted())
        {
            httpServletRequest.setAttribute(
                    AdministratorOperationController.NAME_OF_REQUEST_ATTRIBUTE_OF_DELETING_YOURSELF_ERROR,
                    AdministratorOperationController.MESSAGE_OF_DELETING_YOURSELF_ERROR);
            return AdministratorOperationController.NAME_OF_PAGE_WITH_FORM_TO_UPDATE_ADMINISTRATOR;
        }
        final Optional<User> optionalOfNotDeletedUserWithGivenEmail = this.userService
                .findOptionalOfNotDeletedUserByEmail(updatedAdministrator.getEmail());
        if(!updatedAdministrator.isDeleted() && optionalOfNotDeletedUserWithGivenEmail.isPresent())
        {
            final User notDeletedUserWithGivenEmail = optionalOfNotDeletedUserWithGivenEmail.get();
            if(notDeletedUserWithGivenEmail.getId() != updatedAdministrator.getId())
            {
                final String errorCode = AdministratorOperationController.PREFIX_OF_ERROR_CODE
                        + AdministratorOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_UPDATED_ADMINISTRATOR;
                bindingResult.rejectValue(AdministratorOperationController.NAME_OF_FIELD_OF_EMAIL_OF_USER, errorCode,
                        AdministratorOperationController.MESSAGE_OF_ALREADY_EXISTING_USER_WITH_GIVEN_EMAIL);
            }
        }
        if(!bindingResult.hasErrors())
        {
            this.administratorService.updateEntity(updatedAdministrator);
            return    AdministratorOperationController.PREFIX_TO_REDIRECT
                    + AdministratorOperationController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                    + AdministratorOperationController.PATH_OF_REQUEST_MAPPING_TO_LIST_ADMINISTRATORS;
        }
        else
        {
            return AdministratorOperationController.NAME_OF_PAGE_WITH_FORM_TO_UPDATE_ADMINISTRATOR;
        }
    }

    public static final String NAME_OF_REQUEST_ATTRIBUTE_OF_DELETING_YOURSELF_ERROR = "deleting_yourself_error";
    private static final String MESSAGE_OF_DELETING_YOURSELF_ERROR = "impossible to delete yourself";

    @GetMapping(path = {AdministratorOperationController.PATH_OF_REQUEST_MAPPING_TO_DELETE_ADMINISTRATOR})
    public final String deleteAdministratorAndReturnNameOfPageWithListedAdministrators(
            final HttpServletRequest httpServletRequest,
            @RequestParam(name = AdministratorOperationController.NAME_OF_REQUEST_PARAM_OF_ID_OF_DELETED_ADMINISTRATOR)
            final long idOfDeletedAdministrator, final Model model)
            throws DeletingEntityException, OffloadingEntitiesException
    {
        final HttpSession httpSession = httpServletRequest.getSession();
        final User loggedOnUser = (User)httpSession.getAttribute(
                LogOnController.NAME_OF_MODEL_ATTRIBUTE_OF_LOGGED_ON_USER);
        if(loggedOnUser.getId() != idOfDeletedAdministrator)
        {
            this.administratorService.deleteEntity(idOfDeletedAdministrator);
        }
        else
        {
            httpServletRequest.setAttribute(
                    AdministratorOperationController.NAME_OF_REQUEST_ATTRIBUTE_OF_DELETING_YOURSELF_ERROR,
                    AdministratorOperationController.MESSAGE_OF_DELETING_YOURSELF_ERROR);
        }
        final Collection<Administrator> listedAdministrators = this.administratorService.findAllEntities();
        model.addAttribute(AdministratorOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_LISTED_ADMINISTRATORS,
                listedAdministrators);
        return AdministratorOperationController.NAME_OF_PAGE_WITH_LISTED_ADMINISTRATORS;
    }

    public static final String PATH_OF_REQUEST_MAPPING_TO_DELETE_ADMINISTRATOR = "/delete";
    public static final String NAME_OF_REQUEST_PARAM_OF_ID_OF_DELETED_ADMINISTRATOR = "id_of_deleted_administrator";

    @GetMapping(path = {AdministratorOperationController.PATH_OF_REQUEST_MAPPING_TO_FIND_ADMINISTRATORS_BY_EMAIL})
    public final String returnNameOfPageListingFoundByEmailAdministrators(
            @RequestParam(name = AdministratorOperationController
                    .NAME_OF_REQUEST_PARAM_OF_EMAIL_OF_FOUND_ADMINISTRATORS)
            final String emailOfFoundAdministrators,
            final Model model)
            throws OffloadingEntitiesException
    {
        final Collection<Administrator> foundAdministrators = this.administratorService.findUsersByEmail(
                emailOfFoundAdministrators);
        model.addAttribute(AdministratorOperationController.NAME_OF_MODEL_ATTRIBUTE_OF_LISTED_ADMINISTRATORS,
                foundAdministrators);
        return AdministratorOperationController.NAME_OF_PAGE_WITH_LISTED_ADMINISTRATORS;
    }

    public static final String PATH_OF_REQUEST_MAPPING_TO_FIND_ADMINISTRATORS_BY_EMAIL
            = "/find_administrators_by_email";
    private static final String NAME_OF_REQUEST_PARAM_OF_EMAIL_OF_FOUND_ADMINISTRATORS = "email_of_found_administrators";
}
