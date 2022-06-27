package by.itacademy.zuevvlad.cardpaymentproject.dto.factory;

import by.itacademy.zuevvlad.cardpaymentproject.dto.PaymentCardDTO;
import by.itacademy.zuevvlad.cardpaymentproject.entity.PaymentCard;

public final class PaymentCardDTOFactory implements DTOFactory<PaymentCard, PaymentCardDTO>
{
    public PaymentCardDTOFactory()
    {
        super();
    }

    @Override
    public final PaymentCardDTO createDTO(final PaymentCard paymentCard)
    {
        final long idOfPaymentCard = paymentCard.getId();
        final boolean deletedOfPaymentCard = paymentCard.isDeleted();
        final String cardNumberOfPaymentCard = paymentCard.getCardNumber();
        final PaymentCard.ExpirationDate expirationDateOfPaymentCard = paymentCard.getExpirationDate();
        final String paymentSystemOfPaymentCard = paymentCard.getPaymentSystem();
        final String cvcOfPaymentCard = paymentCard.getCvc();
        final String nameOfBankOfPaymentCard = paymentCard.getNameOfBank();
        final String passwordOfPaymentCard = paymentCard.getPassword();
        return new PaymentCardDTO(idOfPaymentCard, deletedOfPaymentCard, cardNumberOfPaymentCard,
                expirationDateOfPaymentCard, paymentSystemOfPaymentCard, cvcOfPaymentCard, nameOfBankOfPaymentCard,
                passwordOfPaymentCard);
    }
}
