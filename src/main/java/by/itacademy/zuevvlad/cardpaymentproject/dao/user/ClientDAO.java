package by.itacademy.zuevvlad.cardpaymentproject.dao.user;

import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.DAOException;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.DefiningExistingEntityException;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.FindingEntityException;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.UpdatingEntityException;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Client;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Payment;
import by.itacademy.zuevvlad.cardpaymentproject.entity.PaymentCard;
import by.itacademy.zuevvlad.cardpaymentproject.service.cryptographer.Cryptographer;
import by.itacademy.zuevvlad.cardpaymentproject.service.cryptographer.StringToStringCryptographer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import javax.persistence.ParameterMode;
import javax.persistence.PersistenceException;
import javax.persistence.StoredProcedureQuery;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class ClientDAO extends AbstractUserDAO<Client>
{
    private final ClientStoredProcedureQueryConfigurer clientStoredProcedureQueryConfigurer;

    public ClientDAO(final SessionFactory sessionFactory)
    {
        super(sessionFactory, Client.class);
        this.clientStoredProcedureQueryConfigurer = new ClientStoredProcedureQueryConfigurer();
    }

    /*
        обновление только через хранимую процедуру. Через тригеры, как везде, нельзя из-за столбца is_deleted
        и уникальности phone_number среди клиентов, у которых is_deleted = false.
     */
    @Override
    public final void updateEntity(final Client updatedClient)
            throws UpdatingEntityException
    {
        final SessionFactory sessionFactory = super.getSessionFactory();
        try(final Session session = sessionFactory.getCurrentSession())
        {
            final Transaction transaction = session.beginTransaction();
            final StoredProcedureQuery storedProcedureQueryToUpdateClient = this.clientStoredProcedureQueryConfigurer
                    .configureStoredProcedureQueryToUpdateClient(session, updatedClient);
            storedProcedureQueryToUpdateClient.executeUpdate();
            transaction.commit();
        }
        catch(final PersistenceException cause)
        {
            throw new UpdatingEntityException(cause);
        }
    }

    /*
        обновление только через хранимую процедуру. Через тригеры, как везде, нельзя из-за столбца is_deleted
        и уникальности phone_number среди клиентов, у которых is_deleted = false.
     */
    @Override
    public final void updateDeletedStatusOfEntity(final long idOfUpdatedEntity, final boolean newDeleted)
            throws UpdatingEntityException
    {
        final SessionFactory sessionFactory = super.getSessionFactory();
        try(final Session session = sessionFactory.getCurrentSession())
        {
            final Transaction transaction = session.beginTransaction();
            final StoredProcedureQuery storedProcedureQueryToUpdateDeletedStatusOfClient
                    = this.clientStoredProcedureQueryConfigurer
                    .configureStoredProcedureQueryToUpdateDeletedStatusOfClient(session, idOfUpdatedEntity, newDeleted);
            storedProcedureQueryToUpdateDeletedStatusOfClient.executeUpdate();
            transaction.commit();
        }
        catch(final PersistenceException cause)
        {
            throw new UpdatingEntityException(cause);
        }
    }

    public final boolean isNotDeletedClientWithGivenPhoneNumberExist(final String phoneNumber)
            throws DefiningExistingEntityException
    {
        final SessionFactory sessionFactory = super.getSessionFactory();
        try(final Session session = sessionFactory.getCurrentSession())
        {
            final Transaction transaction = session.beginTransaction();
            final Query<Integer> queryToDefineExistingOfNotDeletedClientByPhoneNumber = session.createQuery(
                    ClientDAO.HQL_QUERY_TO_DEFINE_EXISTING_OF_NOT_DELETED_CLIENT_BY_PHONE_NUMBER, Integer.class);
            queryToDefineExistingOfNotDeletedClientByPhoneNumber.setParameter(
                    ClientDAO.NAME_OF_PARAMETER_OF_PHONE_NUMBER_TO_DEFINE_EXISTING_OF_NOT_DELETED_CLIENT_BY_PHONE_NUMBER,
                    phoneNumber);
            final Optional<Integer> optionalOfResult = queryToDefineExistingOfNotDeletedClientByPhoneNumber
                    .uniqueResultOptional();
            transaction.commit();
            return optionalOfResult.isPresent();
        }
        catch(final PersistenceException cause)
        {
            throw new DefiningExistingEntityException(cause);
        }
    }

    private static final String HQL_QUERY_TO_DEFINE_EXISTING_OF_NOT_DELETED_CLIENT_BY_PHONE_NUMBER = "SELECT 1"
            + " FROM Client client WHERE client.phoneNumber = :"
            + ClientDAO.NAME_OF_PARAMETER_OF_PHONE_NUMBER_TO_DEFINE_EXISTING_OF_NOT_DELETED_CLIENT_BY_PHONE_NUMBER
            + " AND client.deleted = false";
    private static final String NAME_OF_PARAMETER_OF_PHONE_NUMBER_TO_DEFINE_EXISTING_OF_NOT_DELETED_CLIENT_BY_PHONE_NUMBER
            = "phoneNumber";

    public final Optional<Client> findOptionalOfNotDeletedClientByPhoneNumber(final String phoneNumber)
            throws FindingEntityException
    {
        final SessionFactory sessionFactory = super.getSessionFactory();
        try(final Session session = sessionFactory.getCurrentSession())
        {
            final Transaction transaction = session.beginTransaction();
            final Query<Client> queryToFindNotDeletedClientByPhoneNumber = session.createQuery(
                    ClientDAO.HQL_QUERY_TO_FIND_NOT_DELETED_CLIENT_BY_PHONE_NUMBER, Client.class);
            queryToFindNotDeletedClientByPhoneNumber.setParameter(
                    ClientDAO.NAME_OF_PARAMETER_OF_PHONE_NUMBER_TO_FIND_NOT_DELETED_CLIENT_BY_PHONE_NUMBER,
                    phoneNumber);
            final Optional<Client> optionalOfFoundClient = queryToFindNotDeletedClientByPhoneNumber
                    .uniqueResultOptional();
            transaction.commit();
            return optionalOfFoundClient;
        }
        catch(final PersistenceException cause)
        {
            throw new FindingEntityException(cause);
        }
    }

    private static final String HQL_QUERY_TO_FIND_NOT_DELETED_CLIENT_BY_PHONE_NUMBER = "FROM Client client WHERE"
            + " client.phoneNumber = :"
            + ClientDAO.NAME_OF_PARAMETER_OF_PHONE_NUMBER_TO_FIND_NOT_DELETED_CLIENT_BY_PHONE_NUMBER + " AND"
            + " client.deleted = false";
    private static final String NAME_OF_PARAMETER_OF_PHONE_NUMBER_TO_FIND_NOT_DELETED_CLIENT_BY_PHONE_NUMBER
            = "phoneNumber";

    public final void updatePersonalDataOfClient(final Client updatedClient)     //TODO: test the method
            throws UpdatingEntityException
    {
        final SessionFactory sessionFactory = super.getSessionFactory();
        try(final Session session = sessionFactory.getCurrentSession())
        {
            final Transaction transaction = session.beginTransaction();
            final Query<?> queryToUpdatePersonalDataOfClient = session.createQuery(
                    ClientDAO.HQL_QUERY_TO_UPDATE_PERSONAL_DATA_OF_CLIENT);

            queryToUpdatePersonalDataOfClient.setParameter(
                    ClientDAO.PARAMETER_NAME_OF_EMAIL_IN_QUERY_TO_UPDATE_PERSONAL_DATA_OF_CLIENT,
                    updatedClient.getEmail());
            queryToUpdatePersonalDataOfClient.setParameter(
                    ClientDAO.PARAMETER_NAME_OF_NAME_IN_QUERY_TO_UPDATE_PERSONAL_DATA_OF_CLIENT,
                    updatedClient.getName());
            queryToUpdatePersonalDataOfClient.setParameter(
                    ClientDAO.PARAMETER_NAME_OF_SURNAME_IN_QUERY_TO_UPDATE_PERSONAL_DATA_OF_CLIENT,
                    updatedClient.getSurname());
            queryToUpdatePersonalDataOfClient.setParameter(
                    ClientDAO.PARAMETER_NAME_OF_PATRONYMIC_IN_QUERY_TO_UPDATE_PERSONAL_DATA_OF_CLIENT,
                    updatedClient.getPatronymic());
            queryToUpdatePersonalDataOfClient.setParameter(
                    ClientDAO.PARAMETER_NAME_OF_PHONE_NUMBER_IN_QUERY_TO_UPDATE_PERSONAL_DATA_OF_CLIENT,
                    updatedClient.getPhoneNumber());
            queryToUpdatePersonalDataOfClient.setParameter(
                    ClientDAO.PARAMETER_NAME_OF_ID_IN_QUERY_TO_UPDATE_PERSONAL_DATA_OF_CLIENT, updatedClient.getId());

            queryToUpdatePersonalDataOfClient.executeUpdate();

            transaction.commit();
        }
        catch(final PersistenceException cause)
        {
            throw new UpdatingEntityException(cause);
        }
    }

    private static final String HQL_QUERY_TO_UPDATE_PERSONAL_DATA_OF_CLIENT = "UPDATE Client client"
            + " SET client.email = :" + ClientDAO.PARAMETER_NAME_OF_EMAIL_IN_QUERY_TO_UPDATE_PERSONAL_DATA_OF_CLIENT
            + ", client.name = :" + ClientDAO.PARAMETER_NAME_OF_NAME_IN_QUERY_TO_UPDATE_PERSONAL_DATA_OF_CLIENT
            + ", client.surname = :" + ClientDAO.PARAMETER_NAME_OF_SURNAME_IN_QUERY_TO_UPDATE_PERSONAL_DATA_OF_CLIENT
            + ", client.patronymic = :" + ClientDAO.PARAMETER_NAME_OF_PATRONYMIC_IN_QUERY_TO_UPDATE_PERSONAL_DATA_OF_CLIENT
            + ", client.phoneNumber = :" + ClientDAO.PARAMETER_NAME_OF_PHONE_NUMBER_IN_QUERY_TO_UPDATE_PERSONAL_DATA_OF_CLIENT
            + " WHERE client.id = :" + ClientDAO.PARAMETER_NAME_OF_ID_IN_QUERY_TO_UPDATE_PERSONAL_DATA_OF_CLIENT;
    private static final String PARAMETER_NAME_OF_EMAIL_IN_QUERY_TO_UPDATE_PERSONAL_DATA_OF_CLIENT = "email";
    private static final String PARAMETER_NAME_OF_NAME_IN_QUERY_TO_UPDATE_PERSONAL_DATA_OF_CLIENT = "name";
    private static final String PARAMETER_NAME_OF_SURNAME_IN_QUERY_TO_UPDATE_PERSONAL_DATA_OF_CLIENT = "surname";
    private static final String PARAMETER_NAME_OF_PATRONYMIC_IN_QUERY_TO_UPDATE_PERSONAL_DATA_OF_CLIENT = "patronymic";
    private static final String PARAMETER_NAME_OF_PHONE_NUMBER_IN_QUERY_TO_UPDATE_PERSONAL_DATA_OF_CLIENT = "phoneNumber";
    private static final String PARAMETER_NAME_OF_ID_IN_QUERY_TO_UPDATE_PERSONAL_DATA_OF_CLIENT = "id";

    public final Collection<Payment> findAllReceivedPaymentsOfClient(final Client client)  //TODO: test the method
            throws DAOException
    {
        final SessionFactory sessionFactory = super.getSessionFactory();
        try(final Session session = sessionFactory.getCurrentSession())
        {
            final Transaction transaction = session.beginTransaction();
            final Query<Payment> queryToFindAllReceivedPaymentsOfClient = session.createNativeQuery(
                    ClientDAO.SQL_QUERY_TO_FIND_ALL_RECEIVED_PAYMENTS_OF_CLIENT, Payment.class);
            queryToFindAllReceivedPaymentsOfClient.setParameter(
                    ClientDAO.PARAMETER_NAME_OF_CLIENT_ID_TO_FIND_ALL_RECEIVED_PAYMENTS, client.getId());
            final List<Payment> allReceivedPaymentsOfClient = queryToFindAllReceivedPaymentsOfClient.getResultList();
            transaction.commit();
            return allReceivedPaymentsOfClient;
        }
        catch(final PersistenceException cause)
        {
            throw new DAOException(cause);
        }
    }

    private static final String SQL_QUERY_TO_FIND_ALL_RECEIVED_PAYMENTS_OF_CLIENT
            = "SELECT payments.id, payments.card_id_of_sender, payments.card_id_of_receiver, payments.money, "
            + "payments.date, payments.is_deleted "
            + "FROM payments INNER JOIN payment_cards ON payments.card_id_of_receiver = payment_cards.id "
            + "INNER JOIN clients ON payment_cards.client_id = clients.id "
            + "WHERE payments.is_deleted = false AND clients.id = :"
            + ClientDAO.PARAMETER_NAME_OF_CLIENT_ID_TO_FIND_ALL_RECEIVED_PAYMENTS;
    private static final String PARAMETER_NAME_OF_CLIENT_ID_TO_FIND_ALL_RECEIVED_PAYMENTS = "client_id";

    public final Collection<Payment> findAllSentPaymentsOfClient(final Client client)   //TODO: test the method
            throws DAOException
    {
        final SessionFactory sessionFactory = super.getSessionFactory();
        try(final Session session = sessionFactory.getCurrentSession())
        {
            final Transaction transaction = session.beginTransaction();
            final Query<Payment> queryToFindAllSentPaymentsOfClient = session.createNativeQuery(
                    ClientDAO.SQL_QUERY_TO_FIND_ALL_SENT_PAYMENTS_OF_CLIENT, Payment.class);
            queryToFindAllSentPaymentsOfClient.setParameter(
                    ClientDAO.PARAMETER_NAME_OF_CLIENT_ID_TO_FIND_ALL_SENT_PAYMENTS, client.getId());
            final Collection<Payment> allSentPaymentsOfClient = queryToFindAllSentPaymentsOfClient.getResultList();
            transaction.commit();
            return allSentPaymentsOfClient;
        }
        catch(final PersistenceException cause)
        {
            throw new DAOException(cause);
        }
    }

    private static final String SQL_QUERY_TO_FIND_ALL_SENT_PAYMENTS_OF_CLIENT
            = "SELECT payments.id, payments.card_id_of_sender, payments.card_id_of_receiver, payments.money, "
            + "payments.date, payments.is_deleted "
            + "FROM payments "
            + "INNER JOIN payment_cards ON payments.card_id_of_sender = payment_cards.id "
            + "INNER JOIN clients ON payment_cards.client_id = clients.id "
            + "WHERE payments.is_deleted = false AND clients.id = :"
            + ClientDAO.PARAMETER_NAME_OF_CLIENT_ID_TO_FIND_ALL_SENT_PAYMENTS;;
    private static final String PARAMETER_NAME_OF_CLIENT_ID_TO_FIND_ALL_SENT_PAYMENTS = "client_id";

    /*public final Map<PaymentCard, BigDecimal> findPaymentCardToTotalReceivedMoneyOfClient(final Client client)
            throws DAOException
    {
        final SessionFactory sessionFactory = super.getSessionFactory();
        try(final Session session = sessionFactory.getCurrentSession())
        {
            final Transaction transaction = session.beginTransaction();
            final Query<?> queryToFindPaymentCardToTotalReceivedMoneyOfClient = session.createQuery(
                    ClientDAO.HQL_QUERY_TO_FIND_PAYMENT_CARD_TO_TOTAL_RECEIVED_MONEY);
            transaction.commit();
        }
        catch(final PersistenceException cause)
        {
            throw new DAOException(cause);
        }
    }

    private static final String HQL_QUERY_TO_FIND_PAYMENT_CARD_TO_TOTAL_RECEIVED_MONEY = "";*/

    private static final class ClientStoredProcedureQueryConfigurer
    {
        private final Cryptographer<String, String> stringCryptographer;

        public ClientStoredProcedureQueryConfigurer()
        {
            super();
            this.stringCryptographer = StringToStringCryptographer.createStringToStringCryptographer();
        }

        public final StoredProcedureQuery configureStoredProcedureQueryToUpdateClient(final Session session,
                                                                                      final Client updatedClient)
        {
            final StoredProcedureQuery storedProcedureQuery = session.createStoredProcedureQuery(
                    NAME_OF_STORED_PROCEDURE_TO_UPDATE_CLIENT);

            storedProcedureQuery.registerStoredProcedureParameter(
                    NAME_OF_PARAMETER_OF_CLIENT_ID_OF_STORED_PROCEDURE_TO_UPDATE_CLIENT, Long.class, ParameterMode.IN);
            storedProcedureQuery.registerStoredProcedureParameter(
                    NAME_OF_PARAMETER_OF_EMAIL_OF_STORED_PROCEDURE_TO_UPDATE_CLIENT, String.class, ParameterMode.IN);
            storedProcedureQuery.registerStoredProcedureParameter(
                    NAME_OF_PARAMETER_OF_ENCRYPTED_PASSWORD_OF_STORED_PROCEDURE_TO_UPDATE_CLIENT, String.class,
                    ParameterMode.IN);
            storedProcedureQuery.registerStoredProcedureParameter(
                    NAME_OF_PARAMETER_OF_IS_DELETED_OF_STORED_PROCEDURE_TO_UPDATE_CLIENT, Boolean.class,
                    ParameterMode.IN);
            storedProcedureQuery.registerStoredProcedureParameter(
                    NAME_OF_PARAMETER_OF_NAME_OF_STORED_PROCEDURE_TO_UPDATE_CLIENT, String.class, ParameterMode.IN);
            storedProcedureQuery.registerStoredProcedureParameter(
                    NAME_OF_PARAMETER_OF_SURNAME_OF_STORED_PROCEDURE_TO_UPDATE_CLIENT, String.class, ParameterMode.IN);
            storedProcedureQuery.registerStoredProcedureParameter(
                    NAME_OF_PARAMETER_OF_PATRONYMIC_OF_STORED_PROCEDURE_TO_UPDATE_CLIENT, String.class,
                    ParameterMode.IN);
            storedProcedureQuery.registerStoredProcedureParameter(
                    NAME_OF_PARAMETER_OF_PHONE_NUMBER_OF_STORED_PROCEDURE_TO_UPDATE_CLIENT, String.class,
                    ParameterMode.IN);
            storedProcedureQuery.registerStoredProcedureParameter(
                    NAME_OF_PARAMETER_OF_BANK_ACCOUNT_ID_OF_STORED_PROCEDURE_TO_UPDATE_CLIENT, Long.class,
                    ParameterMode.IN);

            storedProcedureQuery.setParameter(NAME_OF_PARAMETER_OF_CLIENT_ID_OF_STORED_PROCEDURE_TO_UPDATE_CLIENT,
                    updatedClient.getId());
            storedProcedureQuery.setParameter(NAME_OF_PARAMETER_OF_EMAIL_OF_STORED_PROCEDURE_TO_UPDATE_CLIENT,
                    updatedClient.getEmail());

            final String encryptedPasswordOfUpdatedClient = this.stringCryptographer.encrypt(
                    updatedClient.getPassword());
            storedProcedureQuery.setParameter(
                    NAME_OF_PARAMETER_OF_ENCRYPTED_PASSWORD_OF_STORED_PROCEDURE_TO_UPDATE_CLIENT,
                    encryptedPasswordOfUpdatedClient);

            storedProcedureQuery.setParameter(NAME_OF_PARAMETER_OF_IS_DELETED_OF_STORED_PROCEDURE_TO_UPDATE_CLIENT,
                    updatedClient.isDeleted());
            storedProcedureQuery.setParameter(NAME_OF_PARAMETER_OF_NAME_OF_STORED_PROCEDURE_TO_UPDATE_CLIENT,
                    updatedClient.getName());
            storedProcedureQuery.setParameter(NAME_OF_PARAMETER_OF_SURNAME_OF_STORED_PROCEDURE_TO_UPDATE_CLIENT,
                    updatedClient.getSurname());
            storedProcedureQuery.setParameter(NAME_OF_PARAMETER_OF_PATRONYMIC_OF_STORED_PROCEDURE_TO_UPDATE_CLIENT,
                    updatedClient.getPatronymic());
            storedProcedureQuery.setParameter(NAME_OF_PARAMETER_OF_PHONE_NUMBER_OF_STORED_PROCEDURE_TO_UPDATE_CLIENT,
                    updatedClient.getPhoneNumber());
            storedProcedureQuery.setParameter(NAME_OF_PARAMETER_OF_BANK_ACCOUNT_ID_OF_STORED_PROCEDURE_TO_UPDATE_CLIENT,
                    updatedClient.getBankAccount().getId());

            return storedProcedureQuery;
        }

        private static final String NAME_OF_STORED_PROCEDURE_TO_UPDATE_CLIENT = "update_client";
        private static final String NAME_OF_PARAMETER_OF_CLIENT_ID_OF_STORED_PROCEDURE_TO_UPDATE_CLIENT
                = "id_of_updated_client";
        private static final String NAME_OF_PARAMETER_OF_EMAIL_OF_STORED_PROCEDURE_TO_UPDATE_CLIENT = "new_email";
        private static final String NAME_OF_PARAMETER_OF_ENCRYPTED_PASSWORD_OF_STORED_PROCEDURE_TO_UPDATE_CLIENT
                = "new_encrypted_password";
        private static final String NAME_OF_PARAMETER_OF_IS_DELETED_OF_STORED_PROCEDURE_TO_UPDATE_CLIENT
                = "new_is_deleted";
        private static final String NAME_OF_PARAMETER_OF_NAME_OF_STORED_PROCEDURE_TO_UPDATE_CLIENT = "new_name";
        private static final String NAME_OF_PARAMETER_OF_SURNAME_OF_STORED_PROCEDURE_TO_UPDATE_CLIENT = "new_surname";
        private static final String NAME_OF_PARAMETER_OF_PATRONYMIC_OF_STORED_PROCEDURE_TO_UPDATE_CLIENT
                = "new_patronymic";
        private static final String NAME_OF_PARAMETER_OF_PHONE_NUMBER_OF_STORED_PROCEDURE_TO_UPDATE_CLIENT
                = "new_phone_number";
        private static final String NAME_OF_PARAMETER_OF_BANK_ACCOUNT_ID_OF_STORED_PROCEDURE_TO_UPDATE_CLIENT
                = "new_bank_account_id";

        public final StoredProcedureQuery configureStoredProcedureQueryToUpdateDeletedStatusOfClient(
                final Session session, final long idOfUpdatedClient, final boolean newDeleted)
        {
            final StoredProcedureQuery storedProcedureQuery = session.createStoredProcedureQuery(
                    NAME_OF_STORED_PROCEDURE_TO_UPDATE_DELETED_STATUS_OF_CLIENT);

            storedProcedureQuery.registerStoredProcedureParameter(
                    NAME_OF_PARAMETER_OF_CLIENT_ID_OF_STORED_PROCEDURE_TO_UPDATE_DELETED_STATUS_OF_CLIENT,
                    Long.class, ParameterMode.IN);
            storedProcedureQuery.registerStoredProcedureParameter(
                    NAME_OF_PARAMETER_OF_IS_DELETED_OF_STORED_PROCEDURE_TO_UPDATE_DELETED_STATUS_OF_CLIENT,
                    Boolean.class, ParameterMode.IN);

            storedProcedureQuery.setParameter(
                    NAME_OF_PARAMETER_OF_CLIENT_ID_OF_STORED_PROCEDURE_TO_UPDATE_DELETED_STATUS_OF_CLIENT,
                    idOfUpdatedClient);
            storedProcedureQuery.setParameter(
                    NAME_OF_PARAMETER_OF_IS_DELETED_OF_STORED_PROCEDURE_TO_UPDATE_DELETED_STATUS_OF_CLIENT, newDeleted);

            return storedProcedureQuery;
        }

        private static final String NAME_OF_STORED_PROCEDURE_TO_UPDATE_DELETED_STATUS_OF_CLIENT =
                "update_deleted_status_of_client";
        private static final String NAME_OF_PARAMETER_OF_CLIENT_ID_OF_STORED_PROCEDURE_TO_UPDATE_DELETED_STATUS_OF_CLIENT
                = "new_email";
        private static final String NAME_OF_PARAMETER_OF_IS_DELETED_OF_STORED_PROCEDURE_TO_UPDATE_DELETED_STATUS_OF_CLIENT
                = "new_is_deleted";
    }
}
