package by.itacademy.zuevvlad.cardpaymentproject.configuration;

import by.itacademy.zuevvlad.cardpaymentproject.dao.BankAccountDAO;
import by.itacademy.zuevvlad.cardpaymentproject.dao.PaymentCardDAO;
import by.itacademy.zuevvlad.cardpaymentproject.dao.PaymentDAO;
import by.itacademy.zuevvlad.cardpaymentproject.dao.user.AdministratorDAO;
import by.itacademy.zuevvlad.cardpaymentproject.dao.user.ClientDAO;
import by.itacademy.zuevvlad.cardpaymentproject.dao.user.UserDAO;
import by.itacademy.zuevvlad.cardpaymentproject.entity.*;
import by.itacademy.zuevvlad.cardpaymentproject.service.converter.StringCryptographerConverter;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class DAOConfiguration
{
    public DAOConfiguration()
    {
        super();
    }

    @Bean(value = "hibernateConfiguration")
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    protected org.hibernate.cfg.Configuration createHibernateConfiguration()
    {
        final org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
        configuration.configure(DAOConfiguration.PATH_OF_FILE_WITH_HIBERNATE_CONFIGURATION);
        configuration.addAnnotatedClass(BankAccount.class);      //TODO: replace on scanning packages
        configuration.addAttributeConverter(StringCryptographerConverter.class);
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Administrator.class);
        configuration.addAnnotatedClass(Client.class);
        configuration.addAnnotatedClass(PaymentCard.class);
        configuration.addAnnotatedClass(Payment.class);
        return configuration;
    }

    private static final String PATH_OF_FILE_WITH_HIBERNATE_CONFIGURATION = "hibernate.cfg.xml";

    @Bean(name = "sessionFactory")
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    protected SessionFactory createSessionFactory()
    {
        final org.hibernate.cfg.Configuration configuration = this.createHibernateConfiguration();
        return configuration.buildSessionFactory();
    }

    @Bean(name = DAOConfiguration.NAME_OF_BEAN_OF_BANK_ACCOUNT_DAO)
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    public BankAccountDAO createBankAccountDAO()
    {
        return new BankAccountDAO(this.createSessionFactory());
    }

    public static final String NAME_OF_BEAN_OF_BANK_ACCOUNT_DAO = "bankAccountDAO";

    @Bean(name = DAOConfiguration.NAME_OF_BEAN_OF_USER_DAO)
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    public UserDAO createUserDAO()
    {
        return new UserDAO(this.createSessionFactory());
    }

    public static final String NAME_OF_BEAN_OF_USER_DAO = "userDAO";

    @Bean(name = DAOConfiguration.NAME_OF_BEAN_OF_ADMINISTRATOR_DAO)
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    public AdministratorDAO createAdministratorDAO()
    {
        return new AdministratorDAO(this.createSessionFactory());
    }

    public static final String NAME_OF_BEAN_OF_ADMINISTRATOR_DAO = "administratorDAO";

    @Bean(name = DAOConfiguration.NAME_OF_BEAN_OF_CLIENT_DAO)
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    public ClientDAO createClientDAO()
    {
        return new ClientDAO(this.createSessionFactory());
    }

    public static final String NAME_OF_BEAN_OF_CLIENT_DAO = "clientDAO";

    @Bean(name = DAOConfiguration.NAME_OF_BEAN_OF_PAYMENT_CARD_DAO)
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    public PaymentCardDAO createPaymentCardDAO()
    {
        return new PaymentCardDAO(this.createSessionFactory());
    }

    public static final String NAME_OF_BEAN_OF_PAYMENT_CARD_DAO = "paymentCardDAO";

    @Bean(name = DAOConfiguration.NAME_OF_BEAN_OF_PAYMENT_DAO)
    @Scope(value = BeanDefinition.SCOPE_SINGLETON)
    public PaymentDAO createPaymentDAO()
    {
        return new PaymentDAO(this.createSessionFactory());
    }

    public static final String NAME_OF_BEAN_OF_PAYMENT_DAO = "paymentDAO";
}