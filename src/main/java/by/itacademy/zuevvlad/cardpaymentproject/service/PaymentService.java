package by.itacademy.zuevvlad.cardpaymentproject.service;

import by.itacademy.zuevvlad.cardpaymentproject.dao.PaymentDAO;
import by.itacademy.zuevvlad.cardpaymentproject.dao.exception.OffloadingEntitiesException;
import by.itacademy.zuevvlad.cardpaymentproject.dto.PaymentDTO;
import by.itacademy.zuevvlad.cardpaymentproject.dto.factory.DTOFactory;
import by.itacademy.zuevvlad.cardpaymentproject.dto.modifier.EntityModifierByDTO;
import by.itacademy.zuevvlad.cardpaymentproject.dto.modifier.exception.UpdatingEntityByDTOException;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Client;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Payment;
import by.itacademy.zuevvlad.cardpaymentproject.service.calendarhandler.CalendarHandler;
import by.itacademy.zuevvlad.cardpaymentproject.service.calendarhandler.exception.CalendarHandlingException;
import by.itacademy.zuevvlad.cardpaymentproject.service.sortingkey.payment.PaymentSortingKey;

import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class PaymentService extends Service<Payment>
{
    private final CalendarHandler calendarHandler;
    private final DTOFactory<Payment, PaymentDTO> paymentDTOFactory;
    final EntityModifierByDTO<Payment, PaymentDTO> paymentModifierByDTO;

    public PaymentService(final PaymentDAO paymentDAO, final DTOFactory<Payment, PaymentDTO> paymentDTOFactory,
                          final EntityModifierByDTO<Payment, PaymentDTO> paymentModifierByDTO)
    {
        super(paymentDAO);
        this.paymentDTOFactory = paymentDTOFactory;
        this.paymentModifierByDTO = paymentModifierByDTO;
        this.calendarHandler = CalendarHandler.createCalendarHandler();
    }

    public final Collection<Payment> sortPayments(final Collection<Payment> sortedPayments,
                                                  final PaymentSortingKey paymentSortingKey)
    {
        final Comparator<Payment> paymentComparator = paymentSortingKey.getPaymentComparator();
        return sortedPayments.stream().sorted(paymentComparator).collect(Collectors.toList());
    }

    public final Map<Payment, String> findMapOfPaymentToItsDescriptionOfDate(final Collection<Payment> mappedPayments)
    {
        return mappedPayments.stream().collect(Collectors.toMap(Function.identity(), (final Payment mappedPayment) ->
        {
            final Calendar dateOfMappedPayment = mappedPayment.getDate();
            return PaymentService.this.calendarHandler.findDescriptionOfCalendar(dateOfMappedPayment);
        }));
    }

    public final Calendar parseDescriptionOfDateOfPayment(final String parsedDescription)
            throws CalendarHandlingException
    {
        return this.calendarHandler.parseDescriptionOfCalendar(parsedDescription);
    }

    public final Collection<Payment> findPaymentsByRangeOfDates(final Calendar minimumDateOfFoundPayments,
                                                                final Calendar maximumDateOfFoundPayments)
            throws OffloadingEntitiesException
    {
        final PaymentDAO paymentDAO = (PaymentDAO)super.getDao();
        final Collection<Payment> allPayments = paymentDAO.offloadEntities();
        final Predicate<Payment> predicateToBeFound = (final Payment researchPayment) ->
        {
            final Calendar dateOfResearchPayment = researchPayment.getDate();
            return     dateOfResearchPayment.compareTo(minimumDateOfFoundPayments) >= 0
                    && dateOfResearchPayment.compareTo(maximumDateOfFoundPayments) <= 0;
        };
        return allPayments.parallelStream().filter(predicateToBeFound).collect(Collectors.toList());
    }

    public final Collection<Payment> findNotDeletedPaymentsAssociatedWithGivenClient(final Client client)
            throws OffloadingEntitiesException
    {
        final PaymentDAO paymentDAO = (PaymentDAO)super.getDao();
        return paymentDAO.findNotDeletedPaymentsAssociatedWithGivenClient(client);
    }

    public final Collection<Payment> findNotDeletedPaymentsSentByGivenClient(final Client client)
            throws OffloadingEntitiesException
    {
        final PaymentDAO paymentDAO = (PaymentDAO)super.getDao();
        return paymentDAO.findNotDeletedPaymentsSentByGivenClient(client);
    }

    public final Collection<Payment> findNotDeletedPaymentsReceivedByGivenClient(final Client client)
            throws OffloadingEntitiesException
    {
        final PaymentDAO paymentDAO = (PaymentDAO)super.getDao();
        return paymentDAO.findNotDeletedPaymentsReceivedByGivenClient(client);
    }

    public final Collection<PaymentDTO> transformToPaymentsDTO(final Collection<Payment> transformedPayments)
    {
        return transformedPayments.stream().map(this.paymentDTOFactory::createDTO).collect(Collectors.toSet());
    }

    public final PaymentDTO transformToPaymentDTO(final Payment transformedPayment)
    {
        return this.paymentDTOFactory.createDTO(transformedPayment);
    }

    public final void modifyPaymentByDTO(final Payment modifiedPayment, final PaymentDTO paymentDTO)
            throws UpdatingEntityByDTOException
    {
        this.paymentModifierByDTO.updateEntityByDTO(modifiedPayment, paymentDTO);
    }
}
