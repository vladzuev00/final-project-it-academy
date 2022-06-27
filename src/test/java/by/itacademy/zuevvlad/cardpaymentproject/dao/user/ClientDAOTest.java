package by.itacademy.zuevvlad.cardpaymentproject.dao.user;

import by.itacademy.zuevvlad.cardpaymentproject.configuration.DAOConfiguration;
import by.itacademy.zuevvlad.cardpaymentproject.configuration.WebMainConfiguration;
import by.itacademy.zuevvlad.cardpaymentproject.dao.BankAccountDAO;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.*;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Administrator;
import by.itacademy.zuevvlad.cardpaymentproject.entity.BankAccount;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;

@Test
@ContextConfiguration(classes = {WebMainConfiguration.class})
public final class ClientDAOTest extends AbstractTestNGSpringContextTests
{
    @Autowired
    @Qualifier(value = DAOConfiguration.NAME_OF_BEAN_OF_CLIENT_DAO)
    private final ClientDAO clientDAO;

    @Autowired
    @Qualifier(value = DAOConfiguration.NAME_OF_BEAN_OF_ADMINISTRATOR_DAO)
    private final AdministratorDAO administratorDAO;

    @Autowired
    @Qualifier(value = DAOConfiguration.NAME_OF_BEAN_OF_BANK_ACCOUNT_DAO)
    private final BankAccountDAO bankAccountDAO;

    private BankAccount insertedBankAccount;

    public ClientDAOTest()
    {
        super();
        this.clientDAO = null;
        this.administratorDAO = null;
        this.bankAccountDAO = null;
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
        assert this.bankAccountDAO != null;
        this.bankAccountDAO.addEntity(bankAccount);
        this.insertedBankAccount = bankAccount;
    }

    @AfterClass
    public final void deleteBankAccount()
            throws DeletingEntityException
    {
        assert this.bankAccountDAO != null;
        this.bankAccountDAO.deleteEntity(this.insertedBankAccount);
    }

    @Test
    public final void clientShouldBeAdded()
            throws AddingEntityException, FindingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final String name = "vlad";
        final String surname = "zuev";
        final String patronymic = "sergeevich";
        final String phoneNumber = "+375-44-111-11-11";
        final Client client = new Client(email, password, name, surname, patronymic, phoneNumber,
                this.insertedBankAccount);
        client.setDeleted(true);

        final long idOfClientBeforeAdding = client.getId();
        assert this.clientDAO != null;
        this.clientDAO.addEntity(client);
        try
        {
            final long idOfClientAfterAdding = client.getId();
            final Optional<Client> optionalOfAddedClientFromDataBase = this.clientDAO.findEntityById(
                    idOfClientAfterAdding);
            if(optionalOfAddedClientFromDataBase.isEmpty())
            {
                Assert.fail();
            }
            final Client addedClientFromDataBase = optionalOfAddedClientFromDataBase.get();

            final boolean testSuccess = client.equals(addedClientFromDataBase)
                    && idOfClientBeforeAdding != idOfClientAfterAdding;
            Assert.assertTrue(testSuccess);
        }
        finally
        {
            this.clientDAO.deleteEntity(client);
        }
    }

    //Test for constraint 'name_should_be_correct'
    @Test(expectedExceptions = {AddingEntityException.class})
    public final void clientWithNotValidNameShouldNotBeAdded()
            throws AddingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final String name = "211";
        final String surname = "zuev";
        final String patronymic = "sergeevich";
        final String phoneNumber = "+375-44-111-11-11";
        final Client client = new Client(email, password, name, surname, patronymic, phoneNumber,
                this.insertedBankAccount);
        client.setDeleted(false);

        assert this.clientDAO != null;
        this.clientDAO.addEntity(client);
        try
        {
            Assert.fail();
        }
        finally
        {
            this.clientDAO.deleteEntity(client);
        }
    }

    //Test for constraint 'surname_should_be_correct'
    @Test(expectedExceptions = AddingEntityException.class)
    public final void clientWithNotValidSurnameShouldNotBeAdded()
        throws AddingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final String name = "vlad";
        final String surname = "123";
        final String patronymic = "sergeevich";
        final String phoneNumber = "+375-44-111-11-11";
        final Client client = new Client(email, password, name, surname, patronymic, phoneNumber,
                this.insertedBankAccount);
        client.setDeleted(false);

        assert this.clientDAO != null;
        this.clientDAO.addEntity(client);
        try
        {
            Assert.fail();
        }
        finally
        {
            this.clientDAO.deleteEntity(client);
        }
    }

    //Test for constraint 'patronymic_should_be_correct'
    @Test(expectedExceptions = {AddingEntityException.class})
    public final void clientWithNotValidPatronymicShouldNotBeAdded()
            throws AddingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final String name = "vlad";
        final String surname = "zuev";
        final String patronymic = "123";
        final String phoneNumber = "+375-44-111-11-11";
        final Client client = new Client(email, password, name, surname, patronymic, phoneNumber,
                this.insertedBankAccount);
        client.setDeleted(false);

        assert this.clientDAO != null;
        this.clientDAO.addEntity(client);
        try
        {
            Assert.fail();
        }
        finally
        {
            this.clientDAO.deleteEntity(client);
        }
    }

    //Test for constraint 'phone_number_should_be_correct'
    @Test(expectedExceptions = {AddingEntityException.class})
    public final void clientWithNotValidNumberShouldNotBeAdded()
            throws AddingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final String name = "vlad";
        final String surname = "zuev";
        final String patronymic = "sergeevich";
        final String phoneNumber = "+375-44-111-1111";
        final Client client = new Client(email, password, name, surname, patronymic, phoneNumber,
                this.insertedBankAccount);
        client.setDeleted(false);

        assert this.clientDAO != null;
        this.clientDAO.addEntity(client);
        try
        {
            Assert.fail();
        }
        finally
        {
            this.clientDAO.deleteEntity(client);
        }
    }

    //Test for trigger 'check_unique_phone_number_not_deleted_clients_before_adding'
    @Test
    public final void twoClientsWithSamePhoneNumberButDifferentDeletedStatusShouldBeAdded()
            throws AddingEntityException, OffloadingEntitiesException, DeletingEntityException
    {
        final String emailOfNotDeletedClient = "first_not_existing_email@mail.ru";
        final String passwordOfNotDeletedClient = "password";
        final String nameOfNotDeletedClient = "vlad";
        final String surnameOfNotDeletedClient = "zuev";
        final String patronymicOfNotDeletedClient = "sergeevich";
        final String phoneNumberOfNotDeletedClient = "+375-44-111-11-11";
        final Client notDeletedClient = new Client(emailOfNotDeletedClient, passwordOfNotDeletedClient,
                nameOfNotDeletedClient, surnameOfNotDeletedClient, patronymicOfNotDeletedClient,
                phoneNumberOfNotDeletedClient, this.insertedBankAccount);
        notDeletedClient.setDeleted(false);

        final String emailOfDeletedClient = "second_not_existing_email@mail.ru";
        final String passwordOfDeletedClient = "password";
        final String nameOfDeletedClient = "vlad";
        final String surnameOfDeletedClient = "zuev";
        final String patronymicOfDeletedClient = "sergeevich";
        final Client deletedClient = new Client(emailOfDeletedClient, passwordOfDeletedClient,
                nameOfDeletedClient, surnameOfDeletedClient, patronymicOfDeletedClient,
                phoneNumberOfNotDeletedClient, this.insertedBankAccount);
        deletedClient.setDeleted(true);

        assert this.clientDAO != null;
        this.clientDAO.addEntity(notDeletedClient);
        try
        {
            this.clientDAO.addEntity(deletedClient);
            try
            {
                final Collection<Client> offloadedClients = this.clientDAO.offloadEntities();
                final boolean testSuccess = offloadedClients != null && !offloadedClients.isEmpty()
                        && offloadedClients.contains(notDeletedClient) && offloadedClients.contains(deletedClient);
                Assert.assertTrue(testSuccess);
            }
            finally
            {
                this.clientDAO.deleteEntity(deletedClient);
            }
        }
        finally
        {
            this.clientDAO.deleteEntity(notDeletedClient);
        }
    }

    //Test for trigger 'check_unique_phone_number_not_deleted_clients_before_adding'
    @Test(expectedExceptions = {AddingEntityException.class})
    public final void twoClientsWithSamePhoneNumbersAndNotDeletedStatusShouldNotBeAdded()
            throws AddingEntityException, DeletingEntityException
    {
        final String emailOfFirstClient = "first_not_existing_email@mail.ru";
        final String passwordOfFirstClient = "password";
        final String nameOfFirstClient = "vlad";
        final String surnameOfFirstClient = "zuev";
        final String patronymicOfFirstClient = "sergeevich";
        final String phoneNumberOfFirstClient = "+375-44-111-11-11";
        final Client firstClient = new Client(emailOfFirstClient, passwordOfFirstClient, nameOfFirstClient,
                surnameOfFirstClient, patronymicOfFirstClient, phoneNumberOfFirstClient, this.insertedBankAccount);
        firstClient.setDeleted(false);

        final String emailOfSecondClient = "second_not_existing_email@mail.ru";
        final String passwordOfSecondClient = "password";
        final String nameOfSecondClient = "vlad";
        final String surnameOfSecondClient = "zuev";
        final String patronymicOfSecondClient = "sergeevich";
        final Client secondClient = new Client(emailOfSecondClient, passwordOfSecondClient, nameOfSecondClient,
                surnameOfSecondClient, patronymicOfSecondClient, phoneNumberOfFirstClient, this.insertedBankAccount);
        secondClient.setDeleted(false);

        try
        {
            assert this.clientDAO != null;
            this.clientDAO.addEntity(firstClient);
        }
        catch(final AddingEntityException addingEntityException)
        {
            Assert.fail();
        }
        try
        {
            this.clientDAO.addEntity(secondClient);
            this.clientDAO.deleteEntity(secondClient);
        }
        finally
        {
            this.clientDAO.deleteEntity(firstClient);
        }
    }

    //Test for trigger 'check_unique_email_not_deleted_users_before_adding'
    @Test
    public final void clientAndAdministratorWithSameEmailButDifferentDeletedStatusShouldBeAdded()
            throws AddingEntityException, OffloadingEntitiesException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";

        final String passwordOfAdministrator = "password";
        final Administrator.Level levelOfAdministrator = Administrator.Level.MAIN;
        final Administrator administrator = new Administrator(email, passwordOfAdministrator, levelOfAdministrator);
        administrator.setDeleted(true);

        final String passwordOfClient = "password";
        final String nameOfClient = "vlad";
        final String surnameOfClient = "zuev";
        final String patronymicOfClient = "sergeevich";
        final String phoneNumberOfClient = "+375-44-111-11-11";
        final Client client = new Client(email, passwordOfClient, nameOfClient, surnameOfClient, patronymicOfClient,
                phoneNumberOfClient, this.insertedBankAccount);
        client.setDeleted(false);

        assert this.administratorDAO != null;
        this.administratorDAO.addEntity(administrator);
        try
        {
            assert this.clientDAO != null;
            this.clientDAO.addEntity(client);
            try
            {
                final Collection<Administrator> offloadedAdministrators = this.administratorDAO.offloadEntities();
                final boolean administratorAddingSuccess = offloadedAdministrators != null
                        && !offloadedAdministrators.isEmpty() && offloadedAdministrators.contains(administrator);

                final Collection<Client> offloadedClients = this.clientDAO.offloadEntities();
                final boolean clientAddingSuccess = offloadedClients != null && !offloadedClients.isEmpty()
                        && offloadedClients.contains(client);

                Assert.assertTrue(administratorAddingSuccess && clientAddingSuccess);
            }
            finally
            {
                this.clientDAO.deleteEntity(client);
            }
        }
        finally
        {
            this.administratorDAO.deleteEntity(administrator);
        }
    }

    //Test for trigger 'check_unique_email_not_deleted_users_before_adding'
    @Test(expectedExceptions = {AddingEntityException.class})
    public final void clientShouldNotBeAddedBecauseOfExistingAdministratorWithSameEmailAndNotDeletedStatus()
            throws AddingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";

        final String passwordOfAdministrator = "password";
        final Administrator.Level levelOfAdministrator = Administrator.Level.MAIN;
        final Administrator administrator = new Administrator(email, passwordOfAdministrator, levelOfAdministrator);
        administrator.setDeleted(false);

        final String passwordOfClient = "password";
        final String nameOfClient = "vlad";
        final String surnameOfClient = "zuev";
        final String patronymicOfClient = "sergeevich";
        final String phoneNumberOfClient = "+375-44-111-11-11";
        final Client client = new Client(email, passwordOfClient, nameOfClient, surnameOfClient, patronymicOfClient,
                phoneNumberOfClient, this.insertedBankAccount);
        client.setDeleted(false);

        try
        {
            assert this.administratorDAO != null;
            this.administratorDAO.addEntity(administrator);
        }
        catch(final AddingEntityException addingEntityException)
        {
            Assert.fail();
        }
        try
        {
            assert this.clientDAO != null;
            this.clientDAO.addEntity(client);
            try
            {
                Assert.fail();
            }
            finally
            {
                this.clientDAO.deleteEntity(client);
            }
        }
        finally
        {
            this.administratorDAO.deleteEntity(administrator);
        }
    }

    @Test(dependsOnMethods = {"clientShouldBeAdded"})
    public final void allClientsShouldBeOffloaded()
            throws AddingEntityException, OffloadingEntitiesException, DeletingEntityException
    {
        final String emailOfNotDeletedClient = "first_not_existing_email@mail.ru";
        final String passwordOfNotDeletedClient = "password";
        final String nameOfNotDeletedClient = "vlad";
        final String surnameOfNotDeletedClient = "zuev";
        final String patronymicOfNotDeletedClient = "sergeevich";
        final String phoneNumberOfNotDeletedClient = "+375-44-111-11-11";
        final Client notDeletedClient = new Client(emailOfNotDeletedClient, passwordOfNotDeletedClient,
                nameOfNotDeletedClient, surnameOfNotDeletedClient, patronymicOfNotDeletedClient,
                phoneNumberOfNotDeletedClient, this.insertedBankAccount);
        notDeletedClient.setDeleted(false);

        final String emailOfDeletedClient = "second_not_existing_email@mail.ru";
        final String passwordOfDeletedClient = "password";
        final String nameOfDeletedClient = "vlad";
        final String surnameOfDeletedClient = "zuev";
        final String patronymicOfDeletedClient = "sergeevich";
        final String phoneNumberOfDeletedClient = "+375-44-111-11-12";
        final Client deletedClient = new Client(emailOfDeletedClient, passwordOfDeletedClient,
                nameOfDeletedClient, surnameOfDeletedClient, patronymicOfDeletedClient,
                phoneNumberOfDeletedClient, this.insertedBankAccount);
        deletedClient.setDeleted(true);

        assert this.clientDAO != null;
        this.clientDAO.addEntity(notDeletedClient);
        try
        {
            this.clientDAO.addEntity(deletedClient);
            try
            {
                final Collection<Client> offloadedClients = this.clientDAO.offloadEntities();
                final boolean testSuccess = offloadedClients != null && !offloadedClients.isEmpty()
                        && offloadedClients.contains(notDeletedClient) && offloadedClients.contains(deletedClient);
                Assert.assertTrue(testSuccess);
            }
            finally
            {
                this.clientDAO.deleteEntity(deletedClient);
            }
        }
        finally
        {
            this.clientDAO.deleteEntity(notDeletedClient);
        }
    }

    @Test(dependsOnMethods = {"clientShouldBeAdded"})
    public final void clientShouldBeFoundById()
            throws AddingEntityException, FindingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final String name = "vlad";
        final String surname = "zuev";
        final String patronymic = "sergeevich";
        final String phoneNumber = "+375-44-111-11-11";
        final Client client = new Client(email, password, name, surname, patronymic, phoneNumber,
                this.insertedBankAccount);
        client.setDeleted(true);

        assert this.clientDAO != null;
        this.clientDAO.addEntity(client);
        try
        {
            final Optional<Client> optionalOfClientFoundInDataBase = this.clientDAO.findEntityById(client.getId());
            if(optionalOfClientFoundInDataBase.isEmpty())
            {
                Assert.fail();
            }
            final Client clientFoundInDataBase = optionalOfClientFoundInDataBase.get();
            Assert.assertEquals(client, clientFoundInDataBase);
        }
        finally
        {
            this.clientDAO.deleteEntity(client);
        }
    }

    @Test
    public final void clientShouldNotBeFoundByNotValidId()
            throws FindingEntityException
    {
        final long idOfFoundClient = -1;
        assert this.clientDAO != null;
        final Optional<Client> optionalOfFoundClient = this.clientDAO.findEntityById(idOfFoundClient);
        Assert.assertTrue(optionalOfFoundClient.isEmpty());
    }

    @Test(dependsOnMethods = {"clientShouldBeAdded"})
    public final void notDeletedClientsShouldBeFound()
            throws AddingEntityException, OffloadingEntitiesException, DeletingEntityException
    {
        final String emailOfNotDeletedClient = "first_not_existing_email@mail.ru";
        final String passwordOfNotDeletedClient = "password";
        final String nameOfNotDeletedClient = "vlad";
        final String surnameOfNotDeletedClient = "zuev";
        final String patronymicOfNotDeletedClient = "sergeevich";
        final String phoneNumberOfNotDeletedClient = "+375-44-111-11-11";
        final Client notDeletedClient = new Client(emailOfNotDeletedClient, passwordOfNotDeletedClient,
                nameOfNotDeletedClient, surnameOfNotDeletedClient, patronymicOfNotDeletedClient,
                phoneNumberOfNotDeletedClient, this.insertedBankAccount);
        notDeletedClient.setDeleted(false);

        final String emailOfDeletedClient = "first_existing_email@mail.ru";
        final String passwordOfDeletedClient = "password";
        final String nameOfDeletedClient = "vlad";
        final String surnameOfDeletedClient = "zuev";
        final String patronymicOfDeletedClient = "sergeevich";
        final String phoneNumberOfDeletedClient = "+375-44-222-22-22";
        final Client deletedClient = new Client(emailOfDeletedClient, passwordOfDeletedClient, nameOfDeletedClient,
                surnameOfDeletedClient, patronymicOfDeletedClient, phoneNumberOfDeletedClient,
                this.insertedBankAccount);
        deletedClient.setDeleted(true);

        assert this.clientDAO != null;
        this.clientDAO.addEntity(notDeletedClient);
        try
        {
            this.clientDAO.addEntity(deletedClient);
            try
            {
                final Collection<Client> notDeletedClients = this.clientDAO.findNotDeletedEntities();
                final boolean testSuccess = notDeletedClients != null && !notDeletedClients.isEmpty()
                        && notDeletedClients.contains(notDeletedClient) && !notDeletedClients.contains(deletedClient);
                Assert.assertTrue(testSuccess);
            }
            finally
            {
                this.clientDAO.deleteEntity(deletedClient);
            }
        }
        finally
        {
            this.clientDAO.deleteEntity(notDeletedClient);
        }
    }

    @Test(dependsOnMethods = {"clientShouldBeAdded"})
    public final void deletedClientsShouldBeFound()
            throws AddingEntityException, OffloadingEntitiesException, DeletingEntityException
    {
        final String emailOfNotDeletedClient = "first_not_existing_email@mail.ru";
        final String passwordOfNotDeletedClient = "password";
        final String nameOfNotDeletedClient = "vlad";
        final String surnameOfNotDeletedClient = "zuev";
        final String patronymicOfNotDeletedClient = "sergeevich";
        final String phoneNumberOfNotDeletedClient = "+375-44-111-11-11";
        final Client notDeletedClient = new Client(emailOfNotDeletedClient, passwordOfNotDeletedClient,
                nameOfNotDeletedClient, surnameOfNotDeletedClient, patronymicOfNotDeletedClient,
                phoneNumberOfNotDeletedClient, this.insertedBankAccount);
        notDeletedClient.setDeleted(false);

        final String emailOfDeletedClient = "first_existing_email@mail.ru";
        final String passwordOfDeletedClient = "password";
        final String nameOfDeletedClient = "vlad";
        final String surnameOfDeletedClient = "zuev";
        final String patronymicOfDeletedClient = "sergeevich";
        final String phoneNumberOfDeletedClient = "+375-44-222-22-22";
        final Client deletedClient = new Client(emailOfDeletedClient, passwordOfDeletedClient, nameOfDeletedClient,
                surnameOfDeletedClient, patronymicOfDeletedClient, phoneNumberOfDeletedClient,
                this.insertedBankAccount);
        deletedClient.setDeleted(true);

        assert this.clientDAO != null;
        this.clientDAO.addEntity(notDeletedClient);
        try
        {
            this.clientDAO.addEntity(deletedClient);
            try
            {
                final Collection<Client> deletedClients = this.clientDAO.findDeletedEntities();
                final boolean testSuccess = deletedClients != null && !deletedClients.isEmpty()
                        && deletedClients.contains(deletedClient) && !deletedClients.contains(notDeletedClient);
                Assert.assertTrue(testSuccess);
            }
            finally
            {
                this.clientDAO.deleteEntity(deletedClient);
            }
        }
        finally
        {
            this.clientDAO.deleteEntity(notDeletedClient);
        }
    }

    @Test(dependsOnMethods = {"clientShouldBeAdded", "clientShouldBeFoundById", "clientShouldBeDeleted"})
    public final void clientShouldBeUpdated()
            throws AddingEntityException, UpdatingEntityException, FindingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final String name = "vlad";
        final String surname = "zuev";
        final String patronymic = "sergeevich";
        final String phoneNumber = "+375-44-111-11-11";
        final Client client = new Client(email, password, name, surname, patronymic, phoneNumber,
                this.insertedBankAccount);
        client.setDeleted(true);

        assert this.clientDAO != null;
        this.clientDAO.addEntity(client);
        try
        {
            final String newPhoneNumber = "+375-44-222-22-22";
            client.setPhoneNumber(newPhoneNumber);
            this.clientDAO.updateEntity(client);

            final Optional<Client> optionalOfUpdatedClientFromDataBase = this.clientDAO.findEntityById(
                    client.getId());
            if(optionalOfUpdatedClientFromDataBase.isEmpty())
            {
                Assert.fail();
            }

            final Client updatedClientFromDataBase = optionalOfUpdatedClientFromDataBase.get();
            Assert.assertEquals(client, updatedClientFromDataBase);
        }
        finally
        {
            this.clientDAO.deleteEntity(client);
        }
    }

    //Test for constraint 'name_should_be_correct'
    @Test(dependsOnMethods = {"clientShouldBeAdded"}, expectedExceptions = {UpdatingEntityException.class})
    public final void clientShouldNotBeUpdatedByNotValidName()
            throws AddingEntityException, UpdatingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final String name = "vlad";
        final String surname = "zuev";
        final String patronymic = "sergeevich";
        final String phoneNumber = "+375-44-111-11-11";
        final Client client = new Client(email, password, name, surname, patronymic, phoneNumber,
                this.insertedBankAccount);
        client.setDeleted(true);

        assert this.clientDAO != null;
        this.clientDAO.addEntity(client);
        try
        {
            final String newName = "123";
            client.setName(newName);
            this.clientDAO.updateEntity(client);
        }
        finally
        {
            this.clientDAO.deleteEntity(client);
        }
    }

    //Test for constraint 'surname_should_be_correct'
    @Test(dependsOnMethods = {"clientShouldBeAdded"}, expectedExceptions = UpdatingEntityException.class)
    public final void clientShouldNotBeUpdatedByNotValidSurname()
            throws AddingEntityException, UpdatingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final String name = "vlad";
        final String surname = "zuev";
        final String patronymic = "sergeevich";
        final String phoneNumber = "+375-44-111-11-11";
        final Client client = new Client(email, password, name, surname, patronymic, phoneNumber,
                this.insertedBankAccount);
        client.setDeleted(true);

        assert this.clientDAO != null;
        this.clientDAO.addEntity(client);
        try
        {
            final String newSurname = "123";
            client.setSurname(newSurname);
            this.clientDAO.updateEntity(client);
        }
        finally
        {
            this.clientDAO.deleteEntity(client);
        }
    }

    //Test for constraint 'patronymic_should_be_correct'
    @Test(dependsOnMethods = {"clientShouldBeAdded"}, expectedExceptions = {UpdatingEntityException.class})
    public final void clientShouldNotBeUpdatedByNotValidPatronymic()
            throws AddingEntityException, UpdatingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final String name = "vlad";
        final String surname = "zuev";
        final String patronymic = "sergeevich";
        final String phoneNumber = "+375-44-111-11-11";
        final Client client = new Client(email, password, name, surname, patronymic, phoneNumber,
                this.insertedBankAccount);
        client.setDeleted(true);

        assert this.clientDAO != null;
        this.clientDAO.addEntity(client);
        try
        {
            final String newPatronymic = "123";
            client.setPatronymic(newPatronymic);
            this.clientDAO.updateEntity(client);
        }
        finally
        {
            this.clientDAO.deleteEntity(client);
        }
    }

    //Test for constraint 'phone_number_should_be_correct'
    @Test(dependsOnMethods = {"clientShouldBeAdded"}, expectedExceptions = {UpdatingEntityException.class})
    public final void clientShouldNotBeUpdatedByNotValidPhoneNumber()
            throws AddingEntityException, UpdatingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final String name = "vlad";
        final String surname = "zuev";
        final String patronymic = "sergeevich";
        final String phoneNumber = "+375-44-111-11-11";
        final Client client = new Client(email, password, name, surname, patronymic, phoneNumber,
                this.insertedBankAccount);
        client.setDeleted(true);

        assert this.clientDAO != null;
        this.clientDAO.addEntity(client);
        try
        {
            final String newPhoneNumber = "+375-44-11111-11";
            client.setPhoneNumber(newPhoneNumber);
            this.clientDAO.updateEntity(client);
        }
        finally
        {
            this.clientDAO.deleteEntity(client);
        }
    }

    //Test for function 'update_client'
    @Test(dependsOnMethods = {"clientShouldBeAdded"}, expectedExceptions = UpdatingEntityException.class)
    public final void notDeletedClientShouldNotBeUpdatedByPhoneNumberOfAnotherNotDeletedClient()
            throws AddingEntityException, UpdatingEntityException, DeletingEntityException
    {
        final String emailOfFirstClient = "first_not_existing_email@mail.ru";
        final String passwordOfFirstClient = "password";
        final String nameOfFirstClient = "vlad";
        final String surnameOfFirstClient = "zuev";
        final String patronymicOfFirstClient = "sergeevich";
        final String phoneNumberOfFirstClient = "+375-44-111-11-11";
        final Client firstClient = new Client(emailOfFirstClient, passwordOfFirstClient, nameOfFirstClient,
                surnameOfFirstClient, patronymicOfFirstClient, phoneNumberOfFirstClient, this.insertedBankAccount);
        firstClient.setDeleted(false);

        final String emailOfSecondClient = "second_not_existing_email@mail.ru";
        final String passwordOfSecondClient = "password";
        final String nameOfSecondClient = "vlad";
        final String surnameOfSecondClient = "zuev";
        final String patronymicOfSecondClient = "sergeevich";
        final String phoneNumberOfSecondClient = "+375-44-222-22-22";
        final Client secondClient = new Client(emailOfSecondClient, passwordOfSecondClient, nameOfSecondClient,
                surnameOfSecondClient, patronymicOfSecondClient, phoneNumberOfSecondClient, this.insertedBankAccount);
        secondClient.setDeleted(false);

        assert this.clientDAO != null;
        this.clientDAO.addEntity(firstClient);
        try
        {
            this.clientDAO.addEntity(secondClient);
            try
            {
                firstClient.setPhoneNumber(secondClient.getPhoneNumber());
                this.clientDAO.updateEntity(firstClient);
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

    //Test for trigger 'check_unique_email_not_deleted_users_before_updating'
    @Test(dependsOnMethods = {"clientShouldBeAdded"}, expectedExceptions = {UpdatingEntityException.class})
    public final void notDeletedClientShouldNotBeUpdatedByEmailOfNotDeletedAdministrator()
            throws AddingEntityException, UpdatingEntityException, DeletingEntityException
    {
        final String emailOfClient = "first_not_existing_email@mail.ru";
        final String passwordOfClient = "password";
        final String nameOfClient = "vlad";
        final String surnameOfClient = "zuev";
        final String patronymicOfClient = "sergeevich";
        final String phoneNumberOfClient = "+375-44-111-11-11";
        final Client client = new Client(emailOfClient, passwordOfClient, nameOfClient, surnameOfClient,
                patronymicOfClient, phoneNumberOfClient, this.insertedBankAccount);
        client.setDeleted(false);

        final String emailOfAdministrator = "second_not_existing_email@mail.ru";
        final String passwordOfAdministrator = "password";
        final Administrator.Level levelOfAdministrator = Administrator.Level.MAIN;
        final Administrator administrator = new Administrator(emailOfAdministrator, passwordOfAdministrator,
                levelOfAdministrator);
        administrator.setDeleted(false);

        assert this.clientDAO != null;
        this.clientDAO.addEntity(client);
        try
        {
            assert this.administratorDAO != null;
            this.administratorDAO.addEntity(administrator);
            try
            {
                client.setEmail(administrator.getEmail());
                this.clientDAO.updateEntity(client);
            }
            finally
            {
                this.administratorDAO.deleteEntity(administrator);
            }
        }
        finally
        {
            this.clientDAO.deleteEntity(client);
        }
    }

    @Test(dependsOnMethods = {"clientShouldBeAdded", "clientShouldBeFoundById", "clientShouldBeDeleted"})
    public final void deletedStatusOfClientWithGivenIdShouldBeUpdated()
            throws AddingEntityException, UpdatingEntityException, FindingEntityException, DeletingEntityException
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

        assert this.clientDAO != null;
        this.clientDAO.addEntity(client);
        try
        {
            final boolean newDeleted = !client.isDeleted();
            this.clientDAO.updateDeletedStatusOfEntity(client.getId(), newDeleted);

            final Optional<Client> optionalOfUpdatedClient = this.clientDAO.findEntityById(client.getId());
            if(optionalOfUpdatedClient.isEmpty())
            {
                Assert.fail();
            }
            final Client updatedClient = optionalOfUpdatedClient.get();

            Assert.assertEquals(updatedClient.isDeleted(), newDeleted);
        }
        finally
        {
            this.clientDAO.deleteEntity(client);
        }
    }

    @Test(dependsOnMethods = {"clientShouldBeAdded", "clientShouldBeFoundById", "clientShouldBeDeleted"})
    public final void deletedStatusOfClientShouldBeUpdated()
            throws AddingEntityException, UpdatingEntityException, FindingEntityException, DeletingEntityException
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

        assert this.clientDAO != null;
        this.clientDAO.addEntity(client);
        try
        {
            final boolean newDeleted = !client.isDeleted();
            this.clientDAO.updateDeletedStatusOfEntity(client, newDeleted);

            final Optional<Client> optionalOfUpdatedClient = this.clientDAO.findEntityById(client.getId());
            if(optionalOfUpdatedClient.isEmpty())
            {
                Assert.fail();
            }
            final Client updatedClient = optionalOfUpdatedClient.get();

            Assert.assertEquals(updatedClient.isDeleted(), newDeleted);
        }
        finally
        {
            this.clientDAO.deleteEntity(client);
        }
    }

    //Test for function 'update_deleted_status_of_client'
    @Test(dependsOnMethods = {"clientShouldBeAdded"}, expectedExceptions = UpdatingEntityException.class)
    public final void deletedStatusOfDeletedClientShouldNotBeUpdatedBecauseOfExistingNotDeletedClientWithSamePhoneNumber()
            throws AddingEntityException, UpdatingEntityException, DeletingEntityException
    {
        final String emailOfNotDeletedClient = "first_not_existing_email@mail.ru";
        final String passwordOfNotDeletedClient = "password";
        final String nameOfNotDeletedClient = "Vlad";
        final String surnameOfNotDeletedClient = "Zuev";
        final String patronymicOfNotDeletedClient = "Sergeevich";
        final String phoneNumberOfNotDeletedClient = "+375-44-111-11-11";
        final Client notDeletedClient = new Client(emailOfNotDeletedClient, passwordOfNotDeletedClient,
                nameOfNotDeletedClient, surnameOfNotDeletedClient, patronymicOfNotDeletedClient,
                phoneNumberOfNotDeletedClient, this.insertedBankAccount);
        notDeletedClient.setDeleted(false);

        final String emailOfDeletedClient = "second_existing_email@mail.ru";
        final String passwordOfDeletedClient = "password";
        final String nameOfDeletedClient = "Vlad";
        final String surnameOfDeletedClient = "Zuev";
        final String patronymicOfDeletedClient = "Sergeevich";
        final String phoneNumberOfDeletedClient = "+375-44-111-11-11";
        final Client deletedClient = new Client(emailOfDeletedClient, passwordOfDeletedClient, nameOfDeletedClient,
                surnameOfDeletedClient, patronymicOfDeletedClient, phoneNumberOfDeletedClient,
                this.insertedBankAccount);
        deletedClient.setDeleted(true);

        assert this.clientDAO != null;
        this.clientDAO.addEntity(notDeletedClient);
        try
        {
            this.clientDAO.addEntity(deletedClient);
            try
            {
                this.clientDAO.updateDeletedStatusOfEntity(deletedClient, false);
            }
            finally
            {
                this.clientDAO.deleteEntity(deletedClient);
            }
        }
        finally
        {
            this.clientDAO.deleteEntity(notDeletedClient);
        }
    }

    //Test for trigger 'check_unique_email_not_deleted_users_before_updating'
    @Test(dependsOnMethods = {"clientShouldBeAdded"}, expectedExceptions = {UpdatingEntityException.class})
    public final void deletedStatusOfClientShouldNotBeUpdatedBecauseOfExistingNotDeletedAdministratorWithSameEmail()
            throws AddingEntityException, UpdatingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";

        final String passwordOfAdministrator = "password";
        final Administrator.Level levelOfAdministrator = Administrator.Level.MAIN;
        final Administrator administrator = new Administrator(email, passwordOfAdministrator, levelOfAdministrator);
        administrator.setDeleted(false);

        final String passwordOfClient = "password";
        final String nameOfClient = "vlad";
        final String surnameOfClient = "zuev";
        final String patronymicOfClient = "sergeevich";
        final String phoneNumberOfClient = "+375-44-111-11-11";
        final Client client = new Client(email, passwordOfClient, nameOfClient, surnameOfClient, patronymicOfClient,
                phoneNumberOfClient, this.insertedBankAccount);
        client.setDeleted(true);

        assert this.administratorDAO != null;
        this.administratorDAO.addEntity(administrator);
        try
        {
            assert this.clientDAO != null;
            this.clientDAO.addEntity(client);
            try
            {
                final boolean newDeleted = false;
                this.clientDAO.updateDeletedStatusOfEntity(client.getId(), newDeleted);
            }
            finally
            {
                this.clientDAO.addEntity(client);
            }
        }
        finally
        {
            this.administratorDAO.deleteEntity(administrator);
        }
    }

    @Test(dependsOnMethods = {"clientShouldBeAdded", "clientShouldBeFoundById"})
    public final void clientShouldBeDeletedById()
            throws AddingEntityException, DeletingEntityException, FindingEntityException

    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final String name = "vlad";
        final String surname = "zuev";
        final String patronymic = "sergeevich";
        final String phoneNumber = "+375-44-111-11-11";
        final Client client = new Client(email, password, name, surname, patronymic, phoneNumber,
                this.insertedBankAccount);
        client.setDeleted(true);

        assert this.clientDAO != null;
        this.clientDAO.addEntity(client);

        this.clientDAO.deleteEntity(client.getId());

        final Optional<Client> optionalOfFoundClient = this.clientDAO.findEntityById(client.getId());
        Assert.assertFalse(optionalOfFoundClient.isPresent());
    }

    @Test
    public final void clientShouldNotBeDeletedByNotExistingId()
            throws OffloadingEntitiesException, DeletingEntityException
    {
        final long idOfDeletedClient = -1;
        assert this.clientDAO != null;
        final Collection<Client> clientsBeforeDeleting = this.clientDAO.offloadEntities();
        this.clientDAO.deleteEntity(idOfDeletedClient);
        final Collection<Client> clientsAfterDeleting = this.clientDAO.offloadEntities();
        Assert.assertEquals(clientsBeforeDeleting, clientsAfterDeleting);
    }

    @Test(dependsOnMethods = {"clientShouldBeAdded", "clientShouldBeFoundById"})
    public final void clientShouldBeDeleted()
            throws AddingEntityException, DeletingEntityException, FindingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final String name = "vlad";
        final String surname = "zuev";
        final String patronymic = "sergeevich";
        final String phoneNumber = "+375-44-111-11-11";
        final Client client = new Client(email, password, name, surname, patronymic, phoneNumber,
                this.insertedBankAccount);
        client.setDeleted(true);

        assert this.clientDAO != null;
        this.clientDAO.addEntity(client);
        this.clientDAO.deleteEntity(client);

        final Optional<Client> optionalOfFoundClient = this.clientDAO.findEntityById(client.getId());
        Assert.assertTrue(optionalOfFoundClient.isEmpty());
    }

    @Test
    public final void clientWithNotExistingIdShouldNotBeDeleted()
            throws OffloadingEntitiesException, DeletingEntityException
    {
        final long id = -1;
        final Client client = new Client();
        client.setId(id);
        assert this.clientDAO != null;
        final Collection<Client> clientsBeforeDeleting = this.clientDAO.offloadEntities();
        this.clientDAO.deleteEntity(client);
        final Collection<Client> clientsAfterDeleting = this.clientDAO.offloadEntities();
        Assert.assertEquals(clientsBeforeDeleting, clientsAfterDeleting);
    }

    @Test(dependsOnMethods = {"clientShouldBeAdded"})
    public final void clientWithGivenIdShouldBeExisted()
            throws AddingEntityException, DefiningExistingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final String name = "vlad";
        final String surname = "zuev";
        final String patronymic = "sergeevich";
        final String phoneNumber = "+375-44-111-11-11";
        final Client client = new Client(email, password, name, surname, patronymic, phoneNumber,
                this.insertedBankAccount);
        client.setDeleted(true);

        assert this.clientDAO != null;
        this.clientDAO.addEntity(client);
        try
        {
            final boolean clientExists = this.clientDAO.isEntityWithGivenIdExisting(client.getId());
            Assert.assertTrue(clientExists);
        }
        finally
        {
            this.clientDAO.deleteEntity(client);
        }
    }

    @Test
    public final void clientWithGivenNotValidIdShouldNotBeExisted()
            throws DefiningExistingEntityException
    {
        final long idOfResearchClient = -1;
        assert this.clientDAO != null;
        final boolean clientExists = this.clientDAO.isEntityWithGivenIdExisting(idOfResearchClient);
        Assert.assertFalse(clientExists);
    }

    @Test(dependsOnMethods = {"clientShouldBeAdded"})
    public final void clientShouldBeFoundByEmail()
            throws AddingEntityException, FindingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final String name = "vlad";
        final String surname = "zuev";
        final String patronymic = "sergeevich";
        final String phoneNumber = "+375-44-111-11-11";
        final Client client = new Client(email, password, name, surname, patronymic, phoneNumber,
                this.insertedBankAccount);

        assert this.clientDAO != null;
        this.clientDAO.addEntity(client);
        try
        {
            final Optional<Client> optionalOfFoundClient = this.clientDAO.findNotDeletedUserByEmail(client.getEmail());
            if(optionalOfFoundClient.isEmpty())
            {
                Assert.fail();
            }
            final Client foundClient = optionalOfFoundClient.get();

            Assert.assertEquals(client, foundClient);
        }
        finally
        {
            this.clientDAO.deleteEntity(client);
        }
    }

    @Test
    public final void clientShouldNotBeFoundByNotValidEmail()
            throws FindingEntityException
    {
        final String email = "not_valid_email";
        assert this.clientDAO != null;
        final Optional<Client> optionalOfFoundClient = this.clientDAO.findNotDeletedUserByEmail(email);
        Assert.assertTrue(optionalOfFoundClient.isEmpty());
    }

    @Test(dependsOnMethods = {"clientShouldBeAdded"})
    public final void notDeletedClientsShouldBeFoundByPassword()
            throws AddingEntityException, OffloadingEntitiesException, DeletingEntityException
    {
        final String emailOfFirstClient = "first_not_existing_email@mail.ru";
        final String passwordOfFirstClient = "first_password";
        final String nameOfFirstClient = "vlad";
        final String surnameOfFirstClient = "zuev";
        final String patronymicOfFirstClient = "sergeevich";
        final String phoneNumberOfFirstClient = "+375-44-111-11-11";
        final Client firstClient = new Client(emailOfFirstClient, passwordOfFirstClient, nameOfFirstClient,
                surnameOfFirstClient, patronymicOfFirstClient, phoneNumberOfFirstClient, this.insertedBankAccount);

        final String emailOfSecondClient = "second_not_existing_email@mail.ru";
        final String passwordOfSecondClient = "second_password";
        final String nameOfSecondClient = "vlad";
        final String surnameOfSecondClient = "zuev";
        final String patronymicOfSecondClient = "sergeevich";
        final String phoneNumberOfSecondClient = "+375-44-111-11-12";
        final Client secondClient = new Client(emailOfSecondClient, passwordOfSecondClient, nameOfSecondClient,
                surnameOfSecondClient, patronymicOfSecondClient, phoneNumberOfSecondClient, this.insertedBankAccount);

        assert this.clientDAO != null;
        this.clientDAO.addEntity(firstClient);
        try
        {
            this.clientDAO.addEntity(secondClient);
            try
            {
                final Collection<Client> foundClients = this.clientDAO.findNotDeletedUsersByPassword(
                        passwordOfFirstClient);
                final boolean testSuccess = foundClients != null && !foundClients.isEmpty()
                        && foundClients.contains(firstClient) && !foundClients.contains(secondClient);
                Assert.assertTrue(testSuccess);
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

    @Test(dependsOnMethods = {"clientShouldBeAdded"})
    public final void clientWithGivenEmailShouldExist()
            throws AddingEntityException, DefiningExistingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final String name = "vlad";
        final String surname = "zuev";
        final String patronymic = "sergeevich";
        final String phoneNumber = "+375-44-111-11-11";
        final Client client = new Client(email, password, name, surname, patronymic, phoneNumber,
                this.insertedBankAccount);

        assert this.clientDAO != null;
        this.clientDAO.addEntity(client);
        try
        {
            final boolean clientExists = this.clientDAO.isNotDeletedUserWithGivenEmailExist(client.getEmail());
            Assert.assertTrue(clientExists);
        }
        finally
        {
            this.clientDAO.deleteEntity(client);
        }
    }

    @Test
    public final void clientWithNotValidEmailShouldNotExist()
            throws DefiningExistingEntityException
    {
        final String email = "not_valid_email";
        assert this.clientDAO != null;
        final boolean clientExists = this.clientDAO.isNotDeletedUserWithGivenEmailExist(email);
        Assert.assertFalse(clientExists);
    }

    @Test(dependsOnMethods = {"clientShouldBeAdded"})
    public final void passwordShouldBeCorrectForGivenEmail()
            throws AddingEntityException, DefiningExistingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final String name = "vlad";
        final String surname = "zuev";
        final String patronymic = "sergeevich";
        final String phoneNumber = "+375-44-111-11-11";
        final Client client = new Client(email, password, name, surname, patronymic, phoneNumber,
                this.insertedBankAccount);

        assert this.clientDAO != null;
        this.clientDAO.addEntity(client);
        try
        {
            final boolean passwordIsCorrect = this.clientDAO.isCorrectPasswordForGivenEmail(client.getPassword(),
                    client.getEmail());
            Assert.assertTrue(passwordIsCorrect);
        }
        finally
        {
            this.clientDAO.deleteEntity(client);
        }
    }

    @Test(dependsOnMethods = {"clientShouldBeAdded"})
    public final void passwordShouldNotBeCorrectForGivenEmail()
            throws AddingEntityException, DefiningExistingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final String name = "vlad";
        final String surname = "zuev";
        final String patronymic = "sergeevich";
        final String phoneNumber = "+375-44-111-11-11";
        final Client client = new Client(email, password, name, surname, patronymic, phoneNumber,
                this.insertedBankAccount);

        assert this.clientDAO != null;
        this.clientDAO.addEntity(client);
        try
        {
            final String researchPassword = "second_password";
            final boolean passwordIsCorrect = this.clientDAO.isCorrectPasswordForGivenEmail(researchPassword,
                    client.getEmail());
            Assert.assertFalse(passwordIsCorrect);
        }
        finally
        {
            this.clientDAO.deleteEntity(client);
        }
    }

    @Test(dependsOnMethods = {"clientShouldBeAdded"})
    public final void clientsShouldBeFoundBeFoundByEmail()
            throws AddingEntityException, OffloadingEntitiesException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";

        final String passwordOfNotDeletedClient = "password";
        final String nameOfNotDeletedClient = "Vlad";
        final String surnameOfNotDeletedClient = "Zuev";
        final String patronymicOfNotDeletedClient = "Sergeevich";
        final String phoneNumberOfNotDeletedClient = "+375-44-111-11-11";
        final Client notDeletedClient = new Client(email, passwordOfNotDeletedClient,
                nameOfNotDeletedClient, surnameOfNotDeletedClient, patronymicOfNotDeletedClient,
                phoneNumberOfNotDeletedClient, this.insertedBankAccount);
        notDeletedClient.setDeleted(false);

        final String passwordOfDeletedClient = "password";
        final String nameOfDeletedClient = "Vlad";
        final String surnameOfDeletedClient = "Zuev";
        final String patronymicOfDeletedClient = "Sergeevich";
        final String phoneNumberOfDeletedClient = "+375-44-111-11-11";
        final Client deletedClient = new Client(email, passwordOfDeletedClient, nameOfDeletedClient,
                surnameOfDeletedClient, patronymicOfDeletedClient, phoneNumberOfDeletedClient,
                this.insertedBankAccount);
        deletedClient.setDeleted(true);

        assert this.clientDAO != null;
        this.clientDAO.addEntity(notDeletedClient);
        try
        {
            this.clientDAO.addEntity(deletedClient);
            try
            {
                final Collection<Client> foundClients = this.clientDAO.findUsersByEmail(email);
                final boolean testSuccess = foundClients != null && !foundClients.isEmpty()
                        && foundClients.contains(notDeletedClient) && foundClients.contains(deletedClient);
                Assert.assertTrue(testSuccess);
            }
            finally
            {
                this.clientDAO.deleteEntity(deletedClient);
            }
        }
        finally
        {
            this.clientDAO.deleteEntity(notDeletedClient);
        }
    }

    @Test
    public final void clientsShouldNotBeFoundBeNotValidEmail()
            throws OffloadingEntitiesException
    {
        final String email = "not_valid_email";
        assert this.clientDAO != null;
        final Collection<Client> foundClients = this.clientDAO.findUsersByEmail(email);
        Assert.assertTrue(foundClients.isEmpty());
    }

    @Test(dependsOnMethods = {"clientShouldBeAdded"})
    public final void notDeletedClientWithGivenPhoneNumberShouldExist()
            throws AddingEntityException, DefiningExistingEntityException, DeletingEntityException
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

        assert this.clientDAO != null;
        this.clientDAO.addEntity(client);
        try
        {
            final boolean clientExists = this.clientDAO.isNotDeletedClientWithGivenPhoneNumberExist(
                    client.getPhoneNumber());
            Assert.assertTrue(clientExists);
        }
        finally
        {
            this.clientDAO.deleteEntity(client);
        }
    }

    @Test
    public final void notDeletedClientWithGivenNotValidPhoneNumberShouldNotExist()
            throws DefiningExistingEntityException
    {
        final String phoneNumber = "not_valid_phone_number";
        assert this.clientDAO != null;
        final boolean clientExists = this.clientDAO.isNotDeletedClientWithGivenPhoneNumberExist(phoneNumber);
        Assert.assertFalse(clientExists);
    }

    @Test(dependsOnMethods = {"clientShouldBeAdded"})
    public final void notDeletedClientShouldBeFoundByGivenPhoneNumber()
            throws AddingEntityException, FindingEntityException, DeletingEntityException
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

        assert this.clientDAO != null;
        this.clientDAO.addEntity(client);
        try
        {
            final Optional<Client> optionalOfFoundClient = this.clientDAO.findOptionalOfNotDeletedClientByPhoneNumber(
                    client.getPhoneNumber());
            if(optionalOfFoundClient.isEmpty())
            {
                Assert.fail();
            }
            final Client foundClient = optionalOfFoundClient.get();
            Assert.assertEquals(client, foundClient);
        }
        finally
        {
            this.clientDAO.deleteEntity(client);
        }
    }

    @Test
    public final void notDeletedClientShouldBeFoundByGivenNotValidPhoneNumber()
            throws FindingEntityException
    {
        final String phoneNumber = "not valid phone number";
        assert this.clientDAO != null;
        final Optional<Client> optionalOfFoundClient = this.clientDAO.findOptionalOfNotDeletedClientByPhoneNumber(
                phoneNumber);
        Assert.assertTrue(optionalOfFoundClient.isEmpty());
    }
}
