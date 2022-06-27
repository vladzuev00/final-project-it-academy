package by.itacademy.zuevvlad.cardpaymentproject.dao.user;

import by.itacademy.zuevvlad.cardpaymentproject.configuration.DAOConfiguration;
import by.itacademy.zuevvlad.cardpaymentproject.configuration.WebMainConfiguration;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.*;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Administrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.Optional;

@Test
@ContextConfiguration(classes = {WebMainConfiguration.class})
public final class AdministratorDAOTest extends AbstractTestNGSpringContextTests
{
    @Autowired
    @Qualifier(value = DAOConfiguration.NAME_OF_BEAN_OF_ADMINISTRATOR_DAO)
    private final AdministratorDAO administratorDAO;

    public AdministratorDAOTest()
    {
        super();
        this.administratorDAO = null;
    }

    @Test
    public final void administratorShouldBeAdded()
            throws AddingEntityException, FindingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final Administrator.Level level = Administrator.Level.SUPPORTER;
        final Administrator addedAdministrator = new Administrator(email, password, level);
        addedAdministrator.setDeleted(true);

        final long idOfAddedAdministratorBeforeAdding = addedAdministrator.getId();
        assert this.administratorDAO != null;
        this.administratorDAO.addEntity(addedAdministrator);
        try
        {
            final long idOfAddedAdministratorAfterAdding = addedAdministrator.getId();

            final Optional<Administrator> optionalOfAddedAdministratorFromDataBase
                    = this.administratorDAO.findEntityById(idOfAddedAdministratorAfterAdding);
            if(optionalOfAddedAdministratorFromDataBase.isEmpty())
            {
                Assert.fail();
            }
            final Administrator addedAdministratorFromDataBase = optionalOfAddedAdministratorFromDataBase.get();

            final boolean testSuccess = addedAdministrator.equals(addedAdministratorFromDataBase)
                    && idOfAddedAdministratorBeforeAdding != idOfAddedAdministratorAfterAdding;
            Assert.assertTrue(testSuccess);
        }
        finally
        {
            this.administratorDAO.deleteEntity(addedAdministrator);
        }
    }

    //Test for constraint 'administrator_level_should_be_correct'
    @Test(expectedExceptions = {AddingEntityException.class})
    public final void administratorWithNotDefinedLevelShouldNotBeAdded()
            throws AddingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final Administrator.Level level = Administrator.Level.NOT_DEFINED;
        final Administrator addedAdministrator = new Administrator(email, password, level);
        addedAdministrator.setDeleted(true);

        assert this.administratorDAO != null;
        this.administratorDAO.addEntity(addedAdministrator);
        try
        {
            Assert.fail();
        }
        finally
        {
            this.administratorDAO.deleteEntity(addedAdministrator);
        }
    }

    //Test for trigger 'check_unique_email_not_deleted_users_before_adding'
    @Test(dependsOnMethods = {"administratorShouldBeAdded"})
    public final void twoAdministratorsWithSameEmailAndDifferentDeletedStatusShouldBeAdded()
            throws AddingEntityException, OffloadingEntitiesException, DeletingEntityException
    {
        final String emailOfDeletedAdministrator = "not_existing_email@mail.ru";
        final String passwordOfDeletedAdministrator = "password";
        final Administrator.Level levelOfDeletedAdministrator = Administrator.Level.SUPPORTER;
        final Administrator deletedAdministrator = new Administrator(emailOfDeletedAdministrator,
                passwordOfDeletedAdministrator, levelOfDeletedAdministrator);
        deletedAdministrator.setDeleted(true);

        final String emailOfNotDeletedAdministrator = "not_existing_email@mail.ru";
        final String passwordOfNotDeletedAdministrator = "password";
        final Administrator.Level levelOfNotDeletedAdministrator = Administrator.Level.SUPPORTER;
        final Administrator notDeletedAdministrator = new Administrator(emailOfNotDeletedAdministrator,
                passwordOfNotDeletedAdministrator, levelOfNotDeletedAdministrator);
        notDeletedAdministrator.setDeleted(false);

        assert this.administratorDAO != null;
        this.administratorDAO.addEntity(deletedAdministrator);
        try
        {
            this.administratorDAO.addEntity(notDeletedAdministrator);
            try
            {
                final Collection<Administrator> offloadedAdministrators = this.administratorDAO.offloadEntities();
                final boolean testSuccess = offloadedAdministrators != null && !offloadedAdministrators.isEmpty()
                        && offloadedAdministrators.contains(deletedAdministrator)
                        && offloadedAdministrators.contains(notDeletedAdministrator);
                Assert.assertTrue(testSuccess);
            }
            finally
            {
                this.administratorDAO.deleteEntity(notDeletedAdministrator);
            }
        }
        finally
        {
            this.administratorDAO.deleteEntity(deletedAdministrator);
        }
    }

    //Test for trigger 'check_unique_email_not_deleted_users_before_adding'
    @Test(dependsOnMethods = {"administratorShouldBeAdded"}, expectedExceptions = {AddingEntityException.class})
    public final void twoAdministratorsWithSameEmailAndNotDeletedStatusShouldNotBeAdded()
            throws AddingEntityException, DeletingEntityException
    {
        final String emailOfFirstAdministrator = "not_existing_email@mail.ru";
        final String passwordOfFirstAdministrator = "password";
        final Administrator.Level levelOfFirstAdministrator = Administrator.Level.SUPPORTER;
        final Administrator firstAdministrator = new Administrator(emailOfFirstAdministrator,
                passwordOfFirstAdministrator, levelOfFirstAdministrator);
        firstAdministrator.setDeleted(false);

        final String emailOfSecondAdministrator = "not_existing_email@mail.ru";
        final String passwordOfSecondAdministrator = "password";
        final Administrator.Level levelOfSecondAdministrator = Administrator.Level.SUPPORTER;
        final Administrator secondAdministrator = new Administrator(emailOfSecondAdministrator,
                passwordOfSecondAdministrator, levelOfSecondAdministrator);
        secondAdministrator.setDeleted(false);

        try
        {
            assert this.administratorDAO != null;
            this.administratorDAO.addEntity(firstAdministrator);
        }
        catch(final AddingEntityException addingEntityException)
        {
            Assert.fail();
        }
        try
        {
            this.administratorDAO.addEntity(secondAdministrator);
            this.administratorDAO.deleteEntity(secondAdministrator);
        }
        finally
        {
            this.administratorDAO.deleteEntity(firstAdministrator);
        }
    }

    @Test(dependsOnMethods = {"administratorShouldBeAdded"})
    public final void allAdministratorsShouldBeOffloaded()
            throws AddingEntityException, OffloadingEntitiesException, DeletingEntityException
    {
        final String emailOfNotDeletedAdministrator = "not_existing_email@mail.ru";
        final String passwordOfNotDeletedAdministrator = "password";
        final Administrator.Level levelOfNotDeletedAdministrator = Administrator.Level.SUPPORTER;
        final Administrator notDeletedAdministrator = new Administrator(emailOfNotDeletedAdministrator,
                passwordOfNotDeletedAdministrator, levelOfNotDeletedAdministrator);
        notDeletedAdministrator.setDeleted(false);

        final String emailOfDeletedAdministrator = "not_existing_email@mail.ru";
        final String passwordOfDeletedAdministrator = "password1";
        final Administrator.Level levelOfDeletedAdministrator = Administrator.Level.SUPPORTER;
        final Administrator deletedAdministrator = new Administrator(emailOfDeletedAdministrator,
                passwordOfDeletedAdministrator, levelOfDeletedAdministrator);
        deletedAdministrator.setDeleted(true);

        assert this.administratorDAO != null;
        this.administratorDAO.addEntity(notDeletedAdministrator);
        try
        {
            this.administratorDAO.addEntity(deletedAdministrator);
            try
            {
                final Collection<Administrator> offloadedAdministrators = this.administratorDAO.offloadEntities();
                final boolean testSuccess = offloadedAdministrators != null && !offloadedAdministrators.isEmpty()
                        && offloadedAdministrators.contains(notDeletedAdministrator)
                        && offloadedAdministrators.contains(deletedAdministrator);
                Assert.assertTrue(testSuccess);
            }
            finally
            {
                this.administratorDAO.deleteEntity(deletedAdministrator);
            }
        }
        finally
        {
            this.administratorDAO.deleteEntity(notDeletedAdministrator);
        }
    }

    @Test(dependsOnMethods = {"administratorShouldBeAdded"})
    public final void administratorShouldBeFoundById()
            throws AddingEntityException, FindingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final Administrator.Level level = Administrator.Level.SUPPORTER;
        final Administrator addedAdministrator = new Administrator(email, password, level);
        addedAdministrator.setDeleted(true);

        assert this.administratorDAO != null;
        this.administratorDAO.addEntity(addedAdministrator);
        try
        {
            final Optional<Administrator> optionalOfAdministratorFoundInDataBase = this.administratorDAO.findEntityById(
                    addedAdministrator.getId());
            if(optionalOfAdministratorFoundInDataBase.isEmpty())
            {
                Assert.fail();
            }
            final Administrator administratorFoundInDataBase = optionalOfAdministratorFoundInDataBase.get();
            Assert.assertEquals(addedAdministrator, administratorFoundInDataBase);
        }
        finally
        {
            this.administratorDAO.deleteEntity(addedAdministrator);
        }
    }

    @Test
    public final void administratorShouldNotBeFoundByNotValidId()
            throws FindingEntityException
    {
        final long idOfFoundAdministrator = -1;
        assert this.administratorDAO != null;
        final Optional<Administrator> optionalOfFoundAdministrator = this.administratorDAO.findEntityById(
                idOfFoundAdministrator);
        Assert.assertTrue(optionalOfFoundAdministrator.isEmpty());
    }

    @Test(dependsOnMethods = {"administratorShouldBeAdded"})
    public final void notDeletedAdministratorsShouldBeFound()
            throws AddingEntityException, OffloadingEntitiesException, DeletingEntityException
    {
        final String emailOfDeletedAdministrator = "first_not_existing_email@mail.ru";
        final String passwordOfDeletedAdministrator = "password";
        final Administrator.Level levelOfDeletedAdministrator = Administrator.Level.SUPPORTER;
        final Administrator deletedAdministrator = new Administrator(emailOfDeletedAdministrator,
                passwordOfDeletedAdministrator, levelOfDeletedAdministrator);
        deletedAdministrator.setDeleted(true);

        final String emailOfNotDeletedAdministrator = "second_not_existing_email@mail.ru";
        final String passwordOfNotDeletedAdministrator = "password";
        final Administrator.Level levelOfNotDeletedAdministrator = Administrator.Level.SUPPORTER;
        final Administrator notDeletedAdministrator = new Administrator(emailOfNotDeletedAdministrator,
                passwordOfNotDeletedAdministrator, levelOfNotDeletedAdministrator);
        notDeletedAdministrator.setDeleted(false);

        assert this.administratorDAO != null;
        this.administratorDAO.addEntity(deletedAdministrator);
        try
        {
            this.administratorDAO.addEntity(notDeletedAdministrator);
            try
            {
                final Collection<Administrator> notDeletedAdministrators = this.administratorDAO
                        .findNotDeletedEntities();
                final boolean testSuccess = notDeletedAdministrators != null && !notDeletedAdministrators.isEmpty()
                        && notDeletedAdministrators.contains(notDeletedAdministrator)
                        && !notDeletedAdministrators.contains(deletedAdministrator);
                Assert.assertTrue(testSuccess);
            }
            finally
            {
                this.administratorDAO.deleteEntity(notDeletedAdministrator);
            }
        }
        finally
        {
            this.administratorDAO.deleteEntity(deletedAdministrator);
        }
    }

    @Test(dependsOnMethods = {"administratorShouldBeAdded"})
    public final void deletedAdministratorsShouldBeFound()
            throws AddingEntityException, OffloadingEntitiesException, DeletingEntityException
    {
        final String emailOfDeletedAdministrator = "first_not_existing_email@mail.ru";
        final String passwordOfDeletedAdministrator = "password";
        final Administrator.Level levelOfDeletedAdministrator = Administrator.Level.SUPPORTER;
        final Administrator deletedAdministrator = new Administrator(emailOfDeletedAdministrator,
                passwordOfDeletedAdministrator, levelOfDeletedAdministrator);
        deletedAdministrator.setDeleted(true);

        final String emailOfNotDeletedAdministrator = "second_not_existing_email@mail.ru";
        final String passwordOfNotDeletedAdministrator = "password";
        final Administrator.Level levelOfNotDeletedAdministrator = Administrator.Level.SUPPORTER;
        final Administrator notDeletedAdministrator = new Administrator(emailOfNotDeletedAdministrator,
                passwordOfNotDeletedAdministrator, levelOfNotDeletedAdministrator);
        notDeletedAdministrator.setDeleted(false);

        assert this.administratorDAO != null;
        this.administratorDAO.addEntity(deletedAdministrator);
        try
        {
            this.administratorDAO.addEntity(notDeletedAdministrator);
            try
            {
                final Collection<Administrator> deletedAdministrators = this.administratorDAO.findDeletedEntities();
                final boolean testSuccess = deletedAdministrators != null && !deletedAdministrators.isEmpty()
                        && deletedAdministrators.contains(deletedAdministrator)
                        && !deletedAdministrators.contains(notDeletedAdministrator);
                Assert.assertTrue(testSuccess);
            }
            finally
            {
                this.administratorDAO.deleteEntity(notDeletedAdministrator);
            }
        }
        finally
        {
            this.administratorDAO.deleteEntity(deletedAdministrator);
        }
    }

    @Test(dependsOnMethods = {"administratorShouldBeAdded", "administratorShouldBeFoundById",
            "administratorShouldBeDeleted"})
    public final void administratorShouldBeUpdated()
            throws AddingEntityException, UpdatingEntityException, FindingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final Administrator.Level level = Administrator.Level.SUPPORTER;
        final Administrator administrator = new Administrator(email, password, level);

        assert this.administratorDAO != null;
        this.administratorDAO.addEntity(administrator);
        try
        {
            final String newPassword = "new_password";
            final Administrator.Level newLevel = Administrator.Level.MODIFIER;
            administrator.setPassword(newPassword);
            administrator.setLevel(newLevel);
            this.administratorDAO.updateEntity(administrator);

            final Optional<Administrator> optionalOfUpdatedAdministratorFromDataBase
                    = this.administratorDAO.findEntityById(administrator.getId());
            if(optionalOfUpdatedAdministratorFromDataBase.isEmpty())
            {
                Assert.fail();
            }

            final Administrator updatedAdministratorFromDataBase = optionalOfUpdatedAdministratorFromDataBase.get();
            Assert.assertEquals(administrator, updatedAdministratorFromDataBase);
        }
        finally
        {
            this.administratorDAO.deleteEntity(administrator);
        }
    }

    //Test for constraint 'administrator_level_should_be_correct'
    @Test(dependsOnMethods = {"administratorShouldBeAdded"}, expectedExceptions = {UpdatingEntityException.class})
    public final void administratorShouldNotBeUpdatedByNotDefinedLevel()
            throws AddingEntityException, UpdatingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final Administrator.Level level = Administrator.Level.SUPPORTER;
        final Administrator administrator = new Administrator(email, password, level);
        administrator.setDeleted(false);

        assert this.administratorDAO != null;
        this.administratorDAO.addEntity(administrator);
        try
        {
            final Administrator.Level newLevel = Administrator.Level.NOT_DEFINED;
            administrator.setLevel(newLevel);
            this.administratorDAO.updateEntity(administrator);
        }
        finally
        {
            this.administratorDAO.deleteEntity(administrator);
        }
    }

    //Test for trigger 'check_unique_email_not_deleted_users_before_updating'
    @Test(dependsOnMethods = {"administratorShouldBeAdded"}, expectedExceptions = {UpdatingEntityException.class})
    public final void notDeletedAdministratorShouldNotBeUpdatedByEmailOfAnotherNotDeletedUser()
            throws AddingEntityException, UpdatingEntityException, DeletingEntityException
    {
        final String emailOfFirstAdministrator = "first_not_existing_email@mail.ru";
        final String passwordOfFirstAdministrator = "password";
        final Administrator.Level levelOfFirstAdministrator = Administrator.Level.SUPPORTER;
        final Administrator firstAdministrator = new Administrator(emailOfFirstAdministrator,
                passwordOfFirstAdministrator, levelOfFirstAdministrator);
        firstAdministrator.setDeleted(false);

        final String emailOfSecondAdministrator = "second_not_existing_email@mail.ru";
        final String passwordOfSecondAdministrator = "password";
        final Administrator.Level levelOfSecondAdministrator = Administrator.Level.MAIN;
        final Administrator secondAdministrator = new Administrator(emailOfSecondAdministrator,
                passwordOfSecondAdministrator, levelOfSecondAdministrator);
        secondAdministrator.setDeleted(false);

        assert this.administratorDAO != null;
        this.administratorDAO.addEntity(firstAdministrator);
        try
        {
            this.administratorDAO.addEntity(secondAdministrator);
            try
            {
                secondAdministrator.setEmail(firstAdministrator.getEmail());
                this.administratorDAO.updateEntity(secondAdministrator);
            }
            finally
            {
                this.administratorDAO.deleteEntity(secondAdministrator);
            }
        }
        finally
        {
            this.administratorDAO.deleteEntity(firstAdministrator);
        }
    }

    @Test(dependsOnMethods = {"administratorShouldBeAdded", "administratorShouldBeFoundById",
            "administratorShouldBeDeleted"})
    public final void deletedStatusOfAdministratorWithGivenIdShouldBeUpdated()
            throws AddingEntityException, UpdatingEntityException, FindingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final Administrator.Level level = Administrator.Level.SUPPORTER;
        final Administrator administrator = new Administrator(email, password, level);
        administrator.setDeleted(true);

        assert this.administratorDAO != null;
        this.administratorDAO.addEntity(administrator);
        try
        {
            final boolean newDeleted = !administrator.isDeleted();
            this.administratorDAO.updateDeletedStatusOfEntity(administrator.getId(), newDeleted);

            final Optional<Administrator> optionalOfUpdatedAdministrator = this.administratorDAO.findEntityById(
                    administrator.getId());
            if(optionalOfUpdatedAdministrator.isEmpty())
            {
                Assert.fail();
            }
            final Administrator updatedAdministrator = optionalOfUpdatedAdministrator.get();

            Assert.assertEquals(updatedAdministrator.isDeleted(), newDeleted);
        }
        finally
        {
            this.administratorDAO.deleteEntity(administrator);
        }
    }

    @Test(dependsOnMethods = {"administratorShouldBeAdded", "administratorShouldBeFoundById",
            "administratorShouldBeDeleted"})
    public final void deletedStatusOfAdministratorShouldBeUpdated()
            throws AddingEntityException, UpdatingEntityException, FindingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final Administrator.Level level = Administrator.Level.SUPPORTER;
        final Administrator administrator = new Administrator(email, password, level);
        administrator.setDeleted(true);

        assert this.administratorDAO != null;
        this.administratorDAO.addEntity(administrator);
        try
        {
            final boolean newDeleted = !administrator.isDeleted();
            this.administratorDAO.updateDeletedStatusOfEntity(administrator, newDeleted);

            final Optional<Administrator> optionalOfUpdatedAdministrator = this.administratorDAO.findEntityById(
                    administrator.getId());
            if(optionalOfUpdatedAdministrator.isEmpty())
            {
                Assert.fail();
            }
            final Administrator updatedAdministrator = optionalOfUpdatedAdministrator.get();

            Assert.assertEquals(updatedAdministrator.isDeleted(), newDeleted);
        }
        finally
        {
            this.administratorDAO.deleteEntity(administrator);
        }
    }

    //Test for trigger 'check_unique_email_not_deleted_users_before_updating'
    @Test(dependsOnMethods = {"administratorShouldBeAdded"}, expectedExceptions = {UpdatingEntityException.class})
    public final void deletedStatusOfDeletedAdministratorShouldNotBeUpdatedBecauseOfExistingNotDeletedUserWithSameEmail()
            throws AddingEntityException, UpdatingEntityException, DeletingEntityException
    {
        final String emailOfNotDeletedUser = "not_existing_email@mail.ru";
        final String passwordOfNotDeletedUser = "password";
        final Administrator.Level levelOfNotDeletedAdministrator = Administrator.Level.SUPPORTER;
        final Administrator notDeletedAdministrator = new Administrator(emailOfNotDeletedUser, passwordOfNotDeletedUser,
                levelOfNotDeletedAdministrator);
        notDeletedAdministrator.setDeleted(false);

        final String emailOfDeletedUser = "not_existing_email@mail.ru";
        final String passwordOfDeletedUser = "password";
        final Administrator.Level levelOfDeletedAdministrator = Administrator.Level.MAIN;
        final Administrator deletedAdministrator = new Administrator(emailOfDeletedUser, passwordOfDeletedUser,
                levelOfDeletedAdministrator);
        deletedAdministrator.setDeleted(true);

        assert this.administratorDAO != null;
        this.administratorDAO.addEntity(notDeletedAdministrator);
        try
        {
            this.administratorDAO.addEntity(deletedAdministrator);
            try
            {
                this.administratorDAO.updateDeletedStatusOfEntity(deletedAdministrator, false);
            }
            finally
            {
                this.administratorDAO.deleteEntity(deletedAdministrator);
            }
        }
        finally
        {
            this.administratorDAO.deleteEntity(notDeletedAdministrator);
        }
    }

    @Test(dependsOnMethods = {"administratorShouldBeAdded", "administratorShouldBeFoundById"})
    public final void administratorShouldBeDeletedById()
            throws AddingEntityException, DeletingEntityException, FindingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final Administrator.Level level = Administrator.Level.SUPPORTER;
        final Administrator administrator = new Administrator(email, password, level);
        assert this.administratorDAO != null;
        this.administratorDAO.addEntity(administrator);
        administrator.setDeleted(true);

        final long idOfDeletedAdministrator = administrator.getId();
        this.administratorDAO.deleteEntity(idOfDeletedAdministrator);

        final Optional<Administrator> optionalOfFoundAdministrator = this.administratorDAO.findEntityById(
                idOfDeletedAdministrator);
        Assert.assertTrue(optionalOfFoundAdministrator.isEmpty());
    }

    @Test
    public final void administratorShouldNotBeDeletedByNotValidId()
            throws OffloadingEntitiesException, DeletingEntityException
    {
        final long id = -1;
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final Administrator.Level level = Administrator.Level.SUPPORTER;
        final Administrator administrator = new Administrator(id, email, password, level);
        administrator.setDeleted(false);

        assert this.administratorDAO != null;
        final Collection<Administrator> administratorsBeforeDeleting = this.administratorDAO.offloadEntities();
        this.administratorDAO.deleteEntity(administrator.getId());
        final Collection<Administrator> administratorsAfterDeleting = this.administratorDAO.offloadEntities();

        Assert.assertEquals(administratorsBeforeDeleting, administratorsAfterDeleting);
    }

    @Test(dependsOnMethods = {"administratorShouldBeAdded", "administratorShouldBeFoundById"})
    public final void administratorShouldBeDeleted()
            throws AddingEntityException, DeletingEntityException, FindingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final Administrator.Level level = Administrator.Level.SUPPORTER;
        final Administrator administrator = new Administrator(email, password, level);
        administrator.setDeleted(false);

        assert this.administratorDAO != null;
        this.administratorDAO.addEntity(administrator);
        this.administratorDAO.deleteEntity(administrator);

        final long idOfDeletedAdministrator = administrator.getId();
        final Optional<Administrator> optionalOfFoundAdministrator = this.administratorDAO.findEntityById(
                idOfDeletedAdministrator);
        Assert.assertTrue(optionalOfFoundAdministrator.isEmpty());
    }

    @Test
    public final void administratorWithNotValidIdShouldNotBeDeleted()
            throws OffloadingEntitiesException, DeletingEntityException
    {
        final long id = -1;
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final Administrator.Level level = Administrator.Level.SUPPORTER;
        final Administrator administrator = new Administrator(id, email, password, level);

        assert this.administratorDAO != null;
        final Collection<Administrator> administratorsBeforeDeleting = this.administratorDAO.offloadEntities();
        this.administratorDAO.deleteEntity(administrator);
        final Collection<Administrator> administratorsAfterDeleting = this.administratorDAO.offloadEntities();

        Assert.assertEquals(administratorsBeforeDeleting, administratorsAfterDeleting);
    }

    @Test(dependsOnMethods = {"administratorShouldBeAdded"})
    public final void administratorWithGivenIdShouldBeExisted()
            throws AddingEntityException, DefiningExistingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final Administrator.Level level = Administrator.Level.SUPPORTER;
        final Administrator administrator = new Administrator(email, password, level);
        administrator.setDeleted(true);

        assert this.administratorDAO != null;
        this.administratorDAO.addEntity(administrator);
        try
        {
            final long idOfResearchAdministrator = administrator.getId();
            final boolean administratorExists = this.administratorDAO.isEntityWithGivenIdExisting(
                    idOfResearchAdministrator);
            Assert.assertTrue(administratorExists);
        }
        finally
        {
            this.administratorDAO.deleteEntity(administrator);
        }
    }

    @Test
    public final void administratorWithNotValidIdShouldNotBeExisted()
            throws DefiningExistingEntityException
    {
        final long idOfResearchAdministrator = -1;
        assert this.administratorDAO != null;
        final boolean administratorExists = this.administratorDAO.isEntityWithGivenIdExisting(
                idOfResearchAdministrator);
        Assert.assertFalse(administratorExists);
    }

    @Test(dependsOnMethods = {"administratorShouldBeAdded"})
    public final void notDeletedAdministratorShouldBeFoundByEmail()
            throws AddingEntityException, FindingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final Administrator.Level level = Administrator.Level.SUPPORTER;
        final Administrator administrator = new Administrator(email, password, level);
        administrator.setDeleted(false);

        assert this.administratorDAO != null;
        this.administratorDAO.addEntity(administrator);
        try
        {
            final Optional<Administrator> optionalOfFoundAdministrator = this.administratorDAO
                    .findNotDeletedUserByEmail(administrator.getEmail());
            if(optionalOfFoundAdministrator.isEmpty())
            {
                Assert.fail();
            }
            final Administrator foundAdministrator = optionalOfFoundAdministrator.get();

            Assert.assertEquals(administrator, foundAdministrator);
        }
        finally
        {
            this.administratorDAO.deleteEntity(administrator);
        }
    }

    @Test(dependsOnMethods = {"administratorShouldBeAdded"})
    public final void deletedAdministratorShouldNotBeFoundByEmail()
            throws AddingEntityException, FindingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final Administrator.Level level = Administrator.Level.SUPPORTER;
        final Administrator administrator = new Administrator(email, password, level);
        administrator.setDeleted(true);

        assert this.administratorDAO != null;
        this.administratorDAO.addEntity(administrator);
        try
        {
            final Optional<Administrator> optionalOfFoundAdministrator = this.administratorDAO
                    .findNotDeletedUserByEmail(administrator.getEmail());
            Assert.assertTrue(optionalOfFoundAdministrator.isEmpty());
        }
        finally
        {
            this.administratorDAO.deleteEntity(administrator);
        }
    }

    @Test
    public final void administratorShouldNotBeFoundByNotValidEmail()
            throws FindingEntityException
    {
        final String email = "not_valid_email";
        assert this.administratorDAO != null;
        final Optional<Administrator> optionalOfFoundAdministrator = this.administratorDAO.findNotDeletedUserByEmail(
                email);
        Assert.assertTrue(optionalOfFoundAdministrator.isEmpty());
    }

    @Test(dependsOnMethods = {"administratorShouldBeAdded"})
    public final void notDeletedAdministratorsShouldBeFoundByPassword()
            throws AddingEntityException, OffloadingEntitiesException, DeletingEntityException
    {
        final String emailOfDeletedAdministrator = "first_not_existing_email@mail.ru";
        final String passwordOfDeletedAdministrator = "first_password";
        final Administrator.Level levelOfDeletedAdministrator = Administrator.Level.SUPPORTER;
        final Administrator deletedAdministrator = new Administrator(emailOfDeletedAdministrator,
                passwordOfDeletedAdministrator, levelOfDeletedAdministrator);
        deletedAdministrator.setDeleted(true);

        final String emailOfNotDeletedAdministrator = "second_not_existing_email@mail.ru";
        final String passwordOfNotDeletedAdministrator = "first_password";
        final Administrator.Level levelOfNotDeletedAdministrator = Administrator.Level.SUPPORTER;
        final Administrator notDeletedAdministrator = new Administrator(emailOfNotDeletedAdministrator,
                passwordOfNotDeletedAdministrator, levelOfNotDeletedAdministrator);
        notDeletedAdministrator.setDeleted(false);

        assert this.administratorDAO != null;
        this.administratorDAO.addEntity(deletedAdministrator);
        try
        {
            this.administratorDAO.addEntity(notDeletedAdministrator);
            try
            {
                final Collection<Administrator> foundAdministrators = this.administratorDAO.findNotDeletedUsersByPassword(
                        passwordOfDeletedAdministrator);
                final boolean testSuccess = foundAdministrators != null && !foundAdministrators.isEmpty()
                        && foundAdministrators.contains(notDeletedAdministrator)
                        && !foundAdministrators.contains(deletedAdministrator);
                Assert.assertTrue(testSuccess);
            }
            finally
            {
                this.administratorDAO.deleteEntity(notDeletedAdministrator);
            }
        }
        finally
        {
            this.administratorDAO.deleteEntity(deletedAdministrator);
        }
    }

    @Test(dependsOnMethods = {"administratorShouldBeAdded"})
    public final void notDeletedAdministratorWithGivenEmailShouldExist()
            throws AddingEntityException, DefiningExistingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final Administrator.Level level = Administrator.Level.SUPPORTER;
        final Administrator administrator = new Administrator(email, password, level);
        administrator.setDeleted(false);

        assert this.administratorDAO != null;
        this.administratorDAO.addEntity(administrator);
        try
        {
            final boolean administratorExists = this.administratorDAO.isNotDeletedUserWithGivenEmailExist(
                    administrator.getEmail());
            Assert.assertTrue(administratorExists);
        }
        finally
        {
            this.administratorDAO.deleteEntity(administrator);
        }
    }

    @Test
    public final void notDeletedAdministratorWithNotValidEmailShouldNotExist()
            throws DefiningExistingEntityException
    {
        final String email = "not_valid_email";
        assert this.administratorDAO != null;
        final boolean administratorExists = this.administratorDAO.isNotDeletedUserWithGivenEmailExist(email);
        Assert.assertFalse(administratorExists);
    }

    @Test(dependsOnMethods = {"administratorShouldBeAdded"})
    public final void passwordShouldBeCorrectForGivenEmail()
            throws AddingEntityException, DefiningExistingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final Administrator.Level level = Administrator.Level.SUPPORTER;
        final Administrator administrator = new Administrator(email, password, level);
        administrator.setDeleted(false);

        assert this.administratorDAO != null;
        this.administratorDAO.addEntity(administrator);
        try
        {
            final boolean passwordIsCorrect = this.administratorDAO.isCorrectPasswordForGivenEmail(password, email);
            Assert.assertTrue(passwordIsCorrect);
        }
        finally
        {
            this.administratorDAO.deleteEntity(administrator);
        }
    }

    @Test(dependsOnMethods = {"administratorShouldBeAdded"})
    public final void passwordShouldNotBeCorrectForGivenEmail()
            throws AddingEntityException, DefiningExistingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final Administrator.Level level = Administrator.Level.SUPPORTER;
        final Administrator administrator = new Administrator(email, password, level);
        administrator.setDeleted(false);

        assert this.administratorDAO != null;
        this.administratorDAO.addEntity(administrator);
        try
        {
            final String researchPassword = "research_password";
            final boolean passwordIsCorrect = this.administratorDAO.isCorrectPasswordForGivenEmail(researchPassword,
                    administrator.getEmail());
            Assert.assertFalse(passwordIsCorrect);
        }
        finally
        {
            this.administratorDAO.deleteEntity(administrator);
        }
    }

    @Test(dependsOnMethods = {"administratorShouldBeAdded"})
    public final void passwordShouldNotBeCorrectForGivenEmailBecauseOfDeletedStatusOfAdministrator()
            throws AddingEntityException, DefiningExistingEntityException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";
        final String password = "password";
        final Administrator.Level level = Administrator.Level.SUPPORTER;
        final Administrator administrator = new Administrator(email, password, level);
        administrator.setDeleted(true);

        assert this.administratorDAO != null;
        this.administratorDAO.addEntity(administrator);
        try
        {
            final String researchPassword = "password";
            final boolean passwordIsCorrect = this.administratorDAO.isCorrectPasswordForGivenEmail(researchPassword,
                    administrator.getEmail());
            Assert.assertFalse(passwordIsCorrect);
        }
        finally
        {
            this.administratorDAO.deleteEntity(administrator);
        }
    }

    @Test(dependsOnMethods = "administratorShouldBeAdded")
    public final void administratorsShouldBeFoundByEmail()
            throws AddingEntityException, OffloadingEntitiesException, DeletingEntityException
    {
        final String email = "not_existing_email@mail.ru";

        final String passwordOfDeletedAdministrator = "first_password";
        final Administrator.Level levelOfDeletedAdministrator = Administrator.Level.SUPPORTER;
        final Administrator deletedAdministrator = new Administrator(email, passwordOfDeletedAdministrator,
                levelOfDeletedAdministrator);
        deletedAdministrator.setDeleted(true);

        final String passwordOfNotDeletedAdministrator = "first_password";
        final Administrator.Level levelOfNotDeletedAdministrator = Administrator.Level.SUPPORTER;
        final Administrator notDeletedAdministrator = new Administrator(email, passwordOfNotDeletedAdministrator,
                levelOfNotDeletedAdministrator);
        notDeletedAdministrator.setDeleted(false);

        assert this.administratorDAO != null;
        this.administratorDAO.addEntity(deletedAdministrator);
        try
        {
            this.administratorDAO.addEntity(notDeletedAdministrator);
            try
            {
                final Collection<Administrator> foundAdministrators = this.administratorDAO.findUsersByEmail(email);
                final boolean testSuccess = foundAdministrators != null && !foundAdministrators.isEmpty()
                        && foundAdministrators.contains(deletedAdministrator)
                        && foundAdministrators.contains(notDeletedAdministrator);
                Assert.assertTrue(testSuccess);
            }
            finally
            {
                this.administratorDAO.deleteEntity(notDeletedAdministrator);
            }
        }
        finally
        {
            this.administratorDAO.deleteEntity(deletedAdministrator);
        }
    }

    @Test
    public final void administratorsShouldNotBeFoundByNotValidEmail()
            throws OffloadingEntitiesException
    {
        final String email = "not_valid_email";
        assert this.administratorDAO != null;
        final Collection<Administrator> foundAdministrators = this.administratorDAO.findUsersByEmail(email);
        Assert.assertTrue(foundAdministrators.isEmpty());
    }
}
