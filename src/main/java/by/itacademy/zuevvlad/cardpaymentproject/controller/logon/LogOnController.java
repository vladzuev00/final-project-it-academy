package by.itacademy.zuevvlad.cardpaymentproject.controller.logon;

import by.itacademy.zuevvlad.cardpaymentproject.controller.logon.exception.NotBindingUserDataException;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.DefiningExistingEntityException;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.FindingEntityException;
import by.itacademy.zuevvlad.cardpaymentproject.entity.User;
import by.itacademy.zuevvlad.cardpaymentproject.service.exception.NoSuchEntityException;
import by.itacademy.zuevvlad.cardpaymentproject.service.user.AdministratorService;
import by.itacademy.zuevvlad.cardpaymentproject.service.user.ClientService;
import by.itacademy.zuevvlad.cardpaymentproject.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = {LogOnController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER})
@SessionAttributes(value = {LogOnController.NAME_OF_MODEL_ATTRIBUTE_OF_LOGGED_ON_USER})
public final class LogOnController
{
    public static final String PATH_OF_REQUEST_MAPPING_OF_CONTROLLER = "/logging_on";

    private final UserService userService;
    private final ClientService clientService;
    private final AdministratorService administratorService;

    @Autowired
    public LogOnController(final UserService userService, final ClientService clientService,
                           final AdministratorService administratorService)
    {
        super();
        this.userService = userService;
        this.clientService = clientService;
        this.administratorService = administratorService;
    }

    @GetMapping(path = {LogOnController.PATH_OF_REQUEST_MAPPING_OF_LOG_ON_PAGE})
    public final String returnNameOfLogOnPage()
    {
        return LogOnController.NAME_OF_LOG_ON_PAGE;
    }

    public static final String PATH_OF_REQUEST_MAPPING_OF_LOG_ON_PAGE = "/log_on_page";
    private static final String NAME_OF_LOG_ON_PAGE = "log_on_page";

    @GetMapping(path = {LogOnController.PATH_OF_REQUEST_MAPPING_TO_LOG_ON})
    public final String returnNameOfResultPageOfLogOn(
            @RequestParam(name = LogOnController.NAME_OF_REQUEST_PARAM_OF_EMAIL_OF_LOGGED_ON_USER)
            final String emailOfLoggedOnUser,
            @RequestParam(name = LogOnController.NAME_OF_REQUEST_PARAM_OF_PASSWORD_OF_LOGGED_ON_USER)
            final String passwordOfLoggedOnUser,
            final Model model)
            throws DefiningExistingEntityException, FindingEntityException, NotBindingUserDataException
    {
        String nameOfResultPage;
        if(!this.userService.isNotDeletedUserWithGivenEmailExist(emailOfLoggedOnUser))        //TODO: вынести этот код(AOP)
        {
            model.addAttribute(LogOnController.NAME_OF_MODEL_ATTRIBUTE_OF_LOGGING_ON_ERROR,
                    LogOnController.DESCRIPTION_OF_ERROR_OF_NOT_EXISTING_EMAIL);
            nameOfResultPage = "log on page with error 1";
        }
        else if(!this.userService.isCorrectPasswordForGivenEmail(passwordOfLoggedOnUser, emailOfLoggedOnUser))   //TODO: вынести этот код(AOP)
        {
            model.addAttribute(LogOnController.NAME_OF_MODEL_ATTRIBUTE_OF_LOGGING_ON_ERROR,
                    LogOnController.DESCRIPTION_OF_ERROR_OF_WRONG_PASSWORD);
            nameOfResultPage = "log on page with error 2";
        }
        else
        {
            User loggedOnUser;
            try
            {
                loggedOnUser = this.clientService.findNotDeletedUserByEmail(emailOfLoggedOnUser);
                nameOfResultPage = LogOnController.NAME_OF_CLIENT_MAIN_PAGE;
            }
            catch(final NoSuchEntityException noSuchClientException)
            {
                try
                {
                    loggedOnUser = this.administratorService.findNotDeletedUserByEmail(emailOfLoggedOnUser);
                    nameOfResultPage = LogOnController.NAME_OF_ADMINISTRATOR_MAIN_PAGE;
                }
                catch(final NoSuchEntityException noSuchAdministratorException)
                {
                    throw new NotBindingUserDataException("User with given email '" + emailOfLoggedOnUser
                            + "' is impossible to identify.");
                }
            }
            model.addAttribute(LogOnController.NAME_OF_MODEL_ATTRIBUTE_OF_LOGGED_ON_USER, loggedOnUser);
        }
        return nameOfResultPage;
    }

    public static final String PATH_OF_REQUEST_MAPPING_TO_LOG_ON = "/log_on";
    public static final String NAME_OF_REQUEST_PARAM_OF_EMAIL_OF_LOGGED_ON_USER = "email_of_logged_on_user";
    public static final String NAME_OF_REQUEST_PARAM_OF_PASSWORD_OF_LOGGED_ON_USER = "password_of_logged_on_user";
    public static final String NAME_OF_MODEL_ATTRIBUTE_OF_LOGGING_ON_ERROR = "logging_on_error";
    private static final String DESCRIPTION_OF_ERROR_OF_NOT_EXISTING_EMAIL = "user with given email doesn't exist";
    private static final String DESCRIPTION_OF_ERROR_OF_WRONG_PASSWORD = "wrong password";
    private static final String NAME_OF_CLIENT_MAIN_PAGE = "client/main_page";
    private static final String NAME_OF_ADMINISTRATOR_MAIN_PAGE = "administrator/main_page/main_page";
    public static final String NAME_OF_MODEL_ATTRIBUTE_OF_LOGGED_ON_USER = "logged_on_user";
}
