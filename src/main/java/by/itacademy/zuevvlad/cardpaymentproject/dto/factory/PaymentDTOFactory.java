package by.itacademy.zuevvlad.cardpaymentproject.dto.factory;

import by.itacademy.zuevvlad.cardpaymentproject.dto.PaymentDTO;
import by.itacademy.zuevvlad.cardpaymentproject.entity.Payment;
import by.itacademy.zuevvlad.cardpaymentproject.service.calendarhandler.CalendarHandler;

import java.math.BigDecimal;
import java.util.Calendar;

public final class PaymentDTOFactory implements DTOFactory<Payment, PaymentDTO>
{
    private final CalendarHandler calendarHandler;

    public PaymentDTOFactory(final CalendarHandler calendarHandler)
    {
        super();
        this.calendarHandler = calendarHandler;
    }

    @Override
    public final PaymentDTO createDTO(final Payment payment)
    {
        final long idOfPayment = payment.getId();
        final boolean deletedOfPayment = payment.isDeleted();
        final BigDecimal moneyOfPayment = payment.getMoney();

        final Calendar dateOfPayment = payment.getDate();
        final String descriptionOfDateOfPayment = this.calendarHandler.findDescriptionOfCalendar(dateOfPayment);

        return new PaymentDTO(idOfPayment, deletedOfPayment, moneyOfPayment, descriptionOfDateOfPayment);
    }
}
