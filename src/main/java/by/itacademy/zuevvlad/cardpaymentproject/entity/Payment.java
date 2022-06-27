package by.itacademy.zuevvlad.cardpaymentproject.entity;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Objects;

@javax.persistence.Entity
@Table(name = "payments")
@Access(value = AccessType.PROPERTY)
public final class Payment extends Entity
{
    private PaymentCard cardOfSender;
    private PaymentCard cardOfReceiver;

    @NotNull(message = "is required")
    @DecimalMin(value = "0", message = "should be more or equal 0")
    @DecimalMax(value = "9999999999999999999", message = "should be less than 9999999999999999999")
    private BigDecimal money;

    private Calendar date;        //TODO: replace on LocalDateTime

    public Payment()
    {
        super();
        this.cardOfSender = new PaymentCard();
        this.cardOfReceiver = new PaymentCard();
        this.money = Payment.VALUE_OF_NOT_DEFINED_MONEY;
        this.date = Payment.createDefaultDate();
    }

    private static final BigDecimal VALUE_OF_NOT_DEFINED_MONEY = BigDecimal.ZERO;

    private static Calendar createDefaultDate()
    {
        final Calendar date = Calendar.getInstance();
        date.set(Payment.YEAR_OF_NOT_DEFINED_DATE, Payment.MONTH_OF_NOT_DEFINED_DATE, Payment.DAY_OF_NOT_DEFINED_DATE);
        return date;
    }

    private static final int YEAR_OF_NOT_DEFINED_DATE = 1970;
    private static final int MONTH_OF_NOT_DEFINED_DATE = Calendar.JANUARY;
    private static final int DAY_OF_NOT_DEFINED_DATE = 1;

    public Payment(final long id)
    {
        super(id);
        this.cardOfSender = new PaymentCard();
        this.cardOfReceiver = new PaymentCard();
        this.money = Payment.VALUE_OF_NOT_DEFINED_MONEY;
        this.date = Payment.createDefaultDate();
    }

    public Payment(final PaymentCard cardOfSender, final PaymentCard cardOfReceiver, final BigDecimal money,
                   final Calendar date)
    {
        super();
        this.cardOfSender = cardOfSender;
        this.cardOfReceiver = cardOfReceiver;
        this.money = money;
        this.date = date;
    }

    public Payment(final long id, final PaymentCard cardOfSender, final PaymentCard cardOfReceiver,
                   final BigDecimal money, final Calendar date)
    {
        super(id);
        this.cardOfSender = cardOfSender;
        this.cardOfReceiver = cardOfReceiver;
        this.money = money;
        this.date = date;
    }

    public final void setCardOfSender(final PaymentCard cardOfSender)
    {
        this.cardOfSender = cardOfSender;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "card_id_of_sender")
    public final PaymentCard getCardOfSender()
    {
        return this.cardOfSender;
    }

    public final void setCardOfReceiver(final PaymentCard cardOfReceiver)
    {
        this.cardOfReceiver = cardOfReceiver;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "card_id_of_receiver")
    public final PaymentCard getCardOfReceiver()
    {
        return this.cardOfReceiver;
    }

    public final void setMoney(final BigDecimal money)
    {
        this.money = money;
    }

    @Column(name = "money")
    public final BigDecimal getMoney()
    {
        return this.money;
    }

    public final void setDate(final Calendar date)
    {
        this.date = date;
    }

    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    public final Calendar getDate()
    {
        return this.date;
    }

    @Override
    public final boolean equals(final Object otherObject)
    {
        if(!super.equals(otherObject))
        {
            return false;
        }
        final Payment other = (Payment)otherObject;
        return     Objects.equals(this.cardOfSender, other.cardOfSender)
                && Objects.equals(this.cardOfReceiver, other.cardOfReceiver)
                && Objects.equals(this.money, other.money)
                && Objects.equals(this.date, other.date);
    }

    @Override
    public final int hashCode()
    {
        return super.hashCode() + Objects.hash(this.cardOfSender, this.cardOfReceiver, this.money, this.date);
    }

    @Override
    public final String toString()
    {
        return super.toString() + "[cardOfSender = " + this.cardOfSender + ", cardOfReceiver = " + this.cardOfReceiver
                + ", money = " + this.money + ", date = " + this.date + "]";
    }

    @Override
    public final void writeExternal(final ObjectOutput objectOutput)
            throws IOException
    {
        super.writeExternal(objectOutput);

        objectOutput.writeObject(this.cardOfSender);
        objectOutput.writeObject(this.cardOfReceiver);
        objectOutput.writeObject(this.money);
        objectOutput.writeObject(this.date);
    }

    @Override
    public final void readExternal(final ObjectInput objectInput)
            throws IOException, ClassNotFoundException
    {
        super.readExternal(objectInput);

        this.cardOfSender = (PaymentCard)objectInput.readObject();
        this.cardOfReceiver = (PaymentCard)objectInput.readObject();
        this.money = (BigDecimal)objectInput.readObject();
        this.date = (Calendar)objectInput.readObject();
    }
}
