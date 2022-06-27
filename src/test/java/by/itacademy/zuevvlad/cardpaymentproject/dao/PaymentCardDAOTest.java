package by.itacademy.zuevvlad.cardpaymentproject.dao;

import by.itacademy.zuevvlad.cardpaymentproject.configuration.DAOConfiguration;
import by.itacademy.zuevvlad.cardpaymentproject.configuration.WebMainConfiguration;
import by.itacademy.zuevvlad.cardpaymentproject.contextfactory.ContextFactory;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.*;
import by.itacademy.zuevvlad.cardpaymentproject.dao.user.ClientDAO;
import by.itacademy.zuevvlad.cardpaymentproject.entity.BankAccount;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Client;
import by.itacademy.zuevvlad.cardpaymentproject.entity.PaymentCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Collection;
import java.util.Optional;

@Test
@ContextConfiguration(classes = {WebMainConfiguration.class})
public final class PaymentCardDAOTest extends AbstractTestNGSpringContextTests
{
    @Autowired
    @Qualifier(value = DAOConfiguration.NAME_OF_BEAN_OF_PAYMENT_CARD_DAO)
    private final PaymentCardDAO paymentCardDAO;

    @Autowired
    @Qualifier(value = DAOConfiguration.NAME_OF_BEAN_OF_BANK_ACCOUNT_DAO)
    private final BankAccountDAO bankAccountDAO;

    @Autowired
    @Qualifier(value = DAOConfiguration.NAME_OF_BEAN_OF_CLIENT_DAO)
    private final ClientDAO clientDAO;

    private Client insertedClient;
    private BankAccount insertedBankAccount;

    public PaymentCardDAOTest()
    {
        super();
        this.paymentCardDAO = null;
        this.bankAccountDAO = null;
        this.clientDAO = null;
        this.insertedClient = null;
        this.insertedBankAccount = null;
    }

    @BeforeClass
    public final void insertBankAccount()
            throws AddingEntityException
    {
        final BigDecimal money = BigDecimal.TEN;
        final boolean blocked = false;
        final String number = "11112222333344445555";
        final BankAccount bankAccount = new BankAccount(money, blocked, number);
        bankAccount.setDeleted(false);

        assert this.bankAccountDAO != null;
        this.bankAccountDAO.addEntity(bankAccount);
        this.insertedBankAccount = bankAccount;
    }

    @BeforeClass(dependsOnMethods = {"insertBankAccount"})
    public final void insertClient()
            throws AddingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final String name = "vlad";
        final String surname = "zuev";
        final String patronymic = "sergeevich";
        final String phoneNumber = "+375-44-111-11-11";
        final Client client = new Client(email, password, name, surname, patronymic, phoneNumber,
                this.insertedBankAccount);
        client.setDeleted(false);

        assert clientDAO != null;
        clientDAO.addEntity(client);
        this.insertedClient = client;
    }

    @AfterClass(dependsOnMethods = {"deleteInsertedClient"})
    public final void deleteInsertedBankAccount()
            throws DeletingEntityException
    {
        assert this.bankAccountDAO != null;
        this.bankAccountDAO.deleteEntity(this.insertedBankAccount);
    }

    @AfterClass
    public final void deleteInsertedClient()
            throws DeletingEntityException
    {
        assert clientDAO != null;
        clientDAO.deleteEntity(this.insertedClient);
    }

    @Test
    public final void paymentCardShouldBeAdded()
            throws AddingEntityException, FindingEntityException, DeletingEntityException
    {
        final String cardNumber = "1111-1111-1111-1111";

        final short month = 1;
        final int year = Year.now().getValue() + 1;
        final PaymentCard.ExpirationDate expirationDate = new PaymentCard.ExpirationDate(month, year);

        final String paymentSystem = "visa";
        final String cvc = "111";
        final String nameOfBank = "the best bank";
        final String password = "1111";
        final PaymentCard paymentCard = new PaymentCard(cardNumber, expirationDate, paymentSystem, cvc,
                this.insertedClient, nameOfBank, password);
        paymentCard.setDeleted(true);

        final long idOfAddedPaymentCardBeforeAdding = paymentCard.getId();
        assert this.paymentCardDAO != null;
        this.paymentCardDAO.addEntity(paymentCard);
        try
        {
            final long idOfAddedPaymentCardAfterAdding = paymentCard.getId();
            final Optional<PaymentCard> optionalOfAddedPaymentCardFromDataBase = this.paymentCardDAO.findEntityById(
                    idOfAddedPaymentCardAfterAdding);
            if(optionalOfAddedPaymentCardFromDataBase.isEmpty())
            {
                Assert.fail();
            }
            final PaymentCard addedPaymentCardFromDataBase = optionalOfAddedPaymentCardFromDataBase.get();

            final boolean testSuccess = paymentCard.equals(addedPaymentCardFromDataBase)
                    && idOfAddedPaymentCardBeforeAdding != idOfAddedPaymentCardAfterAdding;
            Assert.assertTrue(testSuccess);
        }
        finally
        {
            this.paymentCardDAO.deleteEntity(paymentCard);
        }
    }

    //Test for constraint 'card_number_should_be_correct'
    @Test(expectedExceptions = {AddingEntityException.class})
    public final void paymentCardWithNotValidCardNumberShouldNotBeAdded()
            throws AddingEntityException, DeletingEntityException
    {
        final String cardNumber = "1111-11111111-1111";

        final short month = 1;
        final int year = Year.now().getValue() + 1;
        final PaymentCard.ExpirationDate expirationDate = new PaymentCard.ExpirationDate(month, year);

        final String paymentSystem = "visa";
        final String cvc = "111";
        final String nameOfBank = "the best bank";
        final String password = "1111";
        final PaymentCard paymentCard = new PaymentCard(cardNumber, expirationDate, paymentSystem, cvc,
                this.insertedClient, nameOfBank, password);
        paymentCard.setDeleted(false);

        assert this.paymentCardDAO != null;
        this.paymentCardDAO.addEntity(paymentCard);
        try
        {
            Assert.fail();
        }
        finally
        {
            this.paymentCardDAO.deleteEntity(paymentCard);
        }
    }

    //Test for constraint 'name_of_bank_should_be_correct'
    @Test(expectedExceptions = {AddingEntityException.class})
    public final void paymentCardWithNotValidNameOfBankShouldNotBeAdded()
            throws AddingEntityException, DeletingEntityException
    {
        final String cardNumber = "1111-1111-1111-1111";

        final short month = 1;
        final int year = Year.now().getValue() + 1;
        final PaymentCard.ExpirationDate expirationDate = new PaymentCard.ExpirationDate(month, year);

        final String paymentSystem = "visa";
        final String cvc = "111";
        final String nameOfBank = "";
        final String password = "1111";
        final PaymentCard paymentCard = new PaymentCard(cardNumber, expirationDate, paymentSystem, cvc,
                this.insertedClient, nameOfBank, password);
        paymentCard.setDeleted(false);

        assert this.paymentCardDAO != null;
        this.paymentCardDAO.addEntity(paymentCard);
        try
        {
            Assert.fail();
        }
        finally
        {
            this.paymentCardDAO.deleteEntity(paymentCard);
        }
    }

    //Test for constraint 'number_of_month_of_expiration_date_should_be_correct'
    @Test(expectedExceptions = {AddingEntityException.class})
    public final void paymentCardWithNotValidMonthOfExpirationDateShouldNotBeAdded()
            throws AddingEntityException, DeletingEntityException
    {
        final String cardNumber = "1111-1111-1111-1111";

        final short month = 13;
        final int year = Year.now().getValue() + 1;
        final PaymentCard.ExpirationDate expirationDate = new PaymentCard.ExpirationDate(month, year);

        final String paymentSystem = "visa";
        final String cvc = "111";
        final String nameOfBank = "name of bank";
        final String password = "1111";
        final PaymentCard paymentCard = new PaymentCard(cardNumber, expirationDate, paymentSystem, cvc,
                this.insertedClient, nameOfBank, password);
        paymentCard.setDeleted(false);

        assert this.paymentCardDAO != null;
        this.paymentCardDAO.addEntity(paymentCard);
        try
        {
            Assert.fail();
        }
        finally
        {
            this.paymentCardDAO.deleteEntity(paymentCard);
        }
    }

    //Test for constraint 'year_of_expiration_date_should_be_correct'
    @Test(expectedExceptions = {AddingEntityException.class})
    public final void paymentCardWithNotValidYearOfExpirationDateShouldNotBeAdded()
            throws AddingEntityException, DeletingEntityException
    {
        final String cardNumber = "1111-1111-1111-1111";

        final short month = 1;
        final int year = 1899;         //1900 is minimum allowable value
        final PaymentCard.ExpirationDate expirationDate = new PaymentCard.ExpirationDate(month, year);

        final String paymentSystem = "visa";
        final String cvc = "111";
        final String nameOfBank = "name of bank";
        final String password = "1111";
        final PaymentCard paymentCard = new PaymentCard(cardNumber, expirationDate, paymentSystem, cvc,
                this.insertedClient, nameOfBank, password);
        paymentCard.setDeleted(false);

        assert this.paymentCardDAO != null;
        this.paymentCardDAO.addEntity(paymentCard);
        try
        {
            Assert.fail();
        }
        finally
        {
            this.paymentCardDAO.deleteEntity(paymentCard);
        }
    }

    //Test for constraint 'payment_system_should_be_correct'
    @Test(expectedExceptions = {AddingEntityException.class})
    public final void paymentCardWithNotValidPaymentSystemShouldNotBeAdded()
            throws AddingEntityException, DeletingEntityException
    {
        final String cardNumber = "1111-1111-1111-1111";

        final short month = 12;
        final int year = Year.now().getValue() + 1;
        final PaymentCard.ExpirationDate expirationDate = new PaymentCard.ExpirationDate(month, year);

        final String paymentSystem = "";
        final String cvc = "111";
        final String nameOfBank = "name of bank";
        final String password = "1111";
        final PaymentCard paymentCard = new PaymentCard(cardNumber, expirationDate, paymentSystem, cvc,
                this.insertedClient, nameOfBank, password);
        paymentCard.setDeleted(false);

        assert this.paymentCardDAO != null;
        this.paymentCardDAO.addEntity(paymentCard);
        try
        {
            Assert.fail();
        }
        finally
        {
            this.paymentCardDAO.deleteEntity(paymentCard);
        }
    }

    //Test for trigger 'check_unique_number_not_deleted_card_before_add'
    @Test(expectedExceptions = {AddingEntityException.class})
    public final void notDeletedPaymentCardShouldNotBeAddedBecauseOfExistingAnotherNotDeletedPaymentCardWithSameCardNumber()
            throws AddingEntityException, DeletingEntityException
    {
        final String cardNumber = "1111-1111-1111-1111";

        final short month = 1;
        final int year = Year.now().getValue() + 1;
        final PaymentCard.ExpirationDate expirationDate = new PaymentCard.ExpirationDate(month, year);

        final String paymentSystem = "visa";
        final String cvc = "111";
        final String nameOfBank = "the best bank";
        final String password = "1111";

        final PaymentCard firstPaymentCard = new PaymentCard(cardNumber, expirationDate, paymentSystem, cvc,
                this.insertedClient, nameOfBank, password);
        firstPaymentCard.setDeleted(false);

        try
        {
            assert this.paymentCardDAO != null;
            this.paymentCardDAO.addEntity(firstPaymentCard);
        }
        catch(final AddingEntityException cause)
        {
            Assert.fail();
        }
        try
        {
            final PaymentCard secondPaymentCard = new PaymentCard(cardNumber, expirationDate,
                    paymentSystem, cvc, this.insertedClient, nameOfBank, password);
            secondPaymentCard.setDeleted(false);

            this.paymentCardDAO.addEntity(secondPaymentCard);
            try
            {
                Assert.fail();
            }
            finally
            {
                this.paymentCardDAO.deleteEntity(secondPaymentCard);
            }
        }
        finally
        {
            this.paymentCardDAO.deleteEntity(firstPaymentCard);
        }
    }

    @Test(dependsOnMethods = {"paymentCardShouldBeAdded"})
    public final void allPaymentCardsShouldBeOffloaded()
            throws AddingEntityException, OffloadingEntitiesException, DeletingEntityException
    {
        final String cardNumber = "1111-1111-1111-1111";

        final short month = 1;
        final int year = Year.now().getValue() + 1;
        final PaymentCard.ExpirationDate expirationDate = new PaymentCard.ExpirationDate(month, year);

        final String paymentSystem = "visa";
        final String cvc = "111";
        final String nameOfBank = "the best bank";
        final String password = "1111";

        final PaymentCard notDeletedPaymentCard = new PaymentCard(cardNumber, expirationDate, paymentSystem, cvc,
                this.insertedClient, nameOfBank, password);
        notDeletedPaymentCard.setDeleted(false);

        final PaymentCard deletedPaymentCard = new PaymentCard(cardNumber, expirationDate, paymentSystem, cvc,
                this.insertedClient, nameOfBank, password);
        deletedPaymentCard.setDeleted(true);

        assert this.paymentCardDAO != null;
        this.paymentCardDAO.addEntity(notDeletedPaymentCard);
        try
        {
            this.paymentCardDAO.addEntity(deletedPaymentCard);
            try
            {
                final Collection<PaymentCard> offloadedPaymentCards = this.paymentCardDAO.offloadEntities();
                final boolean testSuccess = offloadedPaymentCards != null && !offloadedPaymentCards.isEmpty()
                        && offloadedPaymentCards.contains(notDeletedPaymentCard)
                        && offloadedPaymentCards.contains(deletedPaymentCard);
                Assert.assertTrue(testSuccess);
            }
            finally
            {
                this.paymentCardDAO.deleteEntity(deletedPaymentCard);
            }
        }
        finally
        {
            this.paymentCardDAO.deleteEntity(notDeletedPaymentCard);
        }
    }

    @Test(dependsOnMethods = {"paymentCardShouldBeAdded"})
    public final void paymentCardShouldBeFoundById()
            throws AddingEntityException, FindingEntityException, DeletingEntityException
    {
        final String cardNumber = "1111-1111-1111-1111";

        final short month = 1;
        final int year = Year.now().getValue() + 1;
        final PaymentCard.ExpirationDate expirationDate = new PaymentCard.ExpirationDate(month, year);

        final String paymentSystem = "visa";
        final String cvc = "111";
        final String nameOfBank = "the best bank";
        final String password = "1111";
        final PaymentCard paymentCard = new PaymentCard(cardNumber, expirationDate, paymentSystem, cvc,
                this.insertedClient, nameOfBank, password);
        paymentCard.setDeleted(true);

        assert this.paymentCardDAO != null;
        this.paymentCardDAO.addEntity(paymentCard);
        try
        {
            final Optional<PaymentCard> optionalOfPaymentCardFoundInDataBase = this.paymentCardDAO.findEntityById(
                    paymentCard.getId());
            if(optionalOfPaymentCardFoundInDataBase.isEmpty())
            {
                Assert.fail();
            }
            final PaymentCard paymentCardFoundInDataBase = optionalOfPaymentCardFoundInDataBase.get();
            Assert.assertEquals(paymentCard, paymentCardFoundInDataBase);
        }
        finally
        {
            this.paymentCardDAO.deleteEntity(paymentCard);
        }
    }

    @Test
    public final void paymentCardShouldNotBeFoundByNotExistingId()
            throws FindingEntityException
    {
        final long idOfFoundPaymentCard = -1;
        assert this.paymentCardDAO != null;
        final Optional<PaymentCard> optionalOfFoundPaymentCard = this.paymentCardDAO.findEntityById(
                idOfFoundPaymentCard);
        Assert.assertTrue(optionalOfFoundPaymentCard.isEmpty());
    }

    @Test(dependsOnMethods = {"paymentCardShouldBeAdded"})
    public final void notDeletedPaymentCardsShouldBeOffloaded()
            throws AddingEntityException, OffloadingEntitiesException, DeletingEntityException
    {
        final String cardNumberOfDeletedPaymentCard = "1111-1111-1111-1111";

        final short monthOfExpirationDate = 1;
        final int yearOfExpirationDate = Year.now().getValue() + 1;
        final PaymentCard.ExpirationDate expirationDate = new PaymentCard.ExpirationDate(
                monthOfExpirationDate, yearOfExpirationDate);

        final String paymentSystem = "visa";
        final String cvc = "111";
        final String nameOfBank = "the best bank";
        final String password = "1111";

        final PaymentCard deletedPaymentCard = new PaymentCard(cardNumberOfDeletedPaymentCard, expirationDate,
                paymentSystem, cvc, this.insertedClient, nameOfBank, password);
        deletedPaymentCard.setDeleted(true);

        final String cardNumberOfNotDeletedPaymentCard = "1111-1111-1111-1112";

        final PaymentCard notDeletedPaymentCard = new PaymentCard(cardNumberOfNotDeletedPaymentCard, expirationDate,
                paymentSystem, cvc, this.insertedClient, nameOfBank, password);
        notDeletedPaymentCard.setDeleted(false);

        assert this.paymentCardDAO != null;
        this.paymentCardDAO.addEntity(deletedPaymentCard);
        try
        {
            this.paymentCardDAO.addEntity(notDeletedPaymentCard);
            try
            {
                final Collection<PaymentCard> notDeletedPaymentCards = this.paymentCardDAO.findNotDeletedEntities();
                final boolean testSuccess = notDeletedPaymentCards != null && !notDeletedPaymentCards.isEmpty()
                        && notDeletedPaymentCards.contains(notDeletedPaymentCard)
                        && !notDeletedPaymentCards.contains(deletedPaymentCard);
                Assert.assertTrue(testSuccess);
            }
            finally
            {
                this.paymentCardDAO.deleteEntity(notDeletedPaymentCard);
            }
        }
        finally
        {
            this.paymentCardDAO.deleteEntity(deletedPaymentCard);
        }
    }

    @Test(dependsOnMethods = {"paymentCardShouldBeAdded"})
    public final void deletedPaymentCardsShouldBeOffloaded()
            throws AddingEntityException, OffloadingEntitiesException, DeletingEntityException
    {
        final String cardNumberOfDeletedPaymentCard = "1111-1111-1111-1111";

        final short monthOfExpirationDate = 1;
        final int yearOfExpirationDate = Year.now().getValue() + 1;
        final PaymentCard.ExpirationDate expirationDate = new PaymentCard.ExpirationDate(
                monthOfExpirationDate, yearOfExpirationDate);

        final String paymentSystem = "visa";
        final String cvc = "111";
        final String nameOfBank = "the best bank";
        final String password = "1111";

        final PaymentCard deletedPaymentCard = new PaymentCard(cardNumberOfDeletedPaymentCard, expirationDate,
                paymentSystem, cvc, this.insertedClient, nameOfBank, password);
        deletedPaymentCard.setDeleted(true);

        final String cardNumberOfNotDeletedPaymentCard = "1111-1111-1111-1112";

        final PaymentCard notDeletedPaymentCard = new PaymentCard(cardNumberOfNotDeletedPaymentCard, expirationDate,
                paymentSystem, cvc, this.insertedClient, nameOfBank, password);
        notDeletedPaymentCard.setDeleted(false);

        assert this.paymentCardDAO != null;
        this.paymentCardDAO.addEntity(deletedPaymentCard);
        try
        {
            this.paymentCardDAO.addEntity(notDeletedPaymentCard);
            try
            {
                final Collection<PaymentCard> deletedPaymentCards = this.paymentCardDAO.findDeletedEntities();
                final boolean testSuccess = deletedPaymentCards != null && !deletedPaymentCards.isEmpty()
                        && deletedPaymentCards.contains(deletedPaymentCard)
                        && !deletedPaymentCards.contains(notDeletedPaymentCard);
                Assert.assertTrue(testSuccess);
            }
            finally
            {
                this.paymentCardDAO.deleteEntity(notDeletedPaymentCard);
            }
        }
        finally
        {
            this.paymentCardDAO.deleteEntity(deletedPaymentCard);
        }
    }

    @Test(dependsOnMethods = {"paymentCardShouldBeAdded", "paymentCardShouldBeFoundById", "paymentCardShouldBeDeleted"})
    public final void paymentCardShouldBeUpdated()
            throws AddingEntityException, UpdatingEntityException, FindingEntityException, DeletingEntityException
    {
        final String cardNumber = "1111-1111-1111-1111";

        final short month = 1;
        final int year = Year.now().getValue() + 1;
        final PaymentCard.ExpirationDate expirationDate = new PaymentCard.ExpirationDate(month, year);

        final String paymentSystem = "visa";
        final String cvc = "111";
        final String nameOfBank = "the best bank";
        final String password = "1111";
        final PaymentCard paymentCard = new PaymentCard(cardNumber, expirationDate, paymentSystem, cvc,
                this.insertedClient, nameOfBank, password);
        paymentCard.setDeleted(true);

        assert this.paymentCardDAO != null;
        this.paymentCardDAO.addEntity(paymentCard);
        try
        {
            final String newCardNumber = "2222-2222-2222-2222";
            paymentCard.setCardNumber(newCardNumber);
            this.paymentCardDAO.updateEntity(paymentCard);

            final Optional<PaymentCard> optionalOfUpdatedPaymentCardFromDataBase = this.paymentCardDAO.findEntityById(
                    paymentCard.getId());
            if(optionalOfUpdatedPaymentCardFromDataBase.isEmpty())
            {
                Assert.fail();
            }

            final PaymentCard updatedPaymentCardFromDataBase = optionalOfUpdatedPaymentCardFromDataBase.get();
            Assert.assertEquals(paymentCard, updatedPaymentCardFromDataBase);
        }
        finally
        {
            this.paymentCardDAO.deleteEntity(paymentCard);
        }
    }

    //Test for constraint 'name_of_bank_should_be_correct'
    @Test(dependsOnMethods = {"paymentCardShouldBeAdded"}, expectedExceptions = {UpdatingEntityException.class})
    public final void paymentCardShouldNotBeUpdatedByNotValidNameOfBank()
            throws AddingEntityException, FindingEntityException, UpdatingEntityException, DeletingEntityException
    {
        final String cardNumber = "1111-1111-1111-1111";

        final short month = 1;
        final int year = Year.now().getValue() + 1;
        final PaymentCard.ExpirationDate expirationDate = new PaymentCard.ExpirationDate(month, year);

        final String paymentSystem = "visa";
        final String cvc = "111";
        final String nameOfBank = "the best bank";
        final String password = "1111";
        final PaymentCard paymentCard = new PaymentCard(cardNumber, expirationDate, paymentSystem, cvc,
                this.insertedClient, nameOfBank, password);
        paymentCard.setDeleted(true);

        assert this.paymentCardDAO != null;
        this.paymentCardDAO.addEntity(paymentCard);
        try
        {
            final String newNameOfBank = "";
            paymentCard.setNameOfBank(newNameOfBank);
            this.paymentCardDAO.updateEntity(paymentCard);
        }
        finally
        {
            this.paymentCardDAO.deleteEntity(paymentCard);
        }
    }

    //Test for constraint 'number_of_month_of_expiration_date_should_be_correct'
    @Test(dependsOnMethods = {"paymentCardShouldBeAdded"}, expectedExceptions = {UpdatingEntityException.class})
    public final void paymentCardShouldNotBeUpdatedByExpirationDateWithNotValidMonth()
            throws AddingEntityException, UpdatingEntityException, FindingEntityException, DeletingEntityException
    {
        final String cardNumber = "1111-1111-1111-1111";

        final short month = 1;
        final int year = Year.now().getValue() + 1;
        final PaymentCard.ExpirationDate expirationDate = new PaymentCard.ExpirationDate(month, year);

        final String paymentSystem = "visa";
        final String cvc = "111";
        final String nameOfBank = "the best bank";
        final String password = "1111";
        final PaymentCard paymentCard = new PaymentCard(cardNumber, expirationDate, paymentSystem, cvc,
                this.insertedClient, nameOfBank, password);
        paymentCard.setDeleted(true);

        assert this.paymentCardDAO != null;
        this.paymentCardDAO.addEntity(paymentCard);
        try
        {
            final short newNumberOfMonth = 13;
            expirationDate.setMonth(newNumberOfMonth);
            this.paymentCardDAO.updateEntity(paymentCard);
        }
        finally
        {
            this.paymentCardDAO.deleteEntity(paymentCard);
        }
    }

    //Test for constraint 'year_of_expiration_date_should_be_correct'
    @Test(dependsOnMethods = {"paymentCardShouldBeAdded"}, expectedExceptions = {UpdatingEntityException.class})
    public final void paymentCardShouldNotBeUpdatedByExpirationDateWithNotValidYear()
            throws AddingEntityException, UpdatingEntityException, FindingEntityException, DeletingEntityException
    {
        final String cardNumber = "1111-1111-1111-1111";

        final short month = 1;
        final int year = Year.now().getValue() + 1;
        final PaymentCard.ExpirationDate expirationDate = new PaymentCard.ExpirationDate(month, year);

        final String paymentSystem = "visa";
        final String cvc = "111";
        final String nameOfBank = "the best bank";
        final String password = "1111";
        final PaymentCard paymentCard = new PaymentCard(cardNumber, expirationDate, paymentSystem, cvc,
                this.insertedClient, nameOfBank, password);
        paymentCard.setDeleted(true);

        assert this.paymentCardDAO != null;
        this.paymentCardDAO.addEntity(paymentCard);
        try
        {
            final int newYear = 1899;    //minimal allowable is 1900
            expirationDate.setYear(newYear);
            this.paymentCardDAO.updateEntity(paymentCard);
        }
        finally
        {
            this.paymentCardDAO.deleteEntity(paymentCard);
        }
    }

    //Test for constraint 'payment_system_should_be_correct'
    @Test(dependsOnMethods = {"paymentCardShouldBeAdded"}, expectedExceptions = {UpdatingEntityException.class})
    public final void paymentCardShouldNotBeUpdatedByNotValidPaymentSystem()
            throws AddingEntityException, UpdatingEntityException, DeletingEntityException
    {
        final String cardNumber = "1111-1111-1111-1111";

        final short month = 1;
        final int year = Year.now().getValue() + 1;
        final PaymentCard.ExpirationDate expirationDate = new PaymentCard.ExpirationDate(month, year);

        final String paymentSystem = "visa";
        final String cvc = "111";
        final String nameOfBank = "the best bank";
        final String password = "1111";
        final PaymentCard paymentCard = new PaymentCard(cardNumber, expirationDate, paymentSystem, cvc,
                this.insertedClient, nameOfBank, password);
        paymentCard.setDeleted(true);

        assert this.paymentCardDAO != null;
        this.paymentCardDAO.addEntity(paymentCard);
        try
        {
            final String newPaymentSystem = "";
            paymentCard.setPaymentSystem(newPaymentSystem);
            this.paymentCardDAO.updateEntity(paymentCard);
        }
        finally
        {
            this.paymentCardDAO.deleteEntity(paymentCard);
        }
    }

    //Test for constraint 'card_number_should_be_correct'
    @Test(dependsOnMethods = {"paymentCardShouldBeAdded"}, expectedExceptions = {UpdatingEntityException.class})
    public final void paymentCardShouldNotBeUpdatedByNotValidCardNumber()
            throws AddingEntityException, UpdatingEntityException, DeletingEntityException
    {
        final String cardNumber = "1111-1111-1111-1111";

        final short month = 1;
        final int year = Year.now().getValue() + 1;
        final PaymentCard.ExpirationDate expirationDate = new PaymentCard.ExpirationDate(month, year);

        final String paymentSystem = "visa";
        final String cvc = "111";
        final String nameOfBank = "the best bank";
        final String password = "1111";
        final PaymentCard paymentCard = new PaymentCard(cardNumber, expirationDate, paymentSystem, cvc,
                this.insertedClient, nameOfBank, password);
        paymentCard.setDeleted(true);

        assert this.paymentCardDAO != null;
        this.paymentCardDAO.addEntity(paymentCard);
        try
        {
            final String newCardNumber = "1111-1111-11111-1111";
            paymentCard.setCardNumber(newCardNumber);
            this.paymentCardDAO.updateEntity(paymentCard);
        }
        finally
        {
            this.paymentCardDAO.deleteEntity(paymentCard);
        }
    }

    //Test for trigger 'check_unique_number_not_deleted_card_before_update'
    @Test(dependsOnMethods = {"paymentCardShouldBeAdded"}, expectedExceptions = {UpdatingEntityException.class})
    public final void notDeletedPaymentCardShouldNotBeUpdatedByNumberOfAnotherNotDeletedPaymentCard()
            throws AddingEntityException, UpdatingEntityException, DeletingEntityException
    {
        final short month = 1;
        final int year = Year.now().getValue() + 1;
        final PaymentCard.ExpirationDate expirationDate = new PaymentCard.ExpirationDate(month, year);

        final String paymentSystem = "visa";
        final String cvc = "111";
        final String nameOfBank = "the best bank";
        final String password = "1111";

        final String cardNumberOfFirstPaymentCard = "1111-1111-1111-1111";
        final PaymentCard firstPaymentCard = new PaymentCard(cardNumberOfFirstPaymentCard, expirationDate,
                paymentSystem, cvc, this.insertedClient, nameOfBank, password);
        firstPaymentCard.setDeleted(false);

        final String cardNumberOfSecondPaymentCard = "2222-2222-2222-2222";
        final PaymentCard secondPaymentCard = new PaymentCard(cardNumberOfSecondPaymentCard, expirationDate,
                paymentSystem, cvc, this.insertedClient, nameOfBank, password);
        secondPaymentCard.setDeleted(false);

        assert this.paymentCardDAO != null;
        this.paymentCardDAO.addEntity(firstPaymentCard);
        try
        {
            this.paymentCardDAO.addEntity(secondPaymentCard);
            try
            {
                secondPaymentCard.setCardNumber(firstPaymentCard.getCardNumber());
                this.paymentCardDAO.updateEntity(secondPaymentCard);
            }
            finally
            {
                this.paymentCardDAO.deleteEntity(secondPaymentCard);
            }
        }
        finally
        {
            this.paymentCardDAO.deleteEntity(firstPaymentCard);
        }
    }

    @Test(dependsOnMethods = {"paymentCardShouldBeAdded", "paymentCardShouldBeFoundById", "paymentCardShouldBeDeleted"})
    public final void deletedStatusOfPaymentCardWithGivenIdShouldBeUpdated()
            throws AddingEntityException, UpdatingEntityException, FindingEntityException, DeletingEntityException
    {
        final String cardNumber = "1111-1111-1111-1111";

        final short month = 1;
        final int year = Year.now().getValue() + 1;
        final PaymentCard.ExpirationDate expirationDate = new PaymentCard.ExpirationDate(month, year);

        final String paymentSystem = "visa";
        final String cvc = "111";
        final String nameOfBank = "the best bank";
        final String password = "1111";
        final PaymentCard paymentCard = new PaymentCard(cardNumber, expirationDate, paymentSystem, cvc,
                this.insertedClient, nameOfBank, password);
        paymentCard.setDeleted(true);

        assert this.paymentCardDAO != null;
        this.paymentCardDAO.addEntity(paymentCard);
        try
        {
            final boolean newDeleted = !paymentCard.isDeleted();
            this.paymentCardDAO.updateDeletedStatusOfEntity(paymentCard.getId(), newDeleted);

            final Optional<PaymentCard> optionalOfUpdatedPaymentCard = this.paymentCardDAO.findEntityById(
                    paymentCard.getId());
            if(optionalOfUpdatedPaymentCard.isEmpty())
            {
                Assert.fail();
            }
            final PaymentCard updatedPaymentCard = optionalOfUpdatedPaymentCard.get();

            Assert.assertEquals(updatedPaymentCard.isDeleted(), newDeleted);
        }
        finally
        {
            this.paymentCardDAO.deleteEntity(paymentCard);
        }
    }

    @Test(dependsOnMethods = {"paymentCardShouldBeAdded", "paymentCardShouldBeFoundById", "paymentCardShouldBeDeleted"})
    public final void deletedStatusOfPaymentCardShouldBeUpdated()
            throws AddingEntityException, UpdatingEntityException, FindingEntityException, DeletingEntityException
    {
        final String cardNumber = "1111-1111-1111-1111";

        final short month = 1;
        final int year = Year.now().getValue() + 1;
        final PaymentCard.ExpirationDate expirationDate = new PaymentCard.ExpirationDate(month, year);

        final String paymentSystem = "visa";
        final String cvc = "111";
        final String nameOfBank = "the best bank";
        final String password = "1111";
        final PaymentCard paymentCard = new PaymentCard(cardNumber, expirationDate, paymentSystem, cvc,
                this.insertedClient, nameOfBank, password);

        assert this.paymentCardDAO != null;
        this.paymentCardDAO.addEntity(paymentCard);
        try
        {
            final boolean newDeleted = !paymentCard.isDeleted();
            this.paymentCardDAO.updateDeletedStatusOfEntity(paymentCard, newDeleted);

            final Optional<PaymentCard> optionalOfUpdatedPaymentCard = this.paymentCardDAO.findEntityById(
                    paymentCard.getId());
            if(optionalOfUpdatedPaymentCard.isEmpty())
            {
                Assert.fail();
            }
            final PaymentCard updatedPaymentCard = optionalOfUpdatedPaymentCard.get();

            Assert.assertEquals(updatedPaymentCard.isDeleted(), newDeleted);
        }
        finally
        {
            this.paymentCardDAO.deleteEntity(paymentCard);
        }
    }

    @Test(dependsOnMethods = {"paymentCardShouldBeAdded"}, expectedExceptions = {UpdatingEntityException.class})
    public final void deletedStatusOfDeletedCardShouldBeUpdatedBecauseOfExistingAnotherNotDeletedCardWithSameNumber()
            throws AddingEntityException, UpdatingEntityException, DeletingEntityException
    {
        final String cardNumber = "1111-1111-1111-1111";

        final short month = 1;
        final int year = Year.now().getValue() + 1;
        final PaymentCard.ExpirationDate expirationDate = new PaymentCard.ExpirationDate(month, year);

        final String paymentSystem = "visa";
        final String cvc = "111";
        final String nameOfBank = "the best bank";
        final String password = "1111";

        final PaymentCard notDeletedPaymentCard = new PaymentCard(cardNumber, expirationDate, paymentSystem, cvc,
                this.insertedClient, nameOfBank, password);
        notDeletedPaymentCard.setDeleted(false);

        final PaymentCard deletedPaymentCard = new PaymentCard(cardNumber, expirationDate, paymentSystem, cvc,
                this.insertedClient, nameOfBank, password);
        deletedPaymentCard.setDeleted(true);

        assert this.paymentCardDAO != null;
        this.paymentCardDAO.addEntity(notDeletedPaymentCard);
        try
        {
            this.paymentCardDAO.addEntity(deletedPaymentCard);
            try
            {
                final boolean newDeletedOfDeletedPaymentCard = !deletedPaymentCard.isDeleted();
                this.paymentCardDAO.updateDeletedStatusOfEntity(deletedPaymentCard, newDeletedOfDeletedPaymentCard);
            }
            finally
            {
                this.paymentCardDAO.deleteEntity(deletedPaymentCard);
            }
        }
        finally
        {
            this.paymentCardDAO.deleteEntity(notDeletedPaymentCard);
        }
    }

    @Test(dependsOnMethods = {"paymentCardShouldBeAdded", "paymentCardShouldBeFoundById"})
    public final void paymentCardShouldBeDeletedById()
            throws AddingEntityException, DeletingEntityException, FindingEntityException
    {
        final String cardNumber = "1111-1111-1111-1111";

        final short month = 1;
        final int year = Year.now().getValue() + 1;
        final PaymentCard.ExpirationDate expirationDate = new PaymentCard.ExpirationDate(month, year);

        final String paymentSystem = "visa";
        final String cvc = "111";
        final String nameOfBank = "the best bank";
        final String password = "1111";
        final PaymentCard paymentCard = new PaymentCard(cardNumber, expirationDate, paymentSystem, cvc,
                this.insertedClient, nameOfBank, password);
        paymentCard.setDeleted(true);

        assert this.paymentCardDAO != null;
        this.paymentCardDAO.addEntity(paymentCard);
        this.paymentCardDAO.deleteEntity(paymentCard.getId());
        final Optional<PaymentCard> optionalOfFoundPaymentCard = this.paymentCardDAO.findEntityById(paymentCard.getId());
        Assert.assertTrue(optionalOfFoundPaymentCard.isEmpty());
    }

    @Test
    public final void paymentCardShouldNotBeDeletedByNotExistingId()
            throws OffloadingEntitiesException, DeletingEntityException
    {
        final long idOfDeletedPaymentCard = -1;
        assert this.paymentCardDAO != null;
        final Collection<PaymentCard> paymentCardsBeforeDeleting = this.paymentCardDAO.offloadEntities();
        this.paymentCardDAO.deleteEntity(idOfDeletedPaymentCard);
        final Collection<PaymentCard> paymentCardsAfterDeleting = this.paymentCardDAO.offloadEntities();
        Assert.assertEquals(paymentCardsBeforeDeleting, paymentCardsAfterDeleting);
    }

    @Test(dependsOnMethods = {"paymentCardShouldBeAdded", "paymentCardShouldBeFoundById"})
    public final void paymentCardShouldBeDeleted()
            throws AddingEntityException, DeletingEntityException, FindingEntityException
    {
        final String cardNumber = "1111-1111-1111-1111";

        final short month = 1;
        final int year = Year.now().getValue() + 1;
        final PaymentCard.ExpirationDate expirationDate = new PaymentCard.ExpirationDate(month, year);

        final String paymentSystem = "visa";
        final String cvc = "111";
        final String nameOfBank = "the best bank";
        final String password = "1111";
        final PaymentCard paymentCard = new PaymentCard(cardNumber, expirationDate, paymentSystem, cvc,
                this.insertedClient, nameOfBank, password);
        paymentCard.setDeleted(true);

        assert this.paymentCardDAO != null;
        this.paymentCardDAO.addEntity(paymentCard);
        this.paymentCardDAO.deleteEntity(paymentCard);
        final Optional<PaymentCard> optionalOfFoundPaymentCard = this.paymentCardDAO.findEntityById(paymentCard.getId());
        Assert.assertTrue(optionalOfFoundPaymentCard.isEmpty());
    }

    @Test
    public final void paymentCardWithNotExistingIdShouldNotBeDeleted()
            throws OffloadingEntitiesException, DeletingEntityException
    {
        final long id = -1;
        final String cardNumber = "1111-1111-1111-1111";

        final short month = 1;
        final int year = Year.now().getValue() + 1;
        final PaymentCard.ExpirationDate expirationDate = new PaymentCard.ExpirationDate(month, year);

        final String paymentSystem = "visa";
        final String cvc = "111";
        final String nameOfBank = "the best bank";
        final String password = "1111";
        final PaymentCard paymentCard = new PaymentCard(id, cardNumber, expirationDate, paymentSystem, cvc,
                this.insertedClient, nameOfBank, password);
        paymentCard.setDeleted(false);

        assert this.paymentCardDAO != null;
        final Collection<PaymentCard> paymentCardsBeforeDeleting = this.paymentCardDAO.offloadEntities();
        this.paymentCardDAO.deleteEntity(paymentCard);
        final Collection<PaymentCard> paymentCardsAfterDeleting = this.paymentCardDAO.offloadEntities();

        Assert.assertEquals(paymentCardsBeforeDeleting, paymentCardsAfterDeleting);
    }

    @Test(dependsOnMethods = {"paymentCardShouldBeAdded"})
    public final void paymentCardWithGivenIdShouldBeExisted()
            throws AddingEntityException, DefiningExistingEntityException, DeletingEntityException
    {
        final String cardNumber = "1111-1111-1111-1111";

        final short month = 1;
        final int year = Year.now().getValue() + 1;
        final PaymentCard.ExpirationDate expirationDate = new PaymentCard.ExpirationDate(month, year);

        final String paymentSystem = "visa";
        final String cvc = "111";
        final String nameOfBank = "the best bank";
        final String password = "1111";
        final PaymentCard paymentCard = new PaymentCard(cardNumber, expirationDate, paymentSystem, cvc,
                this.insertedClient, nameOfBank, password);
        paymentCard.setDeleted(true);

        assert this.paymentCardDAO != null;
        this.paymentCardDAO.addEntity(paymentCard);
        try
        {
            final boolean paymentCardExists = this.paymentCardDAO.isEntityWithGivenIdExisting(paymentCard.getId());
            Assert.assertTrue(paymentCardExists);
        }
        finally
        {
            this.paymentCardDAO.deleteEntity(paymentCard);
        }
    }

    @Test
    public final void paymentCardWithGivenNotExistingIdShouldNotBeExisted()
            throws DefiningExistingEntityException
    {
        final long idOfResearchPaymentCard = -1;
        assert this.paymentCardDAO != null;
        final boolean paymentCardExists = this.paymentCardDAO.isEntityWithGivenIdExisting(idOfResearchPaymentCard);
        Assert.assertFalse(paymentCardExists);
    }

    @Test(dependsOnMethods = {"paymentCardShouldBeAdded"})
    public final void notDeletedPaymentCardWithGivenCardNumberShouldExist()
            throws AddingEntityException, DefiningExistingEntityException, DeletingEntityException
    {
        final String cardNumber = "1111-1111-1111-1111";

        final short month = 1;
        final int year = Year.now().getValue() + 1;
        final PaymentCard.ExpirationDate expirationDate = new PaymentCard.ExpirationDate(month, year);

        final String paymentSystem = "visa";
        final String cvc = "111";
        final String nameOfBank = "the best bank";
        final String password = "1111";
        final PaymentCard paymentCard = new PaymentCard(cardNumber, expirationDate, paymentSystem, cvc,
                this.insertedClient, nameOfBank, password);
        paymentCard.setDeleted(false);

        assert this.paymentCardDAO != null;
        this.paymentCardDAO.addEntity(paymentCard);
        try
        {
            final boolean paymentCardExists = this.paymentCardDAO.isNotDeletedPaymentCardWithGivenCardNumberExist(
                    paymentCard.getCardNumber());
            Assert.assertTrue(paymentCardExists);
        }
        finally
        {
            this.paymentCardDAO.deleteEntity(paymentCard);
        }
    }

    @Test
    public final void notDeletedPaymentCardWithGivenNotValidCardNumberShouldNotExist()
            throws DefiningExistingEntityException
    {
        final String cardNumber = "1111-1111-1111-111";
        assert this.paymentCardDAO != null;
        final boolean paymentCardExists = this.paymentCardDAO.isNotDeletedPaymentCardWithGivenCardNumberExist(
                cardNumber);
        Assert.assertFalse(paymentCardExists);
    }

    @Test(dependsOnMethods = {"paymentCardShouldBeAdded"})
    public final void notDeletedPaymentCardShouldBeFoundByCardNumber()
            throws AddingEntityException, FindingEntityException, DeletingEntityException
    {
        final String cardNumber = "1111-1111-1111-1111";

        final short month = 1;
        final int year = Year.now().getValue() + 1;
        final PaymentCard.ExpirationDate expirationDate = new PaymentCard.ExpirationDate(month, year);

        final String paymentSystem = "visa";
        final String cvc = "111";
        final String nameOfBank = "the best bank";
        final String password = "1111";
        final PaymentCard paymentCard = new PaymentCard(cardNumber, expirationDate, paymentSystem, cvc,
                this.insertedClient, nameOfBank, password);
        paymentCard.setDeleted(false);

        assert this.paymentCardDAO != null;
        this.paymentCardDAO.addEntity(paymentCard);
        try
        {
            final Optional<PaymentCard> optionalOfFoundPaymentCard = this.paymentCardDAO
                    .findOptionalOfNotDeletedPaymentCardByCardNumber(paymentCard.getCardNumber());
            if(optionalOfFoundPaymentCard.isEmpty())
            {
                Assert.fail();
            }
            final PaymentCard foundPaymentCard = optionalOfFoundPaymentCard.get();
            Assert.assertEquals(paymentCard, foundPaymentCard);
        }
        finally
        {
            this.paymentCardDAO.deleteEntity(paymentCard);
        }
    }

    @Test
    public final void notDeletedPaymentCardShouldNotBeFoundByNotValidCardNumber()
            throws FindingEntityException
    {
        final String cardNumber = "1111-1111-1111-111";
        assert this.paymentCardDAO != null;
        final Optional<PaymentCard> optionalOfFoundPaymentCard = this.paymentCardDAO
                .findOptionalOfNotDeletedPaymentCardByCardNumber(cardNumber);
        Assert.assertTrue(optionalOfFoundPaymentCard.isEmpty());
    }

    @Test(dependsOnMethods = {"paymentCardShouldBeAdded"})
    public final void paymentCardsShouldBeFoundByCardNumber()
            throws AddingEntityException, OffloadingEntitiesException, DeletingEntityException
    {
        final String cardNumber = "1111-1111-1111-1111";

        final short month = 1;
        final int year = Year.now().getValue() + 1;
        final PaymentCard.ExpirationDate expirationDate = new PaymentCard.ExpirationDate(month, year);

        final String paymentSystem = "visa";
        final String cvc = "111";
        final String nameOfBank = "the best bank";
        final String password = "1111";

        final PaymentCard notDeletedPaymentCard = new PaymentCard(cardNumber, expirationDate, paymentSystem, cvc,
                this.insertedClient, nameOfBank, password);
        notDeletedPaymentCard.setDeleted(false);

        final PaymentCard deletedPaymentCard = new PaymentCard(cardNumber, expirationDate, paymentSystem, cvc,
                this.insertedClient, nameOfBank, password);
        deletedPaymentCard.setDeleted(true);

        assert this.paymentCardDAO != null;
        this.paymentCardDAO.addEntity(notDeletedPaymentCard);
        try
        {
            this.paymentCardDAO.addEntity(deletedPaymentCard);
            try
            {
                final Collection<PaymentCard> foundPaymentCards = this.paymentCardDAO.findPaymentCardsByCardNumber(
                        cardNumber);
                final boolean testSuccess = foundPaymentCards != null && !foundPaymentCards.isEmpty()
                        && foundPaymentCards.contains(notDeletedPaymentCard)
                        && foundPaymentCards.contains(deletedPaymentCard);
                Assert.assertTrue(testSuccess);
            }
            finally
            {
                this.paymentCardDAO.deleteEntity(deletedPaymentCard);
            }
        }
        finally
        {
            this.paymentCardDAO.deleteEntity(notDeletedPaymentCard);
        }
    }

    @Test
    public final void paymentCardsShouldNotBeFoundByNotValidCardNumber()
            throws OffloadingEntitiesException
    {
        final String cardNumber = "1111-1111-1111-111";
        assert this.paymentCardDAO != null;
        final Collection<PaymentCard> foundPaymentCards = this.paymentCardDAO.findPaymentCardsByCardNumber(cardNumber);
        Assert.assertTrue(foundPaymentCards.isEmpty());
    }
}
