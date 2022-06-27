package by.itacademy.zuevvlad.cardpaymentproject.dao.user;

import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.UpdatingEntityException;
import by.itacademy.zuevvlad.cardpaymentproject.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.PersistenceException;

public final class UserDAO extends AbstractUserDAO<User>
{
    public UserDAO(final SessionFactory sessionFactory)
    {
        super(sessionFactory, User.class);
    }
}
