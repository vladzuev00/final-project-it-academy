package by.itacademy.zuevvlad.cardpaymentproject.dao;

import by.itacademy.zuevvlad.cardpaymentproject.configuration.DAOConfiguration;
import by.itacademy.zuevvlad.cardpaymentproject.configuration.WebMainConfiguration;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.*;
import by.itacademy.zuevvlad.cardpaymentproject.dao.user.ClientDAO;
import by.itacademy.zuevvlad.cardpaymentproject.dao.user.UserDAO;
import by.itacademy.zuevvlad.cardpaymentproject.entity.BankAccount;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Client;
import by.itacademy.zuevvlad.cardpaymentproject.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;

@Test
@ContextConfiguration(classes = {WebMainConfiguration.class})
public final class BankAccountDAOTest extends AbstractTestNGSpringContextTests
{
    @Autowired
    @Qualifier(value = DAOConfiguration.NAME_OF_BEAN_OF_BANK_ACCOUNT_DAO)
    private final BankAccountDAO bankAccountDAO;

    @Autowired
    @Qualifier(value = DAOConfiguration.NAME_OF_BEAN_OF_CLIENT_DAO)
    private final ClientDAO clientDAO;

    @Autowired
    @Qualifier(value = DAOConfiguration.NAME_OF_BEAN_OF_USER_DAO)
    private final UserDAO userDAO;

    public BankAccountDAOTest()
    {
        super();
        this.bankAccountDAO = null;
        this.clientDAO = null;
        this.userDAO = null;
    }

    @Test
    public final void bankAccountShouldBeAdded()
            throws AddingEntityException, FindingEntityException, DeletingEntityException
    {
        final BigDecimal money = BigDecimal.ZERO;
        final boolean blocked = false;
        final String number = "11112222333344445555";
        final BankAccount bankAccount = new BankAccount(money, blocked, number);
        bankAccount.setDeleted(true);

        final long idOfBankAccountBeforeAdding = bankAccount.getId();
        assert this.bankAccountDAO != null;
        this.bankAccountDAO.addEntity(bankAccount);
        try
        {
            final long idOfBankAccountAfterAdding = bankAccount.getId();

            final Optional<BankAccount> optionalOfAddedBankAccountFromDataBase = this.bankAccountDAO.findEntityById(
                    idOfBankAccountAfterAdding);
            if(optionalOfAddedBankAccountFromDataBase.isEmpty())
            {
                Assert.fail();
            }
            final BankAccount addedBankAccountFromDataBase = optionalOfAddedBankAccountFromDataBase.get();

            final boolean testSuccess = bankAccount.equals(addedBankAccountFromDataBase)
                    && idOfBankAccountBeforeAdding != idOfBankAccountAfterAdding;
            Assert.assertTrue(testSuccess);
        }
        finally
        {
            this.bankAccountDAO.deleteEntity(bankAccount);
        }
    }

    //Test for constraint 'money_of_bank_account_should_be_correct'
    @Test(expectedExceptions = {AddingEntityException.class})
    public final void bankAccountWithNotPositiveMoneyShouldNotBeAdded()
            throws AddingEntityException, DeletingEntityException
    {
        final BigDecimal money = new BigDecimal("-1");
        final boolean blocked = false;
        final String number = "11112222333344445555";
        final BankAccount bankAccount = new BankAccount(money, blocked, number);
        bankAccount.setDeleted(false);

        assert this.bankAccountDAO != null;
        this.bankAccountDAO.addEntity(bankAccount);
        try
        {
            Assert.fail();
        }
        finally
        {
            this.bankAccountDAO.deleteEntity(bankAccount);
        }
    }

    //Test for constraint 'number_of_bank_account_should_be_correct'
    @Test(expectedExceptions = {AddingEntityException.class})
    public final void bankAccountWithNotValidNumberShouldNotBeAdded()
            throws AddingEntityException, DeletingEntityException
    {
        final BigDecimal money = BigDecimal.TEN;
        final boolean blocked = false;
        final String number = "1111222233334444555"; //should contain 20 digits - not 19
        final BankAccount bankAccount = new BankAccount(money, blocked, number);
        bankAccount.setDeleted(false);

        assert this.bankAccountDAO != null;
        this.bankAccountDAO.addEntity(bankAccount);
        try
        {
            Assert.fail();
        }
        finally
        {
            this.bankAccountDAO.deleteEntity(bankAccount);
        }
    }

    //Test for trigger 'check_unique_number_not_deleted_bank_accounts_before_adding'
    @Test
    public final void twoBankAccountWithSameNumberButDifferentDeletedStatusShouldBeAdded()
            throws AddingEntityException, OffloadingEntitiesException, DeletingEntityException
    {
        final BigDecimal moneyOfNotDeletedBankAccount = BigDecimal.ZERO;
        final boolean blockedOfNotDeletedBankAccount = false;
        final String numberOfNotDeletedBankAccount = "11112222333344445555";
        final BankAccount notDeletedBankAccount = new BankAccount(moneyOfNotDeletedBankAccount,
                blockedOfNotDeletedBankAccount, numberOfNotDeletedBankAccount);
        notDeletedBankAccount.setDeleted(false);

        final BigDecimal moneyOfDeletedBankAccount = BigDecimal.ZERO;
        final boolean blockedOfDeletedBankAccount = false;
        final BankAccount deletedBankAccount = new BankAccount(moneyOfDeletedBankAccount, blockedOfDeletedBankAccount,
                numberOfNotDeletedBankAccount);
        deletedBankAccount.setDeleted(true);

        assert this.bankAccountDAO != null;
        this.bankAccountDAO.addEntity(notDeletedBankAccount);
        try
        {
            this.bankAccountDAO.addEntity(deletedBankAccount);
            try
            {
                final Collection<BankAccount> allBankAccounts = this.bankAccountDAO.offloadEntities();
                final boolean testSuccess = allBankAccounts != null && !allBankAccounts.isEmpty()
                        && allBankAccounts.contains(notDeletedBankAccount)
                        && allBankAccounts.contains(deletedBankAccount);
                Assert.assertTrue(testSuccess);
            }
            finally
            {
                this.bankAccountDAO.deleteEntity(deletedBankAccount);
            }
        }
        finally
        {
            this.bankAccountDAO.deleteEntity(notDeletedBankAccount);
        }
    }
    //Test for trigger 'check_unique_number_not_deleted_bank_accounts_before_adding'
    @Test(expectedExceptions = {AddingEntityException.class})
    public final void twoBankAccountWithNotDeletedStatusAndSameNumberShouldNotBeAdded()
            throws AddingEntityException, DeletingEntityException
    {
        final BigDecimal moneyOfFirstBankAccount = BigDecimal.ZERO;
        final boolean blockedOfFirstBankAccount = false;
        final String numberOfFirstBankAccount = "11112222333344445555";
        final BankAccount firstBankAccount = new BankAccount(moneyOfFirstBankAccount, blockedOfFirstBankAccount,
                numberOfFirstBankAccount);
        firstBankAccount.setDeleted(false);

        final BigDecimal moneyOfSecondBankAccount = BigDecimal.ZERO;
        final boolean blockedOfSecondBankAccount = false;
        final BankAccount secondBankAccount = new BankAccount(moneyOfSecondBankAccount, blockedOfSecondBankAccount,
                numberOfFirstBankAccount);
        secondBankAccount.setDeleted(false);

        try
        {
            assert this.bankAccountDAO != null;
            this.bankAccountDAO.addEntity(firstBankAccount);
        }
        catch(final AddingEntityException addingEntityException)
        {
            Assert.fail();
        }
        try
        {
            this.bankAccountDAO.addEntity(secondBankAccount);
            try
            {
                Assert.fail();
            }
            finally
            {
                this.bankAccountDAO.deleteEntity(secondBankAccount);
            }
        }
        finally
        {
            this.bankAccountDAO.deleteEntity(firstBankAccount);
        }
    }

    @Test(dependsOnMethods = {"bankAccountShouldBeAdded"})
    public final void allBankAccountsShouldBeOffloaded()
            throws AddingEntityException, OffloadingEntitiesException, DeletingEntityException
    {
        final BigDecimal moneyOfNotDeletedBankAccount = BigDecimal.ZERO;
        final boolean blockedOfNotDeletedBankAccount = false;
        final String numberOfNotDeletedBankAccount = "11112222333344445555";
        final BankAccount notDeletedBankAccount = new BankAccount(moneyOfNotDeletedBankAccount,
                blockedOfNotDeletedBankAccount, numberOfNotDeletedBankAccount);
        notDeletedBankAccount.setDeleted(false);

        final BigDecimal moneyOfDeletedBankAccount = BigDecimal.ZERO;
        final String numberOfDeletedBankAccount = "11112222333344445555";
        final boolean blockedOfDeletedBankAccount = false;
        final BankAccount deletedBankAccount = new BankAccount(moneyOfDeletedBankAccount, blockedOfDeletedBankAccount,
                numberOfDeletedBankAccount);
        deletedBankAccount.setDeleted(true);

        assert this.bankAccountDAO != null;
        this.bankAccountDAO.addEntity(notDeletedBankAccount);
        try
        {
            this.bankAccountDAO.addEntity(deletedBankAccount);
            try
            {
                final Collection<BankAccount> offloadedBankAccounts = this.bankAccountDAO.offloadEntities();
                final boolean testSuccess = offloadedBankAccounts != null && !offloadedBankAccounts.isEmpty()
                        && offloadedBankAccounts.contains(notDeletedBankAccount)
                        && offloadedBankAccounts.contains(deletedBankAccount);
                Assert.assertTrue(testSuccess);
            }
            finally
            {
                this.bankAccountDAO.deleteEntity(deletedBankAccount);
            }
        }
        finally
        {
            this.bankAccountDAO.deleteEntity(notDeletedBankAccount);
        }
    }

    @Test(dependsOnMethods = {"bankAccountShouldBeAdded"})
    public final void bankAccountShouldBeFoundById()
            throws AddingEntityException, FindingEntityException, DeletingEntityException
    {
        final BigDecimal money = BigDecimal.TEN;
        final boolean blocked = false;
        final String number = "11112222333344445555";
        final BankAccount bankAccount = new BankAccount(money, blocked, number);
        bankAccount.setDeleted(true);

        assert this.bankAccountDAO != null;
        this.bankAccountDAO.addEntity(bankAccount);
        try
        {
            final Optional<BankAccount> optionalOfBankAccountFoundInDataBase = this.bankAccountDAO.findEntityById(
                    bankAccount.getId());
            if(optionalOfBankAccountFoundInDataBase.isEmpty())
            {
                Assert.fail();
            }
            final BankAccount bankAccountFoundInDataBase = optionalOfBankAccountFoundInDataBase.get();
            Assert.assertEquals(bankAccount, bankAccountFoundInDataBase);
        }
        finally
        {
            this.bankAccountDAO.deleteEntity(bankAccount);
        }
    }

    @Test
    public final void bankAccountShouldNotBeFoundByNotValidId()
            throws FindingEntityException
    {
        final long idOfFoundBankAccount = -1;
        assert this.bankAccountDAO != null;
        final Optional<BankAccount> optionalOfFoundBankAccount = this.bankAccountDAO.findEntityById(
                idOfFoundBankAccount);
        Assert.assertTrue(optionalOfFoundBankAccount.isEmpty());
    }

    @Test(dependsOnMethods = {"bankAccountShouldBeAdded"})
    public final void notDeletedBankAccountsShouldBeFound()
            throws AddingEntityException, OffloadingEntitiesException, DeletingEntityException
    {
        final BigDecimal moneyOfDeletedBankAccount = BigDecimal.TEN;
        final boolean blockedOfDeletedBankAccount = false;
        final String numberOfDeletedBankAccount = "11112222333344445555";
        final BankAccount deletedBankAccount = new BankAccount(moneyOfDeletedBankAccount, blockedOfDeletedBankAccount,
                numberOfDeletedBankAccount);
        deletedBankAccount.setDeleted(true);

        final BigDecimal moneyOfNotDeletedBankAccount = BigDecimal.TEN;
        final boolean blockedOfNotDeletedBankAccount = false;
        final String numberOfNotDeletedBankAccount = "11112222333344440000";
        final BankAccount notDeletedBankAccount = new BankAccount(moneyOfNotDeletedBankAccount,
                blockedOfNotDeletedBankAccount, numberOfNotDeletedBankAccount);
        notDeletedBankAccount.setDeleted(false);

        assert this.bankAccountDAO != null;
        this.bankAccountDAO.addEntity(deletedBankAccount);
        try
        {
            this.bankAccountDAO.addEntity(notDeletedBankAccount);
            try
            {
                final Collection<BankAccount> notDeletedBankAccounts = this.bankAccountDAO.findNotDeletedEntities();
                final boolean testSuccess = notDeletedBankAccounts != null && !notDeletedBankAccounts.isEmpty()
                        && notDeletedBankAccounts.contains(notDeletedBankAccount)
                        && !notDeletedBankAccounts.contains(deletedBankAccount);
                Assert.assertTrue(testSuccess);
            }
            finally
            {
                this.bankAccountDAO.deleteEntity(notDeletedBankAccount);
            }
        }
        finally
        {
            this.bankAccountDAO.deleteEntity(deletedBankAccount);
        }
    }

    @Test(dependsOnMethods = {"bankAccountShouldBeAdded"})
    public final void deletedBankAccountsShouldBeFound()
            throws AddingEntityException, OffloadingEntitiesException, DeletingEntityException
    {
        final BigDecimal moneyOfDeletedBankAccount = BigDecimal.TEN;
        final boolean blockedOfDeletedBankAccount = false;
        final String numberOfDeletedBankAccount = "11112222333344445555";
        final BankAccount deletedBankAccount = new BankAccount(moneyOfDeletedBankAccount, blockedOfDeletedBankAccount,
                numberOfDeletedBankAccount);
        deletedBankAccount.setDeleted(true);

        final BigDecimal moneyOfNotDeletedBankAccount = BigDecimal.TEN;
        final boolean blockedOfNotDeletedBankAccount = false;
        final String numberOfNotDeletedBankAccount = "11112222333344440000";
        final BankAccount notDeletedBankAccount = new BankAccount(moneyOfNotDeletedBankAccount,
                blockedOfNotDeletedBankAccount, numberOfNotDeletedBankAccount);
        notDeletedBankAccount.setDeleted(false);

        assert this.bankAccountDAO != null;
        this.bankAccountDAO.addEntity(deletedBankAccount);
        try
        {
            this.bankAccountDAO.addEntity(notDeletedBankAccount);
            try
            {
                final Collection<BankAccount> deletedBankAccounts = this.bankAccountDAO.findDeletedEntities();
                final boolean testSuccess = deletedBankAccounts != null && !deletedBankAccounts.isEmpty()
                        && deletedBankAccounts.contains(deletedBankAccount)
                        && !deletedBankAccounts.contains(notDeletedBankAccount);
                Assert.assertTrue(testSuccess);
            }
            finally
            {
                this.bankAccountDAO.deleteEntity(notDeletedBankAccount);
            }
        }
        finally
        {
            this.bankAccountDAO.deleteEntity(deletedBankAccount);
        }
    }

    @Test(dependsOnMethods = {"bankAccountShouldBeAdded", "bankAccountShouldBeFoundById", "bankAccountShouldBeDeleted"})
    public final void bankAccountShouldBeUpdated()
            throws AddingEntityException, UpdatingEntityException, FindingEntityException, DeletingEntityException
    {
        final BigDecimal money = BigDecimal.TEN;
        final boolean blocked = false;
        final String number = "11112222333344445555";
        final BankAccount bankAccount = new BankAccount(money, blocked, number);
        bankAccount.setDeleted(true);

        assert this.bankAccountDAO != null;
        this.bankAccountDAO.addEntity(bankAccount);
        try
        {
            final BigDecimal newMoney = BigDecimal.ONE;
            final boolean newBlocked = !bankAccount.isBlocked();
            bankAccount.setMoney(newMoney);
            bankAccount.setBlocked(newBlocked);
            this.bankAccountDAO.updateEntity(bankAccount);

            final Optional<BankAccount> optionalOfUpdatedBankAccountFromDataBase = this.bankAccountDAO.findEntityById(
                    bankAccount.getId());
            if(optionalOfUpdatedBankAccountFromDataBase.isEmpty())
            {
                Assert.fail();
            }

            final BankAccount updatedBankAccountFromDataBase = optionalOfUpdatedBankAccountFromDataBase.get();
            Assert.assertEquals(bankAccount, updatedBankAccountFromDataBase);
        }
        finally
        {
            this.bankAccountDAO.deleteEntity(bankAccount);
        }
    }

    //Test for constraint 'money_of_bank_account_should_be_correct'
    @Test(dependsOnMethods = {"bankAccountShouldBeAdded"}, expectedExceptions = {UpdatingEntityException.class})
    public final void bankAccountShouldNotBeUpdatedByNegativeMoney()
            throws AddingEntityException, UpdatingEntityException, DeletingEntityException
    {
        final BigDecimal money = BigDecimal.TEN;
        final boolean blocked = false;
        final String number = "11112222333344445555";
        final BankAccount bankAccount = new BankAccount(money, blocked, number);
        bankAccount.setDeleted(false);

        assert this.bankAccountDAO != null;
        this.bankAccountDAO.addEntity(bankAccount);
        try
        {
            final BigDecimal newMoney = new BigDecimal("-1");
            bankAccount.setMoney(newMoney);
            this.bankAccountDAO.updateEntity(bankAccount);
        }
        finally
        {
            this.bankAccountDAO.deleteEntity(bankAccount);
        }
    }

    //Test for constraint 'number_of_bank_account_should_be_correct'
    @Test(dependsOnMethods = {"bankAccountShouldBeAdded"}, expectedExceptions = {UpdatingEntityException.class})
    public final void bankAccountShouldNotBeUpdatedByNotValidNumber()
            throws AddingEntityException, UpdatingEntityException, DeletingEntityException
    {
        final BigDecimal money = BigDecimal.TEN;
        final boolean blocked = false;
        final String number = "11112222333344445555";
        final BankAccount bankAccount = new BankAccount(money, blocked, number);
        bankAccount.setDeleted(false);

        assert this.bankAccountDAO != null;
        this.bankAccountDAO.addEntity(bankAccount);
        try
        {
            final String newNumber = "1111222233334444555"; //should contain 20 digits - not 19
            bankAccount.setNumber(newNumber);
            this.bankAccountDAO.updateEntity(bankAccount);
        }
        finally
        {
            this.bankAccountDAO.deleteEntity(bankAccount);
        }
    }

    //Test for trigger 'check_unique_number_not_deleted_bank_accounts_before_updating'
    @Test(dependsOnMethods = {"bankAccountShouldBeAdded"}, expectedExceptions = {UpdatingEntityException.class})
    public final void notDeletedBankAccountShouldNotBeUpdatedByNumberOfAlreadyExistingAnotherNotDeletedBankAccount()
            throws AddingEntityException, UpdatingEntityException, DeletingEntityException
    {
        final BigDecimal moneyOfFirstBankAccount = BigDecimal.TEN;
        final boolean blockedOfFirstBankAccount = false;
        final String numberOfFirstBankAccount = "11112222333344445555";
        final BankAccount firstBankAccount = new BankAccount(moneyOfFirstBankAccount, blockedOfFirstBankAccount,
                numberOfFirstBankAccount);
        firstBankAccount.setDeleted(false);

        final BigDecimal moneyOfSecondBankAccount = BigDecimal.TEN;
        final boolean blockedOfSecondBankAccount = false;
        final String numberOfSecondBankAccount = "11112222333344440000";
        final BankAccount secondBankAccount = new BankAccount(moneyOfSecondBankAccount, blockedOfSecondBankAccount,
                numberOfSecondBankAccount);
        secondBankAccount.setDeleted(false);

        assert this.bankAccountDAO != null;
        this.bankAccountDAO.addEntity(firstBankAccount);
        try
        {
            this.bankAccountDAO.addEntity(secondBankAccount);
            try
            {
                secondBankAccount.setNumber(firstBankAccount.getNumber());
                this.bankAccountDAO.updateEntity(secondBankAccount);
            }
            finally
            {
                this.bankAccountDAO.deleteEntity(secondBankAccount);
            }
        }
        finally
        {
            this.bankAccountDAO.deleteEntity(firstBankAccount);
        }
    }

    //Test for trigger 'check_unique_number_not_deleted_bank_accounts_before_updating'
    @Test(dependsOnMethods = {"bankAccountShouldBeAdded", "bankAccountShouldBeFoundById"})
    public final void notDeletedBankAccountShouldBeUpdatedByNumberOfAlreadyExistingAnotherDeletedBankAccount()
            throws AddingEntityException, UpdatingEntityException, FindingEntityException, DeletingEntityException
    {
        final BigDecimal moneyOfNotDeletedBankAccount = BigDecimal.TEN;
        final boolean blockedOfNotDeletedBankAccount = false;
        final String numberOfNotDeletedBankAccount = "11112222333344445555";
        final BankAccount notDeletedBankAccount = new BankAccount(moneyOfNotDeletedBankAccount,
                blockedOfNotDeletedBankAccount, numberOfNotDeletedBankAccount);
        notDeletedBankAccount.setDeleted(false);

        final BigDecimal moneyOfDeletedBankAccount = BigDecimal.TEN;
        final boolean blockedOfDeletedBankAccount = false;
        final String numberOfDeletedBankAccount = "11112222333344440000";
        final BankAccount deletedBankAccount = new BankAccount(moneyOfDeletedBankAccount, blockedOfDeletedBankAccount,
                numberOfDeletedBankAccount);
        deletedBankAccount.setDeleted(true);

        assert this.bankAccountDAO != null;
        this.bankAccountDAO.addEntity(notDeletedBankAccount);
        try
        {
            this.bankAccountDAO.addEntity(deletedBankAccount);
            try
            {
                notDeletedBankAccount.setNumber(deletedBankAccount.getNumber());
                this.bankAccountDAO.updateEntity(notDeletedBankAccount);

                final Optional<BankAccount> optionalOfUpdatedBankAccount = this.bankAccountDAO.findEntityById(
                        notDeletedBankAccount.getId());
                if(optionalOfUpdatedBankAccount.isEmpty())
                {
                    Assert.fail();
                }
                final BankAccount updatedBankAccount = optionalOfUpdatedBankAccount.get();
                Assert.assertEquals(updatedBankAccount, notDeletedBankAccount);
            }
            finally
            {
                this.bankAccountDAO.deleteEntity(deletedBankAccount);
            }
        }
        finally
        {
            this.bankAccountDAO.deleteEntity(notDeletedBankAccount);
        }
    }

    @Test(dependsOnMethods = {"bankAccountShouldBeAdded", "bankAccountShouldBeFoundById", "bankAccountShouldBeDeleted"})
    public final void deletedStatusOfBankAccountWithGivenIdShouldBeUpdated()
            throws AddingEntityException, UpdatingEntityException, FindingEntityException, DeletingEntityException
    {
        final BigDecimal money = BigDecimal.TEN;
        final boolean blocked = false;
        final String number = "11112222333344445555";
        final BankAccount bankAccount = new BankAccount(money, blocked, number);
        bankAccount.setDeleted(true);

        assert this.bankAccountDAO != null;
        this.bankAccountDAO.addEntity(bankAccount);
        try
        {
            final boolean newDeleted = !bankAccount.isDeleted();
            this.bankAccountDAO.updateDeletedStatusOfEntity(bankAccount.getId(), newDeleted);

            final Optional<BankAccount> optionalOfUpdatedBankAccount = this.bankAccountDAO.findEntityById(
                    bankAccount.getId());
            if(optionalOfUpdatedBankAccount.isEmpty())
            {
                Assert.fail();
            }
            final BankAccount updatedBankAccount = optionalOfUpdatedBankAccount.get();

            Assert.assertEquals(updatedBankAccount.isDeleted(), newDeleted);
        }
        finally
        {
            this.bankAccountDAO.deleteEntity(bankAccount);
        }
    }

    @Test(dependsOnMethods = {"bankAccountShouldBeAdded", "bankAccountShouldBeFoundById", "bankAccountShouldBeDeleted"})
    public final void deletedStatusOfBankAccountShouldBeUpdated()
            throws AddingEntityException, UpdatingEntityException, FindingEntityException, DeletingEntityException
    {
        final BigDecimal money = BigDecimal.TEN;
        final boolean blocked = false;
        final String number = "11112222333344445555";
        final BankAccount bankAccount = new BankAccount(money, blocked, number);
        bankAccount.setDeleted(false);

        assert this.bankAccountDAO != null;
        this.bankAccountDAO.addEntity(bankAccount);
        try
        {
            final boolean newDeleted = !bankAccount.isDeleted();
            this.bankAccountDAO.updateDeletedStatusOfEntity(bankAccount, newDeleted);

            final Optional<BankAccount> optionalOfUpdatedBankAccount = this.bankAccountDAO.findEntityById(
                    bankAccount.getId());
            if(optionalOfUpdatedBankAccount.isEmpty())
            {
                Assert.fail();
            }
            final BankAccount updatedBankAccount = optionalOfUpdatedBankAccount.get();

            Assert.assertEquals(updatedBankAccount.isDeleted(), newDeleted);
        }
        finally
        {
            this.bankAccountDAO.deleteEntity(bankAccount);
        }
    }

    //Test for trigger 'check_unique_number_not_deleted_bank_accounts_before_updating'
    @Test(dependsOnMethods = {"bankAccountShouldBeAdded"}, expectedExceptions = UpdatingEntityException.class)
    public final void deletedStatusShouldNotBeUpdatedBecauseNumbersOfNotDeletedBankAccountsShouldBeUnique()
            throws AddingEntityException, UpdatingEntityException, DeletingEntityException
    {
        final BigDecimal moneyOfNotDeletedBankAccount = BigDecimal.TEN;
        final boolean blockedOfNotDeletedBankAccount = false;
        final String numberOfNotDeletedBankAccount = "11112222333344445555";
        final BankAccount notDeletedBankAccount = new BankAccount(moneyOfNotDeletedBankAccount,
                blockedOfNotDeletedBankAccount, numberOfNotDeletedBankAccount);
        notDeletedBankAccount.setDeleted(false);

        final BigDecimal moneyOfDeletedBankAccount = BigDecimal.TEN;
        final boolean blockedOfDeletedBankAccount = false;
        final String numberOfDeletedBankAccount = "11112222333344445555";
        final BankAccount deletedBankAccount = new BankAccount(moneyOfDeletedBankAccount, blockedOfDeletedBankAccount,
                numberOfDeletedBankAccount);
        deletedBankAccount.setDeleted(true);

        assert this.bankAccountDAO != null;
        this.bankAccountDAO.addEntity(notDeletedBankAccount);
        try
        {
            this.bankAccountDAO.addEntity(deletedBankAccount);
            try
            {
                this.bankAccountDAO.updateDeletedStatusOfEntity(deletedBankAccount, false);
                Assert.fail();
            }
            finally
            {
                this.bankAccountDAO.deleteEntity(deletedBankAccount);
            }
        }
        finally
        {
            this.bankAccountDAO.deleteEntity(notDeletedBankAccount);
        }
    }

    //Test for trigger 'update_deleted_status_in_clients_of_deleted_bank_account'
    @Test(dependsOnMethods = {"bankAccountShouldBeAdded"})
    public final void clientsAssociatedWithDeletedBankAccountShouldBeMarkedAsDeleted()
            throws AddingEntityException, UpdatingEntityException, FindingEntityException, DeletingEntityException
    {
        final BigDecimal money = BigDecimal.TEN;
        final boolean blocked = false;
        final String number = "11112222333344445555";
        final BankAccount bankAccount = new BankAccount(money, blocked, number);
        bankAccount.setDeleted(false);

        assert this.bankAccountDAO != null;
        this.bankAccountDAO.addEntity(bankAccount);
        try
        {
            final String emailOfFirstClient = "first_not_existing_email@mail.ru";
            final String passwordOfFirstClient = "password";
            final String nameOfFirstClient = "Vlad";
            final String surnameOfFirstClient = "Zuev";
            final String patronymicOfFirstClient = "Sergeevich";
            final String phoneNumberOfFirstClient = "+375-44-111-11-11";
            final Client firstClient = new Client(emailOfFirstClient, passwordOfFirstClient, nameOfFirstClient,
                    surnameOfFirstClient, patronymicOfFirstClient, phoneNumberOfFirstClient, bankAccount);
            firstClient.setDeleted(false);

            final String emailOfSecondClient = "second_not_existing_email@mail.ru";
            final String passwordOfSecondClient = "password";
            final String nameOfSecondClient = "Ivan";
            final String surnameOfSecondClient = "Ivanov";
            final String patronymicOfSecondClient = "Ivanovich";
            final String phoneNumberOfSecondClient = "+375-44-222-22-22";
            final Client secondClient = new Client(emailOfSecondClient, passwordOfSecondClient, nameOfSecondClient,
                    surnameOfSecondClient, patronymicOfSecondClient, phoneNumberOfSecondClient, bankAccount);
            secondClient.setDeleted(false);

            assert this.clientDAO != null;
            this.clientDAO.addEntity(firstClient);
            try
            {
                this.clientDAO.addEntity(secondClient);
                try
                {
                    this.bankAccountDAO.updateDeletedStatusOfEntity(bankAccount, true);

                    final Optional<Client> optionalOfUpdatedFirstClient = this.clientDAO.findEntityById(
                            firstClient.getId());
                    final Optional<Client> optionalOfUpdatedSecondClient = this.clientDAO.findEntityById(
                            secondClient.getId());
                    if(optionalOfUpdatedFirstClient.isEmpty() || optionalOfUpdatedSecondClient.isEmpty())
                    {
                        Assert.fail();
                    }
                    final Client updatedFirstClient = optionalOfUpdatedFirstClient.get();
                    final Client updatedSecondClient = optionalOfUpdatedSecondClient.get();

                    Assert.assertTrue(updatedFirstClient.isDeleted() && updatedSecondClient.isDeleted());
                }
                finally
                {
                    this.clientDAO.deleteEntity(secondClient);
                }
            }
            finally
            {
                this.clientDAO.deleteEntity(firstClient);
            }
        }
        finally
        {
            this.bankAccountDAO.deleteEntity(bankAccount);
        }
    }

    @Test(dependsOnMethods = {"bankAccountShouldBeAdded", "bankAccountShouldBeFoundById"})
    public final void bankAccountShouldBeDeletedById()
            throws AddingEntityException, DeletingEntityException, FindingEntityException
    {
        final BigDecimal money = BigDecimal.TEN;
        final boolean blocked = false;
        final String number = "11112222333344445555";
        final BankAccount bankAccount = new BankAccount(money, blocked, number);
        bankAccount.setDeleted(true);
        assert this.bankAccountDAO != null;
        this.bankAccountDAO.addEntity(bankAccount);

        this.bankAccountDAO.deleteEntity(bankAccount.getId());

        final Optional<BankAccount> optionalOfFoundBankAccount = this.bankAccountDAO.findEntityById(
                bankAccount.getId());
        Assert.assertTrue(optionalOfFoundBankAccount.isEmpty());
    }

    @Test
    public final void bankAccountShouldNotBeDeletedByNotValidId()
            throws OffloadingEntitiesException, DeletingEntityException
    {
        final long idOfDeletedBankAccount = -1;

        assert this.bankAccountDAO != null;
        final Collection<BankAccount> bankAccountsBeforeDeleting = this.bankAccountDAO.offloadEntities();
        this.bankAccountDAO.deleteEntity(idOfDeletedBankAccount);
        final Collection<BankAccount> bankAccountsAfterDeleting = this.bankAccountDAO.offloadEntities();

        Assert.assertEquals(bankAccountsBeforeDeleting, bankAccountsAfterDeleting);
    }

    @Test(dependsOnMethods = {"bankAccountShouldBeAdded", "bankAccountShouldBeFoundById"})
    public final void bankAccountShouldBeDeleted()
            throws AddingEntityException, DeletingEntityException, FindingEntityException
    {
        final BigDecimal money = BigDecimal.TEN;
        final boolean blocked = false;
        final String number = "11112222333344445555";
        final BankAccount bankAccount = new BankAccount(money, blocked, number);
        assert this.bankAccountDAO != null;
        this.bankAccountDAO.addEntity(bankAccount);

        this.bankAccountDAO.deleteEntity(bankAccount);

        final Optional<BankAccount> optionalOfFoundBankAccount = this.bankAccountDAO.findEntityById(
                bankAccount.getId());
        Assert.assertTrue(optionalOfFoundBankAccount.isEmpty());
    }

    @Test
    public final void bankAccountWithNotValidIdShouldNotBeDeleted()
            throws OffloadingEntitiesException, DeletingEntityException
    {
        final long id = -1;
        final BigDecimal money = BigDecimal.TEN;
        final boolean blocked = false;
        final String number = "11112222333344445555";
        final BankAccount bankAccount = new BankAccount(id, money, blocked, number);

        assert this.bankAccountDAO != null;
        final Collection<BankAccount> bankAccountsBeforeDeleting = this.bankAccountDAO.offloadEntities();
        this.bankAccountDAO.deleteEntity(bankAccount);
        final Collection<BankAccount> bankAccountsAfterDeleting = this.bankAccountDAO.offloadEntities();

        Assert.assertEquals(bankAccountsBeforeDeleting, bankAccountsAfterDeleting);
    }

    //Test for trigger 'delete_not_associated_users'
    @Test
    public final void clientsAssociatedWithDeletedBankAccountShouldBeDeleted()
            throws AddingEntityException, DeletingEntityException, FindingEntityException
    {
        final BigDecimal money = BigDecimal.TEN;
        final boolean blocked = false;
        final String number = "11112222333344445555";
        final BankAccount bankAccount = new BankAccount(money, blocked, number);
        bankAccount.setDeleted(false);

        assert this.bankAccountDAO != null;
        this.bankAccountDAO.addEntity(bankAccount);
        try
        {
            final String emailOfFirstClient = "first_not_existing_email@mail.ru";
            final String passwordOfFirstClient = "password";
            final String nameOfFirstClient = "Vlad";
            final String surnameOfFirstClient = "Zuev";
            final String patronymicOfFirstClient = "Sergeevich";
            final String phoneNumberOfFirstClient = "+375-44-111-11-11";
            final Client firstClient = new Client(emailOfFirstClient, passwordOfFirstClient, nameOfFirstClient,
                    surnameOfFirstClient, patronymicOfFirstClient, phoneNumberOfFirstClient, bankAccount);
            firstClient.setDeleted(false);

            final String emailOfSecondClient = "second_not_existing_email@mail.ru";
            final String passwordOfSecondClient = "password";
            final String nameOfSecondClient = "Ivan";
            final String surnameOfSecondClient = "Ivanov";
            final String patronymicOfSecondClient = "Ivanovich";
            final String phoneNumberOfSecondClient = "+375-44-222-22-22";
            final Client secondClient = new Client(emailOfSecondClient, passwordOfSecondClient, nameOfSecondClient,
                    surnameOfSecondClient, patronymicOfSecondClient, phoneNumberOfSecondClient, bankAccount);
            secondClient.setDeleted(false);

            assert this.clientDAO != null;
            this.clientDAO.addEntity(firstClient);
            try
            {
                this.clientDAO.addEntity(secondClient);
                try
                {
                    this.bankAccountDAO.deleteEntity(bankAccount);

                    final Optional<Client> optionalOfFirstClient = this.clientDAO.findEntityById(firstClient.getId());
                    final Optional<Client> optionalOfSecondClient = this.clientDAO.findEntityById(secondClient.getId());

                    assert this.userDAO != null;
                    final Optional<User> optionalOfFirstUser = this.userDAO.findEntityById(firstClient.getId());
                    final Optional<User> optionalOfSecondUser = this.userDAO.findEntityById(secondClient.getId());

                    final boolean testSuccess = optionalOfFirstClient.isEmpty() && optionalOfSecondClient.isEmpty()
                            && optionalOfFirstUser.isEmpty() && optionalOfSecondUser.isEmpty();
                    Assert.assertTrue(testSuccess);
                }
                catch(final DeletingEntityException | FindingEntityException exception)
                {
                    this.clientDAO.deleteEntity(secondClient);
                    throw exception;
                }
            }
            catch(final AddingEntityException | DeletingEntityException | FindingEntityException exception)
            {
                this.clientDAO.deleteEntity(firstClient);
                throw exception;
            }
        }
        catch(final AddingEntityException | DeletingEntityException | FindingEntityException exception)
        {
            this.bankAccountDAO.deleteEntity(bankAccount);
            throw exception;
        }
    }

    @Test(dependsOnMethods = {"bankAccountShouldBeAdded"})
    public final void bankAccountWithGivenIdShouldBeExisted()
            throws AddingEntityException, DefiningExistingEntityException, DeletingEntityException
    {
        final BigDecimal money = BigDecimal.TEN;
        final boolean blocked = false;
        final String number = "11112222333344445555";
        final BankAccount bankAccount = new BankAccount(money, blocked, number);
        bankAccount.setDeleted(true);

        assert this.bankAccountDAO != null;
        this.bankAccountDAO.addEntity(bankAccount);
        try
        {
            final boolean bankAccountExists = this.bankAccountDAO.isEntityWithGivenIdExisting(bankAccount.getId());
            Assert.assertTrue(bankAccountExists);
        }
        finally
        {
            this.bankAccountDAO.deleteEntity(bankAccount);
        }
    }

    @Test
    public final void bankAccountWithNotValidIdShouldNotBeExisted()
            throws DefiningExistingEntityException
    {
        final long idOfResearchBankAccount = -1;
        assert this.bankAccountDAO != null;
        final boolean bankAccountExists = this.bankAccountDAO.isEntityWithGivenIdExisting(idOfResearchBankAccount);
        Assert.assertFalse(bankAccountExists);
    }

    @Test(dependsOnMethods = {"bankAccountShouldBeAdded"})
    public final void notDeletedBankAccountShouldBeFoundByNumber()
            throws AddingEntityException, FindingEntityException, DeletingEntityException
    {
        final BigDecimal money = BigDecimal.TEN;
        final boolean blocked = false;
        final String number = "11112222333344445555";
        final BankAccount bankAccount = new BankAccount(money, blocked, number);
        bankAccount.setDeleted(false);

        assert this.bankAccountDAO != null;
        this.bankAccountDAO.addEntity(bankAccount);
        try
        {
            final Optional<BankAccount> optionalOfFoundBankAccount = this.bankAccountDAO
                    .findNotDeletedBankAccountByNumber(bankAccount.getNumber());
            if(optionalOfFoundBankAccount.isEmpty())
            {
                Assert.fail();
            }
            final BankAccount foundBankAccount = optionalOfFoundBankAccount.get();
            Assert.assertEquals(bankAccount, foundBankAccount);
        }
        finally
        {
            this.bankAccountDAO.deleteEntity(bankAccount);
        }
    }

    @Test
    public final void bankAccountShouldNotBeFoundByNotValidNumber()
            throws FindingEntityException
    {
        final String number = "9999999999999999999";    //short number
        assert this.bankAccountDAO != null;
        final Optional<BankAccount> optionalOfBankAccount = this.bankAccountDAO.findNotDeletedBankAccountByNumber(number);
        Assert.assertTrue(optionalOfBankAccount.isEmpty());
    }

    @Test(dependsOnMethods = {"bankAccountShouldBeAdded"})
    public final void notDeletedBankAccountShouldBeDefinedAsExistingByNumber()
            throws AddingEntityException, DefiningExistingEntityException, DeletingEntityException
    {
        final BigDecimal money = BigDecimal.TEN;
        final boolean blocked = false;
        final String number = "11112222333344445555";
        final BankAccount bankAccount = new BankAccount(money, blocked, number);
        bankAccount.setDeleted(false);

        assert this.bankAccountDAO != null;
        this.bankAccountDAO.addEntity(bankAccount);
        try
        {
            final boolean bankAccountExists = this.bankAccountDAO.isNotDeletedBankAccountWithGivenNumberExist(
                    bankAccount.getNumber());
            Assert.assertTrue(bankAccountExists);
        }
        finally
        {
            this.bankAccountDAO.deleteEntity(bankAccount);
        }
    }

    @Test
    public final void bankAccountWithNotValidNumberShouldNotBeDefinedAsExisting()
            throws DefiningExistingEntityException
    {
        final String number = "1111222233334444555";  //short number: 19 digits instead of 20
        assert this.bankAccountDAO != null;
        final boolean bankAccountExists = this.bankAccountDAO.isNotDeletedBankAccountWithGivenNumberExist(number);
        Assert.assertFalse(bankAccountExists);
    }

    @Test(dependsOnMethods = {"bankAccountShouldBeAdded"})
    public final void bankAccountsShouldBeFoundByNumber()
            throws AddingEntityException, DeletingEntityException, OffloadingEntitiesException {
        final String numberOfBankAccounts = "11112222333344445555";

        final BigDecimal moneyOfNotDeletedBankAccount = BigDecimal.TEN;
        final boolean blockedOfNotDeletedBankAccount = false;
        final BankAccount notDeletedBankAccount = new BankAccount(moneyOfNotDeletedBankAccount,
                blockedOfNotDeletedBankAccount, numberOfBankAccounts);
        notDeletedBankAccount.setDeleted(false);

        final BigDecimal moneyOfDeletedBankAccount = BigDecimal.TEN;
        final boolean blockedOfDeletedBankAccount = false;
        final BankAccount deletedBankAccount = new BankAccount(moneyOfDeletedBankAccount, blockedOfDeletedBankAccount,
                numberOfBankAccounts);
        deletedBankAccount.setDeleted(true);

        assert this.bankAccountDAO != null;
        this.bankAccountDAO.addEntity(notDeletedBankAccount);
        try
        {
            this.bankAccountDAO.addEntity(deletedBankAccount);
            try
            {
                final Collection<BankAccount> foundBankAccounts = this.bankAccountDAO.findBankAccountsByNumber(
                        numberOfBankAccounts);
                final boolean testSuccess = foundBankAccounts != null && !foundBankAccounts.isEmpty()
                        && foundBankAccounts.contains(notDeletedBankAccount)
                        && foundBankAccounts.contains(deletedBankAccount);
                Assert.assertTrue(testSuccess);
            }
            finally
            {
                this.bankAccountDAO.deleteEntity(deletedBankAccount);
            }
        }
        finally
        {
            this.bankAccountDAO.deleteEntity(notDeletedBankAccount);
        }
    }

    @Test
    public final void bankAccountsShouldNotBeFoundByNotValidNumber()
            throws OffloadingEntitiesException
    {
        final String numberOfBankAccounts = "1111222233334444555";   //short number of bank account
        assert this.bankAccountDAO != null;
        final Collection<BankAccount> foundBankAccounts = this.bankAccountDAO.findBankAccountsByNumber(
                numberOfBankAccounts);
        Assert.assertTrue(foundBankAccounts.isEmpty());
    }
}
