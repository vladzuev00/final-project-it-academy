package by.itacademy.zuevvlad.cardpaymentproject.service.user;

import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.DAOException;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.DefiningExistingEntityException;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.FindingEntityException;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.UpdatingEntityException;
import by.itacademy.zuevvlad.cardpaymentproject.dao.user.ClientDAO;
import by.itacademy.zuevvlad.cardpaymentproject.dto.ClientDTO;
import by.itacademy.zuevvlad.cardpaymentproject.dto.ClientPersonalDataDTO;
import by.itacademy.zuevvlad.cardpaymentproject.dto.factory.DTOFactory;
import by.itacademy.zuevvlad.cardpaymentproject.dto.modifier.EntityModifierByDTO;
import by.itacademy.zuevvlad.cardpaymentproject.dto.modifier.exception.UpdatingEntityByDTOException;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Client;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Payment;
import by.itacademy.zuevvlad.cardpaymentproject.entity.PaymentCard;
import by.itacademy.zuevvlad.cardpaymentproject.service.sortingkey.client.ClientSortingKey;
import by.itacademy.zuevvlad.cardpaymentproject.service.statisticsholder.ClientStatisticsHolder;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

public final class ClientService extends AbstractUserService<Client>
{
    private final DTOFactory<Client, ClientPersonalDataDTO> clientPersonalDataDTOFactory;
    private final DTOFactory<Client, ClientDTO> clientDTOFactory;
    private final EntityModifierByDTO<Client, ClientPersonalDataDTO> clientModifierByPersonalData;
    private final EntityModifierByDTO<Client, ClientDTO> clientModifierByDTO;

    public ClientService(final ClientDAO clientDAO,
                         final DTOFactory<Client, ClientPersonalDataDTO> clientPersonalDataDTOFactory,
                         final DTOFactory<Client, ClientDTO> clientDTOFactory,
                         final EntityModifierByDTO<Client, ClientPersonalDataDTO> clientModifierByPersonalData,
                         final EntityModifierByDTO<Client, ClientDTO> clientModifierByDTO)
    {
        super(clientDAO);
        this.clientPersonalDataDTOFactory = clientPersonalDataDTOFactory;
        this.clientDTOFactory = clientDTOFactory;
        this.clientModifierByPersonalData = clientModifierByPersonalData;
        this.clientModifierByDTO = clientModifierByDTO;
    }

    public final Collection<Client> sortClients(final Collection<Client> sortedClients,
                                                final ClientSortingKey clientSortingKey)
    {
        final Comparator<Client> clientComparator = clientSortingKey.getClientComparator();
        return sortedClients.stream().sorted(clientComparator).collect(Collectors.toList());
    }

    public final boolean isNotDeletedClientWithGivenPhoneNumberExist(final String phoneNumber)
            throws DefiningExistingEntityException
    {
        final ClientDAO clientDAO = (ClientDAO)super.getDao();
        return clientDAO.isNotDeletedClientWithGivenPhoneNumberExist(phoneNumber);
    }

    public final Optional<Client> findOptionalOfNotDeletedClientByPhoneNumber(final String phoneNumber)
            throws FindingEntityException
    {
        final ClientDAO clientDAO = (ClientDAO)super.getDao();
        return clientDAO.findOptionalOfNotDeletedClientByPhoneNumber(phoneNumber);
    }

    public final Collection<PaymentCard> findNotDeletedPaymentCardsOfGivenClient(final Client client)
    {
        final Set<PaymentCard> paymentCardsOfClient = client.getPaymentCards();
        final Predicate<PaymentCard> predicateToBeNotDeletedPaymentCard = (final PaymentCard researchPaymentCard) -> {
            return !researchPaymentCard.isDeleted();
        };
        return paymentCardsOfClient.stream().filter(predicateToBeNotDeletedPaymentCard).collect(Collectors.toSet());
    }

    public final ClientPersonalDataDTO createClientPersonalDTO(final Client client)
    {
        return this.clientPersonalDataDTOFactory.createDTO(client);
    }

    public final void updateClientByPersonalData(final Client updatedClient,
                                                 final ClientPersonalDataDTO clientPersonalDataDTO)
            throws UpdatingEntityException, UpdatingEntityByDTOException
    {
        this.clientModifierByPersonalData.updateEntityByDTO(updatedClient, clientPersonalDataDTO);
        final ClientDAO clientDAO = (ClientDAO)super.getDao();
        clientDAO.updatePersonalDataOfClient(updatedClient);
    }

    public final ClientStatisticsHolder collectStatisticsOfClient(final Client client)
            throws DAOException
    {
        final ClientStaticsCollector clientStaticsCollector = this.new ClientStaticsCollector(client);
        return clientStaticsCollector.initialize()
                .collectTotalReceivedMoney()
                .collectTotalSentMoney()
                .collectAverageReceivedMoney()
                .collectAverageSentMoney()
                .collectMapPaymentCardOfClientToTotalReceivedMoney()
                .collectMapPaymentCardOfClientToTotalSentMoney()
                .collectMapPaymentCardOfClientToAverageReceivedMoney()
                .collectMapPaymentCardOfClientToAverageSentMoney()
                .collectMapSenderToTotalReceivedMoney()
                .collectMapReceiverToTotalSentMoney()
                .collectMapSenderToAverageReceivedMoney()
                .collectMapReceiverToAverageSentMoney()
                .finishCollect();
    }

    public final ClientDTO transformToClientDTO(final Client transformedClient)
    {
        return this.clientDTOFactory.createDTO(transformedClient);
    }

    public final Collection<ClientDTO> transformToClientsDTO(final Collection<Client> transformedClients)
    {
        return transformedClients.stream().map(this.clientDTOFactory::createDTO).collect(Collectors.toSet());
    }

    public final void modifyClientByDTO(final Client modifiedClient, final ClientDTO clientDTO)
            throws UpdatingEntityByDTOException
    {
        this.clientModifierByDTO.updateEntityByDTO(modifiedClient, clientDTO);
    }

    private final class ClientStaticsCollector
    {
        private final ClientStatisticsHolder clientStatisticsHolder;
        private final Client client;
        private Collection<Payment> allReceivedPaymentsOfClient;
        private Collection<Payment> allSentPaymentsOfClient;
        private ToDoubleFunction<Payment> functionOfPaymentToMoney;
        private Function<Payment, Client> functionOfPaymentToReceiver;
        private Function<Payment, Client> functionOfPaymentToSender;

        public ClientStaticsCollector(final Client client)
        {
            super();
            this.clientStatisticsHolder = new ClientStatisticsHolder();
            this.client = client;
            this.allReceivedPaymentsOfClient = null;
            this.allSentPaymentsOfClient = null;
            this.functionOfPaymentToMoney = null;
            this.functionOfPaymentToReceiver = null;
            this.functionOfPaymentToSender = null;
        }

        public final ClientStaticsCollector initialize()
                throws DAOException
        {
            final ClientDAO clientDAO = (ClientDAO)ClientService.super.getDao();
            this.allReceivedPaymentsOfClient = clientDAO.findAllReceivedPaymentsOfClient(this.client);
            this.allSentPaymentsOfClient = clientDAO.findAllSentPaymentsOfClient(this.client);

            this.functionOfPaymentToMoney = (final Payment payment) ->
            {
                final BigDecimal moneyOfPayment = payment.getMoney();
                return moneyOfPayment.doubleValue();
            };
            this.functionOfPaymentToReceiver = (final Payment payment) ->
            {
                final PaymentCard paymentCardOfReceiver = payment.getCardOfReceiver();
                return paymentCardOfReceiver.getClient();
            };
            this.functionOfPaymentToSender = (final Payment payment) ->
            {
                final PaymentCard paymentCardOfSender = payment.getCardOfSender();
                return paymentCardOfSender.getClient();
            };

            return this;
        }

        public final ClientStaticsCollector collectTotalReceivedMoney()
        {
            final double totalReceivedMoney = this.allReceivedPaymentsOfClient.stream().mapToDouble(
                    this.functionOfPaymentToMoney).sum();
            this.clientStatisticsHolder.setTotalReceivedMoney(totalReceivedMoney);
            return this;
        }

        public final ClientStaticsCollector collectTotalSentMoney()
        {
            final double totalSentMoney = this.allSentPaymentsOfClient.stream().mapToDouble(
                    this.functionOfPaymentToMoney).sum();
            this.clientStatisticsHolder.setTotalSentMoney(totalSentMoney);
            return this;
        }

        public final ClientStaticsCollector collectAverageReceivedMoney()
        {
            final OptionalDouble optionalOfAverageReceivedMoney = this.allReceivedPaymentsOfClient.stream()
                    .mapToDouble(this.functionOfPaymentToMoney).average();
            final double averageReceivedMoney = optionalOfAverageReceivedMoney.isPresent()
                    ? optionalOfAverageReceivedMoney.getAsDouble()
                    : ClientStaticsCollector.DEFAULT_VALUE_OF_AVERAGE_RECEIVED_MONEY;
            this.clientStatisticsHolder.setAverageReceivedMoney(averageReceivedMoney);
            return this;
        }

        private static final double DEFAULT_VALUE_OF_AVERAGE_RECEIVED_MONEY = 0;

        public final ClientStaticsCollector collectAverageSentMoney()
        {
            final OptionalDouble optionalOfAverageSentMoney = this.allSentPaymentsOfClient.stream().mapToDouble(
                    this.functionOfPaymentToMoney).average();
            final double averageSentMoney = optionalOfAverageSentMoney.isPresent()
                    ? optionalOfAverageSentMoney.getAsDouble()
                    : ClientStaticsCollector.DEFAULT_VALUE_OF_AVERAGE_SENT_MONEY;
            this.clientStatisticsHolder.setAverageSentMoney(averageSentMoney);
            return this;
        }

        private static final double DEFAULT_VALUE_OF_AVERAGE_SENT_MONEY = 0;

        public final ClientStaticsCollector collectMapPaymentCardOfClientToTotalReceivedMoney()
        {
            final Map<PaymentCard, Double> paymentCardOfClientToTotalReceivedMoney
                    = this.allReceivedPaymentsOfClient.stream().collect(
                            Collectors.groupingBy(Payment::getCardOfReceiver,
                                    Collectors.summingDouble(this.functionOfPaymentToMoney)));
            this.clientStatisticsHolder.setPaymentCardOfClientToTotalReceivedMoney(
                    paymentCardOfClientToTotalReceivedMoney);
            return this;
        }

        public final ClientStaticsCollector collectMapPaymentCardOfClientToTotalSentMoney()
        {
            final Map<PaymentCard, Double> paymentCardOfClientToTotalSentMoney
                    = this.allSentPaymentsOfClient.stream().collect(
                            Collectors.groupingBy(Payment::getCardOfSender,
                                    Collectors.summingDouble(this.functionOfPaymentToMoney)));
            this.clientStatisticsHolder.setPaymentCardOfClientToTotalSentMoney(
                    paymentCardOfClientToTotalSentMoney);
            return this;
        }

        public final ClientStaticsCollector collectMapPaymentCardOfClientToAverageReceivedMoney()
        {
            final Map<PaymentCard, Double> paymentCardOfClientToAverageReceivedMoney
                    = this.allReceivedPaymentsOfClient.stream().collect(
                            Collectors.groupingBy(Payment::getCardOfReceiver,
                                    Collectors.averagingDouble(this.functionOfPaymentToMoney)));
            this.clientStatisticsHolder.setPaymentCardOfClientToAverageReceivedMoney(
                    paymentCardOfClientToAverageReceivedMoney);
            return this;
        }

        public final ClientStaticsCollector collectMapPaymentCardOfClientToAverageSentMoney()
        {
            final Map<PaymentCard, Double> paymentCardOfClientToAverageSentMoney
                    = this.allSentPaymentsOfClient.stream().collect(
                            Collectors.groupingBy(Payment::getCardOfSender,
                                    Collectors.averagingDouble(this.functionOfPaymentToMoney)));
            this.clientStatisticsHolder.setPaymentCardOfClientToAverageSentMoney(
                    paymentCardOfClientToAverageSentMoney);
            return this;
        }

        public final ClientStaticsCollector collectMapSenderToTotalReceivedMoney()
        {
            final Map<Client, Double> senderToTotalReceivedMoney
                    = this.allReceivedPaymentsOfClient.stream().collect(
                            Collectors.groupingBy(this.functionOfPaymentToSender,
                                    Collectors.summingDouble(this.functionOfPaymentToMoney)));
            this.clientStatisticsHolder.setSenderToTotalReceivedMoney(senderToTotalReceivedMoney);
            return this;
        }

        public final ClientStaticsCollector collectMapReceiverToTotalSentMoney()
        {
            final Map<Client, Double> receiverToTotalSentMoney
                    = this.allSentPaymentsOfClient.stream().collect(
                            Collectors.groupingBy(this.functionOfPaymentToReceiver,
                                    Collectors.summingDouble(this.functionOfPaymentToMoney)));
            this.clientStatisticsHolder.setReceiverToTotalSentMoney(receiverToTotalSentMoney);
            return this;
        }

        public final ClientStaticsCollector collectMapSenderToAverageReceivedMoney()
        {
            final Map<Client, Double> senderToAverageReceivedMoney
                    = this.allReceivedPaymentsOfClient.stream().collect(
                            Collectors.groupingBy(this.functionOfPaymentToSender,
                                    Collectors.averagingDouble(this.functionOfPaymentToMoney)));
            this.clientStatisticsHolder.setSenderToAverageReceivedMoney(senderToAverageReceivedMoney);
            return this;
        }

        public final ClientStaticsCollector collectMapReceiverToAverageSentMoney()
        {
            final Map<Client, Double> receiverToAverageSentMoney
                    = this.allSentPaymentsOfClient.stream().collect(
                            Collectors.groupingBy(this.functionOfPaymentToReceiver,
                                    Collectors.averagingDouble(this.functionOfPaymentToMoney)));
            this.clientStatisticsHolder.setReceiverToAverageSentMoney(receiverToAverageSentMoney);
            return this;
        }

        public final ClientStatisticsHolder finishCollect()
        {
            return this.clientStatisticsHolder;
        }
    }
}
