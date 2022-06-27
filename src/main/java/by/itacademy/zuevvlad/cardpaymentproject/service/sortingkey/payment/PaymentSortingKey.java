package by.itacademy.zuevvlad.cardpaymentproject.service.sortingkey.payment;

import by.itacademy.zuevvlad.cardpaymentproject.entity.Payment;
import by.itacademy.zuevvlad.cardpaymentproject.entity.PaymentCard;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Comparator;

public enum PaymentSortingKey
{
    CARD_NUMBER_OF_SENDER((final Payment firstPayment, final Payment secondPayment) ->
    {
        final PaymentCard paymentCardOfSenderOfFirstPayment = firstPayment.getCardOfSender();
        final String cardNumberOfPaymentCardOfSenderOfFirstPayment = paymentCardOfSenderOfFirstPayment.getCardNumber();

        final PaymentCard paymentCardOfSenderOfSecondPayment = secondPayment.getCardOfSender();
        final String cardNumberOfPaymentCardOfSenderOfSecondPayment = paymentCardOfSenderOfSecondPayment
                .getCardNumber();

        return cardNumberOfPaymentCardOfSenderOfFirstPayment.compareTo(cardNumberOfPaymentCardOfSenderOfSecondPayment);
    }),
    CARD_NUMBER_OF_RECEIVER((final Payment firstPayment, final Payment secondPayment) ->
    {
        final PaymentCard paymentCardOfReceiverOfFirstPayment = firstPayment.getCardOfReceiver();
        final String cardNumberOfPaymentCardOfReceiverOfFirstPayment = paymentCardOfReceiverOfFirstPayment
                .getCardNumber();

        final PaymentCard paymentCardOfReceiverOfSecondPayment = secondPayment.getCardOfReceiver();
        final String cardNumberOfPaymentCardOfReceiverOfSecondPayment = paymentCardOfReceiverOfSecondPayment
                .getCardNumber();

        return cardNumberOfPaymentCardOfReceiverOfFirstPayment.compareTo(
                cardNumberOfPaymentCardOfReceiverOfSecondPayment);
    }),
    MONEY((final Payment firstPayment, final Payment secondPayment) ->
    {
        final BigDecimal moneyOfFirstPayment = firstPayment.getMoney();
        final BigDecimal moneyOfSecondPayment = secondPayment.getMoney();
        return moneyOfFirstPayment.compareTo(moneyOfSecondPayment);
    }),
    DATE((final Payment firstPayment, final Payment secondPayment) ->
    {
        final Calendar dateOfFirstPayment = firstPayment.getDate();
        final Calendar dateOfSecondPayment = secondPayment.getDate();
        return dateOfFirstPayment.compareTo(dateOfSecondPayment);
    }),
    DELETED((final Payment firstPayment, final Payment secondPayment) ->
    {
        final boolean deletedOfFirstPayment = firstPayment.isDeleted();
        final boolean deletedOfSecondPayment = secondPayment.isDeleted();
        return Boolean.compare(deletedOfFirstPayment, deletedOfSecondPayment);
    });

    private final Comparator<Payment> paymentComparator;

    private PaymentSortingKey(final Comparator<Payment> paymentComparator)
    {
        this.paymentComparator = paymentComparator;
    }

    public final Comparator<Payment> getPaymentComparator()
    {
        return this.paymentComparator;
    }
}
