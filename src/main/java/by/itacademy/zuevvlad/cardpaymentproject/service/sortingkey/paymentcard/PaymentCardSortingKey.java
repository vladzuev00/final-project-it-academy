package by.itacademy.zuevvlad.cardpaymentproject.service.sortingkey.paymentcard;

import by.itacademy.zuevvlad.cardpaymentproject.entity.Client;
import by.itacademy.zuevvlad.cardpaymentproject.entity.PaymentCard;

import java.util.Comparator;

public enum PaymentCardSortingKey
{
    CARD_NUMBER((final PaymentCard firstPaymentCard, final PaymentCard secondPaymentCard) ->
    {
        final String cardNumberOfFirstPaymentCard = firstPaymentCard.getCardNumber();
        final String cardNumberOfSecondPaymentCard = secondPaymentCard.getCardNumber();
        return cardNumberOfFirstPaymentCard.compareTo(cardNumberOfSecondPaymentCard);
    }),
    EXPIRATION_DATE((final PaymentCard firstPaymentCard, final PaymentCard secondPaymentCard) ->
    {
        final PaymentCard.ExpirationDate expirationDateOfFirstPaymentCard = firstPaymentCard.getExpirationDate();
        final PaymentCard.ExpirationDate expirationDateOfSecondPaymentCard = secondPaymentCard.getExpirationDate();
        final Comparator<PaymentCard.ExpirationDate> expirationDateComparator = Comparator
                .comparingInt(PaymentCard.ExpirationDate::getYear)
                .thenComparing(PaymentCard.ExpirationDate::getMonth);
        return expirationDateComparator.compare(expirationDateOfFirstPaymentCard, expirationDateOfSecondPaymentCard);
    }),
    PAYMENT_SYSTEM((final PaymentCard firstPaymentCard, final PaymentCard secondPaymentCard) ->
    {
        final String paymentSystemOfFirstPaymentCard = firstPaymentCard.getPaymentSystem();
        final String paymentSystemOfSecondPaymentCard = secondPaymentCard.getPaymentSystem();
        return paymentSystemOfFirstPaymentCard.compareTo(paymentSystemOfSecondPaymentCard);
    }),
    CVC((final PaymentCard firstPaymentCard, final PaymentCard secondPaymentCard) ->
    {
        final String cvcOfFirstPaymentCard = firstPaymentCard.getCvc();
        final String cvcOfSecondPaymentCard = secondPaymentCard.getCvc();
        return cvcOfFirstPaymentCard.compareTo(cvcOfSecondPaymentCard);
    }),
    EMAIL_OF_CLIENT((final PaymentCard firstPaymentCard, final PaymentCard secondPaymentCard) ->
    {
        final Client clientOfFirstPaymentCard = firstPaymentCard.getClient();
        final String emailOfClientOfFirstPaymentCard = clientOfFirstPaymentCard.getEmail();

        final Client clientOfSecondPaymentCard = secondPaymentCard.getClient();
        final String emailOfClientOfSecondPaymentCard = clientOfSecondPaymentCard.getEmail();

        return emailOfClientOfFirstPaymentCard.compareTo(emailOfClientOfSecondPaymentCard);
    }),
    PHONE_NUMBER_OF_CLIENT((final PaymentCard firstPaymentCard, final PaymentCard secondPaymentCard) ->
    {
        final Client clientOfFirstPaymentCard = firstPaymentCard.getClient();
        final String phoneNumberOfClientOfFirstPaymentCard = clientOfFirstPaymentCard.getPhoneNumber();

        final Client clientOfSecondPaymentCard = secondPaymentCard.getClient();
        final String phoneNumberOfClientOfSecondPaymentCard = clientOfSecondPaymentCard.getPhoneNumber();

        return phoneNumberOfClientOfFirstPaymentCard.compareTo(phoneNumberOfClientOfSecondPaymentCard);
    }),
    NAME_OF_BANK((final PaymentCard firstPaymentCard, final PaymentCard secondPaymentCard) ->
    {
        final String nameOfBankOfFirstPaymentCard = firstPaymentCard.getNameOfBank();
        final String nameOfBankOfSecondPaymentCard = secondPaymentCard.getNameOfBank();
        return nameOfBankOfFirstPaymentCard.compareTo(nameOfBankOfSecondPaymentCard);
    }),
    PASSWORD((final PaymentCard firstPaymentCard, final PaymentCard secondPaymentCard) ->
    {
        final String passwordOfFirstPaymentCard = firstPaymentCard.getPassword();
        final String passwordOfSecondPaymentCard = secondPaymentCard.getPassword();
        return passwordOfFirstPaymentCard.compareTo(passwordOfSecondPaymentCard);
    }),
    DELETED((final PaymentCard firstPaymentCard, final PaymentCard secondPaymentCard) ->
    {
        final boolean deletedOfFirstPaymentCard = firstPaymentCard.isDeleted();
        final boolean deletedOfSecondPaymentCard = secondPaymentCard.isDeleted();
        return Boolean.compare(deletedOfFirstPaymentCard, deletedOfSecondPaymentCard);
    });

    private final Comparator<PaymentCard> paymentCardComparator;

    private PaymentCardSortingKey(final Comparator<PaymentCard> paymentCardComparator)
    {
        this.paymentCardComparator = paymentCardComparator;
    }

    public final Comparator<PaymentCard> getPaymentCardComparator()
    {
        return this.paymentCardComparator;
    }
}
