package by.itacademy.zuevvlad.cardpaymentproject.dao;

import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.OffloadingEntitiesException;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Client;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Payment;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.PersistenceException;
import java.util.Collection;
import java.util.List;
//Во всех таблицах нужны доп тригеры: нельзя обновить статус удаление у клиента если псевдо удален его банковский счет и так везде
public final class PaymentDAO extends DAO<Payment>
{
    public PaymentDAO(final SessionFactory sessionFactory)
    {
        super(sessionFactory, Payment.class);
    }

    public final Collection<Payment> findNotDeletedPaymentsAssociatedWithGivenClient(final Client client)
            throws OffloadingEntitiesException
    {
        final SessionFactory sessionFactory = super.getSessionFactory();
        try(final Session session = sessionFactory.getCurrentSession())
        {
            final Transaction transaction = session.beginTransaction();
            final Query<Payment> queryToFindNotDeletedPaymentsAssociatedWithClient = session.createNativeQuery(
                    PaymentDAO.SQL_QUERY_TO_FIND_NOT_DELETED_PAYMENTS_ASSOCIATED_WITH_CLIENT, Payment.class);
            queryToFindNotDeletedPaymentsAssociatedWithClient.setParameter(
                    PaymentDAO.PARAMETER_NAME_OF_CLIENT_ID_TO_FIND_ASSOCIATED_PAYMENTS, client.getId());
            final List<Payment> foundPayments = queryToFindNotDeletedPaymentsAssociatedWithClient.getResultList();
            transaction.commit();
            return foundPayments;
        }
        catch(final PersistenceException cause)
        {
            throw new OffloadingEntitiesException(cause);
        }
    }

    private static final String SQL_QUERY_TO_FIND_NOT_DELETED_PAYMENTS_ASSOCIATED_WITH_CLIENT
            = "SELECT payments.id, payments.is_deleted, payments.card_id_of_sender, payments.card_id_of_receiver,"
            + " payments.money, payments.date"
            + " FROM payments INNER JOIN payment_cards"
            + " ON payments.card_id_of_sender = payment_cards.id OR payments.card_id_of_receiver = payment_cards.id"
            + " INNER JOIN clients"
            + " ON payment_cards.client_id = clients.id"
            + " WHERE payments.is_deleted = false AND clients.id = :"
            + PaymentDAO.PARAMETER_NAME_OF_CLIENT_ID_TO_FIND_ASSOCIATED_PAYMENTS;
    private static final String PARAMETER_NAME_OF_CLIENT_ID_TO_FIND_ASSOCIATED_PAYMENTS = "client_id";

    public final Collection<Payment> findNotDeletedPaymentsSentByGivenClient(final Client client)
            throws OffloadingEntitiesException
    {
        final SessionFactory sessionFactory = super.getSessionFactory();
        try(final Session session = sessionFactory.getCurrentSession())
        {
            final Transaction transaction = session.beginTransaction();
            final Query<Payment> queryToFindNotDeletedPaymentsSentByClient = session.createNativeQuery(
                    PaymentDAO.SQL_QUERY_TO_FIND_NOT_DELETED_PAYMENTS_SENT_BY_CLIENT, Payment.class);
            queryToFindNotDeletedPaymentsSentByClient.setParameter(
                    PaymentDAO.PARAMETER_NAME_OF_CLIENT_ID_TO_FIND_SENT_PAYMENTS, client.getId());
            final List<Payment> foundPayments = queryToFindNotDeletedPaymentsSentByClient.getResultList();
            transaction.commit();
            return foundPayments;
        }
        catch(final PersistenceException cause)
        {
            throw new OffloadingEntitiesException(cause);
        }
    }

    private static final String SQL_QUERY_TO_FIND_NOT_DELETED_PAYMENTS_SENT_BY_CLIENT
            = "SELECT payments.id, payments.card_id_of_sender, payments.card_id_of_receiver, payments.money, payments.date, payments.is_deleted"
            + " FROM payments WHERE payments.is_deleted = false AND"
            + " payments.card_id_of_sender IN (SELECT payment_cards.id FROM payment_cards"
            + " INNER JOIN clients ON payment_cards.client_id = clients.id"
            + " WHERE clients.id = :" + PaymentDAO.PARAMETER_NAME_OF_CLIENT_ID_TO_FIND_SENT_PAYMENTS + ")";
    private static final String PARAMETER_NAME_OF_CLIENT_ID_TO_FIND_SENT_PAYMENTS = "client_id";

    public final Collection<Payment> findNotDeletedPaymentsReceivedByGivenClient(final Client client)
            throws OffloadingEntitiesException
    {
        final SessionFactory sessionFactory = super.getSessionFactory();
        try(final Session session = sessionFactory.getCurrentSession())
        {
            final Transaction transaction = session.beginTransaction();
            final Query<Payment> queryToFindNotDeletedPaymentsReceivedByGivenClient = session.createNativeQuery(
                    PaymentDAO.SQL_QUERY_TO_FIND_NOT_DELETED_PAYMENTS_RECEIVED_BY_CLIENT, Payment.class);
            queryToFindNotDeletedPaymentsReceivedByGivenClient.setParameter(
                    PaymentDAO.PARAMETER_NAME_OF_CLIENT_ID_TO_FIND_RECEIVED_PAYMENTS, client.getId());
            final List<Payment> foundPayments = queryToFindNotDeletedPaymentsReceivedByGivenClient.getResultList();
            transaction.commit();;
            return foundPayments;
        }
        catch(final PersistenceException cause)
        {
            throw new OffloadingEntitiesException(cause);
        }
    }

    private static final String SQL_QUERY_TO_FIND_NOT_DELETED_PAYMENTS_RECEIVED_BY_CLIENT
            = "SELECT payments.id, payments.card_id_of_sender, payments.card_id_of_receiver, payments.money, payments.date, payments.is_deleted"
            + " FROM payments WHERE payments.is_deleted = false AND"
            + " payments.card_id_of_receiver IN (SELECT payment_cards.id FROM payment_cards"
            + " INNER JOIN clients ON payment_cards.client_id = clients.id"
            + " WHERE clients.id = :" + PaymentDAO.PARAMETER_NAME_OF_CLIENT_ID_TO_FIND_RECEIVED_PAYMENTS + ")";
    private static final String PARAMETER_NAME_OF_CLIENT_ID_TO_FIND_RECEIVED_PAYMENTS = "client_id";
}
