package by.itacademy.zuevvlad.cardpaymentproject.dao.user;

import by.itacademy.zuevvlad.cardpaymentproject.configuration.DAOConfiguration;
import by.itacademy.zuevvlad.cardpaymentproject.configuration.WebMainConfiguration;
import by.itacademy.zuevvlad.cardpaymentproject.dao.BankAccountDAO;
import by.itacademy.zuevvlad.cardpaymentproject.dao.PaymentCardDAO;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.*;
import by.itacademy.zuevvlad.cardpaymentproject.entity.BankAccount;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Client;
import by.itacademy.zuevvlad.cardpaymentproject.entity.PaymentCard;
import by.itacademy.zuevvlad.cardpaymentproject.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Collection;
import java.util.Optional;

@Test
@ContextConfiguration(classes = {WebMainConfiguration.class})
public final class UserDAOTest extends AbstractTestNGSpringContextTests
{
    @Autowired
    @Qualifier(value = DAOConfiguration.NAME_OF_BEAN_OF_USER_DAO)
    private final UserDAO userDAO;

    @Autowired
    @Qualifier(value = DAOConfiguration.NAME_OF_BEAN_OF_BANK_ACCOUNT_DAO)
    private final BankAccountDAO bankAccountDAO;

    @Autowired
    @Qualifier(value = DAOConfiguration.NAME_OF_BEAN_OF_CLIENT_DAO)
    private final ClientDAO clientDAO;

    @Autowired
    @Qualifier(value = DAOConfiguration.NAME_OF_BEAN_OF_PAYMENT_CARD_DAO)
    private final PaymentCardDAO paymentCardDAO;

    public UserDAOTest()
    {
        super();
        this.userDAO = null;
        this.bankAccountDAO = null;
        this.clientDAO = null;
        this.paymentCardDAO = null;
    }

    //Test for constraint 'user_email_should_be_valid'
    @Test
    public final void userShouldBeAdded()
            throws AddingEntityException, FindingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final User addedUser = new User(email, password);
        addedUser.setDeleted(true);

        final long idOfAddedUserBeforeAdding = addedUser.getId();
        assert this.userDAO != null;
        this.userDAO.addEntity(addedUser);
        try
        {
            final long idOfAddedUserAfterAdding = addedUser.getId();

            final Optional<User> optionalOfAddedUserFromDataBase
                    = this.userDAO.findEntityById(idOfAddedUserAfterAdding);
            if(optionalOfAddedUserFromDataBase.isEmpty())
            {
                Assert.fail();
            }
            final User addedUserFromDataBase = optionalOfAddedUserFromDataBase.get();

            final boolean testSuccess = addedUser.equals(addedUserFromDataBase)
                    && idOfAddedUserBeforeAdding != idOfAddedUserAfterAdding;
            Assert.assertTrue(testSuccess);
        }
        finally
        {
            this.userDAO.deleteEntity(addedUser);
        }
    }

    @Test(expectedExceptions = {AddingEntityException.class})
    public final void userWithNotValidEmailShouldNotBeAdded()
            throws AddingEntityException, DeletingEntityException
    {
        final String email = "not_valid_email";
        final String password = "password";
        final User addedUser = new User(email, password);

        assert this.userDAO != null;
        this.userDAO.addEntity(addedUser);
        try
        {
            Assert.fail();
        }
        finally
        {
            this.userDAO.deleteEntity(addedUser);
        }
    }

    //Test for trigger 'check_unique_email_not_deleted_users_before_adding'
    @Test
    public final void twoUsersWithSameEmailButDifferentDeletedStatusShouldBeAdded()
            throws AddingEntityException, OffloadingEntitiesException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";

        final User notDeletedUser = new User(email, password);
        notDeletedUser.setDeleted(false);

        final User deletedUser = new User(email, password);
        deletedUser.setDeleted(true);

        assert this.userDAO != null;
        this.userDAO.addEntity(notDeletedUser);
        try
        {
            this.userDAO.addEntity(deletedUser);
            try
            {
                final Collection<User> allUsers = this.userDAO.offloadEntities();
                final boolean testSuccess = allUsers != null && !allUsers.isEmpty() && allUsers.contains(notDeletedUser)
                        && allUsers.contains(deletedUser);
                Assert.assertTrue(testSuccess);
            }
            finally
            {
                this.userDAO.deleteEntity(deletedUser);
            }
        }
        finally
        {
            this.userDAO.deleteEntity(notDeletedUser);
        }
    }

    //Test for trigger 'check_unique_email_not_deleted_users_before_adding'
    @Test(expectedExceptions = {AddingEntityException.class})
    public final void twoUsersWithNotDeletedStatusAndSameEmailShouldNotBeAdded()
            throws AddingEntityException, DeletingEntityException
    {
        final String emailOfFirstUser = "not_existing_email@mail.ru";
        final String passwordOfFirstUser = "password";
        final User firstUser = new User(emailOfFirstUser, passwordOfFirstUser);
        firstUser.setDeleted(false);

        final String passwordOfSecondUser = "password";
        final User secondUser = new User(emailOfFirstUser, passwordOfSecondUser);
        secondUser.setDeleted(false);

        try
        {
            assert this.userDAO != null;
            this.userDAO.addEntity(firstUser);
        }
        catch(final AddingEntityException addingEntityException)
        {
            Assert.fail();
        }
        try
        {
            this.userDAO.addEntity(secondUser);
            try
            {
                Assert.fail();
            }
            finally
            {
                this.userDAO.deleteEntity(secondUser);
            }
        }
        finally
        {
            this.userDAO.deleteEntity(firstUser);
        }
    }

    @Test(dependsOnMethods = {"userShouldBeAdded"})
    public final void allUsersShouldBeOffloaded()
            throws AddingEntityException, OffloadingEntitiesException, DeletingEntityException
    {
        final String emailOfNotDeletedUser = "first_not_existing_email@mail.ru";
        final String passwordOfNotDeletedUser = "password";
        final User notDeletedUser = new User(emailOfNotDeletedUser, passwordOfNotDeletedUser);
        notDeletedUser.setDeleted(false);

        final String emailOfDeletedUser = "second_not_existing_email@mail.ru";
        final String passwordOfDeletedUser = "password";
        final User deletedUser = new User(emailOfDeletedUser, passwordOfDeletedUser);
        deletedUser.setDeleted(true);

        assert this.userDAO != null;
        this.userDAO.addEntity(notDeletedUser);
        try
        {
            this.userDAO.addEntity(deletedUser);
            try
            {
                final Collection<User> offloadedUsers = this.userDAO.offloadEntities();
                final boolean testSuccess = offloadedUsers != null && !offloadedUsers.isEmpty()
                        && offloadedUsers.contains(notDeletedUser) && offloadedUsers.contains(deletedUser);
                Assert.assertTrue(testSuccess);
            }
            finally
            {
                this.userDAO.deleteEntity(deletedUser);
            }
        }
        finally
        {
            this.userDAO.deleteEntity(notDeletedUser);
        }
    }

    @Test(dependsOnMethods = {"userShouldBeAdded"})
    public final void userShouldBeFoundById()
            throws AddingEntityException, FindingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password6";
        final User user = new User(email, password);
        user.setDeleted(true);

        assert this.userDAO != null;
        this.userDAO.addEntity(user);
        try
        {
            final Optional<User> optionalOfUserFoundInDataBase = this.userDAO.findEntityById(user.getId());
            if(optionalOfUserFoundInDataBase.isEmpty())
            {
                Assert.fail();
            }
            final User userFoundInDataBase = optionalOfUserFoundInDataBase.get();
            Assert.assertEquals(user, userFoundInDataBase);
        }
        finally
        {
            this.userDAO.deleteEntity(user);
        }
    }

    @Test
    public final void userShouldNotBeFoundByNotValidId()
            throws FindingEntityException
    {
        final long idOfFoundUser = -1;
        assert this.userDAO != null;
        final Optional<User> optionalOfFoundUser = this.userDAO.findEntityById(idOfFoundUser);
        Assert.assertTrue(optionalOfFoundUser.isEmpty());
    }

    @Test(dependsOnMethods = {"userShouldBeAdded"})
    public final void notDeletedUsersShouldBeFound()
            throws AddingEntityException, OffloadingEntitiesException, DeletingEntityException
    {
        final String emailOfDeletedUser = "first_not_existing_email@mail.ru";
        final String passwordOfDeletedUser = "password6";
        final User deletedUser = new User(emailOfDeletedUser, passwordOfDeletedUser);
        deletedUser.setDeleted(true);

        final String emailOfNotDeletedUser = "second_not_existing_email@mail.ru";
        final String passwordOfNotDeletedUser = "password6";
        final User notDeletedUser = new User(emailOfNotDeletedUser, passwordOfNotDeletedUser);
        notDeletedUser.setDeleted(false);

        assert this.userDAO != null;
        this.userDAO.addEntity(deletedUser);
        try
        {
            this.userDAO.addEntity(notDeletedUser);
            try
            {
                final Collection<User> notDeletedUsers = this.userDAO.findNotDeletedEntities();
                final boolean testSuccess = notDeletedUsers != null && !notDeletedUsers.isEmpty()
                        && notDeletedUsers.contains(notDeletedUser) && !notDeletedUsers.contains(deletedUser);
                Assert.assertTrue(testSuccess);
            }
            finally
            {
                this.userDAO.deleteEntity(notDeletedUser);
            }
        }
        finally
        {
            this.userDAO.deleteEntity(deletedUser);
        }
    }

    @Test(dependsOnMethods = {"userShouldBeAdded"})
    public final void deletedUsersShouldBeFound()
            throws AddingEntityException, OffloadingEntitiesException, DeletingEntityException
    {
        final String emailOfDeletedUser = "first_not_existing_email@mail.ru";
        final String passwordOfDeletedUser = "password6";
        final User deletedUser = new User(emailOfDeletedUser, passwordOfDeletedUser);
        deletedUser.setDeleted(true);

        final String emailOfNotDeletedUser = "second_not_existing_email@mail.ru";
        final String passwordOfNotDeletedUser = "password6";
        final User notDeletedUser = new User(emailOfNotDeletedUser, passwordOfNotDeletedUser);
        notDeletedUser.setDeleted(false);

        assert this.userDAO != null;
        this.userDAO.addEntity(deletedUser);
        try
        {
            this.userDAO.addEntity(notDeletedUser);
            try
            {
                final Collection<User> deletedUsers = this.userDAO.findDeletedEntities();
                final boolean testSuccess = deletedUsers != null && !deletedUsers.isEmpty()
                        && deletedUsers.contains(deletedUser) && !deletedUsers.contains(notDeletedUser);
                Assert.assertTrue(testSuccess);
            }
            finally
            {
                this.userDAO.deleteEntity(notDeletedUser);
            }
        }
        finally
        {
            this.userDAO.deleteEntity(deletedUser);
        }
    }

    @Test(dependsOnMethods = {"userShouldBeAdded", "userShouldBeFoundById", "userShouldBeDeleted"})
    public final void userShouldBeUpdated()
            throws AddingEntityException, UpdatingEntityException, FindingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password6";
        final User user = new User(email, password);
        user.setDeleted(true);

        assert this.userDAO != null;
        this.userDAO.addEntity(user);
        try
        {
            final String newEmail = "new_not_existing_email@mail.ru";
            user.setEmail(newEmail);
            this.userDAO.updateEntity(user);

            final Optional<User> optionalOfUpdatedUserFromDataBase = this.userDAO.findEntityById(user.getId());
            if(optionalOfUpdatedUserFromDataBase.isEmpty())
            {
                Assert.fail();
            }

            final User updatedUserFromDataBase = optionalOfUpdatedUserFromDataBase.get();
            Assert.assertEquals(updatedUserFromDataBase, user);
        }
        finally
        {
            this.userDAO.deleteEntity(user);
        }
    }

    //Test for constraint 'user_email_should_be_valid'
    @Test(dependsOnMethods = {"userShouldBeAdded"}, expectedExceptions = {UpdatingEntityException.class})
    public final void userShouldNotBeUpdatedByNotValidEmail()
            throws AddingEntityException, UpdatingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final User user = new User(email, password);

        assert this.userDAO != null;
        this.userDAO.addEntity(user);
        try
        {
            final String newEmail = "new_not_valid_email";
            user.setEmail(newEmail);
            this.userDAO.updateEntity(user);
        }
        finally
        {
            this.userDAO.deleteEntity(user);
        }
    }

    //Test for trigger 'check_unique_email_not_deleted_users_before_updating'
    @Test(dependsOnMethods = {"userShouldBeAdded"}, expectedExceptions = UpdatingEntityException.class)
    public final void notDeletedUserShouldNotBeUpdatedByEmailOfAnotherNotDeletedUser()
            throws AddingEntityException, UpdatingEntityException, DeletingEntityException
    {
        final String emailOfFirstUser = "first_not_existing_email@mail.ru";
        final String passwordOfFirstUser = "password";
        final User firstUser = new User(emailOfFirstUser, passwordOfFirstUser);
        firstUser.setDeleted(false);

        final String emailOfSecondUser = "second_not_existing_email@mail.ru";
        final String passwordOfSecondUser = "password";
        final User secondUser = new User(emailOfSecondUser, passwordOfSecondUser);
        secondUser.setDeleted(false);

        assert this.userDAO != null;
        this.userDAO.addEntity(firstUser);
        try
        {
            this.userDAO.addEntity(secondUser);
            try
            {
                secondUser.setEmail(firstUser.getEmail());
                this.userDAO.updateEntity(secondUser);
            }
            finally
            {
                this.userDAO.deleteEntity(secondUser);
            }
        }
        finally
        {
            this.userDAO.deleteEntity(firstUser);
        }
    }

    @Test(dependsOnMethods = {"userShouldBeAdded", "userShouldBeFoundById", "userShouldBeDeleted"})
    public final void deletedStatusOfUserWithGivenIdShouldBeUpdated()
            throws AddingEntityException, UpdatingEntityException, FindingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final User user = new User(email, password);
        user.setDeleted(false);

        assert this.userDAO != null;
        this.userDAO.addEntity(user);
        try
        {
            final boolean newDeleted = !user.isDeleted();
            this.userDAO.updateDeletedStatusOfEntity(user.getId(), newDeleted);

            final Optional<User> optionalOfUpdatedUser = this.userDAO.findEntityById(user.getId());
            if(optionalOfUpdatedUser.isEmpty())
            {
                Assert.fail();
            }
            final User updatedUser = optionalOfUpdatedUser.get();

            Assert.assertEquals(updatedUser.isDeleted(), newDeleted);
        }
        finally
        {
            this.userDAO.deleteEntity(user);
        }
    }

    @Test(dependsOnMethods = {"userShouldBeAdded", "userShouldBeFoundById", "userShouldBeDeleted"})
    public final void deletedStatusOfUserShouldBeUpdated()
            throws AddingEntityException, UpdatingEntityException, FindingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final User user = new User(email, password);
        user.setDeleted(false);

        assert this.userDAO != null;
        this.userDAO.addEntity(user);
        try
        {
            final boolean newDeleted = !user.isDeleted();
            this.userDAO.updateDeletedStatusOfEntity(user, newDeleted);

            final Optional<User> optionalOfUpdatedUser = this.userDAO.findEntityById(user.getId());
            if(optionalOfUpdatedUser.isEmpty())
            {
                Assert.fail();
            }
            final User updatedUser = optionalOfUpdatedUser.get();

            Assert.assertEquals(updatedUser.isDeleted(), newDeleted);
        }
        finally
        {
            this.userDAO.deleteEntity(user);
        }
    }

    //Test for trigger 'check_unique_email_not_deleted_users_before_updating'
    @Test(dependsOnMethods = {"userShouldBeAdded"}, expectedExceptions = UpdatingEntityException.class)
    public final void deletedStatusOfDeletedUserShouldNotBeUpdatedBecauseOfExistingNotDeletedUserWithSameEmail()
            throws AddingEntityException, UpdatingEntityException, DeletingEntityException
    {
        final String emailOfNotDeletedUser = "not_existing_email@mail.ru";
        final String passwordOfNotDeletedUser = "password";
        final User notDeletedUser = new User(emailOfNotDeletedUser, passwordOfNotDeletedUser);
        notDeletedUser.setDeleted(false);

        final String emailOfDeletedUser = "not_existing_email@mail.ru";
        final String passwordOfDeletedUser = "password";
        final User deletedUser = new User(emailOfDeletedUser, passwordOfDeletedUser);
        deletedUser.setDeleted(true);

        assert this.userDAO != null;
        this.userDAO.addEntity(notDeletedUser);
        try
        {
            this.userDAO.addEntity(deletedUser);
            try
            {
                this.userDAO.updateDeletedStatusOfEntity(deletedUser, false);
            }
            finally
            {
                this.userDAO.deleteEntity(deletedUser);
            }
        }
        finally
        {
            this.userDAO.deleteEntity(notDeletedUser);
        }
    }

    //Test for trigger 'mark_as_deleted_payment_cards_of_marked_as_deleted_user'
    @Test
    public final void associatedPaymentCardShouldAlsoBeMarkedAsDeleted()
            throws AddingEntityException, UpdatingEntityException, FindingEntityException, DeletingEntityException
    {
        final String emailOfClient = "not_existing_email@mail.ru";
        final String passwordOfClient = "password";
        final String nameOfClient = "Vlad";
        final String surnameOfClient = "Zuev";
        final String patronymicOfClient = "Sergeevich";
        final String phoneNumberOfClient = "+375-44-111-11-11";

        final BigDecimal moneyOfBankAccountOfClient = BigDecimal.TEN;
        final boolean blockedOfBankAccountOfClient = false;
        final String numberOfBankAccountOfClient = "11111222223333344444";
        final BankAccount bankAccountOfClient = new BankAccount(moneyOfBankAccountOfClient,
                blockedOfBankAccountOfClient, numberOfBankAccountOfClient);
        bankAccountOfClient.setDeleted(false);

        final Client client = new Client(emailOfClient, passwordOfClient, nameOfClient, surnameOfClient,
                patronymicOfClient, phoneNumberOfClient, bankAccountOfClient);
        client.setDeleted(false);

        final String cardNumberOfPaymentCard = "1111-2222-3333-4444";

        final short numberOfMonthOfExpirationDate = 12;
        final int yearOfExpirationDate = Year.now().getValue();
        final PaymentCard.ExpirationDate expirationDateOfPaymentCard = new PaymentCard.ExpirationDate(
                numberOfMonthOfExpirationDate, yearOfExpirationDate);

        final String paymentSystemOfPaymentCard = "Visa";
        final String cvcOfPaymentCard = "111";
        final String nameOfBankOfPaymentCard = "Belarusbank";
        final String passwordOfPaymentCard = "1111";

        final PaymentCard paymentCard = new PaymentCard(cardNumberOfPaymentCard, expirationDateOfPaymentCard,
                paymentSystemOfPaymentCard, cvcOfPaymentCard, client, nameOfBankOfPaymentCard, passwordOfPaymentCard);
        paymentCard.setDeleted(false);

        assert this.bankAccountDAO != null;
        this.bankAccountDAO.addEntity(bankAccountOfClient);
        try
        {
            assert this.clientDAO != null;
            this.clientDAO.addEntity(client);
            try
            {
                assert this.paymentCardDAO != null;
                this.paymentCardDAO.addEntity(paymentCard);
                try
                {
                    this.clientDAO.updateDeletedStatusOfEntity(client, true);
                    final Optional<PaymentCard> optionalOfFoundPaymentCard = this.paymentCardDAO.findEntityById(
                            paymentCard.getId());
                    if(optionalOfFoundPaymentCard.isEmpty())
                    {
                        Assert.fail();
                    }
                    final PaymentCard foundPaymentCard = optionalOfFoundPaymentCard.get();
                    Assert.assertTrue(foundPaymentCard.isDeleted());
                }
                finally
                {
                    this.paymentCardDAO.deleteEntity(paymentCard);
                }
            }
            finally
            {
                this.clientDAO.deleteEntity(client);
            }
        }
        finally
        {
            this.bankAccountDAO.deleteEntity(bankAccountOfClient);
        }
    }

    @Test(dependsOnMethods = {"userShouldBeAdded", "userShouldBeFoundById"})
    public final void userShouldBeDeletedById()
            throws AddingEntityException, DeletingEntityException, FindingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password6";
        final User user = new User(email, password);
        user.setDeleted(true);
        assert this.userDAO != null;
        this.userDAO.addEntity(user);

        final long idOfDeletedUser = user.getId();
        this.userDAO.deleteEntity(idOfDeletedUser);

        final Optional<User> optionalOfFoundUser = this.userDAO.findEntityById(idOfDeletedUser);
        Assert.assertTrue(optionalOfFoundUser.isEmpty());
    }

    @Test
    public final void userShouldNotBeDeletedByNotValidId()
            throws OffloadingEntitiesException, DeletingEntityException
    {
        final long id = -1;
        final String email = "not_existing_email@mail.ru";
        final String password = "password6";
        final User user = new User(id, email, password);
        user.setDeleted(false);

        assert this.userDAO != null;
        final Collection<User> usersBeforeDeleting = this.userDAO.offloadEntities();
        this.userDAO.deleteEntity(user.getId());
        final Collection<User> usersAfterDeleting = this.userDAO.offloadEntities();

        Assert.assertEquals(usersBeforeDeleting, usersAfterDeleting);
    }

    @Test(dependsOnMethods = {"userShouldBeAdded", "userShouldBeFoundById"})
    public final void userShouldBeDeleted()
            throws AddingEntityException, DeletingEntityException, FindingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final User user = new User(email, password);
        user.setDeleted(true);

        assert this.userDAO != null;
        this.userDAO.addEntity(user);
        this.userDAO.deleteEntity(user);

        final long idOfDeletedUser = user.getId();
        final Optional<User> optionalOfFoundUser = this.userDAO.findEntityById(idOfDeletedUser);
        Assert.assertTrue(optionalOfFoundUser.isEmpty());
    }

    @Test
    public final void userWithNotValidIdShouldNotBeDeleted()
            throws OffloadingEntitiesException, DeletingEntityException
    {
        final long id = -1;
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final User user = new User(id, email, password);

        assert this.userDAO != null;
        final Collection<User> usersBeforeDeleting = this.userDAO.offloadEntities();
        this.userDAO.deleteEntity(user);
        final Collection<User> usersAfterDeleting = this.userDAO.offloadEntities();

        Assert.assertEquals(usersBeforeDeleting, usersAfterDeleting);
    }

    @Test(dependsOnMethods = {"userShouldBeAdded"})
    public final void userWithGivenIdShouldExist()
            throws AddingEntityException, DefiningExistingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password6";
        final User user = new User(email, password);
        user.setDeleted(true);

        assert this.userDAO != null;
        this.userDAO.addEntity(user);
        try
        {
            final boolean userExists = this.userDAO.isEntityWithGivenIdExisting(user.getId());
            Assert.assertTrue(userExists);
        }
        finally
        {
            this.userDAO.deleteEntity(user);
        }
    }

    @Test
    public final void userWithNotValidIdShouldNotExist()
            throws DefiningExistingEntityException
    {
        final long idOfResearchUser = -1;
        assert this.userDAO != null;
        final boolean userExists = this.userDAO.isEntityWithGivenIdExisting(idOfResearchUser);
        Assert.assertFalse(userExists);
    }

    @Test(dependsOnMethods = {"userShouldBeAdded"})
    public final void notDeletedUserShouldBeFoundByEmail()
            throws AddingEntityException, FindingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final User user = new User(email, password);
        user.setDeleted(false);

        assert this.userDAO != null;
        this.userDAO.addEntity(user);
        try
        {
            final Optional<User> optionalOfFoundUser = this.userDAO.findNotDeletedUserByEmail(user.getEmail());
            if(optionalOfFoundUser.isEmpty())
            {
                Assert.fail();
            }
            final User foundUser = optionalOfFoundUser.get();
            Assert.assertEquals(user, foundUser);
        }
        finally
        {
            this.userDAO.deleteEntity(user);
        }
    }

    @Test
    public final void notDeletedUserShouldNotBeFoundByNotValidEmail()
            throws FindingEntityException
    {
        final String email = "not_valid_email";
        assert this.userDAO != null;
        final Optional<User> optionalOfFoundUser = this.userDAO.findNotDeletedUserByEmail(email);
        Assert.assertTrue(optionalOfFoundUser.isEmpty());
    }

    @Test(dependsOnMethods = {"userShouldBeAdded"})
    public final void userShouldNotBeFoundByEmailBecauseOfDeletedStatus()
            throws AddingEntityException, FindingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final User user = new User(email, password);
        user.setDeleted(true);

        assert this.userDAO != null;
        this.userDAO.addEntity(user);
        try
        {
            final Optional<User> optionalOfFoundUser = this.userDAO.findNotDeletedUserByEmail(user.getEmail());
            Assert.assertTrue(optionalOfFoundUser.isEmpty());
        }
        finally
        {
            this.userDAO.deleteEntity(user);
        }
    }

    @Test(dependsOnMethods = {"userShouldBeAdded"})
    public final void notDeletedUsersShouldBeFoundByPassword()
            throws AddingEntityException, OffloadingEntitiesException, DeletingEntityException
    {
        final String emailOfNotDeletedUser = "first_email@mail.ru";
        final String passwordOfNotDeletedUser = "first_password";
        final User notDeletedUser = new User(emailOfNotDeletedUser, passwordOfNotDeletedUser);
        notDeletedUser.setDeleted(false);

        final String emailOfDeletedUser = "second_email@mail.ru";
        final String passwordOfDeletedUser = "second_password";
        final User deletedUser = new User(emailOfDeletedUser, passwordOfDeletedUser);
        deletedUser.setDeleted(true);

        assert this.userDAO != null;
        this.userDAO.addEntity(deletedUser);
        try
        {
            this.userDAO.addEntity(notDeletedUser);
            try
            {
                final Collection<User> foundUsers = this.userDAO.findNotDeletedUsersByPassword(passwordOfNotDeletedUser);
                final boolean testSuccess = foundUsers != null && !foundUsers.isEmpty()
                        && foundUsers.contains(notDeletedUser) && !foundUsers.contains(deletedUser);
                Assert.assertTrue(testSuccess);
            }
            finally
            {
                this.userDAO.deleteEntity(notDeletedUser);
            }
        }
        finally
        {
            this.userDAO.deleteEntity(deletedUser);
        }
    }

    @Test(dependsOnMethods = {"userShouldBeAdded"})
    public final void notDeletedUserWithGivenEmailShouldExist()
            throws AddingEntityException, DefiningExistingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final User user = new User(email, password);
        user.setDeleted(false);

        assert this.userDAO != null;
        this.userDAO.addEntity(user);
        try
        {
            final boolean userExists = this.userDAO.isNotDeletedUserWithGivenEmailExist(user.getEmail());
            Assert.assertTrue(userExists);
        }
        finally
        {
            this.userDAO.deleteEntity(user);
        }
    }

    @Test
    public final void userWithNotValidEmailShouldNotExist()
            throws DefiningExistingEntityException
    {
        final String email = "not_valid_email";
        assert this.userDAO != null;
        final boolean userExists = this.userDAO.isNotDeletedUserWithGivenEmailExist(email);
        Assert.assertFalse(userExists);
    }

    @Test(dependsOnMethods = {"userShouldBeAdded"})
    public final void userShouldNotExistBecauseOfDeletedStatus()
            throws AddingEntityException, DefiningExistingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final User user = new User(email, password);
        user.setDeleted(true);

        assert this.userDAO != null;
        this.userDAO.addEntity(user);
        try
        {
            final boolean userExists = this.userDAO.isNotDeletedUserWithGivenEmailExist(user.getEmail());
            Assert.assertFalse(userExists);
        }
        finally
        {
            this.userDAO.deleteEntity(user);
        }
    }

    @Test(dependsOnMethods = {"userShouldBeAdded"})
    public final void passwordShouldBeCorrectForGivenEmail()
            throws AddingEntityException, DefiningExistingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final User user = new User(email, password);
        user.setDeleted(false);

        assert this.userDAO != null;
        this.userDAO.addEntity(user);
        try
        {
            final boolean passwordIsCorrect = this.userDAO.isCorrectPasswordForGivenEmail(password, email);
            Assert.assertTrue(passwordIsCorrect);
        }
        finally
        {
            this.userDAO.deleteEntity(user);
        }
    }

    @Test(dependsOnMethods = {"userShouldBeAdded"})
    public final void passwordShouldNotBeCorrectForGivenEmail()
            throws AddingEntityException, DefiningExistingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final User user = new User(email, password);
        user.setDeleted(false);

        assert this.userDAO != null;
        this.userDAO.addEntity(user);
        try
        {
            final String researchPassword = "second_password";
            final boolean passwordIsCorrect = this.userDAO.isCorrectPasswordForGivenEmail(researchPassword, email);
            Assert.assertFalse(passwordIsCorrect);
        }
        finally
        {
            this.userDAO.deleteEntity(user);
        }
    }

    @Test(dependsOnMethods = {"userShouldBeAdded"})
    public final void passwordShouldNotBeCorrectForGivenEmailBecauseOfDeletedStatusOfUser()
            throws AddingEntityException, DefiningExistingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final User user = new User(email, password);
        user.setDeleted(false);

        assert this.userDAO != null;
        this.userDAO.addEntity(user);
        try
        {
            final boolean passwordIsCorrect = this.userDAO.isCorrectPasswordForGivenEmail(password, email);
            Assert.assertTrue(passwordIsCorrect);
        }
        finally
        {
            this.userDAO.deleteEntity(user);
        }
    }

    @Test(dependsOnMethods = {"userShouldBeAdded"})
    public final void usersShouldBeFoundByEmail()
            throws AddingEntityException, OffloadingEntitiesException, DeletingEntityException
    {
        final String email = "first_not_existing_email@mail.ru";

        final String passwordOfNotDeletedUser = "password";
        final User notDeletedUser = new User(email, passwordOfNotDeletedUser);
        notDeletedUser.setDeleted(false);

        final String passwordOfDeletedUser = "password";
        final User deletedUser = new User(email, passwordOfDeletedUser);
        deletedUser.setDeleted(true);

        assert this.userDAO != null;
        this.userDAO.addEntity(notDeletedUser);
        try
        {
            this.userDAO.addEntity(deletedUser);
            try
            {
                final Collection<User> foundUsers = this.userDAO.findUsersByEmail(email);
                final boolean testSuccess = foundUsers != null && !foundUsers.isEmpty()
                        && foundUsers.contains(notDeletedUser) && foundUsers.contains(deletedUser);
                Assert.assertTrue(testSuccess);
            }
            finally
            {
                this.userDAO.deleteEntity(deletedUser);
            }
        }
        finally
        {
            this.userDAO.deleteEntity(notDeletedUser);
        }
    }

    @Test
    public final void usersShouldNotBeFoundByNotValidEmail()
            throws OffloadingEntitiesException
    {
        final String email = "not_valid_email";
        assert this.userDAO != null;
        final Collection<User> foundUsers = this.userDAO.findUsersByEmail(email);
        Assert.assertTrue(foundUsers.isEmpty());
    }
}
