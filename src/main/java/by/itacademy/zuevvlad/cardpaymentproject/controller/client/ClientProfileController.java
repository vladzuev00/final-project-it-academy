package by.itacademy.zuevvlad.cardpaymentproject.controller.client;

import by.itacademy.zuevvlad.cardpaymentproject.configuration.ServiceConfiguration;
import by.itacademy.zuevvlad.cardpaymentproject.controller.logon.LogOnController;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.DAOException;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.FindingEntityException;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.UpdatingEntityException;
import by.itacademy.zuevvlad.cardpaymentproject.dto.ClientPersonalDataDTO;
import by.itacademy.zuevvlad.cardpaymentproject.dto.modifier.exception.UpdatingEntityByDTOException;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Client;
import by.itacademy.zuevvlad.cardpaymentproject.entity.PaymentCard;
import by.itacademy.zuevvlad.cardpaymentproject.entity.User;
import by.itacademy.zuevvlad.cardpaymentproject.service.PaymentCardService;
import by.itacademy.zuevvlad.cardpaymentproject.service.exception.changepassword.NewPasswordAndItsRepetitionNotEqualException;
import by.itacademy.zuevvlad.cardpaymentproject.service.exception.changepassword.WrongOldPasswordException;
import by.itacademy.zuevvlad.cardpaymentproject.service.statisticsholder.ClientStatisticsHolder;
import by.itacademy.zuevvlad.cardpaymentproject.service.user.ClientService;
import by.itacademy.zuevvlad.cardpaymentproject.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@RequestMapping(path = {ClientProfileController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER})
@Controller(value = "clientProfileController")
@Scope(value = BeanDefinition.SCOPE_SINGLETON)
public final class ClientProfileController
{
    public static final String PATH_OF_REQUEST_MAPPING_OF_CONTROLLER = "/client/profile";

    private final ClientService clientService;
    private final UserService userService;
    private final PaymentCardService paymentCardService;

    @Autowired
    public ClientProfileController(
            @Qualifier(value = ServiceConfiguration.NAME_OF_BEAN_OF_CLIENT_SERVICE)
            final ClientService clientService,
            @Qualifier(value = ServiceConfiguration.NAME_OF_BEAN_OF_USER_SERVICE)
            final UserService userService,
            @Qualifier(value = ServiceConfiguration.NAME_OF_BEAN_OF_PAYMENT_CARD_SERVICE)
            final PaymentCardService paymentCardService)
    {
        super();
        this.clientService = clientService;
        this.userService = userService;
        this.paymentCardService = paymentCardService;
    }

    @InitBinder
    public final void initBinder(final WebDataBinder webDataBinder)
    {
        //тримит строки при binding-е, если были введены одни пробелы, то будет присвоено полю значиение null
        final StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        webDataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping(path = {ClientProfileController.PATH_OF_REQUEST_MAPPING_TO_SHOW_PROFILE_OF_LOGGED_ON_CLIENT})
    public final String returnNameOfPageOfProfileOfLoggedOnClient(
            @SessionAttribute(name = LogOnController.NAME_OF_MODEL_ATTRIBUTE_OF_LOGGED_ON_USER)
            final Client loggedOnClient,
            final Model model)
    {
        final Collection<PaymentCard> notDeletedPaymentCardsOfLoggedOnClient = this.clientService
                .findNotDeletedPaymentCardsOfGivenClient(loggedOnClient);
        model.addAttribute(ClientProfileController.NAME_OF_MODEL_ATTRIBUTE_OF_NOT_DELETED_CARDS_OF_LOGGED_ON_CLIENT,
                notDeletedPaymentCardsOfLoggedOnClient);
        return ClientProfileController.NAME_OF_PAGE_WITH_PROFILE_OF_CLIENT;
    }

    public static final String PATH_OF_REQUEST_MAPPING_TO_SHOW_PROFILE_OF_LOGGED_ON_CLIENT = "/profile";
    private static final String NAME_OF_MODEL_ATTRIBUTE_OF_NOT_DELETED_CARDS_OF_LOGGED_ON_CLIENT
            = "not_deleted_cards_of_logged_on_client";
    private static final String NAME_OF_PAGE_WITH_PROFILE_OF_CLIENT = "client/profile/profile";

    @GetMapping(path = {ClientProfileController.PATH_OF_REQUEST_MAPPING_TO_UPDATE_PERSONAL_DATA_OF_CLIENT})
    public final String returnNameOfPageWithFormToUpdatePersonalDataOfClient(
            @SessionAttribute(name = LogOnController.NAME_OF_MODEL_ATTRIBUTE_OF_LOGGED_ON_USER)
            final Client loggedOnClient,
            final Model model)
    {
        final ClientPersonalDataDTO clientPersonalDataDTO = this.clientService.createClientPersonalDTO(loggedOnClient);
        model.addAttribute(ClientProfileController.NAME_OF_MODEL_ATTRIBUTE_OF_PERSONAL_DATA_DTO, clientPersonalDataDTO);
        return ClientProfileController.NAME_OF_PAGE_WITH_FORM_TO_UPDATE_PERSONAL_DATA;
    }

    public static final String PATH_OF_REQUEST_MAPPING_TO_UPDATE_PERSONAL_DATA_OF_CLIENT = "/update_personal_data";
    private static final String NAME_OF_MODEL_ATTRIBUTE_OF_PERSONAL_DATA_DTO = "personal_data_dto";
    private static final String NAME_OF_PAGE_WITH_FORM_TO_UPDATE_PERSONAL_DATA
            = "client/profile/form_to_update_personal_data";

    @PostMapping(path = {ClientProfileController.PATH_OF_REQUEST_MAPPING_TO_UPDATE_PERSONAL_DATA_OF_CLIENT})
    public final String updatedPersonalDataAndReturnRedirectionToPageOfProfileOfLoggedOnClient(
            @Valid
            @ModelAttribute(name = ClientProfileController.NAME_OF_MODEL_ATTRIBUTE_OF_PERSONAL_DATA_DTO)
            final ClientPersonalDataDTO clientPersonalDataDTO,
            final BindingResult bindingResult,
            @SessionAttribute(name = LogOnController.NAME_OF_MODEL_ATTRIBUTE_OF_LOGGED_ON_USER)
            final Client loggedOnClient)
            throws FindingEntityException, UpdatingEntityException, UpdatingEntityByDTOException
    {
        final Optional<User> optionalOfFoundUserWithGivenEmail = this.userService.findOptionalOfNotDeletedUserByEmail(
                clientPersonalDataDTO.getEmail());
        if(optionalOfFoundUserWithGivenEmail.isPresent())
        {
            final User foundUserWithGivenEmail = optionalOfFoundUserWithGivenEmail.get();
            if(foundUserWithGivenEmail.getId() != loggedOnClient.getId())
            {
                final String errorCode = ClientProfileController.PREFIX_OF_ERROR_CODE
                        + ClientProfileController.NAME_OF_MODEL_ATTRIBUTE_OF_PERSONAL_DATA_DTO;
                bindingResult.rejectValue(ClientProfileController.NAME_OF_FIELD_OF_EMAIL_OF_USER, errorCode,
                        ClientProfileController.MESSAGE_OF_ALREADY_EXISTING_USER_WITH_GIVEN_EMAIL);
            }
        }
        final Optional<Client> optionalOfFoundClientWithGivenPhoneNumber = this.clientService
                .findOptionalOfNotDeletedClientByPhoneNumber(clientPersonalDataDTO.getPhoneNumber());
        if(optionalOfFoundClientWithGivenPhoneNumber.isPresent())
        {
            final Client foundClientWithGivenEmail = optionalOfFoundClientWithGivenPhoneNumber.get();
            if(foundClientWithGivenEmail.getId() != loggedOnClient.getId())
            {
                final String errorCode = ClientProfileController.PREFIX_OF_ERROR_CODE
                        + ClientProfileController.NAME_OF_MODEL_ATTRIBUTE_OF_PERSONAL_DATA_DTO;
                bindingResult.rejectValue(ClientProfileController.NAME_OF_FIELD_OF_PHONE_NUMBER_OF_CLIENT, errorCode,
                        ClientProfileController.MESSAGE_OF_ALREADY_EXISTING_CLIENT_WITH_GIVEN_PHONE_NUMBER);
            }
        }
        if(!bindingResult.hasErrors())
        {
            this.clientService.updateClientByPersonalData(loggedOnClient, clientPersonalDataDTO);
            return    ClientProfileController.PREFIX_TO_REDIRECT
                    + ClientProfileController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                    + ClientProfileController.PATH_OF_REQUEST_MAPPING_TO_SHOW_PROFILE_OF_LOGGED_ON_CLIENT;
        }
        else
        {
            return ClientProfileController.NAME_OF_PAGE_WITH_FORM_TO_UPDATE_PERSONAL_DATA;
        }
    }

    private static final String PREFIX_OF_ERROR_CODE = "error.";

    private static final String NAME_OF_FIELD_OF_EMAIL_OF_USER = "email";
    private static final String MESSAGE_OF_ALREADY_EXISTING_USER_WITH_GIVEN_EMAIL
            = "User with given email already exists";

    private static final String NAME_OF_FIELD_OF_PHONE_NUMBER_OF_CLIENT = "phoneNumber";
    private static final String MESSAGE_OF_ALREADY_EXISTING_CLIENT_WITH_GIVEN_PHONE_NUMBER
            = "Client with given phone number already exists";

    private static final String PREFIX_TO_REDIRECT = "redirect:";

    @GetMapping(path = {ClientProfileController.PATH_OF_REQUEST_MAPPING_TO_CHANGE_PASSWORD})
    public final String returnNameOfPageWithFormToUpdatePassword()
    {
        return ClientProfileController.NAME_OF_PAGE_WITH_FORM_TO_CHANGE_PASSWORD;
    }

    public static final String PATH_OF_REQUEST_MAPPING_TO_CHANGE_PASSWORD = "/change_password";
    private static final String NAME_OF_PAGE_WITH_FORM_TO_CHANGE_PASSWORD = "client/profile/form_to_change_password";

    @PostMapping(path = {ClientProfileController.PATH_OF_REQUEST_MAPPING_TO_CHANGE_PASSWORD})
    public final String changePasswordAndReturnRedirectionToPageOfProfileOfLoggedOnClient(
            @RequestParam(name = ClientProfileController.NAME_OF_REQUEST_PARAM_OF_OLD_PASSWORD)
            final String oldPassword,
            @RequestParam(name = ClientProfileController.NAME_OF_REQUEST_PARAM_OF_NEW_PASSWORD)
            final String newPassword,
            @RequestParam(name = ClientProfileController.NAME_OF_REQUEST_PARAM_OF_REPEATED_NEW_PASSWORD)
            final String repeatedNewPassword,
            @SessionAttribute(name = LogOnController.NAME_OF_MODEL_ATTRIBUTE_OF_LOGGED_ON_USER)
            final Client loggedOnClient,
            final HttpServletRequest httpServletRequest)
            throws UpdatingEntityException
    {
        final String messageOfError;
        try
        {
            this.clientService.changePassword(oldPassword, newPassword, repeatedNewPassword, loggedOnClient);
            return    ClientProfileController.PREFIX_TO_REDIRECT
                    + ClientProfileController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER
                    + ClientProfileController.PATH_OF_REQUEST_MAPPING_TO_SHOW_PROFILE_OF_LOGGED_ON_CLIENT;
        }
        catch(final NewPasswordAndItsRepetitionNotEqualException newPasswordAndItsRepetitionNotEqualException)
        {
            messageOfError = ClientProfileController.MESSAGE_OF_NEW_PASSWORD_AND_ITS_REPETITION_ARE_NOT_EQUAL;
        }
        catch(final WrongOldPasswordException wrongOldPasswordException)
        {
            messageOfError = ClientProfileController.MESSAGE_OF_WRONG_OLD_PASSWORD;
        }
        httpServletRequest.setAttribute(ClientProfileController.NAME_OF_HTTP_REQUEST_ATTRIBUTE_OF_MESSAGE_OF_ERROR,
                messageOfError);
        return ClientProfileController.NAME_OF_PAGE_WITH_FORM_TO_CHANGE_PASSWORD;
    }

    private static final String NAME_OF_REQUEST_PARAM_OF_OLD_PASSWORD = "old_password";
    private static final String NAME_OF_REQUEST_PARAM_OF_NEW_PASSWORD = "new_password";
    private static final String NAME_OF_REQUEST_PARAM_OF_REPEATED_NEW_PASSWORD = "repeated_new_password";

    public static final String NAME_OF_HTTP_REQUEST_ATTRIBUTE_OF_MESSAGE_OF_ERROR = "message_of_error";
    private static final String MESSAGE_OF_NEW_PASSWORD_AND_ITS_REPETITION_ARE_NOT_EQUAL
            = "New password and its repetition aren't equal";
    private static final String MESSAGE_OF_WRONG_OLD_PASSWORD = "Wrong old password";

    @GetMapping(ClientProfileController.PATH_OF_REQUEST_MAPPING_TO_SHOW_STATISTICS_OF_LOGGED_ON_CLIENT)
    public final String returnNameOfPageWithStatisticsOfLoggedOnClient(
            @SessionAttribute(name = LogOnController.NAME_OF_MODEL_ATTRIBUTE_OF_LOGGED_ON_USER)
            final Client loggedOnClient,
            final Model model)
            throws DAOException
    {
        final ClientStatisticsHolder clientStatisticsHolder = this.clientService.collectStatisticsOfClient(
                loggedOnClient);
        model.addAttribute(ClientProfileController.NAME_OF_MODEL_ATTRIBUTE_OF_CLIENT_STATISTICS_HOLDER,
                clientStatisticsHolder);
        return ClientProfileController.NAME_OF_PAGE_WITH_STATISTICS_OF_CLIENT;
    }

    public static final String PATH_OF_REQUEST_MAPPING_TO_SHOW_STATISTICS_OF_LOGGED_ON_CLIENT = "/stats";
    private static final String NAME_OF_MODEL_ATTRIBUTE_OF_CLIENT_STATISTICS_HOLDER = "client_statistics_holder";
    private static final String NAME_OF_PAGE_WITH_STATISTICS_OF_CLIENT = "client/profile/client_statistics_page";
}
