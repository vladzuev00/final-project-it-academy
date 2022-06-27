package by.itacademy.zuevvlad.cardpaymentproject.dto.modifier;

import by.itacademy.zuevvlad.cardpaymentproject.dto.PaymentCardDTO;
import by.itacademy.zuevvlad.cardpaymentproject.entity.PaymentCard;

public final class PaymentCardModifierByDTO implements EntityModifierByDTO<PaymentCard, PaymentCardDTO>
{
    public PaymentCardModifierByDTO()
    {
        super();
    }

    @Override
    public final void updateEntityByDTO(final PaymentCard updatedPaymentCard, final PaymentCardDTO paymentCardDTO)
    {
        final long newId = paymentCardDTO.getId();
        updatedPaymentCard.setId(newId);

        final boolean newDeleted = paymentCardDTO.isDeleted();
        updatedPaymentCard.setDeleted(newDeleted);

        final String newCardNumber = paymentCardDTO.getCardNumber();
        updatedPaymentCard.setCardNumber(newCardNumber);

        final PaymentCard.ExpirationDate newExpirationDate = paymentCardDTO.getExpirationDate();
        updatedPaymentCard.setExpirationDate(newExpirationDate);

        final String newPaymentSystem = paymentCardDTO.getPaymentSystem();
        updatedPaymentCard.setPaymentSystem(newPaymentSystem);

        final String newCvc = paymentCardDTO.getCvc();
        updatedPaymentCard.setCvc(newCvc);

        final String newNameOfBank = paymentCardDTO.getNameOfBank();
        updatedPaymentCard.setNameOfBank(newNameOfBank);

        final String newPassword = paymentCardDTO.getPassword();
        updatedPaymentCard.setPassword(newPassword);
    }
}
