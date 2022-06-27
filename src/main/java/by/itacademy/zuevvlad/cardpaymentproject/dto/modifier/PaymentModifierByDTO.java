package by.itacademy.zuevvlad.cardpaymentproject.dto.modifier;

import by.itacademy.zuevvlad.cardpaymentproject.dto.PaymentDTO;
import by.itacademy.zuevvlad.cardpaymentproject.dto.modifier.exception.UpdatingEntityByDTOException;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Payment;
import by.itacademy.zuevvlad.cardpaymentproject.service.calendarhandler.CalendarHandler;
import by.itacademy.zuevvlad.cardpaymentproject.service.calendarhandler.exception.CalendarHandlingException;

import java.math.BigDecimal;
import java.util.Calendar;

public final class PaymentModifierByDTO implements EntityModifierByDTO<Payment, PaymentDTO>
{
    private final CalendarHandler calendarHandler;

    public PaymentModifierByDTO(final CalendarHandler calendarHandler)
    {
        super();
        this.calendarHandler = calendarHandler;
    }

    @Override
    public final void updateEntityByDTO(final Payment updatedPayment, final PaymentDTO paymentDTO)
            throws UpdatingEntityByDTOException
    {
        try
        {
            final long newId = paymentDTO.getId();
            updatedPayment.setId(newId);

            final boolean newDeleted = paymentDTO.isDeleted();
            updatedPayment.setDeleted(newDeleted);

            final BigDecimal newMoney = paymentDTO.getMoney();
            updatedPayment.setMoney(newMoney);

            final String descriptionOfNewDate = paymentDTO.getDescriptionOfDate();    //TODO: провалидировать компоненты времени
            final Calendar newDate = this.calendarHandler.parseDescriptionOfCalendar(descriptionOfNewDate);
            updatedPayment.setDate(newDate);
        }
        catch(final CalendarHandlingException cause)
        {
            throw new UpdatingEntityByDTOException(cause);
        }
    }
}
