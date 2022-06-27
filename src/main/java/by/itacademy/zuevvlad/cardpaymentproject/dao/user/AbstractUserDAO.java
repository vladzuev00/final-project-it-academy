package by.itacademy.zuevvlad.cardpaymentproject.dao.user;

import by.itacademy.zuevvlad.cardpaymentproject.dao.DAO;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.DefiningExistingEntityException;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.FindingEntityException;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.OffloadingEntitiesException;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.UpdatingEntityException;
import by.itacademy.zuevvlad.cardpaymentproject.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.PersistenceException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public abstract class AbstractUserDAO<TypeOfUser extends User> extends DAO<TypeOfUser>
{
    protected AbstractUserDAO(final SessionFactory sessionFactory, final Class<TypeOfUser> typeOfUser)
    {
        super(sessionFactory, typeOfUser);
    }

    public final Optional<TypeOfUser> findNotDeletedUserByEmail(final String emailOfFoundUser)
            throws FindingEntityException
    {
        final SessionFactory sessionFactory = super.getSessionFactory();
        try(final Session session = sessionFactory.getCurrentSession())
        {
            final Transaction transaction = session.beginTransaction();

            final Class<TypeOfUser> typeOfUser = super.getTypeOfStoredEntity();
            final String parameterNameOfEmail = "email";
            final String hqlQueryToSelectUserByEmail = "FROM " + typeOfUser.getName() + " user WHERE user.email = :"
                    + parameterNameOfEmail + " AND user.deleted = false";
            final Query<TypeOfUser> queryToSelectUserByEmail = session.createQuery(hqlQueryToSelectUserByEmail,
                    typeOfUser);
            queryToSelectUserByEmail.setParameter(parameterNameOfEmail, emailOfFoundUser);

            final Optional<TypeOfUser> optionalOfFoundUser = queryToSelectUserByEmail.uniqueResultOptional();
            transaction.commit();
            return optionalOfFoundUser;
        }
        catch(final PersistenceException cause)
        {
            throw new FindingEntityException(cause);
        }
    }

    public final Collection<TypeOfUser> findNotDeletedUsersByPassword(final String passwordOfFoundUsers)
            throws OffloadingEntitiesException
    {
        final SessionFactory sessionFactory = super.getSessionFactory();
        try(final Session session = sessionFactory.getCurrentSession())
        {
            final Transaction transaction = session.beginTransaction();

            final Class<TypeOfUser> typeOfUser = super.getTypeOfStoredEntity();
            final String parameterNameOfPassword = "password";
            final String hqlQueryToSelectUsersByPassword = "FROM " + typeOfUser.getName() + " user"
                    + " WHERE user.password = :" + parameterNameOfPassword + " AND user.deleted = false";
            final Query<TypeOfUser> queryToSelectUsersByPassword = session.createQuery(hqlQueryToSelectUsersByPassword,
                    typeOfUser);
            queryToSelectUsersByPassword.setParameter(parameterNameOfPassword, passwordOfFoundUsers);

            final List<TypeOfUser> foundUsers = queryToSelectUsersByPassword.getResultList();
            transaction.commit();
            return foundUsers;
        }
        catch(final PersistenceException cause)
        {
            throw new OffloadingEntitiesException(cause);
        }
    }

    public final boolean isNotDeletedUserWithGivenEmailExist(final String emailOfResearchUser)
            throws DefiningExistingEntityException
    {
        final SessionFactory sessionFactory = super.getSessionFactory();
        try(final Session session = sessionFactory.getCurrentSession())
        {
            final Transaction transaction = session.beginTransaction();

            final Class<TypeOfUser> typeOfUser = super.getTypeOfStoredEntity();
            final String parameterNameOfEmail = "email";
            final String hqlQueryToDefineExistingOfNotDeletedUserByEmail = "SELECT 1 FROM " + typeOfUser.getName()
                    + " user WHERE user.email = :" + parameterNameOfEmail + " AND user.deleted = false";
            final Query<Integer> queryToDefineExistingOfNotDeleteUserByEmail = session.createQuery(
                    hqlQueryToDefineExistingOfNotDeletedUserByEmail, Integer.class);
            queryToDefineExistingOfNotDeleteUserByEmail.setParameter(parameterNameOfEmail, emailOfResearchUser);

            final Integer result = queryToDefineExistingOfNotDeleteUserByEmail.uniqueResult();
            transaction.commit();
            return result != null;
        }
        catch(final PersistenceException cause)
        {
            throw new DefiningExistingEntityException(cause);
        }
    }

    public final boolean isCorrectPasswordForGivenEmail(final String researchPassword, final String email)
            throws DefiningExistingEntityException
    {
        final SessionFactory sessionFactory = super.getSessionFactory();
        try(final Session session = sessionFactory.getCurrentSession())
        {
            final Transaction transaction = session.beginTransaction();

            final Class<TypeOfUser> typeOfUser = super.getTypeOfStoredEntity();
            final String parameterNameOfEmail = "email";
            final String parameterNameOfPassword = "password";
            final String hqlQueryToDefineCorrectingPasswordForGivenEmail = "SELECT 1 FROM " + typeOfUser.getName()
                    + " user WHERE user.email = :" + parameterNameOfEmail + " AND user.password = :"
                    + parameterNameOfPassword + " AND user.deleted = false";
            final Query<Integer> queryToDefineCorrectingPasswordForGivenEmail = session.createQuery(
                    hqlQueryToDefineCorrectingPasswordForGivenEmail, Integer.class);
            queryToDefineCorrectingPasswordForGivenEmail.setParameter(parameterNameOfEmail, email);
            queryToDefineCorrectingPasswordForGivenEmail.setParameter(parameterNameOfPassword, researchPassword);

            final Integer result = queryToDefineCorrectingPasswordForGivenEmail.uniqueResult();
            transaction.commit();
            return result != null;
        }
        catch(final PersistenceException cause)
        {
            throw new DefiningExistingEntityException(cause);
        }
    }

    public final Collection<TypeOfUser> findUsersByEmail(final String emailOfFoundUsers)
            throws OffloadingEntitiesException
    {
        final SessionFactory sessionFactory = super.getSessionFactory();
        try(final Session session = sessionFactory.getCurrentSession())
        {
            final Transaction transaction = session.beginTransaction();
            final Class<TypeOfUser> typeOfUser = super.getTypeOfStoredEntity();
            final String parameterNameOfEmail = "email";
            final String hqlQueryToFindUsersByEmail = "FROM " + typeOfUser.getName() + " user WHERE user.email = :"
                    + parameterNameOfEmail;
            final Query<TypeOfUser> queryToFindUsersByEmail = session.createQuery(hqlQueryToFindUsersByEmail,
                    typeOfUser);
            queryToFindUsersByEmail.setParameter(parameterNameOfEmail, emailOfFoundUsers);
            final List<TypeOfUser> foundUsers = queryToFindUsersByEmail.getResultList();
            transaction.commit();
            return foundUsers;
        }
        catch(final PersistenceException cause)
        {
            throw new OffloadingEntitiesException(cause);
        }
    }

    public final void updatePassword(final User updatedUser)    //TODO: test the method
            throws UpdatingEntityException
    {
        final SessionFactory sessionFactory = super.getSessionFactory();
        try(final Session session = sessionFactory.getCurrentSession())
        {
            final Transaction transaction = session.beginTransaction();
            final Class<TypeOfUser> typeOfUser = super.getTypeOfStoredEntity();
            final String parameterNameOfPasswordInQueryToUpdatePassword = "password";
            final String parameterNameOfIdInQueryToUpdatePassword = "id";
            final String hqlQueryToUpdatePassword = "UPDATE " + typeOfUser.getName() + " user SET user.password = :"
                    + parameterNameOfPasswordInQueryToUpdatePassword + " WHERE user.id = :"
                    + parameterNameOfIdInQueryToUpdatePassword;
            final Query<?> queryToUpdatePassword = session.createQuery(hqlQueryToUpdatePassword);
            queryToUpdatePassword.setParameter(parameterNameOfPasswordInQueryToUpdatePassword,
                    updatedUser.getPassword());
            queryToUpdatePassword.setParameter(parameterNameOfIdInQueryToUpdatePassword, updatedUser.getId());
            queryToUpdatePassword.executeUpdate();
            transaction.commit();
        }
        catch(final PersistenceException cause)
        {
            throw new UpdatingEntityException(cause);
        }
    }
}
