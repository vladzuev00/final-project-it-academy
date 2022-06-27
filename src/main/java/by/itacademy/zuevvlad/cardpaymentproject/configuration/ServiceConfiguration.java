package by.itacademy.zuevvlad.cardpaymentproject.configuration;

import by.itacademy.zuevvlad.cardpaymentproject.dao.BankAccountDAO;
import by.itacademy.zuevvlad.cardpaymentproject.dao.PaymentCardDAO;
import by.itacademy.zuevvlad.cardpaymentproject.dao.PaymentDAO;
import by.itacademy.zuevvlad.cardpaymentproject.dao.user.AdministratorDAO;
import by.itacademy.zuevvlad.cardpaymentproject.dao.user.ClientDAO;
import by.itacademy.zuevvlad.cardpaymentproject.dao.user.UserDAO;
import by.itacademy.zuevvlad.cardpaymentproject.dto.*;
import by.itacademy.zuevvlad.cardpaymentproject.dto.factory.*;
import by.itacademy.zuevvlad.cardpaymentproject.dto.modifier.*;
import by.itacademy.zuevvlad.cardpaymentproject.entity.BankAccount;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Client;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Payment;
import by.itacademy.zuevvlad.cardpaymentproject.entity.PaymentCard;
import by.itacademy.zuevvlad.cardpaymentproject.service.BankAccountService;
import by.itacademy.zuevvlad.cardpaymentproject.service.PaymentCardService;
import by.itacademy.zuevvlad.cardpaymentproject.service.PaymentService;
import by.itacademy.zuevvlad.cardpaymentproject.service.calendarhandler.CalendarHandler;
import by.itacademy.zuevvlad.cardpaymentproject.service.paymentcardexpirationdateparser.PaymentCardExpirationDateParser;
import by.itacademy.zuevvlad.cardpaymentproject.service.user.AdministratorService;
import by.itacademy.zuevvlad.cardpaymentproject.service.user.ClientService;
import by.itacademy.zuevvlad.cardpaymentproject.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

@Configuration
@Import(value = {DAOConfiguration.class})
public class ServiceConfiguration
{
    @Bean(name = ServiceConfiguration.NAME_OF_BEAN_OF_USER_SERVICE)
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    @Autowired
    public UserService createUserService(
            @Qualifier(value = DAOConfiguration.NAME_OF_BEAN_OF_USER_DAO)
            final UserDAO userDAO)
    {
        return new UserService(userDAO);
    }

    public static final String NAME_OF_BEAN_OF_USER_SERVICE = "userService";

    @Bean(name = ServiceConfiguration.NAME_OF_BEAN_OF_ADMINISTRATOR_SERVICE)
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    @Autowired
    public AdministratorService createAdministratorService(
            @Qualifier(value = DAOConfiguration.NAME_OF_BEAN_OF_ADMINISTRATOR_DAO)
            final AdministratorDAO administratorDAO)
    {
        return new AdministratorService(administratorDAO);
    }

    public static final String NAME_OF_BEAN_OF_ADMINISTRATOR_SERVICE = "administratorService";

    @Bean(name = ServiceConfiguration.NAME_OF_BEAN_OF_CLIENT_SERVICE)
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    @Autowired
    public ClientService createClientService(
            @Qualifier(value = DAOConfiguration.NAME_OF_BEAN_OF_CLIENT_DAO)
            final ClientDAO clientDAO)
    {
        final DTOFactory<Client, ClientPersonalDataDTO> clientPersonalDataDTOFactory
                = this.createClientPersonalDataDTOFactory();
        final DTOFactory<Client, ClientDTO> clientDTOFactory = this.createClientDTOFactory();
        final EntityModifierByDTO<Client, ClientPersonalDataDTO> clientModifierByPersonalData
                = this.createClientModifierByPersonalData();
        final EntityModifierByDTO<Client, ClientDTO> clientModifierByDTO = this.createClientModifierByDTO();
        return new ClientService(clientDAO, clientPersonalDataDTOFactory, clientDTOFactory,
                clientModifierByPersonalData, clientModifierByDTO);
    }

    public static final String NAME_OF_BEAN_OF_CLIENT_SERVICE = "clientService";

    @Bean(value = ServiceConfiguration.NAME_OF_BEAN_OF_CLIENT_DTO_FACTORY)
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    public DTOFactory<Client, ClientDTO> createClientDTOFactory()
    {
        return new ClientDTOFactory();
    }

    private static final String NAME_OF_BEAN_OF_CLIENT_DTO_FACTORY = "clientDTOFactory";

    @Bean(name = ServiceConfiguration.NAME_OF_BEAN_OF_CLIENT_MODIFIER_BY_DTO)
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    public EntityModifierByDTO<Client, ClientDTO> createClientModifierByDTO()
    {
        return new ClientModifierByDTO();
    }

    private static final String NAME_OF_BEAN_OF_CLIENT_MODIFIER_BY_DTO = "clientModifierByDTO";

    @Bean(name = ServiceConfiguration.NAME_OF_BEAN_OF_BANK_ACCOUNT_SERVICE)
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    @Autowired
    public BankAccountService createBankAccountService(
            @Qualifier(value = DAOConfiguration.NAME_OF_BEAN_OF_BANK_ACCOUNT_DAO)
            final BankAccountDAO bankAccountDAO)
    {
        return new BankAccountService(bankAccountDAO, this.createBankAccountDTOFactory(),
                this.createBankAccountModifierByDTO());
    }

    public static final String NAME_OF_BEAN_OF_BANK_ACCOUNT_SERVICE = "bankAccountService";

    @Bean(name = ServiceConfiguration.NAME_OF_BEAN_OF_BANK_ACCOUNT_DTO_FACTORY)
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    public BankAccountDTOFactory createBankAccountDTOFactory()
    {
        return new BankAccountDTOFactory();
    }

    private static final String NAME_OF_BEAN_OF_BANK_ACCOUNT_DTO_FACTORY = "bankAccountDTOFactory";

    @Bean(name = ServiceConfiguration.NAME_OF_BEAN_OF_BANK_ACCOUNT_MODIFIER_BY_DTO)
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    public EntityModifierByDTO<BankAccount, BankAccountDTO> createBankAccountModifierByDTO()
    {
        return new BankAccountModifierByDTO();
    }

    private static final String NAME_OF_BEAN_OF_BANK_ACCOUNT_MODIFIER_BY_DTO = "bankAccountModifierByDTO";

    @Bean(name = ServiceConfiguration.NAME_OF_BEAN_OF_PAYMENT_CARD_SERVICE)
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    @Autowired
    public PaymentCardService createPaymentCardService(
            @Qualifier(value = DAOConfiguration.NAME_OF_BEAN_OF_PAYMENT_CARD_DAO)
            final PaymentCardDAO paymentCardDAO)
    {
        final PaymentCardExpirationDateParser paymentCardExpirationDateParser
                = this.createPaymentCardExpirationDateParser();
        final DTOFactory<PaymentCard, PaymentCardDTO> paymentCardDTOFactory = this.createPaymentCardDTOFactory();
        final EntityModifierByDTO<PaymentCard, PaymentCardDTO> paymentCardModifierByDTO =
                this.createPaymentCardModifierByDTO();
        return new PaymentCardService(paymentCardDAO, paymentCardExpirationDateParser, paymentCardDTOFactory,
                paymentCardModifierByDTO);
    }

    public static final String NAME_OF_BEAN_OF_PAYMENT_CARD_SERVICE = "paymentCardService";

    @Bean(name = ServiceConfiguration.NAME_OF_BEAN_OF_PAYMENT_CARD_EXPIRATION_DATE_PARSER)
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    public PaymentCardExpirationDateParser createPaymentCardExpirationDateParser()
    {
        return new PaymentCardExpirationDateParser();
    }

    public static final String NAME_OF_BEAN_OF_PAYMENT_CARD_EXPIRATION_DATE_PARSER = "paymentCardExpirationDateParser";

    @Bean(name = ServiceConfiguration.NAME_OF_BEAN_PAYMENT_CARD_DTO_FACTORY)
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    public DTOFactory<PaymentCard, PaymentCardDTO> createPaymentCardDTOFactory()
    {
        return new PaymentCardDTOFactory();
    }

    private static final String NAME_OF_BEAN_PAYMENT_CARD_DTO_FACTORY = "paymentCardDTOFactory";

    @Bean(name = ServiceConfiguration.NAME_OF_BEAN_OF_PAYMENT_CARD_MODIFIER_BY_DTO)
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    public EntityModifierByDTO<PaymentCard, PaymentCardDTO> createPaymentCardModifierByDTO()
    {
        return new PaymentCardModifierByDTO();
    }

    private static final String NAME_OF_BEAN_OF_PAYMENT_CARD_MODIFIER_BY_DTO = "paymentCardModifierByDTO";

    @Bean(name = ServiceConfiguration.NAME_OF_BEAN_OF_PAYMENT_SERVICE)
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    @Autowired
    public PaymentService createPaymentService(
            @Qualifier(value = DAOConfiguration.NAME_OF_BEAN_OF_PAYMENT_DAO)
            final PaymentDAO paymentDAO)
    {
        final DTOFactory<Payment, PaymentDTO> paymentDTOFactory = this.createPaymentDTOFactory();
        final EntityModifierByDTO<Payment, PaymentDTO> paymentModifierByDTO = this.createPaymentModifierByDTO();
        return new PaymentService(paymentDAO, paymentDTOFactory, paymentModifierByDTO);
    }

    public static final String NAME_OF_BEAN_OF_PAYMENT_SERVICE = "paymentService";

    @Bean(name = ServiceConfiguration.NAME_OF_BEAN_OF_PAYMENT_DTO_FACTORY)
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    public DTOFactory<Payment, PaymentDTO> createPaymentDTOFactory()
    {
        final CalendarHandler calendarHandler = CalendarHandler.createCalendarHandler();
        return new PaymentDTOFactory(calendarHandler);
    }

    private static final String NAME_OF_BEAN_OF_PAYMENT_DTO_FACTORY = "paymentDTOFactory";

    @Bean(name = ServiceConfiguration.NAME_OF_BEAN_OF_PAYMENT_MODIFIER_BY_DTO)
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    public EntityModifierByDTO<Payment, PaymentDTO> createPaymentModifierByDTO()
    {
        final CalendarHandler calendarHandler = CalendarHandler.createCalendarHandler();
        return new PaymentModifierByDTO(calendarHandler);
    }

    private static final String NAME_OF_BEAN_OF_PAYMENT_MODIFIER_BY_DTO = "paymentModifierByDTO";

    @Bean(name = ServiceConfiguration.NAME_OF_BEAN_OF_CLIENT_PERSONAL_DATA_DTO_FACTORY)
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    public DTOFactory<Client, ClientPersonalDataDTO> createClientPersonalDataDTOFactory()
    {
        return new ClientPersonalDataDTOFactory();
    }

    public static final String NAME_OF_BEAN_OF_CLIENT_PERSONAL_DATA_DTO_FACTORY = "clientPersonalDataDTOFactory";

    @Bean(name = ServiceConfiguration.NAME_OF_BEAN_OF_CLIENT_MODIFIER_BY_PERSONAL_DATA)
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    public EntityModifierByDTO<Client, ClientPersonalDataDTO> createClientModifierByPersonalData()
    {
        return new ClientModifierByPersonalData();
    }

    private static final String NAME_OF_BEAN_OF_CLIENT_MODIFIER_BY_PERSONAL_DATA = "clientModifierByPersonalData";
}