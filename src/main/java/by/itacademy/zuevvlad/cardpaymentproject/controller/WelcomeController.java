package by.itacademy.zuevvlad.cardpaymentproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = {WelcomeController.PATH_OF_REQUEST_MAPPING_OF_CONTROLLER})
public final class WelcomeController
{
    public static final String PATH_OF_REQUEST_MAPPING_OF_CONTROLLER = "/welcome";

    public WelcomeController()
    {
        super();
    }

    @GetMapping(path = {WelcomeController.PATH_OF_REQUEST_MAPPING_OF_WELCOME_PAGE})
    public final String returnNameOfWelcomePage()
    {
        return WelcomeController.NAME_OF_WELCOME_PAGE;
    }

    public static final String PATH_OF_REQUEST_MAPPING_OF_WELCOME_PAGE = "/welcome_page";
    private static final String NAME_OF_WELCOME_PAGE = "welcome_page";
}
