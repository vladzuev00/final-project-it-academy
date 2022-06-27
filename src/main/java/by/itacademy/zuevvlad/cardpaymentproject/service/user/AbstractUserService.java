package by.itacademy.zuevvlad.cardpaymentproject.service.user;

import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.DefiningExistingEntityException;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.FindingEntityException;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.OffloadingEntitiesException;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.UpdatingEntityException;
import by.itacademy.zuevvlad.cardpaymentproject.dao.user.AbstractUserDAO;
import by.itacademy.zuevvlad.cardpaymentproject.entity.User;
import by.itacademy.zuevvlad.cardpaymentproject.service.Service;
import by.itacademy.zuevvlad.cardpaymentproject.service.exception.NoSuchEntityException;
import by.itacademy.zuevvlad.cardpaymentproject.service.exception.changepassword.NewPasswordAndItsRepetitionNotEqualException;
import by.itacademy.zuevvlad.cardpaymentproject.service.exception.changepassword.WrongOldPasswordException;

import java.util.Collection;
import java.util.Optional;

public abstract class AbstractUserService<TypeOfUser extends User> extends Service<TypeOfUser>
{
    protected AbstractUserService(final AbstractUserDAO<TypeOfUser> userDAO)
    {
        super(userDAO);
    }

    public final TypeOfUser findNotDeletedUserByEmail(final String emailOfFoundUser)
            throws FindingEntityException, NoSuchEntityException
    {
        final AbstractUserDAO<TypeOfUser> userDAO = (AbstractUserDAO<TypeOfUser>)super.getDao();
        final Optional<TypeOfUser> optionalOfFoundUser = userDAO.findNotDeletedUserByEmail(emailOfFoundUser);
        if(optionalOfFoundUser.isEmpty())
        {
            throw new NoSuchEntityException("Impossible to find user by given email '" + emailOfFoundUser + "'.");
        }
        return optionalOfFoundUser.get();
    }

    public final Optional<TypeOfUser> findOptionalOfNotDeletedUserByEmail(final String emailOfFoundUser)
            throws FindingEntityException
    {
        final AbstractUserDAO<TypeOfUser> abstractUserDAO = (AbstractUserDAO<TypeOfUser>)super.getDao();
        return abstractUserDAO.findNotDeletedUserByEmail(emailOfFoundUser);
    }

    public final Collection<TypeOfUser> findUsersByPassword(final String passwordOfFoundUser)
            throws OffloadingEntitiesException
    {
        final AbstractUserDAO<TypeOfUser> userDAO = (AbstractUserDAO<TypeOfUser>)super.getDao();
        return userDAO.findNotDeletedUsersByPassword(passwordOfFoundUser);
    }

    public final boolean isNotDeletedUserWithGivenEmailExist(final String emailOfResearchUser)
            throws DefiningExistingEntityException
    {
        final AbstractUserDAO<TypeOfUser> userDAO = (AbstractUserDAO<TypeOfUser>)super.getDao();
        return userDAO.isNotDeletedUserWithGivenEmailExist(emailOfResearchUser);
    }

    public final boolean isCorrectPasswordForGivenEmail(final String researchPassword, final String email)
            throws DefiningExistingEntityException
    {
        final AbstractUserDAO<TypeOfUser> userDAO = (AbstractUserDAO<TypeOfUser>)super.getDao();
        return userDAO.isCorrectPasswordForGivenEmail(researchPassword, email);
    }

    public final Collection<TypeOfUser> findUsersByEmail(final String emailOfFoundUsers)
            throws OffloadingEntitiesException
    {
        final AbstractUserDAO<TypeOfUser> userDAO = (AbstractUserDAO<TypeOfUser>)super.getDao();
        return userDAO.findUsersByEmail(emailOfFoundUsers);
    }

    public final void changePassword(final String oldPassword, final String newPassword,
                                     final String repeatedNewPassword, final TypeOfUser updatedUser)
            throws NewPasswordAndItsRepetitionNotEqualException, WrongOldPasswordException, UpdatingEntityException
    {
        if(!newPassword.equals(repeatedNewPassword))
        {
            throw new NewPasswordAndItsRepetitionNotEqualException("New password '" + newPassword
                    + "' and its repetition '" + repeatedNewPassword + "' aren't equal.");
        }
        final String currentPasswordOfUpdatedUser = updatedUser.getPassword();
        if(!currentPasswordOfUpdatedUser.equals(oldPassword))
        {
            throw new WrongOldPasswordException("Wrong old password '" + oldPassword + "' of user '" + updatedUser
                    + "'.");
        }
        updatedUser.setPassword(newPassword);
        final AbstractUserDAO<TypeOfUser> userDAO = (AbstractUserDAO<TypeOfUser>)super.getDao();
        userDAO.updatePassword(updatedUser);
    }
}
