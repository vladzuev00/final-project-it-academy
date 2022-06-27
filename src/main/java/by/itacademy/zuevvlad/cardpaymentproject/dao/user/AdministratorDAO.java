package by.itacademy.zuevvlad.cardpaymentproject.dao.user;

import by.itacademy.zuevvlad.cardpaymentproject.entity.Administrator;
import org.hibernate.SessionFactory;

public final class AdministratorDAO extends AbstractUserDAO<Administrator>
{
    public AdministratorDAO(final SessionFactory sessionFactory)
    {
        super(sessionFactory, Administrator.class);
    }
}
