package by.itacademy.zuevvlad.cardpaymentproject.service.user;

import by.itacademy.zuevvlad.cardpaymentproject.dao.user.UserDAO;
import by.itacademy.zuevvlad.cardpaymentproject.entity.User;

public final class UserService extends AbstractUserService<User>
{
    public UserService(final UserDAO userDAO)
    {
        super(userDAO);
    }
}
