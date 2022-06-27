package by.itacademy.zuevvlad.cardpaymentproject.dao;

import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.DefiningExistingEntityException;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.FindingEntityException;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.OffloadingEntitiesException;
import by.itacademy.zuevvlad.cardpaymentproject.entity.PaymentCard;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.PersistenceException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public final class PaymentCardDAO extends DAO<PaymentCard>
{
    public PaymentCardDAO(final SessionFactory sessionFactory)
    {
        super(sessionFactory, PaymentCard.class);
    }

    public final boolean isNotDeletedPaymentCardWithGivenCardNumberExist(final String cardNumber)
            throws DefiningExistingEntityException
    {
        final SessionFactory sessionFactory = super.getSessionFactory();
        try(final Session session = sessionFactory.getCurrentSession())
        {
            final Transaction transaction = session.beginTransaction();
            final Query<Integer> queryToDefineExistingNotDeletedPaymentCardByCardNumber = session.createQuery(
                    PaymentCardDAO.HQL_QUERY_TO_DEFINE_EXISTING_NOT_DELETED_PAYMENT_CARD_BY_CARD_NUMBER, Integer.class);
            queryToDefineExistingNotDeletedPaymentCardByCardNumber.setParameter(
                    PaymentCardDAO.PARAMETER_NAME_OF_NUMBER_IN_QUERY_TO_DEFINE_EXISTING_NOT_DELETED_CARD_BY_NUMBER,
                    cardNumber);
            final Optional<Integer> optionalOfResult = queryToDefineExistingNotDeletedPaymentCardByCardNumber
                    .uniqueResultOptional();
            transaction.commit();
            return optionalOfResult.isPresent();
        }
        catch(final PersistenceException cause)
        {
            throw new DefiningExistingEntityException(cause);
        }
    }

    private static final String HQL_QUERY_TO_DEFINE_EXISTING_NOT_DELETED_PAYMENT_CARD_BY_CARD_NUMBER
            = "SELECT 1 FROM PaymentCard paymentCard WHERE paymentCard.cardNumber = :"
            + PaymentCardDAO.PARAMETER_NAME_OF_NUMBER_IN_QUERY_TO_DEFINE_EXISTING_NOT_DELETED_CARD_BY_NUMBER + " AND "
            + "paymentCard.deleted = false";
    private static final String PARAMETER_NAME_OF_NUMBER_IN_QUERY_TO_DEFINE_EXISTING_NOT_DELETED_CARD_BY_NUMBER
            = "cardNumber";

    public final Optional<PaymentCard> findOptionalOfNotDeletedPaymentCardByCardNumber(final String cardNumber)
            throws FindingEntityException
    {
        final SessionFactory sessionFactory = super.getSessionFactory();
        try(final Session session = sessionFactory.getCurrentSession())
        {
            final Transaction transaction = session.beginTransaction();
            final Query<PaymentCard> queryToFindNotDeletedPaymentCardByCardNumber = session.createQuery(
                    PaymentCardDAO.HQL_QUERY_TO_FIND_NOT_DELETED_PAYMENT_CARD_BY_CARD_NUMBER, PaymentCard.class);
            queryToFindNotDeletedPaymentCardByCardNumber.setParameter(
                    PaymentCardDAO.PARAMETER_NAME_OF_NUMBER_IN_QUERY_TO_FIND_NOT_DELETED_CARD_BY_NUMBER,
                    cardNumber);
            final Optional<PaymentCard> optionalOfFoundPaymentCard = queryToFindNotDeletedPaymentCardByCardNumber
                    .uniqueResultOptional();
            transaction.commit();
            return optionalOfFoundPaymentCard;
        }
        catch(final PersistenceException cause)
        {
            throw new FindingEntityException(cause);
        }
    }

    private static final String HQL_QUERY_TO_FIND_NOT_DELETED_PAYMENT_CARD_BY_CARD_NUMBER
            = "FROM PaymentCard paymentCard WHERE paymentCard.cardNumber = :"
            + PaymentCardDAO.PARAMETER_NAME_OF_NUMBER_IN_QUERY_TO_FIND_NOT_DELETED_CARD_BY_NUMBER
            + " AND paymentCard.deleted = false";
    private static final String PARAMETER_NAME_OF_NUMBER_IN_QUERY_TO_FIND_NOT_DELETED_CARD_BY_NUMBER = "cardNumber";

    public final Collection<PaymentCard> findPaymentCardsByCardNumber(final String cardNumber)
            throws OffloadingEntitiesException
    {
        final SessionFactory sessionFactory = super.getSessionFactory();
        try(final Session session = sessionFactory.getCurrentSession())
        {
            final Transaction transaction = session.beginTransaction();
            final Query<PaymentCard> queryToFindPaymentCardsByCardNumber = session.createQuery(
                    PaymentCardDAO.HQL_QUERY_TO_FIND_PAYMENT_CARDS_BY_CARD_NUMBER, PaymentCard.class);
            queryToFindPaymentCardsByCardNumber.setParameter(
                    PaymentCardDAO.PARAMETER_NAME__OF_CARD_NUMBER_IN_QUERY_TO_FIND_PAYMENT_CARDS_BY_CARD_NUMBER,
                    cardNumber);
            final List<PaymentCard> foundPaymentCards = queryToFindPaymentCardsByCardNumber.getResultList();
            transaction.commit();
            return foundPaymentCards;
        }
        catch(final PersistenceException cause)
        {
            throw new OffloadingEntitiesException(cause);
        }
    }

    private static final String HQL_QUERY_TO_FIND_PAYMENT_CARDS_BY_CARD_NUMBER = "FROM PaymentCard paymentCard WHERE "
            + "paymentCard.cardNumber = :"
            + PaymentCardDAO.PARAMETER_NAME__OF_CARD_NUMBER_IN_QUERY_TO_FIND_PAYMENT_CARDS_BY_CARD_NUMBER;
    private static final String PARAMETER_NAME__OF_CARD_NUMBER_IN_QUERY_TO_FIND_PAYMENT_CARDS_BY_CARD_NUMBER
            = "cardNumber";
}
