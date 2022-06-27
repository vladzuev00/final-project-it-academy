package by.itacademy.zuevvlad.cardpaymentproject.dao;

import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.DefiningExistingEntityException;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.FindingEntityException;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.OffloadingEntitiesException;
import by.itacademy.zuevvlad.cardpaymentproject.entity.BankAccount;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.PersistenceException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public final class BankAccountDAO extends DAO<BankAccount>
{
    public BankAccountDAO(final SessionFactory sessionFactory)
    {
        super(sessionFactory, BankAccount.class);
    }

    public final Optional<BankAccount> findNotDeletedBankAccountByNumber(final String numberOfFoundBankAccount)
            throws FindingEntityException
    {
        final SessionFactory sessionFactory = super.getSessionFactory();
        try(final Session session = sessionFactory.getCurrentSession())
        {
            final Transaction transaction = session.beginTransaction();
            final Query<BankAccount> queryToFindBankAccountByNumber = session.createQuery(
                    BankAccountDAO.HQL_QUERY_TO_FIND_BANK_ACCOUNT_BY_NUMBER, BankAccount.class);
            queryToFindBankAccountByNumber.setParameter(
                    BankAccountDAO.PARAMETER_NAME_OF_NUMBER_IN_QUERY_TO_FIND_BANK_ACCOUNT_BY_NUMBER,
                    numberOfFoundBankAccount);
            final Optional<BankAccount> optionalOfFoundBankAccount = queryToFindBankAccountByNumber
                    .uniqueResultOptional();
            transaction.commit();
            return optionalOfFoundBankAccount;
        }
        catch(final PersistenceException cause)
        {
            throw new FindingEntityException(cause);
        }
    }

    private static final String HQL_QUERY_TO_FIND_BANK_ACCOUNT_BY_NUMBER = "FROM BankAccount bankAccount"
            + " WHERE bankAccount.number = :"
            + BankAccountDAO.PARAMETER_NAME_OF_NUMBER_IN_QUERY_TO_FIND_BANK_ACCOUNT_BY_NUMBER
            + " AND bankAccount.deleted = false";
    private static final String PARAMETER_NAME_OF_NUMBER_IN_QUERY_TO_FIND_BANK_ACCOUNT_BY_NUMBER = "number";

    public final boolean isNotDeletedBankAccountWithGivenNumberExist(final String number)
            throws DefiningExistingEntityException
    {
        final SessionFactory sessionFactory = super.getSessionFactory();
        try(final Session session = sessionFactory.getCurrentSession())
        {
            final Transaction transaction = session.beginTransaction();
            final Query<Integer> queryToDefineExistingNotDeletedBankAccountByNumber = session.createQuery(
                    BankAccountDAO.HQL_QUERY_TO_DEFINE_EXISTING_NOT_DELETED_BANK_ACCOUNT_BY_NUMBER, Integer.class);
            queryToDefineExistingNotDeletedBankAccountByNumber.setParameter(
                    BankAccountDAO.PARAMETER_NAME_OF_NUMBER_IN_QUERY_TO_DEFINE_EXISTING_NOT_DELETED_BANK_ACCOUNT_BY_NUMBER,
                    number);
            final Optional<Integer> optionalOfQueryResult = queryToDefineExistingNotDeletedBankAccountByNumber
                    .uniqueResultOptional();
            transaction.commit();
            return optionalOfQueryResult.isPresent();
        }
        catch(final PersistenceException cause)
        {
            throw new DefiningExistingEntityException(cause);
        }
    }

    private static final String HQL_QUERY_TO_DEFINE_EXISTING_NOT_DELETED_BANK_ACCOUNT_BY_NUMBER
            = "SELECT 1 FROM BankAccount bankAccount WHERE bankAccount.number = :"
            + BankAccountDAO.PARAMETER_NAME_OF_NUMBER_IN_QUERY_TO_DEFINE_EXISTING_NOT_DELETED_BANK_ACCOUNT_BY_NUMBER
            + " AND bankAccount.deleted = false";
    private static final String PARAMETER_NAME_OF_NUMBER_IN_QUERY_TO_DEFINE_EXISTING_NOT_DELETED_BANK_ACCOUNT_BY_NUMBER
            = "number";

    public final Collection<BankAccount> findBankAccountsByNumber(final String numberOfFoundBankAccounts)
            throws OffloadingEntitiesException
    {
        final SessionFactory sessionFactory = super.getSessionFactory();
        try(final Session session = sessionFactory.getCurrentSession())
        {
            final Transaction transaction = session.beginTransaction();
            final Query<BankAccount> queryToFindBankAccountsByNumber = session.createQuery(
                    BankAccountDAO.HQL_QUERY_TO_FIND_BANK_ACCOUNTS_BY_NUMBER, BankAccount.class);
            queryToFindBankAccountsByNumber.setParameter(
                    BankAccountDAO.PARAMETER_NAME_OF_NUMBER_IN_QUERY_TO_FIND_BANK_ACCOUNTS_BY_NUMBER,
                    numberOfFoundBankAccounts);
            final List<BankAccount> foundBankAccounts = queryToFindBankAccountsByNumber.getResultList();
            transaction.commit();
            return foundBankAccounts;
        }
        catch(final PersistenceException cause)
        {
            throw new OffloadingEntitiesException(cause);
        }
    }

    private static final String HQL_QUERY_TO_FIND_BANK_ACCOUNTS_BY_NUMBER = "FROM BankAccount bankAccount"
            + " WHERE bankAccount.number = :"
            + BankAccountDAO.PARAMETER_NAME_OF_NUMBER_IN_QUERY_TO_FIND_BANK_ACCOUNTS_BY_NUMBER;
    private static final String PARAMETER_NAME_OF_NUMBER_IN_QUERY_TO_FIND_BANK_ACCOUNTS_BY_NUMBER = "number";
}
